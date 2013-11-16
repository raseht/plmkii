package com.idg.plserver;

class Avatar extends Sprite {
    public Avatar(String map,int x,int y,String name,String model) {
        super(map,x,y,name,model);
    }
    public String toString() {
        return "| "+getName()+"\t| "+getMap()+"\t| "+getX()+", "+getY()+"\t| "+getModel()+"\t|";
    }
}