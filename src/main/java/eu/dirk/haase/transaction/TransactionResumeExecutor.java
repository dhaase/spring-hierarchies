package eu.dirk.haase.transaction;

/**
 * F&uuml;hrt ein {@link Runnable}-Komando aus, wenn
 * die aktuelle Transaktion fortgesetzt wurde.
 *
 * @see TransactionSynchronizationRegistry#onResumeExecute(Runnable)
 */
@FunctionalInterface
public interface TransactionResumeExecutor {

    /**
     * F&uuml;hrt ein {@link Runnable}-Komando aus, wenn
     * die aktuelle Transaktion fortgesetzt wurde.
     *
     * @param command das Kommando das ausgef&uuml;hrt werden
     *                soll.
     * @see TransactionSynchronizationRegistry#onResumeExecute(Runnable)
     */
    void onResumeExecute(Runnable command);

}
