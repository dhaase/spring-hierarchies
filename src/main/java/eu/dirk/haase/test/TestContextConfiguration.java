package eu.dirk.haase.test;


import eu.dirk.haase.bean.hierarchy.ContextRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.SmartContextLoader;
import org.springframework.test.context.TestExecutionListeners;

import java.lang.annotation.*;

/**
 * Mit dieser Meta-Annotation k&ouml;nnen Application-Context Hierarchien
 * f&uuml;r Integrations-Test durch die Angabe von Namen der Java-5-Enum
 * {@link ContextRepository.BeanType}s erzeugt werden.
 * <p>
 * Durch die konkrete Loader-Klasse (Ableitungen des {@link TestContextLoader})
 * wird festgelegt:
 * <ul>
 * <li>welche Application-Context Hierarchie verwendet wird</li>
 * <li>welche Namen f&uuml;r die konkrete Java-5-Enum
 * verwendet werden m&uuml;ssen</li>
 * <li>und welche {@link ContextRepository} abgefragt wird.</li>
 * </ul>
 *
 * @see ContextConfiguration
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@TestExecutionListeners(
        listeners = TestTestExecutionListener.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@ContextConfiguration(initializers = TestContextConfiguration.NullApplicationContextInitializer.class)
public @interface TestContextConfiguration {

    /**
     * Alias f&uuml;r {@link #beanCategories()}.
     *
     * @see #beanCategories()
     */
    @AliasFor("beanCategories")
    String[] value() default {};

    /**
     * Array von Namen der Java-5-Enum {@link ContextRepository.BeanType}s
     * als Basis f&uuml;r die Suche nach dem {@link ApplicationContext} in
     * der {@link ContextRepository}.
     * <p>
     * Sie dienen als Basis f&uuml;r die Suche nach dem passenden
     * {@link ApplicationContext}.
     */
    @AliasFor("value")
    String[] beanCategories() default {};

    /**
     * Die Ressourcen-Positionen, die zum Laden eines ApplicationContext
     * verwendet werden sollen.
     * <p>
     * Dieses Attribut hat dieselben Eigenschaften wie
     * {@link ContextConfiguration#locations()}, wobei
     * {@link ContextConfiguration#inheritLocations()} auf {@code true} steht.
     *
     * @see ContextConfiguration#locations()
     */
    @AliasFor(annotation = ContextConfiguration.class, attribute = "locations")
    String[] locations() default {};

    /**
     * Die mit Annotationen versehenen Klassen, die zum Laden
     * eines ApplicationContext verwendet werden sollen.
     * <p>
     * Dieses Attribut hat dieselben Eigenschaften wie
     * {@link ContextConfiguration#classes()}.
     *
     * @see ContextConfiguration#classes()
     */
    @AliasFor(annotation = ContextConfiguration.class, attribute = "classes")
    Class<?>[] classes() default {};

    /**
     * Der Typ von {@link TestContextLoader}, der zum Laden eines
     * ApplicationContext verwendet werden soll.
     * <p>
     * Dieses Attribut unterscheidet von {@link ContextConfiguration#loader()}
     * insofern das Typ des Loaders auf Ableitungen des {@link TestContextLoader}
     * festgelegt ist.
     *
     * @see ContextConfiguration#loader()
     */
    @AliasFor(annotation = ContextConfiguration.class, attribute = "loader")
    Class<? extends TestContextLoader> loader() default TestContextLoader.class;

    /**
     * Null-Implementation zur Verhinderung einer IllegalStateException beim Laden bzw. Erzeugen
     * {@link ApplicationContext}es.
     * <p>
     * Ist vermutlich ein Fehler des Spring-Frameworks, denn wenn der {@link SmartContextLoader}
     * definiert ist, dann gibt es keine Notwendigkeit f&uuml;r weitere Definitionen und
     * schon gar nicht f&uuml;r einen {@link ApplicationContextInitializer},
     */
    final class NullApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(final ConfigurableApplicationContext context) {
        }
    }
}
