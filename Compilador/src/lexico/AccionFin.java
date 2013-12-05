/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

import herramientaerror.EventoError;

/**
 *
 * @author Gabriel
 */
public class AccionFin extends AccionSemantica{
    final static int MAX_CHAR_ID = 15;
    final static int MAX_INT_VAL = 32767;
    final static int MIN_INT_VAL = -32768;
    
    AccionFin(AnalizadorLexico analizador, EventoError e) {
        super(analizador, e);
        
    }
    
    @Override
    int ejecutar() {
        Token token = null;
        int estado = analizadorLexico.getEstado();
        String cadenaTemporal = analizadorLexico.getCadenaTemporal();
        switch(estado)
        {
        case 1: // Identificando Identificador
            if (analizadorLexico.esReservada(cadenaTemporal)) {
                token = new Token(cadenaTemporal);
            } else {
                if (cadenaTemporal.length() > AccionFin.MAX_CHAR_ID) {
                    String temp = cadenaTemporal;
                    cadenaTemporal = cadenaTemporal.substring(0, AccionFin.MAX_CHAR_ID);
                    this.eventoError.add("Identificador: " + temp + " superaba el máximo de carácteres permitidos, se truncó a: " + cadenaTemporal , this.analizadorLexico.getNroLinea(), "Lexico", "Warning");
                }
                token = new TokenLexemaDistinto("ID", cadenaTemporal );
            }
            break;
        case 2: // Identificando Constantes (Digitos)
            if (cadenaTemporal.length() > 5) {
                cadenaTemporal = cadenaTemporal.substring(0, 6);
            }
            if (Integer.parseInt( cadenaTemporal ) > AccionFin.MAX_INT_VAL) {
                String temp = cadenaTemporal;
                cadenaTemporal = Integer.toString(AccionFin.MAX_INT_VAL);
                this.eventoError.add("Constante: " + temp + " superaba el máximo valor permitido, se re-asignó al máximo: " + cadenaTemporal , this.analizadorLexico.getNroLinea(), "Lexico", "Warning");
            } else {
                if (Integer.parseInt( cadenaTemporal ) < AccionFin.MIN_INT_VAL) {
                    String temp = cadenaTemporal;
                    cadenaTemporal = Integer.toString(AccionFin.MIN_INT_VAL);
                    this.eventoError.add("Constante: " + temp + " superaba el minimo valor permitido, se re-asignó al mínimo: " + cadenaTemporal , this.analizadorLexico.getNroLinea(), "Lexico", "Warning");
                }
            }
            
            token = new TokenLexemaDistinto("CTE", cadenaTemporal);
            break;
        case 11:    // Identificando String (cadena de caracteres)
            token = new TokenLexemaDistinto("string", cadenaTemporal);
            break;
        case 12: // Identificando String (cadena de caracteres)
            token = new TokenLexemaDistinto("string", cadenaTemporal);
            break;
        case 13: // Identificando String (cadena de caracteres)
            token = new TokenLexemaDistinto("string", cadenaTemporal);
            break;    
   //     case
        default:
            token = new Token(cadenaTemporal);
            break;
        }
        token.setNroLinea(this.analizadorLexico.getlineaInicUltimoToken());
        analizadorLexico.addToken(token);
        
        return 0;
    }
    
}
