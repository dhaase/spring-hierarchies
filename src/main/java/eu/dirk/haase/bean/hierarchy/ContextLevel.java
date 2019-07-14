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
    private final Set<ContextRepository.BeanType> inheritBeanTypes;

    ContextLevel(final Set<ContextRepository.BeanType> thisBeanTypes,
                 final Supplier<ApplicationContext> applicationContextSupplier) {
        this.applicationContextSupplier = new MemoizingSupplier(applicationContextSupplier);
        this.inheritBeanTypes = Collections.unmodifiableSet(thisBeanTypes);
    }

    ContextLevel(final Set<ContextRepository.BeanType> parentBeanTypes,
                 final Set<ContextRepository.BeanType> thisBeanTypes,
                 final Supplier<ApplicationContext> applicationContextSupplier) {
        this.applicationContextSupplier = new MemoizingSupplier(applicationContextSupplier);
        final HashSet<ContextRepository.BeanType> beanTypeSet = new HashSet<>(thisBeanTypes);
        beanTypeSet.addAll(parentBeanTypes);
        this.inheritBeanTypes = Collections.unmodifiableSet(beanTypeSet);
    }

    static ContextLevel create(final ContextLevel parentContextLevel,
                               final Set<ContextRepository.BeanType> requiredBeanTypes,
                               final Set<ContextRepository.BeanType> thisBeanTypes,
                               final Supplier<ApplicationContext> applicationContextSupplier) {
        final Supplier<ApplicationContext> supplier = applicationContextSupplier;
        if (!parentContextLevel.getInheritBeanTypes().containsAll(requiredBeanTypes)) {
            throw new IllegalArgumentException("ApplicationContext-Parent has no access to required BeanTypes: " +
                    requiredBeanTypes);
        }
        return new ContextLevel(parentContextLevel.getInheritBeanTypes(), thisBeanTypes, supplier);
    }

    void clear() {
        applicationContextSupplier.invalidate();
    }

    Supplier<ApplicationContext> getApplicationContextSupplier() {
        return applicationContextSupplier;
    }

    Set<ContextRepository.BeanType> getInheritBeanTypes() {
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
