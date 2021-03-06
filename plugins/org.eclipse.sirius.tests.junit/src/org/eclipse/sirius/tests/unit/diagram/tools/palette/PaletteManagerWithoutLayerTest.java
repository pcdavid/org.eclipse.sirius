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
package org.eclipse.sirius.tests.unit.diagram.tools.palette;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.sirius.diagram.ui.tools.api.graphical.edit.palette.PaletteManager;
import org.eclipse.sirius.diagram.ui.tools.internal.palette.PaletteManagerImpl;

/**
 * Test class for {@link PaletteManagerImpl}.
 * 
 * Tests without layers.
 * 
 * @author dlecan
 */
public class PaletteManagerWithoutLayerTest extends AbstractPaletteManagerSectionTest {

    private static final String REPRESENTATION_DESC_NAME = "toolSectionWithoutLayers";

    private static final SortedSet<Entry> EXPECTED_ENTRIES_STD_PALETTE = new TreeSet<Entry>(Arrays.asList(
            //
            createNewEntry("toolSectionWithoutLayers", "CCD50-1", "Tool50-2-1")
    //
    ));

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getRepresentationDescriptionName() {
        return REPRESENTATION_DESC_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getRepresentationDescriptionInstanceName() {
        return "new toolSectionWithoutLayers";
    }

    /**
     * Test method.
     * 
     * @throws Exception
     *             Test error.
     */
    public void testCreatePalette() throws Exception {
        PaletteManager paletteManager = new PaletteManagerImpl(editDomain);
        updateTools(diagram);
        paletteManager.update(dDiagram);
        doContentPaletteTest(EXPECTED_ENTRIES_STD_PALETTE);
    }
}
