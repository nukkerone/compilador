/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cod_intermedio;

import GenerarAssembler.SeguidorEstReg;
import java.util.Vector;

/**
 *
 * @author mountainlion
 */
public class TercetoPush extends Terceto {
    
    public TercetoPush() {
        super("Push");
    }
    
    public String toString() {
        String s = "Terceto Push - Posicion: " + this.posicion;
        
        return s;
    }

    @Override
    public String getEtiqueta() {
        return null;
    }

    @Override
    public String getMessageData() {
        return null;
    }

    @Override
    public Vector<String> generarAssembler(SeguidorEstReg seguidor) {
        Vector<String> v = new Vector<String>();
        v.add("PUSH AX");
        v.add("PUSH BX");
        v.add("PUSH CX");
        v.add("PUSH DX");
        return v;
    }
    
}
