/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

import interfaces.Uso;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

/**
 *
 * @author Gabriel
 */
public class TablaSimbolos implements Uso{
    private Hashtable<IdTS, Token> simbolos;
    
    TablaSimbolos() {
        this.simbolos = new Hashtable();
        this.cargarSimbolosIniciales();
    }
    
    public void addSimbolo(Token token, boolean force) {
        String lexema = token.getLexema();
        int uso = token.getUso();
        IdTS storeKey = new IdTS(lexema, uso);
        if(force || !simbolos.containsKey(storeKey)) {
            this.removeSimbolo(storeKey);
            simbolos.put(storeKey, token);
        }
    }  
    
    public boolean contains(IdTS searchKey) {
        if(simbolos.containsKey(searchKey)) {
            return true;
        }
        return false;
    }
    
    public Token getSimbolo(IdTS id) {
        if(!simbolos.contains(id)) {
            return simbolos.get(id);
        }
        return null;
    }
    
    /**
     * Remueve la key de la tabla de simbolos
     * 
     * @param searchKey 
     */
    public void removeSimbolo(IdTS id) {
        this.simbolos.remove(id);
    }
    
    /**
     * Devuelve un iterador para recorrer los simbolos
     * @return 
     */
    public Iterator createIterator() {
        ArrayList<Token> elements = new ArrayList(Collections.list(this.simbolos.elements()));
        return elements.iterator();
    }
    
    public void cargarSimbolosIniciales() {
        this.addSimbolo(new Token("if"), false);
        this.addSimbolo(new Token("then"), false);
        this.addSimbolo(new Token("else"), false);
        this.addSimbolo(new Token("begin"), false);
        this.addSimbolo(new Token("end"), false);
        this.addSimbolo(new Token("print"), false);
        this.addSimbolo(new Token("function"), false);
        this.addSimbolo(new Token("return"), false);
        this.addSimbolo(new Token("+"), false);
        this.addSimbolo(new Token("-"), false);
        this.addSimbolo(new Token("*"), false);
        this.addSimbolo(new Token("/"), false);
        this.addSimbolo(new Token("="), false);
        this.addSimbolo(new Token("=="), false);
        this.addSimbolo(new Token("<="), false);
        this.addSimbolo(new Token(">="), false);
        this.addSimbolo(new Token("<"), false);
        this.addSimbolo(new Token(">"), false);
        this.addSimbolo(new Token("!="), false);
        this.addSimbolo(new Token("("), false);
        this.addSimbolo(new Token(")"), false);
        this.addSimbolo(new Token(","), false);
        this.addSimbolo(new Token(";"), false);
    }
    
    public void reset() {
        this.simbolos.clear();
    }
    
}

