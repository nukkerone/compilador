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
        return 0;
    }
    
    
}
