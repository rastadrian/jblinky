package com.rastadrian.jblinky.devices;

import com.rastadrian.jblinky.core.usb.UsbRegistry;
import com.rastadrian.jblinky.core.usb.light.UsbLight;

/**
 * The Delcom 904008 Gen 2 is an USB Light device manufactured by Delcom.
 *
 * @author Adrian Pena
 */
@UsbRegistry(vendorId = 0x0fc5, productId = (short) 0xb080)
public class DelcomGen2Light extends UsbLight {

    public void success() {
        off();
        setColor((byte) 0x06);
    }

    public void failure() {
        off();
        setColor((byte) 0x05);
    }

    public void inProgress() {
        off();
        setColor((byte) 0x03);
    }

    public void warning() {
        off();
        setColor((byte) 0x04);
    }

    public void off() {
        setColor((byte) 0x07);
    }

    protected byte getRequestType() {
        return 0x21;
    }

    protected byte getRequest() {
        return 0x09;
    }

    protected short getValue() {
        return 0x0635;
    }

    protected short getIndex() {
        return 0x000;
    }

    protected long getTimeout() {
        return 0;
    }

    private void setColor(byte color) {
        sendMessage(new byte[]{(byte) 0x65, (byte) 0x0C, (byte) 0xFF, color, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00});
    }
}
