package com.rastadrian.jblinky.core.usb.light;

import com.rastadrian.jblinky.core.probe.Probe;
import com.rastadrian.jblinky.core.usb.DeviceRegister;
import com.rastadrian.jblinky.core.usb.UsbCommunicationHandle;
import com.rastadrian.jblinky.core.usb.UsbDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * A USB Light device factory. It is responsible to searching and connecting USB light devices.
 *
 * @author Adrian Pena
 */
public class LightFactory {
    private final List<DeviceRegister> deviceRegisters;
    private final UsbCommunicationHandle handle;
    private final Probe[] probes;

    public LightFactory(List<DeviceRegister> deviceRegisters, UsbCommunicationHandle handle, Probe[] probes) {
        this.deviceRegisters = deviceRegisters;
        this.handle = handle;
        this.probes = probes;
    }

    /**
     * It will search and connect to USB Light devices on the computer based on the
     * factory's available usb device specification registries.
     *
     * @return the list of found lights.
     */
    public List<Light> detectLights() {
        List<Light> lights = new ArrayList<Light>();
        List<UsbDevice> connectedUsbLights = handle.getConnectedUsbLights(deviceRegisters);
        for (UsbDevice connectedUsbLight : connectedUsbLights) {
            if(connectedUsbLight instanceof UsbLight) {
                ((UsbLight) connectedUsbLight).setProbes(probes);
                lights.add((Light) connectedUsbLight);
            } else {
                throw new NonUsbLightFoundException();
            }
        }
        return lights;
    }
}
