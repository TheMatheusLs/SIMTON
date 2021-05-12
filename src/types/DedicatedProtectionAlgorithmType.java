package types;
/**
 * Descreve os tipos de algoritmo de prote��o dedicada 
 * usado no simulador.
 * @author Andr� 
 */
public enum DedicatedProtectionAlgorithmType {
	
	Suurballe(1),
	
	TSSP(2),
	
	KPR(3);
	/**
	 * C�digo do algoritmo de prote��o dedicada.
	 * @author Andr� 			
	 */	
	private int code;
	/**
	 * Construtor da classe.
	 * @param code
	 * @author Andr�
	 */		
	private DedicatedProtectionAlgorithmType(final int code){
		this.code = code;		
	}
	/**
	 * M�todo para retornar o c�digo do algoritmo de  prote��o
	 * dedicada.
	 * @return O atributo code
	 * @author Andr� 			
	 */
	public int getCode() {
		return this.code;
	}
	/**
	 * M�todo para configurar o c�digo do algoritmo de prote��o
	 * dedicada.
	 * @param code
	 * @author Andr� 			
	 */
	public void setCode(final int code) {
		this.code = code;
	}

}
