package eu.dirk.haase.transaction;

@FunctionalInterface
public interface TransactionBeforeCommitExecutor {

    void onBeforeCommitExecute(Runnable command);

}
