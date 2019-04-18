package eu.dirk.haase;

import eu.dirk.haase.bean.ServiceTwo;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.AutowireCandidateQualifier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Main {


    public static void main(String... args) throws ExecutionException, InterruptedException {
        String[] resources1 = {"/eu/dirk/haase/application-context-datasources.xml"};
        ClassPathXmlApplicationContext ctx_datasources = new ClassPathXmlApplicationContext();
        ctx_datasources.setId("ctx_datasources");
        ctx_datasources.setDisplayName("ctx_datasources");
        ctx_datasources.setConfigLocations(resources1);
        ctx_datasources.refresh();

        String[] resources2 = {"/eu/dirk/haase/application-context-all-domains.xml"};
        ClassPathXmlApplicationContext ctx_all_domains = new ClassPathXmlApplicationContext();
        ctx_all_domains.setId("ctx_all_domains");
        ctx_all_domains.setDisplayName("ctx_all_domains");
        ctx_all_domains.setParent(ctx_datasources);
        ctx_all_domains.setConfigLocations(resources2);
        ctx_all_domains.refresh();

        String value = getBean(ctx_all_domains, String.class, MyQualifier.class, "one1");
        System.out.println(value);

        String[] resources3 = {"/eu/dirk/haase/application-context-main.xml"};
        ClassPathXmlApplicationContext ctx_main = new ClassPathXmlApplicationContext();
        ctx_main.setId("ctx_main");
        ctx_main.setDisplayName("ctx_main");
        ctx_main.setParent(ctx_all_domains);
        ctx_main.setConfigLocations(resources3);
        ctx_main.refresh();

        ServiceTwo serviceTwo = ctx_main.getBean("one.serviceTwo", ServiceTwo.class);
        serviceTwo.execute();
    }


    public static <T> T getBean(final ConfigurableApplicationContext context,
                                final Class<T> typeClass,
                                final Class<? extends Annotation> qualifierClass,
                                final String attributeValue) throws NoSuchBeanDefinitionException {
        final Map<String, T> beanMap = context.getBeansOfType(typeClass);
        final ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();

        for (final String beanName : beanMap.keySet()) {
            if (beanFactory.containsBeanDefinition(beanName)) {
                final BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
                if (beanDefinition instanceof AbstractBeanDefinition) {
                    final AbstractBeanDefinition abstractBeanDefinition = (AbstractBeanDefinition) beanDefinition;
                    final AutowireCandidateQualifier autowireCandidateQualifier = abstractBeanDefinition.getQualifier(qualifierClass.getName());
                    if (autowireCandidateQualifier != null) {
                        final Object value = autowireCandidateQualifier.getAttribute("value");
                        if (attributeValue.equals(value)) {
                            return beanMap.get(beanName);
                        }
                    }
                }
            }
        }

        final String qualifier = "With Qualifier " + qualifierClass.getName() + "('" + attributeValue + "')";
        throw new NoSuchBeanDefinitionException(typeClass, qualifier);
    }


}
