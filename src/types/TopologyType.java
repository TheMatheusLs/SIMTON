package types;
/**
 * Descreve os tipos de topologias consideradas no simulador.
 * @author Matheus
 */
public enum TopologyType {

	NSFNET14 (0, "NSFNET_14"),
	PACIFICBELL (1, "PACIFICBELL"),
	FINLANDIA (2, "FINLANDIA"),
	FINLANDIA_NEW (3, "FINLANDIA_NEW"),
	ATTMPLS (4, "ATTMPLS"),
	RING (5, "RING"),
	TOROIDAL (5, "TOROIDAL");
	
	private int code; // Código do algoritmo da Topologia
	private String description; // Descrição do algoritmo de roteamento.

	private TopologyType(final int code, final String description){
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
