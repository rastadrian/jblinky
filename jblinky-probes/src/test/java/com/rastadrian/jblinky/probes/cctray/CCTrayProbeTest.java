package com.rastadrian.jblinky.probes.cctray;

import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import com.rastadrian.jblinky.core.light.Light;
import com.rastadrian.jblinky.core.probe.State;
import com.rastadrian.jblinky.core.probe.Status;
import com.rastadrian.jblinky.probes.NetworkHandle;
import com.rastadrian.jblinky.probes.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
        //Given
        String url = "jenkinsUrl";

        //When
        trayProbe = CCTrayProbe.builder().withUrl(url).build();

        //Then
        assertThat(trayProbe.getName()).isEqualTo(String.format("CC Tray Probe [%s]", url));
    }

    @Test
    public void getName_withCustomName() throws Exception {
        //Given
        String name = "job name";
        String url = "jenkinsUrl";

        //When
        trayProbe = CCTrayProbe.builder().withName(name).withUrl(url).withJobs("job").build();

        //Then
        assertThat(trayProbe.getName()).isEqualTo(name);
    }

    @Test
    public void verify_anonymousCCTray() throws Exception {
        //Given
        String jenkinsCCTray = TestUtil.readResource(this, "/cctray/jenkins-cctray-all-successful.xml");
        when(networkHandle.get(anyString(), eq(String.class), any(HttpHeaders.class))).thenReturn(jenkinsCCTray);
        trayProbe = CCTrayProbe.builder().withUrl("url").withJobs("job1").withNetworkHandle(networkHandle).build();

        //When
        Status statusReport = trayProbe.verify();

        //Then
        assertThat(statusReport.getState()).isEqualTo(State.SUCCESS);
        assertThat(statusReport.getMessages().length).isEqualTo(1);
    }

    @Test
    public void verify_anonymousCCTray_job1Failure() throws Exception {
        //Given
        String jenkinsCCTray = TestUtil.readResource(this, "/cctray/jenkins-cctray-job1-failure.xml");
        when(networkHandle.get(anyString(), eq(String.class), any(HttpHeaders.class))).thenReturn(jenkinsCCTray);
        trayProbe = CCTrayProbe.builder().withUrl("url").withJobs("job1").withNetworkHandle(networkHandle).build();

        //When
        Status statusReport = trayProbe.verify();

        //Then
        assertThat(statusReport.getState()).isEqualTo(State.FAILURE);
        assertThat(statusReport.getMessages()[0]).isEqualTo("job1 \uD83D\uDD25");
    }

    @Test
    public void verify_anonymousCCTray_withNetworkFailure() throws Exception {
        //Given
        when(networkHandle.get(anyString(), eq(String.class), any(HttpHeaders.class))).thenReturn(null);
        trayProbe = CCTrayProbe.builder().withUrl("url").withJobs("job1").withNetworkHandle(networkHandle).build();
        Light light = mock(Light.class);

        //When
        Status statusReport = trayProbe.verify(light);

        //Then
        assertThat(statusReport.getState()).isEqualTo(State.FAILURE);
        assertThat(statusReport.getMessages()[0]).isEqualTo("Network failure. \uD83D\uDD0C");
        verify(light, times(1)).failure();
    }

    @Test
    public void verify_authenticatedCCTray() throws Exception {
        //Given
        String username = "username";
        String password = "password";
        String jenkinsCCTray = TestUtil.readResource(this, "/cctray/jenkins-cctray-all-successful.xml");
        HttpHeaders authenticationHeaders = new HttpHeaders();
        when(networkHandle.createHeaders(eq(username), eq(password))).thenReturn(authenticationHeaders);
        when(networkHandle.get(anyString(), eq(String.class), eq(authenticationHeaders))).thenReturn(jenkinsCCTray);
        trayProbe = CCTrayProbe.builder().withUrl("url").withJobs("job1").withNetworkHandle(networkHandle).build();
        Light light = mock(Light.class);

        //When
        Status statusReport = trayProbe.verify(light);

        //Then
        assertThat(statusReport.getState()).isEqualTo(State.SUCCESS);
        assertThat(statusReport.getMessages().length).isEqualTo(1);
        verify(light, times(1)).success();
    }

    @Test
    public void validateModelPojos() throws Exception {
        //Given
        Class<?>[] classes = {CCTray.class, CCProject.class};

        Validator validator = ValidatorBuilder.create()
                .with(new GetterMustExistRule())
                .with(new SetterMustExistRule())
                .with(new SetterTester())
                .with(new GetterTester())
                .build();

        //Then
        Arrays.stream(classes)
                .forEach(pojo -> validator.validate(PojoClassFactory.getPojoClass(pojo)));
    }
}