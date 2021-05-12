package aco_simulation;

import java.util.ArrayList;
import java.util.List;
/**
 * Descreve o ant no ACO.
 * @author Andr�
 */
public class ACOAnt{ // NOPMD by Andr� on 16/06/17 14:26
	/**
	 * Identificador do n� destino.
	 * @author Andr� 			
	 */	
	private int destinationNode;
	/**
	 * Identificador do n� fonte.
	 * @author Andr� 			
	 */	
	private int sourceNode;
	/**
	 * Identificador do n� atual.
	 * @author Andr� 			
	 */	
	private int currentNode;
	/**
	 * N�s ja visitados.
	 * @author Andr� 			
	 */	
	private final transient List<Boolean> memorizedNodes;
	/**
	 * List with the fibers used by the ant (links of the tour).
	 * @author Andr� 			
	 */	
	private List<ACOOpticalLink> tourUplink; 
	/**
	 * Tour cost.
	 * @author Andr� 			
	 */
	private double tourUplinkCost;
	/**
	 * List with the current possible nodes to be reached by the ant.
	 * @author Andr� 			
	 */
	private transient List<Integer> curPossNextNodes;
	/**
	 * Flag that mark the ant as dead.
	 * @author Andr� 			
	 */
	private boolean dead; 
	/**
	 * Erro.
	 * @author Andr� 			
	 */
	private static final int DECISION_ERROR = -1;
	/**
	 * Topologia de rede usada no ACO.
	 * @author Andr� 			
	 */
	private final transient ACONetwork network;
	/**
	 * Algoritmo usado no ACO.
	 * @author Andr� 			
	 */
	private final transient ACOAlgorithm aco;
	/**
	* Construtor da classe.
	* @param sourceNode
	* @param destinationNode
	* @author Andr�
	*/
	public ACOAnt(final int sourceNode, final int destinationNode) {		
		this.dead = false;
		this.tourUplinkCost = 0.0;
		this.currentNode = sourceNode;
		this.sourceNode = sourceNode;
		this.destinationNode = destinationNode;
		this.memorizedNodes = new ArrayList<Boolean>();
		this.tourUplink = new ArrayList<ACOOpticalLink>();
		this.network = ACONetwork.getACONetworkInstance();
		this.aco = ACOAlgorithm.getACOAlgInstance();
		for (int i=0; i<network.getNumberOfNodes(); i++) {
			this.memorizedNodes.add(false);
		}
	}
	/**
	 * M�todo para retornar o identificador do n� fonte.
	 * @return O atributo sourceNode
	 * @author Andr� 			
	 */		
	public int getSourceNode() {
		return this.sourceNode;
	}
	/**
	 * M�todo para configura o identificador do n� fonte.
	 * @param sourceNode
	 * @author Andr� 			
	 */	
	public void setSourceNode(final int sourceNode) {
		this.sourceNode = sourceNode;
	}
	/**
	 * M�todo para retornar o identificador do n� destino.
	 * @return O atributo destinationNode
	 * @author Andr� 			
	 */	
	public int getDestinationNode() {
		return this.destinationNode;
	}
	/**
	 * M�todo para configura o identificador do n� destino.
	 * @param destinationNode
	 * @author Andr� 			
	 */	
	public void setDestinationNode(final int destinationNode) {
		this.destinationNode = destinationNode;
	}	
	/**
	 * M�todo para retornar o n� corrente.
	 * @return O atributo currentNode.
	 * @author Andr� 			
	 */	
	public int getCurrentNode() {
		return currentNode;
	}
	/**
	 * M�todo para configura o n� corrente.
	 * @param currentNode
	 * @author Andr� 			
	 */	
	public void setCurrentNode(final int currentNode) {
		this.currentNode = currentNode;
	}
	/**
	 * M�todo para retornar a lista de optical link do tour.
	 * @return O atributo tourUplink.
	 * @author Andr� 			
	 */	
	public List<ACOOpticalLink> getTourUplink() {
		return tourUplink;
	}
	/**
	 * M�todo para configurar a lista de optical link do tour.
	 * @return O atributo tourUplink.
	 * @author Andr� 			
	 */	
	public void setTourUplink(final List<ACOOpticalLink> tourUplink) {
		this.tourUplink = tourUplink;
	}
	/**
	 * M�todo para retornar o custo total dos optical link do tour.
	 * @return O atributo tourUplinkCost.
	 * @author Andr� 			
	 */	
	public double getTourUplinkCost() {
		return tourUplinkCost;
	}
	/**
	 * M�todo para configurar o custo total dos optical link do tour.
	 * @param tourUplinkCost.
	 * @author Andr� 			
	 */	
	public void setTourUplinkCost(final double tourUplinkCost) {
		this.tourUplinkCost = tourUplinkCost;
	}
	/**
	 * M�todo que verifica se a ant est� ou n�o morta.
	 * @return O atributo dead
	 * @author Andr� 			
	 */	
	public boolean isDead() {
		return dead;
	}
	/**
	 * M�todo para configurar o estado da ant.
	 * @param dead
	 * @author Andr� 			
	 */
	public void setDead(final boolean dead) {
		this.dead = dead;
	}
	/**
	 * M�todo para mover a ant de n� corrente.
	 * @author Andr� 			
	 */
	public void move(){
		if (currentNode != destinationNode && !dead) {
			this.moveForward();
		}		
	}	
	/**
	 * Memorize visited nodes
	 */
	public void memorizeNode(){
		this.memorizedNodes.set(this.currentNode, true);
	}	
	/**
	 * Forward movement
	 */
	public void moveForward(){
		this.memorizeNode();
		final int selectedNode = this.selectNextNode();
		// ant gets trapped
		if (selectedNode == DECISION_ERROR) {
			this.performHarakiri();
		} else {
			this.crossLink(selectedNode);
			currentNode = selectedNode;
		}		
	}
	/**
	 * Select next node to reach
	 * return: the antDecisionRule method
	 */
	public int selectNextNode(){
		this.curPossNextNodes = this.possibleNextNodes();
		return this.antDecisionRule(); 
	}
	/**
	 * Cross the link
	 * @param: nextNode
	 */
	public void crossLink(final int nextNode){
			if(this.network.getLinkOfNetwork(this.currentNode, nextNode) != null){
				this.tourUplink.add(this.network.getLinkOfNetwork(this.currentNode, nextNode));				
			}			
	}
	
	/**
	 * List the possible next nodes to be reached
	 * return: the possible next nodes to reach
	 */
	private List<Integer> possibleNextNodes(){
		final List<Integer> nextNodes = new ArrayList<Integer>();
		for(int i=0; i<this.network.getNumberOfNodes(); i++){
			if(this.network.getLinkOfNetwork(this.currentNode,i) != null &&
				!this.memorizedNodes.get(i)){
				nextNodes.add(i);
			}
		}
		return nextNodes;		
	}
	
	/**
	 * The ant decision rule
	 * return: the decision rule method that will be used
	 */
	private int antDecisionRule(){
		for(int i=0; i<this.curPossNextNodes.size(); i++){
			if(this.network.getLinkOfNetwork(this.currentNode, curPossNextNodes.get(i)).getPheromone()>1.0){ // NOPMD by Andr� on 16/06/17 14:22
				return this.antPheromoneDecisionRule(); // NOPMD by Andr� on 16/06/17 14:22
			}
		}
		return this.antRandomDecisionRule();
	}	
	/**
	 * Random decision rule to reach the next node (without pheromone)
	 * return: a random node to be reached
	 */
	private int antRandomDecisionRule(){
		// ant have no nodes to decide
		if (this.curPossNextNodes.isEmpty()){
			return DECISION_ERROR; // NOPMD by dwdmlab4 on 16/06/17 14:22
		}
		final int randNextNodeIndex = (int)(Math.random()*this.curPossNextNodes.size()); // NOPMD by Andr� on 16/06/17 14:22
		return this.curPossNextNodes.get(randNextNodeIndex);
	}
	/**
	 * Pheromone based decision rule to reach the next node
	 * return: the next node to be reached
	 */
	private int antPheromoneDecisionRule(){
		// ant have no nodes to decide
		if (this.curPossNextNodes.isEmpty()){
			return DECISION_ERROR; // NOPMD by Andr� on 16/06/17 14:22
		}
		// decides based on ACO heuristic
		ACOOpticalLink fiber = this.network.getLinkOfNetwork(this.currentNode, this.curPossNextNodes.get(0)); // NOPMD by Andr� on 16/06/17 14:22
		int nextNode = this.curPossNextNodes.get(0); // NOPMD by Andr� on 16/06/17 14:22
		ACOOpticalLink tempFiber = null; // NOPMD by Andr� on 16/06/17 14:22
		for(int i=1;i<this.curPossNextNodes.size(); i++){
			tempFiber = this.network.getLinkOfNetwork(this.currentNode, this.curPossNextNodes.get(i));
			if (this.calculateProbability(tempFiber)>this.calculateProbability(fiber)){
				fiber = tempFiber; // NOPMD by Andr� on 16/06/17 14:22
				nextNode = this.curPossNextNodes.get(i); // NOPMD by Andr� on 16/06/17 14:22
			}
		}
		return nextNode;
	}
	/**
	 * ACO heuristic (probability equation)
	 * @param: the link
	 * return: the probability to choose a specific link
	 */
	private double calculateProbability(final ACOOpticalLink fiber){
		
		final List<Double> alpha = this.aco.getAlpha();
		final List<Double> beta = this.aco.getBeta();
	 
		return Math.random()*(Math.pow(fiber.getPheromone(), alpha.get(0))/Math.pow(fiber.getCost(), beta.get(0)));	  // NOPMD by Andr� on 16/06/17 14:24
		
	}
	/** Ant return trip */
	public void performReturnTrip(){
		for(int i=0; i<this.tourUplink.size(); i++){
			this.tourUplinkCost += this.tourUplink.get(i).getCost();
		}		
		for(int i=0; i<this.tourUplink.size(); i++) {
			this.layPheromone(this.tourUplink.get(i));
		}
	}
	/**
	 * Lay pheromone on the link
	 * @param: fiber
	 */	
	public void layPheromone(final ACOOpticalLink fiber){
		final ACOOpticalLink forwardFiber = fiber;
		forwardFiber.setPheromone(forwardFiber.getPheromone()+(this.aco.getPheromoneUnits()/this.tourUplinkCost)); // NOPMD by Andr� on 16/06/17 14:25
		final ACOOpticalLink backwardFiber = this.network.getLinkOfNetwork(fiber.getDestinationNode(), fiber.getSourceNode());
		backwardFiber.setPheromone(backwardFiber.getPheromone()+(this.aco.getPheromoneUnits()/this.tourUplinkCost)); // NOPMD by Andr� on 16/06/17 14:25
	}	

	/** Prideful ritual that the ant performs suicide */
	public void performHarakiri(){
		this.dead = true;
	}
	/** Clears memorized nodes */
	public void memorizedNodesClear(){
		for(int i=0; i<this.memorizedNodes.size(); i++){
			this.memorizedNodes.set(i, false);
		}
	}
    /** Clears tour */
	public void tourClear(){
		this.tourUplinkCost = 0.0;
		this.tourUplink = new ArrayList<ACOOpticalLink>();
	}
}
