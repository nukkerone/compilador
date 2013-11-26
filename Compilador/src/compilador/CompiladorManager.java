/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import cod_intermedio.Terceto;
import filereader.OutputCode;
import filereader.SourceCode;
import herramientaerror.EventoError;
import java.io.Console;
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
        
    CompiladorManager() {
        this.eventoError = new EventoError();
        this.parser = new Parser(false);
        this.parser.addEventoError(this.eventoError);
        this.analizadorLexico = new AnalizadorLexico(this.eventoError);
        this.parser.addAnalizadorLexico(this.analizadorLexico);
        
        String fileOutput = "D:\\Java Projects\\Compilador\\Compilador\\files\\output.txt";
        this.output = new OutputCode(fileOutput);
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
        this.prepareParser(s.getAsString());
        this.parser.parse();
        System.out.println("\n*************************");
        System.out.println("Resultado del análisis: ");
        if (this.eventoError.hayErrores()) {
            System.out.println("Fallido - Errores");
        } else {
            System.out.println("Exitoso - Sin errores");
        }
        System.out.println("\n*************************");
        System.out.println("Errores durante la compilacion: ");
        this.eventoError.visualizar("Error", this.output);
        System.out.println("\n*************************");
        System.out.println("Construcciones sintacticas: ");
        this.eventoError.visualizar("Regla", this.output);
        System.out.println("\n*************************");
        this.analizadorLexico.visualizarTablaSimbolos();
        System.out.println("\n*************************");
        System.out.println("Tercetos generados: ");
        Terceto.printTercetos();
        System.out.println("\n*************************");
    }
}
