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
 * @author mountainlion
 */
public class TercetoCall extends Terceto {
    
    public TercetoCall(Typeable tercetoLabel) {
        super("Call");
        this.parametro1 = tercetoLabel;
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
        v.add("CALL " + ((TercetoLabel)this.parametro1).getNombreEtiq());
        return v;
    }
    
}
