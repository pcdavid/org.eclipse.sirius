/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tests.unit.common;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.factory.SessionFactory;
import org.eclipse.sirius.tests.SiriusTestsPlugin;
import org.eclipse.sirius.tests.support.api.EclipseTestsSupportHelper;
import org.junit.BeforeClass;
import org.junit.Test;

public class SessionLeakageTest {
    private static final int ITERATION = 500;

    @BeforeClass
    public static void importProjectFixture() {
        EclipseTestsSupportHelper helper = EclipseTestsSupportHelper.INSTANCE;
        helper.createProject("extlibrary");
        helper.copyFile(SiriusTestsPlugin.PLUGIN_ID, "data/unit/extlibrary/extlibrary.ecore", "extlibrary/extlibrary.ecore");
        helper.copyFile(SiriusTestsPlugin.PLUGIN_ID, "data/unit/extlibrary/extlibrary.aird", "extlibrary/extlibrary.aird");
    }

    @Test
    public void openAndClodeSessionManyTimes_shouldNotLeakMemory() throws CoreException {
        System.out.printf("Before first session open: %s\n", readFreeMemory());
        openAndCloseSession("/extlibrary/extlibrary.aird");
        System.out.printf("After first session open: %s\n", readFreeMemory());
        for (int i = 0; i < ITERATION; i++) {
            openAndCloseSession("/extlibrary/extlibrary.aird");
            if (i % 25 == 0) {
                System.gc();
            }
        }
        System.out.printf("After %d iterations: %s\n", SessionLeakageTest.ITERATION, readFreeMemory());
    }

    private void openAndCloseSession(String path) throws CoreException {
        IProgressMonitor pm = new NullProgressMonitor();
        Session session = SessionFactory.INSTANCE.createSession(URI.createPlatformResourceURI(path, true), pm);
        session.open(pm);
        session.close(pm);
    }

    private String readFreeMemory() {
        System.gc();
        long freeMem = Runtime.getRuntime().freeMemory();
        return humanReadableByteCount(freeMem, false);
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
