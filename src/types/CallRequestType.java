package types;
/**
 * Descreve os tipos de uma requisi��o de chamada. 
 * @author Andr� 
 */
public enum CallRequestType {
		
	BIDIRECTIONAL(1),
	UNIDIRECTIONAL(2);
	/**
	 * C�digo da requisi��o de chamada.
	 * @author Andr� 			
	 */
	private int code;
	/**
	 * Construtor da classe.
	 * @param code
	 * @author Andr�
	 */
	private CallRequestType(final int code){
		this.code = code;		
	}
	/**
	 * M�todo para retornar o c�digo do algoritmo de ganho.
	 * @return O atributo code
	 * @author Andr� 			
	 */
	public int getCode() {
		return code;
	}
	/**
	 * M�todo para configurar o c�digo do algoritmo de ganho.
	 * @param code
	 * @author Andr� 			
	 */
	public void setCode(final int code) {
		this.code = code;
	}	
}
