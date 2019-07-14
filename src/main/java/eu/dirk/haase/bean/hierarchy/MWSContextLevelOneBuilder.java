package eu.dirk.haase.bean.hierarchy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Supplier;

final class MWSContextLevelOneBuilder {

    private final static Set<ContextRepository.BeanType> thisBeanTypes = EnumSet.of(ContextRepository.BeanType.One);


    static ContextLevel create() {
        final Supplier<ApplicationContext> supplier = () -> createApplicationContext();
        return new ContextLevel(thisBeanTypes, supplier);
    }


    private static ApplicationContext createApplicationContext() {
        final ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext();
        final String[] resources3 = {"/eu/dirk/haase/application-context-1.xml"};
        applicationContext.setConfigLocations(resources3);
        applicationContext.refresh();
        return applicationContext;
    }


}
