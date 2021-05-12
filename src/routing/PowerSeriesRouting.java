package routing;

import java.util.ArrayList;
import java.util.List;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import call_request.CallRequest;
import exceptions.InvalidListOfCoefficientsException;
import exceptions.InvalidNodeIdException;
import parameters.SimulationParameters;
import types.CallRequestType;
/**
 * Descreve o algoritmo de roteamento baseado em série de potências.
 * @author André 
 */
public class PowerSeriesRouting implements IEvolucionaryRoutingAlgorithm { // NOPMD by André on 19/06/17 15:01
	/**
	 * Instância da Classe.
	 * @author André 			
	 */
	private static final PowerSeriesRouting PSR_INSTANCE = new PowerSeriesRouting();
	/**
	 * Método para encontrar uma rota.
	 * @author André 			
	 */	
	public RoutingAlgorithmSolution findRoute(final OpticalLink[][] network, final CallRequest callRequest, final List<OpticalSwitch> listOfNodes, final List<Double> listOfCoeff) throws InvalidNodeIdException, InvalidListOfCoefficientsException{ // NOPMD by André on 19/06/17 15:01
		
		final int idSourceNode = callRequest.getSourceId();	
		final int idDestNode = callRequest.getDestinationId();
		if(idSourceNode<0 || idDestNode<0){
			throw new InvalidNodeIdException("source or destination node is invalid", 1);
		}
		final SimulationParameters parameters = new SimulationParameters();
		final int numNodes = listOfNodes.size();		
		final double maxLength = parameters.getMaxLength();		 // NOPMD by André on 19/06/17 14:47
			
		// conterá todos os nós para os quais já se foi encontrado o caminho com
		// menor fator de ruído
		final List<Integer> listS = new ArrayList<Integer>();
		// conterá os nós para os quais ainda não foram encontrados os caminhos
		// com menor fator de ruído (ou de outra métrica considerada)
		final List<Integer> listQ = new ArrayList<Integer>();
		// vetor de distancias: conterá o valor da menor métrica entre a origem
		// e o nó (índice do vetor)
		double[] distances;
		// indica o nó anterior de cada nó
		int[] father;
		int vertex;
		int totalShortDist;
		int cont;
		final List<Integer> track = new ArrayList<Integer>();
						
		distances = new double[numNodes]; // NOPMD by André on 19/06/17 09:19
		father = new int[numNodes]; // NOPMD by André on 19/06/17 09:19

		for (vertex = 0; vertex < numNodes; vertex++) {
			distances[vertex] = Double.MAX_VALUE; // NOPMD by André on 16/06/17 10:15
			father[vertex] = -1; // NOPMD by André on 16/06/17 10:15
			listQ.add(vertex);
		}

		distances[idSourceNode] = 0;

		final int[] temp = new int[1];
		while (!listQ.isEmpty()) {
			totalShortDist = findMinimum(numNodes, listS, listQ, distances);
			if (totalShortDist != -1) {
				for (cont = 0; cont < numNodes; cont++) {
										
					if(callRequest.getCallRequestType().equals(CallRequestType.BIDIRECTIONAL)){ // NOPMD by dwdmlab4 on 19/06/17 14:23
						
						if(network[totalShortDist][cont] != null && network[cont][totalShortDist] != null){							
							
							double distBetwNodes;
							double x; // NOPMD by André on 19/06/17 14:38
							final int dimension = parameters.getDimension()-2;	// NOPMD by André on 19/06/17 14:38
							final OpticalLink uplink = network[totalShortDist][cont];
							final double uplinkLength = uplink.getLength();		// NOPMD by André on 19/06/17 14:38
							final OpticalLink Downlink = network[cont][totalShortDist];
							final double downlinkLength = Downlink.getLength();	 // NOPMD by André on 19/06/17 14:39
								
							if(uplinkLength==Double.MAX_VALUE || downlinkLength==Double.MAX_VALUE){
								distBetwNodes = Double.MAX_VALUE;
							}else{
									
								x = (uplinkLength+downlinkLength)/(2.0*maxLength);	// NOPMD by André on 19/06/17 14:47
								double potenciaX = 1.0; // NOPMD by André on 19/06/17 14:47
								double parcialX = 0.0;

								for (int j=-2;j<dimension;j++){			                                	
									parcialX += listOfCoeff.get(j+2)*potenciaX;
									potenciaX = potenciaX*x;
								}									
								distBetwNodes = parcialX;
									
							}

							if (cont != totalShortDist && !belongs(listS, cont, temp) && distBetwNodes != Double.MAX_VALUE) {
								final double shortestDistance = distances[totalShortDist] + distBetwNodes;
								if (distances[cont] > shortestDistance) { // NOPMD by André on 16/06/17 10:16
									distances[cont] = shortestDistance; // NOPMD by André on 16/06/17 10:16
									father[cont] = totalShortDist; // NOPMD by André on 16/06/17 10:16
								}
							}		
						
						}
						
					}else{
						
						if(network[totalShortDist][cont] != null){
							
							double distBetwNodes;
							double x; // NOPMD by André on 19/06/17 14:54
							final int dimension = parameters.getDimension()-2; // NOPMD by André on 19/06/17 14:54
							final OpticalLink uplink = network[totalShortDist][cont];
							final double uplinkLength = uplink.getLength(); // NOPMD by André on 19/06/17 14:55
								
							if(uplinkLength==Double.MAX_VALUE){
								distBetwNodes = Double.MAX_VALUE;
							}else{
								x = uplinkLength/maxLength; // NOPMD by André on 19/06/17 14:55
									
								double potenciaX = 1.0;
								double parcialX = 0.0;

								for (int j=-2;j<dimension;j++){		                                	
									parcialX += listOfCoeff.get(j+2)*potenciaX;
									potenciaX = potenciaX*x;
								}
								distBetwNodes = parcialX;
							}

							if (cont != totalShortDist && !belongs(listS, cont, temp) && distBetwNodes != Double.MAX_VALUE) {
								final double shortestDistance = distances[totalShortDist] + distBetwNodes;
								// RELAX
								if (distances[cont] > shortestDistance) {
									distances[cont] = shortestDistance; // NOPMD by André on 16/06/17 10:17
									father[cont] = totalShortDist; // NOPMD by André on 19/06/17 10:00
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
		
		//Rotas da solução
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
				if(callRequest.getCallRequestType().equals(CallRequestType.BIDIRECTIONAL)){ // NOPMD by André on 16/06/17 10:17
					final int sourceDown = track.get(i-1);
					final int destinationDown = track.get(i);
					final OpticalLink optLinkDown = network[sourceDown][destinationDown];
					outBoundSolution.add(optLinkDown);
				}				
			}			
		}
		return  new RoutingAlgorithmSolution(inBoundSolution,outBoundSolution);
		
	}
	
	private int findMinimum(final int size, final List<Integer> listS, final List<Integer> listQ, final double... distances) {
		int lessNode = 0; // NOPMD by André on 16/06/17 10:17
		final int[] position = new int[1]; // NOPMD by André on 16/06/17 10:17
		double lessFvalue = Double.MAX_VALUE; // NOPMD by André on 16/06/17 10:17
		boolean flag = false; // NOPMD by André on 16/06/17 10:17
		for (int i = 0; i < size; i++) {
			if (lessFvalue > distances[i] && belongs(listQ, i, position)) {
				lessNode = i; // NOPMD by André on 16/06/17 10:17
				lessFvalue = distances[i]; // NOPMD by André on 16/06/17 10:17
				flag = true; // NOPMD by André on 16/06/17 10:17
			}
		}
		if (flag) {
			listS.add(lessNode);
			final int element = position[0];
			listQ.remove(element);
			return lessNode; // NOPMD by André on 16/06/17 10:18
		} else {
			listQ.clear();
		}
		return -1;
	
	}

	private boolean belongs(final List<Integer> listQ, final int key, final int... position) { // NOPMD by dwdmlab4 on 16/06/17 10:18
		for (int i = 0; i < listQ.size(); i++) {
			if (listQ.get(i) == key) {
				position[0] = i;
				return true; // NOPMD by André on 16/06/17 10:18
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
	 * Método para retornar a instância da classe.
	 * @return O objeto PSR_INSTANCE
	 * @author André 			
	 */	
	public static PowerSeriesRouting getPSRInstance(){
		return PSR_INSTANCE;
	}

}
