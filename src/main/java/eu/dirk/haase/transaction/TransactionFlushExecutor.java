package eu.dirk.haase.transaction;

import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * F&uuml;hrt ein {@link Runnable}-Komando aus, wenn die in der
 * Transaktion zugrunde liegende Sitzung in den Datenspeicher
 * (zum Beispiel alle betroffenen Hibernate / JPA-Sitzungen)
 * geschrieben werden soll.
 * <p>
 * Dieser Executor kann nur dann erfolgreich aufgerufen werden,
 * wenn die Synchronization der Transaktion des aktuellen Threads
 * aktiviert ist.
 *
 * @see TransactionSynchronizationRegistry#onFlushExecute(Runnable)
 * @see TransactionSynchronizationManager#isSynchronizationActive()
 */
@FunctionalInterface
public interface TransactionFlushExecutor {

    /**
     * F&uuml;hrt ein {@link Runnable}-Komando aus, wenn die in der
     * Transaktion zugrunde liegende Sitzung in den Datenspeicher
     * (zum Beispiel alle betroffenen Hibernate / JPA-Sitzungen)
     * geschrieben werden soll.
     * <p>
     * Diese Methode kann nur dann erfolgreich aufgerufen werden, wenn
     * die Synchronization der Transaktion des aktuellen Threads
     * aktiviert ist.
     *
     * @param command das Kommando das ausgef&uuml;hrt werden
     *                soll.
     * @see TransactionSynchronizationRegistry#onFlushExecute(Runnable)
     * @see TransactionSynchronizationManager#isSynchronizationActive()
     */
    void onFlushExecute(Runnable command);

}
