package types;
/**
 * Descreve os tipos de algoritmo de roteamento 
 * considerados no simulador.
 * @author Andr� 
 */
public enum RoutingAlgorithmType {
	
	SP (1, "Shortest Path", "SP"),
	YEN (2, "Yen Algorithm", "YEN"),
	ACO (3, "ACO Algorithm", "ACO"),
	SCCSP (4, "Spectrum Continuity and Continuity Shortest Path", "SCCSP"),
	PSR (5, "Power Series Routing", "PSR"),
	MH (6, "Minimum Hops", "MH"),
	CASP (7, "Congested Aware Shortest Path", "CASP"),
	FUZZY (8, "Fuzzy Algorithm", "FUZZY"),
	ALTERNATIVE (8, "Diversos altorirmos fixos alternativos", "ALTERNATIVO"),;
	
	/**
	 * C�digo do algoritmo de roteamento.
	 * @author Andr� 			
	 */	
	private int code;
	/**
	 * Descri��o do algoritmo de roteamento.
	 * @author Andr� 			
	 */	
	private String description;
	private String name;
	/**
	 * Construtor da classe.
	 * @param code
	 * @param description
	 * @author Andr�
	 */		
	private RoutingAlgorithmType(final int code, final String description, final String name){
		this.code = code;
		this.description = description;	
		this.name = name;	
	}
	/**
	 * M�todo para retornar o c�digo do algoritmo
	 * de roteamento.
	 * @return code
	 * @author Andr� 			
	 */
	public int getCode() {
		return this.code;
	}
	/**
	 * M�todo para retornar a descri��o do algoritmo
	 * de roteamento.
	 * @return code
	 * @author Andr� 			
	 */
	public String getDescription() {
		return this.description;
	}	

	public String getName() {
		return this.name;
	}
}
