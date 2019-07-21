package eu.dirk.haase.transaction;

import org.springframework.core.Ordered;

import java.util.List;
import java.util.Map;

abstract class AbstractTransactionSynchronization implements Ordered {

    static final ThreadLocal<Map<Event, List<Runnable>>> threadLocalRunnables = new ThreadLocal<>();

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    enum Event {
        afterCommit,
        afterCompletion,
        beforeCommit,
        beforeCompletion,
        flush,
        resume,
        suspend
    }
}
