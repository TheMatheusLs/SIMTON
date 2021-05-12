package test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Vector;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import exceptions.InvalidFiberLengthException;
import exceptions.InvalidListOfCoefficientsException;
import exceptions.InvalidNodeIdException;
import exceptions.InvalidNumberOfFrequencySlotsException;
import exceptions.InvalidRoutesException;
import network_topologies.NsfnetNetwork;
import simulation.IRMLSASimulation;
import simulation.RMLSASimulation;
import types.RoutingAlgorithmType;
import utility.Function;
/**
 * Descreve o algoritmo para avaliar os algoritmos de RMLSA.
 * @author Andr� 
 */
public class EvaluateRMLSAAlgorithms {
	/**
	 * Fluxo principal para executar as simula��es de RMLSA.
	 * @author Andr� 
	 */
	public static void main(final String[] args) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException, InvalidListOfCoefficientsException, IOException, InvalidFiberLengthException {		
		
		//double networkLoad = 1.6;
		double networkLoad = 2.8;
		double step = 0.2;
//		final FileWriter file = new FileWriter("C:\\Users\\dwdmlab4\\Desktop\\simulationCASP.txt");
//		final FileWriter detailedFile = new FileWriter("C:\\Users\\dwdmlab4\\Desktop\\detailedSimulationCASP.txt");
//		final PrintWriter saveFile = new PrintWriter(file);
//		final PrintWriter saveFile2 = new PrintWriter(detailedFile);
//		
		final IRMLSASimulation simRMLSAInstance = RMLSASimulation.getRMLSASimulationInstance();
		
		final NsfnetNetwork nsfnetNetworkInstance = NsfnetNetwork.getNSFNETNetworkInstance();
		final OpticalLink[][] network = nsfnetNetworkInstance.getNetworkAdjacencyMatrix();
		List<OpticalSwitch> listOfNodes = nsfnetNetworkInstance.getListOfNodes();
		
		Function function = new Function();
		
		double coefficients[] = {0.32264456286829524, 0.31840071904430634, -0.4628611404911219, 0.4920861177124545, -0.2550459543313274, -0.34178587229478685, -0.35136220651289407, -0.3896466991110302, -0.5585773784078041, 1.0};
		Vector<Double> listOfCoefficients = new Vector<Double>();
		
		for(int c=0;c<coefficients.length;c++){
			listOfCoefficients.addElement(coefficients[c]);
		}
		
		Vector<Vector<Double>> simulationResults = new Vector<Vector<Double>>();
		Vector<Double> partialPb = new Vector<Double>();
		Vector<Double> simulationTime = new Vector<Double>();
		Vector<Double> partialThroughput = new Vector<Double>();
		
		int numberOfSimulations = 10;				
		
		listOfCoefficients = null;

		for(int i=0;i<5;i++){ //For each network load
			
			partialPb.clear();
			simulationTime.clear();
			partialThroughput.clear();
			networkLoad += step;
			System.out.println();
			System.out.println("Carga: "+(int)(networkLoad*100.0));
//			saveFile2.printf("Network load: "+(int) networkLoad*100+"%n");		
			
			
			for(int j=0;j<numberOfSimulations;j++){ //number of simulations/network load					
			
				System.out.println();
				System.out.print("Simulation: "+ j);
									
				final long tempoInicial = System.currentTimeMillis();
				final double pb = simRMLSAInstance.simulation(network, listOfNodes, 100000, RoutingAlgorithmType.FUZZY, networkLoad, listOfCoefficients);
				final long tempoFinal = System.currentTimeMillis();
				partialPb.addElement(pb);
				simulationTime.addElement(((double)(tempoFinal - tempoInicial))/1000);
				System.out.print(" PB: "+pb);
				System.out.print(" Simulation time: "+ (double)(tempoFinal - tempoInicial)/1000);
				System.out.println();
				
				nsfnetNetworkInstance.resetNetworkAdjacencyMatrix();
				
//				saveFile2.printf(pb+"\t"+(double)(tempoFinal - tempoInicial)/1000+"%n");		
			}			
			
			double pbDesviation = function.getStandardDeviation(partialPb);
			double thDesviation = function.getStandardDeviation(partialThroughput);
			double timeDesviation = function.getStandardDeviation(simulationTime);
			double pbErro = pbDesviation/Math.sqrt(partialPb.size());
			double timeErro = timeDesviation/Math.sqrt(partialPb.size());
			double thErro = thDesviation/Math.sqrt(partialThroughput.size());
			double pbMargem = pbErro*1.96;
			double timeMargem = timeErro*1.96;
			double thMargem = thErro*1.96;
						
//			saveFile.printf("Network load: "+(int) networkLoad*100+"%n");
//			saveFile.printf(function.getMean(partialPb)+ "\t"+pbMargem+"\t"+function.getMean(simulationTime)+"\t"+timeMargem+"\t"+function.getMean(partialThroughput)+"\t"+thMargem+"%n");
			
			System.out.println("Mean: "+function.getMean(partialPb)+ " Desviation: "+pbDesviation+" Erro: "+pbErro+" Margem: "+pbMargem);
			
			simulationResults.addElement(partialPb);			
			
		}
		
//		file.close();
//		detailedFile.close();
	}
}
