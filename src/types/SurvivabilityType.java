package types;
/**
 * Descreve os tipos de algoritmo de sobrevivência 
 * considerados no simulador.
 * @author André 
 */
public enum SurvivabilityType {
	
	NOPROTECTION(1,"1+0"),
	DEDICATED(2, "1+1"),
	RESTAURATION(3, "1+R"),
	HYBRID(4, "1+1+R");
	
	/**
	 * Código do algoritmo de sobrevivência.
	 * @author André 			
	 */	
	private int code;
	/**
	 * Descrição do algoritmo de sobrevivência.
	 * @author André 			
	 */	
	private String description;
	/**
	 * Construtor da classe.
	 * @param code
	 * @param description
	 * @author André
	 */	
	private SurvivabilityType(final int code, final String description){
		this.code = code;
		this.description = description;
	}
	/**
	 * Método para retornar o código do algoritmo
	 * de sobrevivência.
	 * @return code
	 * @author André 			
	 */
	public int getCode() {
		return code;
	}
	/**
	 * Método para configurar o código do algoritmo
	 * de sobrevivência.
	 * @param code
	 * @author André 			
	 */
	public void setCode(final int code) {
		this.code = code;
	}
	/**
	 * Método para retonar a descrição do algoritmo
	 * de sobrevivência.
	 * @return description
	 * @author André 			
	 */	
	public String getDescription(){
		return this.description;
	}
	/**
	 * Método para configurar a descrição do algoritmo
	 * de sobrevivência.
	 * @param description
	 * @author André 			
	 */		
	public void setDescription(final String description){
		this.description = description;
	}	
}
