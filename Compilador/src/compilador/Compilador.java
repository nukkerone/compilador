/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import filereader.SourceCode;
import java.util.Iterator;

/**
 *
 * @author Gabriel
 */
public class Compilador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SourceCode s = new SourceCode("D:\\Java Projects\\TestApp\\files\\source.txt");
        s.generateSource();
        Iterator sourceIterator = s.createIterator();
        String text = "";
        while( sourceIterator.hasNext() ) {
            text += (String)sourceIterator.next() + " ";
        }
        System.out.println(text);
    }
}
