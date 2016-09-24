package com.rastadrian.jblinky.probes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * A Network utility class to encapsulate the usage of Spring's Rest Template.
 *
 * @author Adrian Pena
 */
public class NetworkUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkUtil.class);
    private static final int NETWORK_TIME_OUT_MILLIS = 3000;

    /**
     * Creates headers including a Basic Authentication one.
     *
     * @param username the credential's username
     * @param password the credential's password
     * @return the HttpHeaders including an encoded Basic Auth header.
     */
    public static HttpHeaders createHeaders(final String username, final String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            String encodedAuth = Base64Utils.encodeToString(auth.getBytes());
            String authHeader = "Basic " + encodedAuth;
            set("Authorization", authHeader);
        }};
    }

    /**
     * Performs a GET request to the URL using the provided headers, it will return the response deserialized to the
     * requested response class.
     *
     * @param url           the URL to get.
     * @param responseClass the class to serialize the response to.
     * @param headers       the request headers.
     * @param <T>           the class to serialize the response to.
     * @return the de-serialized response.
     */
    public static <T> T get(String url, Class<T> responseClass, HttpHeaders headers) {
        try {
            RestTemplate template = new RestTemplate();
            SimpleClientHttpRequestFactory factory = (SimpleClientHttpRequestFactory) template.getRequestFactory();
            factory.setConnectTimeout(NETWORK_TIME_OUT_MILLIS);
            factory.setReadTimeout(NETWORK_TIME_OUT_MILLIS);
            return template.exchange(url, HttpMethod.GET, new HttpEntity(headers), responseClass).getBody();
        } catch (RestClientException e) {
            LOGGER.info("A RestClient exception occurred while attempting a GET request.", e);
        }
        return null;
    }
}
