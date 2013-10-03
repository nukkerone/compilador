/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;
import herramientaerror.EventoError;
/**
 *
 * @author damian
 */
public class AccionDescartarUltimoValor extends AccionSemantica{
    AccionDescartarUltimoValor(AnalizadorLexico analizador, EventoError e) {
        super(analizador, e);
    }

@Override
    int ejecutar() {
    String Aux;
        int ind = this.analizadorLexico.getCadenaTemporal().lastIndexOf("+");
    Aux = new StringBuilder(this.analizadorLexico.getCadenaTemporal()).replace(ind, ind+1,"").toString();
    this.analizadorLexico.serCadenaTemporal(Aux);
        return 0;
    }
}

