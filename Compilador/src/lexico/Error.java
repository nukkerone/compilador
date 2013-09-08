/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

/**
 *
 * @author Gabriel
 */
public class Error {
    private String mensaje;
    private int nroLinea;
    
    Error(String mensaje, int nroLinea) {
        this.mensaje = mensaje;
        this.nroLinea = nroLinea;
    }
    
    public String toString() {
        return this.mensaje;
    }
}
