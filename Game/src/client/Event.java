/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import java.awt.Color;

/**
 *
 * @author miracle
 */
public class Event {
    public static final int EVENT_SYSTEM = 0;
    public static final int EVENT_RED = 1;
    public static final int EVENT_BLUE = 2;

    private Color c;
    private String msg;
    private int type;

    public Event(int type, String msg) {
        this.type = type;
        switch(type) {
            case EVENT_SYSTEM:
                c = Color.YELLOW;
                break;
            case EVENT_RED:
                c = Color.RED;
                break;
            case EVENT_BLUE:
                c = Color.BLUE;
                break;
            default:
                c = Color.YELLOW;
        }
        this.msg = msg;
    }

    public Color getColor() {
        return c;
    }

    public String getMsg() {
        return msg;
    }

    public int getType() {
        return type;
    }
}
