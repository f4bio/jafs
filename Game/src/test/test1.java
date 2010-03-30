/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

/**
 *
 * @author adm1n
 */
public class test1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String input = "/js localhost:30000";
        System.out.println(input.substring(input.indexOf(" ")+1,input.indexOf(":")));
        System.out.println(Integer.parseInt(input.substring(input.indexOf(":")+1)));
    }

}
