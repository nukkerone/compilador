/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cod_intermedio;

import interfaces.Typeable;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author Gabriel
 */
public class Terceto implements Typeable {

    
    
    protected int tipo;
    protected int posicion;
    protected Typeable parametro1, parametro2;
    protected String operacion;
    public static Vector<Terceto> tercetos = new Vector<Terceto>();
    
    public Terceto(String op) {
        this.tipo = Terceto.TIPO_DESCONOCIDO;
        this.parametro1 = null;
        this.parametro2 = null;
        operacion = op;
        this.setPosicion(Terceto.tercetos.size());
        Terceto.tercetos.add(this); //agrega tercetos a la lista de tercetos
    }
    
    public Terceto(String op, Typeable p1, Typeable p2) {
        super();
        operacion = op;
        
        //@TODO falta checkeo de errores semanticos (Integer, que no supere limites)
        
        this.parametro1 = p1;
        this.parametro2 = p2;

        if(p2 != null && !(p1.getTipo() == (Terceto.TIPO_DESCONOCIDO)) &&
                !(p2.getTipo() == (Terceto.TIPO_DESCONOCIDO))) {
            this.tipo = p1.getTipo(); //si es una asignacion tambien sirve
        }
        else {
            this.tipo = Terceto.TIPO_DESCONOCIDO;
        }
        
        this.setPosicion(Terceto.tercetos.size());
        Terceto.tercetos.add(this); //agrega tercetos a la lista de tercetos	
    }
    
    public String getName() {
        return "["+this.posicion+"]";
    }

    @Override
    public int getTipo() {
        return this.tipo;
    }
    
    @Override
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    
    public int getPosicion() {
        return this.posicion;
    }
    
    public void setPosicion(int pos) {
        this.posicion = pos;
    }
    
    public Typeable getParametro1() {
        return parametro1;
    }
    public void setParametro1(Typeable parametro) {
            this.parametro1 = parametro;
    }
    public Typeable getParametro2() {
            return parametro2;
    }
    public void setParametro2(Typeable parametro) {
            this.parametro2 = parametro;
    }
    
    public String toString() {
        String s = "Terceto: " + this.posicion + " - Operacion: " + this.operacion;
        String param1Str = " - Null";
        String param2Str = " - Null";
        
        if (this.parametro1 != null) {
            param1Str =  " - Parametro 1: " + this.parametro1.toString();
        }
        
        if (this.parametro2 != null) {
            param2Str = " - Parametro 2: " + this.parametro2.toString();
        }
        
        return s + param1Str + param2Str;
    }
    
    public static void printTercetos() {
        Iterator it = Terceto.tercetos.iterator();
        while (it.hasNext()) {
           Terceto t = (Terceto) it.next();
            System.out.println(t.toString());
        }
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
