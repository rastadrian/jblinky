package com.rastadrian.jblinky.core.usb;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created on 9/24/16.
 *
 * @author Adrian Pena
 */
public class UsbDeviceTest {
    private UsbCommunicationHandle handle;
    private TestUsbDevice testUsbDevice;

    @Before
    public void setUp() throws Exception {
        handle = mock(UsbCommunicationHandle.class);
        testUsbDevice = new TestUsbDevice();
        testUsbDevice.setHandle(handle);
    }

    @Test
    public void disconnect() throws Exception {
        when: {
            testUsbDevice.disconnect();
        }
        then: {
            verify(handle, times(1)).disconnect(eq(testUsbDevice));
        }
    }

    @Test
    public void sendMessage() throws Exception {
        byte[] message;

        given: {
            message = new byte[] {};
        }
        when: {
            testUsbDevice.sendMessage(message);
        }
        then: {
            verify(handle, times(1)).communicateWithDevice(eq(testUsbDevice), eq(message));
        }
    }

    private class TestUsbDevice extends UsbDevice {

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