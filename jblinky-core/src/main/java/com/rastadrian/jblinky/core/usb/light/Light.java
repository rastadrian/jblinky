package com.rastadrian.jblinky.core.usb.light;

import com.rastadrian.jblinky.core.probe.Probe;
import com.rastadrian.jblinky.core.probe.State;

import java.util.Map;

/**
 * A device that produces visible light (preferably in multiple colors), which allows visual indication for multiple states
 * of health probes.
 *
 * @author Adrian Pena
 */
public interface Light {
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
     * It will iterate and verify all the light probes.
     * <p>
     * If the a probe's verification turns {@link State#SUCCESS} the
     * verification will move to the next probe, if its state is {@link State#FAILURE}, it will halt the verification
     * and stay on that probe until it gets resolved. For {@link State#WARNING} or {@link State#IN_PROGRESS} it will remain
     * on the state for an amount of time and then continue for the next probe.
     *</p>
     * <p>
     * This process will block the executor's thread.
     * </p>
     */
    void verifyProbes();

    /**
     * It will iterate and verify all the given light probes.
     * <p>
     * If the a probe's verification turns {@link State#SUCCESS} the
     * verification will move to the next probe, if its state is {@link State#FAILURE}, it will halt the verification
     * and stay on that probe until it gets resolved. For {@link State#WARNING} or {@link State#IN_PROGRESS} it will remain
     * on the state for an amount of time and then continue for the next probe.
     *</p>
     * <p>
     * The verification can also receive an optional {@link ProbeCallback} that will trigger {@link ProbeCallback#onProbesUpdated(Map)}
     * every time a probe is verified.
     * </p>
     * <p>
     * This process will block the executor's thread.
     * </p>
     * @param probes the probes to verify.
     */
    void verifyProbes(Probe... probes);

    /**
     * It will iterate and verify all the light probes.
     * <p>
     * If the a probe's verification turns {@link State#SUCCESS} the
     * verification will move to the next probe, if its state is {@link State#FAILURE}, it will halt the verification
     * and stay on that probe until it gets resolved. For {@link State#WARNING} or {@link State#IN_PROGRESS} it will remain
     * on the state for an amount of time and then continue for the next probe.
     *</p>
     * <p>
     * The verification can also receive an optional {@link ProbeCallback} that will trigger {@link ProbeCallback#onProbesUpdated(Map)}
     * every time a probe is verified.
     * </p>
     * <p>
     * This process will block the executor's thread.
     * </p>
     * @param callback an optional probe callback.
     */
    void verifyProbes(ProbeCallback callback);

    /**
     * It will iterate and verify all the given light probes.
     * <p>
     * If the a probe's verification turns {@link State#SUCCESS} the
     * verification will move to the next probe, if its state is {@link State#FAILURE}, it will halt the verification
     * and stay on that probe until it gets resolved. For {@link State#WARNING} or {@link State#IN_PROGRESS} it will remain
     * on the state for an amount of time and then continue for the next probe.
     *</p>
     * <p>
     * The verification can also receive an optional {@link ProbeCallback} that will trigger {@link ProbeCallback#onProbesUpdated(Map)}
     * every time a probe is verified.
     * </p>
     * <p>
     * This process will block the executor's thread.
     * </p>
     * @param probes the probes to verify.
     * @param callback an optional probe callback.
     */
    void verifyProbes(Probe[] probes, ProbeCallback callback);

    /**
     * Turns off and disconnects from the usb light.
     */
    void disconnect();
}
