package eu.dirk.haase;

import eu.dirk.haase.bean.MwsBean;
import eu.dirk.haase.bean.hierarchy.ContextRepository;
import org.springframework.context.ApplicationContext;

import java.util.EnumSet;

public class Main {


    public static void main(String... args) {
        final ContextRepository contextHierarchy = ContextRepository.global();
        final ApplicationContext context1 = contextHierarchy.findApplicationContextForBeansOf(contextHierarchy.allBeanTypes()).get();
        contextHierarchy.applyOnApplicationContextForBeansOf(ContextRepository.ApplicationContextLifeCycle.refresh,
                ContextRepository.BeanType.Three);
        final ApplicationContext context2 = contextHierarchy.findApplicationContextForBeansOf(contextHierarchy.allBeanTypes()).get();

//        final ApplicationContext context = contextHierarchy.findApplicationContextForBeansOf(ContextRegistry.BeanType.One,
//                ContextRegistry.BeanType.Four).get();

        final MwsBean bean = context1.getBean("eins", MwsBean.class);
        System.out.println(bean);
        System.out.println(contextHierarchy.allBeanTypes().containsAll(EnumSet.allOf(ContextRepository.BeanType.class)));
    }


}
