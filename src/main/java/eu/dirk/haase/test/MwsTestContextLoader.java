package eu.dirk.haase.test;

import eu.dirk.haase.bean.hierarchy.ContextRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Strategieimplementation zum Laden eines {@link ApplicationContext} f&uuml;r
 * einen Integrationstest in der MWS, der vom Spring TestContext Framework
 * verwaltet wird.
 */
public final class MwsTestContextLoader extends TestContextLoader {

    public MwsTestContextLoader() {
        super();
    }

    /**
     * Liefert auf Basis der angegebenen {@link ContextRepository.BeanType}s einen
     * {@link ApplicationContext}.
     *
     * @param beanTypes Array von Namen der Java-5-Enum {@link ContextRepository.BeanType}s
     *                  als Basis f&uuml;r die Suche nach dem {@link ApplicationContext} in
     *                  der {@link ContextRepository}.
     * @return der gefundene {@link ApplicationContext}.
     * @throws IllegalArgumentException wird ausgel&ouml;st wenn keine
     *                                  {@code ApplicationContext}-Defintion
     *                                  gefunden wurde.
     */
    @Override
    protected ApplicationContext findApplicationContextForBeansOf(final String[] beanTypes) {
        final ApplicationContext cfg = new ClassPathXmlApplicationContext("/eu/dirk/haase/application-context-loader-config.xml");
        final Function<String[], ApplicationContext> supplier = cfg.getBean("contextRepository", Function.class);
        return supplier.apply(beanTypes);
    }


}
