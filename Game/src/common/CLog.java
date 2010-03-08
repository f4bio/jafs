/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author miracle
 */
public class CLog {
    private static BufferedWriter bw = null;

    public static void init(String file) {
        try {
            bw = new BufferedWriter(new FileWriter(file));
        } catch(FileNotFoundException e) {

        } catch(IOException e) {

        }
    }

    public static void log(String str) {
        if(bw != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
            String timeStamp = sdf.format(new Date());

            try {
                bw.write(timeStamp + " " + str);
                bw.newLine();
                bw.flush();
            } catch(FileNotFoundException e) {

            } catch(IOException e) {

            }
        }
    }

    public static void close() {
        if(bw != null) {
            try {
                bw.close();
                bw = null;
            } catch(IOException e) {

            }
        }
    }

    public static boolean isInitialized() {
        if(bw != null)
            return true;
        return false;
    }
}
