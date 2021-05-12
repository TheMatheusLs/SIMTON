package types;
/**
 * Descreve os tipos de algoritmo de proteção dedicada 
 * usado no simulador.
 * @author André 
 */
public enum DedicatedProtectionAlgorithmType {
	
	Suurballe(1),
	
	TSSP(2),
	
	KPR(3);
	/**
	 * Código do algoritmo de proteção dedicada.
	 * @author André 			
	 */	
	private int code;
	/**
	 * Construtor da classe.
	 * @param code
	 * @author André
	 */		
	private DedicatedProtectionAlgorithmType(final int code){
		this.code = code;		
	}
	/**
	 * Método para retornar o código do algoritmo de  proteção
	 * dedicada.
	 * @return O atributo code
	 * @author André 			
	 */
	public int getCode() {
		return this.code;
	}
	/**
	 * Método para configurar o código do algoritmo de proteção
	 * dedicada.
	 * @param code
	 * @author André 			
	 */
	public void setCode(final int code) {
		this.code = code;
	}

}
