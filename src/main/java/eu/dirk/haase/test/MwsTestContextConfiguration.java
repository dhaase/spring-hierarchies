package eu.dirk.haase.test;

import eu.dirk.haase.bean.hierarchy.ContextRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.*;

/**
 * Diese Meta-Annotation f&uuml;r die MWS, definiert die Standard
 * Application-Context Hierarchie f&uuml;r Integrations-Test von
 * JpaCommands.
 * <p>
 * Durch die konkrete Loader-Klasse {@link MwsTestContextLoader}
 * wird festgelegt:
 * <ul>
 * <li>das die Application-Context Hierarchie der MWS aufgebaut wird</li>
 * <li>das die Namen f&uuml;r die konkrete Java-5-Enum
 * {@link ContextRepository.BeanType} verwendet werden muss</li>
 * <li>und das die {@link ContextRepository} abgefragt wird.</li>
 * </ul>
 *
 * @see TestContextConfiguration
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@TestContextConfiguration(
        loader = MwsTestContextLoader.class
)
public @interface MwsTestContextConfiguration {

    /**
     * Alias f&uuml;r {@link #beanCategories()}.
     */
    @AliasFor(annotation = TestContextConfiguration.class, attribute = "value")
    String[] value() default {"One", "Two"};

    /**
     * Array von Namen der Java-5-Enum {@link ContextRepository.BeanType}s
     * als Basis f&uuml;r die Suche nach dem {@link ApplicationContext} in
     * der {@link ContextRepository}.
     * <p>
     * Durch Definition dieses Attributes wird die Vorgabe-Definition dieser
     * Annotation ersetzt.
     *
     * @see TestContextConfiguration#locations()
     */
    @AliasFor(annotation = TestContextConfiguration.class, attribute = "beanCategories")
    String[] beanCategories() default {"One", "Two"};

    /**
     * Die Ressourcen-Positionen, die zum Laden eines ApplicationContext
     * verwendet werden sollen.
     * <p>
     * Dieses Attribut hat dieselben Eigenschaften wie
     * {@link ContextConfiguration#locations()}, wobei
     * {@link ContextConfiguration#inheritLocations()} stets auf {@code true}
     * steht.
     *
     * @see ContextConfiguration#locations()
     */
    @AliasFor(annotation = ContextConfiguration.class, attribute = "locations")
    String[] locations() default {};


}
