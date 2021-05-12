package test;

import java.util.List;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import call_request.DPCallRequest;
import exceptions.InvalidFiberLengthException;
import exceptions.InvalidNodeIdException;
import network_topologies.NsfnetNetwork;
import parameters.SimulationParameters;
import survivability.DedicatedProtectionAlgorithmSolution;
import survivability.IDedicatedProtectionAlgorithm;
import survivability.SuurballeAlgorithm;
import types.CallRequestType;

public class DedicatedProtectionTest {

	public static void main(String[] args) throws InvalidNodeIdException, InvalidFiberLengthException {
		
		
		NsfnetNetwork nsfnetNetworkInstance = NsfnetNetwork.getNSFNETNetworkInstance();
		OpticalLink[][] network = nsfnetNetworkInstance.getNetworkAdjacencyMatrix();
		List<OpticalSwitch> listOfNodes = nsfnetNetworkInstance.getListOfNodes();
		//FourNodesNetwork fourNodesNetwork = new FourNodesNetwork();
		//Vector<Link> network = fourNodesNetwork.getNetwork();
		//Vector<Node> listOfNodes = fourNodesNetwork.getListOfNodes();
		final SimulationParameters parameters = new SimulationParameters();
		//IAlternativeDedicatedProtectionAlgorithm kPairsOfRoutesInstance = KPairsOfRoutes.getKPairsOfRoutesDistanceInstance();
		IDedicatedProtectionAlgorithm suurballeInstance = SuurballeAlgorithm.getSuurballeInstance();
		
		
		for(int l=0;l<listOfNodes.size();l++){
			for(int j=0;j<listOfNodes.size();j++){
				if(l!=j){
					
					int source = listOfNodes.get(l).getOpticalSwitchId();
					int destination = listOfNodes.get(j).getOpticalSwitchId();
					DedicatedProtectionAlgorithmSolution solution = new DedicatedProtectionAlgorithmSolution();
					DPCallRequest call = new DPCallRequest(0,parameters.getBitRate(), CallRequestType.BIDIRECTIONAL);
					call.setSourceId(l);
					call.setDestinationId(j);
					
					solution = suurballeInstance.findRoutes(network, call, listOfNodes);
					
					System.out.println("fonte e destino: "+source+" "+destination+"----------------------------");
					
					if(solution.getWorkingPath()!=null && !solution.getWorkingPath().getUpLink().isEmpty()){
						System.out.println("rota de trabalho");
						System.out.print("uplink: "+solution.getWorkingPath().getUpLink().get(0).getSource());
							
						for(int i=0;i<solution.getWorkingPath().getUpLink().size();i++){
							System.out.print(" "+solution.getWorkingPath().getUpLink().get(i).getDestination());
						}
						System.out.println();
						if(!solution.getWorkingPath().getDownLink().isEmpty()){
							System.out.print("Downlink: "+solution.getWorkingPath().getDownLink().get(0).getSource());
								
							for(int i=0;i<solution.getWorkingPath().getDownLink().size();i++){
								System.out.print(" "+solution.getWorkingPath().getDownLink().get(i).getDestination());
							}
							System.out.println();							
						}else{
							System.out.println("Não tem downlink");
						}
							
						System.out.print("Comprimento na rede:");
						for(int i=0;i<solution.getWorkingPath().getUpLink().size();i++){
							OpticalLink f = solution.getWorkingPath().getUpLink().get(i);								
							System.out.print(" "+network[f.getSource()][f.getDestination()].getLength());
						}
					}
					System.out.println();
						
					if(solution.getProtectionPath()!=null && !solution.getProtectionPath().getUpLink().isEmpty()){
						System.out.println("rota de proteção");
						System.out.print("uplink: "+solution.getProtectionPath().getUpLink().get(0).getSource());
							
						for(int i=0;i<solution.getProtectionPath().getUpLink().size();i++){
							System.out.print(" "+solution.getProtectionPath().getUpLink().get(i).getDestination());
						}
						System.out.println();
						if(!solution.getProtectionPath().getDownLink().isEmpty()){
							System.out.print("Downlink: "+solution.getProtectionPath().getDownLink().get(0).getSource());
								
							for(int i=0;i<solution.getProtectionPath().getDownLink().size();i++){
								System.out.print(" "+solution.getProtectionPath().getDownLink().get(i).getDestination());
							}
							System.out.println();
						}else{
							System.out.println("Não tem downlink");
						}
							
						System.out.print("Comprimento na rede:");
						for(int i=0;i<solution.getProtectionPath().getUpLink().size();i++){								
							OpticalLink f = solution.getProtectionPath().getUpLink().get(i);
							System.out.print(" "+network[f.getSource()][f.getDestination()].getLength());
						}						
					}
					System.out.println();		
				}
			}
		}
	}

}
