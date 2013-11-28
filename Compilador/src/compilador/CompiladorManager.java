/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import GenerarAssembler.PasarTercetoToAssembler;
import cod_intermedio.Terceto;
import filereader.OutputCode;
import filereader.SourceCode;
import herramientaerror.EventoError;
import java.io.Console;
import java.util.Iterator;
import java.util.Vector;
import lexico.AnalizadorLexico;
import sintactico.Parser;

/**
 *
 * @author Gabriel
 */
public class CompiladorManager {
    private EventoError eventoError;
    private Parser parser;
    private AnalizadorLexico analizadorLexico;
    private OutputCode output;
    private OutputCode asmOutput;
    private PasarTercetoToAssembler trad;
        
    CompiladorManager() {
        this.eventoError = new EventoError();
        this.parser = new Parser(false);
        this.parser.addEventoError(this.eventoError);
        this.analizadorLexico = new AnalizadorLexico(this.eventoError);
        this.parser.addAnalizadorLexico(this.analizadorLexico);
        
        String fileOutputPath = "output.txt";
        String asmOutputPath = "generated.asm";
        this.output = new OutputCode(fileOutputPath);
        this.asmOutput = new OutputCode(asmOutputPath);
        this.trad = new PasarTercetoToAssembler();
    }
    
    public void start() {
        Console console = System.console();
        execLoop(console);
    }
    
    public  void execLoop(Console console) {
        boolean cont = true;
        while (cont) {
            String filePath = console.readLine("Ingrese el Path del archivo a leer: ");
            SourceCode s = new SourceCode(filePath);
            String strCont;
            if (s.generateSource() != 0) {
                System.out.println("El archivo no puede ser alcanzado");
                strCont = console.readLine("Desea ingresar otro Path? (Y/N): ");
            } else {
                analizar(s, console);
                strCont = console.readLine("Realizar otro analisis? (Y/N): ");
            }
            
            if (!strCont.equals("Y") && !strCont.equals("y")) {
                cont = false;
            }
        }
    }
    
    private void prepareParser(String buffer) {
        this.analizadorLexico.setBuffer(buffer);
        this.parser = new Parser(false);
        this.parser.addEventoError(this.eventoError);
        this.parser.addAnalizadorLexico(this.analizadorLexico);
    }
    
    public  void analizar(SourceCode s, Console console) {
        boolean hayErrores = false;
        this.prepareParser(s.getAsString());
        this.parser.parse();
        
        hayErrores = this.eventoError.hayErrores();
        
        Vector<String> asm = trad.traducir(Terceto.tercetos, this.analizadorLexico.getTablaSimbolos());
        
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
        
        /*System.out.println("\n*************************");
        output.addLine("\n*************************");
        System.out.println("Construcciones sintacticas: ");
        output.addLine("Construcciones sintacticas: ");
        this.eventoError.visualizar("Regla", output);*/
        System.out.println("\n*************************");
        output.addLine("\n*************************");
      //  this.al.visualizarTablaSimbolos();
        this.analizadorLexico.visualizarTablaSimbolos(output);
        System.out.println("\n*************************");
        output.addLine("\n*************************");
        System.out.println("Tercetos generados: ");
        output.addLine("Tercetos generados: ");
        //Terceto.printTercetos(output);
        this.printTercetos();
        System.out.println("\n*************************");
        output.addLine("\n*************************");
        
        if (!hayErrores) {
            Iterator it = asm.iterator();
            while (it.hasNext()) {
               String assemblerLine = (String) it.next();
                System.out.println(assemblerLine);
                asmOutput.addLine(assemblerLine);
            }
        }
        
        this.output.output();
        this.asmOutput.output();
    }

      public   void printTercetos() {
        Iterator it = Terceto.tercetos.iterator();
        while (it.hasNext()) {
           Terceto t = (Terceto) it.next();
           System.out.println(t.toString());
            this.output.addLine(t.toString());
        }
    }
}