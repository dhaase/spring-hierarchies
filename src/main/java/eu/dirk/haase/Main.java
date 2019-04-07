package eu.dirk.haase;

import eu.dirk.haase.bean.ApplicationContextRegistry;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {


    public static void main(String... args) throws ExecutionException, InterruptedException {
        single();
    }


    public static void single() {
        String[] resources1 = {"/eu/dirk/haase/application-context-one.xml"};
        ClassPathXmlApplicationContext ctx1 = new ClassPathXmlApplicationContext(resources1);

        boolean isBeanFactory1 = ApplicationContextRegistry.containsBeanFactory("app-ctx-one");
        System.out.println("isBeanFactory1: " + isBeanFactory1);

        boolean isBeanFactory2 = ApplicationContextRegistry.containsBeanFactory("app-ctx-two");
        System.out.println("isBeanFactory2: " + isBeanFactory2);

        String[] resources2 = {"/eu/dirk/haase/application-context-two.xml"};
        ClassPathXmlApplicationContext ctx2 = new ClassPathXmlApplicationContext(resources2);
        ApplicationContextRegistry reg2 = ctx2.getBean(ApplicationContextRegistry.class);

        BeanFactory beanFactory1 = ApplicationContextRegistry.getBeanFactory("app-ctx-one");
        System.out.println(beanFactory1);

        BeanFactory beanFactory2 = ApplicationContextRegistry.getBeanFactory("app-ctx-two");
        System.out.println(beanFactory2);

        //reg2.stop();
        boolean isBeanFactory2a = ApplicationContextRegistry.containsBeanFactory("app-ctx-two");
        System.out.println("isBeanFactory2a: " + isBeanFactory2a);

        BeanFactory beanFactory2a = ApplicationContextRegistry.getBeanFactory("app-ctx-two");
        System.out.println(beanFactory2a);
    }


    public static void concurrent() throws ExecutionException, InterruptedException {
        String[] resources1 = {"/eu/dirk/haase/application-context-one.xml"};

        ExecutorService service = Executors.newCachedThreadPool();

        Future<BeanFactory> future1 = service.submit(() -> ApplicationContextRegistry.getBeanFactory("app-ctx-one"));
        Future<BeanFactory> future2 = service.submit(() -> ApplicationContextRegistry.getBeanFactory("app-ctx-one"));
        Future<BeanFactory> future3 = service.submit(() -> ApplicationContextRegistry.getBeanFactory("app-ctx-one"));
        Future<BeanFactory> future4 = service.submit(() -> ApplicationContextRegistry.getBeanFactory("app-ctx-one"));

        Future<BeanFactory> future5 = service.submit(() -> new ClassPathXmlApplicationContext(resources1));

        BeanFactory beanFactory1 = future1.get();
        BeanFactory beanFactory2 = future2.get();
        BeanFactory beanFactory3 = future3.get();
        BeanFactory beanFactory4 = future4.get();
        BeanFactory beanFactory5 = future5.get();

        System.out.println(beanFactory1);
        System.out.println(beanFactory2);
        System.out.println(beanFactory3.getBean("bean1"));
        System.out.println(beanFactory4);
        System.out.println(beanFactory5);

        service.shutdownNow();
    }


}
