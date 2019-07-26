package eu.dirk.haase.bean;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.SmartLifecycle;

public class MwsBeanImpl implements MwsBean, SmartLifecycle, ApplicationEventPublisherAware {
    private String name;
    private ApplicationEventPublisher applicationEventPublisher;

    public String getName() {
        return name;
    }

    @Override
    public int getPhase() {
        return 0;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    private volatile boolean isRunning;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void start() {
        isRunning = true;
    }

    @Override
    public void stop() {
        stop(()->{});
    }

    @Override
    public boolean isRunning() {
        //applicationEventPublisher.publishEvent(new MwsBeanApplicationEvent(this));
        return isRunning;
    }

    @Override
    public void stop(Runnable runnable) {
        isRunning = false;
        System.out.println("stop: " + this);
    }

    @Override
    public String toString() {
        return "MwsBeanImpl{" +
                "name='" + name + '\'' +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class MwsBeanApplicationEvent extends ApplicationEvent {

        public MwsBeanApplicationEvent(Object source) {
            super(source);
        }
    }
}
