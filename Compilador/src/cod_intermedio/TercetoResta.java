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
public class TercetoResta extends Terceto {
    public TercetoResta(Typeable p1, Typeable p2) {
        super("-", p1, p2);
        throwsError = true;
    }
    
   /* public String toString() {
        String s = "Terceto Resta - Posicion: " + this.posicion + " - Operacion: " + this.operacion;
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
    
    @Override
    public Vector<String> generarAssembler(SeguidorEstReg ser) {
        Vector<String> v;
        Registro d1;
        DireccionRepreVarAssembler d2;

        d1 = ser.ubicarEnRegistro(this.parametro1);
        d2 = ser.ubicar(this.parametro2);

        v = ser.getCodigoAsm();
        v.add("SUB " + d1.getNombre() + ", " + d2.getNombre());
        
        v.add("CMP " + "EBX" + ", 0");
        v.add("JNS " + "NO_VOLVER_ABSOLUTO_RESTA" + this.posicion);
        v.add("CMP " + "EBX" + ", -32768");
        v.add("JB " + getEtiqueta());
        v.add("JMP ETIQUETA_NO_OVERFLOW_RESTA" + this.posicion);
        v.add("NO_VOLVER_ABSOLUTO_RESTA"  + this.posicion + ":");
        v.add("CMP " + "EBX" + ", 32767");
        v.add("JA " + getEtiqueta());
        v.add("ETIQUETA_NO_OVERFLOW_RESTA"  + this.posicion + ":");
        
        if(this.parametro2 != this.parametro1)
            d2.liberate();
        d1.actualizarT(this);
        return v;
    }

    @Override
    public String getMessageData() {
        return getEtiqueta()+"_MESSAGE DB \"Fuera de rango en Resta\",0";
    }

    @Override
    public String getEtiqueta() {
        return "OVERFLOW_RESTA";
    }
}
