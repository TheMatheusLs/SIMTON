package network_topologies;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import exceptions.InvalidFiberLengthException;
import gain_algorithm.GainAlgorithm;
import parameters.SimulationParameters;
import arquitecture.OpticalSwitch;
import arquitecture.OpticalLink;

/**
 * Descreve a topologia de rede Nsfnet.
 * @author Andr� 
 */
public final class FinlandiaNetwork{
	/**
	 * Inst�ncia da classe.
	 * @author Andr� 			
	 */	
	private static final FinlandiaNetwork FINLANDIA_INSTANCE = new FinlandiaNetwork();
	/**
	 * Inst�ncia do algoritmo de configura��o de ganho dos
	 * amplificadores.
	 * @author Andr� 			
	 */	
	private final transient GainAlgorithm gainAlgorithm = GainAlgorithm.getGainInstance();
	/**
	 * N�mero de n�s da rede.
	 * @author Andr� 			
	 */
	private final transient int numberOfNodes;
	/**
	 * Perda do switch.
	 * @author Andr� 			
	 */
	private final transient double switchLoss;
	/**
	 * Pot�ncia inicial do laser.
	 * @author Andr� 			
	 */
	private final transient double laserPower;
	/**
	 * OSNR de entrada do laser.
	 * @author Andr� 			
	 */
	private final transient double osnr;
	/**
	 * Lista de optical links.
	 * @author Andr� 			
	 */
	private transient List<OpticalLink> opticalLinkList;
	/**
	 * Matriz de adjac�ncia da rede.
	 * @author Andr� 			
	 */
	private transient OpticalLink network[][];
	/**
	 * Matriz de adjac�ncia da rede.
	 * @author Andr� 			
	 */
	private transient List<OpticalSwitch> nodes;
	/**
	 * Construtor da classe.
	 * @author Andr�
	 */		
	private FinlandiaNetwork() {
		final SimulationParameters parameters = new SimulationParameters();
		this.numberOfNodes = 12;
		this.switchLoss = parameters.getSwitchLoss();
		this.laserPower = parameters.getLaserPower();
		this.osnr = parameters.getOSNRIn();		
	}
	/**
	 * M�todo para construit a matrix de adjac�ncia da rede.
	 * @author Andr� 			
	 */	
	private void buildNetworkAdjacencyMatrix() throws InvalidFiberLengthException{
		
		double length = 0.0;
		int linkId = 0;
		int srlgId = 0;
			
		this.network = new OpticalLink[this.numberOfNodes][this.numberOfNodes];			
		
		//create network adjacency matrix
		double[][] lengths = new double[this.numberOfNodes][this.numberOfNodes];			
		for(int x=0;x<this.numberOfNodes;x++){
			for(int y=0;y<this.numberOfNodes;y++){
				lengths[x][y] = Double.MAX_VALUE;
			}
		}
		
		//Vetor com as dist�ncia dos enlaces.
		
		lengths[0][1]   = 400;
		lengths[1][0]   = 400;		
		lengths[0][2]   = 1800;
		lengths[2][0]   = 1800;		
		lengths[1][3]   = 900;
		lengths[3][1]   = 900;		
		lengths[1][5]   = 1100;
		lengths[5][1]   = 1100;		
		lengths[2][3]   = 600;
		lengths[3][2]   = 600;		
		lengths[2][11]   = 700;
		lengths[11][2]   = 700;		
		lengths[2][10]   = 700;
		lengths[10][2]   = 700;		
		lengths[3][4]   = 800; //
		lengths[4][3]   = 800;		
		lengths[4][5]   = 800;
		lengths[5][4]   = 800;	//	
		lengths[5][6]   = 1300; //
		lengths[6][5]   = 1300;	//	
		lengths[4][6]   = 500; ///
		lengths[6][4]   = 500;	////	
		lengths[4][7]   = 400;///
		lengths[7][4]   = 400;//		
		lengths[6][8]   = 200;//
		lengths[8][6]   = 200;//		
		lengths[6][7]   = 200;//
		lengths[7][6]   = 200;//		
		lengths[7][10]   = 200;//
		lengths[10][7]   = 200;//	
		lengths[9][10]   = 500;//
		lengths[10][9]   = 500;		//
		lengths[9][11]   = 300;
		lengths[11][9]   = 300;		
		lengths[8][9]   = 700;
		lengths[9][8]   = 700;		
		lengths[7][8]   = 300;
		lengths[8][7]   = 300;
					
		for(int source=0;source<this.numberOfNodes;source++){
			for(int destination=0;destination<this.numberOfNodes;destination++){
				if(lengths[source][destination] != Double.MAX_VALUE && network[source][destination]==null){
										
					length = lengths[source][destination];
					linkId++;
					srlgId++;
					
					network[source][destination] = new OpticalLink(linkId,source,destination,srlgId,length);
					gainAlgorithm.configureGain(network[source][destination]);
					
					if(network[destination][source]==null){							
						linkId++;							
						network[destination][source] = new OpticalLink(linkId,destination,source,srlgId,length);
						gainAlgorithm.configureGain(network[destination][source]);
					}						
				}
			}
		}			
	}
	
	private void buildOpticalLinkList() throws InvalidFiberLengthException{
		
		this.opticalLinkList = new Vector<OpticalLink>();
		
		int numberOfLinkBetweenNodes = 2; //set the numbers of links for each pair of nodes (same direction).
		
		//create network adjacency matrix
		double[][] lengths = new double[this.numberOfNodes][this.numberOfNodes];			
		for(int x=0;x<this.numberOfNodes;x++){
			for(int y=0;y<this.numberOfNodes;y++){
				lengths[x][y] = Double.MAX_VALUE;
			}
		}
		
		//Vetor com as dist�ncia dos enlaces.
		
		lengths[0][1]   = 400;
		lengths[1][0]   = 400;		
		lengths[0][2]   = 1800;
		lengths[2][0]   = 1800;		
		lengths[1][3]   = 900;
		lengths[3][1]   = 900;		
		lengths[1][5]   = 1100;
		lengths[5][1]   = 1100;		
		lengths[2][3]   = 600;
		lengths[3][2]   = 600;		
		lengths[2][11]   = 700;
		lengths[11][2]   = 700;		
		lengths[2][10]   = 700;
		lengths[10][2]   = 700;		
		lengths[3][4]   = 800; //
		lengths[4][3]   = 800;		
		lengths[4][5]   = 800;
		lengths[5][4]   = 800;	//	
		lengths[5][6]   = 1300; //
		lengths[6][5]   = 1300;	//	
		lengths[4][6]   = 500; ///
		lengths[6][4]   = 500;	////	
		lengths[4][7]   = 400;///
		lengths[7][4]   = 400;//		
		lengths[6][8]   = 200;//
		lengths[8][6]   = 200;//		
		lengths[6][7]   = 200;//
		lengths[7][6]   = 200;//		
		lengths[7][10]   = 200;//
		lengths[10][7]   = 200;//	
		lengths[9][10]   = 500;//
		lengths[10][9]   = 500;		//
		lengths[9][11]   = 300;
		lengths[11][9]   = 300;		
		lengths[8][9]   = 700;
		lengths[9][8]   = 700;		
		lengths[7][8]   = 300;
		lengths[8][7]   = 300;
					
		double length = 0.0;
		int linkId = 0;
		int srlgId = 0;			
		
		for(int source=0;source<this.numberOfNodes;source++){
			for(int destination=0;destination<this.numberOfNodes;destination++){
				if(lengths[source][destination] != Double.MAX_VALUE){					
					for(int i=0;i<numberOfLinkBetweenNodes;i++){						
						length = lengths[source][destination];
						linkId++;
						srlgId++;						
						this.opticalLinkList.add(new OpticalLink(linkId,source,destination,srlgId,length)); 						
					}											
				}
			}
		}		
	}
	/**
	  * M�todo para resetar a matriz de adjac�ncia da rede.
	  * @author Andr� 			
	  */
	public void resetNetworkAdjacencyMatrix(){		
		for(int x=0;x<this.numberOfNodes;x++){
			for(int y=0;y<this.numberOfNodes;y++){
				if(network[x][y] != null){
					network[x][y].eraseOpticalLink(); // NOPMD by Andr� on 21/06/17 13:12										
				}								
			}
		}		
	}
	/**
	  * M�todo para construir a lista de n�s da rede.
	  * @author Andr� 			
	  */
	public void buildNodes(){		
		this.nodes = new ArrayList<OpticalSwitch>();		
		for(int x=0;x<this.numberOfNodes;x++){
			final OpticalSwitch node = new OpticalSwitch(x,this.switchLoss,this.laserPower,this.osnr); // NOPMD by Andr� on 21/06/17 13:12
			nodes.add(node);			
		}		
	}
	/**
	 * M�todo para retornar a inst�ncia da classe.
	 * @return O objeto NSFNET_INSTANCE
	 * @author Andr� 			
	 */	
	public static FinlandiaNetwork getFinlandiaNetworkInstance(){
		return FINLANDIA_INSTANCE;
	}
	/**
	  * M�todo para retornar a matriz de adjac�ncia da rede.
	  * @author Andr� 			
	  */
	public OpticalLink[][] getNetworkAdjacencyMatrix() throws InvalidFiberLengthException {
		this.buildNetworkAdjacencyMatrix();
		return this.network; // NOPMD by Andr� on 21/06/17 13:11
	}
	/**
	  * M�todo para retornar a lista de enlaces da rede.
	  * @author Andr� 			
	  */
	public List<OpticalLink> getOpticalLinkList() throws InvalidFiberLengthException {
		this.buildOpticalLinkList();
		return this.opticalLinkList;
	}
	/**
	  * M�todo para retornar a lista de n�s da rede.
	  * @author Andr� 			
	  */
	public List<OpticalSwitch> getListOfNodes() {
		this.buildNodes();
		return this.nodes;
	}

}
