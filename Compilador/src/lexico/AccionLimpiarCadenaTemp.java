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
public class AccionLimpiarCadenaTemp extends AccionSemantica {
    AccionLimpiarCadenaTemp(AnalizadorLexico analizador, EventoError e) {
        super(analizador, e);
    }

    @Override
    int ejecutar() {
        this.analizadorLexico.emptyTempChar();
        return 0;
    }
    
    
}
