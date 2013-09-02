/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

/**
 *
 * @author Gabriel
 */
public class Transicion {
    private int estadoSig;
    private AccionSemantica accionSemantica;
    
    public Transicion(int estadoSig, AccionSemantica accionSemantica) {
        this.estadoSig = estadoSig;
        this.accionSemantica = accionSemantica;
    }
    
    public int ejecutarAccion() {
        return this.accionSemantica.ejecutar();
    }
}
