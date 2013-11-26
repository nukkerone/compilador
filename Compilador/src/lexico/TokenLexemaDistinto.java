/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

import interfaces.Typeable;

/**
 *
 * @author Gabriel
 */
public class TokenLexemaDistinto extends TypeableToken {
    
    private int tipo;
    
    public TokenLexemaDistinto(String palabraReservada, String lexema) {
        super(palabraReservada);
        this.lexema = lexema;
        this.tipo = TIPO_RECIEN_DECLARADA;
    }

    @Override
    public int getTipo() {
        return this.tipo;
    }

    @Override
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    
    @Override
    public void setAmbito(String ambito) {
        if (this.getTipo() == Typeable.TIPO_CTE_ENTERA ||
                this.lexema.equals("_RET") ||
                this.lexema.equals("_PARAM")) {
            return;
        }
        
        if (this.lexema.contains("_main")) {
            this.lexema = this.lexema.replace("_main", "");
        }
        this.lexema = this.lexema + ambito;
    }
    
}
