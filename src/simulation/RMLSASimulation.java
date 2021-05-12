package simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import MTHS.AlgorithmRoutes;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import call_request.CallRequest;
import call_request.CallRequestList;
import exceptions.InvalidListOfCoefficientsException;
import exceptions.InvalidNodeIdException;
import exceptions.InvalidNumberOfFrequencySlotsException;
import exceptions.InvalidRoutesException;
import MTHS.AlgorithmRoutes;
import MTHS.metrics.MSCLApeture;
import parameters.SimulationParameters;
import report_save.CreateFolder;
import routing.AntColonyBasedRoutingAlgorithm;
import routing.CongestedAwareShortestPath;
import routing.FuzzyAlgorithm;
import routing.FuzzyAlgorithmEON;
import routing.IAlternativeRoutingAlgorithm;
import routing.IEvolucionaryRoutingAlgorithm;
import routing.IRoutingAlgorithm;
import routing.MinimumHops;
import routing.PowerSeriesRouting;
import routing.RoutingAlgorithmSolution;
import routing.SCCShortestPath;
import routing.ShortestPath;
import routing.YenAlgorithm;
import spectrum_assignment.FirstFitAlgorithm;
import spectrum_assignment.ISpectrumAssignmentAlgorithm;
import types.CallRequestType;
import types.MetricMethodType;
import types.ModulationLevelType;
import types.RoutingAlgorithmType;
import utility.Apeture;
import utility.Debug;
import utility.Function;
/**
 * Descreve o motor de simula��o dos algoritmos de RMLSA.
 * @author Andr� 
 */
public class RMLSASimulation implements IRMLSASimulation { // NOPMD by Andr� on 13/06/17 13:18
	/**
	 * Inst�ncia da Classe.
	 * @author Andr� 			
	 */	
	private static final RMLSASimulation RMLSA_INSTANCE = new RMLSASimulation();
	
	public double simulation(OpticalLink[][] network, List<OpticalSwitch> listOfNodes, int numberOfCalls, RoutingAlgorithmType routAlgorType, double meanRateBetCall, ArrayList<List<RoutingAlgorithmSolution>> allRoutes, double[] listOfCoeffs, MetricMethodType metricLogicType, int KYEN , Debug debugClass) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException, InvalidListOfCoefficientsException, IOException{

		int source;
		int destination;
		int	numCallLackOfSpect = 0;
		int numCallUnacceptBer = 0; 
		double time = 0.0;

		final int numberOfNodes = listOfNodes.size();		
		final CallRequestList listOfCalls = new CallRequestList();
		final SimulationParameters parameters = new SimulationParameters();
		final double maxTime = parameters.getMaxTime();
		final double meanRateCallDur = parameters.getMeanRateOfCallsDuration();
		final Function function = new Function();
		final ModulationLevelType[] modulationLevel = parameters.getModulationLevelType();
		final double bZero = SimulationParameters.getSpacing();

		List<Integer> slots = new ArrayList<Integer>();

		String folderName = debugClass.getCreateFolder().getFolderName();

		Scanner histReqs = null;
		String[] reqLine = null;
		if (debugClass.getFolderToReadReqs() != null){
			final String mainFolder = debugClass.getCreateFolder().getMainFolderName();
			String filename = String.format("%s\\%s\\reqHist.txt", mainFolder, debugClass.getFolderToReadReqs());

			histReqs = new Scanner(new FileReader(filename));
		}

		int limitCallRequest = 0;
		for(int i=1;i<=numberOfCalls;i++){	
			
			if (histReqs != null && !histReqs.hasNextLine()){
				System.out.println("Erro, acabou as requisições");
				break;
			}

			if (debugClass.getFolderToReadReqs() != null){
				reqLine = histReqs.nextLine().split(" ");

				assert(i == Integer.parseInt(reqLine[0])):
				"erro";
			}

			boolean hasQoT = false; // NOPMD by Andr� on 13/06/17 11:31
			boolean hasSlots = false; // NOPMD by Andr� on 13/06/17 11:31

			slots.clear();

			// if (i % 1000 == 0){
			// 	System.out.println("req = " + i);
			// }

			if (debugClass.getFolderToReadReqs() == null){
				do{
					source = (int) Math.floor(Math.random()*numberOfNodes);				
					destination = (int) Math.floor(Math.random()*numberOfNodes);				
				}while(source == destination);	
			} else {
				source = Integer.parseInt(reqLine[1]);		
				destination = Integer.parseInt(reqLine[2]);		
			}
			
			listOfCalls.removeCallRequest(time);

			if(time>=maxTime){
				listOfCalls.resetTimeCall(time);
				time = 0.0;				
			}

			if (debugClass.getFolderToReadReqs() == null){
				time += function.exponencial(meanRateBetCall);
			} else {
				time = Double.parseDouble(reqLine[3]);		
			}
			
			final CallRequest callRequest = new CallRequest(i, parameters.getBitRate(), CallRequestType.BIDIRECTIONAL); // NOPMD by Andr� on 13/06/17 11:31

			callRequest.setSourceId(source);
			callRequest.setDestinationId(destination);
			callRequest.setReqID(i);

			if (debugClass.getFolderToReadReqs() == null){
				callRequest.setTime(time, meanRateCallDur);

				callRequest.sortBitRate();
			} else {
				callRequest.setDecayTime(Double.parseDouble(reqLine[4]));
				callRequest.setDuration(Double.parseDouble(reqLine[5]));
				
				callRequest.setBitRate(Integer.parseInt(reqLine[6]));
			}

			int bitRate = callRequest.getBitRate();
			double decayTime = callRequest.getDecayTime();
			double duration = callRequest.getDuration();

			// Captura as rotas para o par origem destino
			List<RoutingAlgorithmSolution> routeSolution = allRoutes.get(source * numberOfNodes + destination);

			// Se a algoritmo for CASP faz a busca da rota
			if (RoutingAlgorithmType.CASP == routAlgorType){
				final CongestedAwareShortestPath caspInstance = CongestedAwareShortestPath.getCASPInstance();
				routeSolution = caspInstance.findRoute(network, source, destination, listOfNodes);
			}//End for CASP

			
			// Indica que a busca deve iniciar na rota 0		
			debugClass.setrouteToFind(0);

			if (debugClass.isDebugReqON()){
				String textReq = String.format("%d %d %d %f %f %f %d", i, source, destination, time, decayTime, duration, bitRate);
				debugClass.getCreateFolder().writeFile("reqHist.txt", textReq);
			}
			
			POINTROUTE:for(RoutingAlgorithmSolution route : routeSolution){ // percorre todas as rotas encontradas pela routeSolution

				// Se o algoritmo de roteamento é fixo alternativo, ordena a melhor rota
				if (routAlgorType == RoutingAlgorithmType.ALTERNATIVE){
					
					if (metricLogicType.equals(MetricMethodType.ORIGINAL_ONE) ){ // Métrica do Artigo original para uma rota sem usar if
						final FuzzyAlgorithm fuzzyInstance = FuzzyAlgorithm.getFuzzyInstance();

						route = fuzzyInstance.findRoute(routeSolution, network, callRequest, listOfNodes, listOfCoeffs);

					} else if (metricLogicType.equals(MetricMethodType.ORIGINAL_KYEN) ){ //Métrica do Artigo original para K rotas
						final FuzzyAlgorithm fuzzyInstance = FuzzyAlgorithm.getFuzzyInstance();							
						route = fuzzyInstance.findRouteKYEN(routeSolution, network, callRequest, listOfNodes, listOfCoeffs).get(debugClass.getRouteToFind());

					} else if (metricLogicType.equals(MetricMethodType.ALPHA_BETA_GAMMA_ONE) || metricLogicType.equals(MetricMethodType.PSR_METRIC) || metricLogicType.equals(MetricMethodType.YEN) || metricLogicType.equals(MetricMethodType.SLOT_APERTURE) || metricLogicType.equals(MetricMethodType.MSCL_APETURE) || metricLogicType.equals(MetricMethodType.DANILO)){// //Método de Sugeno criado por Matheus usando novas regras de tabela verdade
							
						final AlgorithmRoutes MTHSInstance = AlgorithmRoutes.getInstance();
						route = MTHSInstance.sortRoutes(routeSolution, network, callRequest, listOfNodes, listOfCoeffs, metricLogicType, debugClass).get(debugClass.getRouteToFind()); //0
					}
				}// Alternativo

				// Se o algoritmo de roteamento é o SCCSP, encontra a melhor rota
				if (routAlgorType == RoutingAlgorithmType.SCCSP){
					final SCCShortestPath scspInstance = SCCShortestPath.getSCCSPInstance();
					route = scspInstance.findRoute(network, callRequest, listOfNodes);
				}

				double outBoundQot = 0.0;

				hasSlots = false; 
				hasQoT = false;

				int[] allBitRates = parameters.getBitRate();

				int bitRateIndex = -1;
				for (int b = 0; b <= allBitRates.length; b++){
					if (allBitRates[b] == callRequest.getBitRate()){
						bitRateIndex = b;
						break;
					}
				}
				callRequest.setModulationType(route.getModulationsTypeByBitrate().get(bitRateIndex));
								
				final double snrLinear = Math.pow(10, callRequest.getModulationType().getSNRIndB()/10);
				final double osnrLinear = (((double)callRequest.getBitRate()*1E9)/(2*bZero))*snrLinear;
				debugClass.setOSNRTh(osnrLinear);
				
				final int reqNumbOfSlots = function.calculateNumberOfSlots(callRequest.getModulationType(), callRequest);
				debugClass.setReqNumbOfSlots(reqNumbOfSlots);

				callRequest.setRequiredNumberOfSlots(reqNumbOfSlots);

				final ISpectrumAssignmentAlgorithm ffInstance = FirstFitAlgorithm.getFFInstance();
					
				slots = ffInstance.findFrequencySlots(parameters.getNumberOfSlots(), reqNumbOfSlots, route.getUpLink(), route.getDownLink());
				
				// VALIDA OS SLOTS ENCONTRADOS
				slots = isValidSlots(slots, route);

				if(!slots.isEmpty() && slots.size()==reqNumbOfSlots){	// NOPMD by Andr� on 13/06/17 13:12
					hasSlots = true;

					final double inBoundQot = function.evaluateOSNR(route.getUpLink(), 159); // slots.get(0)); // NOPMD by Andr� on 13/06/17 13:13
					
					debugClass.setOSNRInbond(inBoundQot);
					
					if(callRequest.getCallRequestType() == CallRequestType.BIDIRECTIONAL){
						outBoundQot = function.evaluateOSNR(route.getDownLink(), 159); // slots.get(0));	 // NOPMD by Andr� on 13/06/17 13:13
					}																
				
					if(inBoundQot>=osnrLinear && outBoundQot>=osnrLinear){
						hasQoT = true;						
					}
				
				}

				debugClass.setHasSlots(hasSlots);
				debugClass.setHasQoT(hasQoT);

				if(hasSlots && hasQoT){
					callRequest.setFrequencySlots(slots);
					callRequest.setRoute(route);

					// Incrementar os slots que estão sendo utilizados pelas rotas
					route.incrementSlotsOcupy(slots);

					callRequest.allocate(route.getUpLink(), route.getDownLink(), listOfNodes);
					listOfCalls.addCall(callRequest);

					debugClass.setRouteKYEN(route.getYENID());
					
					break POINTROUTE;
				}

				if (debugClass.isAlternativeKYEN()){
					debugClass.setrouteToFind(debugClass.getRouteToFind() + 1);

					if (routAlgorType == RoutingAlgorithmType.FUZZY){
						if (debugClass.getRouteToFind() == KYEN){
							break POINTROUTE;
						}
					}
				} else {
					break POINTROUTE;
				}
			}// Acaba o loop das rotas

			if(!hasSlots){ // NOPMD by Andr� on 14/06/17 12:55
				numCallLackOfSpect++; // NOPMD by Andr� on 13/06/17 13:13
			}else if(!hasQoT){
				numCallUnacceptBer++; // NOPMD by Andr� on 14/06/17 12:55
			}

			// Escreve as requisições
			if (debugClass.isDebugReqON() && debugClass.getNumSimulationID() == 0){
				boolean reqStatus = (hasSlots && hasQoT);
				String slotsString = slots.toString().replace(',', '-');
				String dataLog;

				if (reqStatus){
					List<Integer> routeInt = new ArrayList<Integer>();
					List<OpticalLink> listOfOpticalLink = callRequest.getOpticalLinks();
					int sizeUplink = listOfOpticalLink.size();
					for (int u = 0; u < sizeUplink;u++){
						if (u == sizeUplink - 1){
							routeInt.add(listOfOpticalLink.get(u).getSource());
							routeInt.add(listOfOpticalLink.get(u).getDestination());
							break;
						}
						routeInt.add(listOfOpticalLink.get(u).getSource());
					}
					String routeString = routeInt.toString().replace(',', '-');

					String logSimulationHeader = "";
					String OD = String.format("%d-%d", source, destination);
					dataLog = String.format("%d,%f,%d,%d,%s,%f,%f,%f,%d,%d,%s,%s,%d,%b,%f,%f,%d,%d,%b,%b,%d,%d",i,meanRateBetCall,source,destination,OD,callRequest.getDuration(),callRequest.getDecayTime(), time, callRequest.getBitRate(), callRequest.getRequiredNumberOfSlots(),routeString,slotsString,callRequest.getModulationType().getConstelation(), reqStatus, debugClass.getOSNRTh(),debugClass.getOSNRInbond(), numCallUnacceptBer, numCallLackOfSpect, hasSlots, hasQoT, debugClass.getRouteToFind(), debugClass.getRouteKYEN());

				}else{
					String OD = String.format("%d-%d", source, destination);
					dataLog = String.format("%d,%f,%d,%d,%s,%f,%f,%f,%d,%d,%s,%s,%d,%b,%f,%f,%d,%d,%b,%b,%d,%d",i,meanRateBetCall,source,destination,OD,-1.0, -1.0, time, callRequest.getBitRate(), -1, "null", slotsString, -1, reqStatus, debugClass.getOSNRTh(),debugClass.getOSNRInbond(), numCallUnacceptBer, numCallLackOfSpect, hasSlots, hasQoT, debugClass.getRouteToFind(), debugClass.getRouteKYEN());
				}

				debugClass.getCreateFolder().writeFile(debugClass.getLogReqSimulationFilename(), dataLog);
			}

			limitCallRequest = i;

			// Verfica se o número de erros máximo foi atingido
			if ((numCallLackOfSpect + numCallUnacceptBer) >= 1000){
				System.err.println(" Chegou no limite de 1000 erros!");
				break;
			}
		}// Acaba o loop das requisições

		listOfCalls.desallocateAllRequests(); // Remove todas as requisições alocadas

		// Verifica se todos os links estão limpos
		for (int s = 0; s < network.length;s++){
			for (int d = 0; d < network.length;d++){
				if (network[s][d] != null){
					for (int i = 0; i < parameters.getNumberOfSlots();i++){
						assert(network[s][d].getPowerA(i) == 0);
					}
				}
			}
		}

		// Verifica se todas as rotas estão limpas
		for (List<RoutingAlgorithmSolution> routes: allRoutes){
			if (routes != null){
				for (RoutingAlgorithmSolution route : routes){
					for (int i = 0; i < parameters.getNumberOfSlots();i++){
						assert(route.getSlotValue(i) == 0);
					}
				}
			}
		}

		listOfCalls.eraseCallList();

		if (limitCallRequest != 0){
			System.err.println("Erros Slots: " + numCallLackOfSpect + " Erros QoT: " + numCallUnacceptBer + " numReq: " + limitCallRequest);
			return (double)(numCallLackOfSpect+numCallUnacceptBer)/limitCallRequest;
		}
		return 0.0;
	}

	/**
	 * M�todo para retornar a inst�ncia da classe.
	 * @return O objeto RMLSA_INSTANCE
	 * @author Andr� 			
	 */			
	public static RMLSASimulation getRMLSASimulationInstance(){
		return RMLSA_INSTANCE;
	}

	public List<Integer> isValidSlots(List<Integer> slots, RoutingAlgorithmSolution route){
		Function function = new Function();

		POINT: for (int slot: slots){
			boolean availableSlot = true;
			boolean availableLastSlot = true;

			List<OpticalLink> uplink = route.getUpLink();
			List<OpticalLink> downlink = route.getDownLink();

			for(int f = 0; f < uplink.size(); f++){
				final OpticalLink opticallinkUp = uplink.get(f);
				final OpticalLink opticallinkDown = downlink.get(f);
				final boolean availSlotIn = function.avaliableSlotInOpticalLink(opticallinkUp, slot);
				final boolean availSlotOut = function.avaliableSlotInOpticalLink(opticallinkDown, slot);
				if(!availSlotIn || !availSlotOut){ //Analisa o primeiro slot dispon�vel da grade
					return new ArrayList<Integer>();
				}				
			}
		}

		return slots;
	}

	public void removeAllRequests(List<CallRequest> listOfCalls){

	}
}
