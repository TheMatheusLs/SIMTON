package aco_simulation;

import java.util.List;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import parameters.SimulationParameters;
/**
 * Descreve a topologia usado no ACO.
 * @author André 
 */
public class ACONetwork{
	/**
	 * Instância da Classe.
	 * @author André 			
	 */	
	private static final ACONetwork ACO_NET_INSTANCE = new ACONetwork();
	/**
	 * Matriz de adjacência do ACO.
	 * @author André 			
	 */	
	private static ACOOpticalLink[][] acoOpticalLinks; // NOPMD by André on 16/06/17 13:53
	/**
	 * Lista de nós da rede.
	 * @author André 			
	 */	
	private static List<OpticalSwitch> listOfNodes; // NOPMD by André on 16/06/17 13:53
	/**
	 * Número de nós.
	 * @author André 			
	 */	
	private static int numberOfNodes; // NOPMD by André on 16/06/17 13:52
	/**
	 * Paramêtros da topologia de rede.
	 * @author André 			
	 */	
	private final transient SimulationParameters parameters = new SimulationParameters();
	/**
	* Construtor da classe.
	* @param listOfNodes
	* @author André
	*/
	@SuppressWarnings("static-access")
	public ACONetwork(final List<OpticalSwitch> listOfNodes){
		this.listOfNodes = listOfNodes;
		this.numberOfNodes = listOfNodes.size();
		this.acoOpticalLinks = new ACOOpticalLink[numberOfNodes][numberOfNodes];		
	}
	/**
	* Construtor da classe.
	* @author André
	*/
	public ACONetwork() { // NOPMD by André on 16/06/17 13:53
	}
	/**
	 * Método para retornar um enlace da topologia do ACO.
	 * @param sourceId
	 * @param destinationId
	 * @return O enlace alvo.
	 * @author André 			
	 */	
	public ACOOpticalLink getLinkOfNetwork(final int sourceId, final int destinationId){
		return ACONetwork.acoOpticalLinks[sourceId][destinationId];
	}
	/**
	 * Método para retornar a topologia do ACO.
	 * @return O atributo acoOpticalLinks
	 * @author André 			
	 */	
	public ACOOpticalLink[][] getNetwork() {
		return ACONetwork.acoOpticalLinks; // NOPMD by André on 16/06/17 13:15
	}
	/**
	 * Método para retornar a lista de nós.
	 * @return O atributo listOfNodes
	 * @author André 			
	 */	
	public List<OpticalSwitch> getListOfNodes() {
		return listOfNodes;
	}
	/**
	 * Método para retornar o número de nós.
	 * @return O atributo numberOfNodes
	 * @author André 			
	 */	
	public int getNumberOfNodes() {
		return numberOfNodes;
	}
	/**
	 * Método para retornar a instância da classe.
	 * @return O objeto ACO_NET_INSTANCE
	 * @author André 			
	 */	
	public static ACONetwork getACONetworkInstance(){
		return ACO_NET_INSTANCE;
	}
	/**
	 * Método para configura a topologia do ACO. 
	 * @param  network
	 * @author André 			
	 */		
	public void setNetwork(final OpticalLink[][] network){ // NOPMD by André on 16/06/17 13:17
		for(int i=0;i<numberOfNodes;i++){
			for(int j=0;j<numberOfNodes;j++){
				if(network[i][j]!=null){					
					final OpticalLink opticalLink = network[i][j];						
					final ACOOpticalLink link = new ACOOpticalLink(opticalLink.getOpticalLinkId(),opticalLink.getLength(),parameters.getNumberOfSlots(),opticalLink.getSource(),opticalLink.getDestination()); // NOPMD by André on 16/06/17 13:16
					ACONetwork.acoOpticalLinks[i][j] = link;											
				}				
			}
		}			
	}
}
