/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

/**
 *
 * @author Gabriel
 */
public class AccionConsumir extends AccionSemantica {
    private boolean consume;
    
    AccionConsumir(AnalizadorLexico analizador) {
        super(analizador);
        consume = true;
    }
    
    @Override
    public int ejecutar() {
        // Consume por lo tanto no se hace goBack()
        return 0;
    }
    
}
