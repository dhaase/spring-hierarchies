package eu.dirk.haase.bean;

import org.springframework.context.SmartLifecycle;

public class MwsBeanImpl implements MwsBean {
    private String name;

    public String getName() {
        return name;
    }

    public int getPhase() {
        return 0;
    }

    public boolean isAutoStartup() {
        return true;
    }

    private volatile boolean isRunning;

    public void start() {
        isRunning = true;
    }

    public void stop() {
        stop(() -> {
        });
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void stop(Runnable runnable) {
        isRunning = false;
        System.out.println("stop: " + this);
    }

    public String toString() {
        return "MwsBeanImpl{" +
                "name='" + name + '\'' +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

}
