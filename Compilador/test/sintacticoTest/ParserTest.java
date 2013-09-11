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
    private Parser p;
    AnalizadorLexico al;
    boolean debug;
    
    public ParserTest() {
        this.debug = true;
        this.p = new Parser(this.debug);
        this.al = new AnalizadorLexico();
        this.p.addAnalizadorLexico(this.al);
    }
    
    @BeforeClass
    public static void setUpClass() {
    }

    @Test
    public void testParser() {
        
    }
    
    /**
     * Prepara un nuevo parser con la cadena especificada
     * 
     * @param buffer 
     */
    private void prepareParser(String buffer) {
        this.al.setBuffer(buffer);
        this.p = new Parser(this.debug);
        this.p.addAnalizadorLexico(this.al);
    }
    
    @Test
    public void testDeclaracionesVariables() {
        /*
        this.prepareParser("int peso;");
        assertTrue("Declaracion correcta no pasó el analizador sintáctico", this.p.parse() == 0);
        
        this.prepareParser("int ;");
        assertTrue("Declaración mal armada pasó el analizador sintactico", this.p.parse() != 0);
        
        this.prepareParser(";");
        assertTrue("Declaración mal armada pasó el analizador sintactico", this.p.parse() != 0);
        
        this.prepareParser("string a;");
        assertTrue("Declaración (STRING) bien armada falló", this.p.parse() == 0);
        
        this.prepareParser("int peso, casa;");
        assertTrue("Declaración válida de multiple variables falló", this.p.parse() == 0);
        */
    }
    
    @Test
    public void testDeclaracionesFunciones() {
        
        /*this.prepareParser("function peso()");
        assertTrue("Declaracion de function correcta no pasó el analizador sintáctico", this.p.parse() == 0);
        
        this.prepareParser("function getColor(string color)");
        assertTrue("Declaracion de function correcta no pasó el analizador sintáctico", this.p.parse() == 0);
        */
    }
    
    @Test
    public void testIf() {
        this.prepareParser("if (valor > 10) { valor = 5; }");
        assertTrue("Declaracion de function correcta no pasó el analizador sintáctico", this.p.parse() == 0);
                
    }

   
}