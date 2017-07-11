package com.rastadrian.jblinky.core.light;

/**
 * State values for a light.
 *
 * Created on 7/6/17.
 *
 * @author Adrian Pena
 */
public enum LightState {
    /**
     * The light is in a "success" mode, usually green.
     */
    SUCCESS,
    /**
     * The light is "in progress" mode, usually blue or yellow.
     */
    IN_PROGRESS,
    /**
     * The light is in a "warning" mode, usually yellow.
     */
    WARNING,
    /**
     * The light is in a "failure" mode, usually red.
     */
    FAILURE,
    /**
     * The light is turned off.
     */
    OFF,
    /**
     * The light is disconnected, it shouldn't be attempted to interact with it.
     */
    DISCONNECTED
}
