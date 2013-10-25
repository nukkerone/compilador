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
    
    private String tipo;
    
    TokenLexemaDistinto(String palabraReservada, String lexema) {
        super(palabraReservada);
        this.lexema = lexema;
        this.tipo = TIPO_RECIEN_DECLARADA;
    }

    @Override
    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
}
