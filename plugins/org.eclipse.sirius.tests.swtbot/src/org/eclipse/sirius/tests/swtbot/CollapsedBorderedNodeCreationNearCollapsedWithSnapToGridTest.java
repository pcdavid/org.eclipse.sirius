/*******************************************************************************
 * Copyright (c) 2010, 2017 THALES GLOBAL SERVICES.
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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScalableFigure;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.sirius.diagram.ui.edit.internal.part.PortLayoutHelper;
import org.eclipse.sirius.ext.gmf.runtime.editparts.GraphicalHelper;

/**
 * Same tests as {@link CollapsedBorderedNodeCreationNearCollapsedTest} but with
 * snapToGrid enabled.
 * 
 * @author <a href="mailto:laurent.redor@obeo.fr">Laurent Redor</a>
 */
public class CollapsedBorderedNodeCreationNearCollapsedWithSnapToGridTest extends CollapsedBorderedNodeCreationNearCollapsedTest {

    /**
     * The grid spacing in pixels.
     */
    private static final int GRID_SPACING = 100;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        editor.setSnapToGrid(true, GRID_SPACING, 2);
    }

    @Override
    protected Point adaptExpectedLocation(IFigure parentFigure, Point parentAbsoluteLocation, Point absoluteExpectedLocation) {
        // Do not adapt expected location this is done later in
        // #assertSameLocation(String, Point, Point, Point, Point,
        // IGraphicalEditPart) method.
        // This allows to have in this method the expected location for expanded
        // state of border node.
        return absoluteExpectedLocation.getCopy();

    }

    /**
     * Only one of the coordinates is snap to grid. The other is constrained by
     * the parent border.
     * {@link #assertSameLocation(String, Point, Point, Point, Point, IGraphicalEditPart)}
     * 
     * @see org.eclipse.sirius.tests.swtbot.BorderedNodeCreationTest#assertSameLocation(String,
     *      Point, Point, Point, Point, IGraphicalEditPart)
     */
    @Override
    protected void assertSameLocation(String errorMessage, Point absoluteExpectedLocation, Point nodeLocation, Point parentAbsoluteLocation, Point screenCreationLocation, IGraphicalEditPart parentPart) {
        Point snapToLocation = new Point();
        if (screenCreationLocation == null) {
            // Adapt the expected location to the grid
            snapToLocation = editor.adaptLocationToSnap(absoluteExpectedLocation);
        } else {
            snapToLocation = new PrecisionPoint(screenCreationLocation);
            addDiagramScrollbar(parentPart.getFigure(), snapToLocation);
            considerZoom(parentPart.getFigure(), snapToLocation);
            snapToLocation = editor.adaptLocationToSnap(snapToLocation);
        }
        // Adapt the expected location to the scrollbar of the parents
        Point absoluteSnapToLocation = new Point(snapToLocation);
        if (parentPart != null) {
            addParentScrollbar(parentPart.getFigure(), absoluteSnapToLocation);
        }

        if (createCollapsedBorderedNode) {
            // Adapt the expected location to collapsed one.
            absoluteExpectedLocation = PortLayoutHelper.getCollapseCandidateLocation(new Dimension(1, 1), new Rectangle(absoluteExpectedLocation.x, absoluteExpectedLocation.y, 10, 10),
                    new Rectangle(parentAbsoluteLocation, parentPart.getFigure().getSize())).getTopLeft();
            // Adapt the snap location to collapsed one.
            absoluteSnapToLocation = PortLayoutHelper.getCollapseCandidateLocation(new Dimension(1, 1), new Rectangle(absoluteSnapToLocation.x, absoluteSnapToLocation.y, 10, 10),
                    new Rectangle(parentAbsoluteLocation, parentPart.getFigure().getSize())).getTopLeft();
        }

        if (nodeLocation.x == parentAbsoluteLocation.x + 8 + 4) {
            // + 8 for regular shift and + 4 for collapsed border node
            errorMessage += " expected <Point(" + nodeLocation.x + ", " + absoluteExpectedLocation.y + ")> or <Point(" + nodeLocation.x + ", " + absoluteExpectedLocation.y + ")> but was:"
                    + nodeLocation;
            assertTrue(errorMessage, nodeLocation.y == absoluteSnapToLocation.y || nodeLocation.y == absoluteExpectedLocation.y);
        } else if (nodeLocation.y == parentAbsoluteLocation.y + 4) {
            // The y axis is the same as parent (plus the 4 pixels for collapsed
            // border node). Check that this axis also
            // corresponds to the grid. The x axis is constrained by the border
            // (east or west).
            errorMessage += " expected <Point(" + nodeLocation.x + ", " + absoluteSnapToLocation.y + ")> but was:" + nodeLocation;
            assertTrue(errorMessage, nodeLocation.y == absoluteSnapToLocation.y);
        } else {
            // Get the absolute bounds of parent
            Rectangle parentBounds = GraphicalHelper.getAbsoluteBoundsIn100Percent(parentPart, true);

            errorMessage += " At least x or y must be on the grid (grid spacing = " + GRID_SPACING + "), but was: " + nodeLocation + "for parent: " + parentBounds;

            assertTrue(errorMessage,
                    ((nodeLocation.x - 4) % GRID_SPACING == 0
                            && (nodeLocation.y == absoluteExpectedLocation.y || nodeLocation.y == parentAbsoluteLocation.y + 1 || nodeLocation.y == parentBounds.bottom() - 2))
                            || ((nodeLocation.x == absoluteExpectedLocation.x || nodeLocation.x == parentAbsoluteLocation.x + 1 || nodeLocation.x == parentBounds.right() - 2)
                                    && (nodeLocation.y - 4) % GRID_SPACING == 0));
        }
    }

    /**
     * Add scrolls of parent (except the diagram scroll)
     * 
     * @param figure
     *            the actual figure level
     * @param location
     *            a location in absolute coordinates
     */
    private static void addParentScrollbar(final IFigure figure, final Point location) {
        if (figure instanceof Viewport && figure.getParent() != null && figure.getParent().getParent() != null) {
            location.performTranslate(((Viewport) figure).getHorizontalRangeModel().getValue(), ((Viewport) figure).getVerticalRangeModel().getValue());
        }
        if (figure.getParent() != null) {
            addParentScrollbar(figure.getParent(), location);
        }
    }

    /**
     * Add scrolls of diagram
     * 
     * @param figure
     *            the actual figure level
     * @param location
     *            a location
     */
    private static void addDiagramScrollbar(final IFigure figure, final Point location) {
        if (figure instanceof Viewport && figure.getParent() != null && figure.getParent().getParent() == null) {
            location.performTranslate(((Viewport) figure).getHorizontalRangeModel().getValue(), ((Viewport) figure).getVerticalRangeModel().getValue());
        }
        if (figure.getParent() != null) {
            addDiagramScrollbar(figure.getParent(), location);
        }
    }

    /**
     * Add scrolls of diagram
     * 
     * @param figure
     *            the actual figure level
     * @param location
     *            a location
     */
    private static void considerZoom(final IFigure figure, final Point location) {
        if (figure instanceof ScalableFigure) {
            location.performScale(1 / ((ScalableFigure) figure).getScale());
        }
        if (figure.getParent() != null) {
            considerZoom(figure.getParent(), location);
        }
    }
}
