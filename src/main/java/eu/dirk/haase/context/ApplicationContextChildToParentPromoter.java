package eu.dirk.haase.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

public class ApplicationContextChildToParentPromoter implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        ConfigurableApplicationContext parent = (ConfigurableApplicationContext) applicationContext.getParent();
        if (parent.containsLocalBean("childApplicationContext")) {
            throw new IllegalStateException("childApplicationContext is already defined");
        } else {
            parent.getBeanFactory().registerSingleton("childApplicationContext", applicationContext);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
