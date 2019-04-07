package eu.dirk.haase.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static eu.dirk.haase.context.ChildApplicationContextRegistrator.CHILD_APPLICATION_CONTEXT;

public class LazyDelegatingFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware, BeanNameAware {

    private String beanName;
    private ApplicationContext domainApplicationContext;
    private Class<?> objectType;

    @Override
    public T getObject() {
        return newProxyInstance();
    }

    @Override
    public Class<?> getObjectType() {
        return this.objectType;
    }

    public void setObjectType(final Class<?> objectType) {
        this.objectType = objectType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @SuppressWarnings("unchecked")
    private T newProxyInstance() {
        final ClassLoader classLoader = LazyDelegatingFactoryBean.class.getClassLoader();
        final Class<?>[] ifaces = {this.objectType};
        return (T) Proxy.newProxyInstance(classLoader, ifaces, new LazyProxyHandler(this.domainApplicationContext, this.objectType, this.beanName));
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.domainApplicationContext = applicationContext;
    }

    @Override
    public void setBeanName(final String beanName) {
        this.beanName = beanName;
    }

    private static class LazyProxyHandler implements InvocationHandler {

        private final String beanName;
        private final ApplicationContext domainApplicationContext;
        private final Class<?> objectType;
        private Object bean;

        LazyProxyHandler(final ApplicationContext domainApplicationContext, final Class<?> objectType, final String beanName) {
            this.domainApplicationContext = domainApplicationContext;
            this.objectType = objectType;
            this.beanName = beanName;
        }

        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
            if ((bean == null) && this.domainApplicationContext.containsBean(CHILD_APPLICATION_CONTEXT)) {
                final ApplicationContext childApplicationContext = this.domainApplicationContext.getBean(CHILD_APPLICATION_CONTEXT, ApplicationContext.class);
                if (childApplicationContext.containsLocalBean(beanName)) {
                    bean = childApplicationContext.getBean(beanName);
                } else {
                    final String[] nameArray = childApplicationContext.getBeanNamesForType(objectType);
                    if (nameArray.length == 1) {
                        bean = childApplicationContext.getBean(this.objectType);
                    } else if (nameArray.length > 1) {
                        throw new IllegalStateException("No qualifying bean of type '" +
                                this.objectType +
                                "' available: expected single matching bean but found " +
                                nameArray.length);
                    } else {
                        // mock
                    }
                }
            }
            return method.invoke(bean, args);
        }
    }

}
