package eu.dirk.haase.bean;

import org.springframework.beans.factory.annotation.Autowired;

public class Bean1 {

    private String name;

    private Bean1 bean1;

    private Bean2 bean2;

    public Bean1 getBean1() {
        return bean1;
    }

    public void setBean1(Bean1 bean1) {
        this.bean1 = bean1;
    }

    public Bean2 getBean2() {
        return bean2;
    }

    public void setBean2(Bean2 bean2) {
        this.bean2 = bean2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
