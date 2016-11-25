package com.rastadrian.jblinky.core.usb.light;

import com.rastadrian.jblinky.core.usb.DeviceRegister;
import com.rastadrian.jblinky.core.usb.UsbCommunicationHandle;
import com.rastadrian.jblinky.core.usb.UsbDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A USB Light device factory. It is responsible to searching and connecting USB light devices.
 *
 * @author Adrian Pena
 */
public class LightFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(LightFactory.class);

    private final List<DeviceRegister> deviceRegisters;
    private final UsbCommunicationHandle handle;

    public LightFactory(List<DeviceRegister> deviceRegisters, UsbCommunicationHandle handle) {
        this.deviceRegisters = deviceRegisters;
        this.handle = handle;
    }

    /**
     * It will search and connect to USB Light devices on the computer based on the
     * factory's available usb device specification registries.
     *
     * @return the list of found lights.
     */
    public List<Light> detectLights() {
        List<Light> lights = new ArrayList<>();
        if(deviceRegisters == null || deviceRegisters.isEmpty()) {
            LOGGER.error("No USB specifications were provided..");
            throw new NoUsbDevicesFoundException();
        }
        List<UsbDevice> usbDevices = handle.getConnectedUsbLights(deviceRegisters);
        if (usbDevices.isEmpty()) {
            LOGGER.error("No compatible USB devices were found.");
            throw new NoUsbDevicesFoundException();
        }
        for (UsbDevice usbDevice : usbDevices) {
            if (usbDevice instanceof UsbLight) {
                lights.add((Light) usbDevice);
            } else {
                LOGGER.error("An USB device matched a registered usb light specification, but such specification was not a UsbLight [{}]. The spec class has to extend UsbLight.", usbDevice.getClass().getSimpleName());
                throw new NonUsbLightFoundException();
            }
        }
        return lights;
    }
}
