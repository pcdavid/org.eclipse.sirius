/*******************************************************************************
 * Copyright (c) 2014, 2015 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.business.internal.session.danalysis;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.common.tools.api.util.LazyCrossReferencer;

import com.google.common.collect.Iterables;

import com.google.common.collect.Sets;

/**
 * A LazyCrossReferencer for the session.
 * {@link LazyCrossReferencer#initialize()} is overridden in order to only add
 * the adapter at the first use.
 * 
 * @author <a href="mailto:mickael.lanoe@obeo.fr">Mickael LANOE</a>
 *
 */
public class SessionLazyCrossReferencer extends LazyCrossReferencer {
    private DAnalysisSessionImpl session;

    /**
     * Construct from an opened session.
     * 
     * @param session
     *            an opened session
     */
    public SessionLazyCrossReferencer(DAnalysisSessionImpl session) {
        this.session = session;
    }

    @Override
    protected void initialize() {
        super.initialize();
        
        Collection<Resource> semanticResources = session.getSemanticResources();
        EList<Resource> controlledResources = session.getControlledResources();
        Set<Resource> allSessionResources = session.getAllSessionResources();
        
        Iterable<Resource> resources = Iterables.concat(semanticResources, controlledResources, allSessionResources);
        for (Resource resource : resources) {
            List<Adapter> adapters = resource.eAdapters();
            // add only if it was not added between creation and
            // initialization
            if (!adapters.contains(this)) {
                adapters.add(this);
            }
        }
    }
    
    @Override
    public Collection<Setting> getInverseReferences(EObject object, boolean resolve) {
        Collection<Setting> result = super.getInverseReferences(object, resolve);
        if (object != null) {
            EClass klass = object.eClass();
            // CHECKSTYLE:OFF
            StringBuilder trace = new StringBuilder("### getInverseReferences(").append(klass.getEPackage().getName()).append(".").append(klass.getName()).append("): ");
            Set<EStructuralFeature> features = Sets.newHashSet();
            for (Setting setting : result) {
                features.add(setting.getEStructuralFeature());
            }
            for (EStructuralFeature esf : features) {
                klass = esf.getEContainingClass();
                trace.append(klass.getEPackage().getName()).append(".").append(klass.getName()).append("::").append(esf.getName()).append(" ");
            }
            System.out.println(trace);
            // CHECKSTYLE:ON
        }
        return result;
    }
}
