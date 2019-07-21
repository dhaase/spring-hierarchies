package eu.dirk.haase.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronization;

import java.util.List;
import java.util.Map;

 class TransactionSynchronizationHandler extends AbstractTransactionSynchronization {

    private static final Logger logger = LoggerFactory.getLogger(TransactionSynchronizationHandler.class);

    private static void executeRunnables(final Event event, final String msg) {
        final Map<Event, List<Runnable>> threadRunnableMap = threadLocalRunnables.get();
        final List<Runnable> threadRunnables = (threadRunnableMap != null ? threadRunnableMap.get(event) : null);
        if (threadRunnables != null) {
            logger.info(msg, threadRunnables.size());
            threadRunnables.forEach((r) -> execute(event, r));
        }
    }

    private static void execute(final Event event, final Runnable command) {
        try {
            command.run();
        } catch (RuntimeException ex) {
            logger.error(event + ": Failed to execute runnable " + command + ": " + ex, ex);
        }
    }

    static TransactionSynchronization newHandler() {
        return new Handler();
    }

    static class Handler implements TransactionSynchronization {

        @Override
        public void afterCommit() {
            executeRunnables(Event.afterCommit, "Transaction successfully committed, executing {} runnables");
        }

        @Override
        public void afterCompletion(int status) {
            if (status == STATUS_COMMITTED) {
                logger.info("Transaction completed with status COMMITTED");
            } else {
                logger.warn("Transaction completed with status ROLLED_BACK");
            }
            executeRunnables(Event.afterCompletion, "Transaction successfully committed, executing {} runnables");
            threadLocalRunnables.remove();
        }

        @Override
        public void beforeCommit(boolean b) {
            executeRunnables(Event.beforeCommit, "Transaction before committing, executing {} runnables");
        }

        @Override
        public void beforeCompletion() {
            executeRunnables(Event.beforeCompletion, "Transaction is before completion, executing {} runnables");
        }

        @Override
        public void flush() {
            executeRunnables(Event.flush, "Transaction is flushed, executing {} runnables");
        }

        @Override
        public void resume() {
            executeRunnables(Event.resume, "Transaction is resumed, executing {} runnables");
        }

        @Override
        public void suspend() {
            executeRunnables(Event.suspend, "Transaction is suspended, executing {} runnables");
        }

    }
}
