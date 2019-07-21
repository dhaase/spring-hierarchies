package eu.dirk.haase.transaction;

/**
 * F&uuml;hrt ein {@link Runnable}-Komando aus, wenn
 * die aktuelle Transaktion gerade beendet wurde.
 *
 * @see TransactionSynchronizationRegistry#onAfterCompletionExecute(Runnable)
 */
@FunctionalInterface
public interface TransactionAfterCompletionExecutor {

    /**
     * F&uuml;hrt ein {@link Runnable}-Komando aus, wenn
     * die aktuelle Transaktion gerade beendet wurde.
     *
     * @param command das Kommando das ausgef&uuml;hrt werden
     *                soll.
     * @see TransactionSynchronizationRegistry#onAfterCompletionExecute(Runnable)
     */
    void onAfterCompletionExecute(Runnable command);

}
