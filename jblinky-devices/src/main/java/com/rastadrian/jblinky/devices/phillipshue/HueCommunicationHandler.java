package com.rastadrian.jblinky.devices.phillipshue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestOperations;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by adrian on 7/16/17.
 */
@Slf4j
public class HueCommunicationHandler {

    private final RestOperations restOperations;
    private final String bridgeUrl;
    private final String username;

    private HueCommunicationHandler(Builder builder) {
        this.bridgeUrl = builder.bridgeUrl;
        this.restOperations = builder.restOperations;
        this.username = builder.username;
    }

    public Collection<HueLight> getLights() {
        try {
            String serializedLights = restOperations.getForObject("http://{bridgeUrl}/api/{username}/lights", String.class, bridgeUrl, username);
            ObjectMapper mapper = new ObjectMapper();
            MapType type = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, HueLight.class);
            Map<String, HueLight> lights = mapper.readValue(serializedLights, type);
            lights.forEach((index, light) -> {
                light.setIndex(index);
                light.setCommunicationHandler(this);

            });
            return lights.values();
        } catch (Exception e) {
            LOGGER.error("Unable to retrieve Hue lights", e);
        }
        return null;
    }

    public void updateLightState(HueLight light) {
        try {
            restOperations.put("http://{bridgeUrl}/api/{username}/lights/{index}/state", light.getHueState(), bridgeUrl, username, light.getIndex());
        } catch (HttpStatusCodeException e) {
            LOGGER.error("Unable to communicate with Hue light", e);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String bridgeUrl;
        private String username;
        private RestOperations restOperations;

        private Builder() {
            //NOP
        }

        public Builder withBridgeUrl(String url) {
            this.bridgeUrl = url;
            return this;
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withRestOperations(RestOperations restOperations) {
            this.restOperations = restOperations;
            return this;
        }

        public HueCommunicationHandler build() {
            return new HueCommunicationHandler(this);
        }
    }
}
