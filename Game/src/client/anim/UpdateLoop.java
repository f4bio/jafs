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
        //thread.setDaemon(true);
        thread.setName("UpdateLoop");
        thread.start();
    }

    private void update() {
        synchronized(list) {
            for(UpdateObject uo : list) {
                if(uo != null) {
                    uo.update(this);
                }
            }
        }
    }

    public void run() {
        long lastTime = System.nanoTime();
        double timeDiff;
        double sleepTime;
        curTime = System.nanoTime();
        double sumDiff;

        while(Thread.currentThread() == thread) {
            while(paused) {
                synchronized(thread) {
                    try {
                        thread.wait();
                    } catch(Exception e) {

                    }
                }
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

    public long getCurrentTime() {
        return curTime;
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

    public void pause() {
        paused = true;
    }

    public void wakeUp() {
        if(paused) {
            paused = false;
            synchronized(thread) {
                thread.notify();
            }
        }
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
