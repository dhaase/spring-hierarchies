package eu.dirk.haase.bean.hierarchy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Set;
import java.util.function.Supplier;

/**
 * Zentrale API um ein {@link ApplicationContext} abfragen
 * zu k&ouml;nnen.
 * <p>
 * Diese API liefert generell einen {@link Supplier} zur&uuml;ck,
 * der wiederum einen {@link ApplicationContext} zur&uuml;ck
 * liefert.
 * <p>
 * Sobald ein {@link Supplier} aufgerufen wird, werden alle zuvor
 * abh&auml;ngigen {@code ApplicationContext}e ebenfalls initialisiert.
 * <p>
 * Die {@code ApplicationContext}e werden zwischengespeichert und daher
 * in der Regel nur einmalig initialisiert.
 * Mit der Methode {@link ContextRepository#clearAllApplicationContexts()}
 * k&ouml;nnen die {@code ApplicationContext}e erneut initialisiert
 * werden.
 */
public interface ContextRepository {

    /**
     * Globale Instanz der {@link ContextRepository}.
     *
     * @return die Instanz der {@link ContextRepository}.
     */
    static ContextRepository global() {
        return MWSContextHierarchy.SINGLETON;
    }

    /**
     * Liefert ein {@link Set} von allen {@link BeanType}s die
     * von einem {@link ApplicationContext} in dieser
     * {@link ContextRepository} erreichbar sind.
     *
     * @return ein {@link Set} von allen {@link BeanType}s.
     */
    Set<ContextRepository.BeanType> allBeanTypes();

    /**
     * Wendet auf den gesuchten {@link ApplicationContext} und seinen abh&auml;ngigen
     * {@code ApplicationContext}en die angegebene {@link ApplicationContextLifeCycle}
     * -Methoden an.
     * <p>
     * Die {@code ApplicationContextLifeCycle}-Methode wird nur dann ausgef&uuml;hrt
     * wenn der {@code ApplicationContext} bereits initialisiert ist.
     * Ist er nicht initialisiert, dann ist der Methoden-Aufruf ohne Wirkung.
     * <p>
     * Die {@code ApplicationContextLifeCycle}-Methode wird in der topologischen
     * Reihenfolge der abh&auml;ngigen {@code ApplicationContext}e ausgef&uuml;hrt.
     *
     * @param task        die {@code ApplicationContextLifeCycle}-Methode die an den
     *                    {@code ApplicationContext} angewendet werden soll.
     * @param beanTypeSet ein {@link Set} von {@link BeanType}s als Basis f&uuml;r
     *                    die Suche nach dem {@code ApplicationContext} und deren
     *                    abh&auml;ngigen {@code ApplicationContext}en.
     * @throws IllegalArgumentException wird ausgel&ouml;st wenn keine
     *                                  {@code ApplicationContext}-Defintion
     *                                  gefunden wurde und daher auch keine
     *                                  {@code ApplicationContextLifeCycle}-Methode
     *                                  ausgef&uuml;hrt werden konnte.
     * @see ApplicationContextLifeCycle
     */
    void applyOnApplicationContextForBeansOf(final ApplicationContextLifeCycle task,
                                             final Set<BeanType> beanTypeSet);

    /**
     * Wendet auf den gesuchten {@link ApplicationContext} und seinen abh&auml;ngigen
     * {@code ApplicationContext}en die angegebene {@link ApplicationContextLifeCycle}
     * -Methoden an.
     * <p>
     * Die {@code ApplicationContextLifeCycle}-Methode wird nur dann ausgef&uuml;hrt
     * wenn der {@code ApplicationContext} bereits initialisiert ist.
     * Ist er nicht initialisiert, dann ist der Methoden-Aufruf ohne Wirkung.
     * <p>
     * Die {@code ApplicationContextLifeCycle}-Methode wird in der topologischen
     * Reihenfolge der abh&auml;ngigen {@code ApplicationContext}e ausgef&uuml;hrt.
     *
     * @param task      die {@link ApplicationContextLifeCycle}-Methode die an den
     *                  {@link ApplicationContext} angewendet werden soll.
     * @param beanTypes ein Array von {@link BeanType}s als Basis f&uuml;r die Suche
     *                  nach dem {@link ApplicationContext} und deren
     *                  abh&auml;ngigen {@link ApplicationContext}en.
     * @throws IllegalArgumentException wird ausgel&ouml;st wenn keine
     *                                  {@code ApplicationContext}-Defintion
     *                                  gefunden wurde und daher auch keine
     *                                  {@code ApplicationContextLifeCycle}-Methode
     *                                  ausgef&uuml;hrt werden konnte.
     * @see ApplicationContextLifeCycle
     */
    void applyOnApplicationContextForBeansOf(final ApplicationContextLifeCycle task,
                                             final BeanType... beanTypes);

    /**
     * Alle zwischengespeicherten {@code ApplicationContext}e werden
     * gel&ouml;scht, sodass sie, wenn ein {@link Supplier} aufgerufen
     * wird, alle {@code ApplicationContext}e erneut initialisiert werden.
     */
    void clearAllApplicationContexts();

    /**
     * Liefert auf Basis der angegebenen {@link BeanType}s einen {@link Supplier} der
     * einen {@link ApplicationContext} liefert.
     *
     * @param beanTypes ein Array von {@link BeanType}s als Basis f&uuml;r die Suche
     *                  nach dem {@link ApplicationContext}.
     * @return der gefundene {@link ApplicationContext}.
     * @throws IllegalArgumentException wird ausgel&ouml;st wenn keine
     *                                  {@code ApplicationContext}-Defintion
     *                                  gefunden wurde.
     */
    Supplier<ApplicationContext> findApplicationContextForBeansOf(final BeanType... beanTypes);

    /**
     * Liefert auf Basis der angegebenen {@link BeanType}s einen {@link Supplier} der
     * einen {@link ApplicationContext} liefert.
     *
     * @param beanTypeSet ein {@link Set} von {@link BeanType}s als Basis f&uuml;r
     *                    die Suche nach dem {@link ApplicationContext}.
     * @return der gefundene {@link ApplicationContext}.
     * @throws IllegalArgumentException wird ausgel&ouml;st wenn keine
     *                                  {@code ApplicationContext}-Defintion
     *                                  gefunden wurde.
     */
    Supplier<ApplicationContext> findApplicationContextForBeansOf(final Set<BeanType> beanTypeSet);

    /**
     * Der Zeitstempel (ms), als dieser Wurzel-Kontext zum ersten Mal
     * geladen wurde oder 0 wenn er noch nicht geladen wurde.
     *
     * @return der Zeitstempel (ms), als dieser Wurzel-Kontext zum
     * ersten Mal geladen wurde
     */
    long getStartupDate();

    /**
     * Liefert den Wurzel-{@link ApplicationContext}.
     *
     * @return den Wurzel-{@code ApplicationContext}.
     */
    Supplier<ApplicationContext> rootApplicationContext();

    /**
     * Definiert die LifeCycle-Methoden die an einem
     * {@link ApplicationContext} ausgef&uuml;hrt werden
     * k&ouml;nnen.
     */
    enum ApplicationContextLifeCycle {

        /**
         * Token damit der zwischengespeicherte {@code ApplicationContext}
         * gel&ouml;scht werden kann.
         * <p>
         * Im Gegensatz zu {@link ApplicationContextLifeCycle#refresh} sorgt
         * {@link ApplicationContextLifeCycle#clear} daf&uuml;r das der
         * n&auml;chste Aufruf <b>ein neues</b> {@code ApplicationContext}-Objekt
         * erzeugt.
         *
         * @see ContextLevel#clear()
         */
        clear,
        clearCascading,
        /**
         * Token damit der zwischengespeicherte {@code ApplicationContext}
         * aufgefrischt werden kann.
         * <p>
         * Gibt es keinen zwischengespeicherte {@code ApplicationContext},
         * dann ist der Token und der Methoden-Aufruf ohne Wirkung.
         * <p>
         * Im Gegensatz zu {@link ApplicationContextLifeCycle#clear} sorgt
         * {@link ApplicationContextLifeCycle#refresh} daf&uuml;r das der
         * n&auml;chste Aufruf <b>das erzeugte</b>
         * {@code ApplicationContext}-Objekt auffrischt und kein neues Objekt
         * erzeugt.
         *
         * @see ConfigurableApplicationContext#refresh()
         */
        refresh,
        refreshCascading;

    }


    /**
     * Definiert alle Kategorien von Spring-Beans die sich in
     * einem {@link ApplicationContext} befinden k&ouml;nnen.
     * <p>
     * Es handelt sich bei diesen Kategorien von Spring-Beans
     * selbstverst&auml;ndlich nicht um eine abschliessende
     * Liste, sondern es werden nur diejenigen Kategorien
     * von Spring-Beans aufgef&uuml;hrt die charakteristisch
     * f&uuml;r den jeweiligen Kontext sind.
     */
    enum BeanType implements BeanCategory {

        One, Two, Three, Four;

    }

    interface BeanCategory {

    }
}
