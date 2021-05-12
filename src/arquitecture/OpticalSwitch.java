package arquitecture;

/**
 * Descreve o componente optical switch usado no simulador.
 * @author André 
 */
public class OpticalSwitch {
	
	/**
	 * Identificador do optical switch.
	 * @author André 			
	 */	
	private transient int opticalSwitchId;
	/**
	 * Valor de atenuação (linear) do optical switch.
	 * @author André 			
	 */	
	private transient double switchAtenuation;
	/**
	 * Potência inicial do laser.
	 * @author André 			
	 */	
    private transient double laserPower;
    /**
	 * OSNR de entrada do laser.
	 * @author André 			
	 */	
    private transient double laserOSNR;
    /**
	 * Número de regeneradores no optical switch.
	 * @author André 			
	 */	
    private transient int numberOfRegen;
    /**
   	 * Número de regeneradores disponíveis no optical switch.
   	 * @author André 			
   	 */	
    private transient int numberOfFreeRegen;
    /**
   	 * Número de regenerações ocorridas neste optical switch.
   	 * @author André 			
   	 */	
    private transient int numbOfUsedReg;
    /**
   	 * Número máximo de regeneradores usados no optical switch.
   	 * @author André 			
   	 */	
    private transient int maxSimUsedRegen;
    /**
	 * Construtor da classe.
	 * @param opticalSwitchId
	 * @param switchAtenIndB
	 * @param laserPowerIndBm
	 * @param laserOSNRindB
	 * @author André
	 */		
	public OpticalSwitch(final int opticalSwitchId, final double switchAtenIndB, final double laserPowerIndBm, final double laserOSNRindB){		
		this.opticalSwitchId = opticalSwitchId;
		this.switchAtenuation = Math.pow(10,switchAtenIndB/10);
		this.laserPower = Math.pow(10,laserPowerIndBm/10)*Math.pow(10,switchAtenIndB/10);
		this.laserOSNR = Math.pow(10,laserOSNRindB/10);
		this.numberOfRegen = 0;
		this.numberOfFreeRegen = 0;
		this.numbOfUsedReg = 0;
		this.maxSimUsedRegen = 0;
	}	
	/**
	 * Método para configurar o identificador do optical switch.
	 * @return opticalSwitchId
	 * @author André 			
	 */
    public void setOpticalSwitchId(final int opticalSwitchId){
    	this.opticalSwitchId = opticalSwitchId;
    }
    /**
     * Método para retornar o identificador do optical switch.
     * @return O atributo opticalSwitchId
     * @author André 			
     */    
    public int getOpticalSwitchId(){
    	return this.opticalSwitchId;
    }
    /**
	 * Método para configurar a OSNR do laser.
	 * @param osnrIndB
	 * @author André 			
	 */  
    public void setLaserOSNR(final double osnrIndB){
    	this.laserOSNR = Math.pow(10,osnrIndB/10);
    }
    /**
     * Método para retornar a OSNR do laser.
     * @return O atributo laserOSNR
     * @author André 			
     */ 
    public double getLaserOSNR(){
    	return this.laserOSNR;
    }
    /**
     * Método para retornar a atenuação do optical switch.
     * @return O atributo switchAtenuation
     * @author André 			
     */
    public double getSwitchAtenuation(){
    	return this.switchAtenuation;
    }
    /**
	 * Método para configurar a atenuação do optical switch.
	 * @param switchAtenIndB
	 * @author André 			
	 */
    public void setSwitchAtenuation(final double switchAtenIndB){
    	this.switchAtenuation = Math.pow(10,switchAtenIndB/10);
    }
    /**
     * Método para retornar a potência inicial do laser.
     * @return O atributo laserPower
     * @author André 			
     */ 
    public double getLaserPower(){
    	return this.laserPower;
    }
    /**
	 * Método para configurar a potência inicial do laser.
	 * @param powerIndBm
	 * @author André 			
	 */
    public void setLaserPower(final double powerIndBm){
    	this.laserPower = Math.pow(10,powerIndBm/10)*this.switchAtenuation;
    }
    /**
     * Método para retornar o número de regeneradores.
     * @return O atributo numberOfRegen
     * @author André 			
     */ 
	public int getNumberOfRegenerators() {
		return numberOfRegen;
	}
	/**
	 * Método para configurar o número de regeneradores do optical switch.
	 * @param numberOfRegen
	 * @author André 			
	 */
	public void setNumberOfRegenerators(final int numberOfRegen) {
		this.numberOfRegen = numberOfRegen;
	}
	/**
     * Método para incrementar o número de regeneradores livre
     * no optical switch.
     * @author André 			
     */ 	
	public void increaseNumberOfFreeRegeneratorsd(){		
		this.numbOfUsedReg++;
		final int usedRegenerators = this.numberOfRegen - this.numberOfFreeRegen;
		if (this.maxSimUsedRegen < usedRegenerators){
			this.maxSimUsedRegen = usedRegenerators;
		}
		this.numberOfFreeRegen++;
	}
	/**
     * Método para decrementa o número de regeneradores livre
     * no optical switch.
     * @author André 			
     */ 	
	public void decreaseNumberOfFreeRegenerators(){		
		this.numberOfFreeRegen--;
	}
	/**
     * Método para retornar o número de regeneradores livres
     * no optical switch.
     * @return O atributo numberOfFreeRegen
     * @author André 			
     */
	public int getNumberOfFreeRegenerators() {
		return numberOfFreeRegen;
	}
	/**
	 * Método para configurar o número de regeneradores livres
	 * do optical switch.
	 * @param numbOfFreeRegen
	 * @author André 			
	 */
	public void setNumberOfFreeRegenerators(final int numbOfFreeRegen) {
		this.numberOfFreeRegen = numbOfFreeRegen;
	}
	/**
     * Método para resetar o número de regeneradores livres
     * no optical switch.
     * @author André 			
     */	
	public void resetNumberOfFreeRegen(){
		this.numberOfFreeRegen = this.numberOfRegen;
	}
	/**
     * Método para retornar o número máximo de regeneradores usados
     * no optical switch.
     * @return O atributo maxSimUsedRegen
     * @author André 			
     */
	public int getMaxSimutaneusllyUsedRegenerators() {
		return maxSimUsedRegen;
	}
	/**
	 * Método para configurar o número máximo de regeneradores usados
	 * do optical switch.
	 * @param maxSimUsedRegen
	 * @author André 			
	 */
	public void setMaxSimutaneusllyUsedRegenerators(final int maxSimUsedRegen) {
		this.maxSimUsedRegen = maxSimUsedRegen;
	}
	/**
     * Método para retornar o número de regeneradores usados
     * no optical switch.
     * @return O atributo numbOfUsedReg
     * @author André 			
     */
	public int getNumberOfTimesRegeneratedHere() {
		return numbOfUsedReg;
	}
	/**
	 * Método para configurar o número regenerações ocorridas 
	 * no optical switch.
	 * @param numbOfTimesReg
	 * @author André 			
	 */
	public void setNumberOfTimesRegeneratedHere(final int numbOfTimesReg) {
		this.numbOfUsedReg = numbOfTimesReg;
	}   
}
