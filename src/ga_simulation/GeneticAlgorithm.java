package ga_simulation;

import java.util.List;
import java.util.Random;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import exceptions.InvalidNodeIdException;
import exceptions.InvalidNumberOfFrequencySlotsException;
import exceptions.InvalidRoutesException;
import routing.RoutingAlgorithmSolution;
/**
 * Descreve o algoritmo genético.
 * @author André 
 */
public class GeneticAlgorithm {
	/**
	 * Topologia da rede.
	 * @author André 
	 */
	private static OpticalLink[][] network;
	/**
	 * Lista de nós.
	 * @author André 
	 */
	private static List<OpticalSwitch> listOfNodes;
	/**
	 * Número de chamadas.
	 * @author André 
	 */
	private static int numberOfCalls;
	/**
	 * Carga da rede.
	 * @author André 
	 */
	private static double networkLoad;
	/**
	 * Número de soluções por par de nós.
	 * @author André 
	 */
	private static int numOfSolPerPair;
	/**
	 * Conjunto de soluções.
	 * @author André 
	 */
	private static RoutingAlgorithmSolution[][][] fullSolutions;
	/**
	 * Taxa de crossover.
	 * @author André 
	 */
	private static double crossoverRate;
	/**
	 * Taxa de mutação.
	 * @author André 
	 */
	private static double mutationRate;
	/**
	 * Construtor da classe.
	 * @author André 
	 */
	public GeneticAlgorithm(final OpticalLink[][] network, final List<OpticalSwitch> listOfNodes, final int numberOfCalls, final double networkLoad, // NOPMD by André on 12/07/17 14:16
			final int numOfSolutionsPerPair, final RoutingAlgorithmSolution[][][] fullSolutions, final double crossoverRate, final double mutationRate) {// NOPMD by André on 12/07/17 14:16
		GeneticAlgorithm.network = network;
		GeneticAlgorithm.listOfNodes = listOfNodes;
		GeneticAlgorithm.numberOfCalls = numberOfCalls;
		GeneticAlgorithm.networkLoad = networkLoad;
		GeneticAlgorithm.numOfSolPerPair = numOfSolutionsPerPair;
		GeneticAlgorithm.fullSolutions = fullSolutions;
		GeneticAlgorithm.crossoverRate = crossoverRate;
		GeneticAlgorithm.mutationRate = mutationRate;
	}
	/**
	 * Método para criar nova geração da população.
	 * @param population, elitism
	 * @return A nova população
	 * @author André  
	 * @throws InvalidNodeIdException 
	 * @throws InvalidRoutesException 
	 */
	public static Population newGeneration(final Population population, final boolean elitism) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException{ 
		final Random rand = new Random(); // NOPMD by André on 12/07/17 14:16
        //nova população do mesmo tamanho da antiga
		final Population newPopulation = new Population(population.getPopulationSize());

        //se tiver elitismo, mantém o melhor indivíduo da geração atual
        if (elitism) {
        	newPopulation.setIndividual(population.getIndividual(0));
        }

        //insere novos indivíduos na nova população, até atingir o tamanho máximo
        while (newPopulation.getNumOfIndividuals() < newPopulation.getPopulationSize()) {
            //seleciona os 2 pais por torneio
            final Individual[] parents = selectTournament(population);

            Individual[] childs = new Individual[2]; // NOPMD by André on 12/07/17 14:09

            //verifica a taxa de crossover, se sim realiza o crossover, 
            //se não, mantém os pais selecionados para a próxima geração
            if (rand.nextDouble() <= crossoverRate) {
                childs = crossover(parents[1], parents[0]); // NOPMD by André on 12/07/17 14:09
            } else {
                childs[0] = new Individual(parents[0].getSolutions()); // NOPMD by André on 12/07/17 14:09
                childs[1] = new Individual(parents[1].getSolutions()); // NOPMD by André on 12/07/17 14:09
            }

            //adiciona os filhos na nova geração
            newPopulation.setIndividual(childs[0]); // NOPMD by André on 12/07/17 14:09
            newPopulation.setIndividual(childs[1]); // NOPMD by André on 12/07/17 14:09
        }

        //ordena a nova população
        newPopulation.sortPopulation();
        return newPopulation;
    }
	/**
	 * Método para selecionar indivíduos da população.
	 * @param population, elitism
	 * @return A nova população
	 * @author André
	 * @throws InvalidNodeIdException 
	 * @throws InvalidRoutesException 
	 */
	public static Individual[] selectTournament(final Population population) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException{
        final Random rand = new Random();
        final Population interPopulation = new Population(3);

        //seleciona 3 indivíduos aleatóriamente na população
        interPopulation.setIndividual(population.getIndividual(rand.nextInt(population.getPopulationSize())));
        interPopulation.setIndividual(population.getIndividual(rand.nextInt(population.getPopulationSize())));
        interPopulation.setIndividual(population.getIndividual(rand.nextInt(population.getPopulationSize())));

        //ordena a população
        interPopulation.sortPopulation();

        Individual[] parents = new Individual[2]; // NOPMD by André on 12/07/17 10:25

        //seleciona os 2 melhores deste população
        parents[0] = interPopulation.getIndividual(0); // NOPMD by André on 12/07/17 10:25
        parents[1] = interPopulation.getIndividual(1);

        return parents;
    }
	/**
	 * Método para selecionar indivíduos da população.
	 * @param population, elitism
	 * @return A nova população
	 * @author André 
	 * @throws InvalidNodeIdException 
	 * @throws InvalidRoutesException 
	 */
	public static Individual[] crossover(final Individual individual1, final Individual individual2) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException{
		
		final Random rand = new Random();
		final int numOfNodes = GeneticAlgorithm.listOfNodes.size();  // NOPMD by André on 12/07/17 10:25

		//sorteia o ponto de cortec
		final int midSize = numOfNodes/2;
		final int pOne = rand.nextInt(midSize);
		final int pTwo = rand.nextInt(midSize) + midSize;
		final int pThree = rand.nextInt(midSize);  // NOPMD by André on 12/07/17 10:25
		final int pFour = rand.nextInt(midSize) + midSize;  // NOPMD by André on 12/07/17 10:25

		Individual[] childs = new Individual[2]; // NOPMD by André on 12/07/17 10:25
		
		//pega os genes dos pais
		final RoutingAlgorithmSolution[][] geneParent1 = individual1.getSolutions();  // NOPMD by André on 12/07/17 10:25
		final RoutingAlgorithmSolution[][] geneParent2 = individual2.getSolutions();  // NOPMD by André on 12/07/17 10:25

		RoutingAlgorithmSolution[][] geneChild1 = new RoutingAlgorithmSolution[numOfNodes][numOfNodes];  // NOPMD by André on 12/07/17 10:25
		RoutingAlgorithmSolution[][] geneChild2 = new RoutingAlgorithmSolution[numOfNodes][numOfNodes];  // NOPMD by André on 12/07/17 10:25
		
		for(int x=0;x<numOfNodes;x++){
			for(int y=0;y<numOfNodes;y++){
				geneChild1[x][y] = geneParent1[x][y];  // NOPMD by André on 12/07/17 10:25
				geneChild2[x][y] = geneParent2[x][y];  // NOPMD by André on 12/07/17 10:25
			}
		}
		
		for(int x=pOne;x<=pTwo;x++){ //Permuta a faixa.
			for(int y=pThree;y<=pFour;y++){
				geneChild1[y][x] = geneParent2[y][x];  // NOPMD by André on 12/07/17 10:25
				geneChild2[y][x] = geneParent1[y][x];  // NOPMD by André on 12/07/17 10:25
			}
		}

		//cria o novo indivíduo com os genes dos pais
		childs[0] = new Individual(geneChild1);  // NOPMD by André on 12/07/17 10:25
		childs[1] = new Individual(geneChild2);  // NOPMD by André on 12/07/17 10:25

		return childs;
	}

	/**
	 * Método para retornar a rede usada no GA.
	 * @return O atributo network
	 * @author André
	 */
	public static OpticalLink[][] getNetwork() {
		return network; // NOPMD by André on 13/07/17 08:58
	}

	/**
	 * Método para retornar a lista de nós usada no GA.
	 * @return O atributo listOfNodes
	 * @author André
	 */
	public static List<OpticalSwitch> getListOfNodes() {
		return listOfNodes;
	}

	/**
	 * Método para retornar o número de soluções por
	 * par de nós da rede.
	 * @return O atributo numOfSolutionsPerPair
	 * @author André
	 */
	public static int getNumOfSolutionsPerPair() {
		return numOfSolPerPair;
	}

	/**
	 * Método para retornar o número de chamadas em 
	 * cada avaliação da rede.
	 * @return O atributo numberOfCalls
	 * @author André
	 */
	public static int getNumberOfCalls() {
		return numberOfCalls;
	}

	/**
	 * Método para retornar a carga utilizada na avaliação dos indivíduos.
	 * @return O atributo meanRateBetCall
	 * @author André
	 */
	public static double getNetworkLoad() {
		return networkLoad;
	}

	/**
	 * Método para retornar a o conjunto total de soluções.
	 * @return A matriz fullSolutions
	 * @author André
	 */
	public static RoutingAlgorithmSolution[][][] getFullSolutions() {
		return fullSolutions; // NOPMD by André on 13/07/17 09:03
	}
	/**
	 * Método para retornar a taxa de crossover.
	 * @return O atributo crossoverRate
	 * @author André
	 */
	public static double getCrossoverRate() {
		return crossoverRate;
	}
	/**
	 * Método para retornar a taxa de mutação.
	 * @return O atributo mutationRate
	 * @author André
	 */
	public static double getMutationRate() {
		return mutationRate;
	}	

}
