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
    private int period;
    private Vector<UpdateObject> list;
    private boolean paused;
    private long curTime;

    public UpdateLoop(int ups) {
        setUPS(ups);
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
        long lastTime;
        int timeDiff;

        while(Thread.currentThread() == thread) {
            if(paused)
                continue;

            lastTime = System.nanoTime();

            update();

            curTime = System.nanoTime();
            timeDiff = (int)((curTime - lastTime) / 1000000.0d);

            try {
                Thread.sleep(period - timeDiff);
            } catch(InterruptedException e) {

            }
        }
    }

    public void setUPS(int ups) {
        this.ups = ups;
        period = 1000 / ups;
    }
}
