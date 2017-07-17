package com.rastadrian.jblinky.devices.phillipshue;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

/**
 * Created by adrian on 7/16/17.
 */
public class HueLightSourceTest {

    @Test
    public void test() throws Exception {

        HueCommunicationHandler communicationHandler = HueCommunicationHandler.builder()
                .withRestOperations(new RestTemplate())
                .withBridgeUrl("")
                .withUsername("")
                .build();

        HueLightSource hueLightSource = HueLightSource.builder()
                .withCommunicationHandler(communicationHandler)
                .build();

        hueLightSource.getLights().stream()
                .filter(light -> light.getId().equals(""))
                .findFirst()
                .ifPresent(light -> {
                    light.success();
                    try {
                        Thread.sleep(3500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    light.inProgress();
                    try {
                        Thread.sleep(3500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    light.warning();
                    try {
                        Thread.sleep(3500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    light.failure();
                    try {
                        Thread.sleep(3500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

    }
}