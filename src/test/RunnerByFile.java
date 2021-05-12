package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import network_topologies.INetworkDimention;
import network_topologies.NetworkTopology;
import network_topologies.NsfnetDimention;
import network_topologies.PacificBellDimention;
import network_topologies.TestDimention;
import network_topologies.ToroidalDimention;
import parameters.SimulationParameters;
import report_save.CreateFolder;
import routing.Routing;
import routing.RoutingAlgorithmSolution;
import simulation.IRMLSASimulation;
import simulation.RMLSASimulation;
import types.RoutingAlgorithmType;
import types.TopologyType;
import types.MetricMethodType;
import utility.Debug;
import utility.Function;
/**
 * Descreve o algoritmo para avaliar os algoritmos de RMLSA.
 * @author André adptado por Matheus
 */
public class RunnerByFile {

	public static void main(final String[] args) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException, InvalidListOfCoefficientsException, IOException, InvalidFiberLengthException, Exception {		
		
		// *** Parâmetros da simulação ***
		int networkLoad = 360; 						//Carga inicial da rede
		final short NUMBER_OF_SIMULATIONS = 1; 	// Número de simulação para cada carga. É tirada uma média
		final int NETWORK_LOAD_STEP = 20; 			// Passo da carga da rede
		final short NUMBER_NETWORK_LOAD = 1; 		// Números da rede
		final int NUMBER_REQUEST_MAX = 100000; 		// Números da rede
 
		final TopologyType TOPOLOGY_TYPE = TopologyType.FINLANDIA; // Topologia escolhida
		final RoutingAlgorithmType ROUTING_ALGORITHM_TYPE = RoutingAlgorithmType.ALTERNATIVE; // Algoritmo de roteamente escolhida

		final int KYEN = 3;
		final boolean isKYEN = true; // True testa a alocação em todas as K rotas, false só ordena e utiliza a primeira delas
	
		final MetricMethodType fuzzyLogicType = MetricMethodType.YEN;

		double[] coefficientsMetric = {0.5663032254055006,0.06372554758312637,-0.28139525664072407,1.0,0.665058630282055,0.30380093499433336,0.5646009891390902,1.0,0.2666308088665653}; // PSR -1 1


		// Cria uma pasta para armazenar os resultados da simulação
		final CreateFolder folderClass = new CreateFolder(TOPOLOGY_TYPE.getDescription(), ROUTING_ALGORITHM_TYPE.getName() + "_" + fuzzyLogicType.getName());

		// Armazena os parâmetetros usados na simulação
		//folderClass.writeParameters(TOPOLOGY_TYPE, ROUTING_ALGORITHM_TYPE, networkLoad, NUMBER_OF_SIMULATIONS, NETWORK_LOAD_STEP, NUMBER_NETWORK_LOAD, NUMBER_REQUEST_MAX, KYEN, fuzzyLogicType, coefficientsMetric);

		final long geralInitTime = System.currentTimeMillis();

		//Adiciona o cabeçalho dos resultados
		folderClass.writeInResults("networkLoad,meanPb,pbDesviation,pbErro,pbMargem,meanSimulationTime");

		INetworkDimention NetworkDimentionInstance = null;
		if (TopologyType.NSFNET14 == TOPOLOGY_TYPE){
            NetworkDimentionInstance = new NsfnetDimention();
        } else if (TopologyType.PACIFICBELL == TOPOLOGY_TYPE){
            NetworkDimentionInstance = new PacificBellDimention();
        } else if (TopologyType.FINLANDIA == TOPOLOGY_TYPE){
            NetworkDimentionInstance = new FinlandDimention();
        } else if (TopologyType.FINLANDIA_NEW == TOPOLOGY_TYPE){
            NetworkDimentionInstance = new FinlandNewDimention();
        } else if (TopologyType.TOROIDAL == TOPOLOGY_TYPE){
            NetworkDimentionInstance = new ToroidalDimention();
        }
		// Teste de topologia
		//NetworkDimentionInstance = new TestDimention();
		
		double[][] topologyDimention = NetworkDimentionInstance.getLength();

		final NetworkTopology NetworkTopologyInstance = new NetworkTopology(topologyDimention);

		OpticalLink[][] network = NetworkTopologyInstance.getNetworkAdjacencyMatrix();
        List<OpticalSwitch> listOfNodes = NetworkTopologyInstance.getListOfNodes();

		final IRMLSASimulation simRMLSAInstance = RMLSASimulation.getRMLSASimulationInstance();
		
		System.out.println("Criando todas as rotas para os pares origem destino!");
		Routing routingInstance = new Routing(network, listOfNodes, ROUTING_ALGORITHM_TYPE, KYEN);
		ArrayList<List<RoutingAlgorithmSolution>> allRoutes = routingInstance.getAllRoutes();
		routingInstance.findAllModulationsByRoute();
		routingInstance.generateConflictList();
		routingInstance.OrderConflictRoutes();

		Function function = new Function();

		Vector<Vector<Double>> simulationResults = new Vector<Vector<Double>>();
		Vector<Double> partialPb = new Vector<Double>();
		Vector<Double> simulationTime = new Vector<Double>();
		Vector<Double> partialThroughput = new Vector<Double>();

		SimulationParameters parameters = new SimulationParameters();

		// Cria a estrutura para armazenar as informações de debug
		Debug debugClass = new Debug(true, folderClass, isKYEN);
		debugClass.setFolderToReadReqs("05-05-21_10-34-38_FINLANDIA_ALTERNATIVO_YEN_OK");
		
		for(int i=0;i<NUMBER_NETWORK_LOAD;i++){ //For each network load
			
			partialPb.clear();
			simulationTime.clear();
			partialThroughput.clear();
			
			System.out.println();
			System.out.println("Carga: "+ (networkLoad / parameters.getMeanRateOfCallsDuration()));

			// Inicia o arquivo de armazenamento das requisições
			if (debugClass.isDebugReqON()){
				String logSimulationHeader = "reqID,networkLoad,source,destination,O-D,durationTime,decayTime,time,bitRate,reqNumbOfSlots,routeNodes,slots,modulation,status,OSNRth,OSNR,numQoQErr,numSlotErr,SlotErr,QoQErr,routeToFindFuzzy,routeKYEN";
				debugClass.setLogReqSimulationHeader(logSimulationHeader);

				String logSimulationFilename = String.format("logSimulation_%d.csv",(int)(networkLoad / parameters.getMeanRateOfCallsDuration()));
				debugClass.setLogReqSimulationFilename(logSimulationFilename);

				folderClass.writeFile(logSimulationFilename, logSimulationHeader);

				debugClass.setNetworkLoad(networkLoad);
			}
			
			for(int j=0;j<NUMBER_OF_SIMULATIONS;j++){ //number of simulations/network load					
			
				System.out.println();
				System.out.println("Simulation: "+ j);
									
				final long tempoInicial = System.currentTimeMillis();

				
				// Inicia o arquivo de armazenamento das requisições
				if (debugClass.isDebugReqON()){
					debugClass.setNumSimulationID(j);
				}

				final double pb = simRMLSAInstance.simulation(network, listOfNodes, NUMBER_REQUEST_MAX, ROUTING_ALGORITHM_TYPE, networkLoad, allRoutes, coefficientsMetric, fuzzyLogicType, KYEN, debugClass);
				
				final long tempoFinal = System.currentTimeMillis();

				partialPb.addElement(pb);
				simulationTime.addElement(((double)(tempoFinal - tempoInicial))/1000);

				System.out.print("PB: "+pb);
				System.out.print(" Simulation time: "+ (double)(tempoFinal - tempoInicial)/1000);
				System.out.println();
				
				NetworkTopologyInstance.resetNetworkAdjacencyMatrix();
				routingInstance.cleanRoutes();
			}		
			
			// Retira da média os 10% menores valores e os 10% maiores
			partialPb = function.removeOutliers(partialPb, 0.1);
			
			double pbDesviation = function.getStandardDeviation(partialPb);
			double thDesviation = function.getStandardDeviation(partialThroughput);
			double timeDesviation = function.getStandardDeviation(simulationTime);
			double pbErro = pbDesviation/Math.sqrt(partialPb.size());
			double timeErro = timeDesviation/Math.sqrt(partialPb.size());
			double thErro = thDesviation/Math.sqrt(partialThroughput.size());
			double pbMargem = pbErro*1.96;
			double timeMargem = timeErro*1.96;
			double thMargem = thErro*1.96;
						
			System.out.println("Mean: "+function.getMean(partialPb)+ " Desviation: "+pbDesviation+" Erro: "+pbErro+" Margem: "+pbMargem);

			folderClass.writeInResults((networkLoad / parameters.getMeanRateOfCallsDuration()) + "," + function.getMean(partialPb) + "," + pbDesviation + "," + pbErro + "," + pbMargem + "," + function.getMean(simulationTime));
			
			simulationResults.addElement(partialPb);		
			
			if (function.getMean(partialPb) == 0.0){
				System.out.println("Acabou porque obteve 0 Pb");
				break;
			}
			
			networkLoad -= NETWORK_LOAD_STEP;
		}

		final long geralEndTime = System.currentTimeMillis();
		
		folderClass.writeDone(((double)(geralEndTime - geralInitTime))/1000);		
	}
}