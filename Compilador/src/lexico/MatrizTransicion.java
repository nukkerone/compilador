/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

import java.util.Hashtable;

/**
 *
 * @author Gabriel
 */
public class MatrizTransicion {
    private Hashtable<Integer, Hashtable<String, Transicion>> transiciones;
    
    public MatrizTransicion() {
        this.transiciones = new Hashtable<Integer, Hashtable<String, Transicion>>();
    }
    
    /**
     * Crea y agrega/sobreescribe una transición en la Matriz, para el estado inicial y la entrada seleccionada.
     * Se crea un elemento Transición en base al estado siguiente y la acción semantica proporcionada
     * 
     * @param estadoInicial
     * @param entrada
     * @param estadoSiguiente
     * @param accionSemantica 
     */
    public void setTransicion(int estadoInicial, String entrada, int estadoSiguiente, AccionSemantica accionSemantica) {
        Transicion t = new Transicion(estadoSiguiente, accionSemantica);
        
        if (this.tieneEstado(estadoInicial)) {
            Hashtable<String, Transicion> hash_t = this.transiciones.get(estadoInicial);
            hash_t.put(entrada, t);
            this.transiciones.put(estadoInicial, hash_t);
        } else {
            Hashtable<String, Transicion> hash_t = new Hashtable<String, Transicion>();
            hash_t.put(entrada, t);
            this.transiciones.put(estadoInicial, hash_t);
        }
    }
    
    /**
     * Devuelve si existe una fila para el estado en la Matriz
     * @param estado
     * @return 
     */
    public boolean tieneEstado(int estado) {
        Hashtable<String, Transicion> t;
        try {
            t = this.transiciones.get(estado);
        } catch ( NullPointerException e ) {
            return false;
        }
        if (t == null) {
            return false;
        }
        return true;
    }
    
    /**
     * Cantidad de estados en la matriz
     * @return 
     */
    public int stateSize() {
        return this.transiciones.size();
    }
    
    /**
     * Dado una estado y una entrada y retorna una transición si existe para esos datos
     * 
     * @param estado
     * @param entrada
     * @return 
     */
    public Transicion getTransicion(int estado, String entrada) {
        if (this.tieneEstado(estado)) { // Existe una fila en la matriz para el estado
            Hashtable<String, Transicion> hash_t = this.transiciones.get(estado);
            Transicion t = hash_t.get(entrada);
            return t;
        }
        return null;
    }

    public void reset() {
        this.transiciones.clear();
    }
    
    /**
     * Ejecuta una acción, se delega a la transición la ejecución
     * 
     * @param estado
     * @param entrada 
     */
    public void ejecutarTransicion(int estado, String entrada) {
        Transicion t = this.getTransicion(estado, entrada);
        if (t != null) {    // Si la transición existe
            t.ejecutarAccion(); // Delegar ejecución de la acción
        }
    }
    
}
