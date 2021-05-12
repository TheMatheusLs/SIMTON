package test;

import java.util.List;
import java.util.Vector;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import exceptions.InvalidFiberLengthException;
import exceptions.InvalidNodeIdException;
import exceptions.InvalidNumberOfFrequencySlotsException;
import exceptions.InvalidRoutesException;
import network_topologies.NsfnetNetwork;

public class MultifiberRoutingTest {

	public static void main(String[] args) throws InvalidFiberLengthException, InvalidNodeIdException, InvalidNumberOfFrequencySlotsException, InvalidRoutesException {
					
		NsfnetNetwork nsfnetNetworkInstance = NsfnetNetwork.getNSFNETNetworkInstance();
		List<OpticalLink> network = nsfnetNetworkInstance.getOpticalLinkList();
		List<OpticalSwitch> listOfNodes = nsfnetNetworkInstance.getListOfNodes();
		int numberOfNodes = listOfNodes.size();
		Vector<Vector<Vector<OpticalLink>>> multifiberNetwork = new Vector<Vector<Vector<OpticalLink>>>();
		
		for(int i=0;i<numberOfNodes;i++){	//initialize dimension i.		
			Vector<Vector<OpticalLink>> t = new Vector<Vector<OpticalLink>>();			
			multifiberNetwork.addElement(t);		
			for(int j=0;j<numberOfNodes;j++){ //initialize dimension j.
				Vector<OpticalLink> u = new Vector<OpticalLink>();
				multifiberNetwork.get(i).addElement(u);
			}			
		}		
		
		for(OpticalLink l : network){
			int source = l.getSource();
			int destination = l.getDestination();
			multifiberNetwork.get(source).get(destination).addElement(l);			
		}
			
		System.out.println(multifiberNetwork.get(0).get(0).isEmpty());	
			
				
				/*
				if(x!=y){
					numberOfcalls++;
					CallRequest call = new CallRequest(0,P.getBitRate(), CallRequestType.BIDIRECTIONAL);
					call.setSourceId(x);
					call.setDestinationId(y);
					
					//SurvivabilityCall call = new SurvivabilityCall(0,P.getBitRate(),ProtectionType.DEDICATED, CallType.UNIDIRECTIONAL);
					//call.setSourceId(x);
					//call.setDestinationId(y);
					
					System.out.println("Source: "+x+" Destination: "+y);
					
					RoutingAlgorithmSolution solution = new RoutingAlgorithmSolution();
					//Vector<RoutingAlgorithmSolution> solutions = new Vector<RoutingAlgorithmSolution>();
					
					IRoutingAlgorithm shortestPathInstance = ShortestPath.getShortestPathInstance();					
					//IEvolucionaryRoutingAlgorithm distanceInstance = PSR.getPSRInstance();
					//IAlternativeRoutingAlgorithm distanceInstance = YenAlgorithm.getDistanceInstance();
					//IMultiDisjointPathsAlgorithm distanceInstance = NStepsDijkstraAlgorithm.getDistanceInstance();
					//IMultiDisjointPathsAlgorithm distanceInstance = KPenalty.getDistanceInstance();
					//IMultiDisjointPathsAlgorithm distanceInstance = LeastSharedOfLinksRoutingAlgorithm.getDistanceInstance();
					long tempoInicial = System.currentTimeMillis();
					solution = shortestPathInstance.findRoute(network, call, listOfNodes);
					//solution = distanceInstance.findRoute(network, call, listOfNodes, listOfCoefficients);
					//solutions = distanceInstance.findRoutes(network, call, listOfNodes);
					long tempoFinal = System.currentTimeMillis();
					totaltime += (double)(tempoFinal - tempoInicial)/1000;
					System.out.print("Call Request Simulation time: "+ (double)(tempoFinal - tempoInicial)/1000);
					System.out.println();
					
					//int n = solutions.size();					
					//vetorNRoutes[n]++;
					
					//for(int i=0;i<solutions.size();i++){
						
						if(!solution.getInBound().isEmpty()){
							System.out.print(""+solution.getInBound().get(0).getSource());
							for(int s=0;s<solution.getInBound().size();s++){
								System.out.print(" "+solution.getInBound().get(s).getDestination());
							}
							System.out.println();
							if(call.getCallRequestType().getCode()==CallRequestType.BIDIRECTIONAL.getCode()){
								System.out.print(""+solution.getOutBound().get(0).getSource());
								for(int s=0;s<solution.getOutBound().size();s++){
									System.out.print(" "+solution.getOutBound().get(s).getDestination());
								}
								System.out.println();
							}else{
								if(solution.getOutBound().isEmpty()){
									System.out.println("Não tem rota de downlink!");
								}
								
							}							
						}						
					//}					
					
					
					System.out.println();			
					
					int requiredNumberOfSlots = 10;					
					Vector<Integer> slots = new Vector<Integer>();
					ISpectrumAssignmentAlgorithm ffInstance = FirstFitAlgorithm.getFFInstance();					
					slots = ffInstance.findFrequencySlots(P.getNumberOfSlots(), requiredNumberOfSlots, solution);
					
					System.out.print("Slots: ");
					for(int s=0;s<slots.size();s++){
						System.out.print(""+slots.get(s)+" ");
					}
					
					System.out.println();
				}	*/		
				
		

	}

}
