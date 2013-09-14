/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import filereader.SourceCode;
import herramientaerror.EventoError;
import java.io.*;
import java.util.*;
import lexico.AnalizadorLexico;
import sintactico.Parser;

/**
 *
 * @author Gabriel
 */
public class Compilador {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        /*SourceCode s = new SourceCode("D:\\Java Projects\\TestApp\\files\\source.txt");
        s.generateSource();
        Iterator sourceIterator = s.createIterator();
        System.out.println(s.getAsString());*/
        
        CompiladorManager c = new CompiladorManager();
        c.start();
        
    }
    
    
}
