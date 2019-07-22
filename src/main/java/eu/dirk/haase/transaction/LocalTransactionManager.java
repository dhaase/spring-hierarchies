package eu.dirk.haase.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Ein Transaktion-Manager f&uuml;r lokale Datenbank-Transaktionen der
 * den Start einer Transaktion mit einem {@link AfterTransactionBeginEvent}
 * ver&ouml;ffentlicht.
 */
public final class LocalTransactionManager extends DataSourceTransactionManager implements ApplicationEventPublisherAware {
    private final AfterTransactionBeginEvent afterTransactionBeginEvent;
    private boolean isAfterTransactionBeginEventEnabled;
    @Autowired
    private ApplicationEventPublisher publisher;

    public LocalTransactionManager() {
        this.afterTransactionBeginEvent = new AfterTransactionBeginEvent(this);
    }

    public LocalTransactionManager(DataSource dataSource) {
        super(dataSource);
        this.afterTransactionBeginEvent = new AfterTransactionBeginEvent(this);
    }

    /**
     * Beginnt eine neue Transaktion mit der Semantik gem&auml;&szlig; der angegebenen
     * Transaktionsdefinition.
     * <p>
     * Diese Methode wird aufgerufen, wenn dieser Transaktionsmanager entschieden hat,
     * eine neue Transaktion zu starten. Entweder gab es vorher keine Transaktion, oder
     * die vorherige Transaktion wurde ausgesetzt.
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

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

}
