package eu.dirk.haase.bean;

import org.springframework.beans.factory.annotation.Autowired;

public class MainServiceTwoImpl implements MainServiceTwo {

    private String value;

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
        System.out.println("Main-Service-Two: " + this.value);
    }

}
