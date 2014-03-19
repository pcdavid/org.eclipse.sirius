package org.eclipse.sirius.ext.emf.adapter;

// CHECKSTYLE:OFF
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.InternalEList;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

/**
 * A content adapter which is only interested in some types of EObjects, and
 * avoids installing itself in parts of the model where it can determine
 * statically that no instance of these interesting types can appear.
 * 
 * @author pcdavid
 */
public class ScopedContentAdapter extends EContentAdapter {
    private final Set<EClass> scope;

    private final TypesAnalyzer analyzer;

    public ScopedContentAdapter(TypesAnalyzer analyzer, EClass... scope) {
        this(analyzer, Arrays.asList(scope));
    }

    public ScopedContentAdapter(TypesAnalyzer analyzer, Iterable<EClass> scope) {
        this.analyzer = Preconditions.checkNotNull(analyzer);
        this.scope = ImmutableSet.copyOf(Preconditions.checkNotNull(scope));
    }

    @Override
    protected void setTarget(EObject target) {
        basicSetTarget(target);
        for (Iterator<? extends Notifier> i = resolve() ? target.eContents().iterator() : ((InternalEList<? extends Notifier>) target.eContents()).basicIterator(); i.hasNext();) {
            Notifier notifier = i.next();
            if (notifier instanceof EObject) {
                if (inScope((EObject) notifier)) {
                    addAdapter(notifier);
                }
            } else {
                addAdapter(notifier);
            }
        }
    }

    private boolean inScope(EObject target) {
        EClass targetType = target.eClass();
        if (scope.contains(targetType)) {
            return true;
        } else {
            for (EClass k : scope) {
                if (analyzer.canContain(targetType, k)) {
                    return true;
                }
            }
        }
        return false;
    }
}
