package com.rastadrian.jblinky.devices.usb;

import java.util.Collection;

/**
 * Created on 7/8/17.
 *
 * @author Adrian Pena
 */
public interface UsbCommunicationsHandler {

    Collection<UsbLight> getLights(Collection<UsbRegistry> registries);

    void communicateWithLight(UsbLight light, byte[] message);

    void disconnectLight(UsbLight light);
}
