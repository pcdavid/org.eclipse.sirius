/*******************************************************************************
 * Copyright (c) 2016 Obeo.
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
package org.eclipse.sirius.common.tools.api.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.Objects;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.common.tools.DslCommonPlugin;
import org.eclipse.sirius.common.tools.Messages;
import org.osgi.framework.Bundle;

/**
 * A singleton helper dedicated to process a translatable String and return the message in the active language.
 *
 * @author <a href="mailto:steve.monnier@obeo.fr">Steve Monnier</a>
 *
 */
public class MessageTranslator {

    private static final class NullMessageTranslator extends MessageTranslator {
        NullMessageTranslator() {
            super(null);
        }

        @Override
        public String getMessage(String value) {
            return value;
        }
    }

    private static final String PERCENT = "%"; //$NON-NLS-1$

    private static final String EMPTY = ""; //$NON-NLS-1$

    private final Bundle context;

    private MessageTranslator(Bundle bundle) {
        this.context = bundle;
    }

    public static MessageTranslator forContext(Bundle b) {
        return new MessageTranslator(Objects.requireNonNull(b));
    }

    public static MessageTranslator forContext(EObject eObject) {
        if (eObject != null && eObject.eResource() != null) {
            URI uri = eObject.eResource().getURI();
            if (uri != null && uri.isPlatformPlugin() && uri.segmentCount() > 1) {
                // Segment 0 is the scheme
                // Segment 1 is the bundle
                Bundle bundle = Platform.getBundle(uri.segment(1));
                return forContext(bundle);
            }
        }
        return new NullMessageTranslator();
    }

    /**
     * Service dedicated to process a translatable String and return the message in the active language. If the String
     * is not a translatable one, it will not be processed but return it as is.
     *
     * @param value
     *            {@link String} value wrote in the VSM to process.
     * @return the available translation, or the value if not translatable.
     */
    public String getMessage(String value) {
        String result = value;
        if (result != null && result.startsWith(PERCENT)) {
            if (result != null && result.startsWith(PERCENT + PERCENT)) {
                return result.replaceFirst(PERCENT, EMPTY);
            }
            try {
                result = Platform.getResourceBundle(context).getString(result.substring(PERCENT.length()));
            } catch (MissingResourceException mre) {
                DslCommonPlugin.INSTANCE.log(new Status(IStatus.WARNING, context.getSymbolicName(), 0, MessageFormat.format(Messages.MessageTranslator_missingResourceMessage, value), mre));
            }
        }
        return result;
    }

}
