package eu.dirk.haase.bean;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class MainServiceTwoImpl implements MainServiceTwo {

    @Autowired
    private Optional<MainServiceOne> mainServiceOne;
    private MainServiceOne mainServiceOne2;
    private String value;

    @Override
    public void execute() {
        System.out.println("Main-Service-Two: " + this.value);
    }

    public MainServiceOne getMainServiceOne() {
        return mainServiceOne.get();
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Autowired
    public void setMainServiceOne2(MainServiceOne mainServiceOne2) {
        this.mainServiceOne2 = mainServiceOne2;
    }

}
