/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexicoTest;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import lexico.AccionSemantica;
import lexico.AccionSemantica;
import lexico.MatrizTransicion;

/**
 *
 * @author Gabriel
 */
public class MatrizTransicionTest {
    private MatrizTransicion matrizTransicion;
    private AccionSemantica accionSemantica;
    /*
    public MatrizTransicionTest() {
        this.matrizTransicion = new MatrizTransicion();
        this.accionSemantica = new AccionAgregar();
    }
    
    @Test
    public void agregarItemsTest() {
        this.matrizTransicion.reset();
        this.matrizTransicion.setTransicion(1, "1", 2, this.accionSemantica);
        this.matrizTransicion.setTransicion(2, "1", 2, this.accionSemantica);
        this.matrizTransicion.setTransicion(2, "2", 3, this.accionSemantica);
        int matrizStateSize = this.matrizTransicion.stateSize();
        assertTrue("An element has been added, but not reflected on matriz size", matrizStateSize > 0);
    }
    
    @Test
    public void resetTest() {
        this.matrizTransicion.reset();
        int matrizStateSize = this.matrizTransicion.stateSize();
        assertTrue("El tamaño de estados dentro de una matriz vacia es distinto de 0", matrizStateSize == 0);
        this.matrizTransicion.setTransicion(2, "1", 2, this.accionSemantica);
        this.matrizTransicion.setTransicion(2, "2", 3, this.accionSemantica);
        matrizStateSize = this.matrizTransicion.stateSize();
        assertTrue("La matriz debería tener cargado estados pero no posee ninguno guardado", matrizStateSize > 0);
        this.matrizTransicion.reset();
        matrizStateSize = this.matrizTransicion.stateSize();
        assertTrue("Se realizo un reset de la matriz y todavía posee estados", matrizStateSize == 0);
    }
    
    @Test
    public void transicionExistenteTest() {
        this.matrizTransicion.reset();
        this.matrizTransicion.setTransicion(2, "1", 2, this.accionSemantica);
        Transicion t1 = this.matrizTransicion.getTransicion(2, "1");
        assertTrue("Se agrego una transicion Estado=2, Entrada=1 y luego se preguntó por la transición pero devolvio null", t1 != null);
    }
    
    @Test
    public void transicionInexistenteTest() {
        this.matrizTransicion.reset();
        Transicion t1 = this.matrizTransicion.getTransicion(2, "1");
        assertTrue("Se pregunto por una transición nunca agregada y se devolvio una", t1 == null);
    }
    * */
}