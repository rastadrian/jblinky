package com.rastadrian.jblinky.devices.usb.specs;

import com.rastadrian.jblinky.core.light.LightState;
import com.rastadrian.jblinky.devices.usb.UsbCommunicationsHandler;
import com.rastadrian.jblinky.devices.usb.UsbLight;
import com.rastadrian.jblinky.devices.usb.UsbSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

/**
 * The WebMail Notifier is an USB light sold by DreamCheeky
 *
 * @author Adrian Pena
 */
@Slf4j
@UsbSpecification(vendorId = 0x1d34, productId = 0x0004)
public class WebMailNotifier extends UsbLight {

    @Override
    public void setHandler(UsbCommunicationsHandler handler) {
        super.setHandler(handler);
        communicate(new byte[]{0x1f, 0x02, 0x00, 0x2e, 0x00, 0x00, 0x2b, 0x03});
        communicate(new byte[]{0x00, 0x02, 0x00, 0x2e, 0x00, 0x00, 0x2b, 0x04});
        communicate(new byte[]{0x00, 0x00, 0x00, 0x2e, 0x00, 0x00, 0x2b, 0x05});
    }

    @Override
    public String getId() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void success() {
        if (getState() == LightState.SUCCESS) {
            LOGGER.debug("Light already on SUCCESS state, skipping.");
            return;
        }
        super.success();
        sendColor(new byte[]{0x00, (byte) 0xFF, 0x00});
    }

    @Override
    public void failure() {
        super.failure();
        sendColor(new byte[]{(byte) 0xFF, 0x00, 0x00});
    }

    @Override
    public void inProgress() {
        super.inProgress();
        sendColor(new byte[]{0x00, 0x00, (byte) 0xFF});
    }

    @Override
    public void warning() {
        super.warning();
        sendColor(new byte[]{(byte) 0xFF, 0x2A, 0x00});
    }

    @Override
    public void off() {
        super.off();
        sendColor(new byte[]{0x00, 0x00, 0x00});
    }

    @Override
    public byte getRequestType() {
        return 0x21;
    }

    @Override
    public byte getRequest() {
        return 0x09;
    }

    @Override
    public short getValue() {
        return (3 << 8) | 1;
    }

    @Override
    public short getIndex() {
        return 0;
    }

    @Override
    public long getTimeout() {
        return 0;
    }

    private void sendColor(byte[] color) {
        byte[] suffix = new byte[]{0x00, 0x00, 0x00, 0x00, 0x05};
        communicate(ArrayUtils.addAll(color, suffix));
    }
}
