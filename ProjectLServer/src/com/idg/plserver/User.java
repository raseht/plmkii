/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.idg.plserver;

/**
 * This class defines active users of the server. Instances of this class can
 * be garbage-collected if no clients are active.
 * @author Beau Hahn
 */
public class User {
    private Client activeClient = null;

    protected boolean hasActiveClient() {
        if(this.activeClient==null) return false;
        return true;
    }

    protected User() {
        
    }
}
