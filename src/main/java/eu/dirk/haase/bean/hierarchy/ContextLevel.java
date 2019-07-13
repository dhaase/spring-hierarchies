package eu.dirk.haase.bean.hierarchy;

import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Definition einer {@link ApplicationContext}-Ebene.
 */
final class ContextLevel {

    private final Supplier<ApplicationContext> applicationContextSupplier;
    private final Set<ContextRegistry.BeanType> inheritBeanTypes;

    ContextLevel(final Set<ContextRegistry.BeanType> thisBeanTypes,
                 final Supplier<ApplicationContext> applicationContextSupplier) {
        this.applicationContextSupplier = applicationContextSupplier;
        this.inheritBeanTypes = Collections.unmodifiableSet(thisBeanTypes);
    }

    ContextLevel(final Set<ContextRegistry.BeanType> parentBeanTypes,
                 final Set<ContextRegistry.BeanType> thisBeanTypes,
                 final Supplier<ApplicationContext> applicationContextSupplier) {
        final HashSet<ContextRegistry.BeanType> beanTypeSet = new HashSet<>(thisBeanTypes);
        beanTypeSet.addAll(parentBeanTypes);
        this.applicationContextSupplier = applicationContextSupplier;
        this.inheritBeanTypes = Collections.unmodifiableSet(beanTypeSet);
    }

    Supplier<ApplicationContext> getApplicationContextSupplier() {
        return applicationContextSupplier;
    }

    Set<ContextRegistry.BeanType> getInheritBeanTypes() {
        return inheritBeanTypes;
    }

}
