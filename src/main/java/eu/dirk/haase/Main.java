package eu.dirk.haase;

import eu.dirk.haase.bean.MainServiceOne;
import eu.dirk.haase.bean.MainServiceTwo;
import eu.dirk.haase.bean.ServiceTwo;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ExecutionException;

public class Main {


    public static void main(String... args) throws ExecutionException, InterruptedException {
        String[] resources1 = {"/eu/dirk/haase/application-context-datasources.xml"};
        ClassPathXmlApplicationContext ctx_datasources = new ClassPathXmlApplicationContext();
        ctx_datasources.setId("ctx_datasources");
        ctx_datasources.setDisplayName("ctx_datasources");
        ctx_datasources.setConfigLocations(resources1);
        ctx_datasources.refresh();

        String[] resources2 = {"/eu/dirk/haase/application-context-all-domains.xml"};
        ClassPathXmlApplicationContext ctx_all_domains = new ClassPathXmlApplicationContext();
        ctx_all_domains.setId("ctx_all_domains");
        ctx_all_domains.setDisplayName("ctx_all_domains");
        ctx_all_domains.setParent(ctx_datasources);
        ctx_all_domains.setConfigLocations(resources2);
        ctx_all_domains.refresh();

        String[] resources3 = {"/eu/dirk/haase/application-context-main.xml"};
        ClassPathXmlApplicationContext ctx_main = new ClassPathXmlApplicationContext();
        ctx_main.setId("ctx_main");
        ctx_main.setDisplayName("ctx_main");
        ctx_main.setParent(ctx_all_domains);
        ctx_main.setConfigLocations(resources3);
        ctx_main.refresh();

        ServiceTwo serviceTwo = ctx_main.getBean("serviceTwo", ServiceTwo.class);
        serviceTwo.execute();
    }


}
