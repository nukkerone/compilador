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
public class AccionNoConsumir extends AccionSemantica{
    private boolean consume;
    
    AccionNoConsumir(AnalizadorLexico analizador, EventoError e) {
        super(analizador, e);
        consume = false;
    }

    @Override
    int ejecutar() {
        this.analizadorLexico.goBack();
        return 0;
    }
    
}
