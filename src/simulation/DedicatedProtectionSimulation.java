package simulation;

import java.util.ArrayList;
import java.util.List;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import call_request.DPCallRequest;
import call_request.DPCallRequestList;
import exceptions.InvalidListOfCoefficientsException;
import exceptions.InvalidNodeIdException;
import exceptions.InvalidNumberOfFrequencySlotsException;
import exceptions.InvalidRoutesException;
import parameters.SimulationParameters;
import spectrum_assignment.FirstFitAlgorithm;
import spectrum_assignment.ISpectrumAssignmentAlgorithm;
import survivability.DedicatedProtectionAlgorithmSolution;
import survivability.IDedicatedProtectionAlgorithm;
import survivability.SuurballeAlgorithm;
import survivability.TwoStepsShortestPathAlgorithm;
import types.CallRequestType;
import types.DedicatedProtectionAlgorithmType;
import types.ModulationLevelType;
import utility.Function;
/**
 * Descreve o motor de simula��o dos algoritmos de DP.
 * @author Andr� 
 */
public class DedicatedProtectionSimulation implements IDedicatedProtectionSimulation { // NOPMD by Andr� on 16/06/17 10:04
	/**
	 * Inst�ncia da Classe.
	 * @author Andr� 			
	 */	
	private static final DedicatedProtectionSimulation DP_INSTANCE = new DedicatedProtectionSimulation();
	
	public double simulation(final OpticalLink[][] network, final List<OpticalSwitch> listOfNodes, final int numberOfCalls, final DedicatedProtectionAlgorithmType dedProtecType, // NOPMD by Andr� on 16/06/17 10:04
			final double meanRateBetCalls, final List<Double> listOfCoeff) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException,
			InvalidListOfCoefficientsException {
		
		
		int source;
		int destination;
		int	numOfCallLackOfSpec = 0; // NOPMD by Andr� on 14/06/17 15:46
		int numOfCallsUnacceptBer = 0; // NOPMD by Andr� on 14/06/17 15:46		
		final int numberOfNodes = listOfNodes.size();
		double time = 0.0;
		final DPCallRequestList listOfDPCalls = new DPCallRequestList();
		final SimulationParameters parameters = new SimulationParameters();
		final double maxTime = parameters.getMaxTime();
		final double meanRateCallDur = parameters.getMeanRateOfCallsDuration();
		final Function function = new Function();
		final ModulationLevelType[] modulationLevel = parameters.getModulationLevelType();
		final double bZero = SimulationParameters.getSpacing();
		DedicatedProtectionAlgorithmSolution solution = new DedicatedProtectionAlgorithmSolution();
		final List<DedicatedProtectionAlgorithmSolution> solutions = new ArrayList<DedicatedProtectionAlgorithmSolution>();
		List<Integer> slotsInWP = new ArrayList<Integer>();
		List<Integer> slotsInPP = new ArrayList<Integer>();
		
		for(int i=1;i<=numberOfCalls;i++){	
			
			boolean hasQoTInWP = false; // NOPMD by Andr� on 14/06/17 15:56
			boolean hasQoTInPP = false; // NOPMD by Andr� on 14/06/17 15:56
			boolean hasSlotsInWP = false; // NOPMD by Andr� on 14/06/17 15:56
			boolean hasSlotsInPP = false; // NOPMD by Andr� on 14/06/17 15:56
			solution.clear();
			solutions.clear();
			slotsInWP.clear();
			slotsInPP.clear();			
			
			
			do{
				source = (int) Math.floor(Math.random()*numberOfNodes);				
				destination = (int) Math.floor(Math.random()*numberOfNodes);				
			}while(source == destination);			
		
					
			listOfDPCalls.removeDPCall(time);
			
			if(time>=maxTime){
				listOfDPCalls.resetTimeDPCall(time);
				time = 0.0;				
			}
			
			time += function.exponencial(meanRateBetCalls);			
			
			
			final DPCallRequest call = new DPCallRequest(i,parameters.getBitRate(), CallRequestType.BIDIRECTIONAL); // NOPMD by Andr� on 16/06/17 09:27
			call.setSourceId(source);
			call.setDestinationId(destination);
			call.setTime(time, meanRateCallDur);
			call.sortBitRate();
						
			
			switch(dedProtecType.getCode()){ // NOPMD by Andr� on 16/06/17 09:23
			case 1:				
				final IDedicatedProtectionAlgorithm suurballeInstance = SuurballeAlgorithm.getSuurballeInstance();
				solution = suurballeInstance.findRoutes(network, call, listOfNodes);  // NOPMD by Andr� on 16/06/17 09:23
				break;				
			default:
					
				final IDedicatedProtectionAlgorithm tsspInstance = TwoStepsShortestPathAlgorithm.getTSSPInstance();
				solution = tsspInstance.findRoutes(network, call, listOfNodes);			 // NOPMD by Andr� on 16/06/17 09:23
				break;
			}
			
			//analyzing the working path		
			POINT: for(final ModulationLevelType modulLevelType : modulationLevel){					
					
				hasSlotsInWP = false; // NOPMD by Andr� on 16/06/17 09:31
				hasQoTInWP = false; // NOPMD by Andr� on 16/06/17 09:31
										
				final double snrLinear = Math.pow(10,modulLevelType.getSNRIndB()/10);
				final double osnrLinear = (((double)call.getBitRate()*1E9)/(2*bZero))*snrLinear; // NOPMD by Andr� on 26/06/17 12:11
				final int reqNumbOfSlots = function.calculateNumberOfSlots(modulLevelType, call);
				call.setRequiredNumberOfSlotsInWorkingPath(reqNumbOfSlots);
						
				final ISpectrumAssignmentAlgorithm ffInstance = FirstFitAlgorithm.getFFInstance();
				slotsInWP = ffInstance.findFrequencySlots(parameters.getNumberOfSlots(), reqNumbOfSlots,  // NOPMD by Andr� on 16/06/17 09:35
						solution.getWorkingPath().getUpLink(), solution.getWorkingPath().getDownLink()); // NOPMD by Andr� on 16/06/17 09:36
										

				if(!slotsInWP.isEmpty() && slotsInWP.size()==reqNumbOfSlots){					 // NOPMD by Andr� on 16/06/17 09:40
					hasSlotsInWP = true;							
					final double inBoundQot = function.evaluateOSNR(solution.getWorkingPath().getUpLink(), slotsInWP.get(0));  // NOPMD by Andr� on 16/06/17 09:40
					final double outBoundQot = function.evaluateOSNR(solution.getWorkingPath().getDownLink(), slotsInWP.get(0));	// NOPMD by Andr� on 16/06/17 09:40
							
					if(inBoundQot>=osnrLinear && outBoundQot>=osnrLinear){
						hasQoTInWP = true;						
					}
				}					
				
				if(hasSlotsInWP && hasQoTInWP){
						call.setFrequencySlotsInWorkingPath(slotsInWP);
						call.setModulationTypeInWorkingPath(modulLevelType);					
						break POINT;
					} 					
				}
			
				hasSlotsInPP = true; // NOPMD by Andr� on 16/06/17 09:43
				hasQoTInPP = true; // NOPMD by Andr� on 16/06/17 09:43
			
				
				if(hasSlotsInWP && hasQoTInWP){//analyzing the protection path.
				
					POINTA: for(final ModulationLevelType modulLevelType : modulationLevel){ 
						
						hasSlotsInPP = false; // NOPMD by Andr� on 16/06/17 09:43
						hasQoTInPP = false; // NOPMD by Andr� on 16/06/17 09:43
						
						final double snrLinear = Math.pow(10,modulLevelType.getSNRIndB()/10);
						final double osnrLinear = (((double)call.getBitRate()*1E9)/(2*bZero))*snrLinear; // NOPMD by Andr� on 16/06/17 09:43
												
						final int reqNumbOfSlots = function.calculateNumberOfSlots(modulLevelType, call);
						call.setRequiredNumberOfSlotsInProtectionPath(reqNumbOfSlots);
						
						final ISpectrumAssignmentAlgorithm ffInstance = FirstFitAlgorithm.getFFInstance();					
						slotsInPP = ffInstance.findFrequencySlots(parameters.getNumberOfSlots(), reqNumbOfSlots, solution.getProtectionPath().getUpLink(),solution.getProtectionPath().getDownLink()); // NOPMD by Andr� on 16/06/17 09:44
						
						if(!slotsInPP.isEmpty() && slotsInPP.size()==reqNumbOfSlots){	// NOPMD by Andr� on 16/06/17 09:45
							hasSlotsInPP = true;
							
							final double inBoundQot = function.evaluateOSNR(solution.getProtectionPath().getUpLink(), slotsInPP.get(0));  // NOPMD by Andr� on 16/06/17 09:45
							final double outBoundQot = function.evaluateOSNR(solution.getProtectionPath().getDownLink(), slotsInPP.get(0));	// NOPMD by Andr� on 16/06/17 09:45
							
							if(inBoundQot>=osnrLinear && outBoundQot>=osnrLinear){ // NOPMD by Andr� on 16/06/17 09:47
								hasQoTInPP = true;						
							}
						}				
						
						if(hasSlotsInPP && hasQoTInPP){
							call.setFrequencySlotsInProtectionPath(slotsInPP);
							call.setModulationTypeInProtectionPath(modulLevelType);
							call.allocate(solution.getWorkingPath(), solution.getProtectionPath(), listOfNodes); // NOPMD by Andr� on 16/06/17 09:47
							listOfDPCalls.addDPCall(call);
							break POINTA;
						} 		
					
					}
				
				}	
							
							
			
			if(!hasSlotsInWP || !hasSlotsInPP){ // NOPMD by Andr� on 16/06/17 10:03
				numOfCallLackOfSpec++; // NOPMD by Andr� on 16/06/17 10:03
			}else if(!hasQoTInWP || !hasQoTInPP){
					numOfCallsUnacceptBer++; // NOPMD by Andr� on 26/06/17 12:11
			}			
		}
		
		listOfDPCalls.eraseSurvivabilityCallList();	
		
		
		return (double)(numOfCallLackOfSpec+numOfCallsUnacceptBer)/numberOfCalls;
						
	}
	/**
	 * M�todo para retornar a inst�ncia da classe.
	 * @return O objeto DP_INSTANCE
	 * @author Andr� 			
	 */	
	public static DedicatedProtectionSimulation getDedicatedProtectionSimulationInstance(){
		return DP_INSTANCE;
	}

	

}
