package eu.dirk.haase.scope;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TenantScope implements Scope {

    private final String contextId;
    public final static Map<String, Map<String, Object>> tenantScope = new ConcurrentHashMap<>();
    public final static Map<String, String> beanScopeMap = new ConcurrentHashMap<>();

    public TenantScope(final String contextId) {
        this.contextId = contextId;
    }

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {

        String scope = beanScopeMap.get(name);
        if ((scope == null) || scope.equals(contextId)) {
            System.out.println("TenantScope: " + name + "; contextId: " + contextId);
            Map<String, Object> scopeMap = tenantScope.computeIfAbsent(contextId, (k) -> new ConcurrentHashMap<>());
            Object scopedObject = scopeMap.get(name);
            if (scopedObject == null) {
                beanScopeMap.put(name, contextId);
                scopedObject = objectFactory.getObject();
                scopeMap = new HashMap<>();
                scopeMap.put(name, scopedObject);
                tenantScope.put(name, scopeMap);
            }
            return scopedObject;
        }
        throw new IllegalArgumentException("");
    }

    @Override
    public Object remove(String name) {

        Map<String, Object> scopeMap = tenantScope.get(contextId);
        Object scopedObject = scopeMap.get(name);
        if (scopedObject != null) {
            scopeMap.remove(name);
            return scopedObject;
        } else {
            return null;
        }
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {

    }

    @Override
    public Object resolveContextualObject(String s) {
        return null;
    }

    @Override
    public String getConversationId() {
        return "tenant";
    }
}
