package aco_simulation;

import java.util.List;
/**
 * Descreve o conjunto de paramêtros do ACO.
 * @author André 
 */
public class ACOParametersSet {
	/**
	 * Number of ant groups that compose the colony.
	 * @author André
	 */
	private int numberOfGroups;
	/**
	 * Size of ant groups that compose the colony.
	 * @author André
	 */
	private int groupSize;
	/**
	 * Number of pheromone units deposited on the links.
	 * @author André
	 */
	private double pheromoneUnits;
	/**
	 * Evaporation ratio applied on the Network.
	 * @author André
	 */
	private double evaporationRatio;
	/**
	 * Alpha parameter (probability equation).
	 * @author André
	 */
	private List<Double> alpha; 
	/**
	 * Beta parameter (probability equation).
	 * @author André
	 */
	private List<Double> beta;  
	/**
	 * Gamma parameter (probability equation).
	 * @author André
	 */
	private List<Double> gamma; 
	/**
	 * Network used in the execution.
	 * @author André
	 */
	private ACONetwork network; 	
	/**
	 * Number of time steps used by the ACO execution.
	 * @author André
	 */
	private int timeSteps; 	
	/**
	* Construtor da classe.
	* @author André
	*/
	public ACOParametersSet(final double evaporationRatio, final double pheromoneUnits, final List<Double> alpha, final List<Double> beta,
			final List<Double> gamma, final int numberOfGroups, final int groupSize, final ACONetwork network, final int timeSteps){		
		this.evaporationRatio = evaporationRatio;
		this.pheromoneUnits = pheromoneUnits;
		this.alpha = alpha;
		this.beta = beta;
		this.gamma = gamma;
		this.numberOfGroups = numberOfGroups;
		this.groupSize = groupSize;
		this.network = network;
		this.timeSteps = timeSteps;	
	}
	/**
	 * Método para configurar o número de grupos.
	 * @param numberOfGroups
	 * @author André 			
	 */		
	public void setNumberOfGroups(final int numberOfGroups) {
		this.numberOfGroups = numberOfGroups;
	}
	/**
	 * Método para retornar o número de grupos.
	 * @return O atributo numberOfGroups.
	 * @author André 			
	 */	
	public int getNumberOfGroups() {
		return this.numberOfGroups;
	}
	/**
	 * Método para retornar o tamanho do grupo.
	 * @return O atributo numberOfGroups.
	 * @author André 			
	 */	
	public int getGroupSize() {
		return this.groupSize;
	}
	/**
	 * Método para configurar o tamanho do grupo.
	 * @param numberOfGroups
	 * @author André 			
	 */	
	public void setGroupSize(final int groupSize) {
		this.groupSize = groupSize;
	}
	/**
	 * Método para retornar o ferômonio.
	 * @return O atributo pheromoneUnits.
	 * @author André 			
	 */	
	public double getPheromoneUnits() {
		return this.pheromoneUnits;
	}
	/**
	 * Método para configurar o ferômonio.
	 * @param pheromoneUnits
	 * @author André 			
	 */	
	public void setPheromoneUnits(final double pheromoneUnits) {
		this.pheromoneUnits = pheromoneUnits;
	}
	/**
	 * Método para retornar a taxa de evaporação.
	 * @return O atributo evaporationRatio.
	 * @author André 			
	 */
	public double getEvaporationRatio() {
		return this.evaporationRatio;
	}
	/**
	 * Método para configurar a taxa de evaporação.
	 * @param evaporationRatio
	 * @author André 			
	 */	
	public void setEvaporationRatio(final double evaporationRatio) {
		this.evaporationRatio = evaporationRatio;
	}
	/**
	 * Método para retornar o paramêtro Alpha.
	 * @return O atributo alpha.
	 * @author André 			
	 */
	public List<Double> getAlpha() {
		return this.alpha;
	}
	/**
	 * Método para configurar o paramêtro Alpha.
	 * @param evaporationRatio
	 * @author André 			
	 */
	public void setAlpha(final List<Double> alpha) {
		this.alpha = alpha;
	}
	/**
	 * Método para retornar o paramêtro Beta.
	 * @return O atributo beta.
	 * @author André 			
	 */
	public List<Double> getBeta() {
		return this.beta;
	}
	/**
	 * Método para configurar o paramêtro Beta.
	 * @param beta
	 * @author André 			
	 */
	public void setBeta(final List<Double> beta) {
		this.beta = beta;
	}
	/**
	 * Método para retornar o paramêtro Gamma.
	 * @return O atributo gamma.
	 * @author André 			
	 */
	public List<Double> getGamma() {
		return this.gamma;
	}
	/**
	 * Método para configurar o paramêtro Gamma.
	 * @param gamma
	 * @author André 			
	 */
	public void setGamma(final List<Double> gamma) {
		this.gamma = gamma;
	}
	/**
	 * Método para retornar a topologia do ACO.
	 * @return O atributo gamma.
	 * @author André 			
	 */
	public ACONetwork getNetwork() {
		return this.network;
	}
	/**
	 * Método para configurar a topologia do ACO.
	 * @param network
	 * @author André 			
	 */
	public void setNetwork(final ACONetwork network) {
		this.network = network;
	}
	/**
	 * Método para retornar o númrero de passos do ACO.
	 * @return O atributo timeSteps.
	 * @author André 			
	 */
	public int getTimeSteps() {
		return timeSteps;
	}
	/**
	 * Método para configurar o númrero de passos do ACO.
	 * @param timeSteps
	 * @author André 			
	 */
	public void setTimeSteps(final int timeSteps) {
		this.timeSteps = timeSteps;
	}
}
