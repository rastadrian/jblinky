package com.rastadrian.jblinky.core.usb;

import com.rastadrian.jblinky.core.JBlinky;

/**
 * A usb device specification register. When {@link UsbRegistry} annotated {@link UsbDevice} classes
 * are provided to the {@link JBlinky}, this register provides the information required to scan and find
 * usb devices connected to the computer.
 *
 * @author Adrian Pena
 */
public class DeviceRegister {
    private final short vendorId;
    private final short productId;
    private final Class<? extends UsbDevice> deviceClass;

    public DeviceRegister(short vendorId, short productId, Class<? extends UsbDevice> deviceClass) {
        this.vendorId = vendorId;
        this.productId = productId;
        this.deviceClass = deviceClass;
    }

    /**
     * Matches the provided vendor and product ids to the register ones.
     * @param vendorId the vendorId to verify.
     * @param productId the productId to verify.
     * @return true if the device matches, false otherwise.
     */
    boolean matchesDevice(short vendorId, short productId) {
        return this.vendorId == vendorId && this.productId == productId;
    }

    Class<? extends UsbDevice> getDeviceClass() {
        return deviceClass;
    }
}
