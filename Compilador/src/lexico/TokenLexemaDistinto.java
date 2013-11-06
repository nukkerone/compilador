/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

/**
 *
 * @author Gabriel
 */
public class TokenLexemaDistinto extends TypeableToken {
    
    private int tipo;
    
    TokenLexemaDistinto(String palabraReservada, String lexema) {
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
    
}
