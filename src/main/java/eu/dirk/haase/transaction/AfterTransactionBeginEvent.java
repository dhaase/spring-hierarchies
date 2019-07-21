package eu.dirk.haase.transaction;

import org.springframework.context.ApplicationEvent;

public final class AfterTransactionBeginEvent extends ApplicationEvent {

    AfterTransactionBeginEvent(Object source) {
        super(source);
    }
}
