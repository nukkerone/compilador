/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

/**
 *
 * @author Gabriel
 */
public class TokenLexemaDistinto extends Token {
    
    TokenLexemaDistinto(String palabraReservada, String lexema) {
        super(palabraReservada);
        this.lexema = lexema;
    }
    
}
