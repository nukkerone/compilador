/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cod_intermedio;

/**
 *
 * @author Gabriel
 */
public class TercetoSalto extends Terceto {
    private int dirSalto;
    
    public TercetoSalto(String tipo) {
        super(tipo);
        
    }
    
    public int getDirSalto() {
		return this.dirSalto;
	}
    
    public void setDirSalto(int dirSalto) {
            this.dirSalto = dirSalto;
    }
    
    @Override
    public String toString() {
        String s = "Terceto Salto - Posicion: " + this.posicion + " - Operacion: " + this.operacion + " - Posicion Salto: "
                + this.dirSalto;

        return s;
    }
    
}
