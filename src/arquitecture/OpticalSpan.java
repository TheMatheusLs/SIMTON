package arquitecture;

import java.util.ArrayList;
import java.util.List;

/**
 * Descreve o componente optical span usado no simulador.
 * @author André 
 */

public class OpticalSpan {
	/**
	 * Identificador do Span no enlace.
	 * @author André 			
	 */		
	private transient int spanId;
	/**
	 * Fibra óptica do Span.
	 * @author André 			
	 */	
	private OpticalFiber opticalFiber;
	/**
	 * Amplificador do Span.
	 * @author André 			
	 */
	private OpticalAmplifier opticalAmplifier;
	/**
	 * Número de slots de frequencia (ou comprimentos de onda) da fibra.
	 * @author André			
	 */
	private transient int numberOfSlots;
	/**
	 * Potência total na entrada do amplificador.
	 * @author André 			
	 */
	private transient double  totalPower;
	/**
	 * Lista de potências de cada slot de frequencia.
	 * @author André 			
	 */
	private transient List<Double>  powers;	
	/**
	 * Construtor da classe.
	 * @param spanId
	 * @param numberOfSlots
	 * @param opticalFiber
	 * @param opticalAmplifier
	 * @author André
	 */	
	public OpticalSpan (final int spanId, final int numberOfSlots, final OpticalFiber opticalFiber, final OpticalAmplifier opticalAmplifier){
		this.spanId = spanId;
		this.opticalFiber = opticalFiber;
		this.numberOfSlots = numberOfSlots;
		this.opticalAmplifier = opticalAmplifier;
		this.inicializePowersInSpan(numberOfSlots);
	}
	/**
	 * Método para retornar a fibra óptica do Span.
	 * @return O atributo OpticalFiber
	 * @author André 			
	 */		
	public OpticalFiber getOpticalFiber() {
		return opticalFiber;
	}
	/**
	 * Método para configurar a fibra óptica do Span.
	 * @param opticalFiber
	 * @author André 			
	 */	
	public void setOpticalFiber(final OpticalFiber opticalFiber) {
		this.opticalFiber = opticalFiber;
	}
	/**
	 * Método para retornar o amplificador óptico do Span.
	 * @param O atributo opticalFiber
	 * @author André 			
	 */	
	public OpticalAmplifier getOpticalAmplifier() {
		return opticalAmplifier;
	}
	/**
	 * Método para configurar o amplificador óptico do Span.
	 * @param opticalAmplifier
	 * @author André 			
	 */	
	public void setOpticalAmplifier(final OpticalAmplifier opticalAmplifier) {
		this.opticalAmplifier = opticalAmplifier;
	}
	/**
	 * Método para retornar o identificador do Span.
	 * @return O atributo spanId
	 * @author André 			
	 */	
	public int getSpanId() {
		return spanId;
	}
	/**
	 * Método para configurar o identificador do Span.
	 * @param spanId
	 * @author André 			
	 */
	public void setId(final int spanId) {
		this.spanId = spanId;
	}
	/**
	 * Método para inicializar a lista de potência da fibra óptica do Span.
	 * @param numberOfSlots
	 * @author André 			
	 */
	public void inicializePowersInSpan(final int numberOfSlots){    	
    	this.powers = new ArrayList<Double>(numberOfSlots);    	
    	for(int i=0;i<numberOfSlots;i++){    		
    		this.powers.add(0.0);
    	}    	
    	this.totalPower = 0.0;
    }
	/**
	 * Método para alterar a quantidade de canais no vetor de potência.
	 * @param numberOfSlots
	 * @author André 			
	 */
	public void changeNumberOfSlots(final int numberOfSlots){		   
		this.powers = new ArrayList<Double>(numberOfSlots);    	
    	for(int i=0;i<numberOfSlots;i++){    		
    		this.powers.add(0.0);   		
    	}    	
    	this.totalPower = 0.0;
    	this.numberOfSlots = numberOfSlots;
	}
	/**
	 * Método para remover a potência de um determinado slot na potência
	 * total da fibra do Span.
	 * @param slot
	 * @author André 			
	 */
	public void deallocateTotalPower(final int slot){
		this.totalPower -= this.powers.get(slot);
	}
	/**
	 * Método para remover a potência de um determinado slot da fibra.
	 * @param slot
	 * @author André 			
	 */
	public void deallocate(final int slot){
		this.deallocateTotalPower(slot);
		this.powers.set(slot, 0.0);
	}
	/**
	 * Método para configurar a potência do slot alocado na fibra óptica do Span.
	 * @param powerValue
	 * @param slot
	 * @author André 			
	 */
	public void setPower(final double powerValue, final int slot){
		this.totalPower -= this.powers.get(slot);
		this.totalPower += powerValue;
		this.powers.set(slot, powerValue);
	}
	/**
	 * Método para retornar o número de slots do Span.
	 * @return O atributo numberOfSlots
	 * @author André 			
	 */	
	public int getNumberOfSlots() {
		return numberOfSlots;
	}
	/**
	 * Método para retornar a potência total antes do amplificador óptico.
	 * @return O atributo totalPower
	 * @author André 			
	 */	
	public double getTotalPower() {
		return totalPower;
	}
	/**
	 * Método para retornar o vetor com as potências individuais
	 * antes do amplificador óptico.
	 * @return O atributo powers
	 * @author André 			
	 */	
	public List<Double> getPowers() {
		return powers;
	}
	/**
	 * Método para resetar a potência de todos os slots do Span.
	 * @author André 			
	 */	
	public void erasePowerInSpan(){		
		this.powers = new ArrayList<Double>(numberOfSlots);		   
		for(int i=0;i<numberOfSlots;i++){			   
			this.powers.add(0.0);	
		}		   
		this.totalPower = 0.0;
	}
}
