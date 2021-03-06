/*******************************************************************************
 * Copyright (c) 2016, 2017 Obeo.
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
package org.eclipse.sirius.properties.core.internal.converter;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.eclipse.eef.EEFPageDescription;
import org.eclipse.eef.EEFSemanticValidationRuleDescription;
import org.eclipse.eef.EEFToolbarAction;
import org.eclipse.eef.EefFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.properties.PageDescription;
import org.eclipse.sirius.properties.core.api.AbstractDescriptionConverter;
import org.eclipse.sirius.properties.core.api.TransformationCache;
import org.eclipse.sirius.properties.core.internal.Messages;
import org.eclipse.sirius.viewpoint.description.validation.SemanticValidationRule;

/**
 * This class is used to convert the Sirius description of a page to an EEF one.
 * 
 * @author sbegaudeau
 */
public class PageDescriptionConverter extends AbstractDescriptionConverter {

    @Override
    public boolean canHandle(EObject description) {
        return description instanceof PageDescription;
    }

    @Override
    public EObject convert(EObject description, Map<String, Object> parameters, TransformationCache cache) {
        if (description instanceof PageDescription) {
            PageDescription pageDescription = (PageDescription) description;

            EEFPageDescription page = EefFactory.eINSTANCE.createEEFPageDescription();
            page.setIdentifier(pageDescription.getName());
            page.setLabelExpression(pageDescription.getLabelExpression());
            page.setDomainClass(pageDescription.getDomainClass());
            page.setSemanticCandidateExpression(pageDescription.getSemanticCandidateExpression());
            page.setPreconditionExpression(pageDescription.getPreconditionExpression());
            page.setIndented(pageDescription.isIndented());

            if (page.getIdentifier() == null || page.getIdentifier().trim().length() == 0) {
                page.setIdentifier(EcoreUtil.getURI(pageDescription).toString());
            }

            cache.put(description, page);

            if (pageDescription.getValidationSet() != null) {
                List<SemanticValidationRule> semanticValidationRules = pageDescription.getValidationSet().getSemanticValidationRules();
                page.getSemanticValidationRules().addAll(this.convertCollection(semanticValidationRules, parameters, cache, EEFSemanticValidationRuleDescription.class));
            }

            page.getActions().addAll(this.convertCollection(pageDescription.getActions(), parameters, cache, EEFToolbarAction.class));

            return page;
        } else {
            throw new IllegalArgumentException(MessageFormat.format(Messages.IDescriptionConverter_InvalidDescriptionType, description.getClass().getName(), PageDescription.class.getName()));
        }
    }

}
