/*******************************************************************************
 * Copyright (c) 2007-2013 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.viewpoint.description.filter.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.sirius.viewpoint.description.filter.util.FilterAdapterFactory;

/**
 * This is the factory that is used to provide the interfaces needed to support
 * Viewers. The adapters generated by this factory convert EMF adapter
 * notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}. The
 * adapters also support Eclipse property sheets. Note that most of the adapters
 * are shared among multiple instances. <!-- begin-user-doc --> <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class FilterItemProviderAdapterFactory extends FilterAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public static final String copyright = "Copyright (c) 2007, 2013 THALES GLOBAL SERVICES.\nAll rights reserved. This program and the accompanying materials\nare made available under the terms of the Eclipse Public License v1.0\nwhich accompanies this distribution, and is available at\nhttp://www.eclipse.org/legal/epl-v10.html\n\nContributors:\n   Obeo - initial API and implementation\n";

    /**
     * This keeps track of the root adapter factory that delegates to this
     * adapter factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected ComposedAdapterFactory parentAdapterFactory;

    /**
     * This is used to implement
     * {@link org.eclipse.emf.edit.provider.IChangeNotifier}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected IChangeNotifier changeNotifier = new ChangeNotifier();

    /**
     * This keeps track of all the supported types checked by
     * {@link #isFactoryForType isFactoryForType}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    protected Collection<Object> supportedTypes = new ArrayList<Object>();

    /**
     * This constructs an instance. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     */
    public FilterItemProviderAdapterFactory() {
        supportedTypes.add(IEditingDomainItemProvider.class);
        supportedTypes.add(IStructuredItemContentProvider.class);
        supportedTypes.add(ITreeItemContentProvider.class);
        supportedTypes.add(IItemLabelProvider.class);
        supportedTypes.add(IItemPropertySource.class);
    }

    /**
     * This keeps track of the one adapter used for all
     * {@link org.eclipse.sirius.viewpoint.description.filter.MappingFilter}
     * instances. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected MappingFilterItemProvider mappingFilterItemProvider;

    /**
     * This creates an adapter for a
     * {@link org.eclipse.sirius.viewpoint.description.filter.MappingFilter}.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Adapter createMappingFilterAdapter() {
        if (mappingFilterItemProvider == null) {
            mappingFilterItemProvider = new MappingFilterItemProvider(this);
        }

        return mappingFilterItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all
     * {@link org.eclipse.sirius.viewpoint.description.filter.CompositeFilterDescription}
     * instances. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected CompositeFilterDescriptionItemProvider compositeFilterDescriptionItemProvider;

    /**
     * This creates an adapter for a
     * {@link org.eclipse.sirius.viewpoint.description.filter.CompositeFilterDescription}
     * . <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Adapter createCompositeFilterDescriptionAdapter() {
        if (compositeFilterDescriptionItemProvider == null) {
            compositeFilterDescriptionItemProvider = new CompositeFilterDescriptionItemProvider(this);
        }

        return compositeFilterDescriptionItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all
     * {@link org.eclipse.sirius.viewpoint.description.filter.VariableFilter}
     * instances. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected VariableFilterItemProvider variableFilterItemProvider;

    /**
     * This creates an adapter for a
     * {@link org.eclipse.sirius.viewpoint.description.filter.VariableFilter}.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Adapter createVariableFilterAdapter() {
        if (variableFilterItemProvider == null) {
            variableFilterItemProvider = new VariableFilterItemProvider(this);
        }

        return variableFilterItemProvider;
    }

    /**
     * This keeps track of the one adapter used for all
     * {@link org.eclipse.sirius.viewpoint.description.filter.FilterVariable}
     * instances. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected FilterVariableItemProvider filterVariableItemProvider;

    /**
     * This creates an adapter for a
     * {@link org.eclipse.sirius.viewpoint.description.filter.FilterVariable}.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Adapter createFilterVariableAdapter() {
        if (filterVariableItemProvider == null) {
            filterVariableItemProvider = new FilterVariableItemProvider(this);
        }

        return filterVariableItemProvider;
    }

    /**
     * This returns the root adapter factory that contains this factory. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public ComposeableAdapterFactory getRootAdapterFactory() {
        return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
    }

    /**
     * This sets the composed adapter factory that contains this factory. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
        this.parentAdapterFactory = parentAdapterFactory;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public boolean isFactoryForType(Object type) {
        return supportedTypes.contains(type) || super.isFactoryForType(type);
    }

    /**
     * This implementation substitutes the factory itself as the key for the
     * adapter. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Adapter adapt(Notifier notifier, Object type) {
        return super.adapt(notifier, this);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Object adapt(Object object, Object type) {
        if (isFactoryForType(type)) {
            Object adapter = super.adapt(object, type);
            if (!(type instanceof Class<?>) || (((Class<?>) type).isInstance(adapter))) {
                return adapter;
            }
        }

        return null;
    }

    /**
     * This adds a listener. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void addListener(INotifyChangedListener notifyChangedListener) {
        changeNotifier.addListener(notifyChangedListener);
    }

    /**
     * This removes a listener. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void removeListener(INotifyChangedListener notifyChangedListener) {
        changeNotifier.removeListener(notifyChangedListener);
    }

    /**
     * This delegates to {@link #changeNotifier} and to
     * {@link #parentAdapterFactory}. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @generated
     */
    public void fireNotifyChanged(Notification notification) {
        changeNotifier.fireNotifyChanged(notification);

        if (parentAdapterFactory != null) {
            parentAdapterFactory.fireNotifyChanged(notification);
        }
    }

    /**
     * This disposes all of the item providers created by this factory. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void dispose() {
        if (mappingFilterItemProvider != null)
            mappingFilterItemProvider.dispose();
        if (compositeFilterDescriptionItemProvider != null)
            compositeFilterDescriptionItemProvider.dispose();
        if (variableFilterItemProvider != null)
            variableFilterItemProvider.dispose();
        if (filterVariableItemProvider != null)
            filterVariableItemProvider.dispose();
    }

}
