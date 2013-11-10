/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import lexico.TypeableToken;

/**
 *
 * @author mountainlion
 */
public class Inmediato extends DireccionRepreVarAssembler {
    TypeableToken t;

    public Inmediato(TypeableToken t) {
        super();
        this.t = t;
        this.nombre = t.getLexema();
    }
}
