package com.rastadrian.jblinky.probes.cctray;

import com.rastadrian.jblinky.core.probe.State;
import com.rastadrian.jblinky.core.probe.Status;
import com.rastadrian.jblinky.probes.NetworkHandle;
import com.rastadrian.jblinky.probes.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by adrian on 9/27/16.
 */
public class CCTrayProbeTest {

    private CCTrayProbe trayProbe;
    private NetworkHandle networkHandle;

    @Before
    public void setUp() throws Exception {
        networkHandle = mock(NetworkHandle.class);

    }

    @Test
    public void getName_withDefaultName() throws Exception {
        String url = "jenkinsUrl";
        when:
        {
            trayProbe = new CCTrayProbe(url);
        }
        then:
        {
            assertThat(trayProbe.getName()).isEqualTo(String.format("CC Tray Probe [%s]", url));
        }
    }

    @Test
    public void getName_withCustomName() throws Exception {
        String name = "job name";
        String url = "jenkinsUrl";
        when:
        {
            trayProbe = new CCTrayProbe(name, url, new String[]{"job"});
        }
        then:
        {
            assertThat(trayProbe.getName()).isEqualTo(name);
        }
    }

    @Test
    public void verify_anonymousCCTray() throws Exception {
        Status statusReport;
        given:
        {
            String jenkinsCCTray = TestUtil.readResource(this, "/cctray/jenkins-cctray-all-successful.xml");
            when(networkHandle.get(anyString(), eq(String.class), any(HttpHeaders.class))).thenReturn(jenkinsCCTray);
            trayProbe = new CCTrayProbe("url", new String[]{"job1"});
            trayProbe.setNetworkHandle(networkHandle);
        }
        when:
        {
            statusReport = trayProbe.verify();
        }
        then:
        {
            assertThat(statusReport.getState()).isEqualTo(State.SUCCESS);
            assertThat(statusReport.getMessages().length).isEqualTo(1);
        }
    }

    @Test
    public void verify_anonymousCCTray_job1Failure() throws Exception {
        Status statusReport;
        given:
        {
            String jenkinsCCTray = TestUtil.readResource(this, "/cctray/jenkins-cctray-job1-failure.xml");
            when(networkHandle.get(anyString(), eq(String.class), any(HttpHeaders.class))).thenReturn(jenkinsCCTray);
            trayProbe = new CCTrayProbe("url", new String[]{"job1"});
            trayProbe.setNetworkHandle(networkHandle);
        }
        when:
        {
            statusReport = trayProbe.verify();
        }
        then:
        {
            assertThat(statusReport.getState()).isEqualTo(State.FAILURE);
            assertThat(statusReport.getMessages()[0]).isEqualTo("Project: job1 [Failure]");
        }
    }

    @Test
    public void verify_anonymousCCTray_withNetworkFailure() throws Exception {
        Status statusReport;
        given:
        {
            when(networkHandle.get(anyString(), eq(String.class), any(HttpHeaders.class))).thenReturn(null);
            trayProbe = new CCTrayProbe("url", new String[]{"job1"});
            trayProbe.setNetworkHandle(networkHandle);
        }
        when:
        {
            statusReport = trayProbe.verify();
        }
        then:
        {
            assertThat(statusReport.getState()).isEqualTo(State.FAILURE);
            assertThat(statusReport.getMessages()[0]).isEqualTo("Unable to retrieve cc-tray.");
        }
    }

    @Test
    public void verify_authenticatedCCTray() throws Exception {
        Status statusReport;
        String username;
        String password;
        given:
        {

            username = "username";
            password = "password";
            String jenkinsCCTray = TestUtil.readResource(this, "/cctray/jenkins-cctray-all-successful.xml");
            HttpHeaders authenticationHeaders = new HttpHeaders();
            when(networkHandle.createHeaders(eq(username), eq(password))).thenReturn(authenticationHeaders);
            when(networkHandle.get(anyString(), eq(String.class), eq(authenticationHeaders))).thenReturn(jenkinsCCTray);
            trayProbe = new CCTrayProbe("url", username, password, new String[]{"job1"});
            trayProbe.setNetworkHandle(networkHandle);
        }
        when:
        {
            statusReport = trayProbe.verify();
        }
        then:
        {
            assertThat(statusReport.getState()).isEqualTo(State.SUCCESS);
            assertThat(statusReport.getMessages().length).isEqualTo(1);
        }
    }
}