package eu.dirk.haase.bean.hierarchy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Definition einer {@link ApplicationContext}-Ebene.
 */
final class ContextLevel {

    private final MemoizingSupplier<ApplicationContext> applicationContextSupplier;
    private final Set<ContextRegistry.BeanType> inheritBeanTypes;

    ContextLevel(final Set<ContextRegistry.BeanType> thisBeanTypes,
                 final Supplier<ApplicationContext> applicationContextSupplier) {
        this.applicationContextSupplier = new MemoizingSupplier(applicationContextSupplier);
        this.inheritBeanTypes = Collections.unmodifiableSet(thisBeanTypes);
    }

    ContextLevel(final Set<ContextRegistry.BeanType> parentBeanTypes,
                 final Set<ContextRegistry.BeanType> thisBeanTypes,
                 final Supplier<ApplicationContext> applicationContextSupplier) {
        this.applicationContextSupplier = new MemoizingSupplier(applicationContextSupplier);
        final HashSet<ContextRegistry.BeanType> beanTypeSet = new HashSet<>(thisBeanTypes);
        beanTypeSet.addAll(parentBeanTypes);
        this.inheritBeanTypes = Collections.unmodifiableSet(beanTypeSet);
    }

    void clear() {
        applicationContextSupplier.invalidate();
    }

    Supplier<ApplicationContext> getApplicationContextSupplier() {
        return applicationContextSupplier;
    }

    Set<ContextRegistry.BeanType> getInheritBeanTypes() {
        return inheritBeanTypes;
    }

    long getStartupDate() {
        if (applicationContextSupplier.isInitialized()) {
            return applicationContextSupplier.get().getStartupDate();
        }
        return 0;
    }

    void refresh() {
        if (applicationContextSupplier.isInitialized()) {
            ((ConfigurableApplicationContext) applicationContextSupplier.get()).refresh();
        }
    }

}
