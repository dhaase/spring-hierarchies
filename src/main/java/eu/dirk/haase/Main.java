package eu.dirk.haase;

import eu.dirk.haase.bean.MwsBean;
import eu.dirk.haase.bean.hierarchy.ContextRepository;
import org.springframework.context.ApplicationContext;

import java.util.EnumSet;

public class Main {


    public static void main(String... args) {
        final ContextRepository contextHierarchy = ContextRepository.global();
        final ApplicationContext context1 = contextHierarchy.findApplicationContextForBeansOf(contextHierarchy.allBeanTypes()).get();
        System.out.println("-----------------------------");
        contextHierarchy.applyOnApplicationContextForBeansOf(ContextRepository.ApplicationContextLifeCycle.refreshCascading,
                ContextRepository.BeanType.Three);
        System.out.println("-----------------------------");
        //final ApplicationContext context2 = contextHierarchy.findApplicationContextForBeansOf(contextHierarchy.allBeanTypes()).get();

        //final MwsBean bean = context1.getBean("eins", MwsBean.class);
//        System.out.println(bean);
//        System.out.println(contextHierarchy.allBeanTypes().containsAll(EnumSet.allOf(ContextRepository.BeanType.class)));
    }


}
