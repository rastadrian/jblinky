package com.rastadrian.jblinky.devices;

import com.rastadrian.jblinky.core.usb.UsbCommunicationHandle;
import com.rastadrian.jblinky.core.usb.UsbDevice;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by adrian on 9/27/16.
 */
public class WebMailNotifierTest {
    private WebMailNotifier light;
    private UsbCommunicationHandle handle;

    @Before
    public void setUp() throws Exception {
        handle = mock(UsbCommunicationHandle.class);
        light = new WebMailNotifier();
        light.setHandle(handle);
    }

    @Test
    public void initialization() throws Exception {
        //setting the handle on setup should have communicated three times already.
        verify(handle, times(3)).communicateWithDevice(any(UsbDevice.class), Matchers.any());
    }

    @Test
    public void success() throws Exception {
        byte[] expectedMessage;
        given:
        {
            expectedMessage = getMessageForColor(new byte[]{0x00, (byte) 0xFF, 0x00});
        }
        when:
        {
            light.success();
        }
        then:
        {
            verify(handle, times(1)).communicateWithDevice(any(UsbDevice.class), eq(expectedMessage));
        }
    }

    @Test
    public void failure() throws Exception {
        byte[] expectedMessage;
        given:
        {
            expectedMessage = getMessageForColor(new byte[]{(byte) 0xFF, 0x00, 0x00});
        }
        when:
        {
            light.failure();
        }
        then:
        {
            verify(handle, times(1)).communicateWithDevice(any(UsbDevice.class), eq(expectedMessage));
        }
    }

    @Test
    public void inProgress() throws Exception {
        byte[] expectedMessage;
        given:
        {
            expectedMessage = getMessageForColor(new byte[]{0x00, 0x00, (byte) 0xFF});
        }
        when:
        {
            light.inProgress();
        }
        then:
        {
            verify(handle, times(1)).communicateWithDevice(any(UsbDevice.class), eq(expectedMessage));
        }
    }

    @Test
    public void warning() throws Exception {
        byte[] expectedMessage;
        given:
        {
            expectedMessage = getMessageForColor(new byte[]{(byte) 0xFF, 0x2A, 0x00});
        }
        when:
        {
            light.warning();
        }
        then:
        {
            verify(handle, times(1)).communicateWithDevice(any(UsbDevice.class), eq(expectedMessage));
        }
    }

    @Test
    public void off() throws Exception {
        byte[] expectedMessage;
        given:
        {
            expectedMessage = getMessageForColor(new byte[]{0x00, 0x00, 0x00});
        }
        when:
        {
            light.off();
        }
        then:
        {
            verify(handle, times(1)).communicateWithDevice(any(UsbDevice.class), eq(expectedMessage));
        }
    }

    @Test
    public void getRequestType() throws Exception {
        byte received;
        when: {
            received = light.getRequestType();
        }
        then: {
            assertThat(received).isEqualTo((byte) 0x21);
        }
    }

    @Test
    public void getRequest() throws Exception {
        byte received;
        when: {
            received = light.getRequest();
        }
        then: {
            assertThat(received).isEqualTo((byte) 0x09);
        }
    }

    @Test
    public void getValue() throws Exception {
        short received;
        when: {
            received = light.getValue();
        }
        then: {
            assertThat(received).isEqualTo((short) ((3 << 8) | 1));
        }
    }

    @Test
    public void getIndex() throws Exception {
        short received;
        when: {
            received = light.getIndex();
        }
        then: {
            assertThat(received).isEqualTo((short) 0);
        }
    }

    @Test
    public void getTimeout() throws Exception {
        long received;
        when: {
            received = light.getTimeout();
        }
        then: {
            assertThat(received).isEqualTo(0);
        }
    }

    private byte[] getMessageForColor(byte[] color) {
        byte[] suffix = new byte[]{0x00, 0x00, 0x00, 0x00, 0x05};
        return ArrayUtils.addAll(color, suffix);
    }
}