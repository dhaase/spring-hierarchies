package eu.dirk.haase.bean.hierarchy;

import org.springframework.context.ApplicationContext;

import java.util.Set;
import java.util.function.Supplier;

public interface ContextRegistry {

    static ContextRegistry instance() {
        return MWSContextHierarchy.SINGLETON;
    }

    Set<ContextRegistry.BeanType> allBeanTypes();

    Supplier<ApplicationContext> findApplicationContextForBeansOf(final Set<BeanType> beanTypeSet);

    Supplier<ApplicationContext> findApplicationContextForBeansOf(final BeanType... beanTypes);

    Supplier<ApplicationContext> rootApplicationContext();

    enum BeanType {

        One, Two, Three, Four;

    }
}
