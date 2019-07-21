package eu.dirk.haase.transaction;

/**
 * F&uuml;hrt ein {@link Runnable}-Komando aus, wenn die in der
 * Transaktion zugrunde liegende Sitzung in den Datenspeicher
 * (zum Beispiel alle betroffenen Hibernate / JPA-Sitzungen)
 * geschrieben werden soll.
 *
 * @see TransactionSynchronizationRegistry#onFlushExecute(Runnable)
 */
@FunctionalInterface
public interface TransactionFlushExecutor {

    /**
     * F&uuml;hrt ein {@link Runnable}-Komando aus, wenn die in der
     * Transaktion zugrunde liegende Sitzung in den Datenspeicher
     * (zum Beispiel alle betroffenen Hibernate / JPA-Sitzungen)
     * geschrieben werden soll.
     *
     * @param command das Kommando das ausgef&uuml;hrt werden
     *                soll.
     * @see TransactionSynchronizationRegistry#onFlushExecute(Runnable)
     */
    void onFlushExecute(Runnable command);

}
