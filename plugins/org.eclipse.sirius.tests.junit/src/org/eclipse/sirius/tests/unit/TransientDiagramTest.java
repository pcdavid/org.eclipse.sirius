/*******************************************************************************
 * Copyright (c) 2018 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tests.unit;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.diagram.DEdge;
import org.eclipse.sirius.diagram.DNodeList;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.business.internal.helper.task.operations.CreateViewTask;
import org.eclipse.sirius.tests.unit.common.AbstractEcoreSynchronizerTest;
import org.eclipse.sirius.tests.unit.diagram.modeler.ecore.EcoreModeler;

/**
 * Test that we can create a transient diagram (not associated to a session),
 * and keep it up to date afterwards.
 * 
 * @author pcdavid
 */
public class TransientDiagramTest extends AbstractEcoreSynchronizerTest {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activateViewpoint(EcoreModeler.DESIGN_VIEWPOINT_NAME);
    }

    @Override
    protected String getSemanticResourcePath() {
        return "/org.eclipse.sirius.tests.junit/data/unit/transientTest.ecore";
    }

    public void testTransientDiagram() throws Exception {
        EPackage p = (EPackage) this.semanticModel;
        assertTrue(p instanceof EPackage);
        /*
         * Get the automatically created diagram. It will be useful to find the
         * VSM elements we'll use in the transient one.
         */
        DSemanticDiagram existingDiag = (DSemanticDiagram) DialectManager.INSTANCE.getAllRepresentations(session).iterator().next();
        /*
         * Create the transient diagram.
         */
        DSemanticDiagram diag = (DSemanticDiagram) DialectManager.INSTANCE.createTransientRepresentation("Test", p.getESubpackages().get(0), existingDiag.getDescription(), new NullProgressMonitor());
        /*
         * After creation (and initial refresh), the diagram should contain 2
         * EClasses represented as DNodeLists (classes "A" and "B").
         */
        assertEquals(2, diag.getOwnedRepresentationElements().size());
        assertEquals(2, diag.getOwnedRepresentationElements().stream().filter(DNodeList.class::isInstance).count());
        /*
         * Explicitly invoke a "Create View" operation to add a view for a third
         * EClass, "C", not contained directly in the EPackge represented by the
         * diagram.
         */
        existingDiag.getDescription().getAllContainerMappings().stream().filter(m -> m.getName().equals("EC External EClass")).findFirst().ifPresent(m -> {
            CreateViewTask.createNodeView(diag, diag, p.getEClassifiers().get(0), session, m);
        });
        DialectManager.INSTANCE.refresh(diag, new NullProgressMonitor());
        /*
         * The refresh should have kept the 3rd view created by the tool, and
         * added the edge representing inheritance between "A" and "C".
         */
        assertEquals(3, diag.getOwnedRepresentationElements().stream().filter(DNodeList.class::isInstance).count());
        assertEquals(1, diag.getOwnedRepresentationElements().stream().filter(DEdge.class::isInstance).count());
    }
}
