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
import network_topologies.NetworkTopology;
import network_topologies.NsfnetDimention;
import network_topologies.PacificBellDimention;
import psoMTHS.GenericParticle;
import psoMTHS.GenericSwarm;
import psoMTHS.Particle;
import psoMTHS.Swarm;
import psoMTHS.SwarmSimple;
import types.MetricMethodType;
import types.PSOType;
import types.ParticlePSOType;
import types.RoutingAlgorithmType;
import types.TopologyType;
import utility.Debug;
import report_save.CreateFolder;
import routing.Routing;
import routing.RoutingAlgorithmSolution;
import simulation.IRMLSASimulation;
import simulation.RMLSASimulation;


public class RunnerPSR {

	public static void main(final String[] args) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException, InvalidListOfCoefficientsException, IOException, InvalidFiberLengthException, Exception {
		
        // *** Parâmetros da simulação ***
		final int NETWORKLOAD = 350; 			// Carga usada para simular a rede
		final int NUMBER_OF_SIMULATIONS = 1; 	// Número de simulação para cada carga. É tirada uma média
		final int NUMBER_REQUEST_MAX = 100000;  // Números da rede
		final int KYEN = 30;
        final boolean isKYEN = true; // True testa a alocação em todas as K rotas, false só ordena e utiliza a primeira delas

        final int termN = 3;                    // Número de termos no somatório

		final TopologyType TOPOLOGY_TYPE = TopologyType.FINLANDIA; // Topologia escolhida
		final RoutingAlgorithmType ROUTING_ALGORITHM_TYPE = RoutingAlgorithmType.ALTERNATIVE; // Algoritmo de roteamente escolhida
		final MetricMethodType fuzzyLogicType = MetricMethodType.PSR_METRIC;

        //PSO
        final PSOType PSO_TYPE = PSOType.LOCAL_BEST;    // Regra de comunicação entre as particulas
        final int PSO_NUMBER_OF_PARTICLES =  30;         // Número de particulas do PSO
	    final int PSO_NUMBER_OF_INTERATIONS = 200;        // Número de interações
	    final boolean PSO_SIDESTEP = false;             //
        final ParticlePSOType PSO_PARTICLE_TYPE = ParticlePSOType.PSR_METRIC;
        final double[] PSO_COEFF_SEED = {};

		final long geralInitTime = System.currentTimeMillis();

		// Cria uma pasta para armazenar os resultados da simulação
		final CreateFolder folderClass = new CreateFolder(TOPOLOGY_TYPE.getDescription(), ROUTING_ALGORITHM_TYPE.getName() + "_PSO");
		
        final double[ ] coefficientsMetric = null;

        final double RULES_COEFFS[][] = new double[1][1];

        // Armazena os parâmetetros usados na simulação
		//folderClass.writeParametersPSO(TOPOLOGY_TYPE, ROUTING_ALGORITHM_TYPE, NETWORKLOAD, NUMBER_OF_SIMULATIONS, 1, (short)1, NUMBER_REQUEST_MAX, KYEN, fuzzyLogicType, coefficientsMetric, PSO_TYPE, PSO_NUMBER_OF_PARTICLES, PSO_NUMBER_OF_INTERATIONS, PSO_SIDESTEP, PSO_PARTICLE_TYPE, PSO_COEFF_SEED);

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

        final IRMLSASimulation simRMLSAInstance = RMLSASimulation.getRMLSASimulationInstance();

		System.out.println("Criando todas as rotas para os pares origem destino!");
		Routing routingInstance = new Routing(network, listOfNodes, ROUTING_ALGORITHM_TYPE, KYEN);
		ArrayList<List<RoutingAlgorithmSolution>> allRoutes = routingInstance.getAllRoutes();
        routingInstance.findAllModulationsByRoute();
		routingInstance.generateConflictList();
		routingInstance.OrderConflictRoutes();

		System.out.println("Inicializando as partículas.......%n");

        //final double[] objetive = {0.5, -0.5, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, -0.2}; // {0.0,0.0,0.0};
		
        // Como queremos encontrar o menor fitness, iniciamos todas as partículas com o maior valor possível
        GenericSwarm swarmPSO = new GenericSwarm(PSO_NUMBER_OF_PARTICLES, Double.MAX_VALUE, termN * termN * termN, PSO_TYPE, -1, 1);
        //GenericSwarm swarmPSO = new GenericSwarm(PSO_NUMBER_OF_PARTICLES, Double.MAX_VALUE, objetive.length, PSO_TYPE, -1, 1);
        // Basta trocar 'termN * termN' pelo número de termos que a partícula do PSO deve ter

        // Armazena os melhores coeficientes para cada interação
		double[][] bestPSO = new double[PSO_NUMBER_OF_INTERATIONS][termN * termN];

		long psoSimulationInicialTime = System.currentTimeMillis();	

        // Cria a estrutura para armazenar as informações de debug
		Debug debugClass = new Debug(false, folderClass, isKYEN);
        //debugClass.setFolderToReadReqs("05-05-21_14-15-36_FINLANDIA_ALTERNATIVO_YEN_OK");      // 360 Erlangs
        //debugClass.setFolderToReadReqs("05-05-21_17-56-11_FINLANDIA_ALTERNATIVO_SLOT_APERTURE_OK"); // 380 Erlangs
		//debugClass.setFolderToReadReqs("05-05-21_17-36-26_FINLANDIA_ALTERNATIVO_PSR_METRIC_OK"); // 400 Erlangs
		//debugClass.setFolderToReadReqs("08-05-21_11-16-28_FINLANDIA_ALTERNATIVO_PSR_METRIC_OK"); // 440 Erlangs
		//debugClass.setFolderToReadReqs("09-05-21_10-01-58_NSFNET_14_ALTERNATIVO_YEN_OK"); // 440 Erlangs Nsftnet

        debugClass.setFolderToReadReqs("12-05-21_21-34-03_FINLANDIA_ALTERNATIVO_DANILO_OK"); // 350 Erlangs 320 slots
        //debugClass.setFolderToReadReqs("14-05-21_19-34-51_FINLANDIA_ALTERNATIVO_PSR_METRIC_OK"); // 400 Erlangs 320 slots

        // Percorre todas as interações
		for(int i=0;i<PSO_NUMBER_OF_INTERATIONS;i++){
            
            long psoIterationInicialTime = System.currentTimeMillis();

            double minFitnessCurrent = Double.MAX_VALUE;
            double maxFitnessCurrent = 0.0;


            int particleID = 0;
            for (GenericParticle particle: swarmPSO.getAllParticles()){
                
                double[] coefficientsParticle = particle.getPosition();

                long simParticleTimeInit = System.currentTimeMillis();

                double particleFitness = simRMLSAInstance.simulation(network, listOfNodes, NUMBER_REQUEST_MAX, ROUTING_ALGORITHM_TYPE, NETWORKLOAD, allRoutes, coefficientsParticle, fuzzyLogicType, KYEN, debugClass);

                // double particleFitnessAux = 0;
                // for (int p = 0; p < objetive.length; p++){
                //     particleFitnessAux += Math.pow(objetive[p] - coefficientsParticle[p], 2);
                // }

                // double particleFitness = Math.sqrt(particleFitnessAux);  // Math.hypot(objetive[0] - coefficientsParticle[0], objetive[1] - coefficientsParticle[1]);

                NetworkTopologyInstance.resetNetworkAdjacencyMatrix();   
                
                long simParticleTimeEnd = System.currentTimeMillis();

                System.out.println(String.format("Partícula %d | Fitness %f | Tempo %f%n", particleID, particleFitness, (double)(simParticleTimeEnd - simParticleTimeInit)/1000));
                //System.out.println("Coeffs:");
                //System.out.println(coefficientsParticle.toString());


                particle.setFitness(particleFitness);

                if (particle.getpBestFitness() > particleFitness){
                    particle.setpBestFitness(particleFitness);
                    particle.setpBestPosition(coefficientsParticle);

                }

                //Atualiza a posição global caso esteja vazia
                if (swarmPSO.getgBestFitness() > particleFitness){
                    swarmPSO.setgBestFitness(particleFitness);
                    swarmPSO.setgBestPosition(coefficientsParticle);
                }

                if (particleFitness > maxFitnessCurrent){
                    maxFitnessCurrent = particleFitness;
                }

                if (particleFitness < minFitnessCurrent){
                    minFitnessCurrent = particleFitness;
                }

                particleID++;
            }

		    long psoIterationFinalTime = System.currentTimeMillis();
		    
		    double bestFitness = swarmPSO.getgBestFitness();
		    double[] bestSolution = swarmPSO.getgBestPosition();
			
			System.out.println("Iteration: "  + i + "| Melhor fitness: " + bestFitness);
			System.out.println("Melhor coeficientes:");
			//System.out.println(bestSolution.toString());
			folderClass.writeInResults(String.format("%d,%d,%f,%f",i, NETWORKLOAD, bestFitness, (double)(psoIterationFinalTime - psoIterationInicialTime)/1000));
			
			//String b0 = new String();
			String b0 = Integer.toString(i)+",";
			for(int j = 0 ;j < bestSolution.length; j++){
				System.out.print(bestSolution[j] + " ");
				b0 += "" + bestSolution[j] + ",";
			}
            b0 += "" + bestSolution;
            System.out.println("");
			//folderClass.writeFile("PSO_results.txt", "iteration: "+i+"%n");
			folderClass.writeFile("PSO_results.csv", b0);
			System.out.println();	

            bestPSO[i] = bestSolution;

            folderClass.writeFile("PSOMinMax.csv", String.format("%d,%f,%f",i, minFitnessCurrent, maxFitnessCurrent));

            if (PSO_TYPE == PSOType.GLOBAL_BEST){
                swarmPSO.updateVelocityGlobal();
            } if (PSO_TYPE == PSOType.LOCAL_BEST){
                swarmPSO.updateVelocityLocal();
            } else {
                assert false;
            }
            assert false;
            swarmPSO.updatePosition();
		}
		
		for (double[] coeffs: bestPSO){
			System.out.print(coeffs.toString());
		}

		long psoSimulationFinalTime = System.currentTimeMillis();
		System.out.println("Tempo de simulação: "+(psoSimulationFinalTime-psoSimulationInicialTime)/100);

		final long geralEndTime = System.currentTimeMillis();
		
		folderClass.writeDone(((double)(geralEndTime - geralInitTime))/1000);
	}	
}