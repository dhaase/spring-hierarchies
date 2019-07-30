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
     * Schaltet bei {@code true} die Konfiguration basierend auf Annotationen ein.
     * <p>
     * In Regel m&ouml;chte man das der {@link ApplicationContext} auch Annotationen
     * wie {@code @Autowired} entsprechend beachtet, daher ist der Vorgabe-Wert auf
     * {@code true} gesetzt.
     * <p>
     * Den gleichen Effekt erreicht man auch dann, wenn in der XML-Konfiguration
     * durchg&auml;ngig {@code <context:annotation-config/>} angegeben worden
     * ist.
     * <p>
     * Es ist daher durchaus sinnvoll diesen Schalter {@code false} zu stellen
     * um so sicherzustellen das in der XML-Konfiguration
     * {@code <context:annotation-config/>} angegeben worden ist.
     * <p>
     * Ist dieser Schalter auf {@code false} gesetzt, dann werden Annotationen
     * wie {@code @Autowired} schlicht ignoriert.
     *
     * @return schaltet bei {@code true} die Konfiguration basierend auf
     * Annotationen ein.
     */
    @AliasFor(annotation = TestContextConfiguration.class, attribute = "enableAnnotationBasedConfiguration")
    boolean enableAnnotationBasedConfiguration() default true;

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
