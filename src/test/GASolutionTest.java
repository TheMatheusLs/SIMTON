package test;

import java.util.ArrayList;
import java.util.List;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import call_request.CallRequest;
import exceptions.InvalidFiberLengthException;
import exceptions.InvalidListOfCoefficientsException;
import exceptions.InvalidNodeIdException;
import exceptions.InvalidNumberOfFrequencySlotsException;
import exceptions.InvalidRoutesException;
import ga_simulation.Evaluate;
import ga_simulation.GeneticAlgorithm;
import ga_simulation.Population;
import network_topologies.NsfnetNetwork;
import parameters.SimulationParameters;
import routing.IAlternativeRoutingAlgorithm;
import routing.RoutingAlgorithmSolution;
import routing.YenAlgorithm;
import types.CallRequestType;

public class GASolutionTest {

	public static void main(String[] args) throws InvalidFiberLengthException, InvalidNodeIdException, InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidListOfCoefficientsException {
		
		final NsfnetNetwork nsfnetInstance = NsfnetNetwork.getNSFNETNetworkInstance();
		final OpticalLink[][] network = nsfnetInstance.getNetworkAdjacencyMatrix();
		final List<OpticalSwitch> listOfNodes = nsfnetInstance.getListOfNodes();
		final int numbOfNodes = listOfNodes.size();
		final SimulationParameters parameters = new SimulationParameters();
		final int indexK = parameters.getK();
		final RoutingAlgorithmSolution[][][] solutions = new RoutingAlgorithmSolution[numbOfNodes][numbOfNodes][indexK];
		final RoutingAlgorithmSolution[][] solDijkstra = new RoutingAlgorithmSolution[numbOfNodes][numbOfNodes];
		final Evaluate evalInstance = Evaluate.getEvaluateInstance();
		List<RoutingAlgorithmSolution> solutYen = new ArrayList<RoutingAlgorithmSolution>();
		
		for(int x=0;x<listOfNodes.size();x++){
			for(int y=0;y<listOfNodes.size();y++){				
				if(x!=y){
					//gera a chamada.
					CallRequest call = new CallRequest(0,parameters.getBitRate(), CallRequestType.BIDIRECTIONAL);
					call.setSourceId(x);
					call.setDestinationId(y);
					call.setBitRate(40);
				
					IAlternativeRoutingAlgorithm yenInstance = YenAlgorithm.getYenInstance();					
					solutYen = yenInstance.findRoutes(network, call, listOfNodes);
					
					for(int z=0;z<solutYen.size();z++){
						solutions[x][y][z] = solutYen.get(z); 
					}
				}
				
			}
		}
		/*
		for(int x=0;x<listOfNodes.size();x++){
			for(int y=0;y<listOfNodes.size();y++){
				RoutingAlgorithmSolution solution = solutions[x][y];
				
				System.out.println("Source: "+x+" Destination: "+y);
				
				if(solution != null){
					if(!solution.getUpLink().isEmpty()){
						System.out.print(""+solution.getUpLink().get(0).getSource());
						for(int s=0;s<solution.getUpLink().size();s++){
							System.out.print(" "+solution.getUpLink().get(s).getDestination());
						}
						System.out.println();
						if(!solution.getDownLink().isEmpty()){
							System.out.print(""+solution.getDownLink().get(0).getSource());
							for(int s=0;s<solution.getDownLink().size();s++){
								System.out.print(" "+solution.getDownLink().get(s).getDestination());
							}
							System.out.println();
						}else{
							if(solution.getDownLink().isEmpty()){
								System.out.println("Não tem rota de downlink!");
							}							
						}							
					}
					
				}else{
					System.out.println("Sem solução");
				}
				
			}
		}*/
		
		for(int x=0;x<listOfNodes.size();x++){
			for(int y=0;y<listOfNodes.size();y++){
				System.out.println("Source: "+x+" Destination: "+y);
				
				for(int z=0;z<indexK;z++){					
					
					if(solutions[x][y][z]== null){
						System.out.println("Sem solução na posição "+z);						
					}else{
						RoutingAlgorithmSolution solution = solutions[x][y][z];
						solDijkstra[x][y] = solutions[x][y][0];
						if(solution != null){
							if(!solution.getUpLink().isEmpty()){
								System.out.print(""+solution.getUpLink().get(0).getSource());
								for(int s=0;s<solution.getUpLink().size();s++){
									System.out.print(" "+solution.getUpLink().get(s).getDestination());
								}
								System.out.println();
								if(!solution.getDownLink().isEmpty()){
									System.out.print(""+solution.getDownLink().get(0).getSource());
									for(int s=0;s<solution.getDownLink().size();s++){
										System.out.print(" "+solution.getDownLink().get(s).getDestination());
									}
									System.out.println();
								}else{
									if(solution.getDownLink().isEmpty()){
										System.out.println("Não tem rota de downlink!");
									}							
								}							
							}							
						}
						
						
					}					
				}				
			}
		}
		
		
		double bestSolution = 0.01;
		double crossoverRate = 0.6;
		double mutationRate = 0.03;
		boolean eltism = true;
        int tamPop = 50;
        
        //numero máximo de gerações
        int numMaxGeracoes = 100;
        
        GeneticAlgorithm gaAlgorithm = new GeneticAlgorithm(network, listOfNodes, 100000, 2.20, indexK, solutions, crossoverRate, mutationRate);

        //cria a primeira população aleatérioa
        Population population = new Population(listOfNodes.size(), tamPop);
        
        population.sortPopulation();
        
        System.out.println("Iniciando... Melhor fitness durante criação da população: "+population.getIndividual(0).getFitness());
        

        boolean hasSolution = false;
        int geracao = 0;

        System.out.println("Iniciando... Melhor solução: "+bestSolution);
        
        //loop até o critério de parada
        while (!hasSolution && geracao < numMaxGeracoes) {
            geracao++;

            //cria nova populacao
            population = GeneticAlgorithm.newGeneration(population, eltism);
            
            population.sortPopulation();

            System.out.println("Geração " + geracao + " | Aptidão: " + population.getIndividual(0).getFitness());
            
            //verifica se tem a solucao
            hasSolution = population.hasSolution(bestSolution);
        }

        if (geracao == numMaxGeracoes) {
            System.out.println("Número Maximo de Gerações | Melhor fitness: " + population.getIndividual(0).getFitness());
        }

        if (hasSolution) {
            System.out.println("Encontrado resultado na geração " + geracao + " | melhor fitness: " + population.getIndividual(0).getFitness());
        }
		
	//	double fitness = evalInstance.simulation(network, listOfNodes, 100000, 2.50, solutions);
		
	//	System.out.println("PB da solução "+fitness);
		
		// TODO Auto-generated method stub

	}

}
