package eu.dirk.haase.bean;

import org.springframework.context.event.EventListener;

public class ServiceOneImpl implements ServiceOne {

    private String value;

    @EventListener
    public void listener(MwsBeanImpl.MwsBeanApplicationEvent event) {
        final MwsBeanImpl source = (MwsBeanImpl) event.getSource();
        System.out.println(value + " Event: " + source.getName());
    }

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
