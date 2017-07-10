package com.rastadrian.jblinky.devices.usb;

import com.rastadrian.jblinky.core.light.Light;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created on 7/9/17.
 *
 * @author Adrian Pena
 */
public class UsbLightSourceTest {

    @Test
    public void getLights() throws Exception {
        //Given
        Class<? extends UsbLight> spec = TestUsbLight.class;
        UsbCommunicationsHandler usbHandler = mock(UsbCommunicationsHandler.class);
        UsbLight foundedLight = new TestUsbLight();
        when(usbHandler.getLights(any(Collection.class))).thenReturn(singletonList(foundedLight));
        UsbLightSource usbLightSource = UsbLightSource.builder()
                .withUsbLightSpecs(spec)
                .withUsbHandler(usbHandler)
                .build();

        //When
        Collection<? extends Light> lights = usbLightSource.getLights();

        //Then
        assertThat(lights).first().isEqualTo(foundedLight);
        assertThat(lights).size().isEqualTo(1);
    }

    @UsbSpecification(vendorId = 0x1, productId = 0x1)
    public class TestUsbLight extends UsbLight {

        @Override
        public String getId() {
            return null;
        }

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
    }
}