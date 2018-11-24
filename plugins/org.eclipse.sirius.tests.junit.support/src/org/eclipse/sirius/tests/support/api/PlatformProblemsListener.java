/*******************************************************************************
 * Copyright (c) 2018 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *      Obeo - Initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tests.support.api;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collection;
import java.util.Map.Entry;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.junit.Assert;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

/**
 * A helper to monitor the presence (or absence) of problems (errors, warnings
 * or info) reported to the platform during a test exeuction.
 * 
 * @author pcdavid
 */
public class PlatformProblemsListener {
	/** The reported errors. */
	protected final Multimap<String, IStatus> errors = LinkedHashMultimap.create();

	/** The reported warnings. */
	protected final Multimap<String, IStatus> warnings = LinkedHashMultimap.create();

	/** The reported infos. */
	protected final Multimap<String, IStatus> infos = LinkedHashMultimap.create();

	/** The unchaught exceptions handler. */
	private UncaughtExceptionHandler exceptionHandler;

	/** The platform log listener. */
	private ILogListener logListener;

	/** Boolean to activate error catch. */
	private boolean errorCatchActive;

	/** Boolean to activate warning catch. */
	private boolean warningCatchActive;

	/** Boolean to activate info catch. */
	private boolean infoCatchActive;

	/** Initialize the log listener. */
	public void initLoggers() {
		logListener = new ILogListener() {
			@Override
			public void logging(IStatus status, String plugin) {
				switch (status.getSeverity()) {
				case IStatus.ERROR:
					errorOccurs(status, plugin);
					break;
				case IStatus.WARNING:
					warningOccurs(status, plugin);
					break;
				case IStatus.INFO:
					infoOccurs(status, plugin);
					break;
				default:
					// nothing to do
				}
			}
		};
		Platform.addLogListener(logListener);

		exceptionHandler = new UncaughtExceptionHandler() {
			private final String sourcePlugin = "Uncaught exception";

			@Override
			public void uncaughtException(Thread t, Throwable e) {
				IStatus status = new Status(IStatus.ERROR, sourcePlugin, sourcePlugin, e);
				errorOccurs(status, sourcePlugin);
			}
		};
		Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
	}

	/** Dispose the log listener. */
	public void disposeLoggers() {
		if (logListener != null) {
			Platform.removeLogListener(logListener);
		}
	}

	public Multimap<String, IStatus> getErrors() {
		return errors;
	}

	public Multimap<String, IStatus> getWarnings() {
		return warnings;
	}

	/**
	 * check if an error occurs.
	 * 
	 * @return true if an error occurs.
	 */
	public synchronized boolean doesAnErrorOccurs() {
		if (errors != null) {
			return errors.values().size() != 0;
		}
		return false;
	}

	/**
	 * check if a warning occurs.
	 * 
	 * @return true if a warning occurs.
	 */
	public synchronized boolean doesAWarningOccurs() {
		if (warnings != null) {
			return warnings.values().size() != 0;
		}
		return false;
	}

	/**
	 * check if an info occurs.
	 * 
	 * @return true if an info occurs.
	 */
	protected synchronized boolean doesAnInfoOccurs() {
		if (infos != null) {
			return infos.values().size() != 0;
		}
		return false;
	}

	/**
	 * Clear the list of errors. Can be useful when some messages are expected.
	 */
	public synchronized void clearErrors() {
		errors.clear();
	}

	/**
	 * Clear the list of warnings. Can be useful when some messages are expected.
	 */
	public synchronized void clearWarnings() {
		warnings.clear();
	}

	/**
	 * Clear the list of infos. Can be useful when some messages are expected.
	 */
	protected synchronized void clearInfos() {
		infos.clear();
	}

	/**
	 * Records the error.
	 * 
	 * @param status       error status to record
	 * @param sourcePlugin source plugin in which the error occurred
	 */
	private synchronized void errorOccurs(IStatus status, String sourcePlugin) {
		if (isErrorCatchActive()) {
			errors.put(sourcePlugin, status);
		}
	}

	/**
	 * Records the warning.
	 * 
	 * @param status       warning status to record
	 * @param sourcePlugin source plugin in which the warning occurred
	 */
	private synchronized void warningOccurs(IStatus status, String sourcePlugin) {
		if (isWarningCatchActive()) {
			warnings.put(sourcePlugin, status);
		}
	}

	/**
	 * Records the infos.
	 * 
	 * @param status       info status to record
	 * @param sourcePlugin source plugin in which the info occurred
	 */
	private synchronized void infoOccurs(IStatus status, String sourcePlugin) {
		if (isInfoCatchActive()) {
			infos.put(sourcePlugin, status);
		}
	}

	/**
	 * Activate or deactivate the external error detection: the test will fail in an
	 * error is logged or uncaught.
	 * 
	 * @param errorCatchActive boolean to indicate if we activate or deactivate the
	 *                         external error detection
	 */
	public synchronized void setErrorCatchActive(boolean errorCatchActive) {
		this.errorCatchActive = errorCatchActive;
	}

	/**
	 * Activate or deactivate the external warning detection: the test will fail in
	 * a warning is logged or uncaught.
	 * 
	 * @param warningCatchActive boolean to indicate if we activate or deactivate
	 *                           the external warning detection
	 */
	public synchronized void setWarningCatchActive(boolean warningCatchActive) {
		this.warningCatchActive = warningCatchActive;
	}

	/**
	 * Activate or deactivate the external info detection: the test will fail in a
	 * warning is logged or uncaught.
	 * 
	 * @param infoCatchActive boolean to indicate if we activate or deactivate the
	 *                        external info detection
	 */
	public synchronized void setInfoCatchActive(boolean infoCatchActive) {
		this.infoCatchActive = infoCatchActive;
	}

	/**
	 * check if an error catch is active.
	 * 
	 * @return true if an error catch is active.
	 */
	public synchronized boolean isErrorCatchActive() {
		return errorCatchActive;
	}

	/**
	 * check if a warning catch is active.
	 * 
	 * @return true if a warning catch is active.
	 */
	public synchronized boolean isWarningCatchActive() {
		return warningCatchActive;
	}

	/**
	 * check if a info catch is active.
	 * 
	 * @return true if a warning catch is active.
	 */
	protected synchronized boolean isInfoCatchActive() {
		return infoCatchActive;
	}

	/**
	 * Check that there is no existing error or warning.
	 */
	protected void checkLogs() {
		/* an exception occurs in another thread */
		/*
		 * TODO: skip checkLoggers when we are in a shouldSkipUnreliableTests mode. We
		 * have some unwanted resource notifications during the teardown on jenkins.
		 */
		if (!TestsUtil.shouldSkipUnreliableTests()) {
			if (doesAnErrorOccurs()) {
				Assert.fail(getErrorLoggersMessage());
			}
			if (doesAWarningOccurs()) {
				Assert.fail(getWarningLoggersMessage());
			}
		}
	}

	/**
	 * Compute a message from the detected warnings/errors/infos.
	 * 
	 * @param type     type of message : Error/Warning/Info
	 * @param messages map with message reported and their status
	 * @return the message
	 */
	protected synchronized String getLoggersMessage(String type, Multimap<String, IStatus> messages) {
		StringBuilder log1 = new StringBuilder();
		String br = "\n";

		String testName = getClass().getName();

		log1.append(type + "(s) raised during test : " + testName).append(br);
		for (Entry<String, Collection<IStatus>> entry : messages.asMap().entrySet()) {
			String reporter = entry.getKey();
			log1.append(". Log Plugin : " + reporter).append(br);

			for (IStatus status : entry.getValue()) {
				log1.append("  . " + getSeverity(status) + " from plugin:" + status.getPlugin() + ", message: "
						+ status.getMessage() + ", info: " + status.getException()).append(br);
				appendStackTrace(log1, br, status);
			}
			log1.append(br);
		}
		return log1.toString();
	}

	/**
	 * Compute an error message from the detected errors.
	 * 
	 * @return the error message.
	 */
	public synchronized String getErrorLoggersMessage() {
		return getLoggersMessage("Error", errors);
	}

	/**
	 * Compute an error message from the detected warnings.
	 * 
	 * @return the error message.
	 */
	public synchronized String getWarningLoggersMessage() {
		return getLoggersMessage("Warning", warnings);
	}

	/**
	 * Compute an error message from the detected warnings.
	 * 
	 * @return the error message.
	 */
	public synchronized String getInfoLoggersMessage() {
		return getLoggersMessage("Info", infos);
	}

	/**
	 * Convert the <code>status</code> exception in String and add it at the end of
	 * the <code>stringBuilder</code>. Add the
	 * 
	 * @param stringBuilder    The string build to use
	 * @param endLineDelimiter The end line delimiter to use
	 * @param status           The status to convert
	 */
	protected void appendStackTrace(StringBuilder stringBuilder, String endLineDelimiter, IStatus status) {
		PrintWriter pw = null;
		String stacktrace = null;
		if (status.getException() != null) {
			try {
				StringWriter sw = new StringWriter();
				pw = new PrintWriter(sw);
				// CHECKSTYLE:OFF
				status.getException().printStackTrace(pw);
				// CHECKSTYLE:ON
				stacktrace = sw.toString();
			} finally {
				if (pw != null) {
					pw.close();
				}
				if (stacktrace == null) {
					stacktrace = status.getException().toString();
				}
				stringBuilder.append("   . Stack trace: " + stacktrace).append(endLineDelimiter);
			}
		}
	}

	/**
	 * Convert the severity of the <code>status</code> in string.
	 * 
	 * @param status The status to convert.
	 * @return a string representation of the severity of the status
	 */
	protected String getSeverity(IStatus status) {
		String severity;
		switch (status.getSeverity()) {
		case IStatus.OK:
			severity = "Ok";
			break;
		case IStatus.INFO:
			severity = "Info";
			break;
		case IStatus.WARNING:
			severity = "Warning";
			break;
		case IStatus.CANCEL:
			severity = "Cancel";
			break;
		case IStatus.ERROR:
			severity = "Error";
			break;
		default:
			severity = "Unspecified";
		}
		return severity;
	}

}
