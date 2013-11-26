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
public class TercetoAsignacion extends Terceto {
    public TercetoAsignacion(Typeable p1, Typeable p2) {
        super("=", p1, p2);
        
        this.setTipo(p1.getTipo());
    }
    
    private Terceto quitarUltimo(){
        Terceto salida;
        salida = Terceto.tercetos.remove(tercetos.size()-1);
        return salida;
    }
    
   /* public String toString() {
        String s = "Terceto Asignacion - Posicion: " + this.posicion + " - Operacion: " + this.operacion;
        String param1Str = " - Null";
        String param2Str = " - Null";
        
        if (this.parametro1 != null) {
            param1Str =  " - Tipo Parametro 1: " + this.parametro1.getTipoAmigable();
        }
        
        if (this.parametro2 != null) {
            param2Str = " - Tipo Parametro 2: " + this.parametro2.getTipoAmigable();
        }
        
        return s + param1Str + param2Str;

    }*/
    
    @Override
    public Vector<String> generarAssembler(SeguidorEstReg seg) {
        DireccionRepreVarAssembler d1 = seg.ubicarEnMemoria((Typeable) this.parametro1);
        DireccionRepreVarAssembler d2 = seg.ubicarEnRegistroOInmed(this.parametro2);
        Vector<String> v = seg.getCodigoAsm();
        v.add("MOV " + d1.getNombre() + ", " + d2.getNombre());
        d2.liberate();
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
