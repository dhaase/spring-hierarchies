package eu.dirk.haase.transaction;

/**
 * F&uuml;hrt ein {@link Runnable}-Komando aus, wenn
 * die aktuelle Transaktion gerade vor dem Festschreiben
 * (= Commit) steht.
 *
 * @see TransactionSynchronizationRegistry#onBeforeCommitExecute(Runnable)
 */
@FunctionalInterface
public interface TransactionBeforeCommitExecutor {

    /**
     * F&uuml;hrt ein {@link Runnable}-Komando aus, wenn
     * die aktuelle Transaktion gerade vor dem Festschreiben
     * (= Commit) steht.
     *
     * @param command das Kommando das ausgef&uuml;hrt werden
     *                soll.
     * @see TransactionSynchronizationRegistry#onBeforeCommitExecute(Runnable)
     */
    void onBeforeCommitExecute(Runnable command);

}
