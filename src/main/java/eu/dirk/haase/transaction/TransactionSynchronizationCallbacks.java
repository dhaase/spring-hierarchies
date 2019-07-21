package eu.dirk.haase.transaction;

/**
 * Schnittstelle f&uuml;r Transaktionssynchronisations-Callbacks.
 * <p>
 * Unterst&uuml;tzt von {@link TransactionSynchronizationRegistry}.
 */
public interface TransactionSynchronizationCallbacks {

    /**
     * Wird ausgef&uuml;hrt, wenn
     * die aktuelle Transaktion unterbrochen wurde.
     */
    void onSuspendExecute();

    /**
     * Wird ausgef&uuml;hrt, wenn
     * die aktuelle Transaktion fortgesetzt wurde.
     */
    void onResumeExecute();

    /**
     * Wird ausgef&uuml;hrt, wenn die in der
     * Transaktion zugrunde liegende Sitzung in den Datenspeicher
     * (zum Beispiel alle betroffenen Hibernate / JPA-Sitzungen)
     * geschrieben werden soll.
     */
    void onFlushExecute();

    /**
     * Wird ausgef&uuml;hrt, wenn
     * die aktuelle Transaktion gerade vor dem Festschreiben
     * (= Commit) steht.
     */
    void onBeforeCommitExecute();

    /**
     * Wird ausgef&uuml;hrt, wenn
     * die aktuelle Transaktion kurz vor dem Abschluss
     * steht.
     */
    void onBeforeCompletionExecute();

    /**
     * Wird ausgef&uuml;hrt, wenn
     * die aktuelle Transaktion gerade festgeschrieben
     * (= Commit) wurde.
     */
    void onAfterCommitExecute();

    /**
     * Wird ausgef&uuml;hrt, wenn
     * die aktuelle Transaktion gerade beendet wurde.
     */
    void onAfterCompletionExecute();


}
