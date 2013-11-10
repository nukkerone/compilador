/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import interfaces.Typeable;
import GenerarAssembler.SeguidorEstReg;

/**
 *
 * @author damian
 * Implementa la representacion de los Registros, recordar que seran 4, y seran creados y manejado por el SeguidorEstReg
 * 
 */
public class Registro extends DireccionRepreVarAssembler {
    
    private Typeable t;	
    private SeguidorEstReg seg;
    
    public Registro(String nombre, SeguidorEstReg seg){
        this.nombre = nombre;
        this.seg = seg;
    }
}
