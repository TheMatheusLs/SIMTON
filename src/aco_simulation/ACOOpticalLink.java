package aco_simulation;
/**
 * Descreve o optical link usado no ACO.
 * @author André
 */
public class ACOOpticalLink {
	/**
	 * Custo do optical link.
	 * @author André			
	 */	
	private double cost;
	/**
	 * Identificador do optical link.
	 * @author André			
	 */	
	private transient int opticalLinkId;
	/**
	 * Número de slots do optical link.
	 * @author André			
	 */	
	private int numberOfSlots;
	/**
	 * Número de slots em uso do optical link.
	 * @author André			
	 */	
	private int unusedSlots;
	/**
	 * Ferômonio do optical link.
	 * @author André			
	 */	
	private double pheromone;
	/**
	 * Nó fonte do optical link.
	 * @author André			
	 */	
	private int sourceNode;
	/**
	 * Nó destino do optical link.
	 * @author André			
	 */	
	private int destinationNode;
	/**
	* Construtor da classe.
	* @param opticalLinkId
	* @param cost
	* @param numberOfSlots
	* @author André
	*/
	public ACOOpticalLink(final int opticalLinkId, final double cost, final int numberOfSlots, final int sourceNode, final int destinationNode){
		this.cost = cost;
		this.opticalLinkId = opticalLinkId;
		this.numberOfSlots = numberOfSlots;
		this.unusedSlots = numberOfSlots; 
		this.sourceNode = sourceNode;
		this.destinationNode = destinationNode;
		this.pheromone = 1.0;
	}
	/**
	 * Método para retornar o identificador do opticalLink.
	 * @return O atributo opticalLinkId.
	 * @author André 			
	 */			
	public int getOpticalLinkId() {
		return opticalLinkId;
	}
	/**
	 * Método para configurar o identificador do opticalLink.
	 * @param opticalLinkId
	 * @author André 			
	 */
	public void setOpticalLinkId(final int opticalLinkId) {
		this.opticalLinkId = opticalLinkId;
	}
	/**
	 * Método para retornar o custo do opticalLink.
	 * @return O atributo cost.
	 * @author André 			
	 */
	public double getCost() {
		return cost;
	}
	/**
	 * Método para configurar o custo do opticalLink.
	 * @param opticalLinkId
	 * @author André 			
	 */
	public void setCost(final double cost) {
		this.cost = cost;
	}
	/**
	 * Método para retornar o número de slots.
	 * @return O atributo numberOfSlots.
	 * @author André 			
	 */
	public int getNumberOfSlots() {
		return numberOfSlots;
	}
	/**
	 * Método para configurar o número de slots.
	 * @param numberOfSlots
	 * @author André 			
	 */
	public void setNumberOfSlots(final int numberOfSlots) {
		this.numberOfSlots = numberOfSlots;
	}
	/**
	 * Método para retornar o número de slots usados.
	 * @return O atributo unusedSlots.
	 * @author André 			
	 */
	public int getUnusedSlots() {
		return unusedSlots;
	}
	/**
	 * Método para configurar o número de slots usados.
	 * @param numberOfSlots
	 * @author André 			
	 */
	public void setUnusedSlots(final int unusedSlots) {
		this.unusedSlots = unusedSlots;
	}
	/**
	 * Método para retornar o ferômonio.
	 * @return O atributo pheromone.
	 * @author André 			
	 */
	public double getPheromone() {
		return pheromone;
	}
	/**
	 * Método para configurar o ferômonio.
	 * @param pheromone
	 * @author André 			
	 */
	public void setPheromone(final double pheromone) {
		this.pheromone = pheromone;
	}
	/**
	 * Método para retornar o nó fonte.
	 * @return O atributo sourceNode.
	 * @author André 			
	 */
	public int getSourceNode() {
		return sourceNode;
	}
	/**
	 * Método para configurar o nó fonte.
	 * @param sourceNode
	 * @author André 			
	 */
	public void setSourceNode(final int sourceNode) {
		this.sourceNode = sourceNode;
	}
	/**
	 * Método para retornar o nó destino.
	 * @return O atributo destinationNode.
	 * @author André 			
	 */
	public int getDestinationNode() {
		return destinationNode;
	}
	/**
	 * Método para configurar o nó destino.
	 * @param destinationNode
	 * @author André 			
	 */
	public void setDestinationNode(final int destinationNode) {
		this.destinationNode = destinationNode;
	}
}
