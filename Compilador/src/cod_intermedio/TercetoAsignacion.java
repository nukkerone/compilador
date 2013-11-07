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
 * @author Gabriel
 */
public class TercetoAsignacion extends Terceto {
    public TercetoAsignacion(Typeable p1, Typeable p2) {
        super("=", p1, p2);
        
        /* Me parece que esto por ahora no lo necesitamos, es un chekeo de tipos, pero tenemos siempre integers?
        if(p1.getTipo() != p2.getTipo()){
            Terceto aux = quitarUltimo();
            //agrego conversion implicita
            Terceto t = new TercetoConversion(p1.getTipo(), p2);
            //se tiene que agregar antes que el ultimo, que es This
            aux.setNumero(tercetos.size()+1);
            aux.setParam2(t);
            tercetos.add(aux);
        }
        */
        this.setTipo(p1.getTipo());
    }
    
    private Terceto quitarUltimo(){
        Terceto salida;
        salida = Terceto.tercetos.remove(tercetos.size()-1);
        return salida;
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
