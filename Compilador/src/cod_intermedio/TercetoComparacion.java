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
    
    public String toString() {
        String s = "Terceto Comparacion - Posicion: " + this.posicion + " - Operacion: " + this.operacion;
        String param1Str = " - Null";
        String param2Str = " - Null";
        
        if (this.parametro1 != null) {
            param1Str =  " - Parametro 1: " + this.parametro1.getTipoAmigable();
        }
        
        if (this.parametro2 != null) {
            param2Str = " - Parametro 2: " + this.parametro2.getTipoAmigable();
        }
        
        return s + param1Str + param2Str;
    }
    
}
