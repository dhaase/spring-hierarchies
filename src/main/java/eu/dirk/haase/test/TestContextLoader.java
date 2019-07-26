package eu.dirk.haase.test;

import eu.dirk.haase.bean.hierarchy.ContextRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.SmartContextLoader;

import java.util.HashSet;
import java.util.Set;

public class TestContextLoader implements SmartContextLoader {

    @Override
    public void processContextConfiguration(ContextConfigurationAttributes contextConfigurationAttributes) {

    }

    @Override
    public ApplicationContext loadContext(MergedContextConfiguration mergedContextConfiguration) throws Exception {
        System.out.println(mergedContextConfiguration.getContextInitializerClasses());
        final Class<?> testClass = mergedContextConfiguration.getTestClass();
        final TestContextConfiguration testContextConfiguration = testClass.getAnnotation(TestContextConfiguration.class);
        final ContextRepository contextHierarchy = ContextRepository.global();
        final Set<ContextRepository.BeanType> beanTypeSet = new HashSet<>();
        final String[] beanTypes = testContextConfiguration.beanCategories();
        for (String beanType : beanTypes) {
            beanTypeSet.add(ContextRepository.BeanType.valueOf(beanType));
        }
        return contextHierarchy.findApplicationContextForBeansOf(beanTypeSet).get();
    }

    @Override
    public String[] processLocations(Class<?> aClass, String... strings) {
        return new String[0];
    }

    @Override
    public ApplicationContext loadContext(String... strings) throws Exception {
        return null;
    }
}
