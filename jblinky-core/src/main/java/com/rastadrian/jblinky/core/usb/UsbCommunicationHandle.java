package com.rastadrian.jblinky.core.usb;

import java.util.List;

/**
 * A USB Communication Handle provides the processes required to manage and interact with USB devices.
 *
 * @author Adrian Pena
 */
public interface UsbCommunicationHandle {
    /**
     * Scans the computer's USB ports and attempts to locate devices that comply with the provided Device Registers.
     *
     * @param deviceRegisters the list of usb device specifications to look for.
     * @return the list of USB devices.
     */
    List<UsbDevice> getConnectedUsbLights(List<DeviceRegister> deviceRegisters);

    /**
     * Transfers the message to the given device.
     *
     * @param device  the device to send the message to.
     * @param message the message to be sent.
     */
    void communicateWithDevice(UsbDevice device, byte[] message);

    /**
     * Terminates the communication with the given device.
     *
     * @param device the device to disconnect.
     */
    void disconnect(UsbDevice device);
}
