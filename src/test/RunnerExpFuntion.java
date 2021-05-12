package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
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
public class RunnerExpFuntion {

	public static final Random rand = new Random();
	public static void main(final String[] args) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException, InvalidListOfCoefficientsException, IOException, InvalidFiberLengthException {		

		double networkLoad = 3.4;
		final int NUMBER_REQUEST_MAX = 100000; 	// Números da rede

		final TopologyType TOPOLOGY_TYPE = TopologyType.FINLANDIA; // Topologia escolhida
		final RoutingAlgorithmType ROUTING_ALGORITHM_TYPE = RoutingAlgorithmType.FUZZY; // Algoritmo de roteamente escolhida

		// Cria uma pasta para armazenar os resultados da simulação
		final CreateFolder folderClass = new CreateFolder("Analise", "Exp_function_Original_340");

		final long geralInitTime = System.currentTimeMillis();
		
		Function function = new Function();

		System.out.println();
		System.out.println("Carga: "+ Math.ceil(networkLoad * 100.0));

		folderClass.writeFile("timeAndDuration.csv", "id,networkLoad,generalTimeOriginal,decayTimeOriginal,durationOriginal,generalTimeRaul,IAT,timeDesconect,durationRaul");
		double timeGeneral = 0.0;
		double timeGeneralRaul = 0.0;
		
		final double MU = 1;
		for(int i=0; i <NUMBER_REQUEST_MAX; i++){
			
			timeGeneral += exponencialOriginal(networkLoad);
			timeGeneralRaul += exponencialRaulSim(networkLoad * 100);
			
			double decayTime = timeGeneral + exponencialOriginal(0.01);
			double duration = decayTime - timeGeneral;
			
			double expLoadValue = exponencialRaulSim(networkLoad * 100);
			double IAT = timeGeneralRaul + expLoadValue;

			double expMUValue = exponencialRaulSim(MU);
			double timeDesconect = timeGeneralRaul + expMUValue;
			

			folderClass.writeFile("timeAndDuration.csv", String.format("%d,%f,%f,%f,%f,%f,%f,%f,%f",i, networkLoad, timeGeneral, decayTime, duration, timeGeneralRaul, IAT, timeDesconect, expMUValue));

		}		

		final long geralEndTime = System.currentTimeMillis();
		
		folderClass.writeDone(((double)(geralEndTime - geralInitTime))/1000);

		System.out.println(folderClass.getFolderName());
		
	}

	// public static simulationReq(){

	// }

	public static double exponencialOriginal(final double meanRate){		
		final double random = Math.random();
		return (-1/meanRate)*(Math.log10(random)/Math.log10(Math.E)); 
	}

	public static double exponencialRaulSim(final double networkLoad){		
		return (double)(- Math.log( 1 -  uniform(0.0, 1.0)) / networkLoad);
	}

	public static double uniform(final double xMin, final double xMax){

		// double va = rand.nextInt(Integer.MAX_VALUE) / Integer.MAX_VALUE;
		// va += va / Integer.MAX_VALUE;
		// va += va / Integer.MAX_VALUE;

		// while ((va <= 0) || (va >= 1 - Math.pow(Integer.MAX_VALUE, -3))){
		// 	va = rand.nextInt(Integer.MAX_VALUE) / Integer.MAX_VALUE;
		// 	va += va / Integer.MAX_VALUE;
		// 	va += va / Integer.MAX_VALUE;
		// }

		double va = rand.nextDouble();

		return (double)(xMin + va * (xMax - xMin));
		
	// 	int RAND_MAX = Integer.MAX_VALUE; //32767;

	// 	Math.random.

		
	// 	va = random.randint(0, RAND_MAX) / RAND_MAX
	// 	va += va / RAND_MAX
	// 	va += va / RAND_MAX

    // while ((va <= 0) or (va >= 1.0 - (RAND_MAX ** (-3)))):
    //     va = random.randint(0, RAND_MAX) / RAND_MAX
    //     va += va / RAND_MAX
    //     va += va / RAND_MAX
    
    // if ((type(x_min) == type(1)) and (type(x_max) == type(1))):
    //     return math.floor(x_min + va * (x_max - x_min))
    // else:
    //     return (x_min + va * (x_max - x_min))
	}

}
