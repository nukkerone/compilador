/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public class Transicion {
    private int estadoSig;
    private List<AccionSemantica> accionesSemanticas;
    
    public Transicion(int estadoSig, List<AccionSemantica> accionesSemanticas) {
        this.estadoSig = estadoSig;
        this.accionesSemanticas = accionesSemanticas;
    }
    
    public int ejecutarAccion(AnalizadorLexico analizadorLexico) {
        for(int i=0; i< this.accionesSemanticas.size(); i++) {
            AccionSemantica accion = this.accionesSemanticas.get(i);
            if (accion != null) {
                accion.ejecutar();
            }
        }
        return 0;
    }
    
    public int getEstado() {
        return estadoSig;
    }
}
