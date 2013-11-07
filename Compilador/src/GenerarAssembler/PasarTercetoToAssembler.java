/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GenerarAssembler;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import lexico.TablaSimbolos;
import cod_intermedio.Terceto;
/**
 *
 * @author damian
 */
public class PasarTercetoToAssembler {
    
	public Vector<String> traducir(Vector<Terceto> tercs, TablaSimbolos ts){

		SeguidorEstReg ser = new SeguidorEstReg();
		Vector<String> asm = new Vector<String>();
		Hashtable<String, String> Messages = new Hashtable<String, String>();
		Hashtable<String, String> Codigo = new Hashtable<String, String>();
		
		for(Terceto t: tercs){
			asm.addAll(t.generarAssembler(ser));
			if (t.canShowError()){
				String etiqueta = t.getEtiqueta();
				if (!Messages.containsKey(etiqueta)){
					Messages.put(etiqueta, t.getMessageData());
					Codigo.put(etiqueta, t.getErrorCode());
				}
			}
		}
		
		
		
		Vector<String> variables = ser.getVariables();
		variables.insertElementAt(".MODEL small",0);
		variables.insertElementAt(".386",1);
		variables.insertElementAt(".STACK 200h",2);
		variables.insertElementAt(".DATA",3);
		Collection<String> e = Messages.values();
		for (Iterator<String> it = e.iterator(); it.hasNext();) {
			String string = it.next();
			variables.add(string);
		}
		variables.add(".CODE");
		Enumeration<String> ec = Codigo.keys();
		while (ec.hasMoreElements()) {
			String etiq = (String) ec.nextElement();
			variables.add(etiq+":");
			variables.add(Codigo.get(etiq));
		}
		variables.add("SALIR:");
		variables.add("MOV ah,4ch			; DOS: termina el programa");
		variables.add("MOV al,0			; el código de retorno será 0");
		variables.add("INT 21h			; termina el programa");
		variables.add("START:");
		variables.add("MOV ax,@DATA ;obtener la posicion de los datos");
		variables.add("MOV ds,ax ; cargar el segmento de datos con el puntero");
		
		variables.addAll(asm);
		variables.add("JMP SALIR");
		variables.add("END START");
		return variables;
	}

}