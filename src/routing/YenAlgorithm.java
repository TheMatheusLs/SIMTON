package routing; // NOPMD by Andr� on 20/06/17 09:50

import java.util.ArrayList;
import java.util.List;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import call_request.CallRequest;
import exceptions.InvalidNodeIdException;
import exceptions.InvalidRoutesException;
import parameters.SimulationParameters;
import types.CallRequestType;
import types.ModulationLevelType;
import utility.Function;
/**
 * Descreve o algoritmo de roteamento Yen.
 * @author Andr� 
 */
public class YenAlgorithm { // NOPMD by Andr� on 20/06/17 09:49
	/**
	 * Inst�ncia da Classe.
	 * @author Andr� 			
	 */
	private static final YenAlgorithm YEN_INSTANCE = new YenAlgorithm();
	/**
	 * M�todo para encontrar um conjunto de rotas baseado na dist�ncia f�sica.
	 * @author Andr� 			
	 */		
	public List<RoutingAlgorithmSolution> findRoutes(final OpticalLink[][] network, int originNode,  int destinationNode, final List<OpticalSwitch> listOfNodes, final int KYEN, int routeID) throws InvalidNodeIdException, InvalidRoutesException{ // NOPMD by Andr� on 20/06/17 09:49
		
		final int idSourceNode = originNode;	
		final int idDestNode = destinationNode;
		if(idSourceNode<0 || idDestNode<0){
			throw new InvalidNodeIdException("source or destination node is invalid", 1);
		}
		final List<RoutingAlgorithmSolution> solutions = new ArrayList<RoutingAlgorithmSolution>();
		
		final int numberOfNodes = listOfNodes.size();
		final SimulationParameters parameters = new SimulationParameters();
		final Function function = new Function();
		final int numbOfRoutes = KYEN;
		
		Integer[][] opticalLinkIdMatrix = new Integer[numberOfNodes][numberOfNodes]; // NOPMD by Andr� on 20/06/17 09:38
		double[][] costAuxiliar = new double[numberOfNodes][numberOfNodes]; // NOPMD by Andr� on 20/06/17 09:38
		double[][] costAuxiliar2 = new double[numberOfNodes][numberOfNodes]; // NOPMD by Andr� on 20/06/17 09:38
		
		for(int i=0;i<numberOfNodes;i++){
			for(int j=0;j<numberOfNodes;j++){
				if(network[i][j]==null){
					opticalLinkIdMatrix[i][j] = -1; // NOPMD by Andr� on 20/06/17 09:38
					costAuxiliar[i][j] = Double.MAX_VALUE; // NOPMD by Andr� on 20/06/17 09:38
					costAuxiliar2[i][j] = Double.MAX_VALUE; // NOPMD by Andr� on 20/06/17 09:38
				}else{
					opticalLinkIdMatrix[i][j] = network[i][j].getOpticalLinkId(); // NOPMD by Andr� on 20/06/17 09:39
					costAuxiliar[i][j] = network[i][j].getLength(); // NOPMD by Andr� on 20/06/17 09:39
					costAuxiliar2[i][j] = network[i][j].getLength(); // NOPMD by Andr� on 20/06/17 09:39
					// costAuxiliar[i][j] = 1.0; // NOPMD by Andr� on 20/06/17 09:39
					// costAuxiliar2[i][j] = 1.0; // NOPMD by Andr� on 20/06/17 09:39
				}				
			}			
		}
		
		ArrayList<Integer> aPath;
		ArrayList<Integer> bPath = new ArrayList<>(); // NOPMD by Andr� on 20/06/17 09:39
				
				
		final List<ArrayList<Integer>> listA = new ArrayList<>();
		listA.clear();
		List<ArrayList<Integer>> listB = new ArrayList<>(); // NOPMD by Andr� on 20/06/17 09:39
				
		if (numbOfRoutes==1){	// NOPMD by Andr� on 20/06/17 09:40
			aPath = findRoute(costAuxiliar,idSourceNode,idDestNode,numberOfNodes);
			listA.add(aPath);		
		}else{					
			aPath = findRoute(costAuxiliar2,idSourceNode,idDestNode,numberOfNodes);
					
			if(hasPath(aPath)){						
				listA.add(aPath);						
				for (int k = 1; k < numbOfRoutes; k++){							
					listB.clear();							
					aPath = listA.get(k-1);
					final int aPathSize = aPath.size(); // NOPMD by Andr� on 20/06/17 09:39
					for (int i = 0; i < aPathSize-1; i++){								
						final ArrayList<Integer> root = new ArrayList<>(); // NOPMD by Andr� on 20/06/17 09:42
						ArrayList<Integer> spur = new ArrayList<>(); // NOPMD by Andr� on 20/06/17 09:42
						for (int j = 0; j <= i; j++) {
							root.add(aPath.get(j)); // NOPMD by Andr� on 20/06/17 09:42
						}
									
						checarPreviousPathToRemoveLink(listA, k, root, costAuxiliar2);	
						checarRootToRemoveNodes(root, costAuxiliar2, numberOfNodes);
						final int lastIndex = root.size()-1;
						spur = findRoute(costAuxiliar2,root.get(lastIndex),idDestNode,numberOfNodes); 
									
						if (hasPath(spur)) {
							bPath = root;
							if(root.get(lastIndex) == spur.get(0)){ // NOPMD by dwdmlab4 on 20/06/17 09:42
								for (int l = 1; l < spur.size(); l++) { // NOPMD by dwdmlab4 on 20/06/17 09:43
									bPath.add(spur.get(l)); // NOPMD by dwdmlab4 on 20/06/17 09:43
								}
							}			                        
							final ArrayList<Integer> backCandidPathCl = bPath;
							listB.add(backCandidPathCl);
						}
									
						restoreAuxiliaryTopology(costAuxiliar, costAuxiliar2, numberOfNodes);
					}
							
					listB = filterShortestPaths(listB,costAuxiliar);
					if (listB.isEmpty()){ // NOPMD by Andr� on 20/06/17 09:44
						k = numbOfRoutes; // NOPMD by Andr� on 20/06/17 09:44
					}else if ((listA.size() + listB.size()) >= numbOfRoutes){ // NOPMD by Andr� on 20/06/17 09:44
						for (int m = 0; m < listB.size(); m++){ // NOPMD by Andr� on 20/06/17 09:45
							if (listA.size() < numbOfRoutes) {
								listA.add(listB.get(m)); // NOPMD by Andr� on 20/06/17 09:45
							}
						}					
						k = numbOfRoutes; // NOPMD by Andr� on 20/06/17 09:45

					}else{
						listA.add(listB.get(0)); // NOPMD by Andr� on 20/06/17 09:45
					}							
				}							
			}					
		}
		
		for(final ArrayList<Integer> i : listA){
			final List<OpticalLink> uplink = new ArrayList<OpticalLink>(); // NOPMD by Andr� on 20/06/17 09:47
			final List<OpticalLink> downlink = new ArrayList<OpticalLink>(); // NOPMD by Andr� on 20/06/17 09:47
			for(int j=1;j<i.size();j++){
				uplink.add(network[i.get(j-1)][i.get(j)]);  // NOPMD by Andr� on 20/06/17 09:47
			}
			if(parameters.getCallRequestType().getCode()==CallRequestType.BIDIRECTIONAL.getCode()){ // NOPMD by Andr� on 20/06/17 09:47
				for(int j=i.size()-1;j>0;j--){
					downlink.add(network[i.get(j)][i.get(j-1)]); // NOPMD by Andr� on 20/06/17 09:47
				}
			}			
			final RoutingAlgorithmSolution route = new RoutingAlgorithmSolution(uplink, downlink, routeID); // NOPMD by Andr� on 20/06/17 09:47

			ModulationLevelType[] allModulations = parameters.getModulationLevelType();
			ModulationLevelType lessModulation = allModulations[allModulations.length-1];
			
			int[] allBitRates = parameters.getBitRate();
			int bigBitRate = allBitRates[allBitRates.length-1];

			// Testar se a rota é possível ser obtida pelo menor tipo de modulação, caso contrario não sera usada.
			final double snrLinear = Math.pow(10, lessModulation.getSNRIndB()/10);
			final double osnrLinear = (((double)bigBitRate*1E9)/(2*parameters.getSpacing()))*snrLinear;

			final double inBoundQot = function.evaluateOSNR(route.getUpLink(), 159); // slots.get(0));															
			if(inBoundQot>=osnrLinear){
				solutions.add(route);
				routeID++;				
			}else{
				//System.out.println("Rota não aceita o OSNR");
			}
			
			//solutions.add(route);				

		}
		
		return solutions;
	}
	
	private List<ArrayList<Integer>> filterShortestPaths(final List<ArrayList<Integer>> listB, final double[][] cost) { // NOPMD by Andr� on 20/06/17 09:15

	    double shortestPathValue = Double.MAX_VALUE; // NOPMD by Andr� on 20/06/17 09:13
	   

	    for (int i = 0; i < listB.size(); i++) {
	        final double pathValue = getPathValue(listB.get(i),cost);
	        if (pathValue < shortestPathValue) {
	            shortestPathValue = pathValue; // NOPMD by Andr� on 20/06/17 09:14
	        }
	    }
	    
	    for (int i = (listB.size() - 1); i >= 0; i--) { 
	        final double pathValue = getPathValue(listB.get(i),cost);
	        if (pathValue != shortestPathValue){
	            listB.remove(i);
	        }
	    }

	    return listB;
	}
	
	private void restoreAuxiliaryTopology(final double[][] cost, final double[][] costAuxiliary, final int numNodes) {
		
		for (int i=0;i<numNodes;i++){
			for(int j=0;j<numNodes;j++){
				costAuxiliary[i][j] = cost[i][j];
			}
		}
		
	}
	
	private double getPathValue(final List<Integer> path, final double[][] cost) { // NOPMD by Andr� on 20/06/17 09:18

	    double pathValue = Double.MAX_VALUE; // NOPMD by Andr� on 20/06/17 09:17
	    if (!path.isEmpty()) {
	    	pathValue = 0;
	        for (int i = 1; i < path.size(); i++) {
	            pathValue += getCost(path.get(i-1),path.get(i),cost);
	        }
	    }

	    return pathValue;
	}
	
	private double getCost(final int indexI, final int indexJ, final double[][] cost){ // NOPMD by Andr� on 20/06/17 09:18
	    return cost[indexI][indexJ]; 
	}
	
	private void checarPreviousPathToRemoveLink(final List<ArrayList<Integer>> listA, final int indexK, final List<Integer> root, final double[][] auxiliaryTopology) { // NOPMD by dwdmlab4 on 20/06/17 09:32

	    for (int j = 0; j < listA.size(); j++) {
	    	final ArrayList<Integer> path = listA.get(j);
	    	if(root.size() == 1){ // NOPMD by Andr� on 20/06/17 09:32
	    		if(path.get(0)==root.get(0)){ // NOPMD by Andr� on 20/06/17 09:32
	    			removeLinkOfAuxiliaryTopology(path.get(0),path.get(1), auxiliaryTopology); // NOPMD by Andr� on 20/06/17 09:32
	    		}	    		
	    	}else{
	    		boolean status = false; // NOPMD by Andr� on 20/06/17 09:32
	    		final int rootSource = root.get(0);
	    		final int lastIndex = root.size()-1;
	    		final int rootDest = root.get(lastIndex);
	    		double rootCost = 0.0; // NOPMD by Andr� on 20/06/17 09:32
	    		double tempCost = 0.0; // NOPMD by Andr� on 20/06/17 09:32
	    		final ArrayList<Integer> temp = new ArrayList<Integer>(); // NOPMD by Andr� on 20/06/17 09:33
	    		
	    		for(int m=0;m<path.size()-1;m++){ // NOPMD by Andr� on 20/06/17 09:33
	    			status = false; // NOPMD by Andr� on 20/06/17 09:33
	    			temp.clear();
	    			final int pathFirst = path.get(m); // NOPMD by Andr� on 20/06/17 09:33
	    			
	    			if(pathFirst == rootSource){
	    				temp.add(pathFirst);
	    				for(int z=m+1;z<path.size()-1;z++){ // NOPMD by Andr� on 20/06/17 09:33
	    					temp.add(path.get(z)); // NOPMD by Andr� on 20/06/17 09:33
	    					if(path.get(z)==rootDest){ // NOPMD by Andr� on 20/06/17 09:33
	    						status = true;
	    						break;
	    					}
	    				}
	    			}
	    			
	    			if(status){ //encontrei o trecho em path que tem o fonte e destino de root
	    				break;
	    			}
	    			
	    		}
	    		
	    		if (status){
	    			rootCost = getPathValue(root,auxiliaryTopology);
	    			tempCost = getPathValue(temp,auxiliaryTopology);
	    		}
	    		
	    		
	    		if(status && root.size()==temp.size() && rootCost==tempCost){
	    			boolean statusfinal = true; // NOPMD by Andr� on 20/06/17 09:31
	    			
	    			for(int d=0;d<temp.size();d++){ //verifica os todos nos de ambos os caminhos 
	    				if(root.get(d)!=temp.get(d)){statusfinal = false;} // NOPMD by Andr� on 20/06/17 09:31
	    			}
	    			
	    			if(statusfinal){
	    				for(int g=0;g<path.size();g++){ // NOPMD by Andr� on 20/06/17 09:31
	    					final int lastIndex2 = temp.size()-1;
	    					if(path.get(g)==temp.get(lastIndex2)){ // NOPMD by Andr� on 20/06/17 09:31
	    						removeLinkOfAuxiliaryTopology(path.get(g),path.get(g+1), auxiliaryTopology); // NOPMD by Andr� on 20/06/17 09:31
	    					}
	    				}
	    			}
	    		}
	    	}	        
	    }
	}

	private void removeLinkOfAuxiliaryTopology(final int indexI, final int indexQ, final double[][] auxiliaryTopology){	     // NOPMD by Andr� on 20/06/17 09:31
		auxiliaryTopology[indexI][indexQ] = Double.MAX_VALUE;
	    auxiliaryTopology[indexQ][indexI] = Double.MAX_VALUE;
	}
	
	private void checarRootToRemoveNodes( final List<Integer> root, final double[][] auxiliaryTopology, final int nodesNumber) {
	    if(!root.isEmpty()){
	    	for ( int i = 0; i < root.size()-1; i++ ) {
		        removeNodeOfAuxiliaryTopology(root.get(i),auxiliaryTopology, nodesNumber);
		    }	    	
	    }		
	}

	private void removeNodeOfAuxiliaryTopology(final int index, final double[][] auxiliaryTopology, final int nodesNumber) {
	    for ( int j = 0; j < nodesNumber; j++ ) {
	        auxiliaryTopology[j][index] = Double.MAX_VALUE; 
	        auxiliaryTopology[index][j] = Double.MAX_VALUE;
	    }
	}
	
	private boolean hasPath(final List<Integer> path){	    
	    return !path.isEmpty();
	}
	
	private ArrayList<Integer> findRoute(final double[][] cost, final int idSourceNode, final int idDestNode, final int numNodes) { // NOPMD by Andr� on 20/06/17 09:22
		
		final ArrayList<Integer> path = new ArrayList<>();

		// conter� todos os n�s para os quais j� se foi encontrado o caminho com
		// menor fator de ru�do
		final List<Integer> listS = new ArrayList<Integer>(); // NOPMD by Andr� on 19/06/17 15:51
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

		distances = new double[numNodes]; // NOPMD by Andr� on 16/06/17 10:14
		father = new int[numNodes]; // NOPMD by Andr� on 16/06/17 10:14
				
		for (vertex = 0; vertex < numNodes; vertex++) {
			distances[vertex] = Double.MAX_VALUE; // NOPMD by Andr� on 16/06/17 10:15
			father[vertex] = -1; // NOPMD by Andr� on 16/06/17 10:15
			listQ.add(vertex);
		}

		distances[idSourceNode] = 0; // NOPMD by Andr� on 19/06/17 15:51

		final int[] temp = new int[1]; // NOPMD by Andr� on 19/06/17 15:51
		while (!listQ.isEmpty()) {
			totalShortDist = findMinimum(numNodes, listS, listQ, distances);
			if (totalShortDist != -1) {
				for (cont = 0; cont < numNodes; cont++) {
					
						final double distBetwNodes = cost[totalShortDist][cont];					

						if (cont != totalShortDist && !belongs(listS, cont, temp) && distBetwNodes != Double.MAX_VALUE) {
							final double shortestDistance = distances[totalShortDist] + distBetwNodes;
							if (distances[cont] > shortestDistance) { // NOPMD by Andr� on 16/06/17 10:16
								distances[cont] = shortestDistance; // NOPMD by Andr� on 16/06/17 10:16
								father[cont] = totalShortDist; // NOPMD by Andr� on 16/06/17 10:16
							}
						}
				}
			}

		}
		track.clear();
		this.findPath(father[idDestNode], idDestNode, father, track, idSourceNode);
		if (track.isEmpty()) {
			path.clear();		
		} else {
			path.clear();
			final int index = track.size();			
			for(int i=index-1;i>=0;i--){  
				path.add(track.get(i));
			}
		}
		
		return path;
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
	 * @return O objeto YEN_INSTANCE
	 * @author Andr� 			
	 */	
	public static YenAlgorithm getYenInstance(){
		return YEN_INSTANCE;
	}

}
