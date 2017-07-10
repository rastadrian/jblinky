package com.rastadrian.jblinky.devices.usb;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 7/8/17.
 *
 * @author Adrian Pena
 */
public class UsbRegistryTest {
    private UsbRegistry usbRegistry;

    @Test
    public void matchesDeviceWithValidDevice() throws Exception {
        //Given
        short vendorId = 0x5;
        short productId = 0x6;
        usbRegistry = new UsbRegistry(vendorId, productId, UsbLight.class);

        //When
        boolean matches = usbRegistry.matchesDevice(vendorId, productId);

        //Then
        assertThat(matches).isTrue();
    }

    @Test
    public void matchesDeviceWithInvalidDevice() throws Exception {
        //Given
        short vendorId = 0x5;
        short productId = 0x6;
        usbRegistry = new UsbRegistry(vendorId, productId, UsbLight.class);

        //When
        boolean matches = usbRegistry.matchesDevice((short) 0x7, (short) 0x3);

        //Then
        assertThat(matches).isFalse();
    }

    @Test
    public void getDeviceClass() throws Exception {
        //Given
        short vendorId = 0x5;
        short productId = 0x6;
        usbRegistry = new UsbRegistry(vendorId, productId, UsbLight.class);

        //When
        Class<? extends UsbLight> deviceClass = usbRegistry.getDeviceClass();

        //Then
        assertThat(deviceClass).isEqualTo(UsbLight.class);
    }
}