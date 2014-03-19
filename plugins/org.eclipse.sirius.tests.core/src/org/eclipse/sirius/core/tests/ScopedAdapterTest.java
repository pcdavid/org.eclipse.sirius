package org.eclipse.sirius.core.tests;

// CHECKSTYLE:OFF
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.sirius.ext.emf.AllContents;
import org.eclipse.sirius.ext.emf.adapter.ScopedContentAdapter;
import org.eclipse.sirius.ext.emf.adapter.TypesAnalyzer;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

public class ScopedAdapterTest {
    private static int NB_EOBJECTS;

    private static int NB_EPACKAGES;

    private static int NB_ECLASSES;

    private static int NB_EMODELELEMENT;

    private SiriusModelsFixture fixture;

    private ResourceSet rs;

    private EPackage pkg;

    @BeforeClass
    public static void initialize_EMF() {
        if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
            @SuppressWarnings("unused")
            EPackage ecore = EcorePackage.eINSTANCE;
            Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
        }
    }

    @BeforeClass
    public static void initialize_counts() {
        EPackage pkg = new SiriusModelsFixture().loadPackage(new ResourceSetImpl(), "viewpoint");
        for (EObject obj : AllContents.of(pkg, true)) {
            NB_EOBJECTS++;
            if (EcorePackage.Literals.EPACKAGE.isInstance(obj)) {
                NB_EPACKAGES++;
            }
            if (EcorePackage.Literals.ECLASS.isInstance(obj)) {
                NB_ECLASSES++;
            }
            if (EcorePackage.Literals.EMODEL_ELEMENT.isInstance(obj)) {
                NB_EMODELELEMENT++;
            }
        }
    }

    @Before
    public void setup() {
        this.fixture = new SiriusModelsFixture();
        this.rs = new ResourceSetImpl();
        this.pkg = fixture.loadPackage(rs, "viewpoint");
    }

    @After
    public void dispose() {
        this.fixture = null;
        this.rs = null;
    }

    /**
     * Check the basic behavior of the "dumb" standard EContentAdapter, which
     * installs itself an the complete hierarchy of elements.
     */
    @Test
    public void install_and_count_basic_content_adapter() {
        EContentAdapter adapter = new EContentAdapter();
        pkg.eAdapters().add(adapter);

        int adaptedCount = countAdaptedElements(pkg, EContentAdapter.class);
        assertEquals("EContentAdapter should be installed on every EObject", ScopedAdapterTest.NB_EOBJECTS, adaptedCount);
    }

    /**
     * Check that the ScopeContentAdapter, configured to watch only EClasses,
     * only installs itself on EClasses and EPackages (which can contain
     * EClasses), and not on other types of elements like EReferences, which are
     * irrelevant in this case.
     */
    @Test
    public void install_and_count_scope_content_adapter_on_eclass() {
        ScopedContentAdapter adapter = new ScopedContentAdapter(TypesAnalyzer.on(EcorePackage.eINSTANCE), EcorePackage.Literals.ECLASS);
        pkg.eAdapters().add(adapter);

        int adaptedCount = countAdaptedElements(pkg, ScopedContentAdapter.class);
        assertThat("Not all elements should be adpated", adaptedCount, lessThan(ScopedAdapterTest.NB_EOBJECTS));
        assertThat("At least all EPackages and EClasses should be adapted", adaptedCount, greaterThanOrEqualTo(NB_EPACKAGES + NB_ECLASSES));
        assertThat("Exactly all EPackages and EClasses should be adapted", adaptedCount, equalTo(NB_EPACKAGES + NB_ECLASSES));
    }

    /**
     * Check that the ScopeContentAdapter, configured to watch only EPackages,
     * only installs itself on EPackages and nothing else.
     */
    @Test
    public void install_and_count_scope_content_adapter_on_epackage() {
        ScopedContentAdapter adapter = new ScopedContentAdapter(TypesAnalyzer.on(EcorePackage.eINSTANCE), EcorePackage.Literals.EPACKAGE);
        pkg.eAdapters().add(adapter);

        int adaptedCount = countAdaptedElements(pkg, ScopedContentAdapter.class);
        assertThat("Not all elements should be adpated", adaptedCount, lessThan(ScopedAdapterTest.NB_EOBJECTS));
        assertThat("Exactly all EPackages", adaptedCount, equalTo(NB_EPACKAGES));
    }

    /**
     * Check that the ScopeContentAdapter correctly installs itself on new
     * matching elements even after it has been installed.
     */
    @Test
    public void adapter_adapts_to_new_direct_targets() {
        ScopedContentAdapter adapter = new ScopedContentAdapter(TypesAnalyzer.on(EcorePackage.eINSTANCE), EcorePackage.Literals.EPACKAGE);
        pkg.eAdapters().add(adapter);

        EPackage newPkg = EcoreFactory.eINSTANCE.createEPackage();
        pkg.getESubpackages().add(newPkg);
        assertTrue(Iterables.any(newPkg.eAdapters(), Predicates.instanceOf(ScopedContentAdapter.class)));
    }

    /**
     * Check that the ScopeContentAdapter correctly installs itself on new
     * matching elements even after it has been installed and even if they match
     * indirectly (i.e. become accessible through an indirect path).
     */
    @Test
    public void adapter_adapts_to_new_indirect_targets() {
        ScopedContentAdapter adapter = new ScopedContentAdapter(TypesAnalyzer.on(EcorePackage.eINSTANCE), EcorePackage.Literals.ECLASS);
        pkg.eAdapters().add(adapter);

        EPackage newPkg = EcoreFactory.eINSTANCE.createEPackage();
        pkg.getESubpackages().add(newPkg);
        EClass newClass = EcoreFactory.eINSTANCE.createEClass();
        newPkg.getEClassifiers().add(newClass);
        assertTrue(Iterables.any(newClass.eAdapters(), Predicates.instanceOf(ScopedContentAdapter.class)));
    }

    /**
     * Check that the ScopeContentAdapter correctly installs itself on new
     * matching elements even after it has been installed and even if they match
     * indirectly (i.e. become accessible through an indirect path).
     */
    @Test
    public void adapter_adapts_to_new_indirect_targets2() {
        ScopedContentAdapter adapter = new ScopedContentAdapter(TypesAnalyzer.on(EcorePackage.eINSTANCE), EcorePackage.Literals.ECLASS);
        pkg.eAdapters().add(adapter);

        EPackage newPkg = EcoreFactory.eINSTANCE.createEPackage();
        EClass newClass = EcoreFactory.eINSTANCE.createEClass();
        newPkg.getEClassifiers().add(newClass);
        pkg.getESubpackages().add(newPkg);
        assertTrue(Iterables.any(newClass.eAdapters(), Predicates.instanceOf(ScopedContentAdapter.class)));
    }

    /**
     * Check that if needed, TypesAnalyzer.getAllSubTypes() can be used to
     * explicitly include the sub-types of an EClass in the scope.
     */
    @Test
    public void can_match_subtype_hierarchy_when_needed() {
        TypesAnalyzer ta = TypesAnalyzer.on(EcorePackage.eINSTANCE);
        ScopedContentAdapter adapter = new ScopedContentAdapter(ta, ta.getAllSubTypes(EcorePackage.Literals.EMODEL_ELEMENT));
        pkg.eAdapters().add(adapter);
        int adaptedCount = countAdaptedElements(pkg, EContentAdapter.class);
        assertEquals("EContentAdapter should be installed on every EModelElement", ScopedAdapterTest.NB_EMODELELEMENT, adaptedCount);
    }

    /**
     * Hepler to count how many elements in the hierarchy (identified by its
     * root) have an adapter of the specified type.
     */
    private int countAdaptedElements(EObject root, Class<? extends Adapter> adapterType) {
        int counter = 0;
        for (EObject obj : AllContents.of(root, true)) {
            // EcoreUtil.getAdapter(obj.eAdapters(), adapterType) != null
            if (Iterables.any(obj.eAdapters(), Predicates.instanceOf(adapterType))) {
                counter++;
            }
        }
        return counter;
    }
}
