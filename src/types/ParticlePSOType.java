package types;

public enum ParticlePSOType {
		
	FULL_METRIC(1, "FULL_METRIC"), //  Otimiza todos os coeficientes para as 3 métricas. Usando 8 possições 
	FULL_METRIC_SEED(2, "FULL_METRIC_SEED"), // Otimiza todos os coeficientes para as 3 métricas. Usando 8 possições e adicionando a seed do especialista
	WI_WEIGHT(3, "WI_WEIGHT"), // Otimiza os 7 pesos para as regras
	TWO_POINTS_METRIC(3, "TWO_POINTS_METRIC"), // Dois pontos por métrica, a partícula terá 6 pontos.
	PSR_METRIC(4, "PSR_METRIC"), // Métrica usanda para o PSR
	;

	private int code;
	private String name;

	private ParticlePSOType(final int code, final String name) {
		this.code = code;
		this.name = name;		
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
}