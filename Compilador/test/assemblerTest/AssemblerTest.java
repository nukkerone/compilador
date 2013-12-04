/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assemblerTest;

import GenerarAssembler.PasarTercetoToAssembler;
import semanticoTest.*;
import cod_intermedio.Terceto;
import sintacticoTest.*;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sintactico.*;
import compilador.*;
import filereader.OutputCode;
import filereader.SourceCode;
import herramientaerror.EventoError;
import java.util.Iterator;
import java.util.Vector;
import lexico.*;

/**
 *
 * @author Gabriel
 */
public class AssemblerTest {
    private Parser p;
    AnalizadorLexico al;
    boolean debug;
    EventoError eventoError;
    
    public AssemblerTest() {
        this.debug = false;
        this.eventoError = new EventoError();
        this.p = new Parser(this.debug);
        this.p.addEventoError(this.eventoError);
        this.al = new AnalizadorLexico(this.eventoError);
        this.p.addAnalizadorLexico(this.al);
    }
    
    @BeforeClass
    public static void setUpClass() {
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
        boolean hayErrores = false;
        //String filePath = "/Users/mountainlion/Documents/Projects/Java Projects/compilador/Compilador/files/test.txt";        //String filePath = "D:\\Java Projects\\Compilador\\Compilador\\files\\test.txt";
        String filePath = "D:\\Java Projects\\Compilador\\Compilador\\files\\testsfinales\\1- asignacion.txt";        //String filePath = "D:\\Java Projects\\Compilador\\Compilador\\files\\test.txt";
        //String filePath = "C:\\Users\\Cacho\\Documents\\Compiladores\\compilador\\Compilador\\files\\test.txt";
        SourceCode s = new SourceCode(filePath);
        //String fileOutput = "C:\\Users\\Cacho\\Documents\\Compiladores\\compilador\\Compilador\\files\\output.txt";
        String fileOutput = "D:\\Java Projects\\Compilador\\Compilador\\files\\output.txt";
        OutputCode output = new OutputCode(fileOutput);
        
        s.generateSource();
        
        PasarTercetoToAssembler trad = new PasarTercetoToAssembler();
                
        this.prepareParser(s.getAsString());
        int resultadoParse = this.p.parse();
        
        hayErrores = this.eventoError.hayErrores();
        
        Vector<String> asm = trad.traducir(Terceto.tercetos, this.al.getTablaSimbolos());
        
        System.out.println("\n*************************");
        output.addLine("\n*************************");
        System.out.println("Resultado del análisis: ");
        output.addLine("Resultado del análisis: ");
        if (hayErrores) {
            System.out.println("Fallido - Errores");
            output.addLine("Fallido - Errores");
            System.out.println("\n*************************");
            output.addLine("\n*************************");
            System.out.println("Errores durante la compilacion: ");
            output.addLine("Errores durante la compilacion: ");
            this.eventoError.visualizar("Error", output);
        } else {
            System.out.println("Exitoso - Sin errores");
            output.addLine("Exitoso - Sin errores");
        }
        
        System.out.println("\n*************************");
        output.addLine("\n*************************");
        System.out.println("Construcciones sintacticas: ");
        output.addLine("Construcciones sintacticas: ");
        this.eventoError.visualizar("Regla", output);
        System.out.println("\n*************************");
        output.addLine("\n*************************");
      //  this.al.visualizarTablaSimbolos();
        this.al.visualizarTablaSimbolos(output);
        System.out.println("\n*************************");
        output.addLine("\n*************************");
        System.out.println("Tercetos generados: ");
        output.addLine("Tercetos generados: ");
        //Terceto.printTercetos(output);
        printTercetos(output);
        System.out.println("\n*************************");
        output.addLine("\n*************************");
        
        if (!hayErrores) {
            Iterator it = asm.iterator();
            while (it.hasNext()) {
               String assemblerLine = (String) it.next();
                System.out.println(assemblerLine);
                output.addLine(assemblerLine);
            }
        }
        
        output.output();
        assertTrue("Ultimos tests", resultadoParse == 0);
        
    }
    public   void printTercetos(OutputCode o) {
        Iterator it = Terceto.tercetos.iterator();
        while (it.hasNext()) {
           Terceto t = (Terceto) it.next();
           System.out.println(t.toString());
            o.addLine(t.toString());
        }
    }
   
}