package com.rastadrian.jblinky.core.usb.light;

/**
 * Exception to be thrown when USB device specifications where provided but they were not {@link UsbLight}s.
 *
 * @author Adrian Pena
 */
class NonUsbLightFoundException extends RuntimeException {

    NonUsbLightFoundException() {
        super("An USB Specification was registered and found, but it is not a light.");
    }
}
