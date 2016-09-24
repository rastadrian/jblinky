package com.rastadrian.jblinky.devices;

import com.rastadrian.jblinky.core.usb.UsbRegistry;
import com.rastadrian.jblinky.core.usb.light.UsbLight;
import org.apache.commons.lang3.ArrayUtils;

/**
 * The WebMail Notifier is an USB light sold by DreamCheeky
 *
 * @author Adrian Pena
 */
@UsbRegistry(vendorId = 0x1d34, productId = 0x0004)
public class WebMailNotifier extends UsbLight {

    public WebMailNotifier() {
        sendMessage(new byte[]{0x1f, 0x02, 0x00, 0x2e, 0x00, 0x00, 0x2b, 0x03});
        sendMessage(new byte[]{0x00, 0x02, 0x00, 0x2e, 0x00, 0x00, 0x2b, 0x04});
        sendMessage(new byte[]{0x00, 0x00, 0x00, 0x2e, 0x00, 0x00, 0x2b, 0x05});
    }

    public void success() {
        sendColor(new byte[]{0x00, (byte) 0xFF, 0x00});
    }

    public void failure() {
        sendColor(new byte[]{(byte) 0xFF, 0x00, 0x00});
    }

    public void inProgress() {
        sendColor(new byte[]{0x00, 0x00, (byte) 0xFF});
    }

    public void warning() {
        sendColor(new byte[]{(byte) 0xFF, 0x2A, 0x00});
    }

    public void off() {
        sendColor(new byte[]{0x00, 0x00, 0x00});
    }

    protected byte getRequestType() {
        return 0x21;
    }

    protected byte getRequest() {
        return 0x09;
    }

    protected short getValue() {
        return (3 << 8) | 1;
    }

    protected short getIndex() {
        return 0;
    }

    protected long getTimeout() {
        return 0;
    }

    private void sendColor(byte[] color) {
        byte[] suffix = new byte[]{0x00, 0x00, 0x00, 0x00, 0x05};
        sendMessage(ArrayUtils.addAll(color, suffix));
    }
}
