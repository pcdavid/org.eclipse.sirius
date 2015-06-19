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
package org.eclipse.sirius.business.internal.session;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.business.api.session.Session;

import com.google.common.base.Preconditions;

/**
 * Wraps a {@code SessionIOHandler} and logs requests, with time measures and he
 * invoking thread.
 * 
 * @author pcdavid
 *
 */
public class LoggingSessionIOHandler implements SessionIOHandler {
    private final SessionIOHandler delegate;

    private final PrintStream out;

    private final AtomicInteger level = new AtomicInteger(0);

    /**
     * Creates a new logging handler that wraps the specified concrect handler
     * and logs to stderr.
     * 
     * @param delegate
     *            the actual handler to delegate to.
     */
    public LoggingSessionIOHandler(SessionIOHandler delegate) {
        this.delegate = Preconditions.checkNotNull(delegate);
        this.out = System.err;
    }

    @Override
    public void load(Resource resource, Map<?, ?> options) throws IOException {
        int currentLevel = level.incrementAndGet();
        String message = "load(" + resource.getURI() + ", loadOptions)";
        logStart(currentLevel, message);
        long start = System.nanoTime();
        delegate.load(resource, options);
        level.decrementAndGet();
        logFinish(System.nanoTime() - start, currentLevel, message);
    }

    @Override
    public void load(Resource resource, InputStream inputStream, Map<?, ?> options) throws IOException {
        int currentLevel = level.incrementAndGet();
        String message = "load(" + resource.getURI() + ", inputStream, loadOptions)";
        logStart(currentLevel, message);
        long start = System.nanoTime();
        delegate.load(resource, inputStream, options);
        level.decrementAndGet();
        logFinish(System.nanoTime() - start, currentLevel, message);
    }

    @Override
    public void save(Resource resource, Map<?, ?> options) throws IOException {
        int currentLevel = level.incrementAndGet();
        String message = "save(" + resource.getURI() + ", saveOptions)";
        logStart(currentLevel, message);
        long start = System.nanoTime();
        delegate.save(resource, options);
        level.decrementAndGet();
        logFinish(System.nanoTime() - start, currentLevel, message);
    }

    @Override
    public void save(Resource resource, OutputStream outputStream, Map<?, ?> options) throws IOException {
        int currentLevel = level.incrementAndGet();
        String message = "save(" + resource.getURI() + ", outputStream, saveOptions)";
        logStart(currentLevel, message);
        long start = System.nanoTime();
        delegate.save(resource, outputStream, options);
        level.decrementAndGet();
        logFinish(System.nanoTime() - start, currentLevel, message);
    }

    @Override
    public void unload(Resource resource) {
        int currentLevel = level.incrementAndGet();
        String message = "unload(" + resource.getURI() + ")";
        logStart(currentLevel, message);
        long start = System.nanoTime();
        delegate.unload(resource);
        level.decrementAndGet();
        logFinish(System.nanoTime() - start, currentLevel, message);
    }

    @Override
    public void delete(Resource resource, Map<?, ?> options) throws IOException {
        int currentLevel = level.incrementAndGet();
        String message = "delete(" + resource.getURI() + ", deleteOptions)";
        logStart(currentLevel, message);
        long start = System.nanoTime();
        delegate.delete(resource, options);
        level.decrementAndGet();
        logFinish(System.nanoTime() - start, currentLevel, message);
    }

    @Override
    public void execute(Session session, Runnable task) {
        int currentLevel = level.incrementAndGet();
        String message = "execute(" + session + ", task)";
        logStart(currentLevel, message);
        long start = System.nanoTime();
        delegate.execute(session, task);
        level.decrementAndGet();
        logFinish(System.nanoTime() - start, currentLevel, message);
    }

    private void logStart(int currentLevel, String message) {
        out.printf("[%s] %s -> %s\n", Thread.currentThread().getName(), indent(currentLevel), message);
    }

    private void logFinish(long durationNanos, int currentLevel, String message) {
        out.printf("[%s] %s <- %s [took %dms]\n", Thread.currentThread().getName(), indent(currentLevel), message, TimeUnit.NANOSECONDS.toMillis(durationNanos));
    }

    private String indent(int n) {
        StringBuilder indent = new StringBuilder();
        for (int i = 1; i < n; i++) {
            indent.append("  ");
        }
        return indent.toString();
    }

}
