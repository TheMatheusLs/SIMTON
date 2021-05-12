package simulation;

import java.util.List;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import exceptions.InvalidListOfCoefficientsException;
import exceptions.InvalidNodeIdException;
import exceptions.InvalidNumberOfFrequencySlotsException;
import exceptions.InvalidRoutesException;
import types.DedicatedProtectionAlgorithmType;
/**
 * Descreve a interface do motor de simulação RMLSA.
 * @author André 
 */
public interface IDedicatedProtectionSimulation {	
	/**
	 * Encontra a probabilidade de bloqueio. 
	 * @param network 
	 * @param listOfNodes
	 * @param dProtType 
	 * @param meanRateBetCalls
	 * @return Probabilidade de bloqueio da rede. 
	 */		
	double simulation(OpticalLink[][] network, List<OpticalSwitch> listOfNodes, int numberOfCalls, DedicatedProtectionAlgorithmType dProtType, double meanRateBetCalls, List<Double> listOfCoeff) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException, InvalidListOfCoefficientsException;

}
