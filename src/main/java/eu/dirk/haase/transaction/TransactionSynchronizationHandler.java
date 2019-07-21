package eu.dirk.haase.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronization;

import java.util.List;
import java.util.Map;

final class TransactionSynchronizationHandler extends AbstractTransactionSynchronization {

    private static final Logger logger = LoggerFactory.getLogger(TransactionSynchronizationHandler.class);

    private TransactionSynchronizationHandler() {
    }

    private static void execute(final Event event, final Runnable command) {
        try {
            command.run();
        } catch (RuntimeException ex) {
            logger.error(event + "-Event: Failed to execute runnable " + command + ": " + ex, ex);
        }
    }

    private static void executeRunnables(final Event event) {
        final Map<Event, List<Runnable>> threadRunnableMap = threadLocalRunnables.get();
        final List<Runnable> threadRunnables = (threadRunnableMap != null ? threadRunnableMap.get(event) : null);
        if (threadRunnables != null) {
            logger.info("Executing {} runnables to run on {}-Event", threadRunnables.size(), event);
            threadRunnables.forEach((r) -> execute(event, r));
        }
    }

    static TransactionSynchronization newHandler() {
        return new Handler();
    }

    static class Handler implements TransactionSynchronization {
        Handler() {
        }

        @Override
        public void afterCommit() {
            executeRunnables(Event.afterCommit);
        }

        @Override
        public void afterCompletion(int status) {
            if (status == STATUS_COMMITTED) {
                logger.info("Transaction completed with status: COMMITTED");
            } else {
                logger.warn("Transaction completed with status: ROLLED_BACK");
            }
            executeRunnables(Event.afterCompletion);
            TransactionSynchronizationRegistry.getSingleton().clearCurrent();
        }

        @Override
        public void beforeCommit(boolean isReadOnly) {
            logger.info("{} Transaction is just before commit", (isReadOnly ? "Read only" : ""));
            executeRunnables(Event.beforeCommit);
        }

        @Override
        public void beforeCompletion() {
            executeRunnables(Event.beforeCompletion);
        }

        @Override
        public void flush() {
            executeRunnables(Event.flush);
        }

        @Override
        public void resume() {
            executeRunnables(Event.resume);
        }

        @Override
        public void suspend() {
            executeRunnables(Event.suspend);
        }

    }
}
