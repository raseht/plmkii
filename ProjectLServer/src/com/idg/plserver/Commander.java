package com.idg.plserver;

import java.io.*;
import java.util.Arrays;

class Commander implements Runnable {
    private static final String[] COMMANDS = {"show clients","show avatars","show help","shutdown"};

    public void run() {
        Arrays.sort(COMMANDS);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
            String command = "";
            writer.write("worldserver> ");
            writer.flush();
            while(true) {
                command = reader.readLine();
                if(Arrays.binarySearch(COMMANDS, command)>=0) {
                    //System.err.println(command);
                    writer.write(ChatServer.runCommand(command));
                    writer.newLine();
                    writer.write("worldserver> ");
                    writer.flush();
                }
                else {
                    writer.write("worldserver> ");
                    writer.flush();
                }
            }
        }
        catch (IOException ioe) { System.err.println("Failed to read command."); }
    }
}