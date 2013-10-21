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
import filereader.SourceCode;
import herramientaerror.EventoError;
import lexico.*;

/**
 *
 * @author Gabriel
 */
public class ParserTest {
    private Parser p;
    AnalizadorLexico al;
    boolean debug;
    EventoError eventoError;
    
    public ParserTest() {
        this.debug = true;
        this.eventoError = new EventoError();
        this.p = new Parser(this.debug);
        this.p.addEventoError(this.eventoError);
        this.al = new AnalizadorLexico(this.eventoError);
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
        this.p.addEventoError(this.eventoError);
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
        /*
        this.prepareParser("function peso(){}");
        assertTrue("Declaracion de function correcta no pasó el analizador sintáctico", this.p.parse() == 0);
        
        this.prepareParser("function getColor(string color){}");
        assertTrue("Declaracion de function (Parametros) correcta no pasó el analizador sintáctico", this.p.parse() == 0);
        
        this.prepareParser("function getColor(string color) { if (color > \"azul\") then valor = 10; }");
        assertTrue("Declaracion de function correcta no pasó el analizador sintáctico", this.p.parse() == 0);
        
        this.prepareParser("function getColor(string color) { if (color > \"azul\") then { valor = 10; } else valor = 5; }");
        assertTrue("Declaracion de function correcta no pasó el analizador sintáctico", this.p.parse() == 0);
        
        this.prepareParser("function getColor(string color) { if (color > \"azul\") then valor = 10; else valor = 5; }");
        assertTrue("Declaracion de function correcta no pasó el analizador sintáctico", this.p.parse() == 0);
        
        this.prepareParser("function getColor(string color) { if (color > \"azul\") then { valor = 10; } else { valor = 5; } }");
        assertTrue("Declaracion de function correcta no pasó el analizador sintáctico", this.p.parse() == 0);
        
        this.prepareParser("function getColor(string color) { return(auto); if (color > \"azul\") then { valor = 10; } else { valor = 5; } }");
        assertTrue("Declaracion de function (Con return) correcta no pasó el analizador sintáctico", this.p.parse() == 0);
        */
    }
    
    @Test
    public void testFunciones() {
        /*
        this.prepareParser("getColor();");
        assertTrue("Llamada a función correcta no pasó el analizador sintáctico", this.p.parse() == 0);
        */
    }
    
    @Test
    public void testIf() {
        /*
        this.prepareParser("if (valor > 10) then { valor = 5; } else { valor = 2; }");
        assertTrue("Sentencia IF completa y correcta no pasó el analizador sintáctico", this.p.parse() == 0);
        
        this.prepareParser("if (valor > 10) then { valor = 5; }");
        assertTrue("Sentencia IF sin ELSE correcta no pasó el analizador sintáctico", this.p.parse() == 0);
                
        this.prepareParser("if  then { valor = 5; }");
        assertTrue("Sentencia IF sin CONDICION (Incorrecta) pasó el analizador sintáctico", this.p.parse() != 0);
        
        this.prepareParser("if (valor > 10)");
        assertTrue("Sentencia IF sin Then ni bloque de ejecución (Incorrecta) pasó el analizador sintáctico", this.p.parse() != 0);
        */
    }
    
    @Test
    public void testBloques() {
        /*
        this.prepareParser("valor = 10;");
        assertTrue("Se definio un bloque correcto pero no pasó el analizador sintáctico", this.p.parse() == 0);
        
        this.prepareParser("color = \"rojo\";");
        assertTrue("Se definio un bloque (Asignación String) correcto pero no pasó el analizador sintáctico", this.p.parse() == 0);
        
        this.prepareParser("valor = 10; color = \"rojo\";");
        assertTrue("Bloque multiple correcto pero no pasó el analizador sintáctico", this.p.parse() == 0);   
        */
    }
    
    @Test
    public void testFor() {
        /*
        this.prepareParser("if (c == 10) then { for (i = 1; i > n ) { valor = 10; color = \"azul\"; if (a == b) then { value = 5; } } } else { }");
        assertTrue("Sentencia For correcta pero no pasó el analizador sintáctico", this.p.parse() == 0);   
        */
    }
    
    @Test
    public void testPrint() {
        /*
        this.prepareParser("print (\"casa\");");
        assertTrue("Sentencia Print correcta pero no pasó el analizador sintáctico", this.p.parse() == 0);   
        */
    }    
    
    @Test
    public void testErrores() {
        String filePath = "D:\\Java Projects\\Compilador\\Compilador\\files\\source.txt";
        SourceCode s = new SourceCode(filePath);
        s.generateSource();
        
        this.prepareParser(s.getAsString());
        int resultadoParse = this.p.parse();
        
        System.out.println("\n*************************");
        System.out.println("Resultado del análisis: ");
        if (this.eventoError.hayErrores()) {
            System.out.println("Fallido - Errores");
        } else {
            System.out.println("Exitoso - Sin errores");
        }
        System.out.println("\n*************************");
        System.out.println("Errores durante la compilacion: ");
        this.eventoError.visualizar("Error");
        System.out.println("\n*************************");
        System.out.println("Construcciones sintacticas: ");
        this.eventoError.visualizar("Regla");
        System.out.println("\n*************************");
        this.al.visualizarTablaSimbolos();
        assertTrue("Ultimos tests", resultadoParse == 0);
    }
   
}