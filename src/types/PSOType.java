package types;
/**
 * Descreve os tipos de comunicação entre as particulas para o PSO
 * @author Matheus
 */
public enum PSOType {

	GLOBAL_BEST (0, "GLOBAL_BEST"),
	LOCAL_BEST (1, "LOCAL_BEST");
    
	private int code; // Código do algoritmo da Topologia
	private String description; // Descrição do algoritmo de roteamento.

	private PSOType(final int code, final String description){
		this.code = code;
		this.description = description;		
	}

	public int getCode() {
		return this.code;
	}

	public String getDescription() {
		return this.description;
	}	

}