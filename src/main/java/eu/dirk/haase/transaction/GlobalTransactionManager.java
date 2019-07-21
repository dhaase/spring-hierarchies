package eu.dirk.haase.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

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

    /**
     * Beginnt eine neue Transaktion mit der Semantik gem&auml;&szlig; der angegebenen
     * Transaktionsdefinition.
     * <p>
     * Diese Methode wird aufgerufen, wenn der Transaktionsmanager beschlossen hat, eine
     * neue Transaktion zu starten. Entweder gab es vorher keine Transaktion, oder die
     * vorherige Transaktion wurde ausgesetzt.
     * <p>
     * Ein spezielles Szenario ist eine verschachtelte Transaktion ohne Sicherungspunkt:
     * Wenn {@link #useSavepointForNestedTransaction()} {@code false} zur&uuml;ck gibt,
     * dann wird diese Methode aufgerufen, um bei Bedarf eine verschachtelte Transaktion
     * zu starten.
     * <p>
     * In einem solchen Kontext wird es eine aktive Transaktion geben: Die Implementierung
     * dieser Methode muss dies erkennen und eine entsprechende verschachtelte Transaktion
     * starten.
     * <p>
     * Diese Methode l&ouml;st das {@link ApplicationEvent}-Event
     * {@link AfterTransactionBeginEvent} aus.
     *
     * @param transaction Transaktionsobjekt, das von {@link #doGetTransaction()}
     *                    zur&uuml;ckgegeben wird.
     * @param definition  Eine TransactionDefinition-Instanz, die das Weitergabeverhalten,
     *                    die Isolationsstufe, das Nur-Lese-Flag, das Zeitlimit und den
     *                    Transaktionsnamen beschreibt.
     * @see AbstractPlatformTransactionManager#doBegin(Object, TransactionDefinition)
     * @see AfterTransactionBeginEvent
     */
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
