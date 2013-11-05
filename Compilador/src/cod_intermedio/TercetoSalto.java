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
    
    public String toString() {
        return "(" + this.operacion + ", [" + this.dirSalto + "], - )";
    }
    
}
