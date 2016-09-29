package com.rastadrian.jblinky.core.usb;

import com.rastadrian.jblinky.core.usb.light.UsbLight;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.usb4java.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created on 9/29/16.
 *
 * @author Adrian Pena
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({LibUsb.class, LibUsbHandle.class})
public class LibUsbHandleTest {

    private LibUsbHandle libUsbHandle;

    @Before
    public void setUp() throws Exception {
        libUsbHandle = new LibUsbHandle();
    }

    @Test(expected = LibUsbException.class)
    public void getConnectedUsbLights_withFailedInitialization() throws Exception {
        List<DeviceRegister> deviceSpecs;
        given: {
            deviceSpecs = new ArrayList<>();
            deviceSpecs.add(mock(DeviceRegister.class));
            mockStatic(LibUsb.class);
            when(LibUsb.init(any(Context.class))).thenReturn(LibUsb.ERROR_TIMEOUT);
        }
        when :{
            libUsbHandle.getConnectedUsbLights(deviceSpecs);
        }
    }

    @Test(expected = LibUsbException.class)
    public void getConnectedUsbLights_withUsbConnectionError() throws Exception {
        List<DeviceRegister> deviceSpecs;
        given: {
            deviceSpecs = new ArrayList<>();
            deviceSpecs.add(mock(DeviceRegister.class));
            mockStatic(LibUsb.class);
            when(LibUsb.getDeviceList(any(Context.class), any(DeviceList.class))).thenReturn(-1);
        }
        when :{
            libUsbHandle.getConnectedUsbLights(deviceSpecs);
        }
    }

    @Test
    public void getConnectedUsbLights_withNoDevicesFound() throws Exception {
        List<DeviceRegister> deviceSpecs;
        List<UsbDevice> connectedLights;
        given: {
            deviceSpecs = new ArrayList<>();
            deviceSpecs.add(mock(DeviceRegister.class));
            mockStatic(LibUsb.class);
            when(LibUsb.getDeviceList(any(Context.class), any(DeviceList.class))).thenReturn(0);
        }
        when :{
            connectedLights = libUsbHandle.getConnectedUsbLights(deviceSpecs);
        }
        then: {
            assertThat(connectedLights).isNotNull().isEmpty();
        }
    }

    @Test(expected = LibUsbException.class)
    public void communicateWithDevice_withUsbCommunicationError() throws Exception {
        given: {
            List<DeviceRegister> deviceSpecs = new ArrayList<>();
            deviceSpecs.add(mock(DeviceRegister.class));
            mockStatic(LibUsb.class);
            when(LibUsb.controlTransfer(any(DeviceHandle.class), anyByte(), anyByte(), anyShort(), anyShort(), any(ByteBuffer.class), anyLong())).thenReturn(LibUsb.ERROR_BUSY);
            libUsbHandle.getConnectedUsbLights(deviceSpecs);
        }
        when: {
            libUsbHandle.communicateWithDevice(new TestUsbDevice(), new byte[]{});
        }
    }

    @Test
    public void communicateWithDevice() throws Exception {
        given: {
            List<DeviceRegister> deviceSpecs = new ArrayList<>();
            deviceSpecs.add(mock(DeviceRegister.class));
            mockStatic(LibUsb.class);
            when(LibUsb.controlTransfer(any(DeviceHandle.class), anyByte(), anyByte(), anyShort(), anyShort(), any(ByteBuffer.class), anyLong())).thenReturn(LibUsb.SUCCESS);
            libUsbHandle.getConnectedUsbLights(deviceSpecs);
        }
        when: {
            libUsbHandle.communicateWithDevice(new TestUsbDevice(), new byte[]{});
        }
    }

    @Test
    public void disconnect() throws Exception {
        TestUsbDevice device;
        given: {
            List<DeviceRegister> deviceSpecs = new ArrayList<>();
            deviceSpecs.add(mock(DeviceRegister.class));
            mockStatic(LibUsb.class);
            when(LibUsb.controlTransfer(any(DeviceHandle.class), anyByte(), anyByte(), anyShort(), anyShort(), any(ByteBuffer.class), anyLong())).thenReturn(LibUsb.SUCCESS);
            libUsbHandle.getConnectedUsbLights(deviceSpecs);
            device = new TestUsbDevice();
            libUsbHandle.communicateWithDevice(device, new byte[]{});
        }
        when: {
            libUsbHandle.disconnect(device);
        }
    }

    private class TestUsbDevice extends UsbLight {

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