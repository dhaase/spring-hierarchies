package eu.dirk.haase.bean;

import org.springframework.beans.factory.annotation.Autowired;

public class MainServiceOneImpl implements MainServiceOne {

    private String value;

    @Autowired
    private ServiceOne serviceOne;

    @Autowired
    private ServiceTwo serviceTwo;

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void execute() {
        System.out.println("Main-Service-One: " + this.value);
        serviceOne.execute();
        serviceTwo.execute();
    }

}
