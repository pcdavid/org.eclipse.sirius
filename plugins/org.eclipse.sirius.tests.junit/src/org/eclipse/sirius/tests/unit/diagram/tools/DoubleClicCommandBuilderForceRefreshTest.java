/*******************************************************************************
 * Copyright (c) 2010, 2015 THALES GLOBAL SERVICES and others.
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
package org.eclipse.sirius.tests.unit.diagram.tools;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.sirius.business.api.helper.task.ICommandTask;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.description.tool.DoubleClickDescription;
import org.eclipse.sirius.diagram.description.tool.ToolFactory;
import org.eclipse.sirius.diagram.tools.internal.command.builders.DoubleClickCommandBuilder;
import org.eclipse.sirius.ecore.extender.business.api.accessor.ModelAccessor;
import org.eclipse.sirius.tests.SiriusTestsPlugin;
import org.eclipse.sirius.tests.support.api.SiriusDiagramTestCase;
import org.eclipse.sirius.tools.api.command.SiriusCommand;
import org.eclipse.sirius.tools.api.command.ui.NoUICallback;
import org.eclipse.sirius.viewpoint.description.tool.ChangeContext;
import org.eclipse.sirius.viewpoint.description.tool.InitialOperation;
import org.eclipse.sirius.viewpoint.description.tool.PaneBasedSelectionWizardDescription;

/**
 * PaneBasedSelectionWizard tests.
 * 
 * @author mporhel
 */
public class DoubleClicCommandBuilderForceRefreshTest extends SiriusDiagramTestCase {

    private static final String SEMANTIC_MODEL_PATH = "/" + SiriusTestsPlugin.PLUGIN_ID + "/data/unit/tool/vp-3386/vp-3386.ecore";

    private static final String AIRD_PATH = "/" + SiriusTestsPlugin.PLUGIN_ID + "/data/unit/tool/vp-3386/vp-3386.aird";

    private static final String VIEWPOINT_NAME = "Design";

    private static final String REPRESENTATION_DESC_NAME = "Entities";

    private DDiagram diagram;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        genericSetUp(Collections.singletonList(SEMANTIC_MODEL_PATH), Collections.<String> emptyList(), AIRD_PATH);
        initViewpoint(VIEWPOINT_NAME);
        diagram = (DDiagram) getRepresentations(REPRESENTATION_DESC_NAME).toArray()[0];
    }

    /**
     * Test a {@link PaneBasedSelectionWizardDescription} tool. Test the initial
     * operation execution.
     */
    public void testDoubleClickCommandBuilder() {
        DoubleClickDescription dc = ToolFactory.eINSTANCE.createDoubleClickDescription();
        InitialOperation io = org.eclipse.sirius.viewpoint.description.tool.ToolFactory.eINSTANCE.createInitialOperation();
        ChangeContext cc = org.eclipse.sirius.viewpoint.description.tool.ToolFactory.eINSTANCE.createChangeContext();
        dc.setInitialOperation(io);
        io.setFirstModelOperations(cc);
        cc.setBrowseExpression("aql:self");

        Command cmd = getDoubleClicCommand(dc);
        checkDoubleClicCommand(cmd, false);

        // Set force refresh
        dc.setForceRefresh(true);
        cmd = getDoubleClicCommand(dc);
        checkDoubleClicCommand(cmd, true);

        // Set force refresh
        dc.setForceRefresh(false);
        cc.getSubModelOperations().add(org.eclipse.sirius.viewpoint.description.tool.ToolFactory.eINSTANCE.createRemoveElement());
        cmd = getDoubleClicCommand(dc);
        checkDoubleClicCommand(cmd, false);

        // Set force refresh
        dc.setForceRefresh(true);
        cmd = getDoubleClicCommand(dc);
        checkDoubleClicCommand(cmd, true);

    }

    private void checkDoubleClicCommand(Command cmd, boolean forceRefresh) {
        SiriusCommand vpCmd = (SiriusCommand) cmd;
        List<ICommandTask> tasks = vpCmd.getTasks();
        if (forceRefresh) {
            long nbRefreshPrecommitActivation = tasks.stream().filter(input -> "Set RefreshEditorsPrecommitListener options".equals(input.getLabel())).count();
            assertEquals("The command should contain a refresh editor precommit activation task.", 1, nbRefreshPrecommitActivation);
        }
    }

    private Command getDoubleClicCommand(DoubleClickDescription dc) {
        DoubleClickCommandBuilder builder = new DoubleClickCommandBuilder(dc, diagram.getOwnedDiagramElements().iterator().next());
        builder.init(new ModelAccessor(), null, new NoUICallback());
        Command buildCommand = builder.buildCommand();
        return buildCommand;
    }

    @Override
    protected void tearDown() throws Exception {
        diagram = null;
        super.tearDown();
    }
}
