package test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.xml.crypto.Data;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import exceptions.InvalidFiberLengthException;
import exceptions.InvalidListOfCoefficientsException;
import exceptions.InvalidNodeIdException;
import exceptions.InvalidNumberOfFrequencySlotsException;
import exceptions.InvalidRoutesException;
import network_topologies.NsfnetNetwork;
import pso.Swarm;
import simulation.IRMLSASimulation;
import simulation.RMLSASimulation;
import types.RoutingAlgorithmType;


public class EvaluateRMLSAAlgorithm_PSO {

	public static void main(final String[] args) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException, InvalidListOfCoefficientsException, IOException, InvalidFiberLengthException {
		
		final int numberOfParticles = 20;
		int neighborhood = 0;  //Lbest 0 e GBest 1
		boolean sidestep = false;
		double networkLoad = 2.60;
		
		FileWriter file = new FileWriter("D:\\Acadêmico\\Pesquisa\\Artigo SBrt\\results\\simulationPSOPB.txt");
		FileWriter detailedFile = new FileWriter("D:\\Acadêmico\\Pesquisa\\Artigo SBrt\\results\\detailedSimulationPSOPB.txt");
		PrintWriter saveFile = new PrintWriter(file);
		PrintWriter saveFile2 = new PrintWriter(detailedFile);
		
		NsfnetNetwork nsfnetNetworkInstance = NsfnetNetwork.getNSFNETNetworkInstance();
		OpticalLink[][] network = nsfnetNetworkInstance.getNetworkAdjacencyMatrix();
		List<OpticalSwitch> listOfNodes = nsfnetNetworkInstance.getListOfNodes();	
		int numberOfCalls = 10000;
		
		System.out.println("Inicializando as partículas.......");
		
		final int numberOfIterations = 50;
		
		System.out.println("Inicializando o processo de otimização.......");
		
		Swarm pso = new Swarm(neighborhood,numberOfParticles,sidestep, network, listOfNodes, numberOfCalls, RoutingAlgorithmType.FUZZY, networkLoad, numberOfIterations);
		
		long psoSimulationInicialTime = System.currentTimeMillis();	
		for(int i=0;i<numberOfIterations;i++){
			
			long psoIterationInicialTime = System.currentTimeMillis();	
			pso.develop(i);
			long psoIterationFinalTime = System.currentTimeMillis();
			pso.resetNetwork();
			double fit = pso.getHigherFitness();
			List<Double> vfit = pso.getHigherSolution();
			
			
			System.out.println("iteration: "+i+" melhor fit: "+1/fit);
			
//			saveFile.printf(i+"\t"+1/fit+"\t"+(psoIterationFinalTime-psoIterationInicialTime)/1000+"%n");
			
			String b0 = new String();
			for(int j=0;j<vfit.size();j++){
				System.out.print(vfit.get(j)+" ");
				b0+=""+vfit.get(j)+"\t";
			}
			b0+="\n";
			saveFile2.printf("iteration: "+i+"%n");
			saveFile2.printf(b0);
			System.out.println();
					
			
		}
		
		long psoSimulationFinalTime = System.currentTimeMillis();
		System.out.println("Tempo de simulação: "+(psoSimulationFinalTime-psoSimulationInicialTime)/100);
		System.out.println("checando se o resultado está ok--------------------------------------------------");
		
		
		
//		double load = 2.00;
//		int count = 0;
//		List<Double> vfit = pso.getHigherSolution();
//			
//		while(count<5){
//				
//			IRMLSASimulation simulationRSAInstance = RMLSASimulation.getRMLSASimulationInstance();						
//				
//			
//			load += 0.1; 
//			System.out.println();
//			System.out.println("Simulation: "+ count + " carga: "+(int)(load*100));
//							
//			long tempoInicial = System.currentTimeMillis();			
//			double solution = simulationRSAInstance.simulation(network, listOfNodes, 100000, RoutingAlgorithmType.PSR, load, vfit);
//			long tempoFinal = System.currentTimeMillis();
//			double pb = solution;
//			System.out.println("tempo em segundos da simulation: "+ (tempoFinal - tempoInicial)/1000);
//			System.out.println("PB: "+pb);
//				
//			nsfnetNetworkInstance.resetNetworkAdjacencyMatrix();
//				
//			count++;
//				
//		}			
//		
//		
		saveFile.close();
		saveFile2.close();
		detailedFile.close();
	
	}	

}
