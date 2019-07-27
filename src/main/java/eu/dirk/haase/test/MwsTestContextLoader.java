package eu.dirk.haase.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.function.Function;

public final class MwsTestContextLoader extends TestContextLoader {

    public MwsTestContextLoader() {
        super();
    }

    @Override
    protected ApplicationContext findApplicationContextForBeansOf(final String[] beanTypes) {
        ApplicationContext cfg = new ClassPathXmlApplicationContext("/eu/dirk/haase/application-context-loader-config.xml");
        Function<String[], ApplicationContext> supplier = cfg.getBean("contextRepository", Function.class);
        return supplier.apply(beanTypes);
    }


}
