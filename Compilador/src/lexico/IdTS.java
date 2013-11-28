/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;

import java.util.Objects;

/**
 *
 * @author Gabriel
 */

public class IdTS {
	
	private String lexema;
	private int uso;
	
	
	public IdTS(String lexema, int uso) {
		this.lexema = lexema;
		this.uso = uso;
	}
	
	public String getLexema() {
		return lexema;
	}
	
	public void setLexema(String lexema) {
		this.lexema = lexema;
	}
	
	public int getUso() {
		return uso;
	}
	
	public void setUso(int uso) {
		this.uso = uso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lexema == null) ? 0 : lexema.hashCode());
		result = prime * result + uso;
		return result;
	}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IdTS other = (IdTS) obj;
        if (!Objects.equals(this.lexema, other.lexema)) {
            return false;
        }
        if (this.uso != other.uso) {
            return false;
        }
        return true;
    }

	
	
	
	

}
