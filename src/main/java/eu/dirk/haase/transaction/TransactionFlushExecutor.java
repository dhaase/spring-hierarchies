package eu.dirk.haase.transaction;

@FunctionalInterface
public interface TransactionFlushExecutor {

    void onFlushExecute(Runnable command);

}
