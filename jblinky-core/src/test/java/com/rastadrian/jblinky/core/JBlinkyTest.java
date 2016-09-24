package com.rastadrian.jblinky.core;

import com.rastadrian.jblinky.core.probe.Probe;
import com.rastadrian.jblinky.core.usb.UsbCommunicationHandle;
import com.rastadrian.jblinky.core.usb.UsbDevice;
import com.rastadrian.jblinky.core.usb.UsbRegistry;
import com.rastadrian.jblinky.core.usb.light.Light;
import com.rastadrian.jblinky.core.usb.light.UsbLight;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created on 9/24/16.
 *
 * @author Adrian Pena
 */
public class JBlinkyTest {

    private JBlinky jBlinky;
    private UsbCommunicationHandle handle;

    @Before
    public void setUp() throws Exception {
        handle = mock(UsbCommunicationHandle.class);
    }

    @Test
    public void getLight() throws Exception {
        TestLightDevice firstLight;
        TestLightDevice secondLight;
        Light light;
        given: {
            firstLight = new TestLightDevice();
            secondLight = new TestLightDevice();
            mockHandleConnectedUsbLights(firstLight, secondLight);
            List<Class<? extends UsbLight>> lightSpecs = new ArrayList<Class<? extends UsbLight>>();
            lightSpecs.add(TestLightDevice.class);
            jBlinky = new JBlinky(handle, lightSpecs, new Probe[]{});
        }
        when: {
            light = jBlinky.getLight();
        }
        then: {
            assertThat(light).isEqualTo(firstLight);
        }
    }

    @Test
    public void getLights() throws Exception {
        TestLightDevice firstLight;
        TestLightDevice secondLight;
        List<Light> lights;
        given: {
            firstLight = new TestLightDevice();
            secondLight = new TestLightDevice();
            mockHandleConnectedUsbLights(firstLight, secondLight);
            jBlinky = new JBlinky(handle);
        }
        when: {
            lights = jBlinky.getLights();
        }
        then: {
            assertThat(lights).contains(firstLight, secondLight);
        }
    }

    @Test
    public void close() throws Exception {
        TestLightDevice firstLight;
        TestLightDevice secondLight;
        given: {
            firstLight = spy(new TestLightDevice());
            firstLight.setHandle(handle);
            secondLight = new TestLightDevice();
            secondLight.setHandle(handle);
            mockHandleConnectedUsbLights(firstLight, secondLight);
            jBlinky = new JBlinky(handle);
        }
        when: {
            assertThat(jBlinky.getLights()).contains(firstLight, secondLight);
            jBlinky.close();
        }
        then: {
            assertThat(jBlinky.getLights()).isEmpty();
            verify(firstLight, times(1)).disconnect();
        }
    }

    private void mockHandleConnectedUsbLights(UsbDevice... devices) {
        List<UsbDevice> usbList = new ArrayList<UsbDevice>();
        for (UsbDevice device : devices) {
            usbList.add(device);
        }
        handle = mock(UsbCommunicationHandle.class);
        when(handle.getConnectedUsbLights(any(List.class))).thenReturn(usbList);
    }

    @UsbRegistry(vendorId = 0, productId = 1)
    private static class TestLightDevice extends UsbLight {

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
}