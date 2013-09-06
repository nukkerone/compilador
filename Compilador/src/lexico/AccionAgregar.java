/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

/**
 *
 * @author Gabriel
 */
public class AccionAgregar extends AccionSemantica {
    
    AccionAgregar(AnalizadorLexico analizador) {
        super(analizador);    
    }

    @Override
    int ejecutar() {
        this.analizadorLexico.saveTempChar(this.analizadorLexico.getCaracterActual());
        return 0;
    }
    
    
    
}
