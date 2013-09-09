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
    public void testGetToken() throws Exception {
        this.anlizadorLexico.setBuffer("1if tamanio = 10;\nvar casa;/* Un comentario \n multilinea */;");
        while(this.anlizadorLexico.hasNext()) {
            Token token = this.anlizadorLexico.getNextToken();
            System.out.println("Token : '" + token + "'");
        }
        //this.anlizadorLexico.printSimbolos();
    }

    @Test
    public void testClasificacionEntrada() {
        
    }
}