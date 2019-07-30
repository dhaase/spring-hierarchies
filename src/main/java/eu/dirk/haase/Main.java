package eu.dirk.haase;

import eu.dirk.haase.bean.MwsBeanImpl;
import eu.dirk.haase.bean.aop.sub1.sub2.MyWrap;
import eu.dirk.haase.bean.hierarchy.ContextRepository;
import org.springframework.context.ApplicationContext;

import java.security.AccessControlException;
import java.security.Permission;

public class Main {


    public static void main(String... args) {

        final ContextRepository contextHierarchy = ContextRepository.global();

        final ApplicationContext context1 = contextHierarchy.findApplicationContextForBeansOf(contextHierarchy.allBeanTypes()).get();

        System.out.println("-----------------------------");

        MwsBeanImpl eins = context1.getBean("eins", MwsBeanImpl.class);
        MwsBeanImpl zwei = context1.getBean("zwei", MwsBeanImpl.class);
        MwsBeanImpl vier = context1.getBean("vier", MwsBeanImpl.class);
    }


    static class MySecurityManager extends SecurityManager {

        public static final RuntimePermission CREATE_SECURITY_MANAGER_PERMISSION =
                new RuntimePermission("createSecurityManager");

        public MySecurityManager() {
            super();
        }

        @Override
        public void checkPermission(Permission perm) {
            if (CREATE_SECURITY_MANAGER_PERMISSION.implies(perm)) {
                throw new AccessControlException("access denied " + perm, perm);
            }
        }

        @Override
        public void checkPermission(Permission perm, Object context) {

        }
    }

}
