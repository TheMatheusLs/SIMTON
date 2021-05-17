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
public class Runner {

	public static void main(final String[] args) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException, InvalidListOfCoefficientsException, IOException, InvalidFiberLengthException, Exception {		
		
		// *** Parâmetros da simulação ***
		int networkLoad = 460; 						// Carga inicial da rede (inicia pela carga mais elevada)
		final short NUMBER_OF_SIMULATIONS = 20; 	// Número de simulação para cada carga. É tirada uma média
		final int NETWORK_LOAD_STEP = 20; 			// Passo da carga da rede
		final short NUMBER_NETWORK_LOAD = 12; 		// Números da rede
		final int NUMBER_REQUEST_MAX = 100000; 		// Números da rede
 
		final TopologyType TOPOLOGY_TYPE = TopologyType.FINLANDIA; // Topologia escolhida
		final RoutingAlgorithmType ROUTING_ALGORITHM_TYPE = RoutingAlgorithmType.ALTERNATIVE; // Algoritmo de roteamente escolhida

		final int KYEN = 30;
		final boolean isKYEN = false; // True testa a alocação em todas as K rotas, false só ordena e utiliza a primeira delas
	
		final MetricMethodType fuzzyLogicType = MetricMethodType.PSR_METRIC;

		//double[] coefficientsMetric = {-0.0317384946595442,-0.39308554692825304,-0.3058208896150939,-0.06009584912110333,0.698109163561386,-0.3361904946613041,-0.06292627241242654,0.5548767212265256,-0.611000395689437,1.0,0.12413102230807807,0.9198481893861266,0.10640709963023998,0.007780220601598498,-0.12787550010030724,-0.35143729923613476,0.24776548236053753,0.19009535102397468,0.9804671744507242,0.5731231329930202,0.2870549313312988,0.0,-0.7911221723471716,-0.3902356782415606,0.5241476275767839,0.033124936429372975,-0.21342032735364463}; // PSR (Ocupação + Hops x Banda + Rota interferente) | Treino a 400 Erlangs | K = 3
		//double[] coefficientsMetric = {-0.21601713329099453,0.08291569078595538,-0.057720983944591686,0.6499540885331926,-0.5698220251163655,0.24012706358024294,-1.0,0.4808387103618502,1.0,-0.31858621860363784,-0.5023616199336771,-0.5372809573772185,-0.09634997116181593,-0.7083148364442822,0.7305082581122344,0.6477601310955727,-0.8536537362534622,-0.2619521804708661,0.5492612225063196,-1.0,-0.49363784985885767,0.08622859951785253,-0.10092684236335997,-0.3433063536229435,0.5893962488429738,0.1811745964491535,0.8323562529129838}; // PSR (Ocupação + Hops x Banda) | Treino a 400 Erlangs | K = 30
		//double[] coefficientsMetric = {-1.0,0.5205024981960049,-0.16153685087161684,0.8906685774999481,0.16912863842204084,0.3556997759774557,1.0,0.5309802316087114,-0.24693196085036512}; // PSR (Ocupação, IRoutes) | Treino a 400 Erlangs | K = 30
		//double[] coefficientsMetric = {0.17455599252789844,-0.21602145655010419,0.8560023554084597,-0.5342066292311775,-0.7049076135661811,0.31792588878755434,0.5984522702098226,0.8768038159608974,-0.8206206269221603}; // PSR (Ocupação, IRoutes) | Treino a 440 Erlangs | K = 30 | lOCAL BEST
		//double[] coefficientsMetric = {-0.4628697104568229,0.31860448207149844,0.3402571676516645,0.05985461859496077,0.5867787701309097,-0.20021991462786898,0.43927550947499805,0.66712449679727,0.029161090413778224}; // PSR (Ocupação, Hops x Banda) | Treino a 380 1 Erlangs | K = 30 | lOCAL BEST
		//double[] coefficientsMetric = {0.03172240424782599,-0.07895468350960595,0.3907186603903695,0.43586201352502874,0.9934532118174826,0.795025960549425,0.2543391244904636,-0.01951896051072244,-0.01134470981614119}; // PSR (Ocupação, Hops x Banda) | Treino a 380 2 Erlangs | K = 30 | lOCAL BEST

		// ***** Depois de treinado o coeficiente deve ser colocado na linha abaixo *** DANILO
		//double[] coefficientsMetric = null; // Novos coeficientes
		//double[] coefficientsMetric = {1.0,-0.2009969505502096,1.0,-0.2567625263030997,1.0,-0.4145040473050373,1.0,1.0,-1.0}; // Ocupação, hops x Banda
		//double[] coefficientsMetric = {-0.9831553617417035,-0.9804714553747134,0.8616806807536697,1.0,0.9855427666589702,0.9831509411515779,1.0,-0.9831523955039938,-0.9988673592759575}; // Ocupação, IRotas
		//double[] coefficientsMetric = {-1.0,0.843032977841426,1.0,0.39142351018362304,1.0,-0.2087841635604233,1.0,1.0,0.8924487254582305}; // Ocupação, hops x Banda
		//double[] coefficientsMetric = {-1.0,-0.5955527332716818,0.8037864944870329,0.2609026920980665,-1.0,-0.96674343109799,0.7930910401231256,-0.31520061124388193,1.0,0.6560766114367439,0.9156272053511233,-0.03892058885454207,-0.3913421866935217,1.0,-0.9833941685767187,0.5783678815626098,1.0,-0.7667896561875371,-0.20738357461056445,0.15624303549240134,0.8992007939896669,1.0,-1.0,-1.0,-0.3185029368405188,-1.0,-0.26686505461794485}; // Ocupação, hops x Banda, 0
		double[] coefficientsMetric = {0.22885420829958208,-1.0,0.17234911173405065,1.0,-0.9656927067813067,-0.78553512740841,1.0,-0.15907839084112674,-0.9481149501246433,-0.559116852040598,0.7419079373402878,0.995356902694456,-0.6527107603589984,0.15853427538648346,0.513369914715046,0.34819128503369967,-0.2662912010741523,0.5357667069026263,0.8787235052629222,0.5053944824229946,-0.3419344933967521,0.9788111057700393,1.0,0.6387770965482881,0.6574473390034087,-0.11585041173601479,-0.8813261708732077}; // Ocupação, hops x Banda, 0
		

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
		Debug debugClass = new Debug(false, folderClass, isKYEN);
		// Para criar um arquivo com as requisições pode ser modificado o para true o primeiro parâmetro da linha acima e comentando as linhas de debugClass abaixo.
		// Para ler o txt de um arquivo, modifique o nome da pasta abaixo.

		//debugClass.setFolderToReadReqs("12-05-21_21-34-03_FINLANDIA_ALTERNATIVO_DANILO_OK"); // 350 Erlangs 320 slots

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
