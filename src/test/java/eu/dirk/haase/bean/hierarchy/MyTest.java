package eu.dirk.haase.bean.hierarchy;

import eu.dirk.haase.bean.MwsBeanImpl;
import eu.dirk.haase.test.MwsTestContextConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@MwsTestContextConfiguration(beanCategories = {"One", "Two"}, enableAnnotationBasedConfiguration = false)
@ActiveProfiles("my-profile")
public class MyTest implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Resource(name = "eins")
    private MwsBeanImpl eins;

    @Resource(name = "zwei")
    private MwsBeanImpl zwei;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

//    @Resource(name = "vier")
//    private MwsBeanImpl vier;

    @Test
    public void test_one_ctx() {
        System.out.println("eins: " + this.applicationContext.getBean("eins"));
    }

    @Test
    public void test_two_ctx() {
        System.out.println("zwei: " + this.applicationContext.getBean("zwei"));
    }

    @Test
    public void test_one() {
        System.out.println("eins: " + eins);
    }

    @Test
    public void test_two() {
        System.out.println("zwei: " + zwei);
    }
}
