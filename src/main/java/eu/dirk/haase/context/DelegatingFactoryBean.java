package eu.dirk.haase.context;

import eu.dirk.haase.bean.ServiceOne;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DelegatingFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware, BeanNameAware {

    private String beanName;
    private ApplicationContext domainApplicationContext;
    private Class<?> objectType;

    @Override
    public T getObject() throws Exception {
        return newProxyInstance(this.objectType, this.beanName);
    }

    @Override
    public Class<?> getObjectType() {
        return this.objectType;
    }

    public void setObjectType(Class<?> objectType) {
        this.objectType = objectType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @SuppressWarnings("unchecked")
    private T newProxyInstance(Class<?> iface, final String beanName) {
        ClassLoader classLoader = ServiceOne.class.getClassLoader();
        Class<?>[] ifaces = {iface};
        return (T) Proxy.newProxyInstance(classLoader, ifaces, new LazyProxyHandler(this.domainApplicationContext, beanName));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.domainApplicationContext = applicationContext;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    static class LazyProxyHandler implements InvocationHandler {

        private final String beanName;
        private final ApplicationContext domainApplicationContext;
        private Object bean;

        LazyProxyHandler(final ApplicationContext domainApplicationContext, final String beanName) {
            this.domainApplicationContext = domainApplicationContext;
            this.beanName = beanName;
        }

        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
            if (bean == null) {
                final ApplicationContext ctx = this.domainApplicationContext.getBean("childApplicationContext", ApplicationContext.class);
                bean = ctx.getBean(beanName);
            }
            if ("toString".equals(method.getName())) {
                return "proxy";
            }
            return method.invoke(bean, args);
        }
    }

}
