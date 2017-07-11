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
public class WebMailNotifierLightTest {

    private WebMailNotifier webMailNotifier;
    private UsbCommunicationsHandler usbCommunicationsHandler;

    @Before
    public void setUp() throws Exception {
        usbCommunicationsHandler = mock(UsbCommunicationsHandler.class);
        webMailNotifier = new WebMailNotifier();
        webMailNotifier.setHandler(usbCommunicationsHandler);
    }

    @Test
    public void getId() throws Exception {
        //When
        String id = webMailNotifier.getId();

        //Then
        assertThat(id).isEqualTo(WebMailNotifier.class.getSimpleName());
    }

    @Test
    public void success() throws Exception {
        //Given
        byte[] offMessage = new byte[]{0x00, (byte) 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x05};

        //When
        webMailNotifier.success();

        //Then
        verify(usbCommunicationsHandler, times(1)).communicateWithLight(eq(webMailNotifier), eq(offMessage));

    }

    @Test
    public void successTwoTimes() throws Exception {
        //Given
        byte[] offMessage = new byte[]{0x00, (byte) 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x05};
        int expectedInvocations = 1;

        //When
        webMailNotifier.success();
        webMailNotifier.success();

        //Then
        verify(usbCommunicationsHandler, times(expectedInvocations)).communicateWithLight(eq(webMailNotifier), eq(offMessage));

    }

    @Test
    public void failure() throws Exception {
        //Given
        byte[] offMessage = new byte[]{(byte) 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x05};

        //When
        webMailNotifier.failure();

        //Then
        verify(usbCommunicationsHandler, times(1)).communicateWithLight(eq(webMailNotifier), eq(offMessage));
    }

    @Test
    public void inProgress() throws Exception {
        //Given
        byte[] offMessage = new byte[]{0x00, 0x00, (byte) 0xFF, 0x00, 0x00, 0x00, 0x00, 0x05};

        //When
        webMailNotifier.inProgress();

        //Then
        verify(usbCommunicationsHandler, times(1)).communicateWithLight(eq(webMailNotifier), eq(offMessage));
    }

    @Test
    public void warning() throws Exception {
        //Given
        byte[] offMessage = new byte[]{(byte) 0xFF, 0x2A, 0x00, 0x00, 0x00, 0x00, 0x00, 0x05};

        //When
        webMailNotifier.warning();

        //Then
        verify(usbCommunicationsHandler, times(1)).communicateWithLight(eq(webMailNotifier), eq(offMessage));
    }

    @Test
    public void off() throws Exception {
        //Given
        byte[] offMessage = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x05};

        //When
        webMailNotifier.off();

        //Then
        verify(usbCommunicationsHandler, times(1)).communicateWithLight(eq(webMailNotifier), eq(offMessage));
    }

    @Test
    public void getRequestType() throws Exception {
        //When
        byte requestType = webMailNotifier.getRequestType();

        //Then
        assertThat(requestType).isEqualTo((byte) 0x21);
    }

    @Test
    public void getRequest() throws Exception {
        //When
        byte request = webMailNotifier.getRequest();

        //Then
        assertThat(request).isEqualTo((byte) 0x09);
    }

    @Test
    public void getValue() throws Exception {
        //Given
        short expectedValue = (3 << 8) | 1;

        //When
        short value = webMailNotifier.getValue();

        //Then
        assertThat(value).isEqualTo(expectedValue);
    }

    @Test
    public void getIndex() throws Exception {
        //When
        short index = webMailNotifier.getIndex();

        //Then
        assertThat(index).isEqualTo((short) 0);
    }

    @Test
    public void getTimeout() throws Exception {
        //When
        long timeout = webMailNotifier.getTimeout();

        //Then
        assertThat(timeout).isEqualTo(0);
    }

}