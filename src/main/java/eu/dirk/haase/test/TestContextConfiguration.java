package eu.dirk.haase.test;


import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ContextConfiguration(
        loader = TestContextLoader.class,
        name = "Test-Dirk",
        initializers = TestApplicationContextInitializer.class)
public @interface TestContextConfiguration {

    String[] beanCategories() default {};

}
