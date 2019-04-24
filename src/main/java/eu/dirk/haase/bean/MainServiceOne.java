package eu.dirk.haase.bean;

import org.springframework.beans.factory.annotation.Qualifier;

@Qualifier("eins")
public interface MainServiceOne extends MwsBean {
    void execute();

    String getValue();

    void setValue(String value);
}
