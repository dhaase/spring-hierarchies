package eu.dirk.haase.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.*;
import org.springframework.context.support.AbstractApplicationContext;

import java.sql.Timestamp;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * Diese {@link ApplicationContextRegistry} registriert {@link ApplicationContext}-Instanzen
 * unter der {@code id} die gleichzeitig auch an der ApplicationContext-Instanz gesetzt wird
 * (siehe dazu {@link ConfigurableApplicationContext#setId(String)}).
 * <p>
 * Mit dieser {@link ApplicationContextRegistry} ist dann jede ApplicationContext-Instanz
 * statisch durch den Aufruf von
 * {@link ApplicationContextRegistry#getBeanFactory(String)} erreichbar.
 * <p>
 * Die Bean die im ApplicationContext definiert ist, registriert automatisch ihren
 * ApplicationContext dieser Bean:
 * <pre><code>
 *   &lt;bean id="app-ctx-one" class="eu.dirk.haase.bean.ApplicationContextRegistry" /&gt;
 * </code></pre>
 * Dabei gelten folgende Bedingungen:
 * <ul>
 * <li>Pro ApplicationContext-Instanz darf nur eine Bean definiert werden.</li>
 * <li>Alle ApplicationContext-Instanzen m&uuml;ssen durch ihre Bean einen
 * eindeutigen Namen erhalten. Am Besten dadurch das die Bean selbst einen eindeutigen
 * Namen erh&auml;lt.</li>
 * </ul>
 * <p>
 * Da der Aufruf der statischen Methode
 * {@link ApplicationContextRegistry#getBeanFactory(String)} prinzipiell
 * unabh&auml;ngig von den ApplicationContext-Instanzen ist von ihrer Initialisierung ist,
 * kann es bei nebenl&auml;ufigen Aufrufen zu einer der beiden folgenden Situationen
 * kommen:
 * <ol>
 * <li>die Methode {@link ApplicationContextRegistry#getBeanFactory(String)} wurde
 * vor der Initialisierung (durch {@link ApplicationContextRegistry#start()})
 * aufgerufen oder</li>
 * <li>die Initialisierung {@link ApplicationContextRegistry#start()} wurde
 * vor dem Aufruf der Methode {@link ApplicationContextRegistry#getBeanFactory(String)}
 * durchgef&uuml;hrt.</li>
 * </ol>
 * Das f&uuml;hrt dann jeweils zu diesem Verhalten:
 * <ol>
 * <li>Im ersten Fall wird der Aufrufer in der Methode
 * {@link ApplicationContextRegistry#getBeanFactory(String)}
 * blockiert (mit einem Timeout von 30 Sekunden) bis die Initialisierung mit der Methode
 * {@link ApplicationContextRegistry#start()} durchgef&uuml;hrt ist.</li>
 * <li>Im zweiten Fall wird dem Aufrufer von
 * {@link ApplicationContextRegistry#getBeanFactory(String)}
 * ungehindert die geforderte ApplicationContext-Instanz geliefert. Das sollte der
 * Normalfall sein</li>
 * </ol>
 * <p>
 * Diese Komponente ist eine {@link SmartLifecycle}-Komponente und kann daher gestartet
 * und gestoppt werden. Als eine {@link Phased}-Komponente wird diese Komponente als erste
 * Bean gestartet und zuletzt gestoppt. Die Methoden {@link ApplicationContextRegistry#start()},
 * {@link ApplicationContextRegistry#stop()} oder {@link ApplicationContextRegistry#stop(Runnable)}
 * sollten nur vom Lifecycle des ApplicationContextes ausgef&uuml;hrt werden.
 */
public final class ApplicationContextRegistry implements ApplicationContextAware, SmartLifecycle, BeanNameAware {

    private final static Map<String, Supplier<ApplicationContext>> contextMap = new ConcurrentHashMap<>();
    private final AtomicBoolean runningFlag;
    private String beanName;
    private ApplicationContext context;
    private String contextId;

    /**
     * Eine neue Instanz der {@link ApplicationContextRegistry} wird stets
     * nur als Bean &uuml;ber den ApplicationContext erzeugt:
     * <pre><code>
     * &lt;bean id="app-ctx-one" class="eu.dirk.haase.bean.ApplicationContextRegistry" /&gt;
     * </code></pre>
     */
    public ApplicationContextRegistry() {
        this.runningFlag = new AtomicBoolean(false);
    }

    /**
     * Liefert {@code true} wenn eine {@link BeanFactory} die unter der
     * angebenen {@code id} registriert ist.
     * <p>
     * Diese statische Methode kann auch unter nebenl&auml;ufigen
     * Bedingungen ausgef&uuml;hrt werden, der Aufrufer wird
     * praktisch nicht blockiert.
     * <p>
     * Bei nebenl&auml;ufigen Aufrufen kann das Ergebnis dieser Methode
     * vorl&auml;ufig sein. Das bedeutet zu einem sp&auml;teren Zeitpunkt
     * kann das vorl&auml;ufige {@code false}-Ergebnis zu {@code true}
     * wechseln.
     *
     * @param id die {@code id} unter der die BeanFactory registriert
     *           ist.
     * @return {@code true} wenn eine {@code BeanFactory} die unter der
     * angebenen {@code id} registriert ist oder {@code false} wenn keine
     * {@code BeanFactory} vorhanden ist oder der
     * {@code ApplicationContext} bereits gestoppt ist.
     */
    public static boolean containsBeanFactory(final String id) {
        final Supplier<ApplicationContext> contextSupplier = contextMap.get(id);
        if (contextSupplier instanceof BlockingSupplier) {
            return ((BlockingSupplier) contextSupplier).isPresent();
        } else if (contextSupplier instanceof ThrowingSupplier) {
            // der ApplicationContext ist bereits gestoppt
            // und ist daher nicht mehr verfuegbar:
            return false;
        } else {
            return (contextSupplier != null);
        }
    }

    /**
     * Liefert eine {@link BeanFactory} die unter der angebenen
     * {@code id} registriert ist.
     * <p>
     * Diese statische Methode kann auch unter nebenl&auml;ufigen
     * Bedingungen ausgef&uuml;hrt werden und kann daher den
     * Aufrufer auch blockieren (Timeout liegt bei 30 Sekunden).
     * <p>
     * Wichtig: Der Aufrufer wird bei unbekannten {@code id} stets
     * blockieren. Ist nicht sicher das die {@code id} existiert
     * dann sollte zun&auml;chst mit der Methode
     * {@link ApplicationContextRegistry#containsBeanFactory(String)}
     * gepr&uml;ft werden ob aktuell die {@code id} existiert.
     *
     * @param id die id unter der die BeanFactory registriert ist.
     * @return eine {@link BeanFactory} die unter der angebenen
     * {@code id} registriert ist.
     * @throws NoSuchElementException wird ausgel&ouml;st wenn keine
     *                                {@code BeanFactory} (mehr) verf&uuml;gbar
     *                                ist. Diese Exception wird auch
     *                                ausgel&ouml;st wenn bereits der
     *                                {@code ApplicationContext} gestoppt ist.
     */
    public static BeanFactory getBeanFactory(final String id) throws NoSuchElementException {
        final Supplier<ApplicationContext> contextSupplier = contextMap.computeIfAbsent(id, (k) -> new BlockingSupplier(id));
        // Mit dem folgenden Aufruf wird der Aufrufer blockiert
        // falls der ApplicationContext noch nicht initialisiert
        // ist:
        final ApplicationContext context = contextSupplier.get();

        if ((contextSupplier instanceof BlockingSupplier) && (context != null)) {
            // der BlockingSupplier wird jetzt nicht mehr gebraucht
            // und wird daher durch ein einfaches Lambda-Objekt ersetzt:
            contextMap.put(id, () -> context);
        }

        if (context == null) {
            throw new NoSuchElementException("No such ApplicationContext with id [" + id + "]");
        }
        return context;
    }

    public ApplicationContext getContext() {
        return context;
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    /**
     * Als eine {@link Phased}-Komponente wird diese Komponente als erste
     * Bean gestartet und zuletzt gestoppt.
     *
     * @return den Wert von {@link Integer#MIN_VALUE}.
     */
    @Override
    public int getPhase() {
        return Integer.MIN_VALUE;
    }

    /**
     * Initialisiert die {@code id} f&uuml;r die {@link ApplicationContext}-Instanz.
     * <p>
     * Dabei wird in der folgende Reihenfolge vorgegangen:
     * <ol>
     * <li>ist die {@link ApplicationContext#getId()}
     * bereits gesetzt, dann wird diese verwendet, oder</li>
     * <li>ist die Property {@code contextId} gesetzt,
     * dann wird diese verwendet, oder </li>
     * <li>es wird der Bean-Name der Bean verwendet.</li>
     * </ol>
     * <p>
     * Anschliessend wird die {@code id} an der ApplicationContext-Instanz gesetzt.
     */
    private void initContextId() {
        if ((context.getId() == null) || context.getId().startsWith("org.springframework")) {
            // die ApplicationContext-Instanz hat noch keine id:
            this.contextId = (this.contextId == null ? this.beanName : this.contextId);
        } else {
            // die ApplicationContext-Instanz hat bereits eine id:
            this.contextId = context.getId();
        }
        if (context instanceof ConfigurableApplicationContext) {
            ((ConfigurableApplicationContext) context).setId(this.contextId);
        }
        if (context instanceof AbstractApplicationContext) {
            final String displayName = context.getClass().getSimpleName() + "(" + this.contextId + ")";
            ((AbstractApplicationContext) context).setDisplayName(displayName);
        }
    }

    /**
     * Initialisiert die {@code contextMap} mit der {@link ApplicationContext}-Instanz.
     * <p>
     * Falls bereits ein Aufrufer auf diese ApplicationContext-Instanz wartet,
     * dann wird dieser wieder freigegeben da nun die ApplicationContext-Instanz
     * verf&uuml;gbar ist.
     */
    private void initContextMap() {
        final Supplier<ApplicationContext> lastContext = contextMap.put(context.getId(), () -> context);
        if (lastContext instanceof BlockingSupplier) {
            // es wird bereits auf diese ApplicationContext-Instanz gewartet:
            ((BlockingSupplier) lastContext).set(context);
        } else if ((lastContext != null) && !(lastContext instanceof ThrowingSupplier)) {
            throw new IllegalStateException("ApplicationContext with id [" +
                    context.getId() + "] can not be set multiple times.");
        }
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public boolean isRunning() {
        return this.runningFlag.get();
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void start() {
        if (this.runningFlag.compareAndSet(false, true)) {
            initContextId();
            initContextMap();
        }
    }

    @Override
    public void stop(Runnable callback) {
        if (this.runningFlag.compareAndSet(true, false)) {
            callback.run();
            contextMap.put(this.contextId, new ThrowingSupplier(this.contextId));
        }
    }

    @Override
    public void stop() {
        stop(() -> {
        });
    }

    /**
     * Mit diesem {@link BlockingSupplier} wird der Aufrufer blockiert wenn
     * die Methode {@link BlockingSupplier#get()} aufgerufen wird und dieser
     * {@link BlockingSupplier} hat noch keine
     * {@link ApplicationContext}-Instanz hat.
     * <p>
     * Der Aufrufer wartet bis die Methode (einmalig)
     * {@link BlockingSupplier#set(ApplicationContext)} aufgerufen wurde, erst
     * dann wird die ApplicationContext-Instanz geliefert.
     *
     * @see ApplicationContextRegistry#initContextMap()
     * @see ApplicationContextRegistry#containsBeanFactory(String)
     */
    private static final class BlockingSupplier implements Supplier<ApplicationContext> {
        final String id;
        final CountDownLatch latch;
        final AtomicReference<ApplicationContext> ref;

        BlockingSupplier(final String id) {
            this.id = id;
            this.latch = new CountDownLatch(1);
            this.ref = new AtomicReference<>();
        }

        boolean await() {
            try {
                return latch.await(30, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                throw new IllegalStateException("Interrupted while waiting for an " +
                        "ApplicationContext with id [" + id + "]: " + ex, ex);
            }
        }

        @Override
        public ApplicationContext get() {
            if (await()) {
                return ref.get();
            } else {
                throw new IllegalStateException("Waiting time elapsed before the " +
                        "ApplicationContext with id [" + id + "] was available.");
            }
        }

        boolean isPresent() {
            return this.ref.get() != null;
        }

        void set(final ApplicationContext context) {
            if (ref.compareAndSet(null, context)) {
                latch.countDown();
            } else {
                throw new IllegalStateException("ApplicationContext with id [" +
                        context.getId() + "] can not be set multiple times.");
            }
        }
    }

    /**
     * Dieser {@link Supplier} l&ouml;st eine {@link NoSuchElementException}
     * aus wenn die {@link ThrowingSupplier#get()} Methode aufgerufen wird.
     * <p>
     * Dieser {@link Supplier} wird dann registriert wenn die Bean
     * {@link ApplicationContextRegistry} gestoppt wird. Das in der Regel dann
     * der Fall wenn auch die {@link ApplicationContext}-Instanz gestoppt wird.
     *
     * @see ApplicationContextRegistry#stop(Runnable)
     * @see ApplicationContextRegistry#containsBeanFactory(String)
     */
    private static final class ThrowingSupplier implements Supplier<ApplicationContext> {
        final String msg;

        ThrowingSupplier(final String id) {
            this.msg = "ApplicationContext with id [" +
                    id + "] is already at " +
                    new Timestamp(System.currentTimeMillis()) + " stopped.";
        }

        /**
         * L&ouml;st stets eine {@link NoSuchElementException}
         * aus.
         *
         * @return ended mit einer {@link NoSuchElementException}.
         */
        @Override
        public ApplicationContext get() {
            throw new NoSuchElementException(this.msg);
        }
    }
}