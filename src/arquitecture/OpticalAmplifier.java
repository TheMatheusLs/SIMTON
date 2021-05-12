package arquitecture;

/**
 * Descreve o componente optical amplifier usado no simulador.
 * @author André
 */

public class OpticalAmplifier {
	/**
	 * Ganho do amplificador em dB.
	 * @author André			
	 */	
	private transient double gainIndB;
	/**
	 * Fator de ruído do amplificador em dB.
	 * @author André			
	 */	
	private transient double nfIndB;
	/**
	* Construtor da classe.
	* @param gainIndB
	* @param noiseFactorIndB
	* @author André
	*/
	public OpticalAmplifier(final double gainIndB, final double noiseFactorIndB){		
		this.gainIndB = gainIndB;		
		this.nfIndB = noiseFactorIndB;
	}	
	/**
	 * Construtor da classe.
	 * @author André
	 */		
	public OpticalAmplifier(){
		this.gainIndB = -1.0;
		this.nfIndB = -1.0;
	}	
	/**
	 * Método para retornar o valor do ganho (linear) do amplificador.
	 * @return O atributo gainIndB no valor linear.
	 * @author André 			
	 */	
	public double getGainInLinear() {
		return Math.pow(10, gainIndB/10);
	}
	/**
	 * Método para configurar o valor do ganho (linear) do amplificador.
	 * @param gainIndB
	 * @author André			
	 */	
	public void setGain(final double gainIndB) {
		this.gainIndB = gainIndB;
	}
	/**
	 * Método para retornar o valor do fator de ruído (linear) do amplificador.
	 * @return  O atributo nfIndB no valor linear.
	 * @author André
	 */	
	public double getNoiseFactorInLinear() {
		return Math.pow(10, nfIndB/10);
	}
	/**
	 * Método para configurar o valor do fator de ruído (linear) do amplificador.
	 * @param noiseFactorIndB
	 *			Fator de ruído em dB.
	 * @author André 			
	 */	
	public void setNoiseFactor(final double noiseFactorIndB) {
		this.nfIndB = noiseFactorIndB;
	}
	/**
	 * Método para retornar o valor do ganho (dB) do amplificador.
	 * @return O atributo gainIndB
	 * @author André 			
	*/	
	public double getGainIndB() {
		return gainIndB;
	}
	/**
	 * Método para retornar o valor do fator de ruído (dB) do amplificador.
	 * @return O atributo nfIndB
	 * @author André			
	*/
	public double getNoiseFactorIndB() {
		return nfIndB;
	}
}
