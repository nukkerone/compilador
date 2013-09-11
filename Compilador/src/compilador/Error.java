package compilador;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Gabriel
 */
public class Error {
    private String mensaje;
    private int nroLinea;
    private String seccion;
    private String tipoError;
    
    public Error(String mensaje, int nroLinea, String seccion, String tipo) {
        this.mensaje = mensaje;
        this.nroLinea = nroLinea;
        this.seccion = seccion;
        this.tipoError = tipo;
    }

      
    public String devolverError() {
        return mensaje + "----" + nroLinea + "---" + seccion + "----" + tipoError;
   }
    
    public String getSeccion()
    {return seccion;}
    
    public String gettipoError()
    {return tipoError;}
    
    public int getnroLinea()
    {return nroLinea;}
    
    public String getMensaje()
    {return mensaje;}
}
