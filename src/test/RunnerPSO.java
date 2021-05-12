package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import exceptions.InvalidFiberLengthException;
import exceptions.InvalidListOfCoefficientsException;
import exceptions.InvalidNodeIdException;
import exceptions.InvalidNumberOfFrequencySlotsException;
import exceptions.InvalidRoutesException;
import network_topologies.FinlandDimention;
import network_topologies.FinlandNewDimention;
import network_topologies.FinlandiaNetwork;
import network_topologies.INetworkDimention;
import network_topologies.NsfnetDimention;
import network_topologies.NsfnetNetwork;
import network_topologies.PacificBellDimention;
import network_topologies.PacificBellNetwork;
import pso.Swarm;
import types.FuzzyMetricMethodType;
import types.PSOType;
import types.RoutingAlgorithmType;
import types.TopologyType;
import report_save.CreateFolder;
import routing.Routing;
import routing.RoutingAlgorithmSolution;


public class RunnerPSO {

	public static void main(final String[] args) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException, InvalidListOfCoefficientsException, IOException, InvalidFiberLengthException {
		
        // *** Parâmetros da simulação ***
		double networkLoad = 3.4; 					// Carga usada para simular a rede
		final int NUMBER_OF_SIMULATIONS = 10; 	// Número de simulação para cada carga. É tirada uma média
		final double NETWORK_LOAD_STEP = 0.2; 		// Passo da carga da rede
		final short NUMBER_NETWORK_LOAD = 5; 		// Números da rede
		final int NUMBER_REQUEST_MAX = 100000; 		// Números da rede

		final TopologyType TOPOLOGY_TYPE = TopologyType.FINLANDIA; // Topologia escolhida
		final RoutingAlgorithmType ROUTING_ALGORITHM_TYPE = RoutingAlgorithmType.FUZZY; // Algoritmo de roteamente escolhida

		final int KYEN = 5;
		final FuzzyMetricMethodType fuzzyLogicType = FuzzyMetricMethodType.ORIGINAL_ONE;

        //PSO
        final PSOType PSO_TYPE = PSOType.LOCAL_BEST;    // Regra de comunicação entre as particulas
        final int PSO_NUMBER_OF_PARTICLES =  30;      // Número de particulas do PSO
	    final int PSO_NUMBER_OF_INTERATIONS = 50;      // Número de interações
	    final boolean PSO_SIDESTEP = false;             //

		final long geralInitTime = System.currentTimeMillis();

		// Cria uma pasta para armazenar os resultados da simulação
		final CreateFolder folderClass = new CreateFolder(TOPOLOGY_TYPE.getDescription(), ROUTING_ALGORITHM_TYPE.getName() + "_PSO");
		
        Vector<Double> listOfCoefficients = null;
        // Armazena os parâmetetros usados na simulação
		folderClass.writeParameters(TOPOLOGY_TYPE, ROUTING_ALGORITHM_TYPE, networkLoad, NUMBER_OF_SIMULATIONS, NETWORK_LOAD_STEP, NUMBER_NETWORK_LOAD, NUMBER_REQUEST_MAX, KYEN, fuzzyLogicType, listOfCoefficients);

		folderClass.writeInResults("interation,networkLoad,Pb,simulationTime");

		INetworkDimention NetworkDimentionInstance = null;
		if (TopologyType.NSFNET14 == TOPOLOGY_TYPE){
            NetworkDimentionInstance = new NsfnetDimention();
        } else if (TopologyType.PACIFICBELL == TOPOLOGY_TYPE){
            NetworkDimentionInstance = new PacificBellDimention();
        } else if (TopologyType.FINLANDIA == TOPOLOGY_TYPE){
            NetworkDimentionInstance = new FinlandDimention();
        } else if (TopologyType.FINLANDIA_NEW == TOPOLOGY_TYPE){
            NetworkDimentionInstance = new FinlandNewDimention();
        }
		
		double[][] topologyDimention = NetworkDimentionInstance.getLength();

		final NetworkTopology NetworkTopologyInstance = new NetworkTopology(topologyDimention);

		OpticalLink[][] network = NetworkTopologyInstance.getNetworkAdjacencyMatrix();
        List<OpticalSwitch> listOfNodes = NetworkTopologyInstance.getListOfNodes();


		System.out.println("Criando todas as rotas para os pares origem destino!");
		Routing routingInstance = new Routing(network, listOfNodes, ROUTING_ALGORITHM_TYPE, KYEN);
		ArrayList<List<RoutingAlgorithmSolution>> allRoutes = routingInstance.getAllRoutes();

		System.out.println("Inicializando as partículas.......");
		
		System.out.println("Inicializando o processo de otimização.......");
		
		Swarm pso = new Swarm(PSO_TYPE.getCode(), PSO_NUMBER_OF_PARTICLES, PSO_SIDESTEP, network, listOfNodes, allRoutes, NUMBER_REQUEST_MAX, ROUTING_ALGORITHM_TYPE, networkLoad, PSO_NUMBER_OF_INTERATIONS, fuzzyLogicType, KYEN);

		long psoSimulationInicialTime = System.currentTimeMillis();	
		for(int i=0;i<PSO_NUMBER_OF_INTERATIONS;i++){
			
			long psoIterationInicialTime = System.currentTimeMillis();	
			pso.develop(i);
			long psoIterationFinalTime = System.currentTimeMillis();
			pso.resetNetwork();
			double fit = pso.getHigherFitness();
			List<Double> vfit = pso.getHigherSolution();
			
			System.out.println("iteration: "+i+" melhor fit: "+1/fit);
			folderClass.writeInResults(String.format("%d,%g,%f,%f",i, networkLoad, 1/fit, (double)(psoIterationFinalTime - psoIterationInicialTime)/1000));
			
			//String b0 = new String();
			String b0 = Integer.toString(i)+",";
			for(int j=0;j<vfit.size();j++){
				System.out.print(vfit.get(j)+" ");
				b0+=""+vfit.get(j)+",";
			}
			//folderClass.writeFile("PSO_results.txt", "iteration: "+i+"%n");
			folderClass.writeFile("PSO_results.csv", b0);
			System.out.println();	
		}
		
		long psoSimulationFinalTime = System.currentTimeMillis();
		System.out.println("Tempo de simula��o: "+(psoSimulationFinalTime-psoSimulationInicialTime)/100);
		System.out.println("checando se o resultado est� ok--------------------------------------------------");
		
		final long geralEndTime = System.currentTimeMillis();
		
		folderClass.writeDone(((double)(geralEndTime - geralInitTime))/1000);
	
	}	

}
