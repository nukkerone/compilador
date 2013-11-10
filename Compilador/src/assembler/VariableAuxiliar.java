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
public class VariableAuxiliar extends TypeableToken {
	static int cant = 0;
	private Typeable tipable;
	
	public VariableAuxiliar(Typeable t){
            super("__aux" + cant);
            cant++;
            this.tipable = t;
	}
	

	public String getTipoDeToken() {
            // No necesario acá.
            return "";
	}

	public String getName() {
            return lexema;
	}

	public int getTipo() {
            return tipable.getTipo();
	}
}