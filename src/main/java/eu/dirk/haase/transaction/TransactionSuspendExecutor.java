package eu.dirk.haase.transaction;

import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * F&uuml;hrt ein {@link Runnable}-Komando aus, wenn
 * die aktuelle Transaktion unterbrochen wurde.
 * <p>
 * Dieser Executor kann nur dann erfolgreich aufgerufen werden,
 * wenn die Synchronization der Transaktion des aktuellen Threads
 * aktiviert ist.
 *
 * @see TransactionSynchronizationRegistry#onSuspendExecute(Runnable)
 * @see TransactionSynchronizationManager#isSynchronizationActive()
 */
@FunctionalInterface
public interface TransactionSuspendExecutor {

    /**
     * F&uuml;hrt ein {@link Runnable}-Komando aus, wenn
     * die aktuelle Transaktion unterbrochen wurde.
     * <p>
     * Diese Methode kann nur dann erfolgreich aufgerufen werden, wenn
     * die Synchronization der Transaktion des aktuellen Threads
     * aktiviert ist.
     *
     * @param command das Kommando das ausgef&uuml;hrt werden
     *                soll.
     * @see TransactionSynchronizationRegistry#onSuspendExecute(Runnable)
     * @see TransactionSynchronizationManager#isSynchronizationActive()
     */
    void onSuspendExecute(Runnable command);

}
