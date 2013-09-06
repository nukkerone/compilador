/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

/**
 *
 * @author Gabriel
 */
public class Token {
    
    protected String palabraReservada;
    protected String lexema;
    protected int id;
    
    Token(String palabraReservada) {
        this.palabraReservada = palabraReservada;
        this.lexema = palabraReservada;
    }
    
    public String getLexema() {
        return this.lexema;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
}
