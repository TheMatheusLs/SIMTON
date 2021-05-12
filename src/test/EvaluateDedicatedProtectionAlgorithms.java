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
import simulation.DedicatedProtectionSimulation;
import simulation.IDedicatedProtectionSimulation;
import types.DedicatedProtectionAlgorithmType;
import utility.Function;

public class EvaluateDedicatedProtectionAlgorithms {

	public static void main(String[] args) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException, InvalidListOfCoefficientsException, IOException, InvalidFiberLengthException {
		
		double[] networkLoad = {0.60};
		
				
		FileWriter file = new FileWriter("C:\\Users\\dwdmlab4\\Desktop\\simulationSuurY.txt");
		FileWriter detailedFile = new FileWriter("C:\\Users\\dwdmlab4\\Desktop\\detailedSimulationSuurY.txt");		
		PrintWriter saveFile = new PrintWriter(file);
		PrintWriter saveFile2 = new PrintWriter(detailedFile);
		
		
		IDedicatedProtectionSimulation dedicatedProtectionInstance = DedicatedProtectionSimulation.getDedicatedProtectionSimulationInstance();
		
		NsfnetNetwork nsfnetNetworkInstance = NsfnetNetwork.getNSFNETNetworkInstance();
		OpticalLink[][] network = nsfnetNetworkInstance.getNetworkAdjacencyMatrix();
		List<OpticalSwitch> listOfNodes = nsfnetNetworkInstance.getListOfNodes();		
		
		
		Function f = new Function();
		double coefficients[] = {0.32264456286829524, 0.31840071904430634, -0.4628611404911219, 0.4920861177124545, -0.2550459543313274, -0.34178587229478685, -0.35136220651289407, -0.3896466991110302, -0.5585773784078041, 1.0};
		Vector<Double> listOfCoefficients = new Vector<Double>();
		
		for(int c=0;c<coefficients.length;c++){
			listOfCoefficients.addElement(coefficients[c]);
		}
		
		Vector<Vector<Double>> simulationResults = new Vector<Vector<Double>>();
		Vector<Double> partialResults = new Vector<Double>();
		Vector<Double> simulationTime = new Vector<Double>();
		Vector<Double> partialThroughput = new Vector<Double>();
		
		int numberOfSimulations = 1;	
		
		for(int i=0;i<networkLoad.length;i++){ //For each network load
			
			partialResults.clear();
			simulationTime.clear();
			partialThroughput.clear();
			System.out.println();
			System.out.println("Carga: "+networkLoad[i]*100);
			saveFile2.printf("Network load: "+networkLoad[i]*100+"%n");
			
			for(int j=0;j<numberOfSimulations;j++){ //number of simulations/network load					
			
				System.out.println();
				System.out.print("Simulation: "+ j);
									
				double solution = dedicatedProtectionInstance.simulation(network, listOfNodes, 100000, DedicatedProtectionAlgorithmType.Suurballe, networkLoad[i], listOfCoefficients);
				partialResults.addElement(solution);
				
				System.out.println(" PB: "+ solution);
				
				nsfnetNetworkInstance.resetNetworkAdjacencyMatrix();			
			
			}				
			
			double pbDesviation = f.getStandardDeviation(partialResults);
			double thDesviation = f.getStandardDeviation(partialThroughput);
			double timeDesviation = f.getStandardDeviation(simulationTime);
			double pbErro = pbDesviation/Math.sqrt(partialResults.size());
			double thErro = thDesviation/Math.sqrt(partialThroughput.size());
			double timeErro = timeDesviation/Math.sqrt(partialResults.size());
			double pbMargem = pbErro*1.96;
			double timeMargem = timeErro*1.96;
			double thMargem = thErro*1.96;
			
			saveFile.printf("Network load: "+networkLoad[i]*100+"%n");
			saveFile.printf(f.getMean(partialResults)+ "\t"+pbMargem+"\t"+f.getMean(simulationTime)+"\t"+timeMargem+"\t"+f.getMean(partialThroughput)+"\t"+thMargem+"%n");
			
			System.out.println("Mean: "+f.getMean(partialResults)+ " Desviation: "+pbDesviation+" Erro: "+pbErro+" Margem: "+pbMargem);
			
			simulationResults.addElement(partialResults);		
			
		}	
		
		file.close();
		detailedFile.close();
		
		
	}

}
