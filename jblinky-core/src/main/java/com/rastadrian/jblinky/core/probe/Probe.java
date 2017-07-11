package com.rastadrian.jblinky.core.probe;

import com.rastadrian.jblinky.core.light.Light;

/**
 * A Probe is a verification routine that can be performed to assert the health or availability status of a system
 * or component.
 *
 * @author Adrian Pena
 */
public interface Probe {

    /**
     * A unique name for the probe.
     *
     * @return the probe's name
     */
    String getName();

    /**
     * Performs the verification of the probe. {@link Light}s might be given as a parameter and they will react towards
     * the result of the Probe.
     *
     * @param lights the lights that should react towards the probe.
     * @return the {@link Status} report of the verification.
     */
    Status verify(Light... lights);
}
