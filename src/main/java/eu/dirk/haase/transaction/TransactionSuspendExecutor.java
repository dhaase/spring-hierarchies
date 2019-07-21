package eu.dirk.haase.transaction;

@FunctionalInterface
public interface TransactionSuspendExecutor {

    void onSuspendExecute(Runnable command);

}
