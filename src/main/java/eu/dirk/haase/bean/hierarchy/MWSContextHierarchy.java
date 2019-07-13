package eu.dirk.haase.bean.hierarchy;

import org.springframework.context.ApplicationContext;

import java.util.*;
import java.util.function.Supplier;


/**
 * Baut eine Definition von einer Hierarchie von {@link ApplicationContext}e
 * in der MWS auf.
 */
final class MWSContextHierarchy implements ContextRegistry {

    static final ContextRegistry SINGLETON = new MWSContextHierarchy();

    private final List<ContextLevel> contextLevelList;
    private final Set<ContextRegistry.BeanType> allBeanTypes;

    MWSContextHierarchy() {
        this.contextLevelList = new ArrayList<>();
        this.allBeanTypes = buildHierarchy();
    }

    private Set<ContextRegistry.BeanType> buildHierarchy() {
        final ContextLevel one = MWSContextLevelOneBuilder.create();
        final ContextLevel two = MWSContextLevelTwoBuilder.create(one);
        final ContextLevel three = MWSContextLevelThreeBuilder.create(two);
        final ContextLevel four = MWSContextLevelFourBuilder.create(three);
        this.contextLevelList.add(one);
        this.contextLevelList.add(two);
        this.contextLevelList.add(three);
        this.contextLevelList.add(four);
        return four.getInheritBeanTypes();
    }


    @Override
    public Set<ContextRegistry.BeanType> allBeanTypes() {
        return allBeanTypes;
    }


    @Override
    public Supplier<ApplicationContext> rootApplicationContext() {
        return this.contextLevelList.get(0).getApplicationContextSupplier();
    }


    @Override
    public Supplier<ApplicationContext> findApplicationContextForBeansOf(final BeanType... beans) {
        return findApplicationContextForBeansOf(new HashSet<>(Arrays.asList(beans)));
    }


    @Override
    public Supplier<ApplicationContext> findApplicationContextForBeansOf(final Set<BeanType> beanTypeSet) {
        for (final ContextLevel current : this.contextLevelList) {
            if (current.getInheritBeanTypes().containsAll(beanTypeSet)) {
                return current.getApplicationContextSupplier();
            }
        }
        throw new IllegalArgumentException("No ApplicationContext found for BeanTypes: " + beanTypeSet);
    }


}
