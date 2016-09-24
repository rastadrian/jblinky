package com.rastadrian.jblinky.probes;

import org.apache.commons.io.IOUtils;

/**
 * Created on 9/20/16.
 *
 * @author Adrian Pena
 */
public class TestUtil {
    public static String readResource(Object test, String resource) throws Exception {
        return IOUtils.toString(test.getClass().getResourceAsStream(resource), "UTF-8");
    }
}
