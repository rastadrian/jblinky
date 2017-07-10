package com.rastadrian.jblinky.core.light;

import java.util.Collection;

/**
 * Created on 6/29/17.
 *
 * @author Adrian Pena
 */
public interface LightSource {

    Collection<? extends Light> getLights();
}
