/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idg.plserver;

/**
 *
 * @author Beau Hahn
 */
public class LoggerFactory {
    public static Logger getStdErrLogger() {
        return StdErrLogger.getInstance();
    }
    private LoggerFactory() {}
}
