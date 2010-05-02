package client.anim;

import java.util.Vector;

/**
 *
 * @author J.A.F.S
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

    /**
     *
     * @param ups
     */
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

    /**
     *
     * @param ups
     */
    public void setUPS(int ups) {
        this.ups = ups;
        period = 1000.0d / ups;
    }
    
    /**
     *
     * @return
     */
    public int getUPS() {
        return ups;
    }

    /**
     *
     * @return
     */
    public int getCurrentUPS() {
        return curUPS;
    }

    /**
     *
     * @return
     */
    public long getCurrentTime() {
        return curTime;
    }

    /**
     *
     * @param u
     */
    public void addUpdateObject(UpdateObject u) {
        list.add(u);
    }

    /**
     *
     * @param u
     * @return
     */
    public UpdateObject removeUpdateObject(UpdateObject u) {
        return list.remove(list.indexOf(u));
    }

    /**
     *
     * @return
     */
    public double getSpeedfactor() {
        return speedfactor;
    }

    /**
     *
     */
    public void pause() {
        paused = true;
    }

    /**
     *
     */
    public void wakeUp() {
        if(paused) {
            paused = false;
            synchronized(thread) {
                thread.notify();
            }
        }
    }

    /**
     *
     * @return
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     *
     * @param priority
     */
    public void setPriority(int priority) {
        thread.setPriority(priority);
    }

    /**
     *
     * @return
     */
    public int getPriority() {
        return thread.getPriority();
    }
}
