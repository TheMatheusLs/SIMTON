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
import parameters.SimulationParameters;
import report_save.CreateFolder;
import routing.Routing;
import routing.RoutingAlgorithmSolution;
import simulation.IRMLSASimulation;
import simulation.RMLSASimulation;
import types.RoutingAlgorithmType;
import types.TopologyType;
import types.FuzzyMetricMethodType;
import utility.Function;
/**
 * Descreve o algoritmo para avaliar os algoritmos de RMLSA.
 * @author André adptado por Matheus
 */
public class RunnerAlphaBetaGammaFuzzy {

	public static void main(final String[] args) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException, InvalidListOfCoefficientsException, IOException, InvalidFiberLengthException {		
		
		// *** Parâmetros da simulação ***
		final int NETWORKLOAD = 460; 			//Carga inicial da rede
		final short NUMBER_OF_SIMULATIONS = 5; // Número de simulação para cada carga. É tirada uma média
		final int NUMBER_REQUEST_MAX = 100000; 	// Números da rede

		final TopologyType TOPOLOGY_TYPE = TopologyType.NSFNET14; // Topologia escolhida
		final RoutingAlgorithmType ROUTING_ALGORITHM_TYPE = RoutingAlgorithmType.FUZZY; // Algoritmo de roteamente escolhida

		final int KYEN = 5;
	
		final FuzzyMetricMethodType fuzzyLogicType = FuzzyMetricMethodType.ALPHA_BETA_GAMMA_ONE;

		int numRules = 27;
		final double RULES_COEFFS[][] = new double[numRules][4];
		
		for (int i = 0; i < numRules; i++) {
			double[] aux = {(Math.random()>0.7)? -Math.random()*3:Math.random()*3,(Math.random()>0.7)? -Math.random()*3:Math.random()*3,(Math.random()>0.7)? -Math.random()*3:Math.random()*3,(Math.random()>0.7)? -Math.random()*3:Math.random()*3};
			RULES_COEFFS[i] = aux;
		}

		// Cria uma pasta para armazenar os resultados da simulação
		final CreateFolder folderClass = new CreateFolder(TOPOLOGY_TYPE.getDescription(), ROUTING_ALGORITHM_TYPE.getName());

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
        }
		
		double[][] topologyDimention = NetworkDimentionInstance.getLength();

		final NetworkTopology NetworkTopologyInstance = new NetworkTopology(topologyDimention);

		OpticalLink[][] network = NetworkTopologyInstance.getNetworkAdjacencyMatrix();
        List<OpticalSwitch> listOfNodes = NetworkTopologyInstance.getListOfNodes();

		final IRMLSASimulation simRMLSAInstance = RMLSASimulation.getRMLSASimulationInstance();
		
		System.out.println("Criando todas as rotas para os pares origem destino!");
		Routing routingInstance = new Routing(network, listOfNodes, ROUTING_ALGORITHM_TYPE, KYEN);
		ArrayList<List<RoutingAlgorithmSolution>> allRoutes = routingInstance.getAllRoutes();

		Function function = new Function();

		Vector<Vector<Double>> simulationResults = new Vector<Vector<Double>>();
		Vector<Double> partialPb = new Vector<Double>();
		Vector<Double> simulationTime = new Vector<Double>();
		Vector<Double> partialThroughput = new Vector<Double>();

		SimulationParameters parameters = new SimulationParameters();

        final double alphaLimit = 1.0;
        final double betaLimit = 1.0;
        final double gammaLimit = 1.0;

        final double sizeStep = 0.2;

        final String fileAlphaBetaGamma = "alphaBetaGamma.csv";

        String alphaBetaGammaHeader = "networkLoad,alpha,beta,gamma,pbMean,time";

        folderClass.writeFile(fileAlphaBetaGamma, alphaBetaGammaHeader);

        // Alpha
        for (double a = 0; a <= alphaLimit;){
            // Beta
            for (double b = 0; b <= betaLimit;){
                // Gamma
                for (double g = 0; g <= gammaLimit;){

                    // Carrega alpha beta e gamma
                    double[] coefficientsMetric = {a, b, g};

                    
                    partialPb.clear();
                    simulationTime.clear();
                    partialThroughput.clear();
                    
                    System.out.println();
                    System.out.println("Carga: "+ (NETWORKLOAD / parameters.getMeanRateOfCallsDuration()));
                    System.out.println(String.format("Alpha: %f   Beta: %f  Gamma: %f", a, b , g));
                    
                    String logSimulationFilename = String.format("logSimulation_%d.csv",(int)(NETWORKLOAD / parameters.getMeanRateOfCallsDuration()));

                    for(int j=0;j<NUMBER_OF_SIMULATIONS;j++){ //number of simulations/network load					
			
                        System.out.println();
                        System.out.println("Simulation: "+ j);
                                            
                        final long tempoInicial = System.currentTimeMillis();
        
                        final double pb = simRMLSAInstance.simulation(network, listOfNodes, NUMBER_REQUEST_MAX, ROUTING_ALGORITHM_TYPE, NETWORKLOAD, allRoutes, coefficientsMetric, fuzzyLogicType, KYEN, RULES_COEFFS, folderClass, logSimulationFilename, j);
        
                        final long tempoFinal = System.currentTimeMillis();
                        partialPb.addElement(pb);
                        simulationTime.addElement(((double)(tempoFinal - tempoInicial))/1000);
        
                        System.out.print("PB: "+pb);
                        System.out.print(" Simulation time: "+ (double)(tempoFinal - tempoInicial)/1000);
                        System.out.println();
                        
                        NetworkTopologyInstance.resetNetworkAdjacencyMatrix();
                    }	
                    
                    double pbDesviation = function.getStandardDeviation(partialPb);
                    double thDesviation = function.getStandardDeviation(partialThroughput);
                    double timeDesviation = function.getStandardDeviation(simulationTime);
                    double pbErro = pbDesviation/Math.sqrt(partialPb.size());
                    double timeErro = timeDesviation/Math.sqrt(partialPb.size());
                    double thErro = thDesviation/Math.sqrt(partialThroughput.size());
                    double pbMargem = pbErro*1.96;

                    System.out.println("Mean: " + function.getMean(partialPb) + " Desviation: " + pbDesviation + " Erro: "+ pbErro + " Margem: " + pbMargem);

                    folderClass.writeInResults((NETWORKLOAD / parameters.getMeanRateOfCallsDuration()) + "," + function.getMean(partialPb) + "," + pbDesviation + "," + pbErro + "," + pbMargem + "," + function.getMean(simulationTime));

                    String alphaBetaGamma = String.format("%f,%f,%f,%f,%f,%f", (NETWORKLOAD / parameters.getMeanRateOfCallsDuration()), a, b, g, function.getMean(partialPb), function.getMean(simulationTime));

                    folderClass.writeFile(fileAlphaBetaGamma, alphaBetaGamma);
                    
                    simulationResults.addElement(partialPb);		

                    g += sizeStep;
                }
                b += sizeStep;
            }
            a += sizeStep;
        }

		final long geralEndTime = System.currentTimeMillis();
		
		folderClass.writeDone(((double)(geralEndTime - geralInitTime))/1000);

		System.out.println(folderClass.getFolderName());
		
	}
}

