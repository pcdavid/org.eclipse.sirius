/*******************************************************************************
 * Copyright (c) 2021 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.tests.swtbot;

import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.SiriusNoteEditPart;
import org.eclipse.sirius.tests.swtbot.support.api.business.UIResource;
import org.eclipse.sirius.tests.swtbot.support.api.editor.SWTBotSiriusDiagramEditor;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;

/**
 * Ensures that NoteAttachment with Rectilinear style works correctly when a Representation Link is moved.
 * 
 * @author <a href="mailto:glenn.plouhinec@obeo.fr">Glenn Plouhinec</a>
 *
 */
public class RectilinearNoteAttachmentWithRepresentationLinkTest extends AbstractRectilinearNoteAttachmentTest {

    private static final String MODEL_FILE = "bugzilla_570518_RepresentationLink.ecore";

    private static final String SESSION_FILE = "bugzilla_570518_RepresentationLink.aird";

    private static final String DATA_UNIT_DIR = "data/unit/noteAttachments/bugzilla_570518/";

    @Override
    protected void onSetUpBeforeClosingWelcomePage() throws Exception {
        copyFileToTestProject(Activator.PLUGIN_ID, DATA_UNIT_DIR, MODEL_FILE, SESSION_FILE);
        sessionAirdResource = new UIResource(designerProject, SESSION_FILE);
        localSession = designerPerspective.openSessionFromFile(sessionAirdResource, true);
        editor = (SWTBotSiriusDiagramEditor) openRepresentation(localSession.getOpenedSession(), "Entities", " package entities", DDiagram.class);
    }

    /**
     * Checks in this particular scenario that the bendpoints of the NoteAttachment remain consistent after moving the
     * Representation Link and that there is not just one bendpoint left.
     * 
     * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=570518"
     */
    public void _testConsistentNumberBendpoints() {
        // This test cannot be run correctly. The Representation Link is not moved by SWTBot.
        SWTBotGefEditPart representationLink = editor.getEditPart("Text", SiriusNoteEditPart.class);
        consistentNumberBendpoints(representationLink);
    }

    /**
     * @see AbstractRectilinearNoteAttachmentTest#removeBendpointsOnNoteAttachmentBeforeMovingElement()
     */
    public void testRemoveBendpointsOnNoteAttachmentBeforeMovingElement() {
        super.removeBendpointsOnNoteAttachmentBeforeMovingElement();
    }

    /**
     * @see AbstractRectilinearNoteAttachmentTest#removeBendpointsOnNoteAttachmentAfterMovingElement(SWTBotGefEditPart)
     */
    public void _testRemoveBendpointsOnNoteAttachmentAfterMovingNote() {
        // This test cannot be run correctly. The Representation Link is not moved by SWTBot.
        SWTBotGefEditPart representationLink = editor.getEditPart("Text", SiriusNoteEditPart.class);
        super.removeBendpointsOnNoteAttachmentAfterMovingElement(representationLink);
    }
}
