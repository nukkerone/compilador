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
    
    
    /*public String toString() {
        String s = "Terceto Label - Posicion: " + this.posicion;
        
        return s;
    }*/
    
    public String getNombreEtiq() {
        return "_etiq" + this.posicion;
    }

    @Override
    public Vector<String> generarAssembler(SeguidorEstReg ser) {
        Vector<String> v = new Vector<String>();
        v.add(getNombreEtiq() + ": ");
        return v;
    }
    

    @Override
    public String getMessageData() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getEtiqueta() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
