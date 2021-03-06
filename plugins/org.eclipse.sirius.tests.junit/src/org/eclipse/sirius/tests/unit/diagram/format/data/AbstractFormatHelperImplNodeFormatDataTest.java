/*******************************************************************************
 * Copyright (c) 2010, 2018 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.tests.unit.diagram.format.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.diagram.formatdata.NodeFormatData;
import org.eclipse.sirius.diagram.formatdata.tools.api.util.FormatHelper;
import org.eclipse.sirius.diagram.formatdata.tools.api.util.configuration.ConfigurationFactory;

/**
 * Test class.
 * 
 * @author dlecan
 */
public abstract class AbstractFormatHelperImplNodeFormatDataTest extends AbstractFormatHelperImplTest<NodeFormatData> {

    private final class NodeFormatDataWrapper extends FormatDataWrapper {
        /**
         * @param formatData
         */
        private NodeFormatDataWrapper(final NodeFormatData formatData) {
            super(formatData);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected boolean doEquals(final NodeFormatData otherFormatData) {
            return FormatHelper.INSTANCE.haveSameLayout(getThisFormatData(), otherFormatData, ConfigurationFactory.buildConfiguration());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected AbstractFormatHelperImplTest<NodeFormatData>.FormatDataWrapper createWrappedInstance(final NodeFormatData from) throws Exception {
        // Do not use SiriusCopier here as we want to copy the id in the format data tree.
        final NodeFormatData nodeFormatData = EcoreUtil.copy(from);
        return new NodeFormatDataWrapper(nodeFormatData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected AbstractFormatHelperImplTest<NodeFormatData>.FormatDataWrapper createWrappedNotEqualInstance() throws Exception {
        List<NodeFormatData> formatDataList = new ArrayList<NodeFormatData>();
        Collection<Map<String, NodeFormatData>> rootFormatDataMap = getManager().getRootNodeFormatData().values();
        for (Map<String, NodeFormatData> valueMap : rootFormatDataMap) {
            formatDataList.addAll(valueMap.values());
        }
        return new NodeFormatDataWrapper(formatDataList.get(1));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected NodeFormatData getReferenceFormatData() {
        Collection<Map<String, NodeFormatData>> rootFormatDataMap = getManager().getRootNodeFormatData().values();
        List<NodeFormatData> formatDataList = new ArrayList<NodeFormatData>();
        for (Map<String, NodeFormatData> valueMap : rootFormatDataMap) {
            formatDataList.addAll(valueMap.values());
        }
        return formatDataList.get(getIndexOfReferenceFormatData());
    }

    /**
     * Get position of reference format data.
     * 
     * @return Position of reference format data.
     */
    protected abstract int getIndexOfReferenceFormatData();

}
