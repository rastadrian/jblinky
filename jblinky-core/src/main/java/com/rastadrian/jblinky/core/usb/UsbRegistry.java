package com.rastadrian.jblinky.core.usb;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a class is a USB Device. It is required that the class extends {@link UsbDevice}.
 * <p>
 * A USB Device provides manufacturing information such as the vendor and product identifiers which allows the scanning
 * and detection of connected usb lights from a computer.
 *
 * @author Adrian Pena
 */
@Target(ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface UsbRegistry {
    /**
     * The USB vendor identifier.
     *
     * @return the device's vendor id.
     */
    short vendorId();

    /**
     * The USB device id.
     *
     * @return the device's id.
     */
    short productId();
}
