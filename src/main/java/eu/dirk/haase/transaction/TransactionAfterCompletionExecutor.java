package eu.dirk.haase.transaction;

@FunctionalInterface
public interface TransactionAfterCompletionExecutor {

    void onAfterCompletionExecute(Runnable command);

}
