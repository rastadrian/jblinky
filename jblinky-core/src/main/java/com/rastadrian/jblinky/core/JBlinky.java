package com.rastadrian.jblinky.core;

import com.rastadrian.jblinky.core.light.Light;
import com.rastadrian.jblinky.core.light.LightSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The jBlinky main component, it initializes, searches, connects and provides lights.
 *
 * @author Adrian Pena
 */
public class JBlinky {

    private final Collection<Light> lights;

    private JBlinky(Builder builder) {
        LightSource[] lightSources = builder.lightSources;
        List<Light> lights = new ArrayList<>();
        for (LightSource source : lightSources) {
            lights.addAll(source.getLights());
        }
        if (lights.isEmpty()) {
            throw new JBlinkyException("No lights found.");
        }
        this.lights = lights;
    }

    public Collection<Light> getLights() {
        return lights;
    }

    public Light getLight() {
        return lights.stream().findFirst().orElse(null);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private LightSource[] lightSources;

        private Builder() {
            //NOP
        }

        public Builder withLightSources(LightSource... lightSources) {
            this.lightSources = lightSources;
            return this;
        }

        public JBlinky build() {
            if (lightSources == null || lightSources.length == 0) {
                throw new JBlinkyException("No light sources specified.");
            }
            return new JBlinky(this);
        }
    }
}
