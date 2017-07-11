package com.rastadrian.jblinky.devices.usb;

import java.util.Collection;

/**
 * A component that allows jBlinky to communicate with USB devices.
 * Created on 7/8/17.
 *
 * @author Adrian Pena
 */
public interface UsbCommunicationsHandler {

    /**
     * Retrieves a list of {@link UsbLight} connected to the computer.
     *
     * @param registries the registries (vendorId/productId) that should be looked for.
     * @return a list of connected lights.
     */
    Collection<UsbLight> getLights(Collection<UsbRegistry> registries);

    /**
     * Given the light, it should communicate the message
     *
     * @param light   the light to talk to.
     * @param message the message to be sent.
     */
    void communicateWithLight(UsbLight light, byte[] message);

    /**
     * Allows certain implementations to gracefully disconnect and deallocate the device handler.
     *
     * @param light the light to disconnect.
     */
    void disconnectLight(UsbLight light);
}
