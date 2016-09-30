package com.rastadrian.jblinky.probes.cctray;

import com.rastadrian.jblinky.core.probe.Probe;
import com.rastadrian.jblinky.core.probe.State;
import com.rastadrian.jblinky.core.probe.Status;
import com.rastadrian.jblinky.probes.NetworkHandle;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * The Cruise Control Tray provides a mean to monitor Continuous Integration Servers.
 *
 * @author Adrian Pena
 */
public class CCTrayProbe implements Probe {
    private static final Logger LOGGER = LoggerFactory.getLogger(CCTrayProbe.class);
    private static final String DEFAULT_NAME_FORMAT = "CC Tray Probe [%s]";
    private static final int NETWORK_TIME_OUT_MILLIS = 3000;

    private final String name;
    private final String url;
    private final String username;
    private final String password;
    private final String[] jobs;
    private NetworkHandle networkHandle;

    /**
     * Creates an unauthenticated CCTray Probe that monitors the provided jobs.
     *
     * @param url  the URL for the CI Cruise Control Tray
     * @param jobs the job names to be verified.
     */
    public CCTrayProbe(String url, String... jobs) {
        this(url, null, null, jobs);
    }

    /**
     * Creates an unauthenticated CCTray Probe that monitors the provided jobs.
     *
     * @param name the probe's custom name.
     * @param url  the URL for the CI Cruise Control Tray
     * @param jobs the job names to be verified.
     */
    public CCTrayProbe(String name, String url, String... jobs) {
        this(name, url, null, null, jobs);
    }

    /**
     * Creates an authenticated CCTray Probe that monitors the provided jobs.
     *
     * @param url      the URL for the CI Cruise Control Tray
     * @param username the basic-auth username for the CI Server
     * @param password the basic-auth password for the CI Server
     * @param jobs     the list of job names to be verified.
     */
    public CCTrayProbe(String url, String username, String password, String... jobs) {
        this(String.format(DEFAULT_NAME_FORMAT, url), url, username, password, jobs);
    }

    /**
     * Creates an authenticated CCTray Probe that monitors the provided jobs.
     *
     * @param name     the probe's name.
     * @param url      the URL for the CI Cruise Control Tray
     * @param username the basic-auth username for the CI Server
     * @param password the basic-auth password for the CI Server
     * @param jobs     the list of job names to be verified.
     */
    public CCTrayProbe(String name, String url, String username, String password, String... jobs) {
        LOGGER.info("Creating CCTray Probe [{}]", name);
        this.url = url;
        this.username = username;
        this.password = password;
        this.jobs = jobs;
        this.name = name;
        this.networkHandle = new NetworkHandle(createCCTrayNetworkOperator());
    }

    public String getName() {
        return name;
    }

    public Status verify() {
        LOGGER.debug("CCTray Probe [{}]: Verifying probe...", name);
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
            return new Status(State.FAILURE, new String[]{"Unable to retrieve cc-tray."});
        }
        for (Project project : ccTray.getProjects()) {
            if (ArrayUtils.contains(jobs, project.getName())) {
                messages.add(String.format("Project: %s [%s]", project.getName(), project.getLastBuildStatus()));
                if (!StringUtils.equals(project.getLastBuildStatus(), "Success")) {
                    LOGGER.debug("CCTray Probe [{}]: Project [{}] has non success status [{}]", name, project.getName(), project.getLastBuildStatus());
                    state = State.FAILURE;
                }
            }
        }
        LOGGER.debug("CCTray Probe [{}]: Verification complete, returning state [{}]", name, state);
        return new Status(state, messages.toArray(new String[messages.size()]));
    }

    public void setNetworkHandle(NetworkHandle networkHandle) {
        this.networkHandle = networkHandle;
    }

    private RestOperations createCCTrayNetworkOperator() {
        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory factory = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
        factory.setConnectTimeout(NETWORK_TIME_OUT_MILLIS);
        factory.setReadTimeout(NETWORK_TIME_OUT_MILLIS);
        return restTemplate;
    }
}
