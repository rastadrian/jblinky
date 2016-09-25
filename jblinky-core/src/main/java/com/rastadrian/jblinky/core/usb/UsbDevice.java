package com.rastadrian.jblinky.core.usb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A physical electronic component that complies with the USB standard.
 *
 * @author Adrian Pena
 */
public abstract class UsbDevice {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsbDevice.class);
    private UsbCommunicationHandle handle;

    /**
     * Disconnect from the USB Device communication bridge.
     */
    public void disconnect() {
        LOGGER.debug("Disconnecting USB device [{}].", this.getClass().getSimpleName());
        handle.disconnect(this);
    }

    /**
     * Sets an USB communication device handle for extensible USB control transfer.
     *
     * @param handle the usb device handle to set.
     */
    public void setHandle(UsbCommunicationHandle handle) {
        this.handle = handle;
    }

    /**
     * Sends a message to the usb light device.
     *
     * @param message the message to send.
     */
    protected void sendMessage(byte[] message) {
        handle.communicateWithDevice(this, message);
    }

    /**
     * The request type value for the setup packets on this usb device.
     *
     * @return the usb request type.
     */
    protected abstract byte getRequestType();

    /**
     * The request value for the setup packets on this usb device.
     *
     * @return the usb device request.
     */
    protected abstract byte getRequest();

    /**
     * The value for the setup packets for this usb device.
     *
     * @return the usb device packet value.
     */
    protected abstract short getValue();

    /**
     * The index value for the setup packets on this usb device.
     *
     * @return the usb device index value.
     */
    protected abstract short getIndex();

    /**
     * The timeout for the device communication. For an unlimited timeout, use value 0.
     *
     * @return the timeout in milliseconds.
     */
    protected abstract long getTimeout();
}
