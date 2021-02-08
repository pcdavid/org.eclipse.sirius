/*******************************************************************************
 * Copyright (c) 2007-2015 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.diagram.business.api.query;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.common.tools.api.util.StringUtil;
import org.eclipse.sirius.diagram.AppliedCompositeFilters;
import org.eclipse.sirius.diagram.CollapseFilter;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DDiagramElementContainer;
import org.eclipse.sirius.diagram.DEdge;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.DNodeListElement;
import org.eclipse.sirius.diagram.FoldingFilter;
import org.eclipse.sirius.diagram.FoldingPointFilter;
import org.eclipse.sirius.diagram.HideFilter;
import org.eclipse.sirius.diagram.HideLabelCapabilityStyle;
import org.eclipse.sirius.diagram.HideLabelFilter;
import org.eclipse.sirius.diagram.IndirectlyCollapseFilter;
import org.eclipse.sirius.diagram.LabelPosition;
import org.eclipse.sirius.diagram.description.DiagramElementMapping;
import org.eclipse.sirius.diagram.description.style.EdgeStyleDescription;
import org.eclipse.sirius.diagram.util.DiagramSwitch;
import org.eclipse.sirius.ext.base.Option;
import org.eclipse.sirius.ext.base.Options;
import org.eclipse.sirius.viewpoint.BasicLabelStyle;
import org.eclipse.sirius.viewpoint.Style;
import org.eclipse.sirius.viewpoint.description.RepresentationElementMapping;
import org.eclipse.sirius.viewpoint.description.style.LabelStyleDescription;
import org.eclipse.sirius.viewpoint.description.style.StyleDescription;

/**
 * A class aggregating all the queries (read-only!) having a
 * {@link DDiagramElement} as a starting point.
 * 
 * @author mporhel
 */
public class DDiagramElementQuery {

    private DDiagramElement element;

    /**
     * Create a new query.
     * 
     * @param dDiagramElement
     *            the element to query.
     */
    public DDiagramElementQuery(DDiagramElement dDiagramElement) {
        this.element = dDiagramElement;
    }

    /**
     * Return the mapping of the current {@link DDiagramElement}.
     * 
     * @return the mapping of the current {@link DDiagramElement}.
     */
    public Option<? extends RepresentationElementMapping> getMapping() {
        ActualMappingGetter mappingGetter = new ActualMappingGetter();
        return mappingGetter.doSwitch(this.element);
    }

    /**
     * Return the mapping name of the current {@link DDiagramElement}.
     * 
     * @return the mapping name of the current {@link DDiagramElement}.
     */
    public Option<String> getMappingName() {
        String result = null;
        final Option<? extends RepresentationElementMapping> mapping = getMapping();
        if (mapping != null && mapping.some()) {
            result = mapping.get().getName();
        }
        return Options.newSome(result);
    }

    /**
     * Check if this {@link DDiagramElement} is directly hidden.
     * 
     * @return true if the given element is hidden.
     */
    public boolean isHidden() {
        return element.getGraphicalFilters().stream().anyMatch(HideFilter.class::isInstance);
    }

    /**
     * Tells if the style of this {@link DDiagramElement} has some
     * customizations.
     * 
     * @return true if the style of this {@link DDiagramElement} has some
     *         customizations, false else
     */
    public boolean isCustomized() {
        boolean isCustomized = false;
        if (element instanceof DEdge) {
            isCustomized = new DEdgeQuery((DEdge) element).isCustomized();
        } else if (element instanceof DNode) {
            DNode dNode = (DNode) element;
            if (dNode.getOwnedStyle() != null) {
                isCustomized = !dNode.getOwnedStyle().getCustomFeatures().isEmpty();
            }
        } else if (element instanceof DDiagramElementContainer) {
            DDiagramElementContainer dDiagramElementContainer = (DDiagramElementContainer) element;
            if (dDiagramElementContainer.getOwnedStyle() != null) {
                isCustomized = !dDiagramElementContainer.getOwnedStyle().getCustomFeatures().isEmpty();
            }
        } else if (element instanceof DNodeListElement) {
            DNodeListElement dNodeElementElement = (DNodeListElement) element;
            if (dNodeElementElement.getOwnedStyle() != null) {
                isCustomized = !dNodeElementElement.getOwnedStyle().getCustomFeatures().isEmpty();
            }
        }
        return isCustomized;
    }

    /**
     * Check if the label of this {@link DDiagramElement} is directly hidden.
     * 
     * @return true if the label of the given element is hidden.
     */
    public boolean isLabelHidden() {
        return element.getGraphicalFilters().stream().anyMatch(HideLabelFilter.class::isInstance);
    }

    /**
     * Check if this {@link DDiagramElement} have a label that can be hidden
     * (i.e. a style with labelPosition to border).
     * 
     * @return true if the label of the diagram element can be hidden, false
     *         otherwise.
     */
    public boolean canHideLabel() {
        boolean canHideLabel = false;

        if (!isLabelEmpty()) {
            if (element instanceof DEdge) {
                canHideLabel = true;
            } else if (element instanceof DNode) {
                if (LabelPosition.BORDER_LITERAL.equals(((DNode) element).getOwnedStyle().getLabelPosition())) {
                    canHideLabel = true;
                }
            } else if (element instanceof DDiagramElementContainer) {
                // DNodeContainer & DNodeList
                canHideLabel = true;
            }
        }

        return canHideLabel;
    }

    private boolean isLabelEmpty() {

        boolean result = true;
        DiagramElementMapping mapping = element.getDiagramElementMapping();
        if (mapping == null) {
            return result;
        }

        final Style style = element.getStyle();
        if (style != null) {
            StyleDescription styleDescription = style.getDescription();
            if (styleDescription instanceof LabelStyleDescription) {
                String labelExpression = ((LabelStyleDescription) styleDescription).getLabelExpression();
                result = StringUtil.isEmpty(labelExpression);
            } else if (styleDescription instanceof EdgeStyleDescription && element instanceof DEdge) {
                result = !new DEdgeQuery((DEdge) element).hasNonEmptyNameDefinition();
            }
        }
        return result;
    }

    /**
     * Check if this {@link DDiagramElement} is indirectly hidden. It can be
     * directly hidden or hidden by its container or by its source or target not
     * for edges.
     * 
     * @return true if the given element is indirectly hidden.
     */
    public boolean isIndirectlyHidden() {
        if (isHidden()) {
            return true;
        }

        boolean isHidden = false;

        if (element.eContainer() instanceof DDiagramElement) {
            isHidden = new DDiagramElementQuery((DDiagramElement) element.eContainer()).isIndirectlyHidden();
        } else if (element instanceof DEdge) {
            final DEdge edge = (DEdge) element;
            if (edge.getSourceNode() instanceof DDiagramElement) {
                isHidden = isHidden || new DDiagramElementQuery((DDiagramElement) edge.getSourceNode()).isIndirectlyHidden();
            }
            if (edge.getTargetNode() instanceof DDiagramElement) {
                isHidden = isHidden || new DDiagramElementQuery((DDiagramElement) edge.getTargetNode()).isIndirectlyHidden();
            }
        }

        return isHidden;
    }

    /**
     * Check if this {@link DDiagramElement} is directly collapsed.
     * 
     * @return true if the given element is directly collapsed.
     */
    public boolean isCollapsed() {
        return element.getGraphicalFilters().stream().anyMatch(f -> f instanceof CollapseFilter && !(f instanceof IndirectlyCollapseFilter));
    }

    /**
     * Check if this {@link DDiagramElement} is directly or indirectly
     * collapsed. Ie if this {@link DDiagramElement} is filtered by an activated
     * filter. It can be directly filtered or one of its container / source /
     * target is filtered.
     * 
     * @return true if the given element is indirectly filtered.
     */
    public boolean isIndirectlyCollapsed() {
        if (isCollapsed()) {
            return true;
        }
        return isOnlyIndirectlyCollapsed();
    }

    /**
     * Check if this {@link DDiagramElement} is indirectly collapsed.
     * 
     * @return true if the given element is indirectly filtered.
     */
    public boolean isOnlyIndirectlyCollapsed() {
        return element.getGraphicalFilters().stream().anyMatch(IndirectlyCollapseFilter.class::isInstance);
    }

    /**
     * Check if this {@link DDiagramElement} is directly filtered by an
     * activated filter.
     * 
     * @return true if the given element is filtered.
     */
    public boolean isFiltered() {
        return element.getGraphicalFilters().stream().anyMatch(AppliedCompositeFilters.class::isInstance);
    }

    /**
     * Check if this {@link DDiagramElement} is filtered by an activat// Style
     * Style = null; It can be directly filtered or one of its container /
     * source / target is filtered.
     * 
     * @return true if the given element is indireclty filtered.
     */
    public boolean isIndirectlyFiltered() {
        if (isFiltered()) {
            return true;
        }

        boolean isFiltered = false;

        if (element.eContainer() instanceof DDiagramElement) {
            isFiltered = new DDiagramElementQuery((DDiagramElement) element.eContainer()).isFiltered();
        } else if (element instanceof DEdge) {
            final DEdge edge = (DEdge) element;
            if (edge.getSourceNode() instanceof DDiagramElement) {
                isFiltered = isFiltered || new DDiagramElementQuery((DDiagramElement) edge.getSourceNode()).isFiltered();
            }
            if (edge.getTargetNode() instanceof DDiagramElement) {
                isFiltered = isFiltered || new DDiagramElementQuery((DDiagramElement) edge.getTargetNode()).isFiltered();
            }
        }

        return isFiltered;
    }

    /**
     * Check if this {@link DDiagramElement} is collapsed.
     * 
     * @return true if the given element is collapsed.
     */
    public Option<AppliedCompositeFilters> getAppliedCompositeFilters() {
        // @formatter:off
        List<AppliedCompositeFilters> appliedFilters = element.getGraphicalFilters().stream()
                                                              .filter(AppliedCompositeFilters.class::isInstance)
                                                              .map(AppliedCompositeFilters.class::cast)
                                                              .collect(Collectors.toList());
        // @formatter:on
        if (appliedFilters.isEmpty()) {
            return Options.newNone();
        } else {
            return Options.newSome(appliedFilters.get(0));
        }
    }

    /**
     * Get the {@link BasicLabelStyle} of the current {@link DDiagramElement}.
     * 
     * @return a {@link BasicLabelStyle}
     */
    public Option<BasicLabelStyle> getLabelStyle() {
        BasicLabelStyle labelStyle = null;
        if (element instanceof DEdge) {
            labelStyle = ((DEdge) element).getOwnedStyle().getCenterLabelStyle();
        } else if (element instanceof DNode) {
            labelStyle = ((DNode) element).getOwnedStyle();
        } else if (element instanceof DDiagramElementContainer) {
            labelStyle = ((DDiagramElementContainer) element).getOwnedStyle();
        } else if (element instanceof DNodeListElement) {
            labelStyle = ((DNodeListElement) element).getOwnedStyle();
        }
        return Options.newSome(labelStyle);
    }

    /**
     * Switch to avoid many instanceof tests to get the actual mapping of a
     * {@link DDiagramElement}.
     * 
     * @author mporhel
     */
    private static class ActualMappingGetter extends DiagramSwitch<Option<? extends RepresentationElementMapping>> {
        @Override
        public Option<? extends RepresentationElementMapping> defaultCase(EObject object) {
            return Options.newNone();
        }

        @Override
        public Option<? extends RepresentationElementMapping> caseDDiagramElementContainer(DDiagramElementContainer object) {
            return Options.newSome(object.getActualMapping());
        }

        @Override
        public Option<? extends RepresentationElementMapping> caseDNode(DNode object) {
            return Options.newSome(object.getActualMapping());
        }

        @Override
        public Option<? extends RepresentationElementMapping> caseDNodeListElement(DNodeListElement object) {
            return Options.newSome(object.getActualMapping());
        }

        @Override
        public Option<? extends RepresentationElementMapping> caseDEdge(DEdge object) {
            return new IEdgeMappingQuery(object.getActualMapping()).getEdgeMapping();
        }
    }

    /**
     * Check if the label of the element must be hidden by default at creation.
     * 
     * @return true if the label must be hidden by default at creation, false
     *         otherwise.
     */
    public boolean isLabelVisibleByDefault() {
        boolean result = true;
        if (element.getStyle() instanceof HideLabelCapabilityStyle) {
            result = !((HideLabelCapabilityStyle) element.getStyle()).isHideLabelByDefault();
        }
        return result;
    }

    /**
     * Tests whether the dDiagramElement is explicitly folded, i.e. it is a
     * folding point.
     * 
     * @return <code>true</code> if the dDiagramElement is explicitly folded.
     */
    public boolean isExplicitlyFolded() {
        return element.getGraphicalFilters().stream().anyMatch(FoldingPointFilter.class::isInstance);
    }

    /**
     * Tests whether the dDiagramElement is indirectly folded because it is
     * accessible from another edge which was explicitly folded.
     * 
     * @return <code>true</code> if the dDiagramElement is explicitly folded.
     */
    public boolean isIndirectlyFolded() {
        return element.getGraphicalFilters().stream().anyMatch(FoldingFilter.class::isInstance);
    }

    /**
     * Tests whether the dDiagramElement is explicitly or indirectly folded.
     * 
     * @return <code>true</code> if the dDiagramElement is folded (explicitly or
     *         indirectly).
     */
    public boolean isFolded() {
        return element.getGraphicalFilters().stream().anyMatch(f -> f instanceof FoldingPointFilter || f instanceof FoldingFilter);
    }
}
