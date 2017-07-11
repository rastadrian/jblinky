package com.rastadrian.jblinky.devices.usb;

import com.rastadrian.jblinky.core.light.BaseLight;
import com.rastadrian.jblinky.core.light.Light;
import com.rastadrian.jblinky.core.light.LightSource;
import lombok.extern.slf4j.Slf4j;

/**
 * A {@link Light} that communicates via USB.
 * Created on 6/29/17.
 *
 * @author Adrian Pena
 */
@Slf4j
public abstract class UsbLight extends BaseLight {

    private UsbLightSource lightSource;
    private UsbCommunicationsHandler usbCommunicationsHandler;

    @Override
    public void disconnect() {
        usbCommunicationsHandler.disconnectLight(this);
    }

    @Override
    public LightSource getSource() {
        return this.lightSource;
    }

    /**
     * The {@link Light}'s means to communicate to the actual device.
     *
     * @param usbCommunicationsHandler the communications handler.
     */
    public void setHandler(UsbCommunicationsHandler usbCommunicationsHandler) {
        this.usbCommunicationsHandler = usbCommunicationsHandler;
    }

    void setSource(UsbLightSource lightSource) {
        this.lightSource = lightSource;
    }

    protected void communicate(byte[] message) {
        usbCommunicationsHandler.communicateWithLight(this, message);
    }

    /**
     * The request type value for the setup packets on this usb device.
     *
     * @return the usb request type.
     */
    public abstract byte getRequestType();

    /**
     * The request value for the setup packets on this usb device.
     *
     * @return the usb device request.
     */
    public abstract byte getRequest();

    /**
     * The value for the setup packets for this usb device.
     *
     * @return the usb device packet value.
     */
    public abstract short getValue();

    /**
     * The index value for the setup packets on this usb device.
     *
     * @return the usb device index value.
     */
    public abstract short getIndex();

    /**
     * The timeout for the device communication. For an unlimited timeout, use value 0.
     *
     * @return the timeout in milliseconds.
     */
    public abstract long getTimeout();
}
