package com.github.deripas.pmd;

import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

/**
 * Provides default methods for loading resources as streams and retrieving their file paths
 * using the class loader of the current class.
 */
public interface TestWithResource {

    /**
     * Loads a resource as an InputStream using the class loader of the current class.
     *
     * @param name the name of the resource to be loaded
     * @return an InputStream for the resource, or null if the resource could not be found
     */
    default InputStream stream(String name) {
        return Thread.currentThread()
            .getContextClassLoader()
            .getResourceAsStream(name);
    }

    /**
     * Retrieves the file path of a given resource.
     *
     * @param name the name of the resource to locate
     * @return the file path of the resource, or null if the resource could not be found
     */
    default String path(String name) {
        final URL resource = Thread.currentThread()
            .getContextClassLoader()
            .getResource(name);
        return Optional.ofNullable(resource)
            .map(URL::getFile)
            .orElse(null);
    }
}
