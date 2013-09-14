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
public class AccionConsumir extends AccionSemantica {
    private boolean consume;
    
    AccionConsumir(AnalizadorLexico analizador, EventoError e) {
        super(analizador, e);
        consume = true;
    }
    
    @Override
    public int ejecutar() {
        // Consume por lo tanto no se hace goBack()
        return 0;
    }
    
}
