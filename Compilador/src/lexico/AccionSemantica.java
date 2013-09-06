/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

/**
 *
 * @author Gabriel
 */
public abstract class AccionSemantica {
    
    AnalizadorLexico analizadorLexico;
    
    AccionSemantica(AnalizadorLexico analizador) {
        this.analizadorLexico = analizador;
    }
    
    abstract int ejecutar();
}
