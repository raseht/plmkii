/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idg.plserver;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author Beau Hahn
 */
public class NetworkPoller implements Runnable {

    public void run() {
        try {
            DatagramSocket ds = new DatagramSocket(37000, InetAddress.getLocalHost());
            byte[] udpbuffer = new byte[500];
            DatagramPacket dp = new DatagramPacket(udpbuffer, 500);
            long lastinput = System.nanoTime();
            while (true) {
                System.err.println("Waiting for data...");
                ds.receive(dp);
                System.err.println("Got a Datagram from " + dp.getAddress() + ":" + dp.getPort() + " Payload follows.");
                System.err.println(((System.nanoTime()-lastinput)/1000000l)+"ms since last datagram.");
                lastinput = System.nanoTime();
                char[] chars = new char[500];
                byte[] bytes = dp.getData();
                for (int i = 0; i < dp.getLength(); i++) {
                    chars[i] = (char) bytes[i];
                }
                System.err.println(String.valueOf(chars));
            }
        } catch (Exception e) {
            System.err.println("Cannot provision client socket.");
        }
    }
    
}
