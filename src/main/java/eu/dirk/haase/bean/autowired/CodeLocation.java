package eu.dirk.haase.bean.autowired;

import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

public class CodeLocation {


    public boolean isImplying(final Object thisObject, final Object thatObject) {
        return isImplyingByCodeSource(thisObject, thatObject);
    }

    private boolean isImplyingByCodeSource(final Object thisObject, final Object thatObject) {
        final CodeSource thisCodeSource = codeSource(thisObject);
        final CodeSource thatCodeSource = codeSource(thatObject);
        if ((thisCodeSource != null) && (thatCodeSource != null)) {
            return thisCodeSource.implies(thatCodeSource);
        }
        return isImplyingByUrl(thisObject, thatObject);
    }

    private CodeSource codeSource(Object thisObject) {
        final ProtectionDomain protectionDomain = thisObject.getClass().getProtectionDomain();
        if (protectionDomain != null) {
            final CodeSource codeSource = protectionDomain.getCodeSource();
            return (codeSource.getLocation() != null ? codeSource : null);
        }
        return null;
    }


    private boolean isImplyingByUrl(final Object thisObject, final Object thatObject) {
        final String thisPrefix = locationPrefix(thisObject);
        final String thatPrefix = locationPrefix(thatObject);
        return thisPrefix.equals(thatPrefix);
    }

    private String locationPrefix(Object thisObject) {
        final ClassLoader classLoader = thisObject.getClass().getClassLoader();
        final String className = thisObject.getClass().getName();
        final String classFile = className.replace('.', '/') + ".class";
        final URL url = classLoader.getResource(classFile);
        final String urlStr = url.toExternalForm();
        return urlStr.substring(0, urlStr.length() - classFile.length());
    }


}
