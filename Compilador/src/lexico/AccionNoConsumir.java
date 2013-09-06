/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

/**
 *
 * @author Gabriel
 */
public class AccionNoConsumir extends AccionSemantica{
    private boolean consume;
    
    AccionNoConsumir(AnalizadorLexico analizador) {
        super(analizador);
        consume = false;
    }

    @Override
    int ejecutar() {
        this.analizadorLexico.goBack();
        return 0;
    }
    
}
