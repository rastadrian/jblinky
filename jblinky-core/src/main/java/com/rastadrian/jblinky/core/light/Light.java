package com.rastadrian.jblinky.core.light;

/**
 * Created on 6/29/17.
 *
 * @author Adrian Pena
 */
public interface Light {

    LightState getState();

    LightSource getSource();

    String getId();

    /**
     * Turns the light on to a SUCCESS (usually a green) light.
     */
    void success();

    /**
     * Turns the light on to a FAILURE (usually red) light.
     */
    void failure();

    /**
     * Sets the light to an IN_PROGRESS (usually yellow or strobe) light.
     */
    void inProgress();

    /**
     * Sets the light to a WARNING (usually yellow) light.
     */
    void warning();

    /**
     * Turns the light off.
     */
    void off();

    /**
     * Some lights might provide a disconnect feature.
     */
    void disconnect();
}
