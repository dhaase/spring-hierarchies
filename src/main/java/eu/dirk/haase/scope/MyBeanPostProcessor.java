package eu.dirk.haase.scope;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

public class MyBeanPostProcessor implements BeanPostProcessor {
    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    private String contextId;
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(contextId + " 1 Initialization: " + beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(contextId + " 2 Initialization: " + beanName + "; contains: " + TenantScope.beanScopeMap.get(beanName));
        for(Field field : bean.getClass().getFields()) {
            try {
                System.out.println(contextId + "                  " + TenantScope.beanScopeMap.get(field.get(bean)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }
}
