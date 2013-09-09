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
    protected int nroLinea;
    
    Token(String palabraReservada) {
        this.palabraReservada = palabraReservada;
        this.lexema = palabraReservada;
    }
    
    public String getPalabraReservada() {
        return this.palabraReservada;
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
    
    @Override
    public String toString() {
        return "PalabraReservada: " + this.palabraReservada + " --- Lexema: " + this.getLexema() + " --- Linea: " + this.getNroLinea();
    }

    public void setNroLinea(int nroLinea) {
        this.nroLinea = nroLinea;
    }
    
    public int getNroLinea() {
        return this.nroLinea;
    }
}
