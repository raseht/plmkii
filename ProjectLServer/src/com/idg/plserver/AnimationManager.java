package com.idg.plserver;

import java.util.ArrayList;

class AnimationManager {
    private MotionClock motion = new MotionClock();
    private UpdateClock update = new UpdateClock();
    private FrameClock frame = new FrameClock();

    public MotionClock getMotionClock() {
        return motion;
    }

    public UpdateClock getUpdateClock() {
        return update;
    }

    public FrameClock getFrameClock() {
        return frame;
    }

    public class MotionClock implements Runnable {
        private final int CLOCK = 17;
        public void run () {
            while (true) {
                long start = System.nanoTime();
                for(int a=0;a<ClientManager.SpriteRegistry.size();a++) {
                    ClientManager.SpriteRegistry.get(a).move();
                }
                int stop = ((int)((System.nanoTime() - start)/1000000l));
                try { Thread.sleep(CLOCK - (stop % CLOCK)); }
                catch (InterruptedException ie) {}
            }
        }
    }

    public class UpdateClock implements Runnable {
        private int tminus = 30;
        public UpdateClock() {}
        public void run() {
            while(true) {
                ChatServer.broadcastAvatars();
                try {
                    for(tminus=30;tminus>0;tminus--) {
                        Thread.sleep(17);
                    }
                }
                catch (InterruptedException ie) {}
            }
        }
        public int getTMinus() {
            return tminus;
        }
    }
    public class FrameClock implements Runnable {
        private int frames = 0;

        public void run() {
            while(true) {
                try {
                    Thread.sleep(17);
                    frames = frames+1;
                    //System.err.println(frames);
                }
                catch(InterruptedException ie) {}
            }
        }

        public int getFrames() {
            return frames;
        }
    }
}