package com.rastadrian.jblinky.devices.usb;

import com.rastadrian.jblinky.core.light.LightSource;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created on 7/8/17.
 *
 * @author Adrian Pena
 */
public class UsbLightTest {

    private UsbLight usbLight;
    private UsbCommunicationsHandler usbCommunicationsHandler;

    @Before
    public void setUp() throws Exception {
        usbLight = new UsbLight() {
            @Override
            public byte getRequestType() {
                return 0;
            }

            @Override
            public byte getRequest() {
                return 0;
            }

            @Override
            public short getValue() {
                return 0;
            }

            @Override
            public short getIndex() {
                return 0;
            }

            @Override
            public long getTimeout() {
                return 0;
            }

            @Override
            public String getId() {
                return null;
            }
        };
        usbCommunicationsHandler = mock(UsbCommunicationsHandler.class);
        usbLight.setHandler(usbCommunicationsHandler);
    }

    @Test
    public void disconnect() throws Exception {
        //When
        usbLight.disconnect();

        //Then
        verify(usbCommunicationsHandler, times(1)).disconnectLight(eq(usbLight));
    }

    @Test
    public void getSource() throws Exception {
        //Given
        UsbLightSource lightSource = mock(UsbLightSource.class);
        usbLight.setSource(lightSource);

        //When
        LightSource source = usbLight.getSource();

        //Then
        assertThat(source).isEqualTo(lightSource);
    }

    @Test
    public void communicate() throws Exception {
        //Given
        byte[] message = new byte[10];

        //When
        usbLight.communicate(message);

        //Then
        verify(usbCommunicationsHandler, times(1)).communicateWithLight(eq(usbLight), eq(message));
    }
}