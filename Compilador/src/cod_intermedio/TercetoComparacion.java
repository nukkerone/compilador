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
import lexico.Token;

/**
 *
 * @author Gabriel
 */
public class TercetoComparacion extends Terceto {
    public TercetoComparacion(Token t, Typeable p1, Typeable p2) {
        super(t.getLexema(), p1, p2);
        
    }
    
  /*  public String toString() {
        String s = "Terceto Comparacion - Posicion: " + this.posicion + " - Operacion: " + this.operacion;
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
            v.add("CMP " + d1.getNombre() + ", " + d2.getNombre());
            if(this.parametro2 != this.parametro1)
                    d2.liberate();
            d1.liberate();

            //TODO:: agregar codigo de verificacion fuera de rango
            return v;
	}

    @Override
	public String getMessageData() {
            return null;
	}

	@Override
	public String getEtiqueta() {
            return null;
	}
}
