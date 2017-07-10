package com.rastadrian.jblinky.core.light;

import java.util.Collection;

/**
 * A source where {@link Light} components may be retrieved from.
 *
 * Created on 6/29/17.
 *
 * @author Adrian Pena
 */
public interface LightSource {

    /**
     * @return a collection of lights that may be empty.
     */
    Collection<? extends Light> getLights();
}
