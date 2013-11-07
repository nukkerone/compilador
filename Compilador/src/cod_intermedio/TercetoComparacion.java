/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cod_intermedio;

import GenerarAssembler.SeguidorEstReg;
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

    @Override
    public Vector<String> generarAssembler(SeguidorEstReg seguidor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getEtiqueta() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
