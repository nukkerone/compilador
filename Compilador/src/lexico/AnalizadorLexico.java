/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

/**
 *
 * @author Gabriel
 */
public class AnalizadorLexico {
    private MatrizTransicion matrizTransicion;
    private int estadoActual;
    private int cursorCar;
    
    final static String ET_LETRAS = "letras";
    final static String ET_DIGITOS = "digitos";
    final static String ET_ANGULARES = "<>";
    final static String ET_OPERADORES = "+-";
    final static String ET_POR = "*";
    final static String ET_ESPACIO = " ";
    
    final static int EST_INICIAL = 0;
    final static int EST_POSIBLE_IDENTIFICADOR = 1;
    final static int EST_FINAL = 69;
    
    public AnalizadorLexico() {
        this.matrizTransicion = new MatrizTransicion();
        this.estadoActual = EST_INICIAL;
        this.cursorCar = 0;
        this.cargarGrafo();
    }
    
    public void cargarGrafo() {
        AccionSemantica accionSimple = new AccionAgregar();
        
        // Flujo identificador
        this.matrizTransicion.setTransicion(EST_INICIAL, ET_LETRAS, EST_POSIBLE_IDENTIFICADOR, accionSimple);
        this.matrizTransicion.setTransicion(EST_POSIBLE_IDENTIFICADOR, ET_LETRAS, EST_POSIBLE_IDENTIFICADOR, accionSimple);
        this.matrizTransicion.setTransicion(EST_POSIBLE_IDENTIFICADOR, ET_DIGITOS, EST_POSIBLE_IDENTIFICADOR, accionSimple);
        this.matrizTransicion.setTransicion(EST_POSIBLE_IDENTIFICADOR, ET_ANGULARES, EST_FINAL, accionSimple);
        this.matrizTransicion.setTransicion(EST_POSIBLE_IDENTIFICADOR, ET_OPERADORES, EST_FINAL, accionSimple);
        this.matrizTransicion.setTransicion(EST_POSIBLE_IDENTIFICADOR, ET_POR, EST_FINAL, accionSimple);
        this.matrizTransicion.setTransicion(EST_POSIBLE_IDENTIFICADOR, ET_ESPACIO, EST_FINAL, accionSimple);
    }
    
    public String getToken(String cadena) throws Exception {
        cadena = "v9ar identificador1 = 90";
        int estado = 0;
        String etiquetaEntrada = "";
        for (; this.cursorCar < cadena.length(); this.cursorCar++) {
            etiquetaEntrada = clasificacionEntrada(cadena.charAt(cursorCar));
            Transicion transicion = this.matrizTransicion.getTransicion(estado, etiquetaEntrada);
            if (transicion == null) {
                throw new Exception("No se encontro transición para el estado/entrada");
            }
            estado = transicion.getEstado();
            transicion.ejecutarAccion();
        }
        return "";
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
            return ";";

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
            return "=";

        case 60: // '<'
            return this.ET_ANGULARES;

        case 62: // '>'
            return this.ET_ANGULARES;

        case 33: // '!'
            return "!";
        }
        return "otro";
    }


}
