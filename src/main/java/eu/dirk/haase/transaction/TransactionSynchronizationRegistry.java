package eu.dirk.haase.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionSynchronizationRegistry
        extends AbstractTransactionSynchronization implements TransactionAfterCommitExecutor,
        TransactionAfterCompletionExecutor, TransactionBeforeCommitExecutor, TransactionBeforeCompletionExecutor,
        TransactionSuspendExecutor, TransactionResumeExecutor, TransactionFlushExecutor {

    private static final Logger logger = LoggerFactory.getLogger(TransactionSynchronizationRegistry.class);
    private TransactionSynchronizationCallbacks defaultCallbacks;

    private static void ensureSynchronizationActive() {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            throw new IllegalStateException("Transaction synchronization is NOT ACTIVE");
        }
    }

    public void setDefaultCallbacks(TransactionSynchronizationCallbacks defaultCallbacks) {
        this.defaultCallbacks = defaultCallbacks;
    }

    @Override
    public void onAfterCommitExecute(Runnable command) {
        logger.info("Submitting new runnable {} to run after commit", command);
        registerSynchronization(Event.afterCommit, command);
    }

    @Override
    public void onAfterCompletionExecute(Runnable command) {
        logger.info("Submitting new runnable {} to run after completion", command);
        registerSynchronization(Event.afterCompletion, command);
    }

    @Override
    public void onBeforeCommitExecute(Runnable command) {
        logger.info("Submitting new runnable {} to run before commit", command);
        registerSynchronization(Event.beforeCommit, command);
    }

    @Override
    public void onBeforeCompletionExecute(Runnable command) {
        logger.info("Submitting new runnable {} to run before completion", command);
        registerSynchronization(Event.beforeCompletion, command);
    }

    @Override
    public void onFlushExecute(Runnable command) {
        logger.info("Submitting new runnable {} to run on flush", command);
        registerSynchronization(Event.flush, command);
    }

    @Override
    public void onResumeExecute(Runnable command) {
        logger.info("Submitting new runnable {} to run on resume", command);
        registerSynchronization(Event.resume, command);
    }

    @Override
    public void onSuspendExecute(Runnable command) {
        logger.info("Submitting new runnable {} to run after completion", command);
        registerSynchronization(Event.suspend, command);
    }

    public void registerDefaults() {
        if (this.defaultCallbacks != null) {
            onAfterCommitExecute(() -> this.defaultCallbacks.onAfterCommitExecute());
            onAfterCompletionExecute(() -> this.defaultCallbacks.onAfterCompletionExecute());
            onSuspendExecute(() -> this.defaultCallbacks.onSuspendExecute());
            onResumeExecute(() -> this.defaultCallbacks.onResumeExecute());
            onFlushExecute(() -> this.defaultCallbacks.onFlushExecute());
            onBeforeCommitExecute(() -> this.defaultCallbacks.onBeforeCommitExecute());
            onBeforeCompletionExecute(() -> this.defaultCallbacks.onBeforeCompletionExecute());
        }
    }

    private static List<Runnable> registerSynchronization(final Event event) {
        Map<Event, List<Runnable>> threadRunnableMap = threadLocalRunnables.get();
        if (threadRunnableMap == null) {
            TransactionSynchronizationManager.registerSynchronization(TransactionSynchronizationHandler.newHandler());
            threadRunnableMap = new HashMap<>();
            threadLocalRunnables.set(threadRunnableMap);
        }
        return threadRunnableMap.computeIfAbsent(event, (k) -> new ArrayList<>());
    }

    private void registerSynchronization(final Event event, final Runnable runnable) {
        ensureSynchronizationActive();
        final List<Runnable> threadRunnables = registerSynchronization(event);
        threadRunnables.add(runnable);
    }


}
