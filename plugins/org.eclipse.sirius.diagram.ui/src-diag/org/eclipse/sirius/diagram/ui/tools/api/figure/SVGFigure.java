/**
 * Copyright (c) 2008, 2017 Borland Software Corporation and others.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Dmitry Stadnik - initial API and implementation
 *    Obeo - Adaptations.
 */
package org.eclipse.sirius.diagram.ui.tools.api.figure;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.WeakHashMap;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.sirius.common.tools.api.util.StringUtil;
import org.eclipse.sirius.diagram.DiagramPlugin;
import org.eclipse.sirius.diagram.ui.provider.DiagramUIPlugin;
import org.eclipse.sirius.diagram.ui.provider.Messages;
import org.eclipse.sirius.diagram.ui.tools.internal.figure.svg.SimpleImageTranscoder;
import org.eclipse.sirius.diagram.ui.tools.internal.render.ImageCache;
import org.eclipse.sirius.ext.draw2d.figure.ITransparentFigure;
import org.eclipse.sirius.ext.draw2d.figure.ImageFigureWithAlpha;
import org.eclipse.sirius.ext.draw2d.figure.StyledFigure;
import org.eclipse.sirius.ext.draw2d.figure.TransparentFigureGraphicsModifier;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

//CHECKSTYLE:OFF
public class SVGFigure extends Figure implements StyledFigure, ITransparentFigure, ImageFigureWithAlpha {
    private static final ImageCache CACHE = new ImageCache();

    private static final boolean CACHE_ENABLED = true;

    private static final boolean CACHE_SCALED_IMAGES = true;

    /**
     * The uri of the image to display when the file has not been found.
     */
    protected static final String IMAGE_NOT_FOUND_URI = MessageFormat.format("platform:/plugin/{0}/images/NotFound.svg", DiagramUIPlugin.getPlugin().getSymbolicName()); //$NON-NLS-1$

    /**
     * Key separator.
     */
    protected static final String SEPARATOR = "|"; //$NON-NLS-1$

    private String uri;

    private int viewpointAlpha = ITransparentFigure.DEFAULT_ALPHA;

    private boolean transparent;

    private boolean failedToLoadDocument;

    private SimpleImageTranscoder transcoder;

    /** The image ratio (width/height); computed during a SVG change detected {@link #contentChanged()}. */
    private double initialAspectRatio = 1;

    /**
     * True if the SVG document contains an attribute "viewBox", false otherwise. This attribute is not handled by the
     * current version of batik used by Sirius (1.6). And it causes regression, shrinked image, since the fix of bug
     * 463051.<BR>
     * This field is set at each {@link #contentChanged()}.
     */
    protected boolean modeWithViewBox;

    protected static WeakHashMap<String, Document> documentsMap = new WeakHashMap<String, Document>();

    public SVGFigure() {
        this.setLayoutManager(new XYLayout());
    }

    @Override
    public int getSiriusAlpha() {
        return viewpointAlpha;
    }

    @Override
    public boolean isTransparent() {
        return transparent;
    }

    @Override
    public void setSiriusAlpha(int alpha) {
        this.viewpointAlpha = alpha;
    }

    @Override
    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }

    @Override
    public int getImageHeight() {
        return (transcoder != null) ? transcoder.getImageHeight() : 0;
    }

    @Override
    public int getImageWidth() {
        return (transcoder != null) ? transcoder.getImageWidth() : 0;
    }

    /**
     * Get the original SVG image ratio (width/height).
     *
     * @return the original SVG image ratio (width/height).
     */
    public double getImageAspectRatio() {
        return initialAspectRatio;
    }

    @Override
    public int getImageAlphaValue(int x, int y) {
        return (transcoder != null) ? transcoder.getImageAlphaValue(x, y) : 255;
    }

    public final String getURI() {
        return uri;
    }

    public final void setURI(String uri) {
        setURI(uri, true);
    }

    public void setURI(String uri, boolean loadOnDemand) {
        this.uri = uri;
        transcoder = null;
        failedToLoadDocument = false;
        if (loadOnDemand) {
            loadDocument();
        }
    }

    private void loadDocument() {
        transcoder = null;
        failedToLoadDocument = true;
        if (uri == null) {
            return;
        }

        String documentKey = getDocumentKey();
        Document document;
        if (documentsMap.containsKey(documentKey)) {
            document = documentsMap.get(documentKey);
        } else {
            document = createDocument();
            documentsMap.put(documentKey, document);
        }

        if (document != null) {
            transcoder = new SimpleImageTranscoder(document);
            failedToLoadDocument = false;
        }
    }

    private Document createDocument() {
        String parser = Optional.ofNullable(XMLResourceDescriptor.getXMLParserClassName()).orElse("org.apache.xerces.parsers.SAXParser"); //$NON-NLS-1$
        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
        return createDocument(factory, false);
    }

    private Document createDocument(SAXSVGDocumentFactory factory, boolean forceClassLoader) {
        if (Messages.BundledImageShape_idMissing.equals(uri)) {
            DiagramPlugin.getDefault().logError(Messages.SVGFigure_usingInvalidBundledImageShape);
        } else {
            if (forceClassLoader) {
                Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            }
            try {
                return factory.createDocument(uri);
            } catch (IOException e) {
                boolean saxParserNotFound = e.getMessage() != null && e.getMessage().contains("SAX2 driver class org.apache.xerces.parsers.SAXParser not found"); //$NON-NLS-1$
                if (!forceClassLoader && saxParserNotFound) {
                    return createDocument(factory, true);
                } else {
                    DiagramPlugin.getDefault().logError(MessageFormat.format(Messages.SVGFigure_loadError, uri), e);
                }
            } finally {
                if (forceClassLoader) {
                    Thread.currentThread().setContextClassLoader(null);
                }
            }
        }
        return null;
    }

    protected final Document getDocument() {
        if (failedToLoadDocument) {
            return null;
        }
        if (transcoder == null) {
            loadDocument();
        }
        return transcoder == null ? null : transcoder.getDocument();
    }

    /**
     * Should be called when SVG document has been changed. It will be
     * re-rendered and figure will be repainted.
     */
    public void contentChanged() {
        Document document = getDocument();
        if (document != null) {
            modeWithViewBox = false;
            for (int i = 0; i < document.getChildNodes().getLength(); i++) {
                org.w3c.dom.Node node = document.getChildNodes().item(i);
                if (node instanceof Element) {
                    String viewBoxValue = ((Element) node).getAttribute("viewBox"); //$NON-NLS-1$
                    if (!StringUtil.isEmpty(viewBoxValue)) {
                        // stretch the image is not supported as the current version of Batif used does not handled it
                        // (org.apache.batik.dom.svg.SVGOMSVGElement.getViewBox()).
                        modeWithViewBox = true;
                    }
                }

            }
        }
        if (transcoder != null) {
            transcoder.contentChanged();
            // Each time that SVG document is changed, we store the real ratio of the image (width/height); the transcoder aspect ratio. Indeed, after the transcoder aspect ratio will be
            // the previous displayed ratio and not the real ratio of the image.
            initialAspectRatio = transcoder.getAspectRatio();
        }
        repaint();
    }

    protected SimpleImageTranscoder getTranscoder() {
        return transcoder;
    }

    /**
     * The key used to store the document.
     * 
     * @return the key.
     */
    protected String getDocumentKey() {
        return uri;
    }

    /**
     * Compute a key for this figure. This key is used to store in cache the
     * corresponding {@link org.eclipse.swt.graphics.Image}.
     *
     * The key must begin by the document key.
     *
     * @return The key corresponding to this BundleImageFigure.
     */
    protected String getKey(Graphics graphics) {
        int aaText = SWT.DEFAULT;
        try {
            aaText = graphics.getTextAntialias();
        } catch (Exception e) {
            // not supported
        }

        StringBuffer result = new StringBuffer();
        result.append(getDocumentKey());
        result.append(SVGFigure.SEPARATOR);
        result.append(getSiriusAlpha());
        result.append(SVGFigure.SEPARATOR);
        result.append(aaText);
        result.append(SVGFigure.SEPARATOR);
        Rectangle r = new PrecisionRectangle(getClientArea());
        if (CACHE_SCALED_IMAGES && graphics != null) {
            r.performScale(graphics.getAbsoluteScale());
        }
        result.append(r.width());
        result.append(SVGFigure.SEPARATOR);
        result.append(r.height());

        return result.toString();
    }

    @Override
    protected void paintFigure(Graphics graphics) {
        TransparentFigureGraphicsModifier modifier = new TransparentFigureGraphicsModifier(this, graphics);
        modifier.pushState();
        Rectangle svgArea = getClientArea();
        if (CACHE_SCALED_IMAGES) {
            Rectangle scaledArea = new PrecisionRectangle(svgArea);
            scaledArea.performScale(graphics.getAbsoluteScale());
            Image image = getImage(svgArea, graphics);
            if (image != null) {
                synchronized (image) {
                    if (!image.isDisposed()) {
                        if (modeWithViewBox) {
                            graphics.drawImage(image, 0, 0, scaledArea.width(), scaledArea.height(), svgArea.x(), svgArea.y(), svgArea.width(), svgArea.height());
                        } else {
                            // The scaled width (and height) must not be greater than area width (and height). So
                            // depending
                            // on the initialAspectRatio and zoom factor, the reference can be the width or the height.
                            double scaledWidth = svgArea.width() * graphics.getAbsoluteScale();
                            double scaledHeight = scaledWidth / getImageAspectRatio();
                            if ((scaledHeight / graphics.getAbsoluteScale()) > svgArea.height()) {
                                // The height must be the reference to avoid an IllegalArgumentException later.
                                scaledHeight = svgArea.height() * graphics.getAbsoluteScale();
                                scaledWidth = scaledHeight * getImageAspectRatio();
                            }
                            graphics.drawImage(image, 0, 0, (int) scaledWidth, (int) scaledHeight, svgArea.x(), svgArea.y(), svgArea.width(), svgArea.height());
                        }
                    }
                }
            }
        } else {
            Image image = getImage(svgArea, graphics);
            if (image != null) {
                synchronized (image) {
                    if (!image.isDisposed()) {
                        graphics.drawImage(image, svgArea.x(), svgArea.y());
                    }
                }
            }
        }
        modifier.popState();
    }

    /**
     * Get the image cached or create new one and cache it.
     *
     * @param clientArea
     *            the client area
     * @param graphics
     *            the graphical context
     * @return an image store in a cache
     */
    protected Image getImage(Rectangle clientArea, Graphics graphics) {
        if (CACHE_ENABLED) {
            return CACHE.getImage(this.getKey(graphics), () -> {
                if (this.transcoder != null) {
                    return Optional.of(this.transcoder.render(this, clientArea, graphics, CACHE_SCALED_IMAGES));
                } else {
                    return Optional.empty();
                }
            });
        } else if (transcoder != null) {
            return transcoder.render(this, clientArea, graphics, CACHE_SCALED_IMAGES);
        } else {
            return null;
        }
    }

    /**
     * Remove all entries whose key begins with the given key. Remove from the
     * document map, the entries with the given keys to force to re-read the
     * file.
     *
     * @param documentKey
     *            the document key.
     * @return true of something was removed.
     */
    protected static boolean doRemoveFromCache(final String documentKey) {
        if (!StringUtil.isEmpty(documentKey)) {
            return CACHE.doRemoveFromCache(documentKey) || SVGFigure.documentsMap.remove(documentKey) != null;
        }
        return false;
    }

    // CHECKSTYLE:ON
}
