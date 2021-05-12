package aco_simulation;

import java.util.List;
/**
 * Descreve o conjunto de param�tros do ACO.
 * @author Andr� 
 */
public class ACOParametersSet {
	/**
	 * Number of ant groups that compose the colony.
	 * @author Andr�
	 */
	private int numberOfGroups;
	/**
	 * Size of ant groups that compose the colony.
	 * @author Andr�
	 */
	private int groupSize;
	/**
	 * Number of pheromone units deposited on the links.
	 * @author Andr�
	 */
	private double pheromoneUnits;
	/**
	 * Evaporation ratio applied on the Network.
	 * @author Andr�
	 */
	private double evaporationRatio;
	/**
	 * Alpha parameter (probability equation).
	 * @author Andr�
	 */
	private List<Double> alpha; 
	/**
	 * Beta parameter (probability equation).
	 * @author Andr�
	 */
	private List<Double> beta;  
	/**
	 * Gamma parameter (probability equation).
	 * @author Andr�
	 */
	private List<Double> gamma; 
	/**
	 * Network used in the execution.
	 * @author Andr�
	 */
	private ACONetwork network; 	
	/**
	 * Number of time steps used by the ACO execution.
	 * @author Andr�
	 */
	private int timeSteps; 	
	/**
	* Construtor da classe.
	* @author Andr�
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
	 * M�todo para configurar o n�mero de grupos.
	 * @param numberOfGroups
	 * @author Andr� 			
	 */		
	public void setNumberOfGroups(final int numberOfGroups) {
		this.numberOfGroups = numberOfGroups;
	}
	/**
	 * M�todo para retornar o n�mero de grupos.
	 * @return O atributo numberOfGroups.
	 * @author Andr� 			
	 */	
	public int getNumberOfGroups() {
		return this.numberOfGroups;
	}
	/**
	 * M�todo para retornar o tamanho do grupo.
	 * @return O atributo numberOfGroups.
	 * @author Andr� 			
	 */	
	public int getGroupSize() {
		return this.groupSize;
	}
	/**
	 * M�todo para configurar o tamanho do grupo.
	 * @param numberOfGroups
	 * @author Andr� 			
	 */	
	public void setGroupSize(final int groupSize) {
		this.groupSize = groupSize;
	}
	/**
	 * M�todo para retornar o fer�monio.
	 * @return O atributo pheromoneUnits.
	 * @author Andr� 			
	 */	
	public double getPheromoneUnits() {
		return this.pheromoneUnits;
	}
	/**
	 * M�todo para configurar o fer�monio.
	 * @param pheromoneUnits
	 * @author Andr� 			
	 */	
	public void setPheromoneUnits(final double pheromoneUnits) {
		this.pheromoneUnits = pheromoneUnits;
	}
	/**
	 * M�todo para retornar a taxa de evapora��o.
	 * @return O atributo evaporationRatio.
	 * @author Andr� 			
	 */
	public double getEvaporationRatio() {
		return this.evaporationRatio;
	}
	/**
	 * M�todo para configurar a taxa de evapora��o.
	 * @param evaporationRatio
	 * @author Andr� 			
	 */	
	public void setEvaporationRatio(final double evaporationRatio) {
		this.evaporationRatio = evaporationRatio;
	}
	/**
	 * M�todo para retornar o param�tro Alpha.
	 * @return O atributo alpha.
	 * @author Andr� 			
	 */
	public List<Double> getAlpha() {
		return this.alpha;
	}
	/**
	 * M�todo para configurar o param�tro Alpha.
	 * @param evaporationRatio
	 * @author Andr� 			
	 */
	public void setAlpha(final List<Double> alpha) {
		this.alpha = alpha;
	}
	/**
	 * M�todo para retornar o param�tro Beta.
	 * @return O atributo beta.
	 * @author Andr� 			
	 */
	public List<Double> getBeta() {
		return this.beta;
	}
	/**
	 * M�todo para configurar o param�tro Beta.
	 * @param beta
	 * @author Andr� 			
	 */
	public void setBeta(final List<Double> beta) {
		this.beta = beta;
	}
	/**
	 * M�todo para retornar o param�tro Gamma.
	 * @return O atributo gamma.
	 * @author Andr� 			
	 */
	public List<Double> getGamma() {
		return this.gamma;
	}
	/**
	 * M�todo para configurar o param�tro Gamma.
	 * @param gamma
	 * @author Andr� 			
	 */
	public void setGamma(final List<Double> gamma) {
		this.gamma = gamma;
	}
	/**
	 * M�todo para retornar a topologia do ACO.
	 * @return O atributo gamma.
	 * @author Andr� 			
	 */
	public ACONetwork getNetwork() {
		return this.network;
	}
	/**
	 * M�todo para configurar a topologia do ACO.
	 * @param network
	 * @author Andr� 			
	 */
	public void setNetwork(final ACONetwork network) {
		this.network = network;
	}
	/**
	 * M�todo para retornar o n�mrero de passos do ACO.
	 * @return O atributo timeSteps.
	 * @author Andr� 			
	 */
	public int getTimeSteps() {
		return timeSteps;
	}
	/**
	 * M�todo para configurar o n�mrero de passos do ACO.
	 * @param timeSteps
	 * @author Andr� 			
	 */
	public void setTimeSteps(final int timeSteps) {
		this.timeSteps = timeSteps;
	}
}
