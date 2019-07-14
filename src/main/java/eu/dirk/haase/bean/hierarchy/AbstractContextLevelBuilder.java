package eu.dirk.haase.bean.hierarchy;

import org.springframework.context.ApplicationContext;

import java.util.Set;
import java.util.function.Supplier;

final class AbstractContextLevelBuilder {


    static ContextLevel create(final ContextLevel parentContextLevel,
                               final Set<ContextRegistry.BeanType> requiredBeanTypes,
                               final Set<ContextRegistry.BeanType> thisBeanTypes,
                               final Supplier<ApplicationContext> applicationContextSupplier) {
        final Supplier<ApplicationContext> supplier = applicationContextSupplier;
        if (!parentContextLevel.getInheritBeanTypes().containsAll(requiredBeanTypes)) {
            throw new IllegalArgumentException("ApplicationContext-Parent has no access to required BeanTypes: " +
                    requiredBeanTypes);
        }
        return new ContextLevel(parentContextLevel.getInheritBeanTypes(), thisBeanTypes, supplier);
    }

}
