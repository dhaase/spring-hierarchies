package eu.dirk.haase.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

public class ChildApplicationContextRegistrator implements ApplicationContextAware, InitializingBean {

    public static final String CHILD_APPLICATION_CONTEXT = "childApplicationContext";
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() {
        final ConfigurableApplicationContext parentApplicationContext = (ConfigurableApplicationContext) applicationContext.getParent();
        if (parentApplicationContext.containsLocalBean(CHILD_APPLICATION_CONTEXT)) {
            throw new IllegalStateException(CHILD_APPLICATION_CONTEXT + " is already defined in ApplicationContext(" + parentApplicationContext.getId() + ")");
        } else {
            final ConfigurableListableBeanFactory beanFactory = parentApplicationContext.getBeanFactory();
            beanFactory.registerSingleton(CHILD_APPLICATION_CONTEXT, applicationContext);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
