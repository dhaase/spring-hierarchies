package eu.dirk.haase.bean;

import org.springframework.beans.factory.annotation.Autowired;

public class Bean2 {

    private String name;

    private Bean3 bean3;

    public Bean3 getBean3() {
        return bean3;
    }

    public void setBean3(Bean3 bean3) {
        this.bean3 = bean3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
