package org.eclipse.sirius.ext.emf.adapter;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.ext.base.relations.Relation;
import org.eclipse.sirius.ext.base.relations.TransitiveClosure;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

// CHECKSTYLE:OFF
/**
 * Provides useful static analysis on a set of EMF types (EClasses).
 * 
 * @author pcdavid
 */
public final class TypesAnalyzer {
    /**
     * All the packages considered. This does does includes their sub-packages,
     * if any. They must be included explicitly if needed.
     */
    private final ImmutableSet<EPackage> packages;

    /**
     * All the classes in all the packages considered.
     */
    private final ImmutableSet<EClass> classes;

    /**
     * The reflective transitive closure of the relation "x extends y". In other
     * words, the map contains the pair (x, y) if and only if x == y or y is a
     * direct or indirect super-type of x.
     */
    private final ImmutableMultimap<EClass, EClass> superTypesOf;

    /**
     * The reflective transitive closure of the relation "y extends x". In other
     * words, the map contains the pair (x, y) if and only if x == y or x is a
     * direct or indirect super-type of y.
     */
    private final ImmutableMultimap<EClass, EClass> subTypesOf;

    private final Multimap<EClass, EClass> containement;

    private TypesAnalyzer(Iterable<EPackage> packages) {
        this.packages = ImmutableSet.copyOf(packages);
        this.classes = getAllEClasses(this.packages);
        this.superTypesOf = computeSuperTypes();
        this.subTypesOf = this.superTypesOf.inverse();
        this.containement = computeContainments();
    }

    /**
     * Helper method to compute all the direct and indirect sub-packages of a
     * set of root packages.
     * 
     * @param roots
     *            the root packages to consider.
     * @return the set of all root packages and all their directly and
     *         indirectly contained sub-packages.
     */
    public static Set<EPackage> withDescendants(Iterable<EPackage> roots) {
        Collection<EPackage> result = Lists.newArrayList();
        for (EPackage root : roots) {
            result.add(root);
            result.addAll(withDescendants(root.getESubpackages()));
        }
        return ImmutableSet.copyOf(result);
    }
    
    /**
     * Returns an analyzer for all the types in the specified package (not
     * including any sub-package).
     * 
     * @param pkg
     *            the package to analyze.
     * @return an analyzer aware of all the types in the specified package.
     */
    public static TypesAnalyzer on(EPackage pkg) {
        return TypesAnalyzer.on(Collections.singleton(pkg));
    }

    public static TypesAnalyzer on(Iterable<EPackage> pkgs) {
        return new TypesAnalyzer(pkgs);
    }

    public static TypesAnalyzer on(EPackage.Registry reg) {
        return TypesAnalyzer.on(Iterables.filter(reg.values(), EPackage.class));
    }

    public ImmutableSet<EPackage> getScope() {
        return packages;
    }
    
    public ImmutableSet<EClass> getAllSubTypes(EClass type) {
        return ImmutableSet.copyOf(subTypesOf.get(type));
    }

    public boolean isSuperTypeOf(EClass superType, EClass subType) {
        return subTypesOf.containsEntry(superType, subType);
    }

    public boolean isSubTypeOf(EClass subType, EClass superType) {
        return superTypesOf.containsEntry(subType, superType);
    }

    public boolean canContain(EClass container, EClass contained) {
        return containement.containsEntry(container, contained);
    }

    private ImmutableSet<EClass> getAllEClasses(Set<EPackage> pkgs) {
        Collection<EClass> result = Lists.newArrayList();
        for (EPackage ePackage : pkgs) {
            Iterables.addAll(result, Iterables.filter(ePackage.getEClassifiers(), EClass.class));
        }
        return ImmutableSet.copyOf(result);
    }

    private ImmutableMultimap<EClass, EClass> computeSuperTypes() {
        Multimap<EClass, EClass> result = LinkedHashMultimap.create();
        for (EClass k : classes) {
            result.put(k, k);
            result.putAll(k, k.getEAllSuperTypes());
        }
        return ImmutableMultimap.copyOf(result);
    }

    private Multimap<EClass, EClass> computeContainments() {
        final Multimap<EClass, EClass> directlyContains = HashMultimap.create();
        for (EClass container : classes) {
            directlyContains.putAll(container, getPossiblyContainedTypes(container));
        }
        Multimap<EClass, EClass> containment = HashMultimap.create();
        Relation<EClass> containmentRel = new TransitiveClosure<EClass>(new Relation<EClass>() {
            @Override
            public Set<EClass> apply(EClass container) {
                return ImmutableSet.copyOf(directlyContains.get(container));
            }
        });
        for (EClass container : classes) {
            containment.putAll(container, containmentRel.apply(container));
        }
        return containment;
    }

    private Set<EClass> getPossiblyContainedTypes(EClass container) {
        Collection<EClass> result = Lists.newArrayList();
        for (EClass actualContainerType : subTypesOf.get(container)) {
            Set<EReference> containment = getContainmentReferences(actualContainerType);
            for (EReference ref : containment) {
                result.addAll(subTypesOf.get(ref.getEReferenceType()));
            }
        }
        return ImmutableSet.copyOf(result);
    }

    private Set<EReference> getContainmentReferences(EClass actualContainerType) {
        Collection<EReference> refs = Lists.newArrayList();
        for (EReference ref : actualContainerType.getEReferences()) {
            if (ref.isContainment()) {
                refs.add(ref);
            }
        }
        return ImmutableSet.copyOf(refs);
    }
}
