package com.rastadrian.jblinky.probes;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;


/**
 * Created by adrian on 9/27/16.
 */
public class NetworkHandleTest {
    private NetworkHandle networkHandle;
    private RestOperations restOperations;

    @Before
    public void setUp() throws Exception {
        restOperations = mock(RestOperations.class);
        networkHandle = new NetworkHandle(restOperations);
    }

    @Test
    public void createHeaders() throws Exception {

        HttpHeaders headers;
        String username;
        String password;
        given:
        {
            username = "username";
            password = "password";
        }
        when:
        {
            headers = networkHandle.createHeaders(username, password);
        }
        then:
        {
            List<String> authorization = headers.get("Authorization");
            assertThat(authorization).isNotNull();
            assertThat(authorization.get(0)).startsWith("Basic ");
        }
    }

    @Test
    public void get() throws Exception {
        String expectedBody;
        String returnedBody;
        given: {
            expectedBody = "This is the body";
            ResponseEntity<String> responseEntity = new ResponseEntity<>(expectedBody, HttpStatus.OK);
            when(restOperations.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class))).thenReturn(responseEntity);
        }
        when: {
            returnedBody = networkHandle.get("url", String.class, new HttpHeaders());
        }
        then: {
            assertThat(returnedBody).isEqualTo(expectedBody);
        }
    }

    @Test
    public void get_withNetworkException() throws Exception {
        String returnedBody;
        given: {
            when(restOperations.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class))).thenThrow(RestClientException.class);
        }
        when: {
            returnedBody = networkHandle.get("url", String.class, new HttpHeaders());
        }
        then: {
            assertThat(returnedBody).isNull();
        }
    }
}