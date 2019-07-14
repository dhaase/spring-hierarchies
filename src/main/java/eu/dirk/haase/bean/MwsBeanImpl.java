package eu.dirk.haase.bean;

import org.springframework.context.SmartLifecycle;

public class MwsBeanImpl implements MwsBean, SmartLifecycle {
    private String name;

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
    public void start() {
        isRunning = true;
        System.out.println("start: " + this);
    }

    @Override
    public void stop() {
        stop(()->{});
    }

    @Override
    public boolean isRunning() {
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
}
