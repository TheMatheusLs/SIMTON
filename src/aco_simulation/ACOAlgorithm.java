package aco_simulation;

import java.util.ArrayList;
import java.util.List;
/**
 * Descreve o algoritmo do ACO.
 * @author André
 */
public class ACOAlgorithm { // NOPMD by André on 16/06/17 12:01
	/**
	 * Instância da Classe.
	 * @author André 			
	 */	
	private static final ACOAlgorithm ACO_ALG_INSTANCE = new ACOAlgorithm();
	/**
	 * List with each ant group.
	 * @author André 			
	 */	
	private final transient List<ArrayList<ACOAnt>> colony; 
	/**
	 * Number of pheromone units deposited on the links.
	 * @author André 			
	 */	
	private transient double pheromoneUnits;
	/**
	 * Evaporation ratio applyed on the Network.
	 * @author André 			
	 */	
	private transient double evaporationRatio; 
	/**
	 * Number of ant groups that compose the colony.
	 * @author André 			
	 */	
	private transient double numberOfGroups; 
	/**
	 * Number of ants that compose a group.
	 * @author André 			
	 */	
	private transient double groupSize;
	/**
	 * The network used by the algorithm implementation.
	 * @author André 			
	 */	
	private transient ACONetwork network; 
	/**
	 * Alpha parameter (probability equation).
	 * @author André 			
	 */	
	private transient List<Double> alpha;
	/**
	 * Beta parameter (probability equation).
	 * @author André 			
	 */	
	private transient List<Double> beta;
	/**
	 * Gamma parameter (probability equation).
	 * @author André 			
	 */	
	private transient List<Double> gamma; 
	/**
	 * Frequency of the route calls.
	 * @author André 			
	 */	
	private transient int routeCallFreq;
	/**
	 * List with all the different route solutions 
	 * achieved by the algorithm execution.
	 * @author André 			
	 */	
	private final transient List<ACOAnt> diffRouteSolut; 
	/**
	 * Number of time steps.
	 * @author André 			
	 */	
	private transient int timeSteps;
	/**
	* Construtor da classe.
	* @author André
	*/
	public ACOAlgorithm(){
		this.alpha = new ArrayList<Double>();
		this.beta = new ArrayList<Double>();
		this.gamma = new ArrayList<Double>();
		this.colony = new ArrayList<ArrayList<ACOAnt>>();
		this.diffRouteSolut = new ArrayList<ACOAnt>();
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
	 * Método para retornar o Alpha.
	 * @return O atributo alpha.
	 * @author André 			
	 */	
	public List<Double> getAlpha() {
		return this.alpha;
	}
	/**
	 * Método para retornar o Beta.
	 * @return O atributo beta.
	 * @author André 			
	 */
	public List<Double> getBeta() {
		return this.beta;
	}
	/**
	 * Método para retornar o Gamma.
	 * @return O atributo gamma.
	 * @author André 			
	 */
	public List<Double> getGamma() {
		return this.gamma;
	}
	/**
	 * Método para retornar as soluções diferentes de roteamento.
	 * @return O atributo diffRouteSolut.
	 * @author André 			
	 */
	public List<ACOAnt> getDifferentRouteSolutions() {
		return diffRouteSolut;
	}
	
	/**
	 * Define the essential parameters used by ACO algorithm
	 * @param: ACO essential parameters
	 */
	public void defineParameters(final ACOParametersSet parameters){
		this.routeCallFreq = 2*parameters.getNetwork().getNumberOfNodes(); // NOPMD by André on 16/06/17 11:36
		this.timeSteps = parameters.getTimeSteps()*this.routeCallFreq;
		this.pheromoneUnits = parameters.getPheromoneUnits();
		this.evaporationRatio = parameters.getEvaporationRatio();
		this.network = parameters.getNetwork();
		this.numberOfGroups = parameters.getNumberOfGroups();
		this.groupSize = parameters.getGroupSize();
		this.alpha = parameters.getAlpha();
		this.beta = parameters.getBeta();
		this.gamma = parameters.getGamma();
	}
	
	/**
	 * Performs an ACO simulation
	 * @author André
	 */
	public void performSimulation(){ // NOPMD by André on 16/06/17 11:44
		// clear the instance previous simulation stored solutions
		this.diffRouteSolut.clear();

		int timSteps = 0;
		ACOAnt solutionAnt = null;
		//int[] route = null;
		int routeSource = 0; // NOPMD by André on 16/06/17 11:41
		int routeDestination = 0; // NOPMD by André on 16/06/17 11:41

		while (timSteps <= this.timeSteps) {
			if (timSteps%routeCallFreq == 0) {
				// store previous route solution
				if(solutionAnt != null) {
					this.storeRouteSolution(solutionAnt);
				}
				// select a different route
				final int[] route = this.selectRoute();
				routeSource = route[0];
				routeDestination = route[1];
				// create the next route solution reference
				solutionAnt = new ACOAnt(routeSource, routeDestination); // NOPMD by André on 16/06/17 11:40
				solutionAnt.setTourUplinkCost(Double.MAX_VALUE);
				// clear colony to permit another route call
				for(int n=0; n<this.colony.size(); n++){
					for(int k=0; k<this.groupSize; k++){
						if(this.colony.get(n).get(k) != null){
							this.launchForwardAnt(this.colony.get(n).get(k), routeSource, routeDestination);
						}
					}
				}
			}
			// create group
			if (colony.size() < numberOfGroups) {
				final ArrayList<ACOAnt> antList = new ArrayList<ACOAnt>(); // NOPMD by André on 16/06/17 11:42
				this.colony.add(antList);
			}
			// each created group of the colony
			for (int n=0; n<this.colony.size(); n++){
				// each ant of the actual swarm
				for(int k=0; k<this.groupSize; k++){
					ArrayList<ACOAnt> groupPtr = null; // NOPMD by André on 16/06/17 11:42
					ACOAnt antPtr = null; // NOPMD by André on 16/06/17 11:42
					// ant group in turn
					groupPtr = colony.get(n);
					// complete the group with a new ant
					if(groupPtr.size()<this.groupSize){	 // NOPMD by André on 16/06/17 11:42
						this.launchForwardAnt(n, routeSource, routeDestination);
					}
					// throw away the dead ant
					if(groupPtr.get(k).isDead()){ // NOPMD by André on 16/06/17 11:44
						this.launchForwardAnt(groupPtr.get(k), routeSource, routeDestination); // NOPMD by André on 16/06/17 11:42
					}
					antPtr = groupPtr.get(k); // NOPMD by André on 16/06/17 11:42
					antPtr.move(); // NOPMD by dwdmlab4 on 16/06/17 11:42
					// the k ant is the first to reach the destination
					if(antPtr.getCurrentNode()==routeDestination){ // NOPMD by André on 16/06/17 11:43
						// the k ant lay pheromone on his tour and get killed
						this.lauchBackwardAnt(n, k);
						// best solution of the swarm
						if(antPtr.getTourUplinkCost() < solutionAnt.getTourUplinkCost()){ // NOPMD by André on 16/06/17 11:43
							solutionAnt = antPtr; // NOPMD by André on 16/06/17 11:43
						}
						// kill the current solution ant (k ant)
						antPtr.performHarakiri(); // NOPMD by André on 16/06/17 11:44
					}
				}
			}
			// evaporate network pheromone
			this.evaporatePheromone();

			timSteps++;
		}
		
	}
	/**
	 * Método para retornar a instância da classe.
	 * @return O objeto ACO_ALG_INSTANCE
	 * @author André 			
	 */	
	public static ACOAlgorithm getACOAlgInstance(){
		return ACO_ALG_INSTANCE;
	}
	
	/**
	 * Route request
	 * @param: ant that will receive the best solution
	 * @param: route source node
	 * @param: route destination node
	 */
	public void request(final ACOAnt solution, final int routeSource, final int routeDestination){ // NOPMD by André on 16/06/17 12:00
		int timSteps = 0;
		solution.setTourUplinkCost(Double.MAX_VALUE);
	
	  // clear colony to permit another route call
		for(int n=0; n<this.colony.size(); n++){
			for(int k=0; k<this.groupSize; k++){
				if(this.colony.get(n).get(k) != null){
					this.launchForwardAnt(this.colony.get(n).get(k), routeSource, routeDestination);
				}
			}
		}

		while (timSteps <= this.timeSteps){
			// create group
			if (this.colony.size() < this.numberOfGroups) {
				final ArrayList<ACOAnt> antList = new ArrayList<ACOAnt>(); // NOPMD by André on 16/06/17 11:47
				this.colony.add(antList);
			}
			// each created group of the colony
			for (int n=0; n<this.colony.size(); n++){
				// each ant of the actual swarm
				for(int k=0; k<this.groupSize; k++){
					ArrayList<ACOAnt> groupPtr = null; // NOPMD by André on 16/06/17 11:47
					ACOAnt antPtr = null; // NOPMD by dwdmlab4 on 16/06/17 11:47
					// ant group in turn
					groupPtr = colony.get(n);
					// complete the group with a new ant
					if(groupPtr.size()<this.groupSize){ // NOPMD by André on 16/06/17 11:48
						this.launchForwardAnt(n, routeSource, routeDestination);
					}
					// throw away the dead ant
					if (groupPtr.get(k).isDead()){ // NOPMD by André on 16/06/17 11:48
						this.launchForwardAnt(groupPtr.get(k), routeSource, routeDestination); // NOPMD by André on 16/06/17 11:48
					}
					antPtr = groupPtr.get(k); // NOPMD by André on 16/06/17 11:49
					antPtr.move(); // NOPMD by André on 16/06/17 11:49
					// the k ant is the first to reach the destination
					if(antPtr.getCurrentNode()==routeDestination){ // NOPMD by André on 16/06/17 11:48
						// the k ant lay pheromone on his tour and get killed
						this.lauchBackwardAnt(n, k);
						// best solution of the swarm
						if (antPtr.getTourUplinkCost() < solution.getTourUplinkCost()){ // NOPMD by André on 16/06/17 11:48
							solution.setTourUplink(antPtr.getTourUplink()); // NOPMD by André on 16/06/17 11:48
							solution.setTourUplinkCost(antPtr.getTourUplinkCost()); // NOPMD by André on 16/06/17 11:48
						}
						// kill the current solution ant (k ant)
						antPtr.performHarakiri(); // NOPMD by André on 16/06/17 11:49
					}
				}
			}
			// evaporate network pheromone
			this.evaporatePheromone();

			timSteps++;
		}
	}
	/** Evaporates pheromone on all links of the network */
	public void evaporatePheromone(){
		for (int i = 0; i < this.network.getNumberOfNodes(); i++) {
			for (int j = 0; j < this.network.getNumberOfNodes(); j++) {
				if (this.network.getLinkOfNetwork(i, j)!=null){
					this.network.getLinkOfNetwork(i, j).setPheromone(this.network.getLinkOfNetwork(i, j).getPheromone()*(1-this.evaporationRatio));
					// pheromone min value are 1.0
					final double pheromoneMin = 1.0; // NOPMD by André on 16/06/17 11:52
					if (this.network.getLinkOfNetwork(i, j).getPheromone()<pheromoneMin){
						this.network.getLinkOfNetwork(i, j).setPheromone(1.0);
					}
				}
			}
		}
		
	}

	/**
	 * Select a random route.
	 * return: a route
	 */
	public int[] selectRoute(){
		int[] route = new int[2]; // NOPMD by André on 16/06/17 11:53
		route[0] = (int) Math.floor(Math.random()*this.network.getNumberOfNodes());	 // NOPMD by André on 16/06/17 11:53
		route[1] = (int) Math.floor(Math.random()*this.network.getNumberOfNodes());	// NOPMD by André on 16/06/17 11:53
		while (route[0] == route[1]) {
			route[1] = (int) Math.floor(Math.random()*this.network.getNumberOfNodes());	// NOPMD by André on 16/06/17 11:53
		}
		return route;
	}
	/**
	 * Armazena a rota
	 * @param: solution
	 * @author André
	 */	
	private void storeRouteSolution(final ACOAnt solution){
		if (solution != null){
			this.diffRouteSolut.add(solution);
		}		
	}
	/**
	 * Launch a ant moving forward in the network
	 * @param: ant 
	 * @param: sourceNode the ant source node
	 * @param: destinationNode the ant destination node
	 */	
	public void launchForwardAnt(final ACOAnt ant, final int sourceNode, final int destinationNode){
		ant.setCurrentNode(sourceNode);
		ant.setSourceNode(sourceNode);
		ant.setDestinationNode(destinationNode);
		ant.memorizedNodesClear();
		ant.tourClear();
		ant.setTourUplinkCost(0.0);
		ant.setDead(false);
	}
	/**
	 * Launch a ant moving forward in the network
	 * @param: groupIndex the index of the ant group
	 * @param: sourceNode the ant source node
	 * @param: destinationNode the ant destination node
	 */	
	public void launchForwardAnt(final int groupIndex, final int sourceNode, final int destinationNode){
		this.colony.get(groupIndex).add(new ACOAnt(sourceNode, destinationNode));
	}
	/**
	 * Launch a ant moving backward in the network (return trip)
	 * @param: groupIndex the index of the ant group
	 * @param: antIndex the index of the ant inside the group
	 */
	public void lauchBackwardAnt(final int groupIndex, final int antIndex) {
		this.colony.get(groupIndex).get(antIndex).performReturnTrip();
	}		

}
