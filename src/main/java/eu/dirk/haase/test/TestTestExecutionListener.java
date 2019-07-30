package eu.dirk.haase.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class TestTestExecutionListener implements TestExecutionListener {
    @Override
    public void beforeTestClass(final TestContext testContext) throws Exception {
        System.out.println("beforeTestClass: " );
    }

    @Override
    public void prepareTestInstance(final TestContext testContext) throws Exception {
        System.out.println("prepareTestInstance: " );
        applyApplicationContextAwareness(testContext.getTestInstance(), testContext.getApplicationContext());
    }

    @Override
    public void beforeTestMethod(final TestContext testContext) throws Exception {
        System.out.println("beforeTestMethod: " );
    }

    @Override
    public void afterTestMethod(final TestContext testContext) throws Exception {
        System.out.println("afterTestMethod: " );
    }

    @Override
    public void afterTestClass(final TestContext testContext) throws Exception {
        System.out.println("afterTestClass: ");
    }


    private void applyApplicationContextAwareness(final Object testClassInstance, final ApplicationContext applicationContext) {
        if (testClassInstance instanceof ApplicationContextAware) {
            ((ApplicationContextAware)testClassInstance).setApplicationContext(applicationContext);
        }
    }

}
