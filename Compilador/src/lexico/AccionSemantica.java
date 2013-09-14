/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

import herramientaerror.EventoError;

/**
 *
 * @author Gabriel
 */
public abstract class AccionSemantica {
    
    protected AnalizadorLexico analizadorLexico;
    protected EventoError eventoError;
    
    AccionSemantica(AnalizadorLexico analizador, EventoError e) {
        this.analizadorLexico = analizador;
        this.eventoError = e;
    }
    
    abstract int ejecutar();
}
