package survivability;

import java.util.ArrayList;
import java.util.List;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import call_request.CallRequest;
import call_request.DPCallRequest;
import exceptions.InvalidNodeIdException;
import parameters.SimulationParameters;
import routing.IRoutingAlgorithm;
import routing.RoutingAlgorithmSolution;
import routing.ShortestPath;
import types.CallRequestType;
import utility.Tuple;
/**
 * Descreve o algoritmo de prote��o dedicada Suurballe.
 * @author Andr� 
 */
public class SuurballeAlgorithm implements IDedicatedProtectionAlgorithm { // NOPMD by Andr� on 20/06/17 11:25
	/**
	 * Inst�ncia da Classe.
	 * @author Andr� 			
	 */	
	private static final SuurballeAlgorithm SUURB_INSTANCE = new SuurballeAlgorithm();
	/**
	 * M�todo para encontrar o par de rotas disjuntas baseado na dist�ncia f�sica.
	 * @author Andr� 			
	 */	
	public DedicatedProtectionAlgorithmSolution findRoutes(final OpticalLink[][] network, final DPCallRequest survivabilityCallRequest, final List<OpticalSwitch> listOfNodes)  // NOPMD by Andr� on 20/06/17 11:25
			throws InvalidNodeIdException {
		
		final int idSourceNode = survivabilityCallRequest.getSourceId();
		final int idDestNode = survivabilityCallRequest.getDestinationId();
		final List<Tuple> removedLinks = new ArrayList<Tuple>();
		final DedicatedProtectionAlgorithmSolution solution = new DedicatedProtectionAlgorithmSolution();
		final SimulationParameters parameters = new SimulationParameters();
		
		if(survivabilityCallRequest.getCallRequestType().getCode()==CallRequestType.BIDIRECTIONAL.getCode()){ // NOPMD by Andr� on 20/06/17 10:56
			
			removedLinks.clear();			
			RoutingAlgorithmSolution path1 = new RoutingAlgorithmSolution(); // NOPMD by Andr� on 20/06/17 10:56
			RoutingAlgorithmSolution path2 = new RoutingAlgorithmSolution(); // NOPMD by Andr� on 20/06/17 10:56
			final IRoutingAlgorithm spInstance = ShortestPath.getShortestPathInstance();
			final CallRequest callrequest = new CallRequest(-1,parameters.getBitRate(),survivabilityCallRequest.getCallRequestType()); // requisi��o para encontrar a rota P1.
			callrequest.setSourceId(idSourceNode);
			callrequest.setDestinationId(idDestNode);
			path1 = spInstance.findRoute(network, callrequest, listOfNodes); // NOPMD by Andr� on 20/06/17 10:57
			
			
			removePathInboundNetwork(removedLinks,path1,network);			
				
			path2 = spInstance.findRoute(network, callrequest, listOfNodes); // NOPMD by Andr� on 20/06/17 10:57
			
			restaurePathInNetwork(removedLinks,network);		
				
			final List<OpticalLink> linkToBeRemove = new ArrayList<OpticalLink>();				
			for(final OpticalLink i: path1.getUpLink()){
				for(final OpticalLink j: path2.getUpLink()){
					if(i.getSource()==j.getSource() && i.getDestination()==j.getDestination()){ // NOPMD by Andr� on 20/06/17 10:57
						linkToBeRemove.add(j);
					}
					if(i.getSource()==j.getDestination() && j.getSource()==i.getDestination()){ // NOPMD by Andr� on 20/06/17 10:57
						linkToBeRemove.add(j);
					}						
				}					
			}
				
			if(linkToBeRemove.isEmpty()){ //if there is not conflict. 
				
				removePathInNetwork(removedLinks,path1,network);				
				path2 = spInstance.findRoute(network, callrequest, listOfNodes); // NOPMD by Andr� on 20/06/17 10:57
					
			}else{
				
				removeConflitsInNetwork(removedLinks, linkToBeRemove,network);
				path1 = spInstance.findRoute(network, callrequest, listOfNodes); // NOPMD by Andr� on 20/06/17 11:18				
				removePathInNetwork(removedLinks,path1,network);
				path2 = spInstance.findRoute(network, callrequest, listOfNodes); // NOPMD by Andr� on 20/06/17 11:18					
			}
				
			solution.setWorkingPath(path1);
			solution.setProtectionPath(path2);
			
			restaurePathInNetwork(removedLinks,network);				
			
		}else{			
			removedLinks.clear();			
			RoutingAlgorithmSolution path1 = new RoutingAlgorithmSolution(); // NOPMD by Andr� on 20/06/17 11:18
			RoutingAlgorithmSolution path2 = new RoutingAlgorithmSolution(); // NOPMD by Andr� on 20/06/17 11:18
			final CallRequest callrequest = new CallRequest(-1,parameters.getBitRate(),survivabilityCallRequest.getCallRequestType()); // requisi��o para encontrar a rota P1.
			callrequest.setSourceId(idSourceNode);
			callrequest.setDestinationId(idDestNode);
			final IRoutingAlgorithm spInstance = ShortestPath.getShortestPathInstance();
			path1 = spInstance.findRoute(network, callrequest, listOfNodes); // NOPMD by Andr� on 20/06/17 11:18			
			
			
			removePathInboundNetwork(removedLinks,path1,network);
			
			path2 = spInstance.findRoute(network, callrequest, listOfNodes); // NOPMD by Andr� on 20/06/17 11:18
			
			restaurePathInNetwork(removedLinks,network);
				
			final List<OpticalLink> linkToBeRemove = new ArrayList<OpticalLink>();				
			for(final OpticalLink j: path2.getUpLink()){ // NOPMD by Andr� on 20/06/17 11:18
				for(final OpticalLink i: path1.getUpLink()){	 // NOPMD by Andr� on 20/06/17 11:18				
					if(i.getSource()==j.getSource() && i.getDestination()==j.getDestination()){ // NOPMD by Andr� on 20/06/17 11:18
						linkToBeRemove.add(i);
					}
					if(i.getSource()==j.getDestination() && j.getSource()==i.getDestination()){ // NOPMD by Andr� on 20/06/17 11:18
						linkToBeRemove.add(i);
					}						
				}					
			}
				
			if(linkToBeRemove.isEmpty()){ //if there is not conflict. 				
				removePathInboundNetwork(removedLinks,path1,network);				
				path2 = spInstance.findRoute(network, callrequest, listOfNodes); // NOPMD by Andr� on 20/06/17 11:18					
			}else{					
				removeConflitsInNetwork(removedLinks, linkToBeRemove,network);
				path1 = spInstance.findRoute(network, callrequest, listOfNodes); // NOPMD by Andr� on 20/06/17 11:18
				removePathInboundNetwork(removedLinks,path1,network);
				path2 = spInstance.findRoute(network, callrequest, listOfNodes); // NOPMD by Andr� on 20/06/17 11:18					
			}
				
			solution.setWorkingPath(path1);
			solution.setProtectionPath(path2);
			
			restaurePathInNetwork(removedLinks,network);		
		}			
		
		return solution;
	}
	/**
	 * M�todo para remover temporariamente conflitos na rede.
	 * @param removedLinks
	 * @param linkToBeRemove
	 * @param network
	 * @author Andr� 			
	 */	
	private void removeConflitsInNetwork(final List<Tuple> removedLinks, final List<OpticalLink> linkToBeRemove, final OpticalLink[][] network) { // NOPMD by Andr� on 20/06/17 11:18
		
		for(int i=0;i<linkToBeRemove.size();i++){	// remove the conflicts.					
			final OpticalLink opticalLink = linkToBeRemove.get(i);						
			final int sourceId = opticalLink.getSource(); // NOPMD by Andr� on 20/06/17 11:18
			final int destinationId = opticalLink.getDestination(); // NOPMD by Andr� on 20/06/17 11:18
			final int opticalLinkId = opticalLink.getOpticalLinkId(); // NOPMD by Andr� on 20/06/17 11:18
			final double opticalLinkLength = opticalLink.getLength(); // NOPMD by Andr� on 20/06/17 11:18
			
			network[sourceId][destinationId].setLength(Double.MAX_VALUE); // NOPMD by Andr� on 20/06/17 11:18
			
			final Tuple olIn = new Tuple(sourceId,destinationId,opticalLinkId,opticalLinkLength); // NOPMD by Andr� on 20/06/17 11:18
			removedLinks.add(olIn);
			
			network[destinationId][sourceId].setLength(Double.MAX_VALUE); // NOPMD by Andr� on 20/06/17 11:18
			final Tuple olOut = new Tuple(destinationId,sourceId,opticalLinkId,opticalLinkLength); // NOPMD by Andr� on 20/06/17 11:18
			removedLinks.add(olOut);	
		}
		
	}
	/**
	 * M�todo para remover temporariamente a rota na rede.
	 * @param removedLinks
	 * @param path1
	 * @param network
	 * @author Andr� 			
	 */	
	private void removePathInNetwork(final List<Tuple> removedLinks, final RoutingAlgorithmSolution path1, final OpticalLink[][] network) { // NOPMD by Andr� on 20/06/17 11:10
		//Remove the P1 route of the network
		for(int i=0;i<path1.getUpLink().size();i++){ // NOPMD by Andr� on 20/06/17 11:10
			//inbound
			final int linkSourIn = path1.getUpLink().get(i).getSource(); // NOPMD by Andr� on 20/06/17 11:10
			final int linkDestIn = path1.getUpLink().get(i).getDestination(); // NOPMD by Andr� on 20/06/17 11:10
			final int linkIdIn =  path1.getUpLink().get(i).getOpticalLinkId(); // NOPMD by Andr� on 20/06/17 11:10
			final double linklengIn =  path1.getUpLink().get(i).getLength(); // NOPMD by Andr� on 20/06/17 11:10
			//outbound
			final int linkSourOut = path1.getDownLink().get(i).getSource(); // NOPMD by Andr� on 20/06/17 11:10
			final int linkDestOut = path1.getDownLink().get(i).getDestination(); // NOPMD by Andr� on 20/06/17 11:10
			final int linkIdOut =  path1.getDownLink().get(i).getOpticalLinkId(); // NOPMD by Andr� on 20/06/17 11:10
			final double linklengOut =  path1.getDownLink().get(i).getLength(); // NOPMD by Andr� on 20/06/17 11:10
			
			network[linkSourIn][linkDestIn].setLength(Double.MAX_VALUE); // NOPMD by Andr� on 20/06/17 11:10
			network[linkSourOut][linkDestOut].setLength(Double.MAX_VALUE); // NOPMD by Andr� on 20/06/17 11:10
			
			final Tuple olIn = new Tuple(linkSourIn,linkDestIn,linkIdIn,linklengIn); // NOPMD by Andr� on 20/06/17 11:10
			final Tuple olOut = new Tuple(linkSourOut,linkDestOut,linkIdOut,linklengOut); // NOPMD by Andr� on 20/06/17 11:10
			
			removedLinks.add(olIn);
			removedLinks.add(olOut);
		}
		
	}
	/**
	 * M�todo para restaurar enlaces da rede.
	 * @param removedLinks
	 * @param network
	 * @author Andr� 			
	 */	
	private void restaurePathInNetwork(final List<Tuple> removedLinks, final OpticalLink[][] network) { // NOPMD by Andr� on 20/06/17 10:53
		// restored network 
		for(int i=0;i<removedLinks.size();i++){					
			final Tuple tuple = removedLinks.get(i);					
			network[tuple.getSourceId()][tuple.getDestinationId()].setLength(tuple.getLength());  // NOPMD by Andr� on 20/06/17 10:53
		}		
	}
	/**
	 * M�todo para remover temporariamente o caminho de inbound da rota na rede.
	 * @param removedLinks
	 * @param path1
	 * @param network
	 * @author Andr� 			
	 */	
	private void removePathInboundNetwork(final List<Tuple> removedLinks, final RoutingAlgorithmSolution path1, final OpticalLink[][] network) { // NOPMD by Andr� on 20/06/17 10:52
		//Remove the path route of the network
		for(int i=0;i<path1.getUpLink().size();i++){ // NOPMD by Andr� on 20/06/17 10:52
			//inbound
			final int optSourIn = path1.getUpLink().get(i).getSource(); // NOPMD by Andr� on 20/06/17 10:52
			final int optDestIn = path1.getUpLink().get(i).getDestination(); // NOPMD by Andr� on 20/06/17 10:51
			final int optIdIn =  path1.getUpLink().get(i).getOpticalLinkId(); // NOPMD by Andr� on 20/06/17 10:51
			final double optLinLengIn =  path1.getUpLink().get(i).getLength();		 // NOPMD by Andr� on 20/06/17 10:51
			
			network[optSourIn][optDestIn].setLength(Double.MAX_VALUE); // NOPMD by Andr� on 20/06/17 10:51
			final Tuple olIn = new Tuple(optSourIn,optDestIn,optIdIn,optLinLengIn); // NOPMD by Andr� on 20/06/17 10:51
			removedLinks.add(olIn);					
		}		
	}
	/**
	 * M�todo para retornar a inst�ncia da classe.
	 * @return O objeto SUURB_INSTANCE
	 * @author Andr� 			
	 */	
	public static SuurballeAlgorithm getSuurballeInstance(){
		return SUURB_INSTANCE;
	}

}
