/*******************************************************************************
 * Copyright (c) 2018 Obeo.
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
package org.eclipse.sirius.ecore.extender.business.api.accessor;

import java.text.MessageFormat;
import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.ecore.extender.business.api.accessor.exception.FeatureNotFoundException;
import org.eclipse.sirius.ecore.extender.business.api.accessor.exception.MetaClassNotFoundException;
import org.eclipse.sirius.ecore.extender.business.api.permission.PermissionAuthorityRegistry;
import org.eclipse.sirius.ecore.extender.business.api.permission.exception.LockedInstanceException;
import org.eclipse.sirius.ecore.extender.business.internal.Messages;
import org.eclipse.sirius.ecore.extender.business.internal.accessor.ecore.EcoreIntrinsicExtender;

/**
 * Simple non-multiplexing ModelAccessor hard-coded to only support Ecore (via EcoreIntrinsicExtender).
 * 
 * @author pcdavid
 */
public class EcoreModelAccessor extends ModelAccessor {

    /**
     * Forward every call to the single EcoreIntrinsicExtender; no multiplexing.
     */
    private final EcoreIntrinsicExtender ecore = new EcoreIntrinsicExtender();

    /**
     * Default constructor.
     */
    public EcoreModelAccessor() {
        ecore.activate();
    }

    /**
     * Activate new metamodels on the Accessor.
     * 
     * @param mmDescriptors
     *            metamodel descriptors.
     */
    public void activateMetamodels(final Collection<? extends MetamodelDescriptor> mmDescriptors) {
        ecore.updateMetamodels(mmDescriptors);
    }

    @Override
    public void init(final ResourceSet set) {
        ecore.init(set);
        authority = PermissionAuthorityRegistry.getDefault().getPermissionAuthority(set);
    }

    @Override
    public EObject createInstance(final String name) throws MetaClassNotFoundException {
        final EObject value = ecore.createInstance(name);
        if (value != null) {
            authority.notifyNewInstanceCreation(value);
            return value;
        }
        throw new MetaClassNotFoundException(name);
    }

    @Override
    public boolean eIsKnownType(final String name) {
        return ecore.eIsKnownType(name);
    }

    @Override
    public boolean eValid(final EObject object, final String name) {
        return ecore.eValid(object, name);
    }

    @Override
    public Object eGet(final EObject instance, final String name) throws FeatureNotFoundException {
        if (!eValid(instance, name)) {
            throw new FeatureNotFoundException();
        }
        return ecore.eGet(instance, name);
    }

    @Override
    public Object eSet(final EObject instance, final String name, final Object value) throws FeatureNotFoundException, LockedInstanceException {
        if (authority.canEditFeature(instance, name)) {
            Object feedback = null;
            if (!eIsMany(instance, name)) {
                feedback = ecore.eSet(instance, name, value);
            } else {
                feedback = ecore.eAdd(instance, name, value);
            }

            if (feedback == null) {
                throw new FeatureNotFoundException(name);
            }
            authority.notifyInstanceChange(instance);
            return feedback;
        } else if (!silentMode) {
            throw new LockedInstanceException(instance);
        }
        return null;
    }

    @Override
    public void eAdd(final EObject instance, final String name, final Object value) throws FeatureNotFoundException, LockedInstanceException {
        if (authority.canEditFeature(instance, name)) {
            Object result = null;
            if (value instanceof EObject) {
                if (authority.canEditInstance((EObject) value)) {
                    result = ecore.eAdd(instance, name, value);
                }
            } else {
                result = ecore.eAdd(instance, name, value);
            }
            if (result == null) {
                throw new FeatureNotFoundException(name);
            }
            authority.notifyInstanceChange(instance);
        } else if (!silentMode) {
            throw new LockedInstanceException(instance);
        }
    }

    @Override
    public void eClear(final EObject instance, final String name) throws LockedInstanceException {
        if (authority.canEditFeature(instance, name)) {
            final Object value = ecore.eClear(instance, name);
            if (value != null) {
                authority.notifyInstanceChange(instance);
            }
        } else if (!silentMode) {
            throw new LockedInstanceException(instance);
        }
    }

    @Override
    public Object eRemove(final EObject instance, final String name, final Object value) throws LockedInstanceException {
        if (authority.canEditFeature(instance, name)) {
            final Object result = ecore.eRemove(instance, name, value);
            if (result != null) {
                authority.notifyInstanceChange(instance);
            }
            return result;
        } else if (!silentMode) {
            throw new LockedInstanceException(instance);
        }

        return null;
    }

    @Override
    public boolean eIsMany(final EObject instance, final String featureName) throws FeatureNotFoundException {
        final Boolean result = ecore.eIsMany(instance, featureName);
        if (result != null) {
            return result.booleanValue();
        }
        throw new FeatureNotFoundException(MessageFormat.format(Messages.ModelAccessor_error_featureNotFound, featureName, instance));
    }

    @Override
    public boolean eIsContainment(final EObject instance, final String featureName) throws FeatureNotFoundException {
        final Boolean result = ecore.eIsContainment(instance, featureName);
        if (result != null) {
            return result.booleanValue();
        }
        throw new FeatureNotFoundException(featureName);
    }

    @Override
    public EObject eContainer(final EObject instance) {
        return ecore.eContainer(instance);
    }

    @Override
    public boolean eInstanceOf(final EObject instance, final String typeName) {
        return ecore.eInstanceOf(instance, typeName);
    }

}
