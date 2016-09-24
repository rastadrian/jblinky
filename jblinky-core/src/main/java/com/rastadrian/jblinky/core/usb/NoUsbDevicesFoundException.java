package com.rastadrian.jblinky.core.usb;

/**
 * An exception to be thrown if no connected usb lights have been found on the computer.
 *
 * @author Adrian Pena
 */
public class NoUsbDevicesFoundException extends RuntimeException {

    NoUsbDevicesFoundException() {
        super("No compatible USB lights were found.");
    }
}
