package com.rastadrian.jblinky.core.usb;

import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Created on 9/24/16.
 *
 * @author Adrian Pena
 */
public class DeviceRegisterTest {
    @Test
    public void matchesDevice() throws Exception {
        short vendorId = 0;
        short productId = 1;
        DeviceRegister deviceRegister;
        boolean matches;

        given: {
            deviceRegister = new DeviceRegister(vendorId, productId, TestUsbDevice.class);
        }
        when: {
            matches = deviceRegister.matchesDevice(vendorId, productId);
        }
        then: {
            assertThat(matches).isTrue();
        }
    }

    @Test
    public void matchesDevice_withInvalidVendorId() throws Exception {
        short vendorId = 0;
        short productId = 1;
        DeviceRegister deviceRegister;
        boolean matches;

        given: {
            deviceRegister = new DeviceRegister(vendorId, productId, TestUsbDevice.class);
        }
        when: {
            matches = deviceRegister.matchesDevice((short) 2, productId);
        }
        then: {
            assertThat(matches).isFalse();
        }
    }

    @Test
    public void matchesDevice_withInvalidProductId() throws Exception {
        short vendorId = 0;
        short productId = 1;
        DeviceRegister deviceRegister;
        boolean matches;

        given: {
            deviceRegister = new DeviceRegister(vendorId, productId, TestUsbDevice.class);
        }
        when: {
            matches = deviceRegister.matchesDevice(vendorId, (short) 3);
        }
        then: {
            assertThat(matches).isFalse();
        }
    }

    @Test
    public void getDeviceClass() throws Exception {
        short vendorId = 0;
        short productId = 1;
        DeviceRegister deviceRegister;
        Class<? extends UsbDevice> deviceClass;
        Class<? extends UsbDevice> providedDeviceClass;
        given: {
            deviceClass = TestUsbDevice.class;
            deviceRegister = new DeviceRegister(vendorId, productId, deviceClass);
        }
        when: {
            providedDeviceClass = deviceRegister.getDeviceClass();
        }
        then: {
            assertThat(providedDeviceClass).isEqualTo(deviceClass);
        }
    }

    private class TestUsbDevice extends UsbDevice {

        protected byte getRequestType() {
            return 0;
        }

        protected byte getRequest() {
            return 0;
        }

        protected short getValue() {
            return 0;
        }

        protected short getIndex() {
            return 0;
        }

        protected long getTimeout() {
            return 0;
        }
    }
}