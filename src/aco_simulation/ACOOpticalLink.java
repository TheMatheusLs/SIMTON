package aco_simulation;
/**
 * Descreve o optical link usado no ACO.
 * @author Andr�
 */
public class ACOOpticalLink {
	/**
	 * Custo do optical link.
	 * @author Andr�			
	 */	
	private double cost;
	/**
	 * Identificador do optical link.
	 * @author Andr�			
	 */	
	private transient int opticalLinkId;
	/**
	 * N�mero de slots do optical link.
	 * @author Andr�			
	 */	
	private int numberOfSlots;
	/**
	 * N�mero de slots em uso do optical link.
	 * @author Andr�			
	 */	
	private int unusedSlots;
	/**
	 * Fer�monio do optical link.
	 * @author Andr�			
	 */	
	private double pheromone;
	/**
	 * N� fonte do optical link.
	 * @author Andr�			
	 */	
	private int sourceNode;
	/**
	 * N� destino do optical link.
	 * @author Andr�			
	 */	
	private int destinationNode;
	/**
	* Construtor da classe.
	* @param opticalLinkId
	* @param cost
	* @param numberOfSlots
	* @author Andr�
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
	 * M�todo para retornar o identificador do opticalLink.
	 * @return O atributo opticalLinkId.
	 * @author Andr� 			
	 */			
	public int getOpticalLinkId() {
		return opticalLinkId;
	}
	/**
	 * M�todo para configurar o identificador do opticalLink.
	 * @param opticalLinkId
	 * @author Andr� 			
	 */
	public void setOpticalLinkId(final int opticalLinkId) {
		this.opticalLinkId = opticalLinkId;
	}
	/**
	 * M�todo para retornar o custo do opticalLink.
	 * @return O atributo cost.
	 * @author Andr� 			
	 */
	public double getCost() {
		return cost;
	}
	/**
	 * M�todo para configurar o custo do opticalLink.
	 * @param opticalLinkId
	 * @author Andr� 			
	 */
	public void setCost(final double cost) {
		this.cost = cost;
	}
	/**
	 * M�todo para retornar o n�mero de slots.
	 * @return O atributo numberOfSlots.
	 * @author Andr� 			
	 */
	public int getNumberOfSlots() {
		return numberOfSlots;
	}
	/**
	 * M�todo para configurar o n�mero de slots.
	 * @param numberOfSlots
	 * @author Andr� 			
	 */
	public void setNumberOfSlots(final int numberOfSlots) {
		this.numberOfSlots = numberOfSlots;
	}
	/**
	 * M�todo para retornar o n�mero de slots usados.
	 * @return O atributo unusedSlots.
	 * @author Andr� 			
	 */
	public int getUnusedSlots() {
		return unusedSlots;
	}
	/**
	 * M�todo para configurar o n�mero de slots usados.
	 * @param numberOfSlots
	 * @author Andr� 			
	 */
	public void setUnusedSlots(final int unusedSlots) {
		this.unusedSlots = unusedSlots;
	}
	/**
	 * M�todo para retornar o fer�monio.
	 * @return O atributo pheromone.
	 * @author Andr� 			
	 */
	public double getPheromone() {
		return pheromone;
	}
	/**
	 * M�todo para configurar o fer�monio.
	 * @param pheromone
	 * @author Andr� 			
	 */
	public void setPheromone(final double pheromone) {
		this.pheromone = pheromone;
	}
	/**
	 * M�todo para retornar o n� fonte.
	 * @return O atributo sourceNode.
	 * @author Andr� 			
	 */
	public int getSourceNode() {
		return sourceNode;
	}
	/**
	 * M�todo para configurar o n� fonte.
	 * @param sourceNode
	 * @author Andr� 			
	 */
	public void setSourceNode(final int sourceNode) {
		this.sourceNode = sourceNode;
	}
	/**
	 * M�todo para retornar o n� destino.
	 * @return O atributo destinationNode.
	 * @author Andr� 			
	 */
	public int getDestinationNode() {
		return destinationNode;
	}
	/**
	 * M�todo para configurar o n� destino.
	 * @param destinationNode
	 * @author Andr� 			
	 */
	public void setDestinationNode(final int destinationNode) {
		this.destinationNode = destinationNode;
	}
}
