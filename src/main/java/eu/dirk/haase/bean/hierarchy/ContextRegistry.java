package eu.dirk.haase.bean.hierarchy;

import org.springframework.context.ApplicationContext;

import java.util.Set;
import java.util.function.Supplier;

/**
 * Zentrale API um ein {@link ApplicationContext} abfragen
 * zu k&ouml;nnen.
 */
public interface ContextRegistry {

    /**
     * Globale Instanz der {@link ContextRegistry}.
     *
     * @return die Instanz der {@link ContextRegistry}.
     */
    static ContextRegistry global() {
        return MWSContextHierarchy.SINGLETON;
    }

    /**
     * Liefert ein {@link Set} von allen {@link BeanType}s die
     * von einem {@link ApplicationContext} in dieser
     * {@link ContextRegistry} erreichbar sind.
     *
     * @return ein {@link Set} von allen {@link BeanType}s.
     */
    Set<ContextRegistry.BeanType> allBeanTypes();

    /**
     * Liefert auf Basis der angegebenen {@link BeanType}s einen {@link Supplier} der
     * einen {@link ApplicationContext} liefert.
     *
     * @param beanTypeSet ein {@link Set} von {@link BeanType}s als Basis f&uuml;r
     *                    die Suche nach dem {@link ApplicationContext}.
     * @return der gefundene {@link ApplicationContext}.
     */
    Supplier<ApplicationContext> findApplicationContextForBeansOf(final Set<BeanType> beanTypeSet);

    /**
     * Liefert auf Basis der angegebenen {@link BeanType}s einen {@link Supplier} der
     * einen {@link ApplicationContext} liefert.
     *
     * @param beanTypes ein Array von {@link BeanType}s als Basis f&uuml;r die Suche
     *                  nach dem {@link ApplicationContext}.
     * @return der gefundene {@link ApplicationContext}.
     */
    Supplier<ApplicationContext> findApplicationContextForBeansOf(final BeanType... beanTypes);

    /**
     * Liefert den Wurzel-{@link ApplicationContext}.
     *
     * @return den Wurzel-{@code ApplicationContext}.
     */
    Supplier<ApplicationContext> rootApplicationContext();

    /**
     * Definiert alle Kategorien von Spring-Beans die sich in
     * einem {@link ApplicationContext} befinden k&ouml;nnen.
     */
    enum BeanType {

        One, Two, Three, Four;

    }
}
