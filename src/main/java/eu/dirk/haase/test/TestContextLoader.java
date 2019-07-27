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

    private SmartContextLoader smartContextLoader = new NullSmartContextLoader();

    public TestContextLoader() {
        super();
    }

    @Override
    public final void processContextConfiguration(final ContextConfigurationAttributes ctxConfigAttributes) {
        if (ctxConfigAttributes.hasLocations() && ctxConfigAttributes.hasClasses()) {
            throw new IllegalArgumentException("The Test Spring Context configuration " +
                    "can define either 'locations' or 'classes', but not both.");
        }
        if (ctxConfigAttributes.hasLocations()) {
            this.smartContextLoader = new GenericXmlContextLoader();
        } else if (ctxConfigAttributes.hasClasses()) {
            this.smartContextLoader = new AnnotationConfigContextLoader();
        } else {
            this.smartContextLoader = new NullSmartContextLoader();
        }
        this.smartContextLoader.processContextConfiguration(ctxConfigAttributes);
    }

    @Override
    public final ApplicationContext loadContext(final MergedContextConfiguration mergedCtxConfig) throws Exception {
        final String[] beanTypes = getBeanCategories(mergedCtxConfig);
        final ApplicationContext mainContext = findApplicationContextForBeansOf(beanTypes);
        final ApplicationContext testContext = this.smartContextLoader.loadContext(mergedCtxConfig);
        if (testContext == null) {
            return mainContext;
        } else {
            ((ConfigurableApplicationContext) testContext).setParent(mainContext);
            return testContext;
        }
    }

    private String[] getBeanCategories(final MergedContextConfiguration mergedCtxConfig) {
        final Class<? extends AnnotatedElement> testClass = (Class<? extends AnnotatedElement>) mergedCtxConfig.getTestClass();
        final TestContextConfiguration testContextConfiguration = AnnotatedElementUtils.getMergedAnnotation(testClass, TestContextConfiguration.class);
        return testContextConfiguration.beanCategories();
    }

    abstract protected ApplicationContext findApplicationContextForBeansOf(final String[] beanTypes);


    @Override
    public final String[] processLocations(final Class<?> aClass, final String... strings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final ApplicationContext loadContext(final String... strings) {
        throw new UnsupportedOperationException();
    }


    static class NullSmartContextLoader implements SmartContextLoader {

        @Override
        public void processContextConfiguration(final ContextConfigurationAttributes configAttributes) {

        }

        @Override
        public ApplicationContext loadContext(final MergedContextConfiguration mergedConfig) {
            return null;
        }

        @Override
        public String[] processLocations(final Class<?> clazz, final String... locations) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ApplicationContext loadContext(final String... locations) {
            throw new UnsupportedOperationException();
        }
    }
}
