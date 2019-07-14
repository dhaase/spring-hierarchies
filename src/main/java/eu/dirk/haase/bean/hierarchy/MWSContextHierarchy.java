package eu.dirk.haase.bean.hierarchy;

import org.springframework.context.ApplicationContext;

import java.util.*;
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
final class MWSContextHierarchy implements ContextRegistry {

    static final ContextRegistry SINGLETON = new MWSContextHierarchy();
    private final Set<ContextRegistry.BeanType> allBeanTypes;
    private final List<ContextLevel> contextLevelList;

    MWSContextHierarchy() {
        this.contextLevelList = new ArrayList<>();
        this.allBeanTypes = buildHierarchy();
    }

    @Override
    public Set<ContextRegistry.BeanType> allBeanTypes() {
        return allBeanTypes;
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
    public void clearAllApplicationContexts() {
        for (final ContextLevel current : this.contextLevelList) {
            current.clear();
        }
    }

    @Override
    public void applyOnApplicationContextForBeansOf(final ApplicationContextLifeCycle task,
                                                    final BeanType... beans) {
        applyOnApplicationContextForBeansOf(task, new HashSet<>(Arrays.asList(beans)));
    }

    @Override
    public void applyOnApplicationContextForBeansOf(final ApplicationContextLifeCycle task,
                                                    final Set<BeanType> beanTypeSet) {
        boolean isMatch = false;
        for (final ContextLevel current : this.contextLevelList) {
            if (!isMatch) {
                for (final BeanType beanType : beanTypeSet) {
                    if (current.getInheritBeanTypes().contains(beanType)) {
                        isMatch = true;
                        break;
                    }
                }
            }
            // Hier kein 'else' da der aktuelle auch gleich
            // den Task-Call bekommen soll.
            if (isMatch) {
                switch (task) {
                    case clear:
                        current.clear();
                        break;
                    case refresh:
                        current.refresh();
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown task: " + task);
                }
            }
        }
        if (!isMatch) {
            throw new IllegalArgumentException("No ApplicationContext found for BeanTypes: " + beanTypeSet);
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
    public Supplier<ApplicationContext> rootApplicationContext() {
        return this.contextLevelList.get(0).getApplicationContextSupplier();
    }

    @Override
    public long getStartupDate() {
        return this.contextLevelList.get(0).getStartupDate();
    }


}
