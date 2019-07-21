package eu.dirk.haase.transaction;

/**
 * F&uuml;hrt ein {@link Runnable}-Komando aus, wenn
 * die aktuelle Transaktion kurz vor dem Abschluss
 * steht.
 *
 * @see TransactionSynchronizationRegistry#onBeforeCompletionExecute(Runnable)
 */
@FunctionalInterface
public interface TransactionBeforeCompletionExecutor {

    /**
     * F&uuml;hrt ein {@link Runnable}-Komando aus, wenn
     * die aktuelle Transaktion kurz vor dem Abschluss
     * steht.
     *
     * @param command das Kommando das ausgef&uuml;hrt werden
     *                soll.
     * @see TransactionSynchronizationRegistry#onBeforeCompletionExecute(Runnable)
     */
    void onBeforeCompletionExecute(Runnable command);

}
