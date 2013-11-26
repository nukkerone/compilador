/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cod_intermedio;

import GenerarAssembler.SeguidorEstReg;
import assembler.DireccionRepreVarAssembler;
import interfaces.Typeable;
import java.util.Vector;

/**
 *
 * @author Gabriel
 */
public class TercetoPrint extends Terceto {
    public TercetoPrint(Typeable p1) {
        super("Print",p1,null);
    }
    
 /*   public String toString() {
        String s = "Terceto Print - Posicion: " + this.posicion + " - Operacion: " + this.operacion;
        String param1Str = " - Null";
        
        if (this.parametro1 != null) {
            param1Str =  " - Parametro 1: " + this.parametro1.getTipoAmigable();
        }
        
        return s + param1Str;
    }*/
    
    @Override
    public Vector<String> generarAssembler(SeguidorEstReg mr) {
        Vector<String> v;
        mr.desocuparD();
        DireccionRepreVarAssembler d2 = mr.ubicarEnMemoria(this.parametro1);
        v = mr.getCodigoAsm();
        v.add("MOV dx,OFFSET " + d2.getNombre());
        v.add("mov	ah,9");
        v.add("int	21h");
        return v;
    }

    @Override
    public String getMessageData() {
        return "";
    }

    @Override
    public String getEtiqueta() {
        return null;
    }

}
