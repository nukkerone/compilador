/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cod_intermedio;

import GenerarAssembler.SeguidorEstReg;
import assembler.DireccionRepreVarAssembler;
import assembler.Registro;
import interfaces.Typeable;
import java.util.Vector;

/**
 *
 * @author Gabriel
 */
public class TercetoDivision extends Terceto {
    public TercetoDivision(Typeable p1, Typeable p2) {
        super("/", p1, p2);
        throwsError = true;
    }
    
 /*   public String toString() {
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
    }*/

    public Vector<String> generarAssembler(SeguidorEstReg ser) {
        Vector<String> v = null;
        Registro d1 = null;
        DireccionRepreVarAssembler d2;

        d1 = ser.ubicarEnRegistroA(this.parametro1); //es A
        ser.desocuparD();
        d2 = ser.ubicarEnRegistroOMemoria(this.parametro2);
        //si esta en registro da reg, sino da la memoria, asique nunca ocupa D

        v = ser.getCodigoAsm();
        v.add("CMP " + d2.getNombre() + ", 0");
        v.add("JE " +getEtiqueta());
        v.add("MOV EDX, 0"); //seteo a 0 la parte alta del dividendo
        v.add("DIV " + d2.getNombre());
        if(this.parametro2 != this.parametro1)
            d2.liberate();
        d1.actualizarT(this);
        return v;
    }

    @Override
    public String getMessageData() {
        return getEtiqueta()+"_MESSAGE DB \"División por cero\"";
    }

    @Override
    public String getEtiqueta() {
        return "DIVISION_POR_CERO";
    }
}
