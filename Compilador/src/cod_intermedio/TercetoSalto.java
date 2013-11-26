/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cod_intermedio;

import GenerarAssembler.SeguidorEstReg;
import java.util.Vector;

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
    
  /*  @Override
    public String toString() {
        String s = "Terceto Salto - Posicion: " + this.posicion + " - Operacion: " + this.operacion + " - Posicion Salto: "
                + this.dirSalto;

        return s;
    }*/
    
    public String getNombreEtiq() {
        return "_etiq" + this.posicion;
    }

    @Override
    public Vector<String> generarAssembler(SeguidorEstReg ser) {
        Vector<String> v = new Vector<String>();
        if(this.operacion == "BI") {
            TercetoLabel label = (TercetoLabel) tercetos.get(dirSalto-1);   // @TODO checkiar si no debiera ser dirSalto - 0
            v.add("JMP " + label.getNombreEtiq());
        } else { //es BF
            String tipoSalto;
            //posicion - 1 soy yo, posicion - 2 es el anterior, la comparacion
            String s = tercetos.get(this.posicion - 1).getOperacion();  /// Recupera la operacion del terceto comparacion
            tipoSalto = obtenerTipoSalto(s);	
            v.add(tipoSalto + " _etiq" + (dirSalto - 1));
        }
        return v;
    }
    
    private String obtenerTipoSalto(String s) {         
        switch (s) {
            case ">":  
                return "JNA";
            case ">=":  
                return "JNAE";
            case "<":  
                return "JNB";
            case "<=":  
                return "JNBE";
            case "==":  
                return "JNE";
            case "!=":  
                return "JE";
            default: 
                return "";
        }
    }

    @Override
    public String getEtiqueta() {
        return null;
    }

    @Override
    public String getMessageData() {
        return null;
    }
    
}
