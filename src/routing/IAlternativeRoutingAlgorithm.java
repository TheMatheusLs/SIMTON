package routing;

import java.util.List;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import exceptions.InvalidNodeIdException;
/**
 * Descreve a interface dos algoritmos de roteamento fixo-alternativo.
 * @author Andr� 
 */
public interface IAlternativeRoutingAlgorithm {	
	/**
	 * Encontra o conjunto de rotas de uplink e downlink com base
	 * em uma requisi��o de chamada e uma topologia de rede. 
	 * @param network
	 * @param callRequest
	 * @return uma conjunto de solu��es para atender a demanda.
	 */	
	List<RoutingAlgorithmSolution> findRoutes(OpticalLink[][] network, int originNode,  int destinationNode, List<OpticalSwitch> listOfNodes) throws InvalidNodeIdException;	
}
