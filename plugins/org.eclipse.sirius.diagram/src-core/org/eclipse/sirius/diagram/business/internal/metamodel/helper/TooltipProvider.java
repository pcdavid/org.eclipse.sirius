/*******************************************************************************
 * Copyright (c) 2020 Obeo.
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
package org.eclipse.sirius.diagram.business.internal.metamodel.helper;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.business.api.logger.RuntimeLoggerManager;
import org.eclipse.sirius.common.tools.api.interpreter.EvaluationException;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreter;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreterSiriusVariables;
import org.eclipse.sirius.common.tools.api.util.StringUtil;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DiagramPackage;
import org.eclipse.sirius.viewpoint.DRepresentationElement;
import org.eclipse.sirius.viewpoint.description.style.StylePackage;
import org.eclipse.sirius.viewpoint.description.style.TooltipStyleDescription;

/**
 * Computes the tool-tip of a {@link DRepresentationElement}.
 * 
 * @author pcdavid
 */
public class TooltipProvider {
    private final IInterpreter interpreter;

    private final RuntimeLoggerManager logger;

    /**
     * Constructor.
     * 
     * @param interpreter
     *            the interpreter to use to evaluate the tool-tip expression.
     * @param logger
     *            the logger to use in case of error.
     */
    public TooltipProvider(IInterpreter interpreter, RuntimeLoggerManager logger) {
        this.interpreter = Objects.requireNonNull(interpreter);
        this.logger = Objects.requireNonNull(logger);
    }

    /**
     * Computes the tool-tip for the specified representation element.
     * 
     * @param element
     *            the representation element.
     * @param style
     *            the effective tool-tip style to consider. It may be different from the primary one associated with the
     *            element's mapping, depending on which mapping imports, custom styles and style customizations are in
     *            effect.
     * @return the tool-tip to display for the element, if any could be computed.
     */
    public Optional<String> computeTooltip(DRepresentationElement element, TooltipStyleDescription style) {
        return this.computeTooltip(element, style, Collections.emptyMap());
    }

    /**
     * Computes the tool-tip for the specified representation element.
     * 
     * @param element
     *            the representation element.
     * @param style
     *            the effective tool-tip style to consider. It may be different from the primary one associated with the
     *            element's mapping, depending on which mapping imports, custom styles and style customizations are in
     *            effect.
     * @param vars
     *            additional variables to set for the evaluation of the expression.
     * @return the tool-tip to display for the element, if any could be computed.
     */
    public Optional<String> computeTooltip(DRepresentationElement element, TooltipStyleDescription style, Map<String, Object> vars) {
        Optional<String> result = Optional.of(StylePackage.eINSTANCE.getTooltipStyleDescription_TooltipExpression().getDefaultValueLiteral());
        if (style != null && !StringUtil.isEmpty(style.getTooltipExpression())) {
            try {
                interpreter.setVariable(IInterpreterSiriusVariables.VIEW, element);
                if (vars != null) {
                    vars.forEach((name, value) -> interpreter.setVariable(name, value));
                }
                String tooltip = interpreter.evaluateString(element.getTarget(), style.getTooltipExpression());
                result = Optional.of(tooltip);
            } catch (EvaluationException e) {
                logger.error(style, StylePackage.eINSTANCE.getTooltipStyleDescription_TooltipExpression(), e);
                result = Optional.empty();
            } finally {
                if (vars != null) {
                    vars.forEach((name, value) -> interpreter.unSetVariable(name));
                }
                interpreter.unSetVariable(IInterpreterSiriusVariables.VIEW);
            }
        }
        return result;
    }

    /**
     * Re-computes the tool-tip for the specified representation element and update it if the new value is different
     * from the previous one.
     * 
     * @param element
     *            the representation element.
     * @param style
     *            the effective tool-tip style to consider. It may be different from the primary one associated with the
     *            element's mapping, depending on which mapping imports, custom styles and style customizations are in
     *            effect.
     * @param vars
     *            additional variables to set for the evaluation of the expression.
     */
    public void refreshTooltip(DDiagramElement element, TooltipStyleDescription style, Map<String, Object> vars) {
        if (element.eIsSet(DiagramPackage.Literals.DDIAGRAM_ELEMENT__TOOLTIP_TEXT)) {
            String oldValue = element.getTooltipText();
            Optional<String> newValue = computeTooltip(element, style, vars);
            if (newValue.isPresent() && !Objects.equals(newValue.get(), oldValue)) {
                element.setTooltipText(newValue.get());
            }
        }
    }

}
