/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

/**
 *
 * @author Gabriel
 */
public class TablaSimbolos {
    private Hashtable<String, Token> simbolos;
    
    TablaSimbolos() {
        this.simbolos = new Hashtable();
        this.cargarSimbolosIniciales();
    }
    
    public void addSimbolo(Token token) {
        String storeKey = token.getLexema();
        if(!simbolos.contains(storeKey)) {
            simbolos.put(storeKey, token);
        }
    }
    
    public Token getSimbolo(String key) {
        if(!simbolos.contains(key)) {
            return simbolos.get(key);
        }
        return null;
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
        this.addSimbolo(new Token("if"));
        this.addSimbolo(new Token("then"));
        this.addSimbolo(new Token("else"));
        this.addSimbolo(new Token("begin"));
        this.addSimbolo(new Token("end"));
        this.addSimbolo(new Token("print"));
        this.addSimbolo(new Token("function"));
        this.addSimbolo(new Token("return"));
        this.addSimbolo(new Token("+"));
        this.addSimbolo(new Token("-"));
        this.addSimbolo(new Token("*"));
        this.addSimbolo(new Token("/"));
        this.addSimbolo(new Token("="));
        this.addSimbolo(new Token("=="));
        this.addSimbolo(new Token("<="));
        this.addSimbolo(new Token(">="));
        this.addSimbolo(new Token("<"));
        this.addSimbolo(new Token(">"));
        this.addSimbolo(new Token("!="));
        this.addSimbolo(new Token("("));
        this.addSimbolo(new Token(")"));
        this.addSimbolo(new Token(","));
        this.addSimbolo(new Token(";"));
    }
    
    public void reset() {
        this.simbolos.clear();
    }
    
}

