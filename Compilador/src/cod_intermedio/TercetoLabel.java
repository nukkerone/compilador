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
public class TercetoLabel extends Terceto {
    public TercetoLabel() {
        super("Label");
    }
    
    public String toString() {
        String s = "Terceto Label - Posicion: " + this.posicion;
        
        return s;
    }

    @Override
    public Vector<String> generarAssembler(SeguidorEstReg seguidor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getEtiqueta() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
