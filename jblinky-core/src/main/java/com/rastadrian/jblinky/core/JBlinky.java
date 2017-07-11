package com.rastadrian.jblinky.core;

import com.rastadrian.jblinky.core.light.Light;
import com.rastadrian.jblinky.core.light.LightSource;
import com.rastadrian.jblinky.core.probe.Probe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The jBlinky main component, it initializes, searches, connects and provides lights.
 *
 * It can be built specifying {@link LightSource} components that would be queried for interactive {@link Light}s
 * that can be later used for {@link Probe} verification or just direct manipulation.
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

    /**
     * @return a collection of the connected lights.
     */
    public Collection<Light> getLights() {
        return lights;
    }

    /**
     * @return the first connected light.
     */
    public Light getLight() {
        return lights.stream().findFirst().orElse(null);
    }

    /**
     * @return jBlinky's builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * jBlinky's Building component.
     */
    public static class Builder {

        private LightSource[] lightSources;

        private Builder() {
            //NOP
        }

        /**
         * @param lightSources the light sources that jBlinky should use to look for lights.
         * @return the builder's instance.
         */
        public Builder withLightSources(LightSource... lightSources) {
            this.lightSources = lightSources;
            return this;
        }

        /**
         * @return a jBlinky object.
         */
        public JBlinky build() {
            if (lightSources == null || lightSources.length == 0) {
                throw new JBlinkyException("No light sources specified.");
            }
            return new JBlinky(this);
        }
    }
}
