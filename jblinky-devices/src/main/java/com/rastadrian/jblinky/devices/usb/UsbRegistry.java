package com.rastadrian.jblinky.devices.usb;

import com.rastadrian.jblinky.core.JBlinky;

/**
 * A usb device specification register. When {@link UsbSpecification} annotated {@link UsbLight} classes
 * are provided to the {@link JBlinky}, this register provides the information required to scan and find
 * usb devices connected to the computer.
 *
 * @author Adrian Pena
 */
public class UsbRegistry {
    private final short vendorId;
    private final short productId;
    private final Class<? extends UsbLight> deviceClass;

    public UsbRegistry(short vendorId, short productId, Class<? extends UsbLight> deviceClass) {
        this.vendorId = vendorId;
        this.productId = productId;
        this.deviceClass = deviceClass;
    }

    /**
     * Matches the provided vendor and product ids to the register ones.
     *
     * @param vendorId  the vendorId to verify.
     * @param productId the productId to verify.
     * @return true if the device matches, false otherwise.
     */
    public boolean matchesDevice(short vendorId, short productId) {
        return this.vendorId == vendorId && this.productId == productId;
    }

    /**
     * @return the UsbLight class associated with this registry.
     */
    public Class<? extends UsbLight> getDeviceClass() {
        return deviceClass;
    }
}
