package eu.dirk.haase.bean.hierarchy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Supplier;

final class MWSContextLevelFourBuilder {

    private final static Set<ContextRegistry.BeanType> four = EnumSet.of(ContextRegistry.BeanType.Four);


    static ContextLevel create(final ContextLevel parentContextLevel) {
        final Supplier<ApplicationContext> supplier = () -> createApplicationContextTwo(parentContextLevel);
        return new ContextLevel(parentContextLevel.getInheritBeanTypes(), four, supplier);
    }


    private static ApplicationContext createApplicationContextTwo(final ContextLevel parentContextLevel) {
        final ApplicationContext parent = parentContextLevel.getApplicationContextSupplier().get();
        final ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext();
        final String[] resources3 = {"/eu/dirk/haase/application-context-4.xml"};
        applicationContext.setConfigLocations(resources3);
        applicationContext.setParent(parent);
        applicationContext.refresh();
        return applicationContext;
    }


}
