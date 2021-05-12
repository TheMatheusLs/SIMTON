package survivability;

import java.util.List;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import call_request.DPCallRequest;
import exceptions.InvalidNodeIdException;
/**
 * Descreve a interface dos algoritmos de prote��o dedicada.
 * @author Andr� 
 */
public interface IDedicatedProtectionAlgorithm {
	
	/**
	 * Encontra as rotas disjuntas com base em uma
	 * requisi��o de chamada e uma topologia de rede. * 
	 * @param network 
	 * @param callRequest
	 * @param listOfNodes 
	 * @return Uma solu��o contendo as duas rotas disjuntas.
	 */	
	DedicatedProtectionAlgorithmSolution findRoutes(OpticalLink[][] network, DPCallRequest callRequest, List<OpticalSwitch> listOfNodes) throws InvalidNodeIdException;

}
