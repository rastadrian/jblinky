package com.rastadrian.jblinky.devices.usb.libusb;

import com.rastadrian.jblinky.devices.usb.UsbCommunicationsHandler;
import com.rastadrian.jblinky.devices.usb.UsbLight;
import com.rastadrian.jblinky.devices.usb.UsbRegistry;
import lombok.extern.slf4j.Slf4j;
import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A USB communication handler that uses <a href="http://usb4java.org/quickstart/libusb.html">usb4java</a> as an implementation
 * to read and write to usb devices.
 * <p>
 * Created on 7/8/17.
 *
 * @author Adrian Pena
 */
@Slf4j
public class LibUsbCommunicationHandler implements UsbCommunicationsHandler {

    private Context context;
    private Map<UsbLight, DeviceHandle> usbDeviceHandles;

    @Override
    public Collection<UsbLight> getLights(Collection<UsbRegistry> registries) {
        if (context == null) {
            LOGGER.debug("LibUsb not initialized, creating new context.");
            initialize();
        }
        Collection<UsbLight> foundDevices = new ArrayList<>();
        DeviceList usbDevices = getDeviceList();

        if (usbDevices == null || usbDevices.getSize() == 0) {
            LOGGER.warn("No connected USB devices were found.");
            return foundDevices;
        }
        LOGGER.info("USB devices found!, searching for devices that match the USB light specifications.");
        try {
            for (Device usbDevice : usbDevices) {
                DeviceDescriptor descriptor = getDeviceDescriptor(usbDevice);
                for (UsbRegistry registry : registries) {
                    if (registry.matchesDevice(descriptor.idVendor(), descriptor.idProduct())) {
                        LOGGER.info("USB Light found! [{}]", registry.getDeviceClass().getSimpleName());
                        DeviceHandle deviceHandle = openDevice(usbDevice);
                        UsbLight usbLight = createUsbLight(registry);
                        foundDevices.add(usbLight);
                        usbDeviceHandles.put(usbLight, deviceHandle);
                        usbLight.setHandler(this);
                    }
                }
            }
        } finally {
            LibUsb.freeDeviceList(usbDevices, true);
        }
        return foundDevices;
    }

    @Override
    public void communicateWithLight(UsbLight light, byte[] message) {
        ByteBuffer messageBuffer = ByteBuffer.allocateDirect(8);
        messageBuffer.put(message);
        int result = LibUsb.controlTransfer(usbDeviceHandles.get(light), light.getRequestType(), light.getRequest(), light.getValue(), light.getIndex(), messageBuffer, light.getTimeout());
        if (result < 0) {
            LibUsbException libUsbException = new LibUsbException("Control transfer failed", result);
            LOGGER.error("Unable to communicate with UsbDevice through libUsb", libUsbException);
            throw libUsbException;
        }
    }

    @Override
    public void disconnectLight(UsbLight light) {
        LibUsb.close(usbDeviceHandles.get(light));
    }

    private void initialize() {
        context = new Context();
        usbDeviceHandles = new HashMap<>();

        int result = LibUsb.init(context);
        if (result != LibUsb.SUCCESS) {
            LibUsbException libUsbException = new LibUsbException("Unable to initialize LibUsb.", result);
            LOGGER.error("Unable to initialize LibUsb", libUsbException);
            throw libUsbException;
        }
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

    private UsbLight createUsbLight(UsbRegistry usbRegistry) {
        UsbLight light = null;
        try {
            LOGGER.debug("Creating new USB light from [{}] specification.", usbRegistry.getDeviceClass().getSimpleName());
            light = usbRegistry.getDeviceClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("Unable to create instance from UsbRegistry [{}], this specification class requires an empty public constructor and to be publicly accessible.", usbRegistry.getClass().getSimpleName());
        }
        return light;
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
}
