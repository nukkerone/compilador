/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

/**
 *
 * @author damian
 */
public class DireccionRepreVarAssembler {
    protected String nombre;

    @Override
    public int hashCode() {
            return nombre.hashCode();
    }

    public String getNombre() {
            return nombre;
    }

    public void liberate(){
    }
}
