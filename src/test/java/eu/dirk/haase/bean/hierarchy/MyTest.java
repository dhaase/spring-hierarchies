package eu.dirk.haase.bean.hierarchy;

import eu.dirk.haase.bean.MwsBeanImpl;
import eu.dirk.haase.test.TestContextConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@TestContextConfiguration(beanCategories = {"One", "Two"})
public class MyTest {

    @Resource(name = "eins")
    private MwsBeanImpl eins;

    @Resource(name = "zwei")
    private MwsBeanImpl zwei;

    @Test
    public void test() {
        System.out.println(zwei);
    }
}
