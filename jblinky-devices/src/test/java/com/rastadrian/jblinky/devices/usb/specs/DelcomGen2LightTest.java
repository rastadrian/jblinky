package com.rastadrian.jblinky.devices.usb.specs;

import com.rastadrian.jblinky.devices.usb.UsbCommunicationsHandler;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created on 7/9/17.
 *
 * @author Adrian Pena
 */
public class DelcomGen2LightTest {

    private DelcomGen2Light delcomGen2Light;
    private UsbCommunicationsHandler usbCommunicationsHandler;

    @Before
    public void setUp() throws Exception {
        usbCommunicationsHandler = mock(UsbCommunicationsHandler.class);
        delcomGen2Light = new DelcomGen2Light();
        delcomGen2Light.setHandler(usbCommunicationsHandler);
    }

    @Test
    public void getId() throws Exception {
        //When
        String id = delcomGen2Light.getId();

        //Then
        assertThat(id).isEqualTo(DelcomGen2Light.class.getSimpleName());
    }

    @Test
    public void success() throws Exception {
        //Given
        byte[] offMessage = new byte[]{(byte) 0x65, (byte) 0x0C, (byte) 0xFF, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};

        //When
        delcomGen2Light.success();

        //Then
        verify(usbCommunicationsHandler, times(1)).communicateWithLight(eq(delcomGen2Light), eq(offMessage));
    }

    @Test
    public void successTwoTimes() throws Exception {
        //Given
        byte[] offMessage = new byte[]{(byte) 0x65, (byte) 0x0C, (byte) 0xFF, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        int expectedInvocations = 1;

        //When
        delcomGen2Light.success();
        delcomGen2Light.success();

        //Then
        verify(usbCommunicationsHandler, times(expectedInvocations)).communicateWithLight(eq(delcomGen2Light), eq(offMessage));

    }

    @Test
    public void failure() throws Exception {
        //Given
        byte[] offMessage = new byte[]{(byte) 0x65, (byte) 0x0C, (byte) 0xFF, (byte) 0x05, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};

        //When
        delcomGen2Light.failure();

        //Then
        verify(usbCommunicationsHandler, times(1)).communicateWithLight(eq(delcomGen2Light), eq(offMessage));
    }

    @Test
    public void inProgress() throws Exception {
        //Given
        byte[] offMessage = new byte[]{(byte) 0x65, (byte) 0x0C, (byte) 0xFF, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};

        //When
        delcomGen2Light.inProgress();

        //Then
        verify(usbCommunicationsHandler, times(1)).communicateWithLight(eq(delcomGen2Light), eq(offMessage));
    }

    @Test
    public void warning() throws Exception {
        //Given
        byte[] offMessage = new byte[]{(byte) 0x65, (byte) 0x0C, (byte) 0xFF, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};

        //When
        delcomGen2Light.warning();

        //Then
        verify(usbCommunicationsHandler, times(1)).communicateWithLight(eq(delcomGen2Light), eq(offMessage));
    }

    @Test
    public void off() throws Exception {
        //Given
        byte[] offMessage = new byte[]{(byte) 0x65, (byte) 0x0C, (byte) 0xFF, (byte) 0x07, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};

        //When
        delcomGen2Light.off();

        //Then
        verify(usbCommunicationsHandler, times(1)).communicateWithLight(eq(delcomGen2Light), eq(offMessage));
    }

    @Test
    public void getRequestType() throws Exception {
        //When
        byte requestType = delcomGen2Light.getRequestType();

        //Then
        assertThat(requestType).isEqualTo((byte) 0x21);
    }

    @Test
    public void getRequest() throws Exception {
        //When
        byte request = delcomGen2Light.getRequest();

        //Then
        assertThat(request).isEqualTo((byte) 0x09);
    }

    @Test
    public void getValue() throws Exception {
        //When
        short value = delcomGen2Light.getValue();

        //Then
        assertThat(value).isEqualTo((short) 0x0635);
    }

    @Test
    public void getIndex() throws Exception {
        //When
        short index = delcomGen2Light.getIndex();

        //Then
        assertThat(index).isEqualTo((short) 0x000);
    }

    @Test
    public void getTimeout() throws Exception {
        //When
        long timeout = delcomGen2Light.getTimeout();

        //Then
        assertThat(timeout).isEqualTo(0);
    }

}