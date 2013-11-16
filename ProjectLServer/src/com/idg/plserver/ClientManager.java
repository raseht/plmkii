package com.idg.plserver;

import java.net.*;
import java.io.*;
import java.util.*;

class ClientManager implements Runnable {
    private ArrayList<Client> clients = new ArrayList<Client>();

    public void add(Socket socket,String alias) {
        String ip = ((Inet4Address)socket.getInetAddress()).getHostAddress();
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.clients.add(new Client(socket,reader,writer,ip,alias));
        }
        catch (IOException ioe) {
            System.err.println("Failed to open read/write stream for "+ip);
        }
    }

    public boolean cleanup(Client c) {
        c.cleanup();
        this.clients.remove(c);
        return true;
    }

    public void run() {
        try {
            while(true) {
                Thread.sleep(100);
                int x = clients.size();
                //System.err.println(x+" active connections.");
                for(int a=0;a<x;a++) {
                    String s = clients.get(a).read();
                    if(s == null) {
                        cleanup(clients.get(a));
                        x = clients.size();
                        s = "";
                    }
                    if(!s.equals("")) {
                        if(s.startsWith("/")) command(clients.get(a),s);
                        else broadcast(clients.get(a),s);
                    }
                }
            }
        }
        catch (Exception e) { System.err.println("You fucked up big time."); e.printStackTrace(); }
    }

    public void command(Client source,String s) {
        if(s.equals("/exit")) cleanup(source);
        if(s.startsWith("/version")) source.setVersion(s);
        if(s.startsWith("/alias")) source.setAlias(s);
        if(s.startsWith("/avatar")) source.setAvatar(s);
        if(s.startsWith("/move")) source.move(s);
        if(s.startsWith("/frameclock")) source.frameClock(s);
    }

    public void broadcast(Client source,String s) {
        int x = clients.size();
        for(int a=0;a<x;a++) {
            if(!clients.get(a).write(source,s)) {
                cleanup(clients.get(a));
                x = clients.size();
            }
        }
    }

    public void broadcastAvatars() {
        String s = "";
        for(int a=0;a<SpriteRegistry.size();a++) {
            s += SpriteRegistry.get(a).getString();
            //System.err.println(SpriteRegistry.get(a).getString());
        }
        for(int a=0;a<clients.size();a++) {
            clients.get(a).write(null,s);
        }
    }

    public ArrayList<Client> getClientList() {
        return clients;
    }

    public class Client {
        private Socket socket;
        private BufferedReader reader;
        private BufferedWriter writer;
        private String alias;
        private String ip;
        private String version;
        private boolean verified;
        private String avatar;
        private int latency;
        private int advanced;

        public Client(Socket socket,BufferedReader reader,BufferedWriter writer,String ip,String alias) {
            this.socket = socket;
            this.reader = reader;
            this.writer = writer;
            this.alias = alias;
            this.version = "";
            this.avatar = "";
            this.verified = false;
            this.latency = 0;
            this.ip = ((Inet4Address)socket.getInetAddress()).getHostAddress();
            for(int a=0;a<5;a++) {
                try {
                    Thread.sleep(150);
                }
                catch (Exception e) {}
                doFrameClock();
            }
        }

        public boolean isVerified() {
            return verified;
        }

        public boolean cleanup() {
            try {
                this.write(null,"/term");
                this.reader.close();
                this.writer.close();
                this.socket.close();
                SpriteRegistry.remove(avatar);
            }
            catch (Exception e) {
                System.err.println("Could not close socket "+this.ip+", removed from queue.");
            }
            return true;
        }

        public String getAlias() {
            return alias;
        }

        public int getLatency() {
            return latency;
        }

        public void frameClock(String s) {
            String[] fields = s.split(":");
            latency = (ChatServer.getAnimationManager().getFrameClock().getFrames() - Integer.parseInt(fields[1])/2);
            //System.err.println("Latency: "+latency+" frames.");
            doFrameClock();
        }

        public void doFrameClock() {
            write(null,"/frameclock:"+(ChatServer.getAnimationManager().getFrameClock().getFrames()+latency));
        }

        public void setAvatar(String s) {
            String[] fields = s.split(":");
            try {
                avatar = fields[4];
                SpriteRegistry.update(new Avatar(fields[1],Integer.parseInt(fields[2]),Integer.parseInt(fields[3]),fields[4],fields[5]));
                if(!alias.equals("") && !version.equals("") && !avatar.isEmpty()) verified = true;
            }
            catch (NumberFormatException nfe) {}
        }

        public void setAlias(String s) {
            if(verified) return;
            alias = s.substring(6);
            if(!alias.equals("") && !version.equals("") && !avatar.isEmpty()) verified = true;
        }

        public void setVersion(String s) {
            if(verified) return;
            if(verified) return;
            version = s.substring(8);
            if(!alias.equals("") && !version.equals("") && !avatar.isEmpty()) verified = true;
        }

        public String getAvatar() {
            return avatar;
        }

        public void move(String s) {
            String[] datastamp = s.split("\\" +
                    "|");
            System.err.println(datastamp[0]);
            String[] fields = datastamp[0].split(":");
            if(fields[1].equals("N")) {
                SpriteRegistry.gets(avatar).setMotion(1);
            }
            if(fields[1].equals("E")) {
                SpriteRegistry.gets(avatar).setMotion(2);
            }
            if(fields[1].equals("S")) {
                SpriteRegistry.gets(avatar).setMotion(3);
            }
            if(fields[1].equals("W")) {
                SpriteRegistry.gets(avatar).setMotion(4);
            }
            if(fields[1].equals("X")) {
                //for(int a=0;a<(ChatServer.getAnimationManager().getFrameClock().getFrames()-Integer.parseInt(datastamp[1])-advanced);a++) SpriteRegistry.gets(avatar).unmove();
                SpriteRegistry.gets(avatar).setMotion(0);
            }
            /*else {
                advanced = (Math.min(ChatServer.getAnimationManager().getUpdateClock().getTMinus(),(ChatServer.getAnimationManager().getFrameClock().getFrames()-Integer.parseInt(datastamp[1])%30)));
                for(int a=0;a<advanced;a++) SpriteRegistry.gets(avatar).move();
            } */
        }

        public boolean write(Client source,String s) {
            try {
                if(source == null) this.writer.write(s);
                else writer.write(source.getAlias()+" says, \""+s+"\"");
                this.writer.newLine();
                this.writer.flush();
                return true;
            }
            catch (IOException ioe) {
                System.err.println("Socket failed to write, remove from queue."); ioe.printStackTrace();
                return false;
            }
        }

        public String read() {
            try {
                if(this.reader.ready()) return this.reader.readLine();
            }
            catch (IOException ioe) {
                System.err.println("Socket "+this.ip+" failed to read, remove from queue.");
                return null;
            }
            return "";
        }

        public String toString() {
            return "| "+alias+"\t| "+ip+"\t| "+avatar+"\t| "+latency+" ("+latency*17+"ms)\t|";
        }
    }

    public static class SpriteRegistry {
        private static HashMap<String,Sprite> sprites = new HashMap<String,Sprite>();

        public static int size() {
            return sprites.size();
        }

        public static Sprite get(int i) {
            return sprites.get(sprites.keySet().toArray()[i]);
        }

        public static Sprite gets(String s) {
            return sprites.get(s);
        }

        public static void update(Sprite s) {
            sprites.put(s.getName(),s);
        }

        public static void remove(String s) {
            sprites.remove(s);
        }
    }
}
