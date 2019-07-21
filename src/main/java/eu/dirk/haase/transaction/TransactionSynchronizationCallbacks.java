package eu.dirk.haase.transaction;

public interface TransactionSynchronizationCallbacks {

    void onSuspendExecute();

    void onResumeExecute();

    void onFlushExecute();

    void onBeforeCommitExecute();

    void onBeforeCompletionExecute();

    void onAfterCommitExecute();

    void onAfterCompletionExecute();


}
