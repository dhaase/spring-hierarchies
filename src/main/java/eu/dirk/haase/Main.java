package eu.dirk.haase;

import eu.dirk.haase.bean.MwsBeanImpl;
import eu.dirk.haase.bean.aop.sub1.sub2.MyWrap;
import eu.dirk.haase.bean.hierarchy.ContextRepository;
import org.springframework.context.ApplicationContext;

public class Main {


    public static void main(String... args) {
        final ContextRepository contextHierarchy = ContextRepository.global();

        final ApplicationContext context1 = contextHierarchy.findApplicationContextForBeansOf(contextHierarchy.allBeanTypes()).get();

        System.out.println("-----------------------------");

        MwsBeanImpl eins = context1.getBean("eins", MwsBeanImpl.class);
        MwsBeanImpl zwei = context1.getBean("zwei", MwsBeanImpl.class);
        MwsBeanImpl vier = context1.getBean("vier", MwsBeanImpl.class);
        vier.isRunning();

        MyWrap wrap1 = new MyWrap();
        MyWrap wrap2 = new MyWrap(wrap1);
        MyWrap wrap3 = new MyWrap(wrap2);
        MyWrap wrap4 = new MyWrap(wrap3);

        wrap4.myCall();
        MyWrap myWrap1 = context1.getBean("wrap", MyWrap.class);
        myWrap1.myCall();
    }


}
