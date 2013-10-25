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
    
    public TypeableToken(String palabraReservada) {
        super(palabraReservada);
    }
}
