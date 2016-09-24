package com.rastadrian.jblinky.core.usb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.usb4java.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An USB Communication Handle implementation using the LibUSB library.
 *
 * @author Adrian Pena
 */
public class LibUsbHandle implements UsbCommunicationHandle {
    private static final Logger LOGGER = LoggerFactory.getLogger(LibUsbHandle.class);

    private Context context;
    private Map<UsbDevice, DeviceHandle> libUsbHandles;

    public List<UsbDevice> getConnectedUsbLights(List<DeviceRegister> deviceRegisters) {
        if (context == null) {
            LOGGER.debug("LibUsb not initialized, creating new context.");
            initialize();
        }
        List<UsbDevice> foundDevices = new ArrayList<>();
        DeviceList usbDevices = getDeviceList();

        if (usbDevices == null || usbDevices.getSize() == 0) {
            LOGGER.warn("No connected USB lights were found.");
            return foundDevices;
        }
        LOGGER.info("USB devices found, searching for lights.");
        try {
            for (Device usbDevice : usbDevices) {
                DeviceDescriptor descriptor = getDeviceDescriptor(usbDevice);
                for (DeviceRegister registeredDevice : deviceRegisters) {
                    if (registeredDevice.matchesDevice(descriptor.idVendor(), descriptor.idProduct())) {
                        LOGGER.info("Light found! [{}]", descriptor.getClass().getSimpleName());
                        foundDevices.add(createDevice(usbDevice, registeredDevice));
                    }
                }
            }
        } finally {
            LibUsb.freeDeviceList(usbDevices, true);
        }
        return foundDevices;
    }

    public void communicateWithDevice(UsbDevice device, byte[] message) {
        DeviceHandle handle = libUsbHandles.get(device);
        ByteBuffer messageBuffer = ByteBuffer.allocateDirect(8);
        messageBuffer.put(message);
        int result = LibUsb.controlTransfer(handle, device.getRequestType(), device.getRequest(), device.getValue(), device.getIndex(), messageBuffer, device.getTimeout());
        if (result < 0) {
            LibUsbException libUsbException = new LibUsbException("Control transfer failed", result);
            LOGGER.error("Unable to communicate with UsbDevice through libUsb", libUsbException);
            throw libUsbException;
        }
    }

    public void disconnect(UsbDevice device) {
        DeviceHandle handleToRemove = libUsbHandles.remove(device);
        LibUsb.close(handleToRemove);

        if (libUsbHandles.isEmpty()) {
            disconnect();
        }
    }

    private void disconnect() {
        LOGGER.debug("Disconnecting LibUsb, destroying context.");
        LibUsb.exit(context);
        context = null;
    }

    private UsbDevice createDevice(Device usbDevice, DeviceRegister deviceRegister) {
        UsbDevice device = null;
        try {
            LOGGER.debug("Creating new Usb device form spec [{}]", deviceRegister.getClass().getSimpleName());
            device = deviceRegister.getDeviceClass().newInstance();
            libUsbHandles.put(device, openDevice(usbDevice));
            device.setHandle(this);
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("Unable to create instance from DeviceRegister [{}], this specification class requires an empty public constructor and to be publicly accessible.", deviceRegister.getClass().getSimpleName());
        }
        return device;
    }

    /**
     * Opens the given device, providing a handle to communicate with it.
     *
     * @param device the usb device to open.
     * @return the handle to communicate with the given device.
     */
    private DeviceHandle openDevice(Device device) {
        DeviceHandle handle = new DeviceHandle();
        int result = LibUsb.open(device, handle);
        if (result != LibUsb.SUCCESS) {
            LibUsbException libUsbException = new LibUsbException("Unable to open USB light", result);
            LOGGER.error("Unable to open a device through LibUsb.", libUsbException);
            throw libUsbException;
        }
        return handle;
    }

    /**
     * Retrieves the list of devices.
     *
     * @return the list of connected usb devices.
     */
    private DeviceList getDeviceList() {
        DeviceList list = new DeviceList();
        int result = LibUsb.getDeviceList(null, list);
        if (result < 0) {
            LibUsbException libUsbException = new LibUsbException("Unable to get device list", result);
            LOGGER.error("Unable to retrieve connected usb device list through LibUsb.", libUsbException);
            throw libUsbException;
        }
        return list;
    }

    /**
     * Retrieves the device descriptor from the given device. The descriptor provides information
     * from the device, such as the vendor and product ids.
     *
     * @param device the device to get the descriptor for.
     * @return the device's descriptor.
     */
    private DeviceDescriptor getDeviceDescriptor(Device device) {
        DeviceDescriptor descriptor = new DeviceDescriptor();
        int result = LibUsb.getDeviceDescriptor(device, descriptor);
        if (result != LibUsb.SUCCESS) {
            LibUsbException libUsbException = new LibUsbException("Unable to read device descriptor", result);
            LOGGER.error("Unable to retrieve device descriptor through LibUsb.", libUsbException);
            throw libUsbException;
        }
        return descriptor;
    }

    private void initialize() {
        context = new Context();
        libUsbHandles = new HashMap<>();
        int result = LibUsb.init(context);
        if (result != LibUsb.SUCCESS) {
            LibUsbException libUsbException = new LibUsbException("Unable to initialize LibUsb.", result);
            LOGGER.error("Unable to initialize LibUsb", libUsbException);
            throw libUsbException;
        }
    }
}
