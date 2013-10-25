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
    public static String TIPO_int = "t_int";
    public static String TIPO_DESCONOCIDO = "t_unknown";
    public static String TIPO_RECIEN_DECLARADA = "t_recien_declarada";
    public static String TIPO_STRING = "t_string";
    
    public String getTipo();
}
