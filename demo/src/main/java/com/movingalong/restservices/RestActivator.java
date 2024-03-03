package com.movingalong.restservices;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

/**
 * Activator class for JAX-RS application. This class defines the root path for
 * all RESTful services
 * and registers the REST resource classes.
 */
@ApplicationPath("/rest")
public class RestActivator extends Application {
    /**
     * Configures the JAX-RS application and registers resource classes.
     * 
     * @return a Set of classes that are resource classes or providers to be
     *         included in the
     *         published JAX-RS application.
     */
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(ContentService.class);
        resources.add(UserService.class);
        return resources;
    }
}
