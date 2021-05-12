package routing;

import java.util.List;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import call_request.CallRequest;
import exceptions.InvalidListOfCoefficientsException;
import exceptions.InvalidNodeIdException;
/**
 * Descreve a interface dos algoritmos de roteamento evolucionários.
 * @author André 
 */
public interface IEvolucionaryRoutingAlgorithm {	
	/**
	 * Encontra as rotas de uplink e downlink com base em uma requisição 
	 * de chamada e uma topologia de rede. 
	 * @param network
	 * @param callRequest
	 * @param listOfCoef
	 * @return uma solução contendo as duas rotas.
	 */		
	RoutingAlgorithmSolution findRoute(OpticalLink[][] network, CallRequest callRequest, List<OpticalSwitch> listOfNodes, List<Double> listOfCoef) throws InvalidNodeIdException, InvalidListOfCoefficientsException;

}
