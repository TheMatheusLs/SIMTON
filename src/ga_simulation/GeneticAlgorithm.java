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
 * Descreve o algoritmo gen�tico.
 * @author Andr� 
 */
public class GeneticAlgorithm {
	/**
	 * Topologia da rede.
	 * @author Andr� 
	 */
	private static OpticalLink[][] network;
	/**
	 * Lista de n�s.
	 * @author Andr� 
	 */
	private static List<OpticalSwitch> listOfNodes;
	/**
	 * N�mero de chamadas.
	 * @author Andr� 
	 */
	private static int numberOfCalls;
	/**
	 * Carga da rede.
	 * @author Andr� 
	 */
	private static double networkLoad;
	/**
	 * N�mero de solu��es por par de n�s.
	 * @author Andr� 
	 */
	private static int numOfSolPerPair;
	/**
	 * Conjunto de solu��es.
	 * @author Andr� 
	 */
	private static RoutingAlgorithmSolution[][][] fullSolutions;
	/**
	 * Taxa de crossover.
	 * @author Andr� 
	 */
	private static double crossoverRate;
	/**
	 * Taxa de muta��o.
	 * @author Andr� 
	 */
	private static double mutationRate;
	/**
	 * Construtor da classe.
	 * @author Andr� 
	 */
	public GeneticAlgorithm(final OpticalLink[][] network, final List<OpticalSwitch> listOfNodes, final int numberOfCalls, final double networkLoad, // NOPMD by Andr� on 12/07/17 14:16
			final int numOfSolutionsPerPair, final RoutingAlgorithmSolution[][][] fullSolutions, final double crossoverRate, final double mutationRate) {// NOPMD by Andr� on 12/07/17 14:16
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
	 * M�todo para criar nova gera��o da popula��o.
	 * @param population, elitism
	 * @return A nova popula��o
	 * @author Andr�  
	 * @throws InvalidNodeIdException 
	 * @throws InvalidRoutesException 
	 */
	public static Population newGeneration(final Population population, final boolean elitism) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException{ 
		final Random rand = new Random(); // NOPMD by Andr� on 12/07/17 14:16
        //nova popula��o do mesmo tamanho da antiga
		final Population newPopulation = new Population(population.getPopulationSize());

        //se tiver elitismo, mant�m o melhor indiv�duo da gera��o atual
        if (elitism) {
        	newPopulation.setIndividual(population.getIndividual(0));
        }

        //insere novos indiv�duos na nova popula��o, at� atingir o tamanho m�ximo
        while (newPopulation.getNumOfIndividuals() < newPopulation.getPopulationSize()) {
            //seleciona os 2 pais por torneio
            final Individual[] parents = selectTournament(population);

            Individual[] childs = new Individual[2]; // NOPMD by Andr� on 12/07/17 14:09

            //verifica a taxa de crossover, se sim realiza o crossover, 
            //se n�o, mant�m os pais selecionados para a pr�xima gera��o
            if (rand.nextDouble() <= crossoverRate) {
                childs = crossover(parents[1], parents[0]); // NOPMD by Andr� on 12/07/17 14:09
            } else {
                childs[0] = new Individual(parents[0].getSolutions()); // NOPMD by Andr� on 12/07/17 14:09
                childs[1] = new Individual(parents[1].getSolutions()); // NOPMD by Andr� on 12/07/17 14:09
            }

            //adiciona os filhos na nova gera��o
            newPopulation.setIndividual(childs[0]); // NOPMD by Andr� on 12/07/17 14:09
            newPopulation.setIndividual(childs[1]); // NOPMD by Andr� on 12/07/17 14:09
        }

        //ordena a nova popula��o
        newPopulation.sortPopulation();
        return newPopulation;
    }
	/**
	 * M�todo para selecionar indiv�duos da popula��o.
	 * @param population, elitism
	 * @return A nova popula��o
	 * @author Andr�
	 * @throws InvalidNodeIdException 
	 * @throws InvalidRoutesException 
	 */
	public static Individual[] selectTournament(final Population population) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException{
        final Random rand = new Random();
        final Population interPopulation = new Population(3);

        //seleciona 3 indiv�duos aleat�riamente na popula��o
        interPopulation.setIndividual(population.getIndividual(rand.nextInt(population.getPopulationSize())));
        interPopulation.setIndividual(population.getIndividual(rand.nextInt(population.getPopulationSize())));
        interPopulation.setIndividual(population.getIndividual(rand.nextInt(population.getPopulationSize())));

        //ordena a popula��o
        interPopulation.sortPopulation();

        Individual[] parents = new Individual[2]; // NOPMD by Andr� on 12/07/17 10:25

        //seleciona os 2 melhores deste popula��o
        parents[0] = interPopulation.getIndividual(0); // NOPMD by Andr� on 12/07/17 10:25
        parents[1] = interPopulation.getIndividual(1);

        return parents;
    }
	/**
	 * M�todo para selecionar indiv�duos da popula��o.
	 * @param population, elitism
	 * @return A nova popula��o
	 * @author Andr� 
	 * @throws InvalidNodeIdException 
	 * @throws InvalidRoutesException 
	 */
	public static Individual[] crossover(final Individual individual1, final Individual individual2) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException{
		
		final Random rand = new Random();
		final int numOfNodes = GeneticAlgorithm.listOfNodes.size();  // NOPMD by Andr� on 12/07/17 10:25

		//sorteia o ponto de cortec
		final int midSize = numOfNodes/2;
		final int pOne = rand.nextInt(midSize);
		final int pTwo = rand.nextInt(midSize) + midSize;
		final int pThree = rand.nextInt(midSize);  // NOPMD by Andr� on 12/07/17 10:25
		final int pFour = rand.nextInt(midSize) + midSize;  // NOPMD by Andr� on 12/07/17 10:25

		Individual[] childs = new Individual[2]; // NOPMD by Andr� on 12/07/17 10:25
		
		//pega os genes dos pais
		final RoutingAlgorithmSolution[][] geneParent1 = individual1.getSolutions();  // NOPMD by Andr� on 12/07/17 10:25
		final RoutingAlgorithmSolution[][] geneParent2 = individual2.getSolutions();  // NOPMD by Andr� on 12/07/17 10:25

		RoutingAlgorithmSolution[][] geneChild1 = new RoutingAlgorithmSolution[numOfNodes][numOfNodes];  // NOPMD by Andr� on 12/07/17 10:25
		RoutingAlgorithmSolution[][] geneChild2 = new RoutingAlgorithmSolution[numOfNodes][numOfNodes];  // NOPMD by Andr� on 12/07/17 10:25
		
		for(int x=0;x<numOfNodes;x++){
			for(int y=0;y<numOfNodes;y++){
				geneChild1[x][y] = geneParent1[x][y];  // NOPMD by Andr� on 12/07/17 10:25
				geneChild2[x][y] = geneParent2[x][y];  // NOPMD by Andr� on 12/07/17 10:25
			}
		}
		
		for(int x=pOne;x<=pTwo;x++){ //Permuta a faixa.
			for(int y=pThree;y<=pFour;y++){
				geneChild1[y][x] = geneParent2[y][x];  // NOPMD by Andr� on 12/07/17 10:25
				geneChild2[y][x] = geneParent1[y][x];  // NOPMD by Andr� on 12/07/17 10:25
			}
		}

		//cria o novo indiv�duo com os genes dos pais
		childs[0] = new Individual(geneChild1);  // NOPMD by Andr� on 12/07/17 10:25
		childs[1] = new Individual(geneChild2);  // NOPMD by Andr� on 12/07/17 10:25

		return childs;
	}

	/**
	 * M�todo para retornar a rede usada no GA.
	 * @return O atributo network
	 * @author Andr�
	 */
	public static OpticalLink[][] getNetwork() {
		return network; // NOPMD by Andr� on 13/07/17 08:58
	}

	/**
	 * M�todo para retornar a lista de n�s usada no GA.
	 * @return O atributo listOfNodes
	 * @author Andr�
	 */
	public static List<OpticalSwitch> getListOfNodes() {
		return listOfNodes;
	}

	/**
	 * M�todo para retornar o n�mero de solu��es por
	 * par de n�s da rede.
	 * @return O atributo numOfSolutionsPerPair
	 * @author Andr�
	 */
	public static int getNumOfSolutionsPerPair() {
		return numOfSolPerPair;
	}

	/**
	 * M�todo para retornar o n�mero de chamadas em 
	 * cada avalia��o da rede.
	 * @return O atributo numberOfCalls
	 * @author Andr�
	 */
	public static int getNumberOfCalls() {
		return numberOfCalls;
	}

	/**
	 * M�todo para retornar a carga utilizada na avalia��o dos indiv�duos.
	 * @return O atributo meanRateBetCall
	 * @author Andr�
	 */
	public static double getNetworkLoad() {
		return networkLoad;
	}

	/**
	 * M�todo para retornar a o conjunto total de solu��es.
	 * @return A matriz fullSolutions
	 * @author Andr�
	 */
	public static RoutingAlgorithmSolution[][][] getFullSolutions() {
		return fullSolutions; // NOPMD by Andr� on 13/07/17 09:03
	}
	/**
	 * M�todo para retornar a taxa de crossover.
	 * @return O atributo crossoverRate
	 * @author Andr�
	 */
	public static double getCrossoverRate() {
		return crossoverRate;
	}
	/**
	 * M�todo para retornar a taxa de muta��o.
	 * @return O atributo mutationRate
	 * @author Andr�
	 */
	public static double getMutationRate() {
		return mutationRate;
	}	

}
