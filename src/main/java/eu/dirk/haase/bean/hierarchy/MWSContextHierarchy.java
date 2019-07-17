package eu.dirk.haase.bean.hierarchy;

import org.springframework.context.ApplicationContext;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 * Baut eine Definition einer Hierarchie von {@link ApplicationContext}en
 * in der MWS auf.
 * <p>
 * Es werden die {@code ApplicationContext}e selbst sowie die
 * Parent-/Child-Beziehungen definiert ohne sie jedoch
 * tats&auml;chlich zu initialisieren.
 * <p>
 * Die Definition erfolgt daher lazy.
 */
final class MWSContextHierarchy implements ContextRepository {

    static final ContextRepository SINGLETON = new MWSContextHierarchy();
    private final Set<ContextRepository.BeanType> allBeanTypes;
    private final List<ContextLevel> contextLevelList;

    MWSContextHierarchy() {
        this(MWSContextHierarchy::buildHierarchy);
    }

    MWSContextHierarchy(final Function<List<ContextLevel>, Set<BeanType>> hierarchyBuilder) {
        this.contextLevelList = new ArrayList<>();
        this.allBeanTypes = hierarchyBuilder.apply(this.contextLevelList);
    }

    private static Set<ContextRepository.BeanType> buildHierarchy(final List<ContextLevel> contextLevelList) {
        final ContextLevel one = MWSContextLevelOneBuilder.create();
        final ContextLevel two = MWSContextLevelTwoBuilder.create(one);
        final ContextLevel three = MWSContextLevelThreeBuilder.create(two);
        final ContextLevel four = MWSContextLevelFourBuilder.create(three);
        contextLevelList.add(one);
        contextLevelList.add(two);
        contextLevelList.add(three);
        contextLevelList.add(four);
        return four.getInheritBeanTypes();
    }

    @Override
    public Set<ContextRepository.BeanType> allBeanTypes() {
        return allBeanTypes;
    }

    @Override
    public void applyOnApplicationContextForBeansOf(final ApplicationContextLifeCycle task,
                                                    final BeanType... beans) {
        applyOnApplicationContextForBeansOf(task, new HashSet<>(Arrays.asList(beans)));
    }

    @Override
    public void applyOnApplicationContextForBeansOf(final ApplicationContextLifeCycle task,
                                                    final Set<BeanType> beanTypeSet) {
        for (final ContextLevel current : this.contextLevelList) {
            if (current.getInheritBeanTypes().containsAll(beanTypeSet)) {
                switch (task) {
                    case clear:
                        current.clear();
                        break;
                    case clearCascading:
                        current.clearCascading();
                        break;
                    case refresh:
                        current.refresh();
                        break;
                    case refreshCascading:
                        current.refreshCascading();
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown task: " + task);
                }
                return;
            }
        }
        throw new IllegalArgumentException("No ApplicationContext found for BeanTypes: " + beanTypeSet);
    }

    @Override
    public void clearAllApplicationContexts() {
        for (final ContextLevel current : this.contextLevelList) {
            current.clear();
        }
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

    @Override
    public Supplier<ApplicationContext> findApplicationContextForBeansOf(final BeanType... beans) {
        return findApplicationContextForBeansOf(new HashSet<>(Arrays.asList(beans)));
    }

    @Override
    public long getStartupDate() {
        return this.contextLevelList.get(0).getStartupDate();
    }

    @Override
    public Supplier<ApplicationContext> rootApplicationContext() {
        return this.contextLevelList.get(0).getApplicationContextSupplier();
    }


}
