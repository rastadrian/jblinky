package com.rastadrian.jblinky.core.usb.light;

import com.rastadrian.jblinky.core.probe.Status;

import java.util.Map;

/**
 * A callback listener for the probe verification process.
 *
 * @author Adrian Pena
 */
public interface ProbeCallback {

    /**
     * A callback method to be invoked when a probe verification is completed.
     *
     * @param statuses the list of status reports up until this current probe verification.
     */
    void onProbesUpdated(Map<String, Status> statuses);
}
