/*******************************************************************************
 *  Copyright (c) 2018 Obeo.
 *  This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *  Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.workflow.model.editor.properties.sections.workflow.sectiondescription;

// Start of user code imports

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.sirius.editor.editorPlugin.SiriusEditor;
import org.eclipse.sirius.editor.internal.navigation.NavigationByKeyListener;
import org.eclipse.sirius.editor.properties.sections.common.AbstractTextWithButtonPropertySection;
import org.eclipse.sirius.editor.tools.api.assist.TypeContentProposalProvider;
import org.eclipse.sirius.editor.tools.internal.presentation.TextWithContentProposalDialog;
import org.eclipse.sirius.ui.tools.api.assist.ContentProposalClient;
import org.eclipse.sirius.workflow.WorkflowPackage;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

// End of user code imports

/**
 * A section for the titleExpression property of a SectionDescription object.
 */
public class SectionDescriptionTitleExpressionPropertySection extends AbstractTextWithButtonPropertySection implements ContentProposalClient {

    /**
     * @see org.eclipse.sirius.workflow.model.editor.properties.sections.AbstractTextWithButtonPropertySection#getDefaultLabelText()
     */
    @Override
    protected String getDefaultLabelText() {
        return "TitleExpression"; //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.sirius.workflow.model.editor.properties.sections.AbstractTextWithButtonPropertySection#getLabelText()
     */
    @Override
    protected String getLabelText() {
        String labelText;
        labelText = super.getLabelText() + "*:"; //$NON-NLS-1$
        // Start of user code get label text

        // End of user code get label text
        return labelText;
    }

    /**
     * @see org.eclipse.sirius.workflow.model.editor.properties.sections.AbstractTextWithButtonPropertySection#getFeature()
     */
    @Override
    public EAttribute getFeature() {
        return WorkflowPackage.eINSTANCE.getSectionDescription_TitleExpression();
    }

    /**
     * @see org.eclipse.sirius.workflow.model.editor.properties.sections.AbstractTextWithButtonPropertySection#getFeatureValue(String)
     */
    @Override
    protected Object getFeatureValue(String newText) {
        return newText;
    }

    /**
     * @see org.eclipse.sirius.workflow.model.editor.properties.sections.AbstractTextWithButtonPropertySection#isEqual(String)
     */
    @Override
    protected boolean isEqual(String newText) {
        return getFeatureAsText().equals(newText);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
        super.createControls(parent, tabbedPropertySheetPage);
        /*
         * We set the color as it's a InterpretedExpression
         */
        text.setBackground(SiriusEditor.getColorRegistry().get("yellow"));

        nameLabel.setFont(SiriusEditor.getFontRegistry().get("required"));

        TypeContentProposalProvider.bindPluginsCompletionProcessors(this, text);

        text.addKeyListener(new NavigationByKeyListener(this, text, eObject));

        // Start of user code create controls

        // End of user code create controls

    }

    @Override
    protected SelectionListener createButtonListener() {
        return new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TextWithContentProposalDialog dialog = new TextWithContentProposalDialog(composite.getShell(), SectionDescriptionTitleExpressionPropertySection.this, text.getText());
                dialog.open();
                text.setText(dialog.getResult());
                handleTextModified();
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPropertyDescription() {
        return "";
    }

    // Start of user code user operations

    // End of user code user operations
}