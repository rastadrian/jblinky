package com.rastadrian.jblinky.core.usb;

import com.rastadrian.jblinky.core.usb.light.TestUsbLight;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
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
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

/**
 * Created on 9/29/16.
 *
 * @author Adrian Pena
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({LibUsb.class, LibUsbHandle.class, Device.class, DeviceList.class, DeviceDescriptor.class})
public class LibUsbHandleTest {

    private LibUsbHandle libUsbHandle;

    @Before
    public void setUp() throws Exception {
        libUsbHandle = new LibUsbHandle();
    }

    @Test
    public void getConnectedUsbLights_withOneDeviceFound() throws Exception {
        List<DeviceRegister> deviceSpecs;
        List<UsbDevice> connectedLights;
        given:
        {
            short vendorId = 0x1;
            short productId = 0x2;

            libUsbHandle = spy(libUsbHandle);

            List<Device> deviceList = new ArrayList<>();
            deviceList.add(PowerMockito.mock(Device.class));
            DeviceList libUsbDeviceList = PowerMockito.mock(DeviceList.class);
            when(libUsbDeviceList.getSize()).thenReturn(1);
            when(libUsbDeviceList.iterator()).thenReturn(deviceList.iterator());
            PowerMockito.doReturn(libUsbDeviceList).when(libUsbHandle, method(LibUsbHandle.class, "getDeviceList"))
                    .withNoArguments();


            DeviceDescriptor deviceDescriptor = PowerMockito.mock(DeviceDescriptor.class);
            when(deviceDescriptor.idVendor()).thenReturn(vendorId);
            when(deviceDescriptor.idProduct()).thenReturn(productId);
            PowerMockito.doReturn(deviceDescriptor).when(libUsbHandle, method(LibUsbHandle.class, "getDeviceDescriptor", Device.class))
                    .withArguments(any(Device.class));

            deviceSpecs = new ArrayList<>();
            deviceSpecs.add(new DeviceRegister(vendorId, productId, TestUsbLight.class));

            mockStatic(LibUsb.class);
        }
        when:
        {
            connectedLights = libUsbHandle.getConnectedUsbLights(deviceSpecs);
        }
        then:
        {
            assertThat(connectedLights).isNotNull().first().isInstanceOf(TestUsbLight.class);
        }
    }

    @Test(expected = LibUsbException.class)
    public void getConnectedUsbLights_withFailedDeviceDescriptor() throws Exception {
        List<DeviceRegister> deviceSpecs;
        List<UsbDevice> connectedLights;
        given:
        {
            libUsbHandle = spy(libUsbHandle);
            List<Device> deviceList = new ArrayList<>();
            deviceList.add(PowerMockito.mock(Device.class));
            DeviceList libUsbDeviceList = PowerMockito.mock(DeviceList.class);
            when(libUsbDeviceList.getSize()).thenReturn(1);
            when(libUsbDeviceList.iterator()).thenReturn(deviceList.iterator());
            PowerMockito.doReturn(libUsbDeviceList).when(libUsbHandle, method(LibUsbHandle.class, "getDeviceList"))
                    .withNoArguments();
            deviceSpecs = new ArrayList<>();
            deviceSpecs.add(mock(DeviceRegister.class));
            mockStatic(LibUsb.class);
            when(LibUsb.getDeviceList(any(Context.class), any(DeviceList.class))).thenReturn(1);
            when(LibUsb.getDeviceDescriptor(any(Device.class), any(DeviceDescriptor.class))).thenReturn(LibUsb.ERROR_ACCESS);
        }
        when:
        {
            connectedLights = libUsbHandle.getConnectedUsbLights(deviceSpecs);
        }
        then:
        {
            assertThat(connectedLights).isNotNull().isEmpty();
        }
    }

    @Test(expected = LibUsbException.class)
    public void getConnectedUsbLights_withFailedInitialization() throws Exception {
        List<DeviceRegister> deviceSpecs;
        given:
        {
            deviceSpecs = new ArrayList<>();
            deviceSpecs.add(mock(DeviceRegister.class));
            mockStatic(LibUsb.class);
            when(LibUsb.init(any(Context.class))).thenReturn(LibUsb.ERROR_TIMEOUT);
        }
        when:
        {
            libUsbHandle.getConnectedUsbLights(deviceSpecs);
        }
    }

    @Test(expected = LibUsbException.class)
    public void getConnectedUsbLights_withUsbConnectionError() throws Exception {
        List<DeviceRegister> deviceSpecs;
        given:
        {
            deviceSpecs = new ArrayList<>();
            deviceSpecs.add(mock(DeviceRegister.class));
            mockStatic(LibUsb.class);
            when(LibUsb.getDeviceList(any(Context.class), any(DeviceList.class))).thenReturn(-1);
        }
        when:
        {
            libUsbHandle.getConnectedUsbLights(deviceSpecs);
        }
    }

    @Test
    public void getConnectedUsbLights_withNoDevicesFound() throws Exception {
        List<DeviceRegister> deviceSpecs;
        List<UsbDevice> connectedLights;
        given:
        {
            deviceSpecs = new ArrayList<>();
            deviceSpecs.add(mock(DeviceRegister.class));
            mockStatic(LibUsb.class);
            when(LibUsb.getDeviceList(any(Context.class), any(DeviceList.class))).thenReturn(0);
        }
        when:
        {
            connectedLights = libUsbHandle.getConnectedUsbLights(deviceSpecs);
        }
        then:
        {
            assertThat(connectedLights).isNotNull().isEmpty();
        }
    }

    @Test(expected = LibUsbException.class)
    public void communicateWithDevice_withUsbCommunicationError() throws Exception {
        given:
        {
            List<DeviceRegister> deviceSpecs = new ArrayList<>();
            deviceSpecs.add(mock(DeviceRegister.class));
            mockStatic(LibUsb.class);
            when(LibUsb.controlTransfer(any(DeviceHandle.class), anyByte(), anyByte(), anyShort(), anyShort(), any(ByteBuffer.class), anyLong())).thenReturn(LibUsb.ERROR_BUSY);
            libUsbHandle.getConnectedUsbLights(deviceSpecs);
        }
        when:
        {
            libUsbHandle.communicateWithDevice(new TestUsbLight(), new byte[]{});
        }
    }

    @Test
    public void communicateWithDevice() throws Exception {
        given:
        {
            List<DeviceRegister> deviceSpecs = new ArrayList<>();
            deviceSpecs.add(mock(DeviceRegister.class));
            mockStatic(LibUsb.class);
            when(LibUsb.controlTransfer(any(DeviceHandle.class), anyByte(), anyByte(), anyShort(), anyShort(), any(ByteBuffer.class), anyLong())).thenReturn(LibUsb.SUCCESS);
            libUsbHandle.getConnectedUsbLights(deviceSpecs);
        }
        when:
        {
            libUsbHandle.communicateWithDevice(new TestUsbLight(), new byte[]{});
        }
    }

    @Test
    public void disconnect() throws Exception {
        TestUsbLight device;
        given:
        {
            List<DeviceRegister> deviceSpecs = new ArrayList<>();
            deviceSpecs.add(mock(DeviceRegister.class));
            mockStatic(LibUsb.class);
            when(LibUsb.controlTransfer(any(DeviceHandle.class), anyByte(), anyByte(), anyShort(), anyShort(), any(ByteBuffer.class), anyLong())).thenReturn(LibUsb.SUCCESS);
            libUsbHandle.getConnectedUsbLights(deviceSpecs);
            device = new TestUsbLight();
            libUsbHandle.communicateWithDevice(device, new byte[]{});
        }
        when:
        {
            libUsbHandle.disconnect(device);
        }
    }
}