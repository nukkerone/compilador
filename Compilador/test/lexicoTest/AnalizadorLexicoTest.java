/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexicoTest;

import herramientaerror.EventoError;
import lexico.AnalizadorLexico;
import lexico.Token;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Gabriel
 */
public class AnalizadorLexicoTest {
    private AnalizadorLexico anlizadorLexico;
    
    public AnalizadorLexicoTest() {
        this.anlizadorLexico = new AnalizadorLexico(new EventoError());
    }
    
    @BeforeClass
    public static void setUpClass() {
    }

    @Test
    public void testCargarGrafo() {
    }

    @Test
    public void testGetToken() throws Exception {
	this.anlizadorLexico.setBuffer(" // hola \n int a;");     //   this.anlizadorLexico.setBuffer(" \"HOLA \" + \n +\" hpepe\" ;");

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