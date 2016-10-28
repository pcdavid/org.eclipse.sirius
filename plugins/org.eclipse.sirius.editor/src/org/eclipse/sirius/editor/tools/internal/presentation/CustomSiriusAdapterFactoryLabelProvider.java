/*******************************************************************************
 * Copyright (c) 2011 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.editor.tools.internal.presentation;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.sirius.common.tools.api.util.StringUtil;
import org.eclipse.sirius.viewpoint.description.IdentifiedElement;

/**
 * @author <a href="mailto:julien.dupont@obeo.fr">Julien DUPONT</a>
 * 
 */
class CustomSiriusAdapterFactoryLabelProvider extends AdapterFactoryLabelProvider {
    private boolean showTypes;

    public CustomSiriusAdapterFactoryLabelProvider(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    @Override
    public String getText(Object object) {
        String result = super.getText(object);
        if (object instanceof IdentifiedElement) {
            IdentifiedElement elt = (IdentifiedElement) object;
            String label = addDefaultTranslationIfAvailable(elt, elt.getLabel());
            if (!StringUtil.isEmpty(label)) {
                result = label;
            }
        }
        if (object instanceof EObject && showTypes) {
            result = String.format("<%s> %s", ((EObject) object).eClass().getName(), result);
        }
        return result;
    }

    /**
     * @return the showTypes
     */
    public boolean isShowTypes() {
        return showTypes;
    }

    /**
     * @param showTypes
     *            the showTypes to set
     */
    public void setShowTypes(boolean showTypes) {
        this.showTypes = showTypes;
    }

    private String addDefaultTranslationIfAvailable(IdentifiedElement elt, String key) {
        Resource res = elt.eResource();
        if (isTranslationKey(key) && isModelInWorkspaceProject(res)) {
            IFile translationsFile = getTranslationFileFor(res);
            if (translationsFile.exists()) {
                Properties p = new Properties();
                try (FileInputStream inStream = new FileInputStream(translationsFile.getLocation().toFile())) {
                    p.load(inStream);
                    String translation = p.getProperty(key.substring(1));
                    if (translation != null) {
                        return String.format("%s [%s]", translation, key); //$NON-NLS-1$
                    }
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
        return key;
    }

    private boolean isModelInWorkspaceProject(Resource res) {
        return res != null && res.getURI().isPlatformResource() && res.getURI().segmentCount() > 1;
    }

    private boolean isTranslationKey(String key) {
        return key != null && key.startsWith("%");
    }

    private IFile getTranslationFileFor(Resource res) {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(res.getURI().segment(1)).getFile("plugin.properties"); //$NON-NLS-1$
    }
}
