package org.eclipse.sirius.tests.unit.common;

import java.io.File;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.sirius.ext.emf.AllContents;
import org.eclipse.sirius.tests.support.api.SiriusTestCase;
import org.eclipse.sirius.tools.api.command.ICommandFactory;
import org.eclipse.sirius.viewpoint.description.DescriptionPackage;
import org.eclipse.sirius.viewpoint.description.tool.ToolPackage;
import org.junit.Test;

import com.google.common.base.Objects;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;

public class VSMCustomizationAnalysisTest extends SiriusTestCase {

    private static final String VSM_FOLDER = "/home/pcdavid/vsms";

    private ResourceSet rs;

    private Map<EAttribute, Multiset<Object>> customizations = Maps.newHashMap();

    private Map<EAttribute, Integer> occurrences = Maps.newHashMap();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        rs = new ResourceSetImpl();
        File root = new File(VSMCustomizationAnalysisTest.VSM_FOLDER);
        for (String file : root.list()) {
            if (file.endsWith(".odesign")) {
                URI vsm = URI.createFileURI(root + "/" + file);
                rs.getResource(vsm, true);
            }
        }

    }

    @Test
    public void testAnalyzeVSM() throws Exception {
        for (Resource vsm : rs.getResources()) {
            for (EObject elt : AllContents.of(vsm.getContents().get(0), true)) {
                analyze(elt);
            }
        }
        report();
    }

    private void analyze(EObject o) {
        EClass klass = o.eClass();
        for (EAttribute attr : klass.getEAllAttributes()) {
            if (isModelerSpecific(attr)) {
                // These are 99.9% certainly specific to a user domain.
                continue;
            }
            Integer occ = occurrences.get(attr);
            if (occ == null) {
                occurrences.put(attr, 1);
            } else {
                occurrences.put(attr, occ + 1);
            }
            if (o.eIsSet(attr) && !Objects.equal(o.eGet(attr), attr.getDefaultValue())) {
                Multiset<Object> values = customizations.get(attr);
                if (values == null) {
                    values = HashMultiset.create();
                    customizations.put(attr, values);
                }
                values.add(o.eGet(attr));
            }
        }
    }

    private boolean isModelerSpecific(EAttribute attr) {
        if (attr.getEAttributeType() == DescriptionPackage.Literals.INTERPRETED_EXPRESSION || attr.getEAttributeType() == DescriptionPackage.Literals.FEATURE_NAME
                || attr.getEAttributeType() == DescriptionPackage.Literals.TYPE_NAME) {
            return true;
        } else if (attr == DescriptionPackage.Literals.DOCUMENTED_ELEMENT__DOCUMENTATION || attr == DescriptionPackage.Literals.IDENTIFIED_ELEMENT__LABEL
                || attr == DescriptionPackage.Literals.IDENTIFIED_ELEMENT__NAME || attr == ToolPackage.Literals.ABSTRACT_VARIABLE__NAME) {
            return true;
        } else if (attr.getName().toLowerCase().contains("icon") || attr.getName().toLowerCase().contains("path")) {
            return true;
        }
        return false;
    }

    private void report() {
        for (EAttribute attr : customizations.keySet()) {
            String fqn = attr.getEContainingClass().getEPackage().getName() + "::" + attr.getEContainingClass().getName() + "." + attr.getName();
            Multiset<Object> customValues = customizations.get(attr);
            int occ = occurrences.get(attr);
            int custom = customValues.size();
            double pctCustom = 100.0 * (double) custom / (double) occ;
            int customUnique = customValues.elementSet().size();
            System.out.println(String.format("%s: %d occurrences / %d customized (%.2f%%) / %d unique", fqn, occ ,custom , pctCustom, customUnique));
            ImmutableMultiset<Object> ordered = Multisets.copyHighestCountFirst(customValues);
            for (Object value : ordered.elementSet()) {
                int count = ordered.count(value);
                if (count > 1) {
                    System.out.println(String.format(" %03d × %s", count, value));
                }
            }
        }
    }

    @Override
    protected ICommandFactory getCommandFactory() {
        return null;
    }

}
