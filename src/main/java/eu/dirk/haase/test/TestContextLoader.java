package eu.dirk.haase.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.SmartContextLoader;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.GenericXmlContextLoader;

import java.lang.reflect.AnnotatedElement;

public abstract class TestContextLoader implements SmartContextLoader {

    private SmartContextLoader defaultSmartContextLoader = new NullSmartContextLoader();

    public TestContextLoader() {
        super();
    }

    @Override
    public final void processContextConfiguration(ContextConfigurationAttributes contextConfigurationAttributes) {
        if (contextConfigurationAttributes.hasLocations() && contextConfigurationAttributes.hasClasses()) {
            throw new IllegalArgumentException("The Test Spring Context configuration " +
                    "can define either 'locations' or 'classes', but not both.");
        }
        if (contextConfigurationAttributes.hasLocations()) {
            defaultSmartContextLoader = new GenericXmlContextLoader();
        } else if (contextConfigurationAttributes.hasClasses()) {
            defaultSmartContextLoader = new AnnotationConfigContextLoader();
        } else {
            defaultSmartContextLoader = new NullSmartContextLoader();
        }
        defaultSmartContextLoader.processContextConfiguration(contextConfigurationAttributes);
    }

    @Override
    public final ApplicationContext loadContext(MergedContextConfiguration mergedContextConfiguration) throws Exception {
        final Class<? extends AnnotatedElement> testClass = (Class<? extends AnnotatedElement>) mergedContextConfiguration.getTestClass();
        final TestContextConfiguration testContextConfiguration = AnnotatedElementUtils.getMergedAnnotation(testClass, TestContextConfiguration.class);
        final String[] beanTypes = testContextConfiguration.beanCategories();
        ApplicationContext mainContext = findApplicationContextForBeansOf(beanTypes);
        ApplicationContext testContext = defaultSmartContextLoader.loadContext(mergedContextConfiguration);
        if (testContext == null) {
            return mainContext;
        } else {
            ((ConfigurableApplicationContext) testContext).setParent(mainContext);
            return testContext;
        }
    }

    abstract protected ApplicationContext findApplicationContextForBeansOf(final String[] beanTypes);


    @Override
    public final String[] processLocations(Class<?> aClass, String... strings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final ApplicationContext loadContext(String... strings) throws Exception {
        throw new UnsupportedOperationException();
    }


    static class NullSmartContextLoader implements SmartContextLoader {

        @Override
        public void processContextConfiguration(ContextConfigurationAttributes configAttributes) {

        }

        @Override
        public ApplicationContext loadContext(MergedContextConfiguration mergedConfig) throws Exception {
            return null;
        }

        @Override
        public String[] processLocations(Class<?> clazz, String... locations) {
            return new String[0];
        }

        @Override
        public ApplicationContext loadContext(String... locations) throws Exception {
            return null;
        }
    }
}
