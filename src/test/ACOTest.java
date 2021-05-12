package test;

import java.util.ArrayList;
import java.util.List;

import aco_simulation.ACOAlgorithm;
import aco_simulation.ACOAnt;
import aco_simulation.ACONetwork;
import aco_simulation.ACOOpticalLink;
import aco_simulation.ACOParametersSet;
import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import exceptions.InvalidFiberLengthException;
import network_topologies.NsfnetNetwork;

/**
 * @author Andr�
 *
 */
public class ACOTest {
	/**
	 * Fluxo de teste do ACO.
	 * @param args
	 * @throws InvalidFiberLengthException
	 */
	public static void main(final String[] args) throws InvalidFiberLengthException {
		
		
		final NsfnetNetwork nsfnetInstance = NsfnetNetwork.getNSFNETNetworkInstance();
		final OpticalLink[][] network = nsfnetInstance.getNetworkAdjacencyMatrix(); // NOPMD by Andr� on 26/06/17 15:13
		final List<OpticalSwitch> listOfNodes = nsfnetInstance.getListOfNodes(); // NOPMD by Andr� on 26/06/17 15:13
		
		final ACONetwork net = new ACONetwork(listOfNodes);
		net.setNetwork(network);
		final List<Double> alpha = new ArrayList<Double>();
		final List<Double> beta = new ArrayList<Double>();
		final List<Double> gamma = new ArrayList<Double>();
		
		alpha.add(4.0);
		beta.add(2.0);
		gamma.add(0.0);
		
		final ACOParametersSet parameters = new ACOParametersSet(1.0,10.0,alpha,beta,gamma,2,5,net,100);
		
		final ACOAlgorithm simulation = new ACOAlgorithm();
		simulation.defineParameters(parameters);
		
		simulation.performSimulation();
		
		int source = 0; // NOPMD by Andr� on 26/06/17 15:16
		int destination = 2; // NOPMD by Andr� on 26/06/17 15:16		
		int countWin = 0; // NOPMD by Andr� on 26/06/17 15:16
		int countLose = 0; // NOPMD by Andr� on 26/06/17 15:16
		
		final ACOAnt ant = new ACOAnt(source, destination);
		
		simulation.request(ant, source, destination);
		
		System.out.print("Melhor solu��o: "+ant.getTourUplink().get(0).getSourceNode()); // NOPMD by Andr� on 26/06/17 15:16
		for(final ACOOpticalLink j : ant.getTourUplink()){
			System.out.print(" "+j.getDestinationNode()); // NOPMD by Andr� on 26/06/17 15:16
		}
		
		System.out.println(); // NOPMD by Andr� on 26/06/17 15:16
		System.out.println("-----------------"); // NOPMD by Andr� on 26/06/17 15:16
		
		final List<ArrayList<ACOOpticalLink>> filteredsolution = new ArrayList<ArrayList<ACOOpticalLink>>();
		
		final List<ACOAnt> soluções = simulation.getDifferentRouteSolutions();
		
		for(final ACOAnt i : soluções){
			if(i.getCurrentNode() == destination){
				if(filteredsolution.isEmpty()){
					filteredsolution.add((ArrayList<ACOOpticalLink>) i.getTourUplink());
				}else{
					if(filteredsolution.contains(i.getTourUplink())){
						//System.out.println("ok! j� existe");
					}else{
						filteredsolution.add((ArrayList<ACOOpticalLink>) i.getTourUplink());
					}
				}
				countWin++;
			}else{
				countLose++;
			}
		}
		System.out.println();
		System.out.println("solu��es filtradas");
		for(List<ACOOpticalLink> j :filteredsolution){
			System.out.print("Rota: "+j.get(0).getSourceNode());
			for(ACOOpticalLink m : j){
				System.out.print(" "+m.getDestinationNode());
			}
			System.out.println();
		}
		
		
		System.out.println();
		System.out.println("Quantidades de vitorias: "+countWin + " Derrotas "+countLose);	
		
		
	}

}
