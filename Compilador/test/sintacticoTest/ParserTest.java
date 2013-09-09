/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sintacticoTest;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import compilador.*;
import lexico.*;
import sintactico.*;

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
        al.setBuffer("int peso;");
        p.addAnalizadorLexico(al);
        p.run();
        //System.out.println("Resultado: " + result);
    }

   
}