package types;
/**
 * Descreve os tipos de algoritmo de sobreviv�ncia 
 * considerados no simulador.
 * @author Andr� 
 */
public enum SurvivabilityType {
	
	NOPROTECTION(1,"1+0"),
	DEDICATED(2, "1+1"),
	RESTAURATION(3, "1+R"),
	HYBRID(4, "1+1+R");
	
	/**
	 * C�digo do algoritmo de sobreviv�ncia.
	 * @author Andr� 			
	 */	
	private int code;
	/**
	 * Descri��o do algoritmo de sobreviv�ncia.
	 * @author Andr� 			
	 */	
	private String description;
	/**
	 * Construtor da classe.
	 * @param code
	 * @param description
	 * @author Andr�
	 */	
	private SurvivabilityType(final int code, final String description){
		this.code = code;
		this.description = description;
	}
	/**
	 * M�todo para retornar o c�digo do algoritmo
	 * de sobreviv�ncia.
	 * @return code
	 * @author Andr� 			
	 */
	public int getCode() {
		return code;
	}
	/**
	 * M�todo para configurar o c�digo do algoritmo
	 * de sobreviv�ncia.
	 * @param code
	 * @author Andr� 			
	 */
	public void setCode(final int code) {
		this.code = code;
	}
	/**
	 * M�todo para retonar a descri��o do algoritmo
	 * de sobreviv�ncia.
	 * @return description
	 * @author Andr� 			
	 */	
	public String getDescription(){
		return this.description;
	}
	/**
	 * M�todo para configurar a descri��o do algoritmo
	 * de sobreviv�ncia.
	 * @param description
	 * @author Andr� 			
	 */		
	public void setDescription(final String description){
		this.description = description;
	}	
}
