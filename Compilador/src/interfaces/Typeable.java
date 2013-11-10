/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

/**
 *
 * @author Gabriel
 */
public interface Typeable {
    public static int TIPO_DESCONOCIDO = 1000;
    public static int TIPO_RECIEN_DECLARADA = 1001;
    public static int TIPO_INT = 1002;
    public static int TIPO_CADENA = 1003;
      
    public int getTipo();
    public void setTipo(int tipo);
    
    public String getTipoAmigable();
}
