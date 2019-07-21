package eu.dirk.haase.transaction;

@FunctionalInterface
public interface TransactionResumeExecutor {

    void onResumeExecute(Runnable command);

}
