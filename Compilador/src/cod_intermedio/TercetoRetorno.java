/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cod_intermedio;

import GenerarAssembler.SeguidorEstReg;
import assembler.DireccionRepreVarAssembler;
import assembler.Registro;
import interfaces.Typeable;
import java.util.Vector;

/**
 *
 * @author mountainlion
 */
public class TercetoRetorno extends Terceto {
    
    private int valor;
    
    public TercetoRetorno(Typeable param1) {
        super("Retorno");
        this.valor = 0;
        this.parametro1 = param1;
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
        DireccionRepreVarAssembler valRet = seguidor.ubicarEnRegistro(this.parametro1);
        Registro r = seguidor.generarRegistroLibre();
        
        v.add("MOV " + r.getNombre() + "," +  valRet.getNombre());
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
    
    /*
    String r = this.getAdminReg().getLibre();
		code+="MOV " + r + "," + getTipoElemento(t.getDer().getValor()) + "\n";
		code+="MOV _" + t.getOperador().getValor() + ","+ r + "\n";
		code+="POP DX"+"\n"+"POP CX"+"\n"+"POP BX"+"\n"+"POP AX"+"\n";
		code+="ret"+"\n" ;
		this.getAdminReg().liberar(r);
                */
    
}
