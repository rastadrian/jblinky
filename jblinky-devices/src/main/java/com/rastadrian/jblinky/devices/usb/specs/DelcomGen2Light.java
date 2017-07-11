package com.rastadrian.jblinky.devices.usb.specs;

import com.rastadrian.jblinky.core.light.LightState;
import com.rastadrian.jblinky.devices.usb.UsbLight;
import com.rastadrian.jblinky.devices.usb.UsbSpecification;
import lombok.extern.slf4j.Slf4j;

/**
 * The Delcom 904008 Gen 2 is an USB Light device manufactured by Delcom.
 *
 * @author Adrian Pena
 */
@Slf4j
@UsbSpecification(vendorId = 0x0fc5, productId = (short) 0xb080)
public class DelcomGen2Light extends UsbLight {

    @Override
    public String getId() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void success() {
        if (getState() == LightState.SUCCESS) {
            LOGGER.debug("Light already on SUCCESS state, skipping.");
            return;
        }
        super.success();
        LOGGER.debug("Setting USB Light to SUCCESS.");
        doOff();
        setColor((byte) 0x06);
    }

    @Override
    public void failure() {
        super.failure();
        LOGGER.debug("Setting USB Light to FAILURE.");
        doOff();
        setColor((byte) 0x05);
    }

    @Override
    public void inProgress() {
        super.inProgress();
        LOGGER.debug("Setting USB Light to IN_PROGRESS.");
        doOff();
        setColor((byte) 0x03);
    }

    @Override
    public void warning() {
        super.warning();
        LOGGER.debug("Setting USB Light to WARNING.");
        doOff();
        setColor((byte) 0x04);
    }

    @Override
    public void off() {
        super.off();
        LOGGER.debug("Setting USB Light to OFF.");
        doOff();
    }

    @Override
    public byte getRequestType() {
        return 0x21;
    }

    @Override
    public byte getRequest() {
        return 0x09;
    }

    @Override
    public short getValue() {
        return 0x0635;
    }

    @Override
    public short getIndex() {
        return 0x000;
    }

    @Override
    public long getTimeout() {
        return 0;
    }

    private void doOff() {
        setColor((byte) 0x07);
    }

    private void setColor(byte color) {
        communicate(new byte[]{(byte) 0x65, (byte) 0x0C, (byte) 0xFF, color, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00});
    }
}
