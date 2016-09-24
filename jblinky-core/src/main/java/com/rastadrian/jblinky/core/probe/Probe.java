package com.rastadrian.jblinky.core.probe;

/**
 * A Probe is a verification routine that can be performed to assert the health or availability status of a system
 * or component.
 *
 * @author Adrian Pena
 */
public interface Probe {
    /**
     * A unique name for the probe.
     * @return the probe's name
     */
    String getName();

    /**
     * Performs the verification of the probe. The process has to be synchronous and has to be return a {@link Status} report.
     * @return the {@link Status} report of the verification.
     */
    Status verify();
}
