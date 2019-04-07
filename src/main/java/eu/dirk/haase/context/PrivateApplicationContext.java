package eu.dirk.haase.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

public class PrivateApplicationContext implements ApplicationContextAware, InitializingBean {

    private String[] configLocations;
    private ConfigurableApplicationContext domainApplicationContext;
    private ConfigurableApplicationContext parentApplicationContext;

    @Override
    public void afterPropertiesSet() {
        initConfigLocations();
        this.domainApplicationContext = new ClassPathXmlApplicationContext(configLocations, parentApplicationContext);

        parentApplicationContext.getBeanFactory().registerSingleton("serviceOne", this.domainApplicationContext.getBean("serviceOne"));
        parentApplicationContext.getBeanFactory().registerSingleton("serviceTwo", this.domainApplicationContext.getBean("serviceTwo"));
    }

    public ApplicationContext getDomainApplicationContext() {
        return domainApplicationContext;
    }

    protected String resolvePath(String path) {
        return this.parentApplicationContext.getEnvironment().resolveRequiredPlaceholders(path);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.parentApplicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    public void setConfigLocations(String... locations) {
        this.configLocations = locations;
    }

    public void initConfigLocations() {
        String[] locations = this.configLocations;
        if (locations != null) {
            Assert.noNullElements(locations, "Config locations must not be null");
            this.configLocations = new String[locations.length];
            for (int i = 0; i < locations.length; i++) {
                this.configLocations[i] = resolvePath(locations[i]).trim();
            }
        } else {
            this.configLocations = null;
        }
    }

}
