/**
 * Copyright (c) 2007, 2013 THALES GLOBAL SERVICES.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *
 */
package org.eclipse.sirius.viewpoint.description.tool;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration ' <em><b>Drag Source</b></em>', and
 * utility methods for working with them. <!-- end-user-doc -->
 *
 * @see org.eclipse.sirius.viewpoint.description.tool.ToolPackage#getDragSource()
 * @model
 * @generated
 */
public enum DragSource implements Enumerator {
    /**
     * The '<em><b>DIAGRAM</b></em>' literal object. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #DIAGRAM
     * @generated
     * @ordered
     */
    DIAGRAM_LITERAL(1, "DIAGRAM", "DIAGRAM"), //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * The '<em><b>PROJECT EXPLORER</b></em>' literal object. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #PROJECT_EXPLORER
     * @generated
     * @ordered
     */
    PROJECT_EXPLORER_LITERAL(2, "PROJECT_EXPLORER", "PROJECT_EXPLORER"), //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * The '<em><b>BOTH</b></em>' literal object. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #BOTH
     * @generated
     * @ordered
     */
    BOTH_LITERAL(3, "BOTH", "BOTH"); //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * The '<em><b>DIAGRAM</b></em>' literal value. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>DIAGRAM</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @see #DIAGRAM_LITERAL
     * @model
     * @generated
     * @ordered
     */
    public static final int DIAGRAM = 1;

    /**
     * The '<em><b>PROJECT EXPLORER</b></em>' literal value. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>PROJECT EXPLORER</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @see #PROJECT_EXPLORER_LITERAL
     * @model
     * @generated
     * @ordered
     */
    public static final int PROJECT_EXPLORER = 2;

    /**
     * The '<em><b>BOTH</b></em>' literal value. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>BOTH</b></em>' literal object isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @see #BOTH_LITERAL
     * @model
     * @generated
     * @ordered
     */
    public static final int BOTH = 3;

    /**
     * An array of all the '<em><b>Drag Source</b></em>' enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private static final DragSource[] VALUES_ARRAY = new DragSource[] { DIAGRAM_LITERAL, PROJECT_EXPLORER_LITERAL, BOTH_LITERAL, };

    /**
     * A public read-only list of all the '<em><b>Drag Source</b></em>' enumerators. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     */
    public static final List<DragSource> VALUES = Collections.unmodifiableList(Arrays.asList(DragSource.VALUES_ARRAY));

    /**
     * Returns the '<em><b>Drag Source</b></em>' literal with the specified literal value. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @param literal
     *            the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static DragSource get(String literal) {
        for (DragSource result : DragSource.VALUES_ARRAY) {
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Drag Source</b></em>' literal with the specified name. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @param name
     *            the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static DragSource getByName(String name) {
        for (DragSource result : DragSource.VALUES_ARRAY) {
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Drag Source</b></em>' literal with the specified integer value. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @param value
     *            the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static DragSource get(int value) {
        switch (value) {
        case DIAGRAM:
            return DIAGRAM_LITERAL;
        case PROJECT_EXPLORER:
            return PROJECT_EXPLORER_LITERAL;
        case BOTH:
            return BOTH_LITERAL;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private final int value;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private final String name;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private final String literal;

    /**
     * Only this class can construct instances. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private DragSource(int value, String name, String literal) {
        this.value = value;
        this.name = name;
        this.literal = literal;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public int getValue() {
        return value;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getLiteral() {
        return literal;
    }

    /**
     * Returns the literal value of the enumerator, which is its string representation. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     */
    @Override
    public String toString() {
        return literal;
    }

} // DragSource
