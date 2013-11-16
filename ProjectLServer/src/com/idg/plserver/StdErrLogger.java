/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.idg.plserver;

/**
 *
 * @author bhahn
 */
public class StdErrLogger extends Logger {
    private static StdErrLogger instance;
    
    public static void createInstance() {
        if(instance==null) instance = new StdErrLogger();
    }
    
    public static StdErrLogger getInstance() {
        createInstance();
        return instance;
    }
    
    public void write(String message) {
        System.err.println(message);
    }
    

    StdErrLogger() {}
}
