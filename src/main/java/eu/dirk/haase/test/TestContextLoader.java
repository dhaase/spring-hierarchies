package eu.dirk.haase.test;

import eu.dirk.haase.bean.hierarchy.ContextRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextLoader;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.SmartContextLoader;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.GenericXmlContextLoader;

import java.lang.reflect.AnnotatedElement;
import java.util.function.Supplier;

/**
 * Strategieimplementation zum Laden eines {@link ApplicationContext} f&uuml;r
 * einen Integrationstest, der vom Spring TestContext Framework verwaltet wird.
 */
public abstract class TestContextLoader implements SmartContextLoader {

    private SmartContextLoader smartContextLoader = new NullSmartContextLoader();

    public TestContextLoader() {
        super();
    }

    /**
     * Verarbeitet die {@link ContextConfigurationAttributes} für eine bestimmte Testklasse.
     * <p>
     * Ist die Testklasse mit annotiert mit Context-Configurationen basierend:
     * <ul>
     * <li>auf XML, dann wird der {@link GenericXmlContextLoader}
     * verwendet.</li>
     * <li>auf Configuration-Klassen,
     * dann wird der {@link AnnotationConfigContextLoader} verwendet.</li>
     * </ul>
     * <p>
     * Ist die Testklasse mit keiner zus&auml;tzlichen Context-Configuration ausgestattet,
     * dann wird kein {@link SmartContextLoader} verwendet.
     *
     * @param ctxConfigAttributes die zu verarbeitenden {@link ContextConfigurationAttributes}.
     */
    @Override
    public final void processContextConfiguration(final ContextConfigurationAttributes ctxConfigAttributes) {
        if (ctxConfigAttributes.hasLocations() && ctxConfigAttributes.hasClasses()) {
            throw new IllegalArgumentException("The Test Spring Context configuration " +
                    "can define either 'locations' or 'classes', but not both.");
        }
        if (ctxConfigAttributes.hasLocations()) {
            this.smartContextLoader = new GenericXmlContextLoader();
        } else if (ctxConfigAttributes.hasClasses()) {
            this.smartContextLoader = new AnnotationConfigContextLoader();
        } else {
            this.smartContextLoader = new NullSmartContextLoader();
        }
        this.smartContextLoader.processContextConfiguration(ctxConfigAttributes);
    }

    /**
     * L&auml;dt einen neuen {@link ApplicationContext} basierend auf der angegebenen
     * Konfiguration des zusammengef&uuml;hrten Kontextes, konfiguriert den
     * Kontext und gibt den Kontext schließlich in einem vollst&auml;ndig
     * aktualisierten Zustand zur&uuml;ck.
     * <p>
     * Ist die Testklasse mit keiner zus&auml;tzlichen Context-Configuration ausgestattet,
     * dann wird ein {@link ApplicationContext} als Application-Context Hierarchie
     * f&uuml;r Integrations-Test zur&uuml;ckgliefert.
     * <p>
     * Ist die Testklasse mit zus&auml;tzlichen Context-Configurationen ausgestattet,
     * dann werden zwei ApplicationContexte geladen:
     * <ul>
     * <li>Einmal ein {@link ApplicationContext} als Application-Context Hierarchie
     * f&uuml;r Integrations-Test als Parent der Testklassen Kontext-Konfiguration</li>
     * <li>und ein {@link ApplicationContext} f&uuml;r zus&auml;tzliche Kontext-Konfiguration
     * der Testklasse. Dieser {@link ApplicationContext} wird dann unmittelbar
     * zur&uuml;ckgliefert.</li>
     * </ul>
     *
     * @param mergedCtxConfig Die Konfiguration des zusammengef&uuml;hrten Kontexts,
     *                        die zum Laden des {@link ApplicationContext} verwendet werden soll.
     * @return der neue geladene {@link ApplicationContext}.
     * @throws Exception wird ausgel&ouml;st wenn ein Fehler auftritt.
     */
    @Override
    public final ApplicationContext loadContext(final MergedContextConfiguration mergedCtxConfig) throws Exception {
        final String[] beanTypes = getBeanCategories(mergedCtxConfig);
        final ApplicationContext mainContext = findApplicationContextForBeansOf(beanTypes);
        final ApplicationContext testContext = this.smartContextLoader.loadContext(mergedCtxConfig);
        if (testContext == null) {
            return mainContext;
        } else {
            ((ConfigurableApplicationContext) testContext).setParent(mainContext);
            return testContext;
        }
    }

    /**
     * Ein Array von Namen der Java-5-Enum {@link ContextRepository.BeanType}s.
     * <p>
     * Sie dienen als Basis f&uuml;r die Suche nach dem passenden
     * {@link ApplicationContext}.
     *
     * @param mergedCtxConfig Die Konfiguration des zusammengef&uuml;hrten Kontexts,
     *                        die zum Laden des {@link ApplicationContext} verwendet werden soll.
     * @return ein Array von Namen der Java-5-Enum.
     */
    private String[] getBeanCategories(final MergedContextConfiguration mergedCtxConfig) {
        final Class<? extends AnnotatedElement> testClass = (Class<? extends AnnotatedElement>) mergedCtxConfig.getTestClass();
        final TestContextConfiguration testContextConfiguration = AnnotatedElementUtils.getMergedAnnotation(testClass, TestContextConfiguration.class);
        return testContextConfiguration.beanCategories();
    }

    /**
     * Liefert auf Basis der angegebenen {@link ContextRepository.BeanType}s einen {@link Supplier}
     * der einen {@link ApplicationContext} liefert.
     *
     * @param beanTypes Array von Namen der Java-5-Enum {@link ContextRepository.BeanType}s
     *                  als Basis f&uuml;r die Suche nach dem {@link ApplicationContext} in
     *                  der {@link ContextRepository}.
     * @return der gefundene {@link ApplicationContext}.
     * @throws IllegalArgumentException wird ausgel&ouml;st wenn keine
     *                                  {@code ApplicationContext}-Defintion
     *                                  gefunden wurde.
     */
    abstract protected ApplicationContext findApplicationContextForBeansOf(final String[] beanTypes);


    /**
     * Diese Methode wird nicht aufgerufen und wird nicht unterst&uuml;tzt.
     * <p>
     * Da diese Klasse vom Typ {@link SmartContextLoader} ist gibt es keine sinnvolle
     * Implementation f&uuml;r diese Methode {@link ContextLoader#processLocations(Class, String...)}.
     */
    @Override
    public final String[] processLocations(final Class<?> aClass, final String... strings) {
        throw new UnsupportedOperationException();
    }

    /**
     * Diese Methode wird nicht aufgerufen und wird nicht unterst&uuml;tzt.
     * <p>
     * Da diese Klasse vom Typ {@link SmartContextLoader} ist gibt es keine sinnvolle
     * Implementation f&uuml;r diese Methode {@link ContextLoader#loadContext(String...)}.
     */
    @Override
    public final ApplicationContext loadContext(final String... strings) {
        throw new UnsupportedOperationException();
    }


    static class NullSmartContextLoader implements SmartContextLoader {

        @Override
        public void processContextConfiguration(final ContextConfigurationAttributes configAttributes) {

        }

        @Override
        public ApplicationContext loadContext(final MergedContextConfiguration mergedConfig) {
            return null;
        }

        @Override
        public String[] processLocations(final Class<?> clazz, final String... locations) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ApplicationContext loadContext(final String... locations) {
            throw new UnsupportedOperationException();
        }
    }
}
