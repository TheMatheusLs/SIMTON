package MTHS.metrics;

import java.util.ArrayList;
import java.util.List;

import arquitecture.OpticalLink;
import call_request.CallRequest;
import exceptions.InvalidRoutesException;
import MTHS.AlgorithmNewSolution;
import parameters.SimulationParameters;
import routing.RoutingAlgorithmSolution;
import types.ModulationLevelType;
import utility.Function;
import utility.PertinenceFunction;

public class YENMetric {
    
    private double OSCR;
    private double OMSR;
    private double DNR;

	final SimulationParameters parameters = new SimulationParameters();
    private double CTR;

    private static final YENMetric metricInstance = new YENMetric();

    public static YENMetric getYENMetricInstance(){
		return metricInstance;
	}

    public double calculateCTRFragmentation(RoutingAlgorithmSolution route){

        
        return 0.0;
    }

    public double calculateCTRHxB(RoutingAlgorithmSolution route, CallRequest callRequest) throws InvalidRoutesException{

        SimulationParameters parameters = new SimulationParameters();

        int numHops = route.getNumHops();

        int bitRate = callRequest.getBitRate();

        int[] allBitRates = parameters.getBitRate();

        int bitRateIndex = -1;
        for (int i = 0; i < allBitRates.length; i++){
            if (allBitRates[i] == bitRate){
                bitRateIndex = i;
                break;
            }
        }

        int modulationConstelation = route.getModulationsTypeByBitrate().get(bitRateIndex).getConstelation();

        // Logb2 de modulationConstelation
        double ctr = (double)numHops * ((double)bitRate / (Math.log10(modulationConstelation) / Math.log10(2)));

        return ctr; 
    }

    public double calculateCTRDistance() throws InvalidRoutesException{
        return 1.0; 
    }
    
    public double calculateCTROcu(RoutingAlgorithmSolution route) throws InvalidRoutesException{
        SimulationParameters parameters = new SimulationParameters();
		
		int count = 0;
		double ocupacaomedia = 0.0;

		int[] slotsContiguosMask = new int[parameters.getNumberOfSlots()];
	
        for(OpticalLink link : route.getUpLink()){				
            //Análise ocupação média e Slots Contiguos
            count = 0;
            for(int s=0;s<parameters.getNumberOfSlots();s++){
                if(!link.availableSlot(s)){
                    count++;
                    slotsContiguosMask[s] = 1;
                }
            }				
            ocupacaomedia += (double) count/parameters.getNumberOfSlots();
        }
			
        count = 0;
        for (int i = 0; i < slotsContiguosMask.length; i++) {
            if(slotsContiguosMask[i]==1)
                count++;
        }

        // Calcula e armazena o Ocupação
        return ((double)count)/parameters.getNumberOfSlots();
    }

    public double calculateCTRHops(RoutingAlgorithmSolution route) throws InvalidRoutesException{
        return route.getNumHops(); 
    }

    public double calculateCTRIRoute(RoutingAlgorithmSolution route) throws InvalidRoutesException{
        return route.getConflictRoute().size(); 
    }
}   