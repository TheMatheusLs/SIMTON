package ga_simulation;

import exceptions.InvalidListOfCoefficientsException;
import exceptions.InvalidNodeIdException;
import exceptions.InvalidNumberOfFrequencySlotsException;
import exceptions.InvalidRoutesException;
import routing.RoutingAlgorithmSolution;
/**
 * Descreve a indiv�duo usado no GA.
 * @author Andr� 
 */
public class Individual {
	/**
	 * Descreve o genes do indiv�duo.
	 * @author Andr� 
	 */
	private transient RoutingAlgorithmSolution [][] solutions;
	/**
	 * Descreve o fitness do indiv�duo.
	 * @author Andr� 
	 */
	private transient double fitness;
	/**
	 * Construtor da classe.
	 * @param numOfNodes
	 * @throws InvalidNodeIdException 
	 * @throws InvalidRoutesException 
	 * @author Andr� 
	 */

	public Individual(final int numOfNodes) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException {
		this.solutions = new RoutingAlgorithmSolution[numOfNodes][numOfNodes];		
		for(int x=0;x<numOfNodes;x++){
			for(int y=0;y<numOfNodes;y++){
				if(x!=y){	
					final int numberOfSolutions = GeneticAlgorithm.getFullSolutions()[x][y].length;
					int position;
					do{
						position = (int) Math.floor(Math.random()*numberOfSolutions);
						this.solutions[x][y] = GeneticAlgorithm.getFullSolutions()[x][y][position];	
					}while(GeneticAlgorithm.getFullSolutions()[x][y][position]==null);			
				}				
			}
		}
		evaluateFitness();
		System.out.println("Fitness: "+this.fitness);
	}
	/**
	 * Construtor da classe.
	 * @param solutions
	 * @throws InvalidListOfCoefficientsException 
	 * @throws InvalidNodeIdException 
	 * @throws InvalidRoutesException 
	 * @author Andr� 
	 */
	public Individual(final RoutingAlgorithmSolution[][] solutions) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException{ // NOPMD by Andr� on 11/07/17 13:00
		final int numOfNodes = GeneticAlgorithm.getListOfNodes().size();		
		this.solutions = new RoutingAlgorithmSolution[numOfNodes][numOfNodes];
		
		for(int x=0;x<numOfNodes;x++){
			for(int y=0;y<numOfNodes;y++){
				if(x!=y){
					this.solutions[x][y] = solutions[x][y];
				}				
			}
		}
		evaluateFitness();
	}
	/**
	 * M�todo para avaliar o fitness do indiv�duo.
	 * @throws InvalidListOfCoefficientsException 
	 * @throws InvalidNodeIdException 
	 * @throws InvalidRoutesException 
	 * @throws InvalidNumberOfFrequencySlotsException
	 * @author Andr�  
	 */
	private void evaluateFitness() throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException{
		final Evaluate gaInstance = Evaluate.getEvaluateInstance();
		this.resetNetwork();
		this.fitness = gaInstance.simulation(GeneticAlgorithm.getNetwork(), GeneticAlgorithm.getListOfNodes(), GeneticAlgorithm.getNumberOfCalls(),GeneticAlgorithm.getNetworkLoad(), solutions); // NOPMD by Andr� on 11/07/17 13:00
	}
	/**
	 * M�todo para resetar a rede.
	 * @author Andr�  
	 */
	private void resetNetwork(){		
		int numOfNodes = GeneticAlgorithm.getListOfNodes().size();
		for(int x=0;x<numOfNodes;x++){
			for(int y=0;y<numOfNodes;y++){
				if(GeneticAlgorithm.getNetwork()[x][y] != null){
					GeneticAlgorithm.getNetwork()[x][y].eraseOpticalLink(); // NOPMD by Andr� on 21/06/17 13:12										
				}								
			}
		}	
	}

	/**
	 * M�todo para retornar os genes do indiv�duo. 
	 * @return O atributo solutions
	 * @author Andr� 
	 */
	public RoutingAlgorithmSolution[][] getSolutions() {
		return solutions; // NOPMD by Andr� on 11/07/17 13:01
	}
	/**
	 * M�todo para retornar o fitness do indiv�duo.
	 * @return O atributo fitness
	 * @author Andr� 
	 */
	public double getFitness() {
		return fitness;
	}
}
