package survivability;

import java.util.List;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import call_request.DPCallRequest;
import exceptions.InvalidNodeIdException;
/**
 * Descreve a interface dos algoritmos de proteção dedicada.
 * @author André 
 */
public interface IDedicatedProtectionAlgorithm {
	
	/**
	 * Encontra as rotas disjuntas com base em uma
	 * requisição de chamada e uma topologia de rede. * 
	 * @param network 
	 * @param callRequest
	 * @param listOfNodes 
	 * @return Uma solução contendo as duas rotas disjuntas.
	 */	
	DedicatedProtectionAlgorithmSolution findRoutes(OpticalLink[][] network, DPCallRequest callRequest, List<OpticalSwitch> listOfNodes) throws InvalidNodeIdException;

}
