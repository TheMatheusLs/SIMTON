package routing;

import java.util.ArrayList;
import java.util.List;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import call_request.CallRequest;
import exceptions.InvalidNodeIdException;
import parameters.SimulationParameters;
import types.CallRequestType;
import utility.Function;
/**
 * Descreve o algoritmo de roteamento CASP.
 * @author Andr� 
 */
public class CongestedAwareShortestPath implements IRoutingAlgorithm { // NOPMD by Andr� on 19/06/17 10:02
	/**
	 * Inst�ncia da Classe.
	 * @author Andr� 			
	 */	
	private static final CongestedAwareShortestPath CASP_INSTANCE = new CongestedAwareShortestPath();		
	/**
	 * M�todo para encontrar uma rota baseado no congestionamento dos enlaces.
	 * @author Andr� 			
	 */	
	public List<RoutingAlgorithmSolution> findRoute(final OpticalLink[][] network, int originNode,  int destinationNode, final List<OpticalSwitch> listOfNodes) throws InvalidNodeIdException{ // NOPMD by Andr� on 19/06/17 10:02
		
		final int idSourceNode = originNode;	
		final int idDestNode = destinationNode;
		if(idSourceNode<0 || idDestNode<0){
			throw new InvalidNodeIdException("source or destination node is invalid", 1);
		}		
		
		final int numNodes = listOfNodes.size();		
		final SimulationParameters parameters = new SimulationParameters();
		final int numberOfSlots = parameters.getNumberOfSlots();
		final Function function = new Function();
		
		// conter� todos os n�s para os quais j� se foi encontrado o caminho com
		// menor fator de ru�do
		final List<Integer> listS = new ArrayList<Integer>();
		// conter� os n�s para os quais ainda n�o foram encontrados os caminhos
		// com menor fator de ru�do (ou de outra m�trica considerada)
		final List<Integer> listQ = new ArrayList<Integer>();
		// vetor de distancias: conter� o valor da menor m�trica entre a origem
		// e o n� (�ndice do vetor)
		double[] distances;
		// indica o n� anterior de cada n�
		int[] father;
		int vertex;
		int totalShortDist;
		int cont;
		final List<Integer> track = new ArrayList<Integer>();
		
		distances = new double[numNodes]; // NOPMD by Andr� on 19/06/17 09:19
		father = new int[numNodes]; // NOPMD by Andr� on 19/06/17 09:19

		for (vertex = 0; vertex < numNodes; vertex++) {
			distances[vertex] = Double.MAX_VALUE; // NOPMD by Andr� on 16/06/17 10:15
			father[vertex] = -1; // NOPMD by Andr� on 16/06/17 10:15
			listQ.add(vertex);
		}

		distances[idSourceNode] = 0;

		final int[] temp = new int[1];
		while (!listQ.isEmpty()) {
			totalShortDist = findMinimum(numNodes, listS, listQ, distances);
			if (totalShortDist != -1) {
				for (cont = 0; cont < numNodes; cont++) {
										
					if(parameters.getCallRequestType().equals(CallRequestType.BIDIRECTIONAL)){ // NOPMD by Andr� on 16/06/17 10:16
						
						if(network[totalShortDist][cont] != null && network[cont][totalShortDist] != null){				
							
							double distBetwNodes;
							
							final OpticalLink optLinkUp = network[totalShortDist][cont];
							final OpticalLink optLinkDown = network[cont][totalShortDist];
							final double uplinkCost = function.getLengthInLink(optLinkUp); // NOPMD by Andr� on 19/06/17 09:56
							final double downlinkCost = function.getLengthInLink(optLinkDown); // NOPMD by Andr� on 19/06/17 09:56
							int resultVect = 0; // NOPMD by Andr� on 19/06/17 09:56
							
							for(int f=0;f<numberOfSlots;f++){
								if(network[totalShortDist][cont].availableSlot(f) && network[cont][totalShortDist].availableSlot(f)){ // NOPMD by Andr� on 19/06/17 09:51
									resultVect++; // NOPMD by Andr� on 19/06/17 09:56
								}
							}
							
							final double linkCost = (double) resultVect/numberOfSlots;
							
							if(linkCost>0.0){  // NOPMD by Andr� on 19/06/17 09:56
								distBetwNodes = (uplinkCost + downlinkCost)/2.0;
								distBetwNodes /= linkCost;
							}else{
								distBetwNodes = Double.MAX_VALUE;
							}						
							
							if (cont != totalShortDist && !belongs(listS, cont, temp) && distBetwNodes != Double.MAX_VALUE) {
								final double shortestDistance = distances[totalShortDist] + distBetwNodes;
								if (distances[cont] > shortestDistance) { // NOPMD by Andr� on 16/06/17 10:16
									distances[cont] = shortestDistance; // NOPMD by Andr� on 16/06/17 10:16
									father[cont] = totalShortDist; // NOPMD by Andr� on 16/06/17 10:16
								}
							}							
						}
						
					}else{
						
						if(network[totalShortDist][cont] != null){							
							double distBetwNodes;
							final OpticalLink optLinkUp = network[totalShortDist][cont];							
							final double uplinkCost = function.getLengthInLink(optLinkUp); // NOPMD by Andr� on 19/06/17 09:59
							int resultVect = 0; // NOPMD by Andr� on 19/06/17 09:56
							
							for(int f=0;f<numberOfSlots;f++){
								if(network[totalShortDist][cont].availableSlot(f)){ // NOPMD by Andr� on 19/06/17 09:51
									resultVect++; // NOPMD by Andr� on 19/06/17 09:56
								}
							}
							
							final double linkCost = (double) resultVect/numberOfSlots;
							
							if(linkCost>0.0){  // NOPMD by Andr� on 19/06/17 09:56
								distBetwNodes = uplinkCost/linkCost;
							}else{
								distBetwNodes = Double.MAX_VALUE;
							}	
					
							if (cont != totalShortDist && !belongs(listS, cont, temp) && distBetwNodes != Double.MAX_VALUE) {
								final double shortestDistance = distances[totalShortDist] + distBetwNodes;
								// RELAX
								if (distances[cont] > shortestDistance) {
									distances[cont] = shortestDistance; // NOPMD by Andr� on 16/06/17 10:17
									father[cont] = totalShortDist; // NOPMD by Andr� on 19/06/17 10:00
								}
							}						
						}						
					}					
				}				
			}

		}
		track.clear();
		final int minToalF = father[idDestNode];
		findPath(minToalF, idDestNode, father, track, idSourceNode);
		
		//Rotas da solu��o
		final List<OpticalLink> inBoundSolution = new ArrayList<OpticalLink>();
		final List<OpticalLink> outBoundSolution = new ArrayList<OpticalLink>();
		
		
		if(!track.isEmpty()){
			inBoundSolution.clear();
			outBoundSolution.clear();
			final int index = track.size();
			for(int i=1; i<index;i++){
				final int sourceUp = track.get(index-i);
				final int destinationUp = track.get(index-i-1);
				final OpticalLink optLinkUp = network[sourceUp][destinationUp];
				inBoundSolution.add(optLinkUp);
				if(parameters.getCallRequestType().equals(CallRequestType.BIDIRECTIONAL)){ // NOPMD by Andr� on 16/06/17 10:17
					final int sourceDown = track.get(i-1);
					final int destinationDown = track.get(i);
					final OpticalLink optLinkDown = network[sourceDown][destinationDown];
					outBoundSolution.add(optLinkDown);
				}				
			}			
		}

		final List<RoutingAlgorithmSolution> solutions = new ArrayList<RoutingAlgorithmSolution>();
		RoutingAlgorithmSolution route = new RoutingAlgorithmSolution(inBoundSolution,outBoundSolution);
		solutions.add(route);

		return solutions;
	}
	
	private int findMinimum(final int size, final List<Integer> listS, final List<Integer> listQ, final double... distances) {
		int lessNode = 0; // NOPMD by Andr� on 16/06/17 10:17
		final int[] position = new int[1]; // NOPMD by Andr� on 16/06/17 10:17
		double lessFvalue = Double.MAX_VALUE; // NOPMD by Andr� on 16/06/17 10:17
		boolean flag = false; // NOPMD by Andr� on 16/06/17 10:17
		for (int i = 0; i < size; i++) {
			if (lessFvalue > distances[i] && belongs(listQ, i, position)) {
				lessNode = i; // NOPMD by Andr� on 16/06/17 10:17
				lessFvalue = distances[i]; // NOPMD by Andr� on 16/06/17 10:17
				flag = true; // NOPMD by Andr� on 16/06/17 10:17
			}
		}
		if (flag) {
			listS.add(lessNode);
			final int element = position[0];
			listQ.remove(element);
			return lessNode; // NOPMD by Andr� on 16/06/17 10:18
		} else {
			listQ.clear();
		}
		return -1;
	
	}

	private boolean belongs(final List<Integer> listQ, final int key, final int... position) { // NOPMD by dwdmlab4 on 16/06/17 10:18
		for (int i = 0; i < listQ.size(); i++) {
			if (listQ.get(i) == key) {
				position[0] = i;
				return true; // NOPMD by Andr� on 16/06/17 10:18
			}
		}
		return false;
	}

	private void findPath(final int indice, final int idDestNode, final int[] father, final List<Integer> path, final int idSourceNode) {
		int previous;
		path.clear();
		path.add(idDestNode);	
		if (indice == -1) {
			path.clear();		
		}else{
			path.add(indice);
			previous = indice;
			while (previous != idSourceNode){
				if (father[previous] == -1) {
					break;
				}else{
					final int element = father[previous];
					path.add(element);
					previous = element;
				}			
			}
		}
	}
	/**
	 * M�todo para retornar a inst�ncia da classe.
	 * @return O objeto CASP_INSTANCE
	 * @author Andr� 			
	 */	
	public static CongestedAwareShortestPath getCASPInstance(){
		return CASP_INSTANCE;
	}	
}
