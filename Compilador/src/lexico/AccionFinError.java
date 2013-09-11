/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

/**
 *
 * @author damian
 */
public class AccionFinError extends AccionSemantica{
 AccionFinError(AnalizadorLexico analizador ){
     super(analizador);
}
    @Override
    int ejecutar() {
        switch(analizadorLexico.getEstadoTemporal())
            {
           case 0: // 'se encuentra en estado inicial falta ; o otro caracter para llegar estado final'
               analizadorLexico.addError("Falta finalizar token ",analizadorLexico.getNroLinea(),"Lexico","Error");
               
               //System.out.print("Falta finalizar token ");
               break;
           case 1: // 'cadena ingresada incorrecta, espera fin que nunca esta'
              analizadorLexico.addError("Cadena de caracteres incorrecta ",analizadorLexico.getNroLinea(),"Lexico","Error");
               // System.out.print("cadena de caracteres incorrecta ");
                break;
           case 2: // 'no ingresa un valor distinto al estar en el estado posible digito'
               analizadorLexico.addError("cadena de digitos incorrecta faltante ; ",analizadorLexico.getNroLinea(),"Lexico","Error"); 
               //System.out.print("cadena de digitos incorrecta ; ");
                break;
           case 3: // 'divisor con faltante de dato derecho'
                analizadorLexico.addError("Operador divisor con valor faltante del lado derecho ",analizadorLexico.getNroLinea(),"Lexico","Error");
               //System.out.print("operador divisor con valor faltante del lado derecho ");
                break;
           case 4: // 'comienzo de posible comentario '
                analizadorLexico.addError("No se cierra el fin del comentario ",analizadorLexico.getNroLinea(),"Lexico","Error");
               //System.out.print("no se cierra el fin del comentario ");
                break;
           case 5: // 'nunca realiza el fin del cometaro'
               analizadorLexico.addError("Fin de comentario inexistente ",analizadorLexico.getNroLinea(),"Lexico","Error"); 
               //System.out.print("fin de comentario inexistente ");
                break;
           case 6: // 'caracter incorrecto: faltante de informacion del lado derecho de la regla'
                analizadorLexico.addError("Caracter incorrecto: faltante de informacion en comparadores ",analizadorLexico.getNroLinea(),"Lexico","Error");
               //System.out.print("Caracter incorrecto: faltante de informacion en comparadores ");
                break;
           case 7: // 'no cierra llaves de comentario'
               analizadorLexico.addError("Caracter incorrecto: faltante de informacion en comparadores ",analizadorLexico.getNroLinea(),"Lexico","Error"); 
               //System.out.print("Caracter incorrecto: faltante de informacion en comparadores ");
                break;
           case 8: // 'faltante finalizacion caracter igual'
                analizadorLexico.addError("Caracter incorrecto: faltante de informacion en igual",analizadorLexico.getNroLinea(),"Lexico","Error");
               //System.out.print("Caracter incorrecto: faltante de informacion en igual");
                break;
           case 9: // 'no cierra parentesis'
                analizadorLexico.addError("Caracter incorrecto: faltante cierre de parentesis ",analizadorLexico.getNroLinea(),"Lexico","Error");
               //System.out.print("Caracter incorrecto: faltante cierre de parentesis ");
                break;
           case 10: // 'no cierra llaves'
                analizadorLexico.addError("Se deben cerrar las llaves de funciones ",analizadorLexico.getNroLinea(),"Lexico","Error");
               //System.out.print("se deben cerrar las llaves de funciones ");
                break;
           case 11: // 'no finaliza cadena de caracteres "'
                analizadorLexico.addError("Caracter incorrecto se espera fin de cadena de caracteres",analizadorLexico.getNroLinea(),"Lexico","Error");
               //System.out.print("caracter incorrecto se espera fin de cadena de caracteres ");
                break;
           case 12: // 'se espera otro caracter tanto como para finalizar como un + para multicadena'
                analizadorLexico.addError("Faltante de caracter para continuar. Cadena incorrecta ",analizadorLexico.getNroLinea(),"Lexico","Error");
               //System.out.print("Faltante de caracter para continuar. Cadena incorrecta ");
                break;
           case 13: // 'faltante en la finalizacion del estado multilines'
                analizadorLexico.addError("faltante para continuar la cadena multiline ",analizadorLexico.getNroLinea(),"Lexico","Error");
               //System.out.print("faltante para continuar la cadena multiline ");
                break;

         
         
         }  
        
        
        
        return 0;
        }
    
    
}
