package com.rastadrian.jblinky.devices.usb;

import com.rastadrian.jblinky.core.light.Light;
import com.rastadrian.jblinky.core.light.LightSource;
import com.rastadrian.jblinky.devices.usb.libusb.LibUsbCommunicationHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created on 6/29/17.
 *
 * @author Adrian Pena
 */
@Slf4j
public class UsbLightSource implements LightSource {

    private final Collection<UsbRegistry> registries;
    private final UsbCommunicationsHandler usbHandler;

    private UsbLightSource(Builder builder) {
        this.registries = Arrays.stream(builder.usbLightSpecs)
                .map(light -> {
                    UsbSpecification spec = light.getAnnotation(UsbSpecification.class);
                    return new UsbRegistry(spec.vendorId(), spec.productId(), light);
                })
                .collect(Collectors.toList());
        this.usbHandler = builder.usbHandler;
    }

    @Override
    public Collection<? extends Light> getLights() {
        Collection<UsbLight> lights = usbHandler.getLights(registries);
        lights.forEach(light -> light.setSource(this));
        return lights;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Class<? extends UsbLight>[] usbLightSpecs;
        private UsbCommunicationsHandler usbHandler = new LibUsbCommunicationHandler();

        private Builder() {
            //NOP
        }

        @SafeVarargs
        public final Builder withUsbLightSpecs(Class<? extends UsbLight>... lightSpecs) {
            this.usbLightSpecs = lightSpecs;
            return this;
        }

        public Builder withUsbHandler(UsbCommunicationsHandler handler) {
            this.usbHandler = handler;
            return this;
        }

        public UsbLightSource build() {
            return new UsbLightSource(this);
        }
    }
}
