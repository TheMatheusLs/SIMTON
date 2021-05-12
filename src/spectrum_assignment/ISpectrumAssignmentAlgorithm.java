package spectrum_assignment;

import java.util.List;

import arquitecture.OpticalLink;
import exceptions.InvalidNumberOfFrequencySlotsException;
import exceptions.InvalidRoutesException;
/**
 * Descreve a interface dos algoritmos de alocação de espectro.
 * @author André 
 */
public interface ISpectrumAssignmentAlgorithm {	
	/**
	 * Encontra os n slots de frequência contíguos e contínuos na solução encontrada. 
	 * @param numberOfSlots 
	 * @param reqNumbOfSlots 
	 * @param solution 
	 * @return A lista de slots disponíveis.
	 * @author André
	 */		
	List<Integer> findFrequencySlots(int numberOfSlots, int reqNumbOfSlots, List<OpticalLink> uplink, List<OpticalLink> downlink) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException;

}
