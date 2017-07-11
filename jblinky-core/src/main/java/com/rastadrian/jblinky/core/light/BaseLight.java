package com.rastadrian.jblinky.core.light;

/**
 * A default light that maintains the state of the light's interactions.
 *
 * Created on 7/6/17.
 *
 * @author Adrian Pena
 */
public abstract class BaseLight implements Light {

    private LightState state = LightState.OFF;

    @Override
    public LightState getState() {
        return state;
    }

    @Override
    public void success() {
        state = LightState.SUCCESS;
    }

    @Override
    public void failure() {
        state = LightState.FAILURE;
    }

    @Override
    public void inProgress() {
        state = LightState.IN_PROGRESS;
    }

    @Override
    public void warning() {
        state = LightState.WARNING;
    }

    @Override
    public void off() {
        state = LightState.OFF;
    }

    @Override
    public void disconnect() {
        off();
        state = LightState.DISCONNECTED;
    }
}
