package eu.dirk.haase.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

public final class GlobalTransactionManager extends JtaTransactionManager implements ApplicationEventPublisherAware {

    private final AfterTransactionBeginEvent afterTransactionBeginEvent;
    private boolean isAfterTransactionBeginEventEnabled;
    @Autowired
    private ApplicationEventPublisher publisher;

    public GlobalTransactionManager() {
        this.afterTransactionBeginEvent = new AfterTransactionBeginEvent(this);
    }

    public GlobalTransactionManager(UserTransaction userTransaction) {
        super(userTransaction);
        this.afterTransactionBeginEvent = new AfterTransactionBeginEvent(this);
    }

    public GlobalTransactionManager(UserTransaction userTransaction, TransactionManager transactionManager) {
        super(userTransaction, transactionManager);
        this.afterTransactionBeginEvent = new AfterTransactionBeginEvent(this);
    }

    public GlobalTransactionManager(TransactionManager transactionManager) {
        super(transactionManager);
        this.afterTransactionBeginEvent = new AfterTransactionBeginEvent(this);
    }

    @Override
    protected void doBegin(final Object transaction, final TransactionDefinition definition) {
        super.doBegin(transaction, definition);
        if (isAfterTransactionBeginEventEnabled) {
            publisher.publishEvent(this.afterTransactionBeginEvent);
        }
    }

    public void setAfterTransactionBeginEventEnabled(boolean afterTransactionBeginEventEnabled) {
        isAfterTransactionBeginEventEnabled = afterTransactionBeginEventEnabled;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }
}
