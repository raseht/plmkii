/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idg.plserver;

import java.net.InetAddress;

/**
 * Maintains the state of a UDP stateless connection.
 * 
 * @author Beau Hahn
 */
public class UDPConnection {
    private int srcport;
    private int dstport;
    private InetAddress dstaddr;
    private InetAddress srcaddr;
    
    public void receive() {
        
    }
    
    public void transmit(byte[] payload) {
        
    }
    
    UDPConnection(InetAddress srcaddr,int srcport, InetAddress dstaddr,int dstport) {
        this.srcaddr = srcaddr;
        this.srcport = srcport;
        this.dstaddr = dstaddr;
        this.dstport = dstport;
    }
}
