package aco_simulation;

import java.util.List;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import parameters.SimulationParameters;
/**
 * Descreve a topologia usado no ACO.
 * @author Andr� 
 */
public class ACONetwork{
	/**
	 * Inst�ncia da Classe.
	 * @author Andr� 			
	 */	
	private static final ACONetwork ACO_NET_INSTANCE = new ACONetwork();
	/**
	 * Matriz de adjac�ncia do ACO.
	 * @author Andr� 			
	 */	
	private static ACOOpticalLink[][] acoOpticalLinks; // NOPMD by Andr� on 16/06/17 13:53
	/**
	 * Lista de n�s da rede.
	 * @author Andr� 			
	 */	
	private static List<OpticalSwitch> listOfNodes; // NOPMD by Andr� on 16/06/17 13:53
	/**
	 * N�mero de n�s.
	 * @author Andr� 			
	 */	
	private static int numberOfNodes; // NOPMD by Andr� on 16/06/17 13:52
	/**
	 * Param�tros da topologia de rede.
	 * @author Andr� 			
	 */	
	private final transient SimulationParameters parameters = new SimulationParameters();
	/**
	* Construtor da classe.
	* @param listOfNodes
	* @author Andr�
	*/
	@SuppressWarnings("static-access")
	public ACONetwork(final List<OpticalSwitch> listOfNodes){
		this.listOfNodes = listOfNodes;
		this.numberOfNodes = listOfNodes.size();
		this.acoOpticalLinks = new ACOOpticalLink[numberOfNodes][numberOfNodes];		
	}
	/**
	* Construtor da classe.
	* @author Andr�
	*/
	public ACONetwork() { // NOPMD by Andr� on 16/06/17 13:53
	}
	/**
	 * M�todo para retornar um enlace da topologia do ACO.
	 * @param sourceId
	 * @param destinationId
	 * @return O enlace alvo.
	 * @author Andr� 			
	 */	
	public ACOOpticalLink getLinkOfNetwork(final int sourceId, final int destinationId){
		return ACONetwork.acoOpticalLinks[sourceId][destinationId];
	}
	/**
	 * M�todo para retornar a topologia do ACO.
	 * @return O atributo acoOpticalLinks
	 * @author Andr� 			
	 */	
	public ACOOpticalLink[][] getNetwork() {
		return ACONetwork.acoOpticalLinks; // NOPMD by Andr� on 16/06/17 13:15
	}
	/**
	 * M�todo para retornar a lista de n�s.
	 * @return O atributo listOfNodes
	 * @author Andr� 			
	 */	
	public List<OpticalSwitch> getListOfNodes() {
		return listOfNodes;
	}
	/**
	 * M�todo para retornar o n�mero de n�s.
	 * @return O atributo numberOfNodes
	 * @author Andr� 			
	 */	
	public int getNumberOfNodes() {
		return numberOfNodes;
	}
	/**
	 * M�todo para retornar a inst�ncia da classe.
	 * @return O objeto ACO_NET_INSTANCE
	 * @author Andr� 			
	 */	
	public static ACONetwork getACONetworkInstance(){
		return ACO_NET_INSTANCE;
	}
	/**
	 * M�todo para configura a topologia do ACO. 
	 * @param  network
	 * @author Andr� 			
	 */		
	public void setNetwork(final OpticalLink[][] network){ // NOPMD by Andr� on 16/06/17 13:17
		for(int i=0;i<numberOfNodes;i++){
			for(int j=0;j<numberOfNodes;j++){
				if(network[i][j]!=null){					
					final OpticalLink opticalLink = network[i][j];						
					final ACOOpticalLink link = new ACOOpticalLink(opticalLink.getOpticalLinkId(),opticalLink.getLength(),parameters.getNumberOfSlots(),opticalLink.getSource(),opticalLink.getDestination()); // NOPMD by Andr� on 16/06/17 13:16
					ACONetwork.acoOpticalLinks[i][j] = link;											
				}				
			}
		}			
	}
}
