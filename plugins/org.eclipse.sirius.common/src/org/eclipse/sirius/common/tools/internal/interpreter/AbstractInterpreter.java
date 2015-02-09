/*******************************************************************************
 * Copyright (c) 2013, 2014 THALES GLOBAL SERVICES and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.common.tools.internal.interpreter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.sirius.common.tools.api.interpreter.EvaluationException;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreter;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreterWithDiagnostic;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreter2;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreterContext;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreterStatus;
import org.eclipse.sirius.common.tools.api.interpreter.IVariableStatusListener;
import org.eclipse.sirius.common.tools.api.interpreter.TypedValidation;
import org.eclipse.sirius.ecore.extender.business.api.accessor.MetamodelDescriptor;
import org.eclipse.sirius.ecore.extender.business.api.accessor.ModelAccessor;
import org.eclipse.sirius.ext.emf.InverseReferenceFinder;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * Abstract base class for implementations of {@link IInterpreter}.
 * 
 * @author pcdavid
 */
public abstract class AbstractInterpreter implements IInterpreter, TypedValidation, IInterpreterWithDiagnostic {

    /** The separator between EPackage name and EClass name for domain class. */
    protected static final String SEPARATOR = "."; //$NON-NLS-1$

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean provides(String expression) {
        return expression != null && expression.startsWith(getPrefix());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<EObject> evaluateCollection(EObject context, String expression) throws EvaluationException {
        Object raw = evaluate(context, expression);
        final Collection<EObject> result;
        if (raw instanceof Collection<?>) {
            result = Lists.newArrayList(Iterables.filter((Collection<?>) raw, EObject.class));
        } else if (raw instanceof EObject) {
            result = Collections.singleton((EObject) raw);
        } else if (raw != null && raw.getClass().isArray()) {
            result = Lists.newArrayList(Iterables.filter(Lists.newArrayList((Object[]) raw), EObject.class));
        } else {
            result = Collections.emptySet();
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean evaluateBoolean(EObject context, String expression) throws EvaluationException {
        Object raw = evaluate(context, expression);
        final boolean result;
        if (raw == null) {
            result = false;
        } else if (raw instanceof Boolean) {
            result = ((Boolean) raw).booleanValue();
        } else {
            String toString = raw.toString();
            if ("true".equalsIgnoreCase(toString)) { //$NON-NLS-1$
                result = true;
            } else if ("false".equalsIgnoreCase(toString)) { //$NON-NLS-1$
                result = false;
            } else {
                /*
                 * raw is != null and its toString is neither true or false,
                 * this happens when the user expect the condition to check that
                 * a value is existing, then we consider any non null value
                 * returns true and null returns false.
                 */
                result = true;
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EObject evaluateEObject(EObject context, String expression) throws EvaluationException {
        Object raw = evaluate(context, expression);
        if (raw instanceof EObject) {
            return (EObject) raw;
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String evaluateString(EObject context, String expression) throws EvaluationException {
        Object raw = evaluate(context, expression);
        if (raw != null) {
            return String.valueOf(raw);
        } else {
            return ""; //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer evaluateInteger(EObject context, String expression) throws EvaluationException {
        Object raw = evaluate(context, expression);
        try {
            return Integer.parseInt(String.valueOf(raw));
        } catch (NumberFormatException e) {
            return Integer.valueOf(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearImports() {
        // Nothing to do.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addImport(String dependency) {
        // Nothing to do.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProperty(Object key, Object value) {
        // Nothing to do.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVariable(String name, Object value) {
        // Nothing to do.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unSetVariable(String name) {
        // Nothing to do.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getVariable(String name) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearVariables() {
        // Nothing to do.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        // Nothing to do.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addVariableStatusListener(IVariableStatusListener newListener) {
        // Nothing to do.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeVariableStatusListener(IVariableStatusListener listener) {
        // Nothing to do.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, ?> getVariables() {
        return Collections.emptyMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setModelAccessor(ModelAccessor modelAccessor) {
        // Nothing to do.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVariablePrefix() {
        return ""; //$NON-NLS-1$
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCrossReferencer(ECrossReferenceAdapter crossReferencer) {
        // Nothing to do.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInverseReferenceFinder(InverseReferenceFinder irf) {
        // Nothing to do.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getImports() {
        return Collections.emptySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeImport(String dependency) {
        // Nothing to do.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supportsValidation() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<IInterpreterStatus> validateExpression(IInterpreterContext context, String expression) {
        return analyzeExpression(context, expression).getStatuses();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activateMetamodels(Collection<MetamodelDescriptor> metamodels) {
        // Nothing to do.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IEvaluationResult evaluateExpression(EObject target, String expression) throws EvaluationException {
        final Object result = this.evaluate(target, expression);
        return new IEvaluationResult() {

            @Override
            public Object getValue() {
                return result;
            }

            @Override
            public Diagnostic getDiagnostic() {
                return Diagnostic.OK_INSTANCE;
            }
        };
    }
}
