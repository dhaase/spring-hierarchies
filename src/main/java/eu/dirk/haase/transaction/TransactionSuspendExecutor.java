package eu.dirk.haase.transaction;

/**
 * F&uuml;hrt ein {@link Runnable}-Komando aus, wenn
 * die aktuelle Transaktion unterbrochen wurde.
 *
 * @see TransactionSynchronizationRegistry#onSuspendExecute(Runnable)
 */
@FunctionalInterface
public interface TransactionSuspendExecutor {

    /**
     * F&uuml;hrt ein {@link Runnable}-Komando aus, wenn
     * die aktuelle Transaktion unterbrochen wurde.
     *
     * @param command das Kommando das ausgef&uuml;hrt werden
     *                soll.
     * @see TransactionSynchronizationRegistry#onSuspendExecute(Runnable)
     */
    void onSuspendExecute(Runnable command);

}
