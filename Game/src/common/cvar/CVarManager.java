/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.cvar;

import java.util.Hashtable;

/**
 *
 * @author miracle
 */
public class CVarManager {
    private static Hashtable<String, CVar> list = new Hashtable<String, CVar>();

    public static boolean registerCvar(String key, CVar value) {
        CVar last = null;

        if((last = list.put(key, value)) != null) {
            list.put(key, last);
            return false;
        }

        return true;
    }

    public static <T extends java.lang.Comparable<T>> 
            boolean registerCVar(String key, T standard, T max, T min, boolean ro)
            throws Exception {

        CVar<T> nValue = new CVar<T>(standard, max, min, ro);
        CVar last = null;

        if((last = list.put(key, nValue)) != null) {
            list.put(key, last);
            return false;
        }

        return true;
    }

    public static boolean setValueOf(String key, Comparable value) throws Exception {
        if(!list.containsKey(key))
            return false;

        CVar c = list.get(key);
        c.setValue(value);
        
        return true;
    }

    public Comparable getValueOf(String key) {
        if(!list.containsKey(key))
            return null;

        return list.get(key).getValue();
    }

    public String getDatatypeOf(String key) {
        if(!list.containsKey(key))
            return null;

        return list.get(key).getDatatype();
    }
}
