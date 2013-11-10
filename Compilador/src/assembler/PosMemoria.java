/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import interfaces.Typeable;
import lexico.TypeableToken;

/**
 *
 * @author mountainlion
 */
public class PosMemoria extends DireccionRepreVarAssembler {
    private TypeableToken tk;
    static int cont =0;

    public PosMemoria(TypeableToken tk){
        this.tk = tk;
        if (tk.getTipo() == Typeable.TIPO_CADENA){ 
            this.nombre = "cadena"+cont;
            cont++;
        }
        else{
            this.nombre = "_"+tk.getLexema();
        }
    }

    public TypeableToken getTk() {
        return tk;
    }
}
