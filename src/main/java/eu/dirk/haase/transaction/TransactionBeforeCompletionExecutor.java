package eu.dirk.haase.transaction;

@FunctionalInterface
public interface TransactionBeforeCompletionExecutor {

    void onBeforeCompletionExecute(Runnable command);

}
