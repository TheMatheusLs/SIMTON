package routing;

import java.util.ArrayList;
import java.util.List;

import aco_simulation.ACOAlgorithm;
import aco_simulation.ACOAnt;
import aco_simulation.ACONetwork;
import aco_simulation.ACOOpticalLink;
import aco_simulation.ACOParametersSet;
import arquitecture.OpticalSwitch;
import call_request.CallRequest;
import arquitecture.OpticalLink;
import exceptions.InvalidListOfCoefficientsException;
import exceptions.InvalidNodeIdException;
import parameters.SimulationParameters;
import types.CallRequestType;
/**
 * Descreve o algoritmo de roteamento Ant Colony.
 * @author André 
 */
public class AntColonyBasedRoutingAlgorithm implements IEvolucionaryRoutingAlgorithm {
	
	/**
	 * Instância da Classe.
	 * @author André 			
	 */	
	private static final AntColonyBasedRoutingAlgorithm ACO_SP_INSTANCE = new AntColonyBasedRoutingAlgorithm();
	/**
	 * Método para encontrar uma rota usando o algortimo de roteamento Ant Colony.
	 * @author André 			
	 */
	public RoutingAlgorithmSolution findRoute(final OpticalLink[][] network, final CallRequest callRequest, final List<OpticalSwitch> listOfNodes, final List<Double> listOfCoeff) throws InvalidNodeIdException,
			InvalidListOfCoefficientsException {
		
		final int idSourceNode = callRequest.getSourceId();		
		if(idSourceNode<0){
			throw new InvalidNodeIdException("source node is invalid", 1);
		}
		final int idDestNode = callRequest.getDestinationId();		
		if(idDestNode<0){
			throw new InvalidNodeIdException("destination node is invalid", 2);
		}
		
		final List<Double> alpha = new ArrayList<Double>();
		final List<Double> beta = new ArrayList<Double>();
		final List<Double> gamma = new ArrayList<Double>();
		
		final ACONetwork acoNet = new ACONetwork(listOfNodes);
		acoNet.setNetwork(network);
				
		alpha.add(SimulationParameters.getAlfa());
		beta.add(SimulationParameters.getBeta());
		gamma.add(SimulationParameters.getGamma());		
				
		final ACOParametersSet acoParameters = new ACOParametersSet(SimulationParameters.getEr(),SimulationParameters.getPhk(),alpha,beta,gamma,SimulationParameters.getCreationfrequency(),5,acoNet,1);
		
		final ACOAlgorithm simulation = new ACOAlgorithm();
		simulation.defineParameters(acoParameters);	
		final ACOAnt ant = new ACOAnt(idSourceNode, idDestNode);		
		simulation.performSimulation();			
		simulation.request(ant,idSourceNode, idDestNode);
		final List<OpticalLink> uplink = new ArrayList<OpticalLink>();		
		final List<OpticalLink> downlink = new ArrayList<OpticalLink>();	
		
		uplink.clear(); //Uplink route.
		for(int i=0;i<ant.getTourUplink().size();i++){ // NOPMD by André on 16/06/17 14:47
			final ACOOpticalLink opticalLink = ant.getTourUplink().get(i); // NOPMD by André on 16/06/17 14:47
			uplink.add(network[opticalLink.getSourceNode()][opticalLink.getDestinationNode()]);	// NOPMD by André on 16/06/17 14:47
		}		
		
		//Downlink route	
		if(callRequest.getCallRequestType().getCode()==CallRequestType.BIDIRECTIONAL.getCode()){ // NOPMD by André on 16/06/17 14:48
			for(int x=uplink.size();x>0;x--){
				downlink.add(network[uplink.get(x-1).getDestination()][uplink.get(x-1).getSource()]); // NOPMD by André on 16/06/17 14:48
			}
		}	
				
		return new RoutingAlgorithmSolution(uplink,downlink);
	}
	/**
	 * Método para retornar a instância da classe.
	 * @return O objeto ACO_SP_INSTANCE
	 * @author André 			
	 */	
	public static AntColonyBasedRoutingAlgorithm getACOSPInstance(){
		return ACO_SP_INSTANCE;
	}

}
