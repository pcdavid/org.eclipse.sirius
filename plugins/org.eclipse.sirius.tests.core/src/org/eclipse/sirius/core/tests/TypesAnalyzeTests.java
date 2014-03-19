package org.eclipse.sirius.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Set;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.sirius.ext.emf.adapter.TypesAnalyzer;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TypesAnalyzeTests {
    private SiriusModelsFixture fixture;

    private ResourceSet rs;

    @BeforeClass
    public static void initializeEMF() {
        if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
            @SuppressWarnings("unused")
            EPackage ecore = EcorePackage.eINSTANCE;
            Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
        }
    }

    @Before
    public void setup() {
        this.fixture = new SiriusModelsFixture();
        this.rs = new ResourceSetImpl();
    }

    @After
    public void dispose() {
        this.fixture = null;
        this.rs = null;
    }

    @Test
    public void can_load_viewpoint_ecore() {
        EPackage vp = fixture.loadPackage(rs, "viewpoint");
        assertNotNull(vp);
        assertEquals("viewpoint", vp.getName());
    }

    @Test
    public void can_find_viewpoint_subpackages() {
        EPackage vp = fixture.loadPackage(rs, "viewpoint");
        Set<EPackage> all = TypesAnalyzer.withDescendants(Collections.singleton(vp));
        assertNotNull(all);
        assertTrue(all.contains(vp));
        assertTrue(all.size() > 1);
        assertTrue(all.size() >= 1 + vp.getESubpackages().size());
    }

    @Test
    public void can_find_all_sirius_packages() {
        Set<EPackage> rootPackages = fixture.loadAllPackages(rs);
        assertNotNull(rootPackages);
        assertEquals(5, rootPackages.size());
        Set<EPackage> siriusPackages = TypesAnalyzer.withDescendants(rootPackages);
        for (EPackage root : rootPackages) {
            assertTrue(siriusPackages.contains(root));
        }
        assertEquals(21, siriusPackages.size());
    }

    @Test
    public void can_create_analyzer_on_all_sirius_ecores() {
        TypesAnalyzer ta = TypesAnalyzer.on(TypesAnalyzer.withDescendants(fixture.loadAllPackages(rs)));
        assertNotNull(ta);
    }

    @Test
    public void can_analyze_direct_subtyping() {
        TypesAnalyzer ta = TypesAnalyzer.on(TypesAnalyzer.withDescendants(fixture.loadAllPackages(rs)));
        EClass dview = fixture.findClassByName(ta.getScope(), "viewpoint", "DView");
        assertNotNull(dview);
        EClass drc = fixture.findClassByName(ta.getScope(), "viewpoint", "DRepresentationContainer");
        assertNotNull(drc);
        assertTrue(ta.isSuperTypeOf(dview, drc));
        assertTrue(ta.isSubTypeOf(drc,  dview));
    }
    
    @Test
    public void can_analyze_indirect_subtyping() {
        TypesAnalyzer ta = TypesAnalyzer.on(TypesAnalyzer.withDescendants(fixture.loadAllPackages(rs)));
        EClass dde = fixture.findClassByName(ta.getScope(), "diagram", "DDiagramElement");
        assertNotNull(dde);
        EClass dnode = fixture.findClassByName(ta.getScope(), "diagram", "DNode");
        assertNotNull(dnode);
        assertTrue(ta.isSuperTypeOf(dde, dnode));
        assertTrue(ta.isSubTypeOf(dnode, dde));
    }
    
    @Test
    public void danalysis_can_contain_dnode() {
        TypesAnalyzer ta = TypesAnalyzer.on(TypesAnalyzer.withDescendants(fixture.loadAllPackages(rs)));
        EClass danalysis = fixture.findClassByName(ta.getScope(), "viewpoint", "DAnalysis");
        EClass dnode = fixture.findClassByName(ta.getScope(), "diagram", "DNode");
        assertTrue(ta.canContain(danalysis, dnode));
        assertFalse(ta.canContain(dnode, danalysis));
    }
}
