/*******************************************************************************
 * Copyright (c) 2007, 2016 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.diagram.ui.tools.api.figure;

import java.io.File;
import java.net.MalformedURLException;

import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.sirius.common.tools.api.resource.FileProvider;
import org.eclipse.sirius.diagram.ContainerStyle;
import org.eclipse.sirius.diagram.WorkspaceImage;
import org.eclipse.sirius.diagram.ui.provider.DiagramUIPlugin;
import org.eclipse.sirius.diagram.ui.tools.api.image.DiagramImagesPath;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

/**
 * The {@link WorkspaceImageFigure} is useful to load images using a cache. The
 * image can be in the workspace, or if it's not found in the workspace it will
 * be looked up in the plug-ins.
 * 
 * @author cbrun
 * 
 */
public class WorkspaceImageFigure extends AbstractTransparentImage implements IWorkspaceImageFigure {

    private double imageAspectRatio = 1.0;

    private boolean keepAspectRatio = true;

    /**
     * Create a new {@link WorkspaceImageFigure}.
     * 
     * @param flyWeightImage
     *            an image instance.
     */
    public WorkspaceImageFigure(final Image flyWeightImage) {
        super(flyWeightImage);
        imageAspectRatio = (double) flyWeightImage.getBounds().width / flyWeightImage.getBounds().height;
        minSize = new Dimension(0, 0);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.draw2d.Figure#setSize(int, int)
     */
    @Override
    public void setSize(final int w, final int h) {
        if (keepAspectRatio) {
            final int newHeight = (int) (w / imageAspectRatio);
            super.setSize(w, newHeight);
        } else {
            super.setSize(w, h);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.draw2d.Figure#setMaximumSize(org.eclipse.draw2d.geometry.Dimension)
     */
    @Override
    public void setMaximumSize(final Dimension d) {
        super.setMaximumSize(this.getSize());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.draw2d.Figure#setMinimumSize(org.eclipse.draw2d.geometry.Dimension)
     */
    @Override
    public void setMinimumSize(final Dimension d) {
        super.setMinimumSize(this.getSize());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.draw2d.Figure#setPreferredSize(org.eclipse.draw2d.geometry.Dimension)
     */
    @Override
    public void setPreferredSize(final Dimension size) {
        super.setPreferredSize(this.getSize());
    }

    /**
     * Get an {@link Image} instance. The image will be stored in a cache.
     * 
     * @param path
     *            the path is a "/project/file" path, if it's not found in the
     *            workspace, the class will look for the file in the plug-ins.
     * @return an image instance given the path.
     */
    public static Image flyWeightImage(final String path) {
        if (path != null) {
            ImageDescriptor desc = imageDescriptor(path);
            return WorkspaceImageFigure.flyWeightImage(desc);
        }
        return WorkspaceImageFigure.getImageNotFound();
    }

    /**
     * Get the dimension of an image file. You should prefer this method instead
     * of getting the {@link Image} instance itself and then retrieving its size
     * as this avoid the creation of a native resource which might create
     * deadlock situations if not done in the UI thread. See
     * https://bugs.eclipse.org/bugs/show_bug.cgi?id=265265
     * 
     * @param path
     *            the path is a "/project/file" path, if it's not found in the
     *            workspace, the class will look for the file in the plug-ins.
     * @return a {@link Dimension} instance having the width and height of the
     *         image or null if the image can't be found or
     *         loaded.
     */
    public static Dimension getImageBounds(final String path) {
        ImageDescriptor descriptor = imageDescriptor(path);
        if (descriptor != null) {
            ImageData imgData = descriptor.getImageData();
            if (imgData != null) {
                return new Dimension(imgData.width, imgData.height);
            }
        }
        return null;
    }

    private static ImageDescriptor imageDescriptor(final String path) {
        final File imageFile = WorkspaceFileResourceChangeListener.getInstance().getFileFromURI(path);
        ImageDescriptor desc = null;
        if (imageFile != null && WorkspaceFileResourceChangeListener.getInstance().getReadStatusOfFile(imageFile)) {
            try {
                desc = WorkspaceFileResourceChangeListener.getInstance().findImageDescriptor(imageFile);
            } catch (MalformedURLException e) {
                // do nothing
            }
        }
        return desc;
    }

    /**
     * Get an {@link Image} instance. The image will be stored in a cache.
     * 
     * @param desc
     *            the image descriptor
     * @return an image instance given the image descriptor.
     */
    public static Image flyWeightImage(final ImageDescriptor desc) {
        if (desc != null) {
            return DiagramUIPlugin.getPlugin().getImage(desc);
        } else {
            return WorkspaceImageFigure.getImageNotFound();
        }
    }

    private static Image getImageNotFound() {
        return DiagramUIPlugin.getPlugin().getImage(DiagramUIPlugin.getPlugin().findImageWithDimensionDescriptor(DiagramImagesPath.IMAGE_NOT_FOUND));
    }

    /**
     * Create an {@link WorkspaceImageFigure} instance from an image path.
     * 
     * @param path
     *            the path is a "/project/file" path, if it's not found in the
     *            workspace, the class will look for the file in the plug-ins.
     * @return an image instance given the path.
     */
    public static WorkspaceImageFigure createImageFigure(final String path) {
        final WorkspaceImageFigure fig = new WorkspaceImageFigure(WorkspaceImageFigure.flyWeightImage(path));
        return fig;
    }

    /**
     * Create an {@link WorkspaceImageFigure} instance from a workspace image.
     * 
     * @param wksImage
     *            : an instance of {@link WorkspaceImage}.
     * @return the image figure built using the {@link WorkspaceImage} object.
     */
    public static WorkspaceImageFigure createImageFigure(final WorkspaceImage wksImage) {
        final String path = wksImage.getWorkspacePath();
        return WorkspaceImageFigure.createImageFigure(path);

    }

    /**
     * Get the image aspect ratio.
     * 
     * @return the image aspect ratio
     */
    public double getImageAspectRatio() {
        return imageAspectRatio;
    }

    /**
     * Refresh the figure.
     * 
     * @param bundledImage
     *            the image associated to the figure
     */
    public void refreshFigure(final WorkspaceImage bundledImage) {
        final String path = bundledImage.getWorkspacePath();
        final Image image = WorkspaceImageFigure.flyWeightImage(path);
        if (!image.equals(this.getImage())) {
            this.setImage(WorkspaceImageFigure.flyWeightImage(path));
            this.repaint();
        }
    }

    /**
     * Refresh the figure.
     * 
     * @param containerStyle
     *            the style of the container
     */
    public void refreshFigure(final ContainerStyle containerStyle) {
        if (containerStyle instanceof WorkspaceImage) {
            WorkspaceImage workspaceImage = (WorkspaceImage) containerStyle;
            refreshFigure(workspaceImage);
        }
    }
}
