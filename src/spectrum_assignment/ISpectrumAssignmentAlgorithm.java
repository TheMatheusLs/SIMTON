package spectrum_assignment;

import java.util.List;

import arquitecture.OpticalLink;
import exceptions.InvalidNumberOfFrequencySlotsException;
import exceptions.InvalidRoutesException;
/**
 * Descreve a interface dos algoritmos de aloca��o de espectro.
 * @author Andr� 
 */
public interface ISpectrumAssignmentAlgorithm {	
	/**
	 * Encontra os n slots de frequ�ncia cont�guos e cont�nuos na solu��o encontrada. 
	 * @param numberOfSlots 
	 * @param reqNumbOfSlots 
	 * @param solution 
	 * @return A lista de slots dispon�veis.
	 * @author Andr�
	 */		
	List<Integer> findFrequencySlots(int numberOfSlots, int reqNumbOfSlots, List<OpticalLink> uplink, List<OpticalLink> downlink) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException;

}
