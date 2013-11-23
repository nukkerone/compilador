/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

import interfaces.Uso;

/**
 *
 * @author Gabriel
 */
public class Token implements Uso{
    
    protected String palabraReservada;
    protected String lexema;
    protected int uso;
    protected int id;
    protected int nroLinea;
    
    Token(String palabraReservada) {
        this.palabraReservada = palabraReservada;
        this.lexema = palabraReservada;
        this.uso = Token.USO_VARIABLE;
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
    
    public int getUso() {
        return this.uso;
    }
    
    public void setUso(int uso) {
        this.uso = uso;
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
