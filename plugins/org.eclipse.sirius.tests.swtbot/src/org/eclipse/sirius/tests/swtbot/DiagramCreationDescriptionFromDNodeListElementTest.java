/*******************************************************************************
 * Copyright (c) 2010, 2014 THALES GLOBAL SERVICES.
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
import org.eclipse.sirius.tests.swtbot.support.api.AbstractSiriusSwtBotGefTestCase;
import org.eclipse.sirius.tests.swtbot.support.api.business.UIDiagramRepresentation;
import org.eclipse.sirius.tests.swtbot.support.api.business.UILocalSession;
import org.eclipse.sirius.tests.swtbot.support.api.business.UIResource;
import org.eclipse.sirius.tests.swtbot.support.api.editor.SWTBotSiriusDiagramEditor;

/**
 * Test class for diagram creation description.
 * 
 * @author smonnier
 */
public class DiagramCreationDescriptionFromDNodeListElementTest extends AbstractSiriusSwtBotGefTestCase {

    private static final String REPRESENTATION_INSTANCE_NAME = "new TC814 Square List Package";

    private static final String REPRESENTATION_NAME = "TC814 Square List Package";

    private static final String MODEL = "tc814.ecore";

    private static final String SESSION_FILE = "tc814.aird";

    private static final String VSM_FILE = "tc814.odesign";

    private static final String DATA_UNIT_DIR = "data/unit/navigation/tc814/";

    private static final String FILE_DIR = "/";

    private static final String EXPECTED_NEW_REPRESENTATION_NAME = "Navigate to Not List";

    private static final String EXPECTED_NEW_REPRESENTATION_INSTANCE_NAME = "new Navigate to Not List";

    private UIResource sessionAirdResource;

    private UILocalSession localSession;

    /**
     * Current diagram.
     */
    protected UIDiagramRepresentation diagram;

    @Override
    protected void onSetUpBeforeClosingWelcomePage() throws Exception {
        copyFileToTestProject(Activator.PLUGIN_ID, DATA_UNIT_DIR, MODEL, SESSION_FILE, VSM_FILE);

    }

    @Override
    protected void onSetUpAfterOpeningDesignerPerspective() throws Exception {
        sessionAirdResource = new UIResource(designerProject, FILE_DIR, SESSION_FILE);
        localSession = designerPerspective.openSessionFromFile(sessionAirdResource);

        editor = (SWTBotSiriusDiagramEditor) openRepresentation(localSession.getOpenedSession(), REPRESENTATION_NAME, REPRESENTATION_INSTANCE_NAME, DDiagram.class);
    }

    /**
     * Test method.
     * 
     * @throws Exception
     *             Test error.
     */
    public void testNewRepresentationFromDNodeListElement() throws Exception {
        withTimeout(1000, () -> {
            editor.click(175, 45);
            editor.clickContextMenu(EXPECTED_NEW_REPRESENTATION_NAME);
            bot.button("OK").click();
            assertEditorIsNotError("Right click New representation editor did not opened correctly", bot.activeEditor());
            assertEquals("The active editor is not the one expected", EXPECTED_NEW_REPRESENTATION_INSTANCE_NAME, bot.activeEditor().getTitle());
        });
    }
}
