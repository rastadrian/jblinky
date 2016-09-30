package com.rastadrian.jblinky.core.usb.light;

import com.rastadrian.jblinky.core.probe.Probe;
import com.rastadrian.jblinky.core.usb.UsbCommunicationHandle;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created on 9/29/16.
 *
 * @author Adrian Pena
 */
public class UsbLightTest {
    private UsbLight usbLight;

    @Before
    public void setUp() throws Exception {
        usbLight = spy(new TestUsbLight());
    }

    @Test
    public void disconnect() throws Exception {
        when:
        {
            usbLight.setHandle(mock(UsbCommunicationHandle.class));
            usbLight.disconnect();
        }
        then:
        {
            verify(usbLight, times(1)).off();
        }
    }

    @Test
    public void explicitProbeSetting() throws Exception {
        Probe[] probes;
        Probe[] receivedProbes;
        given:
        {
            probes = new Probe[]{};
            usbLight.setHandle(mock(UsbCommunicationHandle.class));
            usbLight.setProbes(probes);
        }
        when:
        {
            receivedProbes = usbLight.getProbes();
        }
        then:
        {
            assertThat(receivedProbes).isEqualTo(probes);
        }

    }

    private class TestUsbLight extends UsbLight {

        @Override
        public void success() {

        }

        @Override
        public void failure() {

        }

        @Override
        public void inProgress() {

        }

        @Override
        public void warning() {

        }

        @Override
        public void off() {

        }

        @Override
        protected byte getRequestType() {
            return 0;
        }

        @Override
        protected byte getRequest() {
            return 0;
        }

        @Override
        protected short getValue() {
            return 0;
        }

        @Override
        protected short getIndex() {
            return 0;
        }

        @Override
        protected long getTimeout() {
            return 0;
        }
    }
}
