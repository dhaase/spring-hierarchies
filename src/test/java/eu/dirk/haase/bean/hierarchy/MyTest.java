package eu.dirk.haase.bean.hierarchy;

import eu.dirk.haase.bean.MwsBeanImpl;
import eu.dirk.haase.test.MwsTestContextConfiguration;
import eu.dirk.haase.test.TestContextConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@MwsTestContextConfiguration({"One", "Two"})
@ActiveProfiles("my-profile")
public class MyTest {

    @Resource(name = "eins")
    private MwsBeanImpl eins;

    @Resource(name = "zwei")
    private MwsBeanImpl zwei;

//    @Resource(name = "vier")
//    private MwsBeanImpl vier;

    @Test
    public void test_one() {
        System.out.println("eins: " + eins);
    }

    @Test
    public void test_two() {
        System.out.println("zwei: " + zwei);
    }
}
