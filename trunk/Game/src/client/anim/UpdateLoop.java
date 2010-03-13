/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.anim;

import java.util.Vector;

/**
 *
 * @author sanjul00
 */
public class UpdateLoop implements Runnable{
    private Thread thread;
    private int ups;
    private double period;
    private Vector<UpdateObject> list;
    private boolean paused;
    private long curTime;
    private int curUPS;
    private double speedfactor;

    public UpdateLoop(int ups) {
        setUPS(ups);
        list = new Vector<UpdateObject>();
        thread  = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    private void update() {
        for(UpdateObject uo : list) {
            uo.update(this);
        }
    }

    public void run() {
        long lastTime = System.nanoTime();
        double timeDiff;
        double sleepTime;
        curTime = System.nanoTime();
        double sumDiff;

        while(Thread.currentThread() == thread) {
            if(paused) {
                try {
                    Thread.yield();
                } catch(Exception e) {

                }

                continue;
            }

            update();

            timeDiff = (System.nanoTime() - curTime) * 0.000001;
            sumDiff = (curTime - lastTime) * 0.000001;
            speedfactor = sumDiff / period;
            curUPS = (int)(1000.0d / sumDiff);
            sleepTime = period - timeDiff;

            if(sleepTime > 1) {
                try {
                    Thread.sleep((int)sleepTime);
                } catch(InterruptedException e) {

                }
            }

            lastTime = curTime;
            curTime = System.nanoTime();
        }
    }

    public void setUPS(int ups) {
        this.ups = ups;
        period = 1000.0d / ups;
    }
    
    public int getUPS() {
        return ups;
    }

    public int getCurrentUPS() {
        return curUPS;
    }

    public void addUpdateObject(UpdateObject u) {
        list.add(u);
    }

    public UpdateObject removeUpdateObject(UpdateObject u) {
        return list.remove(list.indexOf(u));
    }

    public double getSpeedfactor() {
        return speedfactor;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPriority(int priority) {
        thread.setPriority(priority);
    }

    public int getPriority() {
        return thread.getPriority();
    }
}
