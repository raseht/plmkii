/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.idg.plserver;

/**
 *
 * @author bhahn
 */
public abstract class Logger {
    public void logException(Exception e) {
        write(e.getMessage());
    }
    public void write(String s) {}
}
