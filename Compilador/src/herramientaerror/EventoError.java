/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package herramientaerror;
import lexico.TablaSimbolos;
import lexico.Token;
import compilador.Error;
import filereader.OutputCode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 *
 * @author damian
 */
public class EventoError {
   
   private ArrayList<Error> CadenaError;

   
   
   public  EventoError(){
       
       CadenaError = new ArrayList<Error>();
   }
    
   public void add(String mensaje, int NroLinea, String seccion, String tipoError ){
       Error e = new Error(mensaje,NroLinea,seccion,tipoError);
       CadenaError.add(e);
   }
     public Iterator createIterator() {
        
        return this.CadenaError.iterator();
    }
     
   public ArrayList<String> visualizarMensaje(){
      ArrayList<String> CadenaTemp = new ArrayList<String>();
        
      Iterator<Error> IterError = this.CadenaError.iterator();
      Error simbolo;
      while(IterError.hasNext()) {
            simbolo = IterError.next();
          CadenaTemp.add(simbolo.gettipoError());
      }
    return CadenaTemp;
   }
   
   
   public ArrayList<String> devolverTipoError(String Terror){
      ArrayList<String> CadenaTemp = new ArrayList<String>();
        
      Iterator<Error> IterError = this.CadenaError.iterator();
      Error simbolo;
      while(IterError.hasNext()) {
            simbolo = IterError.next();
            if (Terror.equals(simbolo.gettipoError()))
                    CadenaTemp.add(simbolo.gettipoError());
      }
    return CadenaTemp;
   }
public ArrayList<String> devolverSeccion(String Tseccion){
      ArrayList<String> CadenaTemp = new ArrayList<String>();
        
      Iterator<Error> IterError = this.CadenaError.iterator();
      Error simbolo;
      while(IterError.hasNext()) {
            simbolo = IterError.next();
            if (Tseccion.equals(simbolo.getSeccion()))
                    CadenaTemp.add(simbolo.gettipoError());
      }
    return CadenaTemp;
   }

public ArrayList<Integer> devolverNroLinea(int nroLinea){
      ArrayList<Integer> CadenaTemp = new ArrayList<Integer>();
        
      Iterator<Error> IterError = this.CadenaError.iterator();
      Error simbolo;
      while(IterError.hasNext()) {
            simbolo = IterError.next();
            if (nroLinea == simbolo.getnroLinea())
                    CadenaTemp.add(simbolo.getnroLinea());
      }
    return CadenaTemp;
   }

public ArrayList<String> devolverMensaje(String Merror){
      ArrayList<String> CadenaTemp = new ArrayList<String>();
        
      Iterator<Error> IterError = this.CadenaError.iterator();
      Error simbolo;
      while(IterError.hasNext()) {
            simbolo = IterError.next();
            if (Merror.equals(simbolo.getMensaje()))
                    CadenaTemp.add(simbolo.gettipoError());
      }
    return CadenaTemp;
   }

    public void visualizar() {
         Iterator<Error> IterError = this.CadenaError.iterator();
         while(IterError.hasNext()) {
            Error e = IterError.next();
            System.out.println(e);
         }
    }
    
    public void visualizar(String type, OutputCode o) {
        Iterator it = this.CadenaError.iterator();
        while (it.hasNext()) {
           Error log = (Error) it.next();
           if (log.gettipoError() == type) {
               if (o != null) {
                   o.addLine(log.toString());
               } else {
                   System.out.println(log);
               }
               
           }
        }
    }
    
    /**
     * Devuelve si hay errores capturados
     * 
     * @return 
     */
    public boolean hayErrores() {
       Iterator it = this.CadenaError.iterator();
       while (it.hasNext()) {
           Error log = (Error) it.next();
           if (log.gettipoError() == "Error") {
               return true;
           }
       }
       return false;
    }
}
