package eu.dirk.haase.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Optional;

public class MainServiceTwoImpl implements MainServiceTwo {

    @Autowired
    @Qualifier("one")
    private MainServiceOne mainServiceOne;

    private MainServiceOne mainServiceOne2;
    private String value;

    @Override
    public void execute() {
        System.out.println("Main-Service-Two: " + this.value);
    }

    public MainServiceOne getMainServiceOne() {
        return mainServiceOne;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

}
