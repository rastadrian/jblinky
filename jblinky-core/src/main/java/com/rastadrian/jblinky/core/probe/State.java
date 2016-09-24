package com.rastadrian.jblinky.core.probe;

/**
 * The enumeration of possible probe states.
 *
 * @author Adrian Pena
 */
public enum State {
    /**
     * The probe was valid.
     */
    SUCCESS,
    /**
     * The probe failed verification.
     */
    FAILURE,
    /**
     * The probe was either unable to be verified of on an unstable state.
     */
    WARNING,
    /**
     * The probe can't be verified, due to being in progress.
     */
    IN_PROGRESS
}
