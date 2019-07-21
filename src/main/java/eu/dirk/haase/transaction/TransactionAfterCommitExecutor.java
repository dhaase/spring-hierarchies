package eu.dirk.haase.transaction;

@FunctionalInterface
public interface TransactionAfterCommitExecutor {

    void onAfterCommitExecute(Runnable command);

}
