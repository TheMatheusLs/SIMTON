package routing;

import java.util.List;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import exceptions.InvalidNodeIdException;
/**
 * Descreve a interface dos algoritmos de roteamento.
 * @author Andr� 
 */
public interface IRoutingAlgorithm {	
	/**
	 * Encontra as rotas de uplink e downlink com base em uma
	 * requisi��o de chamada e uma topologia de rede. * 
	 * @param network 
	 * @param callRequest
	 * @param listOfNodes 
	 * @return Uma solu��o contendo as duas rotas.
	 */	
	List<RoutingAlgorithmSolution> findRoute(OpticalLink[][] network, int originNode,  int destinationNode, List<OpticalSwitch> listOfNodes) throws InvalidNodeIdException;	
	

}
