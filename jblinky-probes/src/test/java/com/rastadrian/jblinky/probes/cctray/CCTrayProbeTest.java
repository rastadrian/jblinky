package com.rastadrian.jblinky.probes.cctray;

import com.rastadrian.jblinky.core.probe.State;
import com.rastadrian.jblinky.core.probe.Status;
import com.rastadrian.jblinky.probes.NetworkUtil;
import com.rastadrian.jblinky.probes.TestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpHeaders;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by adrian on 9/27/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({NetworkUtil.class, CCTrayProbe.class})
public class CCTrayProbeTest {

    private CCTrayProbe trayProbe;

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
            mockStatic(NetworkUtil.class);
            String jenkinsCCTray = TestUtil.readResource(this, "/cctray/jenkins-cctray-all-successful.xml");
            when(NetworkUtil.get(anyString(), eq(String.class), any(HttpHeaders.class))).thenReturn(jenkinsCCTray);
            trayProbe = new CCTrayProbe("url", new String[]{"job1"});
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
            mockStatic(NetworkUtil.class);
            String jenkinsCCTray = TestUtil.readResource(this, "/cctray/jenkins-cctray-job1-failure.xml");
            when(NetworkUtil.get(anyString(), eq(String.class), any(HttpHeaders.class))).thenReturn(jenkinsCCTray);
            trayProbe = new CCTrayProbe("url", new String[]{"job1"});
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
            mockStatic(NetworkUtil.class);
            when(NetworkUtil.get(anyString(), eq(String.class), any(HttpHeaders.class))).thenReturn(null);
            trayProbe = new CCTrayProbe("url", new String[]{"job1"});
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
            mockStatic(NetworkUtil.class);
            username = "username";
            password = "password";
            String jenkinsCCTray = TestUtil.readResource(this, "/cctray/jenkins-cctray-all-successful.xml");
            HttpHeaders authenticationHeaders = new HttpHeaders();
            when(NetworkUtil.createHeaders(eq(username), eq(password))).thenReturn(authenticationHeaders);
            when(NetworkUtil.get(anyString(), eq(String.class), eq(authenticationHeaders))).thenReturn(jenkinsCCTray);
            trayProbe = new CCTrayProbe("url", username, password, new String[]{"job1"});
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