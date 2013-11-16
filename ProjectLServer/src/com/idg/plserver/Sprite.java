package com.idg.plserver;

import java.util.*;

class Sprite {
    private String map = "";
    private int x = 0;
    private int y = 0;
    private String name = "";
    private String model = "";
    private boolean moving = false;
    private int facing = 0;
    private int motion = -1;

    public Sprite(String map,int x,int y,String name,String model) {
        this.map = map;
        this.x = x;
        this.y = y;
        this.name = name;
        this.model = model;
    }

    public String getString() {
        return "/avatar:"+getMap()+":"+getX()+":"+getY()+":"+getName()+":"+getModel();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public String getModel() {
        return model;
    }

    public String getMap() {
        return map;
    }

    public void move() {
        switch(motion) {
            case 1:
                y-=4;
                break;
            case 2:
                //System.err.println("Moving east...");
                x+=4;
                break;
            case 3:
                y+=4;
                break;
            case 4:
                x-=4;
            default:
                break;
        }
    }

    public void unmove() {
        switch(motion) {
            case 1:
                y+=4;
                break;
            case 2:
                //System.err.println("Moving east...");
                x-=4;
                break;
            case 3:
                y-=4;
                break;
            case 4:
                x+=4;
            default:
                break;
        }
    }

    public void setMotion(int i) {
        motion = i;
        System.err.println("Motion Direction "+Integer.toString(i));
    }
}