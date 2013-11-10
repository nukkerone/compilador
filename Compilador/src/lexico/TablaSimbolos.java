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
    
    public static final int USO_FUNCION = 1;
    public static final int USO_VARIABLE = 2;
    public static final int USO_CONSTANTE = 3;
    
    
    TablaSimbolos() {
        this.simbolos = new Hashtable();
        this.cargarSimbolosIniciales();
    }
    
    public void addSimbolo(Token token) {
        String storeKey = token.getLexema();
        if(!simbolos.containsKey(storeKey)) {
            simbolos.put(storeKey, token);
        }
    }
    
    public void addSimbolo(Token token, boolean force) {
        if (!force) { 
            this.addSimbolo(token);
            return;
        }
        
        String storeKey = token.getLexema();
        simbolos.put(storeKey, token);
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

