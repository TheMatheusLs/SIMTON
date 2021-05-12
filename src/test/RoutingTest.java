package test;

import java.util.ArrayList;
import java.util.List;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import call_request.CallRequest;
import exceptions.InvalidFiberLengthException;
import exceptions.InvalidNodeIdException;
import exceptions.InvalidNumberOfFrequencySlotsException;
import exceptions.InvalidRoutesException;
import network_topologies.NsfnetNetwork;
import parameters.SimulationParameters;
import routing.IAlternativeRoutingAlgorithm;
import routing.RoutingAlgorithmSolution;
import routing.YenAlgorithm;
import types.CallRequestType;

public class RoutingTest {

public static void main(String[] args) throws InvalidNodeIdException, InvalidFiberLengthException, InvalidNumberOfFrequencySlotsException, InvalidRoutesException {		
		
		/**
		 * Teste dos algoritmos de roteamento.
		 * Tenta encontrar rotas para todos os pares de nós da rede.
		 */			
		final NsfnetNetwork nsfnetInstance = NsfnetNetwork.getNSFNETNetworkInstance();
		final OpticalLink[][] network = nsfnetInstance.getNetworkAdjacencyMatrix();
		final List<OpticalSwitch> listOfNodes = nsfnetInstance.getListOfNodes();
		final SimulationParameters parameters = new SimulationParameters();
		final double coefficients[] = {1.0,0.0,-0.545853,0.97245,0.86421,0.70881,0.766142,0.762792,-0.585103,-0.77974};
		List<Double> listOfCoeff = new ArrayList<Double>();
		for(int c=0;c<coefficients.length;c++){
			listOfCoeff.add(coefficients[c]);
		}
		
		int[] vetorNRoutes = {0,0,0,0,0,0,0};
		int numberOfcalls = 0;
		double totaltime = 0.0;
		
		for(int x=0;x<listOfNodes.size();x++){
			for(int y=0;y<listOfNodes.size();y++){
				
				if(x!=y){
					numberOfcalls++;
					CallRequest call = new CallRequest(0,parameters.getBitRate(), CallRequestType.BIDIRECTIONAL);
					call.setSourceId(x);
					call.setDestinationId(y);
					call.setBitRate(40);
					
					//SurvivabilityCall call = new SurvivabilityCall(0,P.getBitRate(),ProtectionType.DEDICATED, CallType.UNIDIRECTIONAL);
					//call.setSourceId(x);
					//call.setDestinationId(y);
					
					System.out.println("Source: "+x+" Destination: "+y);
					
					//RoutingAlgorithmSolution solution = new RoutingAlgorithmSolution();
					List<RoutingAlgorithmSolution> solutions = new ArrayList<RoutingAlgorithmSolution>();
					
					//IRoutingAlgorithm shortestPathInstance = ShortestPath.getShortestPathInstance();
					//IRoutingAlgorithm SCCSP = SCCShortestPath.getSCCSPInstance();
					//IEvolucionaryRoutingAlgorithm distanceInstance = PSR.getPSRInstance();
					IAlternativeRoutingAlgorithm distanceInstance = YenAlgorithm.getYenInstance();
					//IMultiDisjointPathsAlgorithm distanceInstance = NStepsDijkstraAlgorithm.getDistanceInstance();
					//IMultiDisjointPathsAlgorithm distanceInstance = KPenalty.getDistanceInstance();
					//IMultiDisjointPathsAlgorithm distanceInstance = LeastSharedOfLinksRoutingAlgorithm.getDistanceInstance();
					long tempoInicial = System.currentTimeMillis();
					//solution = SCCSP.findRoute(network, call, listOfNodes);
					//solution = distanceInstance.findRoute(network, call, listOfNodes, listOfCoefficients);
					solutions = distanceInstance.findRoutes(network, call, listOfNodes);
					long tempoFinal = System.currentTimeMillis();
					totaltime += (double)(tempoFinal - tempoInicial)/1000;
					System.out.print("Call Request Simulation time: "+ (double)(tempoFinal - tempoInicial)/1000);
					System.out.println();
					
					//int n = solutions.size();					
					//vetorNRoutes[n]++;
					
					//Única solução.
					/*
					if(!solution.getUpLink().isEmpty()){
						System.out.print(""+solution.getUpLink().get(0).getSource());
						for(int s=0;s<solution.getUpLink().size();s++){
							System.out.print(" "+solution.getUpLink().get(s).getDestination());
						}
						System.out.println();
						if(call.getCallRequestType().getCode()==CallRequestType.BIDIRECTIONAL.getCode()){
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
					
					*/
					//Múltiplas soluções.
					
					for(int i=0;i<solutions.size();i++){
						
						if(!solutions.get(i).getUpLink().isEmpty()){
							System.out.print(""+solutions.get(i).getUpLink().get(0).getSource());
							for(int s=0;s<solutions.get(i).getUpLink().size();s++){
								System.out.print(" "+solutions.get(i).getUpLink().get(s).getDestination());
							}
							System.out.println();
							if(call.getCallRequestType().getCode()==CallRequestType.BIDIRECTIONAL.getCode()){
								System.out.print(""+solutions.get(i).getDownLink().get(0).getSource());
								for(int s=0;s<solutions.get(i).getDownLink().size();s++){
									System.out.print(" "+solutions.get(i).getDownLink().get(s).getDestination());
								}
								System.out.println();
							}else{
								if(solutions.get(i).getDownLink().isEmpty()){
									System.out.println("Não tem rota de downlink!");
								}
								
							}							
						}						
					}					
					
					
					System.out.println();			
					/*
					int requiredNumberOfSlots = 9;					
					List<Integer> slots = new ArrayList<Integer>();
					ISpectrumAssignmentAlgorithm ffInstance = FirstFitAlgorithm.getFFInstance();					
					slots = ffInstance.findFrequencySlots(P.getNumberOfSlots(), requiredNumberOfSlots, solution.getUpLink(), solution.getDownLink());
					
					System.out.print("Slots: ");
					for(int s=0;s<slots.size();s++){
						System.out.print(""+slots.get(s)+" ");
					}
					*/
					System.out.println();
				}			
				
			}
			
		}
		System.out.println();
		System.out.println("Numero de chamadas geradas: "+numberOfcalls);
		System.out.println("Tempo: "+totaltime);
		System.out.println();
		for(int i=0;i<vetorNRoutes.length;i++){			
			System.out.println("solução com "+i+" rotas disjuntas: "+vetorNRoutes[i]);
		}
		
		
	}

}
