/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.resource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author miracle
 */
public class CConfig {
    public static final int ERROR_NONE = 0;
    public static final int ERROR_NOT_FOUND = 1;
    public static final int ERROR_UNABLE_TO_READ = 2;
    public static final int ERROR_UNABLE_TO_WRITE = 3;

    private File file;
    private Hashtable<String, Hashtable<String, String>> table;

    private Hashtable<String, String> tableTmp;
    private boolean started;
    private String tableKey;
    private String inLine;

    public CConfig() {
        initRead(null);
    }

    public CConfig(String file) {
        File f = new File(file);
        initRead(f);
        read(f);
    }

    public CConfig(File file) {
        initRead(file);
        read(file);
    }

    public CConfig(byte[] data) {
        initRead(null);
        read(data);
    }

    private void readLine() {
        inLine = inLine.trim();
        if ((inLine.length() > 2 && inLine.startsWith("[") && inLine.endsWith("]"))) {
            if(started) {
                if(!table.containsKey(tableKey)) {
                    table.put(tableKey, tableTmp);
                }
            } else {
                started = true;
            }

            tableKey = inLine.substring(1, inLine.indexOf("]"));
            tableKey = tableKey.trim();
            tableTmp = new Hashtable<String, String>();
        }

        if(inLine.length() > 1 && inLine.substring(1, inLine.length()).contains("=") && started) {
            String key = inLine.substring(0, inLine.indexOf("=")).trim();
            String value = inLine.substring(inLine.indexOf("=") + 1).trim();
            if(!tableTmp.containsKey(key)) {
                tableTmp.put(key, value);
            }
        }
    }

    private void initRead(File file) {
        this.table = new Hashtable<String, Hashtable<String, String>>();
        this.tableTmp = null;
        this.started = false;
        this.tableKey = null;
        this.inLine = null;
        this.file = file;
    }

    private int read(File file) {
        if(file == null)
            return ERROR_UNABLE_TO_READ;

        initRead(file);

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch(FileNotFoundException e) {
            return ERROR_NOT_FOUND;
        }

        try {
            while((inLine = reader.readLine()) != null) {
                readLine();
            }
            table.put(tableKey, tableTmp);
        } catch(Exception e) {
            try { reader.close(); } catch(IOException ex) { }
            return ERROR_UNABLE_TO_READ;
        }

        try { reader.close(); } catch(IOException ex) { }
        return ERROR_NONE;
    }

    private int read(byte[] data) {
        if(data == null)
            return ERROR_UNABLE_TO_READ;

        initRead(null);

        String content = "";
        for(int i=0; i<data.length; i++) {
            content += (char)data[i];
        }
        String[] line = content.split("\n");

        for(int i=0; i<line.length; i++) {
            inLine = line[i];
            readLine();
        }
        table.put(tableKey, tableTmp);

        return ERROR_NONE;
    }

    public int save() {
        if(file == null)
            return ERROR_NOT_FOUND;

        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(file));
        } catch(IOException e) {
            return ERROR_UNABLE_TO_WRITE;
        }

        Enumeration mainKeys = table.keys();

        try {
            while(mainKeys.hasMoreElements()) {
                String mKey = (String)mainKeys.nextElement();
                writer.write("[" + mKey + "]");
                writer.newLine();
                Hashtable subtable = table.get(mKey);
                Enumeration subKeys = subtable.keys();

                while(subKeys.hasMoreElements()) {
                    String sKey = (String)subKeys.nextElement();
                    writer.write(sKey + "=" + subtable.get(sKey));
                    writer.newLine();
                }

                writer.newLine();
                writer.flush();
            }
        } catch(IOException e) {
            try {
                writer.close();
            } catch(IOException ex) {

            }
            return ERROR_UNABLE_TO_WRITE;
        }

        try {
            writer.close();
        } catch(IOException ex) {

        }
        return ERROR_NONE;
    }

    public int save(String filePath) {
        if(filePath == null)
            return ERROR_NOT_FOUND;

        File prev = file;
        file = new File(filePath);
        int result = save();
        file = prev;
        return result;
    }

    public int save(File sFile) {
        if(sFile == null)
            return ERROR_NOT_FOUND;

        File prev = file;
        file = sFile;
        int result = save();
        file = prev;
        return result;
    }

    public String[] getSections() {
        String[] result = new String[table.size()];
        Enumeration keys = table.keys();
        int i=0;

        while(keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            result[i] = key;
            i++;
        }

        return result;
    }

    public String[] getVariablesOf(String section) {
        if(table.get(section) == null) 
            return null;

        String[] result = new String[table.get(section).size()];
        Enumeration keys = table.get(section).keys();
        int i=0;

        while(keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            result[i] = key;
            i++;
        }

        return result;
    }

    public void setValue(String section, String variable, Object value) {
        if(table.containsKey(section)) {
            table.get(section).remove(variable);
            table.get(section).put(variable, "" + value);
        } else {
            Hashtable<String, String> subtable = new Hashtable<String, String>();
            subtable.put(variable, "" + value);
            table.put(section, subtable);
        }
    }

    public String getValue(String section, String variable) {
        return table.get(section).get(variable);
    }

    public Integer getValueI(String key, String subkey) {
        Integer result = null;

        try {
            result = Integer.parseInt(table.get(key).get(subkey));
        } catch(Exception e) {
            result = null;
        }

        return result;
    }

    public Float getValueF(String key, String subkey) {
        Float result = null;

        try {
            result = Float.parseFloat(table.get(key).get(subkey));
        } catch(Exception e) {
            result = null;
        }

        return result;
    }

    @Override
    public String toString() {
        String result = "";
        Enumeration mainKeys = table.keys();

        while(mainKeys.hasMoreElements()) {
            String mKey = (String)mainKeys.nextElement();
            result += "[" + mKey + "]\n";
            Hashtable subtable = table.get(mKey);
            Enumeration subKeys = subtable.keys();

            while(subKeys.hasMoreElements()) {
                String sKey = (String)subKeys.nextElement();
                result += sKey + "=" + subtable.get(sKey) + "\n";
            }
        }

        return result;
    }
}
