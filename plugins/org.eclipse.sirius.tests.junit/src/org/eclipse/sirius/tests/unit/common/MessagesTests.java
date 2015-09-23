/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tests.unit.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import org.eclipse.sirius.ext.base.I18N.TranslatableMessage;
import org.eclipse.sirius.tests.SiriusTestsPlugin;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

/**
 * Performs some consistency checks on I18N messages.
 * 
 * @author pcdavid
 */
public class MessagesTests {

    @Test
    public void all_sirius_plugins_have_messages_class() {
        BundleContext context = SiriusTestsPlugin.getDefault().getBundle().getBundleContext();
        for (Bundle bundle : context.getBundles()) {
            String bundleName = bundle.getSymbolicName();
            if (bundleName.startsWith("org.eclipse.sirius")
                    && !(bundleName.startsWith("org.eclipse.sirius.editor") || bundleName.startsWith("org.eclipse.sirius.tests") || bundleName.startsWith("org.eclipse.sirius.sample"))) {
                String activatorClassName = bundle.getHeaders().get("Bundle-Activator");
                if (activatorClassName != null) {
                    List<String> segments = Lists.newArrayList(Splitter.on(".").splitToList(activatorClassName));
                    segments.set(segments.size() - 1, "Messages");
                    String messagesClassName = Joiner.on(".").join(segments);
                    try {
                        Class<?> messagesClass = bundle.loadClass(messagesClassName);
                        for (Field field : messagesClass.getDeclaredFields()) {
                            if (isMessageField(field)) {
                                try {
                                    String value = (String) field.get(null);
                                    if (value == null || value.isEmpty()) {
                                        System.err.println(messagesClassName + "." + field.getName() + " is null");
                                    } else {
                                        System.out.println(messagesClassName + "." + field.getName() + "=" + value);
                                    }
                                } catch (IllegalArgumentException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        System.err.println("Bundle " + bundleName + " should have a class named " + messagesClassName);
                    }
                }
            }
        }
    }

    private boolean isMessageField(Field field) {
        int mods = field.getModifiers();
        return Modifier.isPublic(mods) && Modifier.isStatic(mods) && field.getType().equals(String.class) && field.getAnnotation(TranslatableMessage.class) != null;
    }

}
