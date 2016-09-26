package com.rastadrian.jblinky.core;

import com.rastadrian.jblinky.core.probe.Probe;
import com.rastadrian.jblinky.core.usb.DeviceRegister;
import com.rastadrian.jblinky.core.usb.LibUsbHandle;
import com.rastadrian.jblinky.core.usb.UsbCommunicationHandle;
import com.rastadrian.jblinky.core.usb.UsbRegistry;
import com.rastadrian.jblinky.core.usb.light.Light;
import com.rastadrian.jblinky.core.usb.light.LightFactory;
import com.rastadrian.jblinky.core.usb.light.UsbLight;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The jBlinky main component, it initializes, searches, connects and provides lights.
 *
 * @author Adrian Pena
 */
public class JBlinky {
    private static final Logger LOGGER = LoggerFactory.getLogger(JBlinky.class);

    /**
     * The default device package to search specs from. In order for this package search to work,
     * the project requires the jblinky-devices module.
     */
    private static final String DEFAULT_DEVICES_PACKAGE = "com.rastadrian.jblinky.devices";

    private List<Light> lights;

    /**
     * Creates a new jBlinky, it will scan the default device package for usb device specifications and will search for
     * connected lights.
     * <p>
     * If usb lights are found, it will assign them the given probes.
     *
     * @param probes the list of probes that lights can verify.
     */
    public JBlinky(Probe... probes) {
        this(new LibUsbHandle(), DEFAULT_DEVICES_PACKAGE, probes);
    }

    /**
     * Creates a new jBlinky, it will scan the default device package for usb device specifications and will search for
     * connected lights.
     * <p>
     * If usb lights are found, it will assign them the given probes.
     *
     * @param handle an USB communication handle protocol.
     * @param probes the list of probes that lights can verify.
     */
    public JBlinky(UsbCommunicationHandle handle, Probe... probes) {
        this(handle, DEFAULT_DEVICES_PACKAGE, probes);
    }

    /**
     * Creates a new jBlinky, it will scan the provided package for usb device specifications and will search for connected lights.
     * <p>
     * The provided probes will be assigned to the found lights.
     *
     * @param packageToScan the package to scan for usb device specifications.
     * @param probes        the list of probes that lights can verify.
     */
    public JBlinky(String packageToScan, Probe[] probes) {
        List<Class<? extends UsbLight>> lightDevices = scanDevicePackage(packageToScan);
        List<DeviceRegister> deviceRegisters = registerDevices(lightDevices);
        initialize(new LibUsbHandle(), deviceRegisters, probes);
    }

    /**
     * Creates a new jBlinky, it will scan the provided package for usb device specifications and will search for connected lights.
     * <p>
     * The provided probes will be assigned to the found lights.
     *
     * @param handle        an USB communication handle protocol
     * @param packageToScan the package to scan for usb device specifications.
     * @param probes        the list of probes that lights can verify.
     */
    public JBlinky(UsbCommunicationHandle handle, String packageToScan, Probe[] probes) {
        List<Class<? extends UsbLight>> lightDevices = scanDevicePackage(packageToScan);
        List<DeviceRegister> deviceRegisters = registerDevices(lightDevices);
        initialize(handle, deviceRegisters, probes);
    }

    /**
     * Creates a new jBlinky, it will search for connected lights with the provided usb device specifications.
     * <p>
     * The provided probes will be assigned to the found lights.
     *
     * @param usbLightSpecifications the list of usb light specifications.
     * @param probes                 the list of probes that lights can verify.
     */
    public JBlinky(List<Class<? extends UsbLight>> usbLightSpecifications, Probe[] probes) {
        this(new LibUsbHandle(), usbLightSpecifications, probes);
    }

    /**
     * Creates a new jBlinky, it will search for connected lights with the provided usb device specifications.
     * <p>
     * The provided probes will be assigned to the found lights.
     *
     * @param handle                 a USB communication handle protocol.
     * @param usbLightSpecifications the list of usb light specifications.
     * @param probes                 the list of probes that lights can verify.
     */
    public JBlinky(UsbCommunicationHandle handle, List<Class<? extends UsbLight>> usbLightSpecifications, Probe[] probes) {
        List<DeviceRegister> deviceRegisters = registerDevices(usbLightSpecifications);
        initialize(handle, deviceRegisters, probes);
    }

    /**
     * Access for the first available light.
     *
     * @return the first light.
     */
    public Light getLight() {
        return lights.get(0);
    }

    /**
     * Access for the list of available and connected lights.
     *
     * @return the list of available lights.
     */
    public List<Light> getLights() {
        return lights;
    }

    /**
     * Turns off and disconnects from the usb lights.
     */
    public void close() {
        LOGGER.info("Closing and disconnecting jBlinky.");
        for (Light light : lights) {
            light.disconnect();
        }
        lights.clear();
    }

    /**
     * Registers a shutdown hook {@link Runtime#addShutdownHook(Thread)} on the Java Runtime.
     */
    public void registerShutdownHook() {
        LOGGER.info("Registering a Runtime shutdown hook to disconnect jBlinky upon process interruption.");
        Runtime.getRuntime().addShutdownHook(new Thread(new JBlinkyDisconnector(this)));
    }

    private void initialize(UsbCommunicationHandle handle, List<DeviceRegister> deviceRegisters, Probe[] probes) {
        LOGGER.info("Initializing jBlinky with [{}] USB Handle, [{}] USB light specifications and [{}] global probes.", handle.getClass().getSimpleName(), deviceRegisters.size(), probes.length);
        lights = new LightFactory(deviceRegisters, handle, probes).detectLights();
    }

    private List<DeviceRegister> registerDevices(List<Class<? extends UsbLight>> devices) {
        LOGGER.info("Registering [{}] as USB light specifications.", devices);
        List<DeviceRegister> registeredProviders = new ArrayList<DeviceRegister>();
        for (Class<? extends UsbLight> device : devices) {
            UsbRegistry registration = device.getAnnotation(UsbRegistry.class);
            registeredProviders.add(new DeviceRegister(registration.vendorId(), registration.productId(), device));
        }
        return registeredProviders;
    }

    private List<Class<? extends UsbLight>> scanDevicePackage(String packageToScan) {
        LOGGER.info("Scanning package [{}] for USB light specifications.", packageToScan);
        Reflections reflections = new Reflections(packageToScan);
        Set<Class<? extends UsbLight>> usbProviders = reflections.getSubTypesOf(UsbLight.class);
        return new ArrayList<>(usbProviders);
    }

    private class JBlinkyDisconnector implements Runnable {
        private final JBlinky instance;

        private JBlinkyDisconnector(JBlinky instance) {
            this.instance = instance;
        }

        @Override
        public void run() {
            if(instance != null) {
                instance.close();
            }
        }
    }
}
