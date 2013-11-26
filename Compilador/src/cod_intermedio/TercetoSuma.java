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
public class TercetoSuma extends Terceto {
    public TercetoSuma(Typeable p1, Typeable p2) {
        super("+", p1, p2);
        throwsError = true;
    }
    
    public String toString() {
        String s = "Terceto Suma - Posicion: " + this.posicion + " - Operacion: " + this.operacion;
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
	public Vector<String> generarAssembler(SeguidorEstReg ser) {
            Vector<String> v;
            Registro d1;
            DireccionRepreVarAssembler d2;

            if(!ser.estaEnRegistro(this.parametro2)) {
                d1 = ser.ubicarEnRegistro(this.parametro1);
                d2 = ser.ubicar(this.parametro2);
            } else {
                d2 = ser.ubicar(this.parametro1);
                d1 = ser.getRegistro(this.parametro2);
            }

            v = ser.getCodigoAsm();
            v.add("ADD " + d1.getNombre() + ", " + d2.getNombre());
            v.add("JC " + getEtiqueta());
            if(this.parametro2 != this.parametro1)
                d2.liberate();
            d1.actualizarT(this);
            return v;
	}

    @Override
    public String getEtiqueta() {
        return "OVERFLOW_SUMA";
    }
    
    @Override
    public String getMessageData() {
        return getEtiqueta()+"_MESSAGE DB \"Fuera de rango en Suma$\"";
    }
        
}
