package eu.dirk.haase.bean;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;

public class SpringEnvironment implements ApplicationContextAware, Environment {

    private Environment environment;

    public String[] getActiveProfiles() {
        return environment.getActiveProfiles();
    }

    public String[] getDefaultProfiles() {
        return environment.getDefaultProfiles();
    }

    public boolean acceptsProfiles(String... profiles) {
        return environment.acceptsProfiles(profiles);
    }

    public boolean containsProperty(String key) {
        return environment.containsProperty(key);
    }

    public String getProperty(String key) {
        return environment.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return environment.getProperty(key, defaultValue);
    }

    public <T> T getProperty(String key, Class<T> targetType) {
        return environment.getProperty(key, targetType);
    }

    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return environment.getProperty(key, targetType, defaultValue);
    }

    @Deprecated
    public <T> Class<T> getPropertyAsClass(String key, Class<T> targetType) {
        return environment.getPropertyAsClass(key, targetType);
    }

    public String getRequiredProperty(String key) throws IllegalStateException {
        return environment.getRequiredProperty(key);
    }

    public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
        return environment.getRequiredProperty(key, targetType);
    }

    public String resolvePlaceholders(String text) {
        return environment.resolvePlaceholders(text);
    }

    public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
        return environment.resolveRequiredPlaceholders(text);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.environment = applicationContext.getEnvironment();
    }
}
