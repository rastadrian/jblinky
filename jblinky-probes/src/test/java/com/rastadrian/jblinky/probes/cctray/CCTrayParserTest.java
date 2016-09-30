package com.rastadrian.jblinky.probes.cctray;

import com.rastadrian.jblinky.probes.TestUtil;
import com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParserFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created on 9/20/16.
 *
 * @author Adrian Pena
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SAXParserFactory.class, CCTrayParser.class})
public class CCTrayParserTest {

    @Test
    public void parseCCTray() throws Exception {
        String jenkinsCCTray = TestUtil.readResource(this, "/cctray/jenkins-cctray-all-successful.xml");
        CCTray ccTray = CCTrayParser.parseCCTray(jenkinsCCTray);
        assertThat(ccTray).isNotNull();
        assertThat(ccTray.getProjects()).isNotNull();
        assertThat(ccTray.getProjects().size()).isEqualTo(4);
    }

    @Test
    public void parseCCTray_withEmptyContent() throws Exception {
        CCTray ccTray;
        when:
        {
            ccTray = CCTrayParser.parseCCTray("");
        }
        then:
        {
            assertThat(ccTray).isNull();
        }
    }

    @Test
    public void parseCCTray_withParserError() throws Exception {
        CCTray ccTray;
        given:
        {
            SAXParserFactoryImpl saxParserFactory = spy(new SAXParserFactoryImpl());
            mockStatic(SAXParserFactory.class);
            when(SAXParserFactory.newInstance()).thenReturn(saxParserFactory);
            when(saxParserFactory.newSAXParser()).thenThrow(SAXException.class);
        }
        when:
        {
            ccTray = CCTrayParser.parseCCTray("{}");
        }
        then:
        {
            assertThat(ccTray).isNull();
        }
    }
}