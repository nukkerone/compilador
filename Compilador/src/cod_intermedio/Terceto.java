/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cod_intermedio;

import GenerarAssembler.SeguidorEstReg;
import interfaces.Typeable;
import java.util.Iterator;
import java.util.Vector;
import lexico.Token;
import lexico.TypeableToken;
import filereader.OutputCode;

/**
 *
 * @author Gabriel
 */
public abstract class Terceto implements Typeable {

    protected int tipo;
    protected int posicion;
    protected String ambito = "_main";
    protected Typeable parametro1, parametro2;
    protected String operacion;
    protected boolean throwsError;
    public static Vector<Terceto> tercetos = new Vector<Terceto>();
    
    public Terceto(String op) {
        this.tipo = Terceto.TIPO_DESCONOCIDO;
        this.parametro1 = null;
        this.parametro2 = null;
        this.throwsError = false;
        operacion = op;
        this.setPosicion(Terceto.tercetos.size());
        Terceto.tercetos.add(this); //agrega tercetos a la lista de tercetos
        //this.setAmbito("_main");
    }
    
    public Terceto(String op, Typeable p1, Typeable p2) {
        super();
        operacion = op;
        
        //@TODO falta checkeo de errores semanticos (Integer, que no supere limites)
        
        this.parametro1 = p1;
        this.parametro2 = p2;
        
        this.throwsError = false;

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
    
    public String getOperacion() {
        return this.operacion;
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
    
    public String getAmbito() {
        return this.ambito;
    }
    
    /**
     * Setea el ambito del terceto, para lo cual se fija si los parametros son Tokens, entonces
     * actualiza el lexema de los mismo para incluir el prefijo del ambito
     * 
     * 
     * @param ambito El prefijo del ambito ya tiene que venir de la forma "_nombreAmbito"
     */
    public void setAmbito(String ambito) {
        this.ambito = ambito;
        if (this.parametro1 != null && this.parametro1 instanceof TypeableToken) {
            Token tokenParam1 = (Token) this.parametro1;
            tokenParam1.setAmbito(ambito);
        }
        
        if (this.parametro2 != null && this.parametro2 instanceof TypeableToken) {
            Token tokenParam2 = (Token) this.parametro2;
            tokenParam2.setAmbito(ambito);
        }
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
            String s = this.getOperacion();
            String param1 = "NULL";
            String param2 = "NULL";
        
        
        if (this.parametro1 != null) {
            //param1Str = this.parametro1.getTipo();
            if (this.parametro1 instanceof TypeableToken) {
                param1 = ((TypeableToken)this.parametro1).getLexema();
            } else {
                param1 = "[" + ((Terceto)this.parametro1).getPosicion() +"]";
            }
        }
   
        if (this.parametro2 != null) {
            if (this.parametro2 instanceof TypeableToken) {
                param2 = ((TypeableToken)this.parametro2).getLexema();
            } else {
                param2 = "[" + ((Terceto)this.parametro2).getPosicion() +"]";
            }
        }
        
        return "( " + s + ", " + param1 + ", " + param2+ " )";
    }
    
    
 //   public static  void printTercetos(OutputCode o) {
     /* public static  void printTercetos() {
        Iterator it = Terceto.tercetos.iterator();
        while (it.hasNext()) {
           Terceto t = (Terceto) it.next();
           System.out.println(t.toString());
      //     o.addLine(t.toString());
        }
    }*/
    
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
    
    public String getErrorCode(){
        return "MOV dx, OFFSET "+getEtiqueta()+"_MESSAGE\n" +
        "MOV ah, 9\n" +
        "int 21h\n" +
        "JMP SALIR";
    }
    
    public boolean throwsError() {
        return this.throwsError;
    }
    
    public abstract String getEtiqueta();
    public abstract String getMessageData();
    public abstract Vector<String> generarAssembler(SeguidorEstReg seguidor);
}
