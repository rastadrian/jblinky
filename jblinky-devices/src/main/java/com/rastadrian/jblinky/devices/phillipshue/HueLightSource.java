package com.rastadrian.jblinky.devices.phillipshue;

import com.rastadrian.jblinky.core.light.Light;
import com.rastadrian.jblinky.core.light.LightSource;

import java.util.Collection;

/**
 * Created by adrian on 7/16/17.
 */
public class HueLightSource implements LightSource {

    private HueCommunicationHandler communicationHandler;

    private HueLightSource(Builder builder) {
        this.communicationHandler = builder.communicationHandler;
    }

    @Override
    public Collection<? extends Light> getLights() {
        Collection<HueLight> lights = communicationHandler.getLights();
        lights.forEach(light -> light.setSource(this));
        return lights;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private HueCommunicationHandler communicationHandler;

        private Builder() {
            //NOP
        }

        public Builder withCommunicationHandler(HueCommunicationHandler communicationHandler) {
            this.communicationHandler = communicationHandler;
            return this;
        }

        public HueLightSource build() {
            return new HueLightSource(this);
        }
    }
}
