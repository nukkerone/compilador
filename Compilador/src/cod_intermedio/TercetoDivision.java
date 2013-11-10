/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cod_intermedio;

import GenerarAssembler.SeguidorEstReg;
import interfaces.Typeable;
import java.util.Vector;

/**
 *
 * @author Gabriel
 */
public class TercetoDivision extends Terceto {
    public TercetoDivision(Typeable p1, Typeable p2) {
        super("/", p1, p2);
        
    }
    
    public String toString() {
        String s = "Terceto Division - Posicion: " + this.posicion + " - Operacion: " + this.operacion;
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

    @Override
    public Vector<String> generarAssembler(SeguidorEstReg seguidor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getEtiqueta() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getMessageData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
