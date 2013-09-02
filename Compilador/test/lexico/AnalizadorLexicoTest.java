/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Gabriel
 */
public class AnalizadorLexicoTest {
    private AnalizadorLexico anlizadorLexico = new AnalizadorLexico();
    
    public AnalizadorLexicoTest() {
        this.anlizadorLexico = new AnalizadorLexico();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }

    @Test
    public void testCargarGrafo() {
    }

    @Test
    public void testGetToken() {
        try {
            this.anlizadorLexico.getToken("dsgdfdf");
        } catch( Exception e) {
            System.out.println("Exception " + e);
        }
    }

    @Test
    public void testClasificacionEntrada() {
        
    }
}