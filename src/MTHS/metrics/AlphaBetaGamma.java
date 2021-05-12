package MTHS.metrics;

import java.io.FileWriter;
import java.io.PrintWriter;

import arquitecture.OpticalLink;
import call_request.CallRequest;
import MTHS.AlgorithmNewSolution;
import parameters.SimulationParameters;
import routing.RoutingAlgorithmSolution;
import utility.PertinenceFunction;

public class AlphaBetaGamma {
    
    private double OSCR;
    private double OMSR;
    private double DNR;

	final SimulationParameters parameters = new SimulationParameters();
    private double CTR;

    // Coeficientes para o especialistas
    private double[] indicesOSCR ={0.1,0.3,0.2,0.4,0.6,0.8,0.7,0.9};
    private double[] indicesOSMR ={0.1,0.2,0.1,0.3,0.5,0.7,0.6,0.9};
    private double[] indicesDNR ={0.1,0.2,0.1,0.4,0.6,0.9,0.8,0.9};

    private static final AlphaBetaGamma metricInstance = new AlphaBetaGamma();

    public static AlphaBetaGamma getAlphaBetaGammaInstance(){
		return metricInstance;
	}

    public void calculate(RoutingAlgorithmSolution route, double maxDistRoute){

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

        // Calcula e armazena o OSCR
        this.OSCR = ((double)count)/parameters.getNumberOfSlots();
        
        // Calcula e armazena o OSMR
        this.OMSR = ocupacaomedia/route.getUpLink().size();
        
        // Calcula e armazena o DNR
        this.DNR = route.getSizeInBound()/maxDistRoute;
    }

    public double getOSCR() {
        return OSCR;
    }

    public double getOMSR() {
        return OMSR;
    }

    public double getDNR() {
        return DNR;
    }

    public double calculateCTRbyRules(double[] coeffMetric, CallRequest callRequest, int KYEN){

        if (coeffMetric.length == 3){
            double alphaValue = coeffMetric[0];
            double betaValue = coeffMetric[1];
            double gammaValue = coeffMetric[2];

            double ctr = alphaValue * OSCR +  betaValue * OMSR + gammaValue * DNR;

            return ctr;

        } else {
            assert(coeffMetric.length == 3);
        }

        return Double.MAX_VALUE;
    }
}   
