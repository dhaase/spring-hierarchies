package eu.dirk.haase.bean;

public interface MainServiceTwo extends MwsBean {
    void execute();

    String getValue();

    void setValue(String value);
}
