package eu.dirk.haase.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

public class ChildApplicationContextRegistrator implements ApplicationContextAware, InitializingBean {

    public static final String CHILD_APPLICATION_CONTEXT = "childApplicationContext";
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() {
        ConfigurableApplicationContext parent = (ConfigurableApplicationContext) applicationContext.getParent();
        if (parent.containsLocalBean(CHILD_APPLICATION_CONTEXT)) {
            throw new IllegalStateException(CHILD_APPLICATION_CONTEXT + " is already defined");
        } else {
            parent.getBeanFactory().registerSingleton(CHILD_APPLICATION_CONTEXT, applicationContext);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
