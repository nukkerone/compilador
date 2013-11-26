/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cod_intermedio;

import GenerarAssembler.SeguidorEstReg;
import assembler.DireccionRepreVarAssembler;
import interfaces.Typeable;
import java.util.Vector;
import lexico.TypeableToken;

/**
 *
 * @author mountainlion
 */
public class TercetoRetorno extends Terceto {
    
    private int valor;
    private TypeableToken retToken;
    
    public TercetoRetorno(Typeable param1) {
        super("Retorno");
        this.valor = 0;
        this.parametro1 = param1;
        this.retToken = retToken;
    }

    @Override
    public String getEtiqueta() {
        return null;
    }

    @Override
    public String getMessageData() {
        return null;
    }

    /**
     * Genera el assembler para un terceto retorno
     * 
     * @param seguidor  Es el SeguidorEstReg
     * @return 
     */
    @Override
    public Vector<String> generarAssembler(SeguidorEstReg seguidor) {
        Vector<String> v = new Vector<String>();
        // Ubico memoria auxiliar

        DireccionRepreVarAssembler valRet = seguidor.ubicarAuxiliarEnMemoria((TypeableToken)this.parametro1, "RET");
        
        String lexemaRet = ((TypeableToken) this.parametro1).getLexema();
        
        //v.add("MOV " + valRet.getNombre() + ", " + lexemaRet);
        v.add("POP DX");
        v.add("POP CX");
        v.add("POP BX");
        v.add("POP AX");
        v.add("ret");
        return v;
    }
    
    public int getValor() {
        return this.valor;
    }
    
    public void setValor(int val) {
        this.valor = val;
    }
    
    @Override
    public void setAmbito(String ambito) {
        // Retorno no modifica el nombre de su parametro _RET ya que este es igual en cualquier ambito
    }
    
}
