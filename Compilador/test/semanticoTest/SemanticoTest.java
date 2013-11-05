/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticoTest;

import cod_intermedio.Terceto;
import sintacticoTest.*;
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
public class SemanticoTest {
    private Parser p;
    AnalizadorLexico al;
    boolean debug;
    EventoError eventoError;
    
    public SemanticoTest() {
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
    public void testErrores() {
        String filePath = "/Users/mountainlion/Documents/Projects/Java Projects/compilador/Compilador/files/tercetos.txt";
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
        System.out.println("\n*************************");
        Terceto.tercetos.toString();
        assertTrue("Ultimos tests", resultadoParse == 0);
        
    }
   
}