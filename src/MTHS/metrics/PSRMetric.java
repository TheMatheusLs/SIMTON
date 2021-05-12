package MTHS.metrics;

import java.io.FileWriter;
import java.io.PrintWriter;

import arquitecture.OpticalLink;
import call_request.CallRequest;
import parameters.SimulationParameters;
import routing.RoutingAlgorithmSolution;
import utility.Function;

public class PSRMetric {
    
    private double MetricX;
    private double MetricY;
    private double MetricZ;

	final SimulationParameters parameters = new SimulationParameters();
    private double CTR;

    private static final PSRMetric metricInstance = new PSRMetric();

    public static PSRMetric getPSRMetricInstance(){
		return metricInstance;
	}

    public void calculateOHBI(RoutingAlgorithmSolution route, int maxHopsRoute, int maxIRoutes, double maxHopsxBanda, CallRequest callRequest){
        SimulationParameters parameters = new SimulationParameters();
		
		int count = 0;

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
        }
			
        count = 0;
        for (int i = 0; i < slotsContiguosMask.length; i++) {
            if(slotsContiguosMask[i]==1)
                count++;
        }

        // Calcula e armazena o Ocupação
        this.MetricX = ((double)count)/parameters.getNumberOfSlots();

        // Calcula e armazena hops x banda
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

        this.MetricY = ((double)(numHops * (bitRate / Math.sqrt(modulationConstelation)))) / maxHopsxBanda;

        this.MetricZ = ((double)route.getConflictRoute().size()) / maxIRoutes;
    }

    public void calculateOHB(RoutingAlgorithmSolution route, int maxHopsRoute, double maxHopsxBanda, CallRequest callRequest){
        SimulationParameters parameters = new SimulationParameters();
		
		int count = 0;

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
        }
			
        count = 0;
        for (int i = 0; i < slotsContiguosMask.length; i++) {
            if(slotsContiguosMask[i]==1)
                count++;
        }

        // Calcula e armazena o Ocupação
        this.MetricX = ((double)count)/parameters.getNumberOfSlots();

        // Calcula e armazena hops x banda
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

        this.MetricY = ((double)(numHops * (bitRate / Math.sqrt(modulationConstelation)))) / maxHopsxBanda;
    }

    public void calculateOI(RoutingAlgorithmSolution route, int maxIRoutes, CallRequest callRequest){
        SimulationParameters parameters = new SimulationParameters();
		
		int count = 0;

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
        }
			
        count = 0;
        for (int i = 0; i < slotsContiguosMask.length; i++) {
            if(slotsContiguosMask[i]==1)
                count++;
        }

        // Calcula e armazena o Ocupação
        this.MetricX = ((double)count)/parameters.getNumberOfSlots();

        this.MetricY = ((double)route.getConflictRoute().size()) / maxIRoutes;
    }

    public void calculateOD(RoutingAlgorithmSolution route, double maxDistRoute, int  maxHopsRoute, CallRequest callRequest){

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
        this.MetricX = ((double)count)/parameters.getNumberOfSlots();

        // Calcula e armazena hops x banda
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

        this.MetricY = numHops * (bitRate / Math.sqrt(modulationConstelation));

        // Número de 
        
        // Calcula e armazena o OSMR
        //this.OMSR = ocupacaomedia/route.getUpLink().size();

        // Function function = new Function();

        // final int numberOfSlots = parameters.getNumberOfSlots();                // Número total de Slots
		// final int numberOfReqSlots = callRequest.getRequiredNumberOfSlots();    // Número de Slots para a alocação

        // //Calcula o número de formas possíveis para alocar a requisição em todos os links da rota
        // final int waysFree = function.getWaysToAllocate(numberOfSlots, numberOfReqSlots, route.getUpLink(), route.getDownLink());
        // //Dividindo pelo total de slots possíveis e invertendo o valor para que valores baixos sejam melhores.
        // this.OSCR = ((double)((numberOfSlots - numberOfReqSlots + 1) - waysFree) / (numberOfSlots - numberOfReqSlots + 1));
        
        // Calcula e armazena o DNR
        this.MetricY = route.getSizeInBound()/maxDistRoute;

        this.MetricZ = route.getNumHops()/maxHopsRoute; 
        
        // int[] listWaysFree = new int[9];
        // int[] listOfReqs = {1, 2, 3, 4, 6, 7, 8, 11, 16};
        
        // int sumWays = 0;
        
        // for (int r = 0; r < listOfReqs.length; r++){
        //     listWaysFree[r] = function.getWaysToAllocate(numberOfSlots, listOfReqs[r], route.getUpLink(), route.getDownLink());
        // }
        // for (int r = 0; r < listOfReqs.length; r++){
        //     sumWays += listWaysFree[r];
        // }
        // this.OSCR = ((double)((numberOfSlots - numberOfReqSlots + 1) * 9 - sumWays) / ((numberOfSlots - numberOfReqSlots + 1) * 9));
        
        // String dataLog = String.format("%d,%f,%f,%f,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d",callRequest.getReqID(),OSCR, OMSR, DNR, callRequest.getRequiredNumberOfSlots(), sumWays, listWaysFree[0], listWaysFree[1], listWaysFree[2], listWaysFree[3], listWaysFree[4], listWaysFree[5], listWaysFree[6], listWaysFree[7], listWaysFree[8]);

        // try {

        //     final FileWriter file = new FileWriter("D:\\ProgrammingFiles\\ReportsSIMTON\\logMetrics.csv", true); 
        //     final PrintWriter saveFile2 = new PrintWriter(file);

        //     saveFile2.printf(dataLog + "\n");
        //     saveFile2.close();
            
        // } catch (Exception e) {
        //     //TODO: handle exception
        // }
    }

    public double getX() {
        return MetricX;
    }

    public double getY() {
        return MetricY;
    }

    public double getZ() {
        return MetricZ;
    }

    public double calculateCTR3(double[] coeffB, CallRequest callRequest){

        double ctr = 0;

        int N = 9; 
        int N2 = 3;

        double X = getX();
        double Y = getY();
        double Z = getZ();

        for (int s1 = 0; s1 < N2; s1++){
            for (int s2 = 0; s2 < N2; s2++){
                for (int s3 = 0; s3 < N2; s3++){
                    ctr += coeffB[s1 * N + s2 * N2 + s3] * Math.pow(X, s1) * Math.pow(Y, s2) * Math.pow(Z, s3);   
                }  
            }
        }

        return ctr;
    }

    public double calculateCTR2(double[] coeffB, CallRequest callRequest){

        double ctr = 0;

        int N = 3;

        double X = getX();
        double Y = getY();

        for (int s1 = 0; s1 < N; s1++){
            for (int s2 = 0; s2 < N; s2++){
                ctr += coeffB[s1 * N + s2] * Math.pow(X, s1) * Math.pow(Y, s2);   
            }
        }

        return ctr;


    }
}   
