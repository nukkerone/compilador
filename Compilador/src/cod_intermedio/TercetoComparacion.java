/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cod_intermedio;

import interfaces.Typeable;
import lexico.Token;

/**
 *
 * @author Gabriel
 */
public class TercetoComparacion extends Terceto {
    public TercetoComparacion(Token t, Typeable p1, Typeable p2) {
        super(t.getLexema(), p1, p2);
    }
}
