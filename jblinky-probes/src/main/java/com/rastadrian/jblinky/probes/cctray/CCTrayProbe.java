package com.rastadrian.jblinky.probes.cctray;

import com.rastadrian.jblinky.core.light.Light;
import com.rastadrian.jblinky.core.probe.Probe;
import com.rastadrian.jblinky.core.probe.ProbeLightHandler;
import com.rastadrian.jblinky.core.probe.State;
import com.rastadrian.jblinky.core.probe.Status;
import com.rastadrian.jblinky.probes.NetworkHandle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Cruise Control Tray provides a mean to monitor Continuous Integration Servers.
 *
 * @author Adrian Pena
 */
@Slf4j
public class CCTrayProbe implements Probe {
    private static final String DEFAULT_NAME_FORMAT = "CC Tray Probe [%s]";
    private static final int NETWORK_TIME_OUT_MILLIS = 3000;

    private final String name;
    private final String url;
    private final String username;
    private final String password;
    private final String[] jobs;
    private NetworkHandle networkHandle;

    /**
     * Creates an authenticated CCTray Probe that monitors the provided jobs.
     * @param builder the Probe's builder.
     */
    private CCTrayProbe(Builder builder) {
        LOGGER.info("Creating CCTray Probe [{}]", builder.name);
        this.url = builder.url;
        this.username = builder.username;
        this.password = builder.password;
        this.jobs = builder.jobs;
        this.name = builder.name;
        this.networkHandle = builder.networkHandle;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Status verify(Light... lights) {
        LOGGER.debug("CCTray Probe [{}]: Verifying probe...", name);
        Status status = doVerify();

        if (lights == null || lights.length == 0) {
            return status;
        }

        ProbeLightHandler probeLightHandler = new ProbeLightHandler();
        Arrays.stream(lights)
                .forEach(light -> probeLightHandler.accept(status.getState(), light));

        return status;
    }

    private Status doVerify() {
        HttpHeaders headers;
        List<String> messages = new ArrayList<String>();
        State state = State.SUCCESS;
        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
            LOGGER.debug("CCTray Probe [{}]: Using basic authentication.", name);
            headers = networkHandle.createHeaders(username, password);
        } else {
            headers = new HttpHeaders();
        }
        String content = networkHandle.get(url, String.class, headers);
        CCTray ccTray = CCTrayParser.parseCCTray(content);
        if (ccTray == null) {
            LOGGER.warn("CCTray Probe [{}]: Unable to retrieve CCTray.", name);
            return new Status(State.FAILURE, new String[]{"Network failure. 🔌"});
        }
        for (CCProject project : ccTray.getProjects()) {
            if (ArrayUtils.contains(jobs, project.getName())) {
                if (!StringUtils.equals(project.getLastBuildStatus(), "Success")) {
                    LOGGER.debug("CCTray Probe [{}]: Project [{}] has non success status [{}]", name, project.getName(), project.getLastBuildStatus());
                    state = State.FAILURE;
                    messages.add(project.getName() + " 🔥");
                }
            }
        }
        if (state == State.SUCCESS) {
            messages.add("All jobs are healthy. 👌");
        }
        LOGGER.debug("CCTray Probe [{}]: Verification complete, returning state [{}]", name, state);
        return new Status(state, messages.toArray(new String[messages.size()]));
    }

    /**
     * @return the <code>Probe</code>'s builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * The CCTrayProbe's builder.
     */
    public static class Builder {
        private String name;
        private String url;
        private String username;
        private String password;
        private String[] jobs;
        private NetworkHandle networkHandle = new NetworkHandle(createRestOperations());

        private Builder() {
            //NOP
        }

        /**
         * @param name the Probe's name
         * @return the builder's instance.
         */
        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        /**
         * @param username the tray's credential's username
         * @param password the tray's credential's password
         * @return the builder's instance.
         */
        public Builder withCredentials(String username, String password) {
            this.username = username;
            this.password = password;
            return this;
        }

        /**
         * @param jobs the job names that should be verified.
         * @return the builder's instance.
         */
        public Builder withJobs(String... jobs) {
            this.jobs = jobs;
            return this;
        }

        /**
         * @param url the URL for the cruise control tray's xml.
         * @return the builder's instance.
         */
        public Builder withUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder withNetworkHandle(NetworkHandle networkHandle) {
            this.networkHandle = networkHandle;
            return this;
        }

        /**
         * @return a <code>CCTrayProbe</code> instance.
         */
        public CCTrayProbe build() {
            if (this.name == null) {
                this.name = String.format(DEFAULT_NAME_FORMAT, url);
            }
            return new CCTrayProbe(this);
        }

        private RestOperations createRestOperations() {
            RestTemplate restTemplate = new RestTemplate();
            SimpleClientHttpRequestFactory factory = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
            factory.setConnectTimeout(NETWORK_TIME_OUT_MILLIS);
            factory.setReadTimeout(NETWORK_TIME_OUT_MILLIS);
            return restTemplate;
        }
    }
}
