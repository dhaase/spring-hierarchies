package eu.dirk.haase.transaction;

/**
 * F&uuml;hrt ein {@link Runnable}-Komando aus, wenn
 * die aktuelle Transaktion gerade festgeschrieben
 * (= Commit) wurde.
 *
 * @see TransactionSynchronizationRegistry#onAfterCompletionExecute(Runnable)
 */
@FunctionalInterface
public interface TransactionAfterCommitExecutor {

    /**
     * F&uuml;hrt ein {@link Runnable}-Komando aus, wenn
     * die aktuelle Transaktion gerade festgeschrieben
     * (= Commit) wurde.
     *
     * @param command das Kommando das ausgef&uuml;hrt werden
     *                soll.
     * @see TransactionSynchronizationRegistry#onAfterCompletionExecute(Runnable)
     */
    void onAfterCommitExecute(Runnable command);

}
