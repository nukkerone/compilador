/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sintacticoTest;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sintactico.*;
import compilador.*;
import lexico.*;

/**
 *
 * @author Gabriel
 */
public class ParserTest {
    
    public ParserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }

    @Test
    public void testParser() {
        Parser p = new Parser(true);
        AnalizadorLexico al = new AnalizadorLexico();
        al.setBuffer("int");
        p.addAnalizadorLexico(al);
        int resultado = p.parse();
        System.out.println("Resultado: " + resultado);
    }

   
}