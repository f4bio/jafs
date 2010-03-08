/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.resource;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 *
 * @author miracle
 */
public class CArchive {
    private Hashtable<String, Integer> m_sizes = new Hashtable<String, Integer>();
    private Hashtable<String, byte[]> m_conContents = new Hashtable<String, byte[]>();
    private String m_conFileName;

    public CArchive(String conFile) {
        this.m_conFileName = conFile;
        initialize();
    }

    public byte[] getFileData(String file) {
        return m_conContents.get(file);
    }

    public String[] getContent() {
        String[] result = new String[m_conContents.size()];
        Enumeration keys = m_conContents.keys();
        int i=0;

        while(keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            result[i] = key;
            i++;
        }

        return result;
    }

    public boolean contains(String file) {
        Enumeration keys = m_conContents.keys();

        while(keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            if(key.equals(file))
                return true;
        }

        return false;
    }

    private void initialize() {
        ZipFile zf = null;
        try {
            zf = new ZipFile(m_conFileName);
            Enumeration e = zf.entries();

            while (e.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) e.nextElement();
                if(ze.isDirectory())
                    continue;
                m_sizes.put(ze.getName(), new Integer((int)ze.getSize()));
            }

            zf.close();
            zf = null;
            FileInputStream fis = new FileInputStream(m_conFileName);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ZipInputStream zis = new ZipInputStream(bis);
            ZipEntry ze = null;

            while ((ze = zis.getNextEntry()) != null) {
                if (ze.isDirectory())
                    continue;

                int size = (int)ze.getSize();
                if (size == -1) {
                    size = m_sizes.get(ze.getName());
                }
                byte[] b = new byte[size];
                int rb = 0;
                int chunk = 0;
                while ((size - rb) > 0) {
                    chunk = zis.read(b, rb, size - rb);
                    if (chunk == -1) {
                        break;
                    }
                    rb += chunk;
                }
                m_conContents.put(ze.getName(), b);
            }

            zis.close();
        } catch (NullPointerException e) {

        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }
}
