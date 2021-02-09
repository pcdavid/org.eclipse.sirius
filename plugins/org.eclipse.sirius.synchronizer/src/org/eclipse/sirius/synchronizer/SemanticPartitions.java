/*******************************************************************************
 * Copyright (c) 2011, 2021 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.synchronizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * A set of classical semantic partitions.
 * 
 * @author Cedric Brun <cedric.brun@obeo.fr>
 * 
 */
public class SemanticPartitions {

    public static SemanticPartition eAllContents() {
        return new EAllContentsPartition();
    }

    public static SemanticPartition eAllContents(String domainClass) {
        return null;
    }

    public static EvaluatedSemanticPartition eObjectList(List<EObject> set) {
        return new EObjectList(set);
    }
}

class EAllContentsPartition implements SemanticPartition {

    public EvaluatedSemanticPartition evaluate(EObject root, CreatedOutput parentElement) {
        ArrayList<EObject> eObjects = new ArrayList<>();
        root.eAllContents().forEachRemaining(eObjects::add);
        return new EObjectList(eObjects);
    }

}

class EObjectList implements EvaluatedSemanticPartition {

    private Collection<EObject> set;

    public EObjectList(Collection<EObject> set) {
        this.set = set;
    }

    public boolean isElementOf(EObject sem) {
        return set.contains(sem);
    }

    public Iterator<EObject> elements() {
        return set.iterator();
    }
}
