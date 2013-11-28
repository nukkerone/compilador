/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

import interfaces.Typeable;

/**
 *
 * @author Gabriel
 */
public abstract class TypeableToken extends Token implements Typeable {
    protected int tipo;
    
    public TypeableToken(String palabraReservada) {
        super(palabraReservada);
        this.tipo = Typeable.TIPO_DESCONOCIDO;
    }
    
    public String getInitialValue() {
        if (this.getTipo() == Typeable.TIPO_CADENA) {
            //String aux = lexema.substring(1, lexema.length()-1);
            //return "'"+aux+"$'";
            return "'" + this.getLexema() + "'";
        }
        
        if (this.getTipo() == Typeable.TIPO_CTE_ENTERA) {
            //String aux = lexema.substring(1, lexema.length()-1);
            //return "'"+aux+"$'";
            return  this.getLexema();
        }
        
        if (this.getTipo() == Typeable.TIPO_INT) {
            //String aux = lexema.substring(1, lexema.length()-1);
            //return "'"+aux+"$'";
            return "0";
        }
        
        return "'?'";
    }
    
    @Override
    public int getTipo() {
        return this.tipo;
    }
    
    @Override
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    
    public String getTipoAmigable() {
        String tipoAmigable = "";
        int tipo = this.getTipo();
        switch (tipo) {
            case Typeable.TIPO_DESCONOCIDO:  tipoAmigable = "Desconocido";
                break;
            case Typeable.TIPO_RECIEN_DECLARADA:  tipoAmigable = "Recien Declarada";
                break;
            case Typeable.TIPO_INT:  tipoAmigable = "Int";
                break;
            case Typeable.TIPO_CADENA:  tipoAmigable = "Cadena";
                break;
            case Typeable.TIPO_CTE_ENTERA:  tipoAmigable = "Cte Entera";
                break;
        }
                
        return tipoAmigable;
    }
}
