package eu.dirk.haase.test;


import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextLoader;
import org.springframework.test.context.TestExecutionListeners;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@TestExecutionListeners(
        listeners = TestTestExecutionListener.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@ContextConfiguration(
        initializers = TestApplicationContextInitializer.class)
public @interface TestContextConfiguration {

    @AliasFor("beanCategories")
    String[] value() default {};

    @AliasFor("value")
    String[] beanCategories() default {};

    @AliasFor(annotation = ContextConfiguration.class, attribute = "locations")
    String[] locations() default {};

    @AliasFor(annotation = ContextConfiguration.class, attribute = "classes")
    Class<?>[] classes() default {};

    @AliasFor(annotation = ContextConfiguration.class, attribute = "loader")
    Class<? extends TestContextLoader> loader() default TestContextLoader.class;

}
