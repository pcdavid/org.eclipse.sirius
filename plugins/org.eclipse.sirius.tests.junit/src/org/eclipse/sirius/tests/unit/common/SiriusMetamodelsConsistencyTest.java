/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tests.unit.common;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.Lists;

/**
 * Performs some basic checks on the metamodels defined by Sirius to ensure they
 * are always consistent with our architectural rules.
 * 
 * @author pcdavid
 *
 */
public class SiriusMetamodelsConsistencyTest {
    private static final Collection<URI> METAMODELS = Lists.newArrayList(URI.createPlatformPluginURI("org.eclipse.sirius/model/viewpoint.ecore", true),
            URI.createPlatformPluginURI("org.eclipse.sirius/model/contribution.ecore", true), URI.createPlatformPluginURI("org.eclipse.sirius.diagram/model/diagram.ecore", true),
            URI.createPlatformPluginURI("org.eclipse.sirius.diagram.layoutdata/model/layoutdata.ecore", true),
            URI.createPlatformPluginURI("org.eclipse.sirius.diagram.sequence/model/sequence.ecore", true), URI.createPlatformPluginURI("org.eclipse.sirius.table/model/table.ecore", true),
            URI.createPlatformPluginURI("org.eclipse.sirius.tree/model/tree.ecore", true));

    @BeforeClass
    public static void initializeEMF() {
        if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
            @SuppressWarnings("unused")
            EPackage ecore = EcorePackage.eINSTANCE;
            Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
        }
    }

    @Test
    public void allMetamodelsLoadWithoutErrors() {
        ResourceSet rs = new ResourceSetImpl();
        for (URI uri : METAMODELS) {
            Resource res = rs.getResource(uri, true);
            assertEquals(0, res.getWarnings().size());
            assertEquals(0, res.getErrors().size());
        }
    }
}
