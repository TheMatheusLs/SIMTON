package pso;

import java.util.ArrayList;
import java.util.List;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import exceptions.InvalidListOfCoefficientsException;
import exceptions.InvalidNodeIdException;
import exceptions.InvalidNumberOfFrequencySlotsException;
import exceptions.InvalidRoutesException;
import parameters.SimulationParameters;
import routing.RoutingAlgorithmSolution;
import types.FuzzyMetricMethodType;
import types.RoutingAlgorithmType;


/**
 * Descreve o exame de part�cula usado no PSO.
 * @author Andr� 
 */
public class Swarm {
	
	/**
	 * Topologia de rede.
	 * @author Andr� 
	 */
	private transient final OpticalLink[][] network;
	/**
	 * Lista de n�s.
	 * @author Andr� 
	 */
	private transient final List<OpticalSwitch> listOfNodes;
	/**
	 * N�mero de requisi��es.
	 * @author Andr� 
	 */
	private transient final int numberOfCalls;
	/**
	 * Tipo de algoritmo de roteamento.
	 * @author Andr� 
	 */
	private transient final RoutingAlgorithmType routAlgType;
	/**
	 * Taxa m�dia entre chamadas.
	 * @author Andr� 
	 */
	private transient final double meanRateBetCalls;
	/**
	 * Lista de part�culas do exame.
	 * @author Andr� 
	 */
	private transient final List<Particle> agents;
	/**
	 * N�mero de part�culas.
	 * @author Andr� 
	 */
	private transient int numberOfAgents;
	/**
	 * Vizinhan�a da part�cula.
	 * @author Andr� 
	 */
	private transient final int neighborhood;
	/**
	 * Fitness da melhor part�cula.
	 * @author Andr� 
	 */
	private transient double worstFitness; //Fitness from the worst particle
	/**
	 * Dimens�o da part�cula.
	 * @author Andr� 
	 */
	private int dimension;
	/**
	 * N�mero de itera��es.
	 * @author Andr� 
	 */
	public transient int iterations;

	private transient ArrayList<List<RoutingAlgorithmSolution>> allRoutes;

	private transient FuzzyMetricMethodType fuzzyLogicType;

	private transient int KYEN;
	/**
	 * Construtor da classe.
	 * @param neighborhood
	 * @param numberOfAgents
	 * @param numberOfIter
	 * @author Andr� 
	 */
	public Swarm(final int neighborhood, final int numberOfAgents, final boolean sidestep, final OpticalLink[][] network,
			final List<OpticalSwitch> listOfNodes, ArrayList<List<RoutingAlgorithmSolution>> allRoutes, final int numberOfCalls, final RoutingAlgorithmType routAlgType, final double meanRateBetCalls, final int numberOfIter, FuzzyMetricMethodType fuzzyLogicType, int KYEN) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException, InvalidListOfCoefficientsException {
		
		final SimulationParameters parameters = new SimulationParameters();
		this.agents = new ArrayList<Particle>(numberOfAgents);
		this.neighborhood = neighborhood;
		this.numberOfAgents = numberOfAgents;
		this.dimension = parameters.getDimension();
		this.worstFitness = Double.MAX_VALUE;
		this.network = network;
		this.listOfNodes = listOfNodes;
		this.allRoutes = allRoutes;
		this.numberOfCalls = numberOfCalls;
		this.routAlgType = routAlgType;
		this.meanRateBetCalls = meanRateBetCalls;
		this.iterations = numberOfIter;
		this.fuzzyLogicType = fuzzyLogicType;
		this.KYEN = KYEN;
		
		for (int i=0; i<this.numberOfAgents;i++){
			   
			   final Particle particle = new Particle(dimension, sidestep); // NOPMD by Andr� on 27/06/17 11:45
			   
			   particle.fitnessEvaluation(network, listOfNodes, allRoutes, numberOfCalls, routAlgType, meanRateBetCalls, fuzzyLogicType, KYEN);
			   
			   resetNetwork(); // NOPMD by Andr� on 27/06/17 11:45
			   
			   System.out.println("Id do individuo:  "+i+" Fitness: "+1/(particle.getFitness())); // NOPMD by Andr� on 27/06/17 11:45
			   
			   if (particle.getFitness() < worstFitness){
				   worstFitness = particle.getFitness();
			   }
			   
			   this.agents.add(particle);
			  
		   }
	}
	/**
	 * reseta a topologia de rede.
	 * @author Andr� 
	 */	
	public void resetNetwork(){
		
		for(int x=0;x<this.listOfNodes.size();x++){
			for(int y=0;y<this.listOfNodes.size();y++){
				if(network[x][y] != null){
					network[x][y].eraseOpticalLink(); // NOPMD by Andr� on 27/06/17 11:18
				}								
			}
		}
	}
	/**
	 * M�todo para retornar o n�mero de part�culas.
	 * @return O atributo numberOfAgents
	 * @author Andr� 
	 */
	public int getNumberOfAgents() {
		return numberOfAgents;
	}
	/**
	 * M�todo para configurar o n�mero de part�culas.
	 * @return O atributo numberOfAgents
	 * @author Andr� 
	 */
	public void setNumberOfAgents(final int numberOfAgents) {
		this.numberOfAgents = numberOfAgents;
	}
	/**
	 * M�todo para retornar a dimens�o.
	 * @return O atributo dimension
	 * @author Andr� 
	 */
	public int getDimension() {
		return dimension;
	}
	/**
	 * M�todo para configurar a dimens�o.
	 * @param dimension
	 * @author Andr� 
	 */
	public void setDimension(final int dimension) {
		this.dimension = dimension;
	}
	/**
	 * M�todo para retornar o melhor fitness da vizinhan�a.
	 * @param dimension
	 * @author Andr� 
	 */
	public List<Double> lbestMaximum(final Particle neighbor1, final Particle neighbor2){
		
		if (neighbor1.getpBest() > neighbor2.getpBest()){
			return neighbor1.getpBestX(); // NOPMD by Andr� on 27/06/17 11:52
		}else{
			return neighbor2.getpBestX();
		}
	}
	/**
	 * M�todo para inicializar o exame.
	 * @param dimension
	 * @author Andr� 
	 */
	public void develop (final int iteration) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException, InvalidListOfCoefficientsException{
		
		double wFitness = Double.MAX_VALUE;
		
		System.out.println("Itera��o: "+ iteration+" ------------------" ); // NOPMD by Andr� on 27/06/17 11:54
		
		for (int i=0; i<this.agents.size(); i++){
			
			if(this.neighborhood == 0){
				
				this.agents.get(i).updateVelocity(lbestMaximum(agents.get((i+agents.size()-1)%agents.size()),agents.get((i+1)%agents.size())),
				     										iteration, iterations,worstFitness);				
				
			}else if (neighborhood == 1){ // NOPMD by Andr� on 27/06/17 11:54
				this.agents.get(i).updateVelocity(iteration);
			}
	   
			this.agents.get(i).updatePosition();
			
			this.agents.get(i).fitnessEvaluation(network, listOfNodes, allRoutes, numberOfCalls, routAlgType, meanRateBetCalls, fuzzyLogicType, KYEN);

			this.resetNetwork();
			
			//////////
			System.out.println("Id do individuo: "+i+" Fitnesse: "+1/this.agents.get(i).getpBest()); // NOPMD by Andr� on 27/06/17 11:54
			
//			for(int j=0;j<this.agents.get(i).getpBestX().size();j++){
//				System.out.print(this.agents.get(i).getpBestX().get(j)+" "); // NOPMD by Andr� on 27/06/17 11:54
//			}
			System.out.println(); // NOPMD by Andr� on 27/06/17 11:54
			//////////
			
			if(this.agents.get(i).getFitness() < wFitness){
				wFitness = this.agents.get(i).getFitness();
			}
		}
		
		this.worstFitness = wFitness;     //Update the iteration's worst fitness

	}
	/**
	 * M�todo para retornar o melhor fitness do exame.
	 * @return O melhor fitness
	 * @author Andr� 
	 */
	@SuppressWarnings("static-access")
	public double getHigherFitness(){
	   return this.agents.get(0).getgBest();
	}
	/**
	 * M�todo para retornar a melhro posi��o do exame.
	 * @return O melhor fitness
	 * @author Andr� 
	 */	
	@SuppressWarnings("static-access")
	public List<Double> getHigherSolution(){
	   return this.agents.get(0).getgBestPosition();
	}			
			

}
