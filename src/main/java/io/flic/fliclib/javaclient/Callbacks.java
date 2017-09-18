package io.flic.fliclib.javaclient;

import java.io.IOException;

import io.flic.fliclib.javaclient.enums.ClickType;
import io.flic.fliclib.javaclient.enums.ConnectionStatus;
import io.flic.fliclib.javaclient.enums.CreateConnectionChannelError;
import io.flic.fliclib.javaclient.enums.DisconnectReason;
import io.flic.fliclib.javaclient.enums.RemovedReason;

public class Callbacks {

    /**
     * Called when the server has received the create connection channel command.
     *
     * If createConnectionChannelError is {@link CreateConnectionChannelError#NoError}, other events will arrive until {@link #onRemoved} is received.
     * There will be no {@link #onRemoved} if an error occurred.
     *
     * @param channel
     * @param createConnectionChannelError
     * @param connectionStatus
     * @throws IOException
     */
    public void onCreateConnectionChannelResponse(ButtonConnectionChannel channel, CreateConnectionChannelError createConnectionChannelError, ConnectionStatus connectionStatus) throws IOException {
    }

    /**
     * Called when the connection channel has been removed.
     *
     * Check the removedReason to find out why. From this point, the connection channel can be re-added again if you wish.
     *
     * @param channel
     * @param removedReason
     * @throws IOException
     */
    public void onRemoved(ButtonConnectionChannel channel, RemovedReason removedReason) throws IOException {
    }

    /**
     * Called when the connection status changes.
     *
     * @param channel
     * @param connectionStatus
     * @param disconnectReason Only valid if connectionStatus is {@link ConnectionStatus#Disconnected}
     * @throws IOException
     */
    public void onConnectionStatusChanged(ButtonConnectionChannel channel, ConnectionStatus connectionStatus, DisconnectReason disconnectReason) throws IOException {
    }

    public void onButtonUpOrDown(ButtonConnectionChannel channel, ClickType clickType, boolean wasQueued, int timeDiff) throws IOException {
    }

    public void onButtonClickOrHold(ButtonConnectionChannel channel, ClickType clickType, boolean wasQueued, int timeDiff) throws IOException {
    }

    public void onButtonSingleOrDoubleClick(ButtonConnectionChannel channel, ClickType clickType, boolean wasQueued, int timeDiff) throws IOException {
    }

    public void onButtonSingleOrDoubleClickOrHold(ButtonConnectionChannel channel, ClickType clickType, boolean wasQueued, int timeDiff) throws IOException {
    }
}
