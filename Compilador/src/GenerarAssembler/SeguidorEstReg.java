/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GenerarAssembler;
import java.util.*;
import assembler.DireccionRepreVarAssembler;
import assembler.Inmediato;
import assembler.PosMemoria;
import assembler.Registro;
import assembler.VariableAuxiliar;
import lexico.TypeableToken;
import interfaces.Typeable;
import lexico.IdTS;
import lexico.TablaSimbolos;

/*import assembler.Inmediate;
import assembler.PosMemoria;
import assembler.VariableAuxiliar;*/
/**
 *
 * @author damian
 */
public class SeguidorEstReg {
    
static int CANTREG = 4;
    /**
     * Dado un terceto o una variable, saber en que registro está, si es que está..
     */
    Hashtable<Typeable, Registro> registros = new Hashtable<Typeable, Registro>();
    /**
     * Almacena todos los registros fisicos.
     */
    Registro[] reg = {new Registro("A", this), new Registro("B", this), new Registro("C", this) ,new Registro("D", this)};
    /**
     * Almacena
     */
    private Hashtable<TypeableToken, PosMemoria> memoria = new Hashtable<TypeableToken, PosMemoria>();
    
    private TablaSimbolos tablaSimbolos;


    public boolean estaEnRegistro(Typeable t){
        return registros.containsKey(t);
    }

    public boolean estaEnRegistroA(Typeable t){
        return reg[0].getT() == t;
    }
	
	
    private void ubicarEnRegNum(int nreg, Typeable t) {
        reg[nreg].ocupar(t); //ocupo para que sepa el tipo cuando le pido el nombre
        DireccionRepreVarAssembler d = ubicar(t); //encontrar donde estaba
        this.codigoAux.add("MOV " + reg[nreg].getNombre()+ ", " + d.getNombre());
        d.liberate(); //libero (si muevo de registro a registro debe estar este paso si o si antes del siguiente, sino t ya esta en la tabla d hash
        registros.put(t, reg[nreg]);//guardo donde esta el tipable actual
        ultimoReg = nreg;
    }
	
	public Registro getRegistro(Typeable t) {
		return registros.get(t);
	}
	
        // Intenta ubicar en inmediato si es posible (Constante entera)
	public DireccionRepreVarAssembler ubicarEnInmed(Typeable t) {
            if(t.getTipo() == Typeable.TIPO_CTE_ENTERA && t instanceof TypeableToken)
                return new Inmediato((TypeableToken) t);
            else
                return null;
	}
	
	public DireccionRepreVarAssembler ubicarEnRegistroOInmed(Typeable t) {
            DireccionRepreVarAssembler d = this.ubicarEnInmed(t);
            if(d == null)
                d = ubicarEnRegistro(t);
            return d;
	}
	
	int ultimoReg;
	
	/**
	 * UbicarEnRegistro(typeable)
	 * damelo en un registro cualquiera
	 * puede ser que ya estaba en un registro
	 * trata de que sea B o C
	 */
	public Registro ubicarEnRegistro(Typeable t) {
            if(estaEnRegistro(t))
                return getRegistro(t);


            int lugar = buscarRegLibre();
            if(lugar == -1)
                if(ultimoReg == 1)
                    lugar = 2;
                else
                    lugar = 1;
            desocupar(lugar);
            ubicarEnRegNum(lugar, t);
            return reg[lugar];	
	}
        
        
        /**
         * Devuelve un registro libre
         * 
         * @return 
         */
	public Registro generarRegistroLibre() {
           
            int lugar = buscarRegLibre();
            if(lugar == -1)
                if(ultimoReg == 1)
                    lugar = 2;
                else
                    lugar = 1;
            desocupar(lugar);
            return reg[lugar];	
	}

	

	/**
	 * este donde este ( cualquier registro, o en memoria) que me lo de en A, 
	 * lo de A lo guarda, pq se va a necesitar mas adelante
	 * @param t
	 * @return
	 */
	/**
	 * Pedir el que esta desde hace mas tiempo aca, 
	 * el q vino ultiumo puede ser que lo necesite
	 */
	public Registro ubicarEnRegistroA(Typeable t){
            if(registros.get(t)== reg[0]) //si ya esta en A retorno A
                return reg[0];
            desocupar(0);
            ubicarEnRegNum(0, t);
            return reg[0];
	}
	
	/**
	 * Desocupa el registro A, guardando en memoria lo que tenia.
	 */
	public void desocuparA() {
            desocupar(0);
	}
	public void desocuparD() {
            desocupar(3);
	}
	
	/**
	 * ubica el Typeable en cualquier lugar
	 * @param t Typeable que quiere ubicar
	 * @return ubicacion del Typeable
	 */
	public DireccionRepreVarAssembler ubicar(Typeable t) {		
            DireccionRepreVarAssembler d = this.ubicarEnInmed(t);
            if(d == null)
                if(estaEnRegistro(t))
                    d = getRegistro(t);
                else
                    d = ubicarEnMemoria(t);
            return d;
	}
	
	public DireccionRepreVarAssembler ubicarEnInmedOMemoria(Typeable t) {
            DireccionRepreVarAssembler d = this.ubicarEnInmed(t);
            if(d == null)
                d = ubicarEnMemoria(t);
            return d;
	}
	
	public DireccionRepreVarAssembler ubicarEnRegistroOMemoria(Typeable t) {
            DireccionRepreVarAssembler d = null;
            if(estaEnRegistro(t))
                d = getRegistro(t);
            else
                d = ubicarEnMemoria(t);
            return d;
	}

	/**
	 * Desocupa un registro, guardando en memoria lo que contenia antes.
	 * @param pos: registro a desocupar
	 */
	private void desocupar(int pos) {
            if (reg[pos].estaOcupado()){
                int rnew = buscarRegLibre();
                if(rnew == -1 || rnew == 0 /* ||rnew == 3*/){
                    //no hay otro registro, lo paso a memoria
                    Typeable t = reg[pos].getT();
                    VariableAuxiliar v = new VariableAuxiliar(t, null);
                    PosMemoria pm =new PosMemoria(v); 
                    memoria.put(v, pm);
                    this.codigoAux.add("MOV " + pm.getNombre() + ", "+ reg[pos].getNombre());
                    registros.remove(t);
                    reg[pos].liberar();
                }
                else {
                    //lo paso a otro registro
                    Typeable t = reg[pos].getT();
                    reg[rnew].ocupar(t);
                    this.codigoAux.add("MOV " + reg[rnew].getNombre() + ", "+reg[pos].getNombre());
                    reg[pos].liberar();
                    registros.remove(t);
                    registros.put(t, reg[rnew]);
                }
            }
	}
        
        public DireccionRepreVarAssembler ubicarAuxiliarEnMemoria(TypeableToken t, String nombre) {
            VariableAuxiliar v = new VariableAuxiliar(t, nombre);
            PosMemoria pm =new PosMemoria(v); 
            memoria.put(v, pm);
            return pm;
        }
	
	/**
	 * Devuelve la posición del registro que esté libre. En caso de que no haya ninguno, devuelve -1.
	 */
	private int buscarRegLibre() {
            for (int i = 1; i < 4; i++) {
                if(reg[i].estaOcupado() == false)
                    return i;
            }
            return -1;
	}
	public void liberarReg(Typeable t){
            Registro r = registros.get(t);
            if(r != null){
                r.liberar();
                registros.remove(t);
            }
	}
	
	public void liberarReg(int i){
            reg[i].liberate();
	}
	

	
	/**
	 * desocuparD
	 * 
	 * cada vez q desocupo generar codigo assembler para subir a memoria, asignar memoria si era un terceto
	 * (recordar actualizar arreglo de asignaciones y tambien la tabla de hash)
	 * 
	 * 

	 * 
	 * 
	 * liberarRegistro( Regs)
	 * solo libera, el q lo llamo asegura q este valor ya lo uso y no lo necesita 
	 * 
	 */

	/**
	 * Devuelve la posicion en memoria en donde se encuentra el TypeableToken pasado
	 * por parametro.
	 * @return
	 */
	public PosMemoria ubicarEnMemoria(Typeable t){
            if (memoria.containsKey(t))
                return memoria.get(t);
            else {
                PosMemoria pos = new PosMemoria( (TypeableToken)t);
                memoria.put((TypeableToken)t, pos);
                return pos;
            }
	}

	public SeguidorEstReg(TablaSimbolos ts){
            this.tablaSimbolos = ts;
	}
	
	Vector<String> codigoAux = new Vector<String>();
	public Vector<String> getCodigoAsm(){
            Vector<String> aux = codigoAux;
            codigoAux = new Vector<String>();
            return aux;
	}
	
	/**
	 * Devuelve el codigo assembler necesario para definir las variables en el .asm
	 * @return
	 */
	public Vector<String> getVariables(){
            Vector<String> salida = new Vector<String>();
            Enumeration<PosMemoria> e = memoria.elements();
            while (e.hasMoreElements()) {
                PosMemoria variable = (PosMemoria) e.nextElement();
                TypeableToken tt = variable.getTk();
                TypeableToken tt_fromTs = (TypeableToken)this.tablaSimbolos.getSimbolo(new IdTS(tt.getLexema(), tt.getUso()));
                int tipoReal = tt_fromTs.getTipo();
                if ( tipoReal == Typeable.TIPO_CTE_ENTERA  ) {
                    salida.add(variable.getNombre()+" DD "+tt_fromTs.getInitialValue());
                    continue;
                }
                if ( false /* tipoReal == Typeable.TIPO_ULONG */ ) {
                    salida.add(variable.getNombre()+" DW "+tt_fromTs.getInitialValue());
                    continue;
                }
                
                if (tipoReal == Typeable.TIPO_CADENA) {
                    salida.add(variable.getNombre()+" DB "+tt_fromTs.getInitialValue() + ",0");
                    continue;
                }
                
                salida.add(variable.getNombre()+" DD "+tt_fromTs.getInitialValue());
                        
            }
            return salida;
	}

	public void actualizarRegistro(Registro r1, Typeable t) {
            Typeable t_old = r1.liberar();
            registros.remove(t_old);

            r1.ocupar(t);
            registros.put(t, r1);
        }
}
