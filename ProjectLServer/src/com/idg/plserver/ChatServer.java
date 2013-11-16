package com.idg.plserver;

import java.net.*;
import java.io.*;
import java.util.*;
/**
 * @author Beau Hahn
 * @version 0.2
 * */
public class ChatServer {
    private static final String REVISION = "/version:PLTP 0 Rev 5";
    private static ServerSocket ss;
    private static ClientManager cm = new ClientManager();
    private static AnimationManager am = new AnimationManager();
    /**
     * Does something.
     *
     * @param   args    blah
     * */
    public static void main(String[] args) {
        Thread waiter = new Thread(new NetworkPoller());
        Thread cmgr = new Thread(cm);
        Thread mmgr = new Thread(am.getMotionClock());
        Thread umgr = new Thread(am.getUpdateClock());
        Thread fmgr = new Thread(am.getFrameClock());
        Thread cmdr = new Thread(new Commander());
        cmdr.start();
        waiter.start();
        cmgr.start();
        mmgr.start();
        umgr.start();
        fmgr.start();
    }

    public static String runCommand(String command) {
        ArrayList<ClientManager.Client> clients = cm.getClientList();
        String output = "";
        if(command.equals("show clients")) {
            for(int a=0;a<clients.size();a++) {
                output += clients.get(a).toString()+"\n";
            }
            if(output.isEmpty()) output = "No clients are connected.";
            else output = "| Alias\t| IP Address\t| Avatar\t| Latency\t|\n"+output;
        }
        else if(command.equals("show avatars")) {
            for(int a=0;a<ClientManager.SpriteRegistry.size();a++) {
                output += ((Avatar)ClientManager.SpriteRegistry.get(a)).toString()+"\n";
            }
            if(output.isEmpty()) output = "No avatars exist.";
            else output = "| Name\t\t| Map\t\t| Location\t| Model\t\t|\n"+output;
        }
        else if(command.equals("shutdown")) {
            Iterator<ClientManager.Client> iterator = cm.getClientList().iterator();
            while(iterator.hasNext()) {
                iterator.next().cleanup();
            }
            System.exit(0);
        }
        else if(command.equals("show help")) {
            output = "\n-----Status Commands-----\n"+
                     "show avatars\tDisplays all avatars online on this server.\n"+
                     "show clients\tDisplays all client machines connected to this server.\n"+
                     "show help\tDisplays this screen.\n"+
                     "\n-----Operational Commands-----\n"+
                     "debugging\tEnables display of debugging output.\n"+
                     "shutdown\tCleans up all connections and halts this server.\n";
                     
        }
        return output;
    }

        /**
         * Function to write to a socket and remove sockets and streams that
         * throw exceptions from the iterated array.
         *
         * @param   i   the index of the Socket to write to
         * @param   s   the string to write to the socket
         * @return      true if no exception is thrown, false otherwise
         * */

    public static void broadcastAvatars() {
        cm.broadcastAvatars();
    }

    public static AnimationManager getAnimationManager() {
        return am;
    }
}