/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.engine;

/**
 *
 * @author miracle
 */
public class UpdateCountdown {
    private String name;
    private long startTime;
    private int intervall;
    private long timeLeft;

    public UpdateCountdown(String name, int intervall) {
        this.name = name;
        this.intervall = intervall;
        this.timeLeft = 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setIntervall(int intervall) {
        this.intervall = intervall;
    }

    public int getIntervall() {
        return intervall;
    }

    public long getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(long t) {
        timeLeft = t;
    }
}
