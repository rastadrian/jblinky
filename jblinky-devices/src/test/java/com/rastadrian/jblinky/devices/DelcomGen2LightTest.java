package com.rastadrian.jblinky.devices;

import com.rastadrian.jblinky.core.usb.UsbCommunicationHandle;
import com.rastadrian.jblinky.core.usb.UsbDevice;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by adrian on 9/27/16.
 */
public class DelcomGen2LightTest {
    private DelcomGen2Light light;
    private UsbCommunicationHandle handle;

    @Before
    public void setUp() throws Exception {
        handle = mock(UsbCommunicationHandle.class);
        light = new DelcomGen2Light();
        light.setHandle(handle);
    }

    @Test
    public void success() throws Exception {
        byte[] message = getMessageForColor((byte) 0x06);
        when: {
            light.success();
        }
        then: {
            verify(handle, times(1)).communicateWithDevice(any(UsbDevice.class), eq(message));
        }
    }

    @Test
    public void failure() throws Exception {
        byte[] message = getMessageForColor((byte) 0x07);
        when: {
            light.failure();
        }
        then: {
            verify(handle, times(1)).communicateWithDevice(any(UsbDevice.class), eq(message));
        }
    }

    @Test
    public void inProgress() throws Exception {
        byte[] message = getMessageForColor((byte) 0x03);
        when: {
            light.inProgress();
        }
        then: {
            verify(handle, times(1)).communicateWithDevice(any(UsbDevice.class), eq(message));
        }
    }

    @Test
    public void warning() throws Exception {
        byte[] message = getMessageForColor((byte) 0x04);
        when: {
            light.warning();
        }
        then: {
            verify(handle, times(1)).communicateWithDevice(any(UsbDevice.class), eq(message));
        }
    }

    @Test
    public void off() throws Exception {
        byte[] message = getMessageForColor((byte) 0x07);
        when: {
            light.off();
        }
        then: {
            verify(handle, times(1)).communicateWithDevice(any(UsbDevice.class), eq(message));
        }
    }

    @Test
    public void getRequestType() throws Exception {
        byte requestType;
        when: {
            requestType = light.getRequestType();
        }
        then: {
            assertThat(requestType).isEqualTo((byte) 0x21);
        }
    }

    @Test
    public void getRequest() throws Exception {
        byte request;
        when: {
            request = light.getRequest();
        }
        then: {
            assertThat(request).isEqualTo((byte) 0x09);
        }
    }

    @Test
    public void getValue() throws Exception {
        short value;
        when: {
            value = light.getValue();
        }
        then: {
            assertThat(value).isEqualTo((short) 0x0635);
        }
    }

    @Test
    public void getIndex() throws Exception {
        short index;
        when: {
            index = light.getIndex();
        }
        then: {
            assertThat(index).isEqualTo((short) 0x000);
        }
    }

    @Test
    public void getTimeout() throws Exception {
        long timeout;
        when: {
            timeout = light.getTimeout();
        }
        then: {
            assertThat(timeout).isEqualTo(0);
        }
    }

    private byte[] getMessageForColor(byte color) {
        return new byte[]{(byte) 0x65, (byte) 0x0C, (byte) 0xFF, color, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
    }
}