package com.rastadrian.jblinky.probes.cctray;

import com.rastadrian.jblinky.probes.TestUtil;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 9/20/16.
 *
 * @author Adrian Pena
 */
public class CCTrayParserTest {

    @Test
    public void parseCCTray() throws Exception {
        String jenkinsCCTray = TestUtil.readResource(this, "/cctray/JenkinsCCTray.xml");
        CCTray ccTray = CCTrayParser.parseCCTray(jenkinsCCTray);
        assertThat(ccTray).isNotNull();
        assertThat(ccTray.getProjects()).isNotNull();
        assertThat(ccTray.getProjects().size()).isEqualTo(4);
    }
}