package eu.dirk.haase.bean.hierarchy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.EnumSet;
import java.util.Set;

final class MWSContextLevelThreeBuilder {

    private final static Set<ContextRepository.BeanType> thisBeanTypes = EnumSet.of(ContextRepository.BeanType.Three);
    private final static Set<ContextRepository.BeanType> requiredBeanTypes = EnumSet.of(ContextRepository.BeanType.One,
            ContextRepository.BeanType.Two);


    static ContextLevel create(final ContextLevel parentContextLevel) {
        return AbstractContextLevelBuilder.create(parentContextLevel,
                requiredBeanTypes,
                thisBeanTypes,
                () -> createApplicationContext(parentContextLevel));
    }


    private static ApplicationContext createApplicationContext(final ContextLevel parentContextLevel) {
        final ApplicationContext parent = parentContextLevel.getApplicationContextSupplier().get();
        final ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext();
        final String[] resources3 = {"/eu/dirk/haase/application-context-3.xml"};
        applicationContext.setConfigLocations(resources3);
        applicationContext.setParent(parent);
        applicationContext.refresh();
        return applicationContext;
    }


}
