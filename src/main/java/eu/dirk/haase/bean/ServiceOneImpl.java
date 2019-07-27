package eu.dirk.haase.bean;

public class ServiceOneImpl implements ServiceOne {

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
        System.out.println("Service-One: " + this.value);
    }

}
