package network_topologies;

import java.util.List;

import arquitecture.OpticalSwitch;
import arquitecture.OpticalLink;

/**
 * Descreve a interface das topologias de rede usadas no simulador.
 * @author André 
 */
public interface INetworkTopology {	
	/**
	 * Obtém a matriz de adjacencia da rede.
	 * @author André 
	 */	
	OpticalLink[][] getNetworkAdjacencyMatrix();
	/**
	 * Obtém a lista de enlaces da rede.
	 * @author André 
	 */	
	List<OpticalLink> getOpticalLinkList();
	/**
	 * Obtém a lista de nós da rede.
	 * @author André 
	 */
	List<OpticalSwitch> getListOfNodes();
	/**
	 * Método para resetar a topologia de rede.
	 * @author André 
	 */	
	void resetNetwork();	

}
