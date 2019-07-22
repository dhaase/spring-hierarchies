package eu.dirk.haase.transaction;

import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * F&uuml;hrt ein {@link Runnable}-Komando aus, wenn
 * die aktuelle Transaktion kurz vor dem Abschluss
 * steht.
 * <p>
 * Dieser Executor kann nur dann erfolgreich aufgerufen werden,
 * wenn die Synchronization der Transaktion des aktuellen Threads
 * aktiviert ist.
 *
 * @see TransactionSynchronizationRegistry#onBeforeCompletionExecute(Runnable)
 * @see TransactionSynchronizationManager#isSynchronizationActive()
 */
@FunctionalInterface
public interface TransactionBeforeCompletionExecutor {

    /**
     * F&uuml;hrt ein {@link Runnable}-Komando aus, wenn
     * die aktuelle Transaktion kurz vor dem Abschluss
     * steht.
     * <p>
     * Diese Methode kann nur dann erfolgreich aufgerufen werden, wenn
     * die Synchronization der Transaktion des aktuellen Threads
     * aktiviert ist.
     *
     * @param command das Kommando das ausgef&uuml;hrt werden
     *                soll.
     * @see TransactionSynchronizationRegistry#onBeforeCompletionExecute(Runnable)
     * @see TransactionSynchronizationManager#isSynchronizationActive()
     */
    void onBeforeCompletionExecute(Runnable command);

}
