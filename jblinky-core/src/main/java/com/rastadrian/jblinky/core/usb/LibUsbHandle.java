package com.rastadrian.jblinky.core.usb;

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

    private Context context;
    private Map<UsbDevice, DeviceHandle> libUsbHandles;

    public List<UsbDevice> getConnectedUsbLights(List<DeviceRegister> deviceRegisters) {
        if (context == null) {
            initialize();
        }
        List<UsbDevice> foundDevices = new ArrayList<UsbDevice>();
        DeviceList usbDevices = getDeviceList();
        try {
            for (Device usbDevice : usbDevices) {
                DeviceDescriptor descriptor = getDeviceDescriptor(usbDevice);
                for (DeviceRegister registeredDevice : deviceRegisters) {
                    if (registeredDevice.matchesDevice(descriptor.idVendor(), descriptor.idProduct())) {
                        foundDevices.add(createDevice(usbDevice, registeredDevice));
                    }
                }
            }
        } finally {
            LibUsb.freeDeviceList(usbDevices, true);
        }
        if(foundDevices.isEmpty()) {
            throw new NoUsbDevicesFoundException();
        }
        return foundDevices;
    }

    public void communicateWithDevice(UsbDevice device, byte[] message) {
        DeviceHandle handle = libUsbHandles.get(device);
        ByteBuffer messageBuffer = ByteBuffer.allocateDirect(8);
        messageBuffer.put(message);
        int result = LibUsb.controlTransfer(handle, device.getRequestType(), device.getRequest(), device.getValue(), device.getIndex(), messageBuffer, device.getTimeout());
        if (result < 0) {
            throw new LibUsbException("Control transfer failed", result);
        }
    }

    public void disconnect(UsbDevice device) {
        DeviceHandle handleToRemove = libUsbHandles.remove(device);
        LibUsb.close(handleToRemove);

        if(libUsbHandles.isEmpty()) {
            disconnect();
        }
    }

    private void disconnect() {
        LibUsb.exit(context);
        context = null;
    }

    private UsbDevice createDevice(Device usbDevice, DeviceRegister deviceRegister) {
        UsbDevice device = null;
        try {
            device = deviceRegister.getDeviceClass().newInstance();
            libUsbHandles.put(device, openDevice(usbDevice));
            device.setHandle(this);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
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
            throw new LibUsbException("Unable to open USB light", result);
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
            throw new LibUsbException("Unable to get device list", result);
        }
        return list;
    }

    /**
     * Retrieves the device descriptor from the given device. The descriptor provides information
     * from the device, such as the vendor and product ids.
     * @param device the device to get the descriptor for.
     * @return the device's descriptor.
     */
    private DeviceDescriptor getDeviceDescriptor(Device device) {
        DeviceDescriptor descriptor = new DeviceDescriptor();
        int result = LibUsb.getDeviceDescriptor(device, descriptor);
        if (result != LibUsb.SUCCESS) {
            throw new LibUsbException("Unable to read device descriptor", result);
        }
        return descriptor;
    }

    private void initialize() {
        context = new Context();
        libUsbHandles = new HashMap<UsbDevice, DeviceHandle>();
        int result = LibUsb.init(context);
        if (result != LibUsb.SUCCESS) {
            throw new LibUsbException("Unable to initialize libusb.", result);
        }
    }
}
