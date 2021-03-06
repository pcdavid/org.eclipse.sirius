/**
 * Copyright (c) 2012, 2018 THALES GLOBAL SERVICES and others
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *      Obeo - Initial API and implementation
 */
package org.eclipse.sirius.tests.swtbot.support.api.condition;

import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

/**
 * A condition testing the number of children items of a {@link SWTBotTreeItem}.
 * 
 * @author <a href="mailto:steve.monnier@obeo.fr">Steve Monnier</a>
 */
public class TreeItemChildrenNumberCondition extends DefaultCondition {

    private final SWTBotTreeItem treeItem;

    private final SWTBotTree tree;

    private final int expectedNumberOfChild;

    private boolean forceTreeItemExpand;

    /**
     * Constructor.
     * 
     * @param treeItem
     *            The {@link SWTBotTreeItem} to test its children
     * @param expectedNumberOfChild
     *            The number of children expected for treeItem
     */
    public TreeItemChildrenNumberCondition(SWTBotTreeItem treeItem, int expectedNumberOfChild) {
        this.tree = null;
        this.treeItem = treeItem;
        this.expectedNumberOfChild = expectedNumberOfChild;
    }

    /**
     * Constructor.
     * 
     * @param treeItem
     *            The {@link SWTBotTreeItem} to test its children
     * @param expectedNumberOfChild
     *            The number of children expected for treeItem
     * @param forceTreeItemExpand
     *            should expand treeItem before running test
     */
    public TreeItemChildrenNumberCondition(SWTBotTreeItem treeItem, int expectedNumberOfChild, boolean forceTreeItemExpand) {
        this.tree = null;
        this.treeItem = treeItem;
        this.expectedNumberOfChild = expectedNumberOfChild;
        this.forceTreeItemExpand = forceTreeItemExpand;
    }

    /**
     * Constructor.
     * 
     * @param tree
     *            The {@link SWTBotTree} to test its children
     * @param expectedNumberOfChild
     *            The number of children expected for treeItem
     */
    public TreeItemChildrenNumberCondition(SWTBotTree tree, int expectedNumberOfChild) {
        this.tree = tree;
        this.treeItem = null;
        this.expectedNumberOfChild = expectedNumberOfChild;
    }

    @Override
    public String getFailureMessage() {
        return "The TreeItem " + treeItem.getText() + " has " + treeItem.getItems().length + " children instead of the expected " + expectedNumberOfChild;
    }

    @Override
    public boolean test() throws Exception {
        if (forceTreeItemExpand) {
            treeItem.expand();
        }
        if (treeItem != null) {
            return treeItem.getItems().length == expectedNumberOfChild;
        } else {
            return tree.getAllItems().length == expectedNumberOfChild;
        }
    }

}
