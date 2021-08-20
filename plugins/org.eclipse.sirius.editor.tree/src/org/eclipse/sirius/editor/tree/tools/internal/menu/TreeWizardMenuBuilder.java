/*******************************************************************************
 * Copyright (c) 2010, 2015 THALES GLOBAL SERVICES and others.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.editor.tree.tools.internal.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.sirius.common.tools.api.util.StringUtil;
import org.eclipse.sirius.editor.tools.api.menu.AbstractEObjectRefactoringAction;
import org.eclipse.sirius.editor.tools.api.menu.AbstractMenuBuilder;
import org.eclipse.sirius.editor.tools.api.menu.AbstractUndoRecordingCommand;
import org.eclipse.sirius.ext.base.Option;
import org.eclipse.sirius.ext.base.Options;
import org.eclipse.sirius.ext.emf.AllContents;
import org.eclipse.sirius.tree.description.DescriptionFactory;
import org.eclipse.sirius.tree.description.TreeDescription;
import org.eclipse.sirius.tree.description.TreeItemEditionTool;
import org.eclipse.sirius.tree.description.TreeItemMapping;
import org.eclipse.sirius.tree.description.TreeItemMappingContainer;
import org.eclipse.sirius.tree.description.TreeItemStyleDescription;
import org.eclipse.sirius.tree.description.TreePopupMenu;
import org.eclipse.sirius.viewpoint.description.tool.ChangeContext;
import org.eclipse.sirius.viewpoint.description.tool.ContainerViewVariable;
import org.eclipse.sirius.viewpoint.description.tool.CreateInstance;
import org.eclipse.sirius.viewpoint.description.tool.EditMaskVariables;
import org.eclipse.sirius.viewpoint.description.tool.ElementDropVariable;
import org.eclipse.sirius.viewpoint.description.tool.InitialOperation;
import org.eclipse.sirius.viewpoint.description.tool.MenuItemDescriptionReference;
import org.eclipse.sirius.viewpoint.description.tool.OperationAction;
import org.eclipse.sirius.viewpoint.description.tool.SetValue;
import org.eclipse.sirius.viewpoint.description.tool.ToolFactory;
import org.eclipse.ui.IEditorPart;

public class TreeWizardMenuBuilder extends AbstractMenuBuilder {

    @Override
    public String getLabel() {
        return "Initialize";
    }

    @Override
    public int getPriority() {
        return AbstractMenuBuilder.INITIALIZE;
    }

    @Override
    public void update(final Collection newChildDescriptors, final ISelection selection, final IEditorPart editor) {
        depopulate();
        advancedChildActions = generateRefactoringActions(selection, editor);
    }

    private Collection generateRefactoringActions(final ISelection selection, final IEditorPart editor) {
        Set<AbstractEObjectRefactoringAction> allActions = new LinkedHashSet<>();
        allActions.add(new InitializeTreeFromEClass(editor, selection));
        return allActions.stream().filter(AbstractEObjectRefactoringAction::isSelectionValid).collect(Collectors.toList());
    }

    @Override
    protected boolean isMine(CommandParameter object) {
        // not relevant here
        return false;
    }

}

class InitializeTreeFromEClass extends AbstractEObjectRefactoringAction {

    InitializeTreeFromEClass(IEditorPart editor, ISelection selection) {
        super(editor, selection);
    }

    @Override
    protected Command buildActionCommand(EditingDomain arg0, Collection<EObject> selection) {
        Command result = UnexecutableCommand.INSTANCE;
        setSelectionValid(false);
        if (selection.size() == 1) {
            final EObject elementToMove = selection.iterator().next();
            if (isAbleToInitialize(elementToMove)) {
                setSelectionValid(true);
                final TreeDescription treeDesc = (TreeDescription) elementToMove;
                result = new AbstractUndoRecordingCommand(arg0.getResourceSet()) {

                    @Override
                    protected void doExecute() {
                        EClass eClassToStartFrom = lookForEClass(treeDesc.getDomainClass(), treeDesc.getMetamodel());
                        new TreeDescriptionBuilderFromEClass(treeDesc, eClassToStartFrom).fillContent();
                    }

                    @Override
                    protected String getText() {
                        return "Create content from Ecore model";
                    }

                };
            }
        }
        return result;
    }

    private boolean isAbleToInitialize(final EObject elementToMove) {
        if (elementToMove instanceof TreeDescription && !StringUtil.isEmpty(((TreeDescription) elementToMove).getDomainClass())) {
            TreeDescription tree = (TreeDescription) elementToMove;
            if (!tree.getMetamodel().isEmpty()) {
                EClass foundEClass = lookForEClass(tree.getDomainClass(), tree.getMetamodel());
                if (foundEClass != null) {
                    return true;
                } else {
                    setTextIfDisable("Can not find the EClass named :" + tree.getName() + "in the associated metamodels");
                }
            } else {
                setTextIfDisable("Unable to initialize tree content if no EClass is explicitely associated with the Tree");
            }

        }
        return false;
    }

    private EClass lookForEClass(String domainClass, EList<EPackage> metamodel) {
        Iterator<EPackage> itPak = metamodel.iterator();
        while (itPak.hasNext()) {
            EPackage curPack = itPak.next();
            Iterator<EObject> itClaz = AllContents.of(curPack, EcorePackage.Literals.ECLASS).iterator();
            while (itClaz.hasNext()) {
                EObject cur = itClaz.next();
                // TODO handle qualified name too.
                if (cur instanceof EClass && domainClass.equals(((EClass) cur).getName())) {
                    return (EClass) cur;
                }
            }
        }
        return null;
    }

}

class TreeDescriptionBuilderFromEClass {

    private Map<EClass, List<TreeItemMapping>> doneClasses;

    private Map<EClass, List<OperationAction>> doneItems;

    private EClass rootEClass;

    private TreeDescription treeDesc;

    private EClassHierarchy hiearchy;

    TreeDescriptionBuilderFromEClass(TreeDescription treeDesc, EClass eClassToStartFrom) {
        this.rootEClass = eClassToStartFrom;
        this.treeDesc = treeDesc;
        /*
         * handling subclassing
         */

        hiearchy = new EClassHierarchy(treeDesc.eResource().getResourceSet());
    }

    public void fillContent() {
        doneClasses = HashMultimap.create();
        doneItems = HashMultimap.create();
        buildMappingRecursively(treeDesc, rootEClass, "var:self");
    }

    private TreeItemMapping buildMappingRecursively(TreeItemMappingContainer container, EClass eClassToStartFrom, String semanticCandidateExpression2) {

        TreeItemMapping map = DescriptionFactory.eINSTANCE.createTreeItemMapping();
        map.setDomainClass(qualifiedName(eClassToStartFrom));
        map.setName(eClassToStartFrom.getName());
        map.setSemanticCandidatesExpression(semanticCandidateExpression2);
        map.setSemanticElements("var:self");
        TreeItemStyleDescription style = DescriptionFactory.eINSTANCE.createTreeItemStyleDescription();
        map.setDefaultStyle(style);
        style.setLabelExpression("feature:name");
        style.setLabelSize(10);

        Option<EAttribute> editableName = lookForEditableName(eClassToStartFrom);
        if (editableName.some()) {
            handleEditableName(map, editableName);
        }

        doneClasses.put(eClassToStartFrom, map);
        container.getSubItemMappings().add(map);
        /*
         * Popup menu for childs.
         */
        TreePopupMenu menu = DescriptionFactory.eINSTANCE.createTreePopupMenu();
        menu.setName("New");

        for (EReference ownedReferences : Iterables.filter(eClassToStartFrom.getEAllReferences(), (EReference input) -> {
            return input.isContainment() && input.getEType() instanceof EClass;
        })) {
            EClass targetClass = (EClass) ownedReferences.getEType();

            Collection<EClass> concreteSubclasses = Lists.newArrayList(hiearchy.getConcreteSubclassesLeafs(targetClass));

            if (concreteSubclasses.size() > 0) {
                map.getPopupMenus().add(menu);
            }
            for (EClass leaf : concreteSubclasses) {
                String semanticCandidateExpression = "feature:" + ownedReferences.getName();
                TreeItemMapping childMap = getAlreadyExistingMapping(leaf, semanticCandidateExpression);
                if (childMap == null) {
                    childMap = buildMappingRecursively(map, leaf, semanticCandidateExpression);
                } else {
                    map.getReusedTreeItemMappings().add(childMap);
                }

                Option<OperationAction> createChild = getAlreadyExistingMenuItem(leaf, ownedReferences.getName());
                if (createChild.some()) {
                    MenuItemDescriptionReference ref = ToolFactory.eINSTANCE.createMenuItemDescriptionReference();
                    ref.setItem(createChild.get());
                    menu.getMenuItemDescriptions().add(ref);
                } else {
                    OperationAction newMenuItem = ToolFactory.eINSTANCE.createOperationAction();
                    newMenuItem.setName(leaf.getName());

                    ContainerViewVariable var = ToolFactory.eINSTANCE.createContainerViewVariable();
                    var.setName("view");
                    newMenuItem.setView(var);

                    ChangeContext goToElement = ToolFactory.eINSTANCE.createChangeContext();
                    goToElement.setBrowseExpression("feature:target");

                    CreateInstance newChild = ToolFactory.eINSTANCE.createCreateInstance();
                    newChild.setReferenceName(ownedReferences.getName());
                    newChild.setTypeName(qualifiedName(leaf));
                    goToElement.getSubModelOperations().add(newChild);

                    InitialOperation op = ToolFactory.eINSTANCE.createInitialOperation();
                    op.setFirstModelOperations(goToElement);

                    newMenuItem.setInitialOperation(op);
                    newMenuItem.setForceRefresh(true);

                    doneItems.put(leaf, newMenuItem);
                    menu.getMenuItemDescriptions().add(newMenuItem);
                }

            }

        }
        return map;
    }

    protected void handleEditableName(TreeItemMapping map, Option<EAttribute> editableName) {
        EAttribute editableFeature = editableName.get();
        TreeItemEditionTool editTool = DescriptionFactory.eINSTANCE.createTreeItemEditionTool();
        editTool.setName(editableFeature.getName());
        EditMaskVariables maskVar = ToolFactory.eINSTANCE.createEditMaskVariables();
        maskVar.setMask("{0}");
        editTool.setMask(maskVar);

        SetValue setVal = ToolFactory.eINSTANCE.createSetValue();
        setVal.setFeatureName(editableFeature.getName());
        setVal.setValueExpression("var:arg0");

        ElementDropVariable elementVar = ToolFactory.eINSTANCE.createElementDropVariable();
        elementVar.setName("element");
        editTool.setElement(elementVar);

        ElementDropVariable rootVar = ToolFactory.eINSTANCE.createElementDropVariable();
        elementVar.setName("root");
        editTool.setRoot(rootVar);
        editTool.setFirstModelOperation(setVal);
        map.setDirectEdit(editTool);
    }

    private String qualifiedName(EClass eClassToStartFrom) {
        return eClassToStartFrom.getEPackage().getName() + "." + eClassToStartFrom.getName();
    }

    private TreeItemMapping getAlreadyExistingMapping(EClass leaf, String semanticCandidateExpression) {
        TreeItemMapping found = null;
        Iterator<TreeItemMapping> it = doneClasses.get(leaf).iterator();
        while (it.hasNext() && found == null) {
            TreeItemMapping cur = it.next();
            if (semanticCandidateExpression.equals(cur.getSemanticCandidatesExpression())) {
                found = cur;
            }
        }
        return found;
    }

    private Option<OperationAction> getAlreadyExistingMenuItem(EClass leaf, String referenceName) {
        OperationAction found = null;
        Iterator<OperationAction> it = doneItems.get(leaf).iterator();
        while (it.hasNext() && found == null) {
            OperationAction cur = it.next();
            Iterator<CreateInstance> itNewInstance = Iterators.filter(cur.eAllContents(), CreateInstance.class);
            while (itNewInstance.hasNext()) {
                CreateInstance newInstance = itNewInstance.next();
                if (referenceName.equals(newInstance.getReferenceName())) {
                    found = cur;
                }
            }
        }
        return Options.newSome(found);
    }

    private Optional<EAttribute> lookForEditableName(EClass eClassToStartFrom) {
        return eClassToStartFrom.getEAllAttributes().stream().filter((EAttribute input) -> {
            return "name".equals(input.getName()) && "EString".equals(input.getEType().getName());
        }).findFirst();
    }

}

class EClassHierarchy {

    private Map<EClass, List<EClass>> mostSpecific = new HashMap<>();

    EClassHierarchy(ResourceSet resourceSet) {

        Set<EClass> allClasses = new LinkedHashSet<>();
        resourceSet.getAllContents().forEachRemaining(o -> {
            if (o instanceof EClass) {
                allClasses.add((EClass) o);
            }
        });

        Set<EClass> allSuperTypes = new LinkedHashSet<>();
        for (EClass eClass : allClasses) {
            if (!"DiagramImportDescription".equals(eClass.getName())) {
                allSuperTypes.addAll(eClass.getEAllSuperTypes());
            }
        }
        allClasses.removeAll(allSuperTypes);
        
        for (EClass leaf : allClasses) {
            mostSpecific.computeIfAbsent(leaf, key -> new ArrayList<>()).add(leaf);
            Iterator<EClass> it = leaf.getEAllSuperTypes().iterator();
            while (it.hasNext()) {
                EClass next = it.next();
                mostSpecific.computeIfAbsent(next, key -> new ArrayList<>()).add(leaf);
            }
        }
    }

    public Iterable<EClass> getConcreteSubclassesLeafs(EClass targetClass) {
        return mostSpecific.get(targetClass).stream().filter((EClass klass) -> !klass.isAbstract()).collect(Collectors.toList());
    }

}
