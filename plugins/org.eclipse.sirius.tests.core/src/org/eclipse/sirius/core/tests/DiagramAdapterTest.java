package org.eclipse.sirius.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.factory.SessionFactory;
import org.eclipse.sirius.diagram.DiagramPackage;
import org.eclipse.sirius.diagram.ui.business.internal.parser.AirDResourceFactory;
import org.eclipse.sirius.ext.emf.AllContents;
import org.eclipse.sirius.ext.emf.adapter.ScopedContentAdapter;
import org.eclipse.sirius.ext.emf.adapter.TypesAnalyzer;
import org.eclipse.sirius.viewpoint.ViewpointPackage;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

public class DiagramAdapterTest {
    @BeforeClass
    @SuppressWarnings("unused")
    public static void initialize_EMF() {
        if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
            EPackage ecore = EcorePackage.eINSTANCE;
            EPackage viewpoint = ViewpointPackage.eINSTANCE;
            EPackage diagram = DiagramPackage.eINSTANCE;
            Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
            Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("aird", new AirDResourceFactory());
        }
    }

    @Test
    public void add_adapter_on_gmf_node() throws CoreException {
        URI uri = URI.createFileURI("/home/pcdavid/src/sirius/viewpoint/plugins/fr.obeo.dsl.viewpoint.tests/data/unit/modelers/ecore/tc1180/1180.aird");
        Session session = SessionFactory.INSTANCE.createSession(uri, new NullProgressMonitor());
        assertNotNull(session);

        ResourceSet rs = session.getTransactionalEditingDomain().getResourceSet();

        int totalElements = 0;
        for (Resource res : rs.getResources()) {
            for (EObject root : res.getContents()) {
                for (EObject element : AllContents.of(root, true)) {
                    totalElements++;
                }
            }
        }

        SiriusModelsFixture fixture = new SiriusModelsFixture();
        Set<EPackage> scope = Sets.newHashSet();
        scope.addAll(TypesAnalyzer.withDescendants(Collections.singleton((EPackage) NotationPackage.eINSTANCE)));
        scope.addAll(fixture.loadAllPackages(rs));
        TypesAnalyzer ta = TypesAnalyzer.on(scope);

        assertEquals(0, countAdapters(rs, ScopedContentAdapter.class));
        rs.eAdapters().add(new ScopedContentAdapter(ta, NotationPackage.Literals.NODE));
        int adaptedCount = countAdapters(rs, ScopedContentAdapter.class);
        assertTrue(0 < adaptedCount);
        assertTrue(adaptedCount < totalElements);
        System.out.println(adaptedCount);

    }

    private int countAdapters(ResourceSet rs, Class<?> adapterType) {
        int counter = 0;
        for (Resource res : rs.getResources()) {
            for (EObject root : res.getContents()) {
                for (EObject obj : AllContents.of(root, true)) {
                    counter += Iterables.size(Iterables.filter(obj.eAdapters(), Predicates.instanceOf(adapterType)));
                }
            }
        }
        return counter;
    }
}
