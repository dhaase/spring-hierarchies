package eu.dirk.haase.bean;

import org.springframework.beans.factory.annotation.Autowired;

public class ServiceTwoImpl implements ServiceTwo {

    private String value;

    @Autowired
    private MainServiceTwo mainServiceTwo;

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
        System.out.println("Service-Two: " + this.value);
        mainServiceTwo.execute();
    }

}
