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
public class AccionNoAgregar extends AccionSemantica {
    AccionNoAgregar(AnalizadorLexico analizador, EventoError e) {
        super(analizador, e);
    }

    @Override
    int ejecutar() {
        Character caracter = this.analizadorLexico.getCaracterActual();
        if (caracter == null) { // END OF FILE
            return 0;
        }
        if (Character.toString(caracter).equals("\n")) {   // 10 es el salto de línea => \n
            this.analizadorLexico.avanzarLinea();
        }
        return 0;
    }
}
