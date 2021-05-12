package network_topologies;

import java.util.List;

import arquitecture.OpticalSwitch;
import arquitecture.OpticalLink;

/**
 * Descreve a interface das topologias de rede usadas no simulador.
 * @author Andr� 
 */
public interface INetworkTopology {	
	/**
	 * Obt�m a matriz de adjacencia da rede.
	 * @author Andr� 
	 */	
	OpticalLink[][] getNetworkAdjacencyMatrix();
	/**
	 * Obt�m a lista de enlaces da rede.
	 * @author Andr� 
	 */	
	List<OpticalLink> getOpticalLinkList();
	/**
	 * Obt�m a lista de n�s da rede.
	 * @author Andr� 
	 */
	List<OpticalSwitch> getListOfNodes();
	/**
	 * M�todo para resetar a topologia de rede.
	 * @author Andr� 
	 */	
	void resetNetwork();	

}
