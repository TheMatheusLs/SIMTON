package types;

public enum MetricMethodType {
	
	ORIGINAL_ONE(1, "Métrica original com uma rota sem usar if", "ORIGINAL_ONE"),
	ORIGINAL_KYEN(2, "Métrica original com K rotas", "ORIGINAL_KYEN"),
	MAMDANI_EON_ONE_TRI(3, "Métrica Hops + Forms usando Mamdani com uma rota. Triângulo", "MAMDANI_EON_ONE_TRI"),
	MAMDANI_EON_KYEN_TRI(4, "Métrica Hops + Forms usando Mamdani com K rotas. Triângulo", "MAMDANI_EON_KYEN_TRI"),
	MAMDANI_EON_ONE_TRA(5, "Métrica Hops + Forms usando Mamdani com uma rota. Trápezio", "MAMDANI_EON_ONE_TRA"),
	MAMDANI_EON_KYEN_TRA(6, "Métrica Hops + Forms usando Mamdani com K rotas. Trápezio", "MAMDANI_EON_KYEN_TRA"),
	SUGENO_WDM_ONE_MTHS(7, "Métrica original do artigo, usando as regras por Matheus. Trápezio", "SUGENO_WDM_ONE_MTHS"),
	SUGENO_WDM_ONE_ORIGINAL(8, "Métrica original do artigo, usando as regras originais com if. Trápezio", "SUGENO_WDM_ONE_ORIGINAL"),
	TWO_POINTS_METRIC(9, "Dois pontos por métrica e wi = 1", "TWO_POINTS_METRIC"),
	//SUGENO_WDM_ONE_TRI(7, "Métrica usando Slots + MSlots + Distância com uma rota. Triângulo", "SUGENO_WDM_ONE_TRI"),
	//SUGENO_WDM_KYEN_TRI(8, "Métrica usando Slots + MSlots + Distância com K rotas. Triângulo", "SUGENO_WDM_KYEN_TRI"),
	//SUGENO_WDM_ONE_TRA(9, "Métrica usando Slots + MSlots + Distância com uma rota. Trápezio", "SUGENO_WDM_ONE_TRA"),
	SUGENO_EON_KYEN(10, "Métrica usando Forms + Hops + Distância com K rotas.", "SUGENO_EON_KYEN"),
	SUGENO_EON_ONE(11, "Métrica usando Forms + MForms + Distância com uma rota.", "SUGENO_EON_ONE"),
	ORIGINAL_KYEN_MTHS(12, "Métrica original no arquivo novo", "ORIGINALKYEN_MTHS"),
	ALPHA_BETA_GAMMA_ONE(13, "Usa alpha, beta e gamma como constantes. Uma rota", "ALPHA_BETA_GAMMA_ONE"),
	PSR_METRIC(14, "Usa os coeficientes do PSO para otimizar o PSR e encontrar o CTR", "PSR_METRIC"),
	YEN(15, "Ordena o YEN", "YEN"),
	SLOT_APERTURE(16, "Métrica para calcular os buracos", "SLOT_APERTURE"),
	MSCL_APETURE(17, "Métrica para calcular o buraco com a menor perda de capacidade", "MSCL_APETURE"),
	DANILO(18, "Métrica desenvolvida por Danilo", "DANILO"),
	//SUGENO_EON_ONE_TRI(11, "Métrica usando Hops + MForms + Forms com uma rota. Triângulo", "SUGENO_EON_ONE_TRI"),
	//SUGENO_EON_KYEN_TRI(12, "Métrica usando Hops + MForms + Forms com K rotas. Triângulo", "SUGENO_EON_KYEN_TRI"),
	//SUGENO_EON_ONE_TRA(13, "Métrica usando Hops + MForms + Forms com uma rota. Trápezio", "SUGENO_EON_ONE_TRA"),
	//SUGENO_EON_KYEN_TRA(14, "Métrica usando Hops + MForms + Forms com K rotas. Trápezio", "SUGENO_EON_KYEN_TRA");
	;
	private int code;
	private String description;
	private String name;
	
	private MetricMethodType(final int code, final String description, final String name){
		this.code = code;
		this.description = description;	
		this.name = name;	
	}
	public int getCode() {
		return this.code;
	}
	public String getDescription() {
		return this.description;
	}	
	public String getName() {
		return this.name;
	}
}
