/*******************************************************************************
 * Copyright (c) 2020 Obeo.
 * All rights reserved.
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tests.unit.diagram.layout;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.WorkspaceViewerProperties;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.ArrangeRequest;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.ui.tools.api.editor.DDiagramEditor;
import org.eclipse.sirius.tests.support.api.SiriusDiagramTestCase;
import org.eclipse.sirius.tests.support.api.TestsUtil;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager;
import org.eclipse.sirius.ui.business.api.session.SessionUIManager;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.ui.IEditorPart;

/**
 * Tests to realize some verification of arrange result with basic ELK layouts.
 * 
 * @author lredor
 */
@SuppressWarnings("restriction")
public class SimpleELKLayoutTest extends SiriusDiagramTestCase {
    private static final String SEMANTIC_MODEL_PATH = "/org.eclipse.sirius.tests.junit/data/unit/layout/withELK/My.ecore";

    private static final String VSM_PATH = "/org.eclipse.sirius.tests.junit/data/unit/layout/withELK/My.odesign";

    private static final String REPRESENTATIONS_MODEL_PATH = "/org.eclipse.sirius.tests.junit/data/unit/layout/withELK/representations.aird";

    private DDiagram diagram;

    private IDiagramWorkbenchPart editorPart;

    private boolean initialSnapToGridValue;

    private double initialGridSpacingValue;

    private int initialRulerUnitValue;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        genericSetUp(SEMANTIC_MODEL_PATH, VSM_PATH, REPRESENTATIONS_MODEL_PATH);
        SessionUIManager.INSTANCE.createUISession(session);
    }

    @Override
    protected void tearDown() throws Exception {
        if (editorPart != null) {
            SessionUIManager.INSTANCE.getUISession(session).closeEditors(false, Collections.singleton((DDiagramEditor) editorPart));
        }
        TestsUtil.emptyEventsFromUIThread();
        super.tearDown();
    }

    /**
     * Makes sure that activating the Snap to grid has no effect on the ELK layout.
     */
    public void testArrangeWithSnapToWithELK() {
        // We create a new diagram
        EObject root = session.getSemanticResources().stream().findFirst().get().getContents().get(0);
        DRepresentation representation = createRepresentation("SimpleDiagram", root);
        // We open the editor and set the preferences for the test.
        IEditorPart newEditorPart = DialectUIManager.INSTANCE.openEditor(session, representation, new NullProgressMonitor());
        IPreferenceStore workspaceViewerPreferenceStore = ((DiagramGraphicalViewer) ((DiagramEditor) newEditorPart).getDiagramGraphicalViewer()).getWorkspaceViewerPreferenceStore();
        changeSnapToPreferences(workspaceViewerPreferenceStore);
        try {
            arrangeAll((DiagramEditor) newEditorPart);
            TestsUtil.synchronizationWithUIThread();
            // We keep the figures bounds after the arrange all without the Snap to grid.
            Map<DNode, Rectangle> DNodes2Bounds = computeNodesBounds(representation);
            // We activate the Snap to Grid.
            workspaceViewerPreferenceStore.setValue(WorkspaceViewerProperties.SNAPTOGRID, true);
            // We perform the arrange all again.
            arrangeAll((DiagramEditor) newEditorPart);
            TestsUtil.synchronizationWithUIThread();
            // We check that the layout did not change
            Map<DNode, Rectangle> afterDNodes2Bounds = computeNodesBounds(representation);
            afterDNodes2Bounds.forEach((dNode, rect) -> {
                assertEquals("The layout should not change after having activated the snap to grid with ELK algorithm.", DNodes2Bounds.get(dNode), rect);
            });
        } finally {
            restoreInitilaPreferences(workspaceViewerPreferenceStore);
        }
    }

    /**
     * Makes sure that activating the Snap to grid has effect on diagram without ELK algorithm
     */
    public void testArrangeWithSnapToWithoutELK() {
        // We create a new diagram
        EObject root = session.getSemanticResources().stream().findFirst().get().getContents().get(0);
        DRepresentation representation = createRepresentation("SimpleDiagramNoELK", root);
        // We open the editor and set the preferences for the test.
        IEditorPart newEditorPart = DialectUIManager.INSTANCE.openEditor(session, representation, new NullProgressMonitor());
        IPreferenceStore workspaceViewerPreferenceStore = ((DiagramGraphicalViewer) ((DiagramEditor) newEditorPart).getDiagramGraphicalViewer()).getWorkspaceViewerPreferenceStore();
        changeSnapToPreferences(workspaceViewerPreferenceStore);
        try {
            arrangeAll((DiagramEditor) newEditorPart);
            TestsUtil.synchronizationWithUIThread();
            // We keep the figures bounds after the arrange all without the Snap to grid.
            Map<DNode, Rectangle> dNodes2Bounds = computeNodesBounds(representation);
            // We activate the Snap to Grid.
            workspaceViewerPreferenceStore.setValue(WorkspaceViewerProperties.SNAPTOGRID, true);
            // We perform the arrange all again.
            arrangeAll((DiagramEditor) newEditorPart);
            TestsUtil.synchronizationWithUIThread();
            // We check that the layout has changed
            Map<DNode, Rectangle> afterDNodes2Bounds = computeNodesBounds(representation);
            boolean atLeastOneElementHasChanged = false;
            for (Iterator<Entry<DNode, Rectangle>> iterator = afterDNodes2Bounds.entrySet().iterator(); iterator.hasNext();) {
                Entry<DNode, Rectangle> dNodeToRect = iterator.next();
                if (!dNodeToRect.getValue().equals(dNodes2Bounds.get(dNodeToRect.getKey()))) {
                    atLeastOneElementHasChanged = true;
                    break;
                }
            }
            assertTrue("The activation of the Snap to grid should have changed the layout", atLeastOneElementHasChanged);
        } finally {
            restoreInitilaPreferences(workspaceViewerPreferenceStore);
        }
    }

    private void restoreInitilaPreferences(IPreferenceStore workspaceViewerPreferenceStore) {
        workspaceViewerPreferenceStore.setValue(WorkspaceViewerProperties.SNAPTOGRID, initialSnapToGridValue);
        workspaceViewerPreferenceStore.setValue(WorkspaceViewerProperties.GRIDSPACING, initialGridSpacingValue);
        workspaceViewerPreferenceStore.setValue(WorkspaceViewerProperties.RULERUNIT, initialRulerUnitValue);
    }

    private void changeSnapToPreferences(IPreferenceStore workspaceViewerPreferenceStore) {
        initialSnapToGridValue = workspaceViewerPreferenceStore.getBoolean(WorkspaceViewerProperties.SNAPTOGRID);
        initialGridSpacingValue = workspaceViewerPreferenceStore.getDouble(WorkspaceViewerProperties.GRIDSPACING);
        initialRulerUnitValue = workspaceViewerPreferenceStore.getInt(WorkspaceViewerProperties.RULERUNIT);
        workspaceViewerPreferenceStore.setValue(WorkspaceViewerProperties.SNAPTOGRID, false);
        workspaceViewerPreferenceStore.setValue(WorkspaceViewerProperties.GRIDSPACING, 100.0);
        workspaceViewerPreferenceStore.setValue(WorkspaceViewerProperties.RULERUNIT, RulerProvider.UNIT_PIXELS);
    }

    private Map<DNode, Rectangle> computeNodesBounds(DRepresentation representation) {
        Map<DNode, Rectangle> dNodes2Bounds = new HashMap<>();
        ((DDiagram) representation).getNodes().stream().forEach(dNode -> {
            IGraphicalEditPart editPart = getEditPart(dNode);
            dNodes2Bounds.put(dNode, editPart.getFigure().getBounds().getCopy());
        });
        return dNodes2Bounds;
    }

    protected void openDiagram(String diagramName) {
        diagram = (DDiagram) getRepresentationsByName(diagramName).toArray()[0];
        editorPart = (IDiagramWorkbenchPart) DialectUIManager.INSTANCE.openEditor(session, diagram, new NullProgressMonitor());
        TestsUtil.synchronizationWithUIThread();
    }

    private void arrangeAll(final DiagramEditor editorPart) {
        ArrangeRequest arrangeRequest = new ArrangeRequest(ActionIds.ACTION_ARRANGE_ALL);
        arrangeRequest.setPartsToArrange(Collections.singletonList(editorPart));
        editorPart.getDiagramEditPart().performRequest(arrangeRequest);
        TestsUtil.synchronizationWithUIThread();
    }
}