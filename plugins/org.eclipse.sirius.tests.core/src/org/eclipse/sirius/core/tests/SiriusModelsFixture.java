package org.eclipse.sirius.core.tests;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SiriusModelsFixture {

    private static final String SRC_ROOT = "/home/pcdavid/src/sirius/org.eclipse.sirius/plugins";

    private final Map<String, URI> ecoreURIs = Maps.newLinkedHashMap();

    public SiriusModelsFixture() {
        initializeEMF();
        registerSiriusModels(SRC_ROOT);
    }

    private void initializeEMF() {
        if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
            @SuppressWarnings("unused")
            EPackage ecore = EcorePackage.eINSTANCE;
            Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
        }
    }

    private void registerSiriusModels(String rootPath) {
        registerEcore("viewpoint", rootPath + "/org.eclipse.sirius/model/viewpoint.ecore");
        registerEcore("diagram", rootPath + "/org.eclipse.sirius.diagram/model/diagram.ecore");
        registerEcore("sequence", rootPath + "/org.eclipse.sirius.diagram.sequence/model/sequence.ecore");
        registerEcore("tree", rootPath + "/org.eclipse.sirius.tree/model/tree.ecore");
        registerEcore("table", rootPath + "/org.eclipse.sirius.table/model/table.ecore");
    }

    private final void registerEcore(String name, String path) {
        ecoreURIs.put(name, URI.createFileURI(path));
    }

    public Set<EPackage> loadAllPackages(ResourceSet rs) {
        return loadPackages(rs, ecoreURIs.keySet().toArray(new String[0]));
    }

    public EPackage loadPackage(ResourceSet rs, String name) {
        Set<EPackage> result = loadPackages(rs, name);
        if (result.size() == 1) {
            return result.iterator().next();
        } else {
            return null;
        }
    }

    public Set<EPackage> loadPackages(ResourceSet rs, String... names) {
        Collection<EPackage> result = Lists.newArrayList();
        for (String name : names) {
            URI uri = ecoreURIs.get(name);
            if (uri != null) {
                Resource res = rs.getResource(uri, true);
                if (res != null && res.getContents().size() > 0 && res.getContents().get(0) instanceof EPackage) {
                    result.add((EPackage) res.getContents().get(0));
                }
            }
        }
        return ImmutableSet.copyOf(result);
    }
    
    public EPackage findPackageByName(Iterable<EPackage> scope, String packageName) {
        for (EPackage p : scope) {
            if (Objects.equal(packageName, p.getName())) {
                return p;
            }
        }
        return null;
    }
    
    public EClass findClassByName(Iterable<EPackage> scope, String packageName, String className) {
        EPackage p = findPackageByName(scope, packageName);
        if (p != null) {
            EClassifier k = p.getEClassifier(className);
            if (k instanceof EClass) {
                return (EClass) k;
            }
        }
        return null;
    }
}
