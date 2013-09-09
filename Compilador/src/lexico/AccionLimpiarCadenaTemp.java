/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

/**
 *
 * @author Gabriel
 */
public class AccionLimpiarCadenaTemp extends AccionSemantica {
    AccionLimpiarCadenaTemp(AnalizadorLexico analizador) {
        super(analizador);
    }

    @Override
    int ejecutar() {
        this.analizadorLexico.emptyTempChar();
        return 0;
    }
    
    
}
