package eu.dirk.haase.test;

import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@TestContextConfiguration(
        loader = MwsTestContextLoader.class
)
public @interface MwsTestContextConfiguration {

    @AliasFor(annotation = TestContextConfiguration.class, attribute = "value")
    String[] value() default {"One", "Two"};

    @AliasFor(annotation = TestContextConfiguration.class, attribute = "beanCategories")
    String[] beanCategories() default {"One", "Two"};

    @AliasFor(annotation = ContextConfiguration.class, attribute = "locations")
    String[] locations() default {};

}
