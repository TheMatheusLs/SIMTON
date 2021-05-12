package types;
/**
 * Descreve os tipos de algoritmo de atribuição de ganhos dos 
 * amplificadores usado no simulador.
 * @author André 
 */
public enum GainAlgorithmType {
	
	BASIC (1);
	
	/**
	 * Código do algoritmo de ganho.
	 * @author André 			
	 */	
	private int code;	
	/**
	 * Construtor da classe.
	 * @param code
	 * @author André
	 */	
	private GainAlgorithmType(final int code){
		this.code = code;		
	}
	/**
	 * Método para retornar o código do algoritmo de ganho.
	 * @return O atributo code
	 * @author André 			
	 */
	public int getCode() {
		return code;
	}
	/**
	 * Método para configurar o código do algoritmo de ganho.
	 * @param code
	 * @author André 			
	 */
	public void setCode(final int code) {
		this.code = code;
	}
}
