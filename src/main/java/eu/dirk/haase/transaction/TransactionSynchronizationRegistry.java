package eu.dirk.haase.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TransactionSynchronizationRegistry
        extends AbstractTransactionSynchronization implements TransactionAfterCommitExecutor,
        TransactionAfterCompletionExecutor, TransactionBeforeCommitExecutor, TransactionBeforeCompletionExecutor,
        TransactionSuspendExecutor, TransactionResumeExecutor, TransactionFlushExecutor {

    private static final TransactionSynchronizationRegistry SINGLETON = new TransactionSynchronizationRegistry();
    private static final Logger logger = LoggerFactory.getLogger(TransactionSynchronizationRegistry.class);
    private TransactionSynchronizationCallbacks defaultCallbacks;
    private boolean isEnabled;

    private TransactionSynchronizationRegistry() {
    }

    private static void ensureSynchronizationActive() {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            throw new IllegalTransactionStateException("Transaction synchronization is NOT ACTIVE");
        }
    }

    public void clearCurrent() {
        threadLocalRunnables.remove();
    }

    public void clearAll() {

    }

    public static TransactionSynchronizationRegistry getSingleton() {
        return SINGLETON;
    }

    private static List<Runnable> registerSynchronization(final Event event) {
        Map<Event, List<Runnable>> threadRunnableMap = threadLocalRunnables.get();
        if (threadRunnableMap == null) {
            threadRunnableMap = new HashMap<>();
            threadLocalRunnables.set(threadRunnableMap);
            TransactionSynchronizationManager.registerSynchronization(TransactionSynchronizationHandler.newHandler());
        }
        return threadRunnableMap.computeIfAbsent(event, (k) -> new ArrayList<>());
    }

    @EventListener(classes = AfterTransactionBeginEvent.class)
    public void onAfterTransactionBegin(final AfterTransactionBeginEvent event) {
        logger.info("Transaction has started");
        registerDefaults();
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public void onAfterCommitExecute(Runnable command) {
        registerSynchronization(Event.afterCommit, command);
    }

    @Override
    public void onAfterCompletionExecute(Runnable command) {
        registerSynchronization(Event.afterCompletion, command);
    }

    @Override
    public void onBeforeCommitExecute(Runnable command) {
        registerSynchronization(Event.beforeCommit, command);
    }

    @Override
    public void onBeforeCompletionExecute(Runnable command) {
        registerSynchronization(Event.beforeCompletion, command);
    }

    @Override
    public void onFlushExecute(Runnable command) {
        registerSynchronization(Event.flush, command);
    }

    @Override
    public void onResumeExecute(Runnable command) {
        registerSynchronization(Event.resume, command);
    }

    @Override
    public void onSuspendExecute(Runnable command) {
        registerSynchronization(Event.suspend, command);
    }

    private void registerDefaults() {
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

    private void registerSynchronization(final Event event, final Runnable command) {
        if (isEnabled) {
            logger.info("Submitting new runnable {} to run on {}-Event", command, event);
            ensureSynchronizationActive();
            final List<Runnable> threadRunnables = registerSynchronization(event);
            threadRunnables.add(command);
        }
    }

    public void setDefaultCallbacks(TransactionSynchronizationCallbacks defaultCallbacks) {
        this.defaultCallbacks = defaultCallbacks;
    }


}
