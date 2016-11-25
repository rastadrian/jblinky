package com.rastadrian.jblinky.core.usb.light;

/**
 * Created by adrian on 10/6/16.
 */
public class TestUsbLight extends UsbLight {
    @Override
    public void success() {

    }

    @Override
    public void failure() {

    }

    @Override
    public void inProgress() {

    }

    @Override
    public void warning() {

    }

    @Override
    public void off() {

    }

    @Override
    protected byte getRequestType() {
        return 0;
    }

    @Override
    protected byte getRequest() {
        return 0;
    }

    @Override
    protected short getValue() {
        return 0;
    }

    @Override
    protected short getIndex() {
        return 0;
    }

    @Override
    protected long getTimeout() {
        return 0;
    }
}
