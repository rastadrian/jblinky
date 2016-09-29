package com.rastadrian.jblinky.core.usb.light;

import com.rastadrian.jblinky.core.probe.Probe;
import com.rastadrian.jblinky.core.usb.UsbCommunicationHandle;
import com.rastadrian.jblinky.core.usb.UsbDevice;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created on 9/24/16.
 *
 * @author Adrian Pena
 */
public class LightFactoryTest {

    private UsbCommunicationHandle handle;
    private LightFactory lightFactory;

    @Before
    public void setUp() throws Exception {
        handle = mock(UsbCommunicationHandle.class);
    }

    @Test
    public void detectLights() throws Exception {
        List<Light> foundLights;
        UsbLightDevice lightDevice;
        Probe[] probes;
        given:
        {
            probes = new Probe[]{};
            lightFactory = new LightFactory(null, handle, probes);
            lightDevice = new UsbLightDevice();
            List<UsbDevice> deviceList = new ArrayList<UsbDevice>();
            deviceList.add(lightDevice);
            when(handle.getConnectedUsbLights(any(List.class))).thenReturn(deviceList);
        }
        when:
        {
            foundLights = lightFactory.detectLights();
        }
        then:
        {
            assertThat(foundLights).contains(lightDevice);
            assertThat(foundLights.get(0)).isEqualTo(lightDevice);
            assertThat(((UsbLight) foundLights.get(0)).getProbes()).contains(probes);
        }
    }


    @Test(expected = NonUsbLightFoundException.class)
    public void detectLights_withNonLightRegistries() throws Exception {
        given:
        {
            lightFactory = new LightFactory(null, handle, null);
            List<UsbDevice> deviceList = new ArrayList<UsbDevice>();
            deviceList.add(new NonUsbLightDevice());
            when(handle.getConnectedUsbLights(any(List.class))).thenReturn(deviceList);
        }
        when:
        {
            lightFactory.detectLights();
        }
    }

    @Test(expected = NoUsbDevicesFoundException.class)
    public void detectLights_withNoDevicesFound() throws Exception {
        given:
        {
            lightFactory = new LightFactory(null, handle, null);
            when(handle.getConnectedUsbLights(any(List.class))).thenReturn(new ArrayList<UsbDevice>());
        }
        when:
        {
            lightFactory.detectLights();
        }

    }

    private class UsbLightDevice extends UsbLight {

        public void success() {

        }

        public void failure() {

        }

        public void inProgress() {

        }

        public void warning() {

        }

        public void off() {

        }

        protected byte getRequestType() {
            return 0;
        }

        protected byte getRequest() {
            return 0;
        }

        protected short getValue() {
            return 0;
        }

        protected short getIndex() {
            return 0;
        }

        protected long getTimeout() {
            return 0;
        }
    }

    private class NonUsbLightDevice extends UsbDevice {

        protected byte getRequestType() {
            return 0;
        }

        protected byte getRequest() {
            return 0;
        }

        protected short getValue() {
            return 0;
        }

        protected short getIndex() {
            return 0;
        }

        protected long getTimeout() {
            return 0;
        }
    }
}