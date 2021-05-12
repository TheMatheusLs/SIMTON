package MTHS.metrics;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import arquitecture.OpticalLink;
import call_request.CallRequest;
import parameters.SimulationParameters;
import routing.RoutingAlgorithmSolution;
import utility.Apeture;
import utility.Function;

public class MSCLApeture {
    
    private double MetricX;
    private double MetricY;
    private double MetricZ;

	final SimulationParameters parameters = new SimulationParameters();
    private double CTR;

    private static final MSCLApeture metricInstance = new MSCLApeture();

    public static MSCLApeture getMSCLApetureInstance(){
		return metricInstance;
	}

    public double routeCapacity(RoutingAlgorithmSolution route, int reqSlotSize){

        SimulationParameters parameters = new SimulationParameters();

        // Encontrar todos os buracos para a rota principal.
        List<Apeture> allApertures = getApetures(route, 0, parameters.getNumberOfSlots() - 1);

        //Se FS não exite então Retorna um valor alto
        if (allApertures == null){
            return Double.MAX_VALUE / 2; //Divide por 2 para evitar bugs do desempate
        }

        // Cria uma lista dos possíveis tamanhos para a requisição (Reqs)
        int[] listNumReqs = { 1, 2, 3, 4, 6, 7, 8, 11 , 16};

        double minCtr = Double.MAX_VALUE / 2;
        // Verifica o buraco com a menor perda de Capacidade na rota
        for (Apeture apeture : allApertures){
            double Ct = 0.0;

            if (apeture.getSize() < reqSlotSize){
                continue;
            }

            int FS = apeture.getPosition();
            int SA = apeture.getSize();

            // ** Rota principal
            // Capacidade antes da alocação
            int Ca = 0;
            for (int nReq: listNumReqs){
                if (nReq > SA){
                    break;
                }

                Ca += (SA - nReq + 1); //Ca <= Calcula a quantidade de formas que é possível alocar uma requisição de tamanho nReq em SA;
            }
            int Cb = 0;
            for (int nReq: listNumReqs){
                if (nReq > (SA - reqSlotSize)){
                    break;
                }

                Cb += ((SA - reqSlotSize) - nReq + 1); //Ca <= Calcula a quantidade de formas que é possível alocar uma requisição de tamanho nReq em SA;
            }

            Ct = Ca - Cb; // Perda de capacidade na rota principal

            if (Ct < minCtr){
                minCtr = Ct;
            }

        }

        return minCtr;
    }

    public List<Apeture> getApetures(RoutingAlgorithmSolution route, int initSlotToSearch, int finalSlotToSearch){

        Function function = new Function();

        List<Apeture> allApertures = new ArrayList<Apeture>();

        int emptySlots = initSlotToSearch;

		INDEX_SLOT:for(int indexSlot = emptySlots; indexSlot <= finalSlotToSearch; indexSlot++){
            if (indexSlot == -1){
                System.out.println("OK");
            }
            if (route.getSlotValue(indexSlot) > 0){
                continue INDEX_SLOT;
            }

            int countEmptySlots = 0;

			// Para cada slot necessário para alocar a requisição;
			EMPTY_SLOTS:for (emptySlots = indexSlot; emptySlots <= finalSlotToSearch; emptySlots++){

				if (route.getSlotValue(emptySlots) > 0){
                    break EMPTY_SLOTS;
                }

				countEmptySlots++;
			}

            allApertures.add(new Apeture(indexSlot, countEmptySlots));

            indexSlot = emptySlots;
        }

		return allApertures;
    }

    public void calculate(RoutingAlgorithmSolution route, CallRequest callRequest){

        int reqSlotSize = callRequest.getRequiredNumberOfSlots();

        this.MetricX = routeCapacity(route, reqSlotSize);

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

    public double calculateCTR(double[] coeffB){

        return getX();
    }
}   
