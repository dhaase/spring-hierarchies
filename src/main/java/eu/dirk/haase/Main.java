package eu.dirk.haase;

import eu.dirk.haase.bean.MwsBean;
import eu.dirk.haase.bean.hierarchy.ContextRegistry;
import org.springframework.context.ApplicationContext;

import java.util.EnumSet;

public class Main {


    public static void main(String... args) {
        final ContextRegistry contextHierarchy = ContextRegistry.global();
        final ApplicationContext context = contextHierarchy.findApplicationContextForBeansOf(contextHierarchy.allBeanTypes()).get();

//        final ApplicationContext context = contextHierarchy.findApplicationContextForBeansOf(ContextRegistry.BeanType.One,
//                ContextRegistry.BeanType.Four).get();

        final MwsBean bean = context.getBean("eins", MwsBean.class);
        System.out.println(bean);
        System.out.println(contextHierarchy.allBeanTypes().containsAll(EnumSet.allOf(ContextRegistry.BeanType.class)));
    }


}
