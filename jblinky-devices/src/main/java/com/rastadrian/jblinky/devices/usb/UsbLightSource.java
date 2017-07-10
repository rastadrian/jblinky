package com.rastadrian.jblinky.devices.usb;

import com.rastadrian.jblinky.core.light.Light;
import com.rastadrian.jblinky.core.light.LightSource;
import com.rastadrian.jblinky.devices.usb.libusb.LibUsbCommunicationHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * A Light Source that reads from the computer's connected USB dock.
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

    /**
     * @return the {@link UsbLightSource} builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A <code>UsbLightSource</code> builder.
     */
    public static class Builder {
        private Class<? extends UsbLight>[] usbLightSpecs;
        private UsbCommunicationsHandler usbHandler = new LibUsbCommunicationHandler();

        private Builder() {
            //NOP
        }

        /**
         * This classes should be annotated with {@link UsbSpecification}.
         *
         * @param lightSpecs one or more {@link UsbLight} implementation classes.
         * @return the builder's instance.
         */
        @SafeVarargs
        public final Builder withUsbLightSpecs(Class<? extends UsbLight>... lightSpecs) {
            this.usbLightSpecs = lightSpecs;
            return this;
        }

        /**
         * If this is not specified, the default {@link LibUsbCommunicationHandler} is provided.
         *
         * @param handler a UsbCommunicationsHandler implementation.
         * @return the builder's instance.
         */
        public Builder withUsbHandler(UsbCommunicationsHandler handler) {
            this.usbHandler = handler;
            return this;
        }

        /**
         * @return an <code>UsbLightSource</code> instance.
         */
        public UsbLightSource build() {
            return new UsbLightSource(this);
        }
    }
}
