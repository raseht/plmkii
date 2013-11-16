/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.idg.plserver;

import java.net.DatagramSocket;
import java.net.Socket;

/**
 * This class represents the client application as seen by the server. 
 * Information such as remote port, latency, version, and state information is
 * stored here. All communications to a client may be handled by this class.
 * @author Beau Hahn
 */
public class Client {
    private DatagramSocket socket = null;
    private long lastRx = 0;
    private long lastTx = 0;
    private int version = 0;

    protected void send() {
        this.lastTx = System.nanoTime();
    }

    protected String receive() {
        this.lastRx = System.nanoTime();
        return "";
    }

    protected Client(DatagramSocket socket) {
        this.socket = socket;
    }
}
