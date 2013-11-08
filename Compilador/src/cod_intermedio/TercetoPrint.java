/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cod_intermedio;

import interfaces.Typeable;

/**
 *
 * @author Gabriel
 */
public class TercetoPrint extends Terceto {
    public TercetoPrint(Typeable p1) {
        super("Print",p1,null);
    }
    
    public String toString() {
        String s = "Terceto Print - Posicion: " + this.posicion + " - Operacion: " + this.operacion;
        String param1Str = " - Null";
        
        if (this.parametro1 != null) {
            param1Str =  " - Parametro 1: " + this.parametro1.getTipoAmigable();
        }
        
        return s + param1Str;
    }
}
