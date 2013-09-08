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
    
    
    final static String ET_LETRAS = "letras";
    final static String ET_DIGITOS = "digitos";
    final static String ET_ANGULARES = "<>";
    final static String ET_SALTO_LINEA = "\n";
    final static String ET_OPERADORES = "+-";
    final static String ET_IGUAL = "=";
    final static String ET_POR = "*";
    final static String ET_ESPACIO = " ";
    final static String ET_PUNTOCOMA = ";";
    
    final static int EST_INICIAL = 0;
    final static int EST_POSIBLE_IDENTIFICADOR = 1;
    final static int EST_POSIBLE_DIGITO = 2;
    final static int EST_FINAL = 69;
    
    final static String[] RESERVADAS = {"if", "then", "else", "begin", "end", "print", "function", "return"};
    
    public AnalizadorLexico() {
        this.matrizTransicion = new MatrizTransicion();
        this.cursorCar = 0;
        this.cargarGrafo();
        this.buffer = "";
        this.cadenaTemporal = "";
        this.nroLinea = 1;
        this.estadoTemporal = EST_INICIAL;
        this.tokens = new ArrayList();
        this.errors = new ArrayList();
        this.tablaSimbolos = new TablaSimbolos();
    }
    
    public void cargarGrafo() {
        AccionSemantica accionAgregar = new AccionAgregar(this);
        AccionSemantica accionNoAgregar = new AccionNoAgregar(this);
        AccionSemantica accionConsumir = new AccionConsumir(this);
        AccionSemantica accionNoConsumir = new AccionNoConsumir(this);
        AccionSemantica accionFin = new AccionFin(this);
        
        ArrayList<AccionSemantica> accionesAgregarConsumir = new ArrayList();
        accionesAgregarConsumir.add(accionAgregar);
        accionesAgregarConsumir.add(accionConsumir);
        
        ArrayList<AccionSemantica> accionesNoAgregarNoConsumir = new ArrayList();
        accionesNoAgregarNoConsumir.add(accionNoAgregar);
        accionesNoAgregarNoConsumir.add(accionNoConsumir);
        
        ArrayList<AccionSemantica> accionesNoAgregarConsumir = new ArrayList();
        accionesNoAgregarConsumir.add(accionNoAgregar);
        accionesNoAgregarConsumir.add(accionConsumir);
        
        // Reglas Identificar Identificadores
        this.matrizTransicion.setTransicion(EST_INICIAL, ET_LETRAS, EST_POSIBLE_IDENTIFICADOR, 
                accionesAgregarConsumir);
        this.matrizTransicion.setTransicion(EST_POSIBLE_IDENTIFICADOR, ET_LETRAS, EST_POSIBLE_IDENTIFICADOR, accionesAgregarConsumir);
        this.matrizTransicion.setTransicion(EST_POSIBLE_IDENTIFICADOR, ET_DIGITOS, EST_POSIBLE_IDENTIFICADOR, accionesAgregarConsumir);
        this.setTransicionOtros(EST_POSIBLE_IDENTIFICADOR, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar, accionNoConsumir, accionFin }), 
                (List<String>) Arrays.asList(new String[] { ET_LETRAS, ET_DIGITOS }));
        
        // Reglas Identificar Operador Asignacion  (=)
        this.matrizTransicion.setTransicion(EST_INICIAL, ET_IGUAL, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir, accionFin}));
        
        // Reglas Identificar Digitos
        this.matrizTransicion.setTransicion(EST_INICIAL, ET_DIGITOS, EST_POSIBLE_DIGITO, accionesAgregarConsumir);
        this.matrizTransicion.setTransicion(EST_POSIBLE_DIGITO, ET_DIGITOS, EST_POSIBLE_DIGITO, accionesAgregarConsumir);
        this.setTransicionOtros(EST_POSIBLE_DIGITO, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar, accionNoConsumir, accionFin }), 
                (List<String>) Arrays.asList(new String[] { ET_DIGITOS }));
        
        // Reglas Identificar Espacio
        this.matrizTransicion.setTransicion(EST_INICIAL, ET_ESPACIO, EST_INICIAL, accionesNoAgregarConsumir);
        // Reglas Identificar Punto/Coma
        this.matrizTransicion.setTransicion(EST_INICIAL, ET_PUNTOCOMA, EST_FINAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionAgregar, accionConsumir, accionFin }));
        
        // Reglas Identificar Salto Línea
        this.matrizTransicion.setTransicion(EST_INICIAL, ET_SALTO_LINEA, EST_INICIAL, 
                (List<AccionSemantica>) Arrays.asList(new AccionSemantica[] { accionNoAgregar, accionConsumir }));
        
    }
    
    public Token getNextToken() {
        this.cadenaTemporal = "";
        this.estadoTemporal = 0;
        String etiquetaEntrada = "";
        for (; this.cursorCar < buffer.length(); this.cursorCar++) {
            char caracter = buffer.charAt(cursorCar);
            etiquetaEntrada = clasificacionEntrada(caracter);
            Transicion transicion = this.matrizTransicion.getTransicion(this.estadoTemporal, etiquetaEntrada);
            if (this.estadoTemporal == this.EST_FINAL) {
                return this.getLastToken();
            } else {
                if (transicion == null) {
                    Error err = new Error("La entrada del símbolo " + caracter + " luego de la cadena " + this.cadenaTemporal +
                            " no es un token valido para el lenguaje", this.getNroLinea());
                    this.addError(err);
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
        return null;
    }
    
    public void goBack() {
        this.cursorCar--;
    }
    
    public void saveTempChar(char c) {
        this.cadenaTemporal += c;
    }
    
    public void setBuffer(String buffer) {
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
    public char getCaracterActual() {
        return this.buffer.charAt(cursorCar);        
    }
    
    /**
     * Devuelve el estado temporal, que está analizando el analizador léxico
     * 
     * @return 
     */
    public int getEstado() {
        return this.estadoTemporal;
    }
    
    public void addToken(Token token) {
        this.tokens.add(token);
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
            return "/";

        case 42: // '*'
            return "*";

        case 40: // '('
            return "(";

        case 41: // ')'
            return ")";

        case 123: // '{'
            return "{";

        case 125: // '}'
            return "}";

        case 43: // '+'
            return "+";

        case 45: // '-'
            return "-";

        case 59: // ';'
            return ET_PUNTOCOMA;

        case 44: // ','
            return ",";

        case 32: // ' '
            return " ";

        case 9: // '\t'
            return "\t";

        case 10: // '\n'
            return "\n";

        case 13: // '\r'
            return "\r";

        case 34: // '"'
            return "\"";

        case 61: // '='
            return this.ET_IGUAL;

        case 60: // '<'
            return this.ET_ANGULARES;

        case 62: // '>'
            return this.ET_ANGULARES;

        case 33: // '!'
            return "!";
        }
        return "otro";
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
        String[] etiquetas = {ET_LETRAS,ET_DIGITOS,ET_ANGULARES,ET_OPERADORES,ET_IGUAL,ET_POR,ET_ESPACIO, ET_PUNTOCOMA, ET_SALTO_LINEA};
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

    private void addError(Error err) {
        this.errors.add(err);
    }


}
