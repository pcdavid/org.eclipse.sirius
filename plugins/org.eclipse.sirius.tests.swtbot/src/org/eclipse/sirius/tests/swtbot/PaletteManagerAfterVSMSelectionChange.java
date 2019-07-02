/*******************************************************************************
 * Copyright (c) 2015 THALES GLOBAL SERVICES and others.
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

import java.util.Arrays;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.ui.tools.internal.palette.SectionPaletteDrawer;
import org.eclipse.sirius.diagram.ui.tools.internal.palette.SiriusPaletteViewer;
import org.eclipse.sirius.tests.swtbot.support.api.AbstractSiriusSwtBotGefTestCase;
import org.eclipse.sirius.tests.swtbot.support.api.business.UIResource;
import org.eclipse.sirius.tests.swtbot.support.api.editor.SWTBotSiriusDiagramEditor;
import org.eclipse.sirius.tests.swtbot.support.utils.SWTBotUtils;

import com.google.common.collect.Iterables;

/**
 * Tests palette update after viewpoint selection change.
 * 
 * @author <a href="mailto:belqassim.djafer@obeo.fr">Belqassim Djafer</a>
 *
 */
public class PaletteManagerAfterVSMSelectionChange extends AbstractSiriusSwtBotGefTestCase {

    private static final String DATA_UNIT_DIR = "/data/unit/tools/palette/extension/";

    private static final String FILE_DIR = "/";

    private static final String MODEL_NAME = "My.ecore";

    private static final String MODELER_NAME = "main.odesign";

    private static final String MODELER_EXTENSION_NAME = "extended.odesign";

    private static final String SESSION_NAME = "My.aird";

    private static final String REPRESENTATION_DESC_NAME = "classDiagram";

    private static final String REPRESENTATION_INST_DESC_NAME = "new classDiagram";

    private static final String EXTENSION_VIEWPOINT_NAME = "others";

    private static final String LAYER_1 = "Layer_1";

    private static final String OTHERS_LAYER_1 = "Others_Layer_1";

    private static final SortedSet<String> EXPECTED_ENTRIES_LAYER_PERSON1_SHOWN = new TreeSet<>(Arrays.asList("createClass_1"));

    private static final SortedSet<String> EXPECTED_ENTRIES_LAYER_PERSON1_OTHERS1_SHOWN = new TreeSet<>(Arrays.asList("createClass_1", "createEEnum_1"));

    @Override
    protected void onSetUpAfterOpeningDesignerPerspective() throws Exception {
        copyFileToTestProject(Activator.PLUGIN_ID, DATA_UNIT_DIR, MODEL_NAME, SESSION_NAME, MODELER_NAME, MODELER_EXTENSION_NAME);
        final UIResource sessionAirdResource = new UIResource(designerProject, FILE_DIR, SESSION_NAME);
        localSession = designerPerspective.openSessionFromFile(sessionAirdResource);
    }

    @Override
    protected void tearDown() throws Exception {
        localSession = null;
        super.tearDown();
    }

    /**
     * Ensure that only selected viewpoint tools still available from the palette after a viewpoint selection change.
     * 
     * @throws Exception
     *             Test error.
     */
    public void testHideShowLayersAfterDiagramCreation() throws Exception {
        editor = (SWTBotSiriusDiagramEditor) openRepresentation(localSession.getOpenedSession(), REPRESENTATION_DESC_NAME, REPRESENTATION_INST_DESC_NAME, DDiagram.class);
        editor.changeLayerActivation(LAYER_1);
        SWTBotUtils.waitAllUiEvents();
        editor.changeLayerActivation(OTHERS_LAYER_1);
        assertEquals("Palette entries content is wrong", EXPECTED_ENTRIES_LAYER_PERSON1_OTHERS1_SHOWN, getVisiblePaletteEntries());
        localSession.changeViewpointSelection(Collections.<String> emptySet(), Collections.singleton(EXTENSION_VIEWPOINT_NAME));
        assertEquals("Palette entries content is wrong", EXPECTED_ENTRIES_LAYER_PERSON1_SHOWN, getVisiblePaletteEntries());
    }

    private TreeSet<String> getVisiblePaletteEntries() {
        PaletteRoot paletteRoot = ((SiriusPaletteViewer) editor.getSiriusPaletteGroupEditPartBot().part().getViewer()).getPaletteRoot();
        Iterable<SectionPaletteDrawer> drawers = Iterables.filter(paletteRoot.getChildren(), SectionPaletteDrawer.class);
        TreeSet<String> result = new TreeSet<>();
        for (PaletteEntry paletteEntry : drawers) {
            for (Object child : ((SectionPaletteDrawer) paletteEntry).getChildren()) {
                if (child instanceof PaletteEntry && ((PaletteEntry) child).isVisible()) {
                    result.add(((PaletteEntry) child).getLabel());
                }
            }
        }
        return result;
    }
}
