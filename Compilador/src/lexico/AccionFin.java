/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

/**
 *
 * @author Gabriel
 */
public class AccionFin extends AccionSemantica{
    
    AccionFin(AnalizadorLexico analizador) {
        super(analizador);
        
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
                token = new TokenLexemaDistinto("ID", cadenaTemporal );
            }
            break;
        case 2: // Identificando Constantes (Digitos)
            token = new TokenLexemaDistinto("CTE", cadenaTemporal);
            break;
        default:
            token = new Token(cadenaTemporal);;
            break;
        }
        token.setNroLinea(this.analizadorLexico.getNroLinea());
        analizadorLexico.addToken(token);
        
        return 0;
    }
    
}
