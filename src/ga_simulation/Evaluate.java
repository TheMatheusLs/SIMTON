package ga_simulation;

import java.util.ArrayList;
import java.util.List;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import call_request.CallRequest;
import call_request.CallRequestList;
import exceptions.InvalidNodeIdException;
import exceptions.InvalidNumberOfFrequencySlotsException;
import exceptions.InvalidRoutesException;
import parameters.SimulationParameters;
import routing.RoutingAlgorithmSolution;
import spectrum_assignment.FirstFitAlgorithm;
import spectrum_assignment.ISpectrumAssignmentAlgorithm;
import types.CallRequestType;
import types.ModulationLevelType;
import utility.Function;

/**
 * Descreve o motor de simula��o usado para calcular
 * o fitness dos indiv�duos no algoritmo GA.
 * @author Andr� 
 */
public class Evaluate { // NOPMD by Andr� on 10/07/17 19:13
	
	/**
	 * Inst�ncia da Classe.
	 * @author Andr� 			
	 */	
	private static final Evaluate EVA_INSTANCE = new Evaluate();
	
	public double simulation(final OpticalLink[][] network, final List<OpticalSwitch> listOfNodes, final int numberOfCalls, final double meanRateBetCall, final RoutingAlgorithmSolution[][] solutions) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException{ // NOPMD by Andr� on 13/06/17 13:14
		
		
		int source;
		int destination;
		int	numCallLackOfSpect = 0; // NOPMD by Andr� on 13/06/17 11:30
		int numCallUnacceptBer = 0; // NOPMD by Andr� on 13/06/17 11:30
		final int numberOfNodes = listOfNodes.size();		
		double time = 0.0;
		final CallRequestList listOfCalls = new CallRequestList();
		final SimulationParameters parameters = new SimulationParameters();
		final double maxTime = parameters.getMaxTime();
		final double meanRateCallDur = parameters.getMeanRateOfCallsDuration();
		final Function function = new Function();
		final ModulationLevelType[] modulationLevel = parameters.getModulationLevelType();
		final double bZero = SimulationParameters.getSpacing();
		RoutingAlgorithmSolution solution;
		List<Integer> slots = new ArrayList<Integer>();
		
		
		
				
		for(int i=1;i<=numberOfCalls;i++){		
			
			boolean hasQoT = false; // NOPMD by Andr� on 13/06/17 11:31
			boolean hasSlots =false; // NOPMD by Andr� on 13/06/17 11:31
			slots.clear();
								
			do{
				source = (int) Math.floor(Math.random()*numberOfNodes);				
				destination = (int) Math.floor(Math.random()*numberOfNodes);				
			}while(source == destination);	
			
			listOfCalls.removeCallRequest(time);
					
			if(time>=maxTime){
				listOfCalls.resetTimeCall(time);
				time = 0.0;				
			}
			
			
			time += function.exponencial(meanRateBetCall);			
			
			
			final CallRequest callRequest = new CallRequest(i,parameters.getBitRate(), CallRequestType.BIDIRECTIONAL); // NOPMD by Andr� on 13/06/17 11:31
			callRequest.setSourceId(source);
			callRequest.setDestinationId(destination);
			callRequest.setTime(time, meanRateCallDur);
			callRequest.sortBitRate();
									
			solution = solutions[source][destination]; // NOPMD by Andr� on 10/07/17 19:13		
			
				
			POINT: for(final ModulationLevelType r : modulationLevel){ 
				
				double outBoundQot = 0.0; // NOPMD by Andr� on 13/06/17 11:58
				hasSlots = false; // NOPMD by Andr� on 13/06/17 11:58
				hasQoT = false; // NOPMD by Andr� on 13/06/17 11:58
									
				final double snrLinear = Math.pow(10,r.getSNRIndB()/10);
				final double osnrLinear = (((double)callRequest.getBitRate()*1E9)/(2*bZero))*snrLinear; // NOPMD by Andr� on 10/07/17 19:13
					
				final int reqNumbOfSlots = function.calculateNumberOfSlots(r, callRequest);
					
				callRequest.setRequiredNumberOfSlots(reqNumbOfSlots);
					
				final ISpectrumAssignmentAlgorithm ffInstance = FirstFitAlgorithm.getFFInstance();
				
				slots = ffInstance.findFrequencySlots(parameters.getNumberOfSlots(), reqNumbOfSlots, solution.getUpLink(), solution.getDownLink()); // NOPMD by Andr� on 13/06/17 12:47
				
				if(!slots.isEmpty() && slots.size()==reqNumbOfSlots){	// NOPMD by Andr� on 13/06/17 13:00
						hasSlots = true;
						final double inBoundQot = function.evaluateOSNR(solution.getUpLink(), slots.get(0)); // NOPMD by Andr� on 13/06/17 13:00
						if(callRequest.getCallRequestType() == CallRequestType.BIDIRECTIONAL){
							outBoundQot = function.evaluateOSNR(solution.getDownLink(), slots.get(0));	 // NOPMD by Andr� on 13/06/17 13:07
						}
						
						if(inBoundQot>=osnrLinear && outBoundQot>=osnrLinear){
							hasQoT = true;						
						}
				}				
					
				if(hasSlots && hasQoT){
					callRequest.setFrequencySlots(slots);
					callRequest.setModulationType(r);
					callRequest.allocate(solution.getUpLink(), solution.getDownLink(), listOfNodes); // NOPMD by Andr� on 13/06/17 13:08
					listOfCalls.addCall(callRequest);
					break POINT;
				}				
			}					
			
			if(!hasSlots){ // NOPMD by Andr� on 14/06/17 12:55
				numCallLackOfSpect++; // NOPMD by Andr� on 13/06/17 13:13
			}else if(!hasQoT){
				numCallUnacceptBer++; // NOPMD by Andr� on 14/06/17 12:55
			}
					
			
		}
		
		listOfCalls.eraseCallList();
		
		return (double)(numCallLackOfSpect+numCallUnacceptBer)/numberOfCalls;
					
	}
	/**
	 * M�todo para retornar a inst�ncia da classe.
	 * @return O objeto RMLSA_INSTANCE
	 * @author Andr� 			
	 */			
	public static Evaluate getEvaluateInstance(){
		return EVA_INSTANCE;
	}


}
