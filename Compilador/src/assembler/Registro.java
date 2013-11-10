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
    private SeguidorEstReg ser;
    
    public Registro(String nombre, SeguidorEstReg ser){
        this.nombre = nombre;
        this.ser = ser;
    }
    public String getRegX(){
        return this.nombre + "X";
    }

    public String getNombre() {
        //if(t.getTipo() == Typeable.TIPO_UINT)
          //  return getRegX();
        //else
            return getRegEX();
    }

    // Retorna reg 16bits
    public String getRegEX(){
        return "E"+ this.nombre + "X";
    }

    // Retorna reg 8bits low
    public String getRegL(){
        return this.nombre + "L";
    }

    // Retorna reg 8bits high
    public String getRegH(){
        return this.nombre + "H";
    }

    // Ocupa este registro con el Typeable (TypeableToken o Terceto)
    public void ocupar(Typeable t){
        this.t = t;
    }

    // Libera el registro
    public Typeable liberar(){
        Typeable _t = this.t;
        t = null;
        return _t;
    }
	
    @Override
    public void liberate(){
        ser.liberarReg(this.t);
        t = null;
    }
	
    public boolean estaOcupado(){
        return this.t!=null;
    }


    public void actualizarT(Typeable t) {
        ser.actualizarRegistro(this, t);
    }
    
    public Typeable getT() {
        return t;
    }
}
