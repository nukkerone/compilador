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
            case Typeable.TIPO_int:  tipoAmigable = "Int";
                break;
        }
                
        return tipoAmigable;
    }
}
