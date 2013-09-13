/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

/**
 *
 * @author Gabriel
 */
public class AccionNoAgregar extends AccionSemantica {
    AccionNoAgregar(AnalizadorLexico analizador) {
        super(analizador);
    }

    @Override
    int ejecutar() {
        Character caracter = this.analizadorLexico.getCaracterActual();
        if (caracter == null) { // END OF FILE
            return 0;
        }
        if (caracter == 10) {   // 10 es el salto de línea => \n
            this.analizadorLexico.avanzarLinea();
        }
        return 0;
    }
}
