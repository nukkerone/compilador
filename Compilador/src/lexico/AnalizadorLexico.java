/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import compilador.Error;
import herramientaerror.EventoError;

/**
 *
 * @author Gabriel
 */
public class AnalizadorLexico {
    private MatrizTransicion matrizTransicion;
    private int cursorCar;
    private String buffer;
    private String cadenaTemporal;
    private int estadoTemporal;
    private int nroLinea;
    private ArrayList<Token> tokens;
    private ArrayList<Error> errors;
    private TablaSimbolos tablaSimbolos;
    private int lineaInicUltimoToken;
    private AccionSemantica accionFinError;
    private EventoError eventoError;
    
    
    final static String ET_LETRAS = "letras";
    final static String ET_DIGITOS = "digitos";
    final static String ET_DIVISOR = "/";
    final static String ET_ANGMAYOR = ">";
    final static String ET_ANGMENOR = "<";
    final static String ET_MAS = "+";
    final static String ET_MENOS = "-";
    //final static String ET_OPERADORES = "+-";
    final static String ET_IGUAL = "=";
    final static String ET_POR = "*";
    final static String ET_ESPACIO = " ";
    final static String ET_PUNTOCOMA = ";";
    final static String ET_SALTO_LINEA = "\n";
    final static String ET_TAB = "\t";
    final static String ET_ABRE_PARENT = "(";
    final static String ET_CIERRA_PARENT = ")";
    final static String ET_ABRE_LLAVE = "{";
    final static String ET_CIERRA_LLAVE = "}";
    final static String ET_CADENA = "\"";
    final static String ET_BARRA_INVERTIDA = "\\";
    final static String ET_EXCLAMACION = "!";
    final static String ET_COMA = ",";
    final static String ET_OTRO = "otro";
    final static String ET_EOF = "EOF";
    
    final static int EST_INICIAL = 0;
    final static int EST_POSIBLE_IDENTIFICADOR = 1;
    final static int EST_POSIBLE_DIGITO = 2;
    final static int EST_POSIBLE_COMENTARIO = 3;
    final static int EST_COMENTARIO = 4;
    final static int EST_POSIBLE_FIN_COMENTARIO = 5;
    final static int EST_COMPMAYOR = 6;
    final static int EST_COMPMENOR = 7;
    final static int EST_IGUAL = 8;
    final static int EST_PARENTESIS = 9;
    final static int EST_LLAVES = 10;
    final static int EST_CADENA_CARACTER = 11;
    final static int EST_POSIBLE_MULTILINEA = 12;
    final static int EST_MULTILINEA = 13;
    final static int EST_SALTO_LINEA = 14;
    
    final static int EST_FINAL = 69;
    
    final static String[] RESERVADAS = {"if", "then", "else", "begin", "end", "print", "function", "return", "int", "string", "for"};
    
    public AnalizadorLexico(EventoError eventoError) {
        this.matrizTransicion = new MatrizTransicion();
        this.cursorCar = 0;
        this.buffer = "";
        this.cadenaTemporal = "";
        this.nroLinea = 1;
        this.lineaInicUltimoToken = 1;
        this.estadoTemporal = EST_INICIAL;
        this.tokens = new ArrayList();
        this.errors = new ArrayList();
        this.tablaSimbolos = new TablaSimbolos();
        this.eventoError = eventoError;
        this.accionFinError = new AccionFinError(this, this.eventoError);
        this.cargarGrafo();
    }
    
    public void cargarGrafo() {
        AccionSemantica accionAgregar = new AccionAgregar(this, this.eventoError);
        AccionSemantica accionNoAgregar = new AccionNoAgregar(this, this.eventoError);
        AccionSemantica accionLimpiarCadenaTemp = new AccionLimpiarCadenaTemp(this, this.eventoError);
        AccionSemantica accionConsumir = new AccionConsumir(this, this.eventoError);
        AccionSemantica accionNoConsumir = new AccionNoConsumir(this, this.eventoError);
        AccionSemantica accionFin = new AccionFin(this, this.eventoError);
         AccionSemantica accionDescartarUltimoValor = new AccionDescartarUltimoValor(this, this.eventoError);
       
/******** NO VAN MASSS*////
        
        ArrayList<AccionSemantica> accionesAgregarConsumir = new ArrayList();
        accionesAgregarConsumir.add(accionAgregar);
        accionesAgregarConsumir.add(accionConsumir);
        
        ArrayList<AccionSemantica> accionesNoAgregarNoConsumir = new ArrayList();
        accionesNoAgregarNoConsumir.add(accionLimpiarCadenaTemp);
        accionesNoAgregarNoConsumir.add(accionNoConsumir);
        
        ArrayList<AccionSemantica> accionesNoAgregarConsumir = new ArrayList();
        accionesNoAgregarConsumir.add(accionLimpiarCadenaTemp);
        accionesNoAgregarConsumir.add(accionConsumir);
/*********** HASTA ACA*************************/
 
       //DISTINTO
         this.matrizTransicion.setTransicion(EST_INICIAL, ET_EXCLAMACION, EST_IGUAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar,accionConsumir}));
         this.matrizTransicion.setTransicion(EST_IGUAL, ET_IGUAL, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar,accionConsumir, accionFin }));
       
            
        
 //Parentesis
         /*this.matrizTransicion.setTransicion(EST_INICIAL, ET_ABRE_PARENT, EST_PARENTESIS, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar,accionConsumir}));
         this.setTransicionOtros(EST_PARENTESIS, EST_PARENTESIS, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar, accionNoConsumir, accionFin }), 
                (List<String>) Arrays.asList(new String[] { ET_CIERRA_PARENT }));
         this.matrizTransicion.setTransicion(EST_PARENTESIS, ET_CIERRA_PARENT, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar,accionConsumir, accionFin }));
          */
         this.matrizTransicion.setTransicion(EST_INICIAL, ET_ABRE_PARENT, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir, accionFin}));
         this.matrizTransicion.setTransicion(EST_INICIAL, ET_CIERRA_PARENT, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir, accionFin}));
         

        this.matrizTransicion.setTransicion(EST_INICIAL, ET_ABRE_LLAVE, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir, accionFin}));
        this.matrizTransicion.setTransicion(EST_INICIAL, ET_CIERRA_LLAVE, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir, accionFin}));
         

        //multilineas  
        this.matrizTransicion.setTransicion(EST_INICIAL, ET_CADENA, EST_CADENA_CARACTER, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar,accionConsumir}));
        this.setTransicionOtros(EST_CADENA_CARACTER, EST_CADENA_CARACTER, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir }), 
                (List<String>) Arrays.asList(new String[] { ET_CADENA, ET_MAS }));
        this.matrizTransicion.setTransicion(EST_CADENA_CARACTER, ET_CADENA, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar,accionConsumir,accionFin}));
        this.matrizTransicion.setTransicion(EST_CADENA_CARACTER, ET_MAS, EST_MULTILINEA, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar,accionConsumir}));
        this.setTransicionOtros(EST_MULTILINEA, EST_CADENA_CARACTER, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir }), 
                (List<String>) Arrays.asList(new String[] { ET_CADENA,ET_SALTO_LINEA }));
        this.matrizTransicion.setTransicion(EST_MULTILINEA, ET_SALTO_LINEA,EST_CADENA_CARACTER, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar,accionConsumir,accionDescartarUltimoValor}));
        this.matrizTransicion.setTransicion(EST_MULTILINEA, ET_CADENA,EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar,accionConsumir,accionFin}));
        
        /*
         this.matrizTransicion.setTransicion(EST_INICIAL, ET_CADENA, EST_CADENA_CARACTER, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar,accionConsumir}));
         this.setTransicionOtros(EST_CADENA_CARACTER, EST_CADENA_CARACTER, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir }), 
                (List<String>) Arrays.asList(new String[] { ET_CADENA }));
         
         
         
         this.matrizTransicion.setTransicion(EST_CADENA_CARACTER, ET_CADENA, EST_POSIBLE_MULTILINEA, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar,accionConsumir}));
         this.setTransicionOtros(EST_POSIBLE_MULTILINEA, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar, accionNoConsumir, accionFin }), 
                (List<String>) Arrays.asList(new String[] { ET_MAS }));
         this.matrizTransicion.setTransicion(EST_POSIBLE_MULTILINEA, ET_MAS, EST_MULTILINEA, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar,accionConsumir }));
         this.matrizTransicion.setTransicion(EST_MULTILINEA, ET_CADENA, EST_CADENA_CARACTER, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar,accionConsumir }));
         
         this.matrizTransicion.setTransicion(EST_MULTILINEA, ET_SALTO_LINEA, EST_SALTO_LINEA, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar,accionConsumir }));
         this.matrizTransicion.setTransicion(EST_SALTO_LINEA, ET_SALTO_LINEA, EST_SALTO_LINEA, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar,accionConsumir }));
         this.matrizTransicion.setTransicion(EST_SALTO_LINEA, ET_MAS, EST_MULTILINEA, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar,accionConsumir }));
         this.setTransicionOtros(EST_SALTO_LINEA, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar, accionNoConsumir, accionFin }), 
                (List<String>) Arrays.asList(new String[] { ET_MAS,ET_SALTO_LINEA }));
        */         
                 
// Reglas Comparadores MENOR
        this.matrizTransicion.setTransicion(EST_INICIAL, ET_ANGMENOR, EST_COMPMENOR, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar,accionConsumir}));
        this.matrizTransicion.setTransicion(EST_COMPMENOR, ET_IGUAL, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar,accionConsumir, accionFin }));
        this.setTransicionOtros(EST_COMPMENOR, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar, accionNoConsumir, accionFin }), 
                (List<String>) Arrays.asList(new String[] { ET_IGUAL  }));
        
        
// Reglas Comparadores MAYOR
        this.matrizTransicion.setTransicion(EST_INICIAL, ET_ANGMAYOR, EST_COMPMAYOR,
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar,accionConsumir}));
        this.matrizTransicion.setTransicion(EST_COMPMAYOR, ET_IGUAL, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir, accionFin }));
        this.setTransicionOtros(EST_COMPMAYOR, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar, accionNoConsumir, accionFin }), 
                (List<String>) Arrays.asList(new String[] { ET_IGUAL })); 
        
        
// Reglas COMENTARIO ADEMAS SE OBTIENE LA BARRA O DIVISION
        
      this.matrizTransicion.setTransicion(EST_INICIAL, ET_BARRA_INVERTIDA, EST_POSIBLE_COMENTARIO, 
         (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir }));
         this.setTransicionOtros(EST_POSIBLE_COMENTARIO, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar, accionNoConsumir, accionFin }), 
                (List<String>) Arrays.asList(new String[] { ET_BARRA_INVERTIDA }));
       this.matrizTransicion.setTransicion(EST_POSIBLE_COMENTARIO, ET_BARRA_INVERTIDA, EST_COMENTARIO, 
         (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir}));
        this.setTransicionOtros(EST_COMENTARIO, EST_COMENTARIO, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir}), 
                (List<String>) Arrays.asList(new String[] { ET_SALTO_LINEA}));
        this.matrizTransicion.setTransicion(EST_COMENTARIO,ET_SALTO_LINEA , EST_INICIAL, 
         (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir,accionLimpiarCadenaTemp }));
        
        
        
        
        
        
        
        
      /*  this.matrizTransicion.setTransicion(EST_INICIAL, ET_DIVISOR, EST_POSIBLE_COMENTARIO, 
         (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir }));
         this.setTransicionOtros(EST_POSIBLE_COMENTARIO, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar, accionNoConsumir, accionFin }), 
                (List<String>) Arrays.asList(new String[] { ET_POR }));
         
        this.matrizTransicion.setTransicion(EST_POSIBLE_COMENTARIO, ET_POR, EST_COMENTARIO, 
         (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir }));
        this.setTransicionOtros(EST_COMENTARIO, EST_COMENTARIO, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir}), //modificado
                (List<String>) Arrays.asList(new String[] { ET_POR}));
        this.matrizTransicion.setTransicion(EST_COMENTARIO, ET_POR, EST_POSIBLE_FIN_COMENTARIO, 
         (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir }));
        this.matrizTransicion.setTransicion(EST_POSIBLE_FIN_COMENTARIO, ET_POR, EST_POSIBLE_FIN_COMENTARIO, 
         (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir }));
        this.setTransicionOtros(EST_POSIBLE_FIN_COMENTARIO, EST_COMENTARIO, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir}), //modificado
                (List<String>) Arrays.asList(new String[] { ET_POR, ET_DIVISOR }));
        this.matrizTransicion.setTransicion(EST_POSIBLE_FIN_COMENTARIO, ET_DIVISOR, EST_INICIAL, 
         (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionLimpiarCadenaTemp }));
     */   
// Reglas Identificar Identificadores
        this.matrizTransicion.setTransicion(EST_INICIAL, ET_LETRAS, EST_POSIBLE_IDENTIFICADOR, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir}));
        this.matrizTransicion.setTransicion(EST_POSIBLE_IDENTIFICADOR, ET_LETRAS, EST_POSIBLE_IDENTIFICADOR, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir }));
        this.matrizTransicion.setTransicion(EST_POSIBLE_IDENTIFICADOR, ET_DIGITOS, EST_POSIBLE_IDENTIFICADOR,
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir }));
        this.setTransicionOtros(EST_POSIBLE_IDENTIFICADOR, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar, accionNoConsumir, accionFin }), 
                (List<String>) Arrays.asList(new String[] { ET_LETRAS, ET_DIGITOS }));
       
        // Reglas identificar operadores
        this.matrizTransicion.setTransicion(EST_INICIAL, ET_MAS, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir, accionFin}));
        this.matrizTransicion.setTransicion(EST_INICIAL, ET_MENOS, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir, accionFin}));
        this.matrizTransicion.setTransicion(EST_INICIAL, ET_POR, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir, accionFin}));
        
 // Reglas Identificar Operador Asignacion  (=)
        this.matrizTransicion.setTransicion(EST_INICIAL, ET_IGUAL, EST_IGUAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir}));
        this.matrizTransicion.setTransicion(EST_IGUAL, ET_IGUAL, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir,accionFin}));
        this.setTransicionOtros(EST_IGUAL, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar, accionNoConsumir, accionFin }), 
                (List<String>) Arrays.asList(new String[] { ET_IGUAL}));
        
        
        
 // Reglas Identificar Digitos
        this.matrizTransicion.setTransicion(EST_INICIAL, ET_DIGITOS, EST_POSIBLE_DIGITO, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir }));
        this.matrizTransicion.setTransicion(EST_POSIBLE_DIGITO, ET_DIGITOS, EST_POSIBLE_DIGITO,
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir }));
        this.setTransicionOtros(EST_POSIBLE_DIGITO, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar, accionNoConsumir, accionFin }), 
                (List<String>) Arrays.asList(new String[] { ET_DIGITOS }));
        
 // Reglas Identificar Espacio
        this.matrizTransicion.setTransicion(EST_INICIAL, ET_ESPACIO, EST_INICIAL,
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar, accionConsumir }));
 // Reglas Identificar Tab
        this.matrizTransicion.setTransicion(EST_INICIAL, ET_TAB, EST_INICIAL,
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar, accionConsumir }));
        
 // Reglas Identificar PuntoyComa
        this.matrizTransicion.setTransicion(EST_INICIAL, ET_PUNTOCOMA, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir, accionFin }));

        
// Reglas Identificar Coma
        this.matrizTransicion.setTransicion(EST_INICIAL, ET_COMA, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir, accionFin }));
        
        
        // Reglas Identificar Salto Linea
        this.matrizTransicion.setTransicion(EST_INICIAL, ET_SALTO_LINEA, EST_INICIAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar, accionConsumir }));
    }
    
    public Token getNextToken() {
        this.cadenaTemporal = "";
        this.estadoTemporal = 0;
        String etiquetaEntrada = "";
        lineaInicUltimoToken = this.getNroLinea();
        for (; this.cursorCar <= buffer.length(); this.cursorCar++) {
            int bufferLength = buffer.length();
            if (this.cursorCar == bufferLength) {    // Esto es cuando ya se terminaron los caracteres
                etiquetaEntrada = this.ET_EOF;  // La etiqueta será el Fin de entrada
            } else {
                char caracter = buffer.charAt(cursorCar);
                etiquetaEntrada = clasificacionEntrada(caracter);
            }
            
            Transicion transicion = this.matrizTransicion.getTransicion(this.estadoTemporal, etiquetaEntrada);
            if (this.estadoTemporal == AnalizadorLexico.EST_FINAL) {
                return this.getLastToken();
            } else {
                  if (transicion == null) {
                    //Error err = new Error("La entrada del símbolo " + caracter + " luego de la cadena " + this.cadenaTemporal +
                      //      " no es un token valido para el lenguaje", this.getNroLinea());
                   // this.addError(err);
                    return this.getLastToken(); // Intento devolver el siguiente token
                }
            }
            
            transicion.ejecutarAccion(this);
            this.estadoTemporal = transicion.getEstado();
        }
        if (this.estadoTemporal == this.EST_FINAL) {
            if (this.tokens.size() > 0) {
                return this.getLastToken();
            }
        }
        else{
            int errorlex;
            errorlex = accionFinError.ejecutar();
           }
        
        return null;
    }
    
    
    /***************************************************************************************************************/
    
    public void addError(String m,int Nl,String Te,String E){
       this.eventoError.add(m, Nl, Te,E);
     }  
    
    
    /**borrar**/
    
    
    public void visualizar(){
     ArrayList<String> aux = new ArrayList<String>();
     aux = eventoError.visualizarMensaje();
      Iterator<String> IterError = aux.iterator();
      String simbolo;
      while(IterError.hasNext()) {
            simbolo = IterError.next();
          System.out.println(simbolo);
      }
    
    }
    public int getEstadoTemporal() {
        return this.estadoTemporal;
    }
    public void goBack() {
        this.cursorCar--;
    }
    public int getlineaInicUltimoToken(){
        return this.lineaInicUltimoToken;
    }
    
    public void saveTempChar(char c) {
        this.cadenaTemporal += c;
    }
    public void elimino_ultimo_valor(){
        int ind = this.cadenaTemporal.lastIndexOf("+");
       this.cadenaTemporal = new StringBuilder(this.cadenaTemporal).replace(ind, ind+1,"").toString();
   }
     public void emptyTempChar() {
        this.cadenaTemporal ="";
    }
    
    public void setBuffer(String buffer) {
        this.reset();
        this.buffer = buffer;
    }
    
    public boolean hasNext() {
        if (this.cursorCar < this.buffer.length()) {
            return true;
        }
        return false;
    }
    
    public String getCadenaTemporal() {
        return this.cadenaTemporal;
    }
    
    /**
     * Devuelve el caracter en la posicion del cursor actual
     * 
     * @return 
     */
    public Character getCaracterActual() {
        if (this.cursorCar == this.buffer.length()) {    // Esto es cuando ya se terminaron los caracteres
            return null;  // La etiqueta será el Fin de entrada
        } else {
            return (Character) this.buffer.charAt(cursorCar);
        }
                
    }
    
    /**
     * Devuelve el estado temporal, que está analizando el analizador léxico
     * 
     * @return 
     */
    public int getEstado() {
        return this.estadoTemporal;
    }
    public void serCadenaTemporal(String cade){
            this.cadenaTemporal = cade;
    }
    public void addToken(Token token) {
        this.tokens.add(token);
        if (token.getLexema().equals(token.getPalabraReservada())) {    // En este caso el toke no va a la tabla de simbolos
            return;
        }
        this.tablaSimbolos.addSimbolo(token);
    }
    
    public void reset() {
        this.tokens.clear();
        this.errors.clear();
        this.cadenaTemporal = "";
        this.estadoTemporal = this.EST_INICIAL;
        this.cursorCar = 0;
        this.buffer = "";
        this.tablaSimbolos.reset();
    }
    
    public void printSimbolos() {
        Iterator<Token> tablaSimbolosIterator = this.tablaSimbolos.createIterator();
        while(tablaSimbolosIterator.hasNext()) {
            Token simbolo = tablaSimbolosIterator.next();
            System.out.println("Simbolo: " + simbolo.getLexema() );
        }                                                                                                                                                                                                          
    }
    
    public String clasificacionEntrada(char c)
    {
        if(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' )
            return ET_LETRAS;
        if(c >= '0' && c <= '9')
            return ET_DIGITOS;
        switch(c)
        {
        case 47: // '/'
            return ET_DIVISOR;

        case 42: // '*'
            return ET_POR;

        case 40: // '('
            return ET_ABRE_PARENT;

        case 41: // ')'
            return ET_CIERRA_PARENT;

        case 123: // '{'
            return ET_ABRE_LLAVE;

        case 125: // '}'
            return ET_CIERRA_LLAVE;

        case 43: // '+'
            return ET_MAS;

        case 45: // '-'
            return ET_MENOS;

        case 59: // ';'
            return ET_PUNTOCOMA;

        case 44: // ','
            return ET_COMA;

        case 32: // ' '
            return ET_ESPACIO;

        case 9: // '\t'
            return ET_TAB;

        case 10: // '\n'
            return ET_SALTO_LINEA;

        case 13: // '\r'
            return "\r";

        case 34: // '"'
            return ET_CADENA;
            
        case 92: // '\\'
            return ET_BARRA_INVERTIDA;

        case 61: // '='
            return this.ET_IGUAL;

        case 60: // '<'
            return this.ET_ANGMAYOR;

        case 62: // '>'
            return this.ET_ANGMENOR;

        case 33: // '!'
            return ET_EXCLAMACION;
        }
        
        return ET_OTRO;
    }

    /**
     * Setea todas las transiciones para 'Otros', es dinamico y depende de la etiqueta a evitar
     * 
     * @param estadoActual
     * @param estadoSiguiente
     * @param acciones
     * @param etiquetaAEvitar 
     */
    private void setTransicionOtros(int estadoActual, int estadoSiguiente, List<AccionSemantica> acciones, List<String> etiquetaAEvitar) {
        String[] etiquetas = {ET_LETRAS,ET_DIGITOS,ET_DIVISOR,ET_ANGMENOR,ET_ANGMAYOR,ET_IGUAL,ET_POR,
            ET_ESPACIO, ET_PUNTOCOMA, ET_SALTO_LINEA,ET_ABRE_LLAVE,ET_CIERRA_LLAVE,ET_ABRE_PARENT,ET_CIERRA_PARENT,
            ET_CADENA, ET_BARRA_INVERTIDA, ET_EXCLAMACION,ET_COMA,ET_OTRO, ET_EOF, ET_MAS, ET_MENOS, ET_TAB};
        ArrayList<String> etiquetasArray = new ArrayList(Arrays.asList(etiquetas));
        for(int i=0; i<etiquetasArray.size(); i++) {
            String etiqueta = etiquetasArray.get(i);
            if ( !etiquetaAEvitar.contains(etiqueta) ) {
                this.matrizTransicion.setTransicion(estadoActual, etiqueta, estadoSiguiente, acciones);
            }
        }
    }
    
      
   boolean esReservada(String cadenaTemporal) {
        return Arrays.asList(this.RESERVADAS).contains(cadenaTemporal);
    }

    private Token getLastToken() {
        if (this.tokens.size() > 0) {
            return this.tokens.get(this.tokens.size()-1);
        }
        
        return null;        
    }

    public void avanzarLinea() {
        this.nroLinea++;
    }
    
    public int getNroLinea() {
        return this.nroLinea;
    }

   /* private void addError(Error err) {
        this.errors.add(err);
    }*/
    
   public void visualizarTablaSimbolos() {
       Iterator it = this.tablaSimbolos.createIterator();
       System.out.println("Tabla de símbolos: ");
       while (it.hasNext()) {
           Token t = (Token)it.next();
           System.out.println("Token: " + t);
       }
   }


}
