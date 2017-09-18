package io.flic.fliclib.javaclient;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import io.flic.fliclib.javaclient.enums.*;

/**
 * Button connection channel.
 *
 * Add this button connection channel to a {@link FlicClient} by executing {@link FlicClient#addConnectionChannel(ButtonConnectionChannel)}.
 * You may only have this connection channel added to one {@link FlicClient} at a time.
 */
public class ButtonConnectionChannel {
    private static AtomicInteger nextId = new AtomicInteger();
    int connId = nextId.getAndIncrement();

    FlicClient client;

    private Bdaddr bdaddr;
    private LatencyMode latencyMode;
    private short autoDisconnectTime;
    Callbacks callbacks;

    final Object lock = new Object();

    /**
     * Create a connection channel using the specified parameters.
     *
     * Add this button connection channel to a {@link FlicClient} by executing {@link FlicClient#addConnectionChannel(ButtonConnectionChannel)}.
     *
     * @param bdaddr
     * @param latencyMode
     * @param autoDisconnectTime Number of seconds (0 - 511) until disconnect if no button event happens. 512 disables this feature.
     * @param callbacks
     */
    public ButtonConnectionChannel(Bdaddr bdaddr, LatencyMode latencyMode, short autoDisconnectTime, Callbacks callbacks) {
        if (bdaddr == null) {
            throw new IllegalArgumentException("bdaddr is null");
        }
        if (latencyMode == null) {
            throw new IllegalArgumentException("latencyMode is null");
        }
        if (callbacks == null) {
            throw new IllegalArgumentException("callbacks is null");
        }
        this.bdaddr = bdaddr;
        this.latencyMode = latencyMode;
        this.autoDisconnectTime = autoDisconnectTime;
        this.callbacks = callbacks;
    }

    /**
     * Create a connection channel using the specified parameters.
     *
     * Add this button connection channel to a {@link FlicClient} by executing {@link FlicClient#addConnectionChannel(ButtonConnectionChannel)}.
     *
     * @param bdaddr
     * @param callbacks
     */
    public ButtonConnectionChannel(Bdaddr bdaddr, Callbacks callbacks) {
        this(bdaddr, LatencyMode.NormalLatency, (short)0x1ff, callbacks);
    }

    /**
     * Get the {@link FlicClient} for this {@link ButtonConnectionChannel}.
     *
     * @return
     */
    public FlicClient getFlicClient() {
        return client;
    }

    public Bdaddr getBdaddr() {
        return bdaddr;
    }

    public LatencyMode getLatencyMode() {
        return latencyMode;
    }

    public short getAutoDisconnectTime() {
        return autoDisconnectTime;
    }

    /**
     * Applies new latency mode parameter.
     *
     * @param latencyMode
     */
    public void setLatencyMode(LatencyMode latencyMode) throws IOException {
        if (latencyMode == null) {
            throw new IllegalArgumentException("latencyMode is null");
        }
        synchronized (lock) {
            this.latencyMode = latencyMode;

            FlicClient cl = client;
            if (cl != null) {
                CmdChangeModeParameters pkt = new CmdChangeModeParameters();
                pkt.latencyMode = latencyMode;
                pkt.autoDisconnectTime = autoDisconnectTime;
                cl.sendPacket(pkt);
            }
        }
    }

    /**
     * Applies new auto disconnect time parameter.
     *
     * @param autoDisconnectTime Number of seconds (0 - 511) until disconnect if no button event happens. 512 disables this feature.
     */
    public void setAutoDisconnectTime(short autoDisconnectTime) throws IOException {
        if (latencyMode == null) {
            throw new IllegalArgumentException("latencyMode is null");
        }
        synchronized (lock) {
            this.autoDisconnectTime = autoDisconnectTime;

            FlicClient cl = client;
            if (cl != null) {
                CmdChangeModeParameters pkt = new CmdChangeModeParameters();
                pkt.latencyMode = latencyMode;
                pkt.autoDisconnectTime = autoDisconnectTime;
                cl.sendPacket(pkt);
            }
        }
    }

}
