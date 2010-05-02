package common.engine;

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
    private Vector<UpdateCountdownObject> countList;
    private Vector<UpdateCountdown> count;
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
        countList = new Vector<UpdateCountdownObject>();
        count = new Vector<UpdateCountdown>();
        thread  = new Thread(this);
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

    private void countdown() {
        for(UpdateCountdown o : count) {
            /*long end = o.getStartTime() + o.getIntervall() * 1000000;
            long left = (long)((end - curTime) * 0.000001);*/
            long cur = System.currentTimeMillis();
            long end = o.getStartTime() + o.getIntervall();
            long left = end - cur;
            o.setTimeLeft(left);

            if(left <= 0) {
                for(UpdateCountdownObject c : countList) {
                    c.countdown(o);
                }
                o.setStartTime(cur);
            }
        }
    }

    public void run() {
        curTime = System.nanoTime();
        long lastTime = System.nanoTime();
        double timeDiff;
        double sleepTime;
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
            countdown();

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

    public void addUpdateCountdownObject(UpdateCountdownObject o) {
        countList.add(o);
    }

    public void addUpdateCountdown(UpdateCountdown o) {
        count.add(o);
        o.setStartTime(System.currentTimeMillis());
    }

    /**
     *
     * @param u
     * @return
     */
    public UpdateObject removeUpdateObject(UpdateObject u) {
        return list.remove(list.indexOf(u));
    }

    public UpdateCountdownObject removeUpdateCountdownObject(UpdateCountdownObject o) {
        return countList.remove(countList.indexOf(o));
    }

    public UpdateCountdown removeUpdateCountdown(UpdateCountdown o) {
        return count.remove(count.indexOf(o));
    }

    public void resetCountdown(UpdateCountdown o) {
        o.setStartTime(curTime);
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
