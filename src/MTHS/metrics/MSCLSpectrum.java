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

public class MSCLSpectrum {

	final SimulationParameters parameters = new SimulationParameters();

    private double CTR;

    private static final MSCLSpectrum metricInstance = new MSCLSpectrum();

    public static MSCLSpectrum getMSCLSpectrumInstance(){
		return metricInstance;
	}

    public double calculate(RoutingAlgorithmSolution route, CallRequest callRequest){

        // Encontra o tamanho do Slot da requisição
        int bitRate = callRequest.getBitRate();

        CTR = routeMSCL(route, bitRate);

        return CTR;
    }

    public double routeMSCL(RoutingAlgorithmSolution route, int bitRate){

        int[] allBitRates = parameters.getBitRate();

        int bitRateIndex = -1;
        for (int i = 0; i < allBitRates.length; i++){
            if (allBitRates[i] == bitRate){
                bitRateIndex = i;
                break;
            }
        }

        int sizeSlotReq = route.getSizeSlotTypeByBitrate().get(bitRateIndex);

        // Encontrar todos os buracos para a rota principal.
        List<Apeture> allApertures = getApetures(route, 0, parameters.getNumberOfSlots() - 1);
        
        boolean isPossibleToAlocateReq = false;
        // Verifica se é possível alocar essa requisção dentro da rota principal (route)
        for (Apeture aperture : allApertures){
            if (aperture.getSize() >= sizeSlotReq){
                isPossibleToAlocateReq = true;
                break;
            }
        }
        
        if (!isPossibleToAlocateReq){
            //Se FS não exite então Retorna um valor alto
            return Double.MAX_VALUE / 2; //Divide por 2 para evitar bugs do desempate
        }
        

        double bestLostCapacity = Double.MAX_VALUE / 2;
        int bestSlotToCapacity = -1;
        // Percorre os slots possíveis.
        for (Apeture aperture: allApertures){
            POINT_SLOT:for (int indexSlot = aperture.getPosition(); indexSlot < aperture.getPosition() + aperture.getSize(); indexSlot++){

                int startSlot = indexSlot;
                int finalSlot = indexSlot + sizeSlotReq - 1;

                if (finalSlot >= parameters.getNumberOfSlots()){
                    break;
                }

                // Verifica se é possível alocar nessa posição
                POINT_TEST:for (int s = startSlot; s <= finalSlot;s++){
                    if (route.getSlotValue(s) > 0){
                        continue POINT_SLOT;
                    }
                    else {
                        continue POINT_TEST;
                    }
                }

                // Chegando aqui é possível fazer a alocação e começa o cálculo de capacidade para o slot
                // Cria a lista dos slots para a requisição
                List<Integer> slotsReqFake = new ArrayList<Integer>();
                for (int s = startSlot; s <= finalSlot; s++){
                    slotsReqFake.add(s);
                }
            
                double lostCapacityTotal = 0.0;
        
                // *** Rota principal
                // Cálculo da capacidade antes da alocação na rota principal
                double capacityBeforeRoute = 0.0;

                List<Integer> possibleSlotsByRoute = route.getSizeSlotTypeByBitrate();
                for (int possibleReqSize: possibleSlotsByRoute){
                    if (possibleReqSize > aperture.getSize()){
                        break;
                    }
                    capacityBeforeRoute += (aperture.getSize() - possibleReqSize + 1);
                }
                
                // Aloca a requisição fake
                route.incrementSlotsOcupy(slotsReqFake);

                //Cálculo da capacidade depois da alocação na rota principal
                //Encontra os buracos formados
                List<Apeture> apertureMainroute = getApetures(route, aperture.getPosition(), aperture.getPosition() + aperture.getSize() - 1);

                double capacityAfterRoute = 0.0;
                for (Apeture apetureInApeture : apertureMainroute) {
                    for (int possibleReqSize: possibleSlotsByRoute){
                        if (possibleReqSize > apetureInApeture.getSize()){
                            break;
                        }
                        capacityAfterRoute += (apetureInApeture.getSize() - possibleReqSize + 1);
                    }
                }

                route.decreasesSlotsOcupy(slotsReqFake);

                // Calcula a perda de capacidade na rota principal
                lostCapacityTotal += capacityBeforeRoute - capacityAfterRoute;

                for (RoutingAlgorithmSolution iRoute: route.getConflictRoute()){

                    // Busca o mínimo a esquerda
                    int minSlot = findSlotLeft(iRoute, startSlot);
                    
                    // Busca o máximo a direita
                    int maxSlot = findSlotRight(iRoute, finalSlot);
                    if (minSlot == -1){
                        System.out.println("Erro -1");
                    }
                    List<Apeture> apetureAfectInIRoute = getApetures(iRoute, minSlot, maxSlot);

                    possibleSlotsByRoute = iRoute.getSizeSlotTypeByBitrate();
                    
                    capacityBeforeRoute = 0.0;
                    
                    // *** Rota interferente
                    // Cálculo da capacidade antes da alocação na rota inteferente
                    for (Apeture apetureIRoute : apetureAfectInIRoute) {
                        for (int possibleReqSize: possibleSlotsByRoute){
                            if (possibleReqSize > apetureIRoute.getSize()){
                                break;
                            }
                            capacityBeforeRoute += (apetureIRoute.getSize() - possibleReqSize + 1);
                        }
                    }

                    // Aloca a requisição fake
                    iRoute.incrementSlotsOcupy(slotsReqFake);

                    //Cálculo da capacidade depois da alocação na rota inteferente
                    //Encontra os buracos formados
                    apertureMainroute = getApetures(iRoute, minSlot, maxSlot);

                    capacityAfterRoute = 0.0;
                    for (Apeture apetureInApeture : apertureMainroute) {
                        for (int possibleReqSize: possibleSlotsByRoute){
                            if (possibleReqSize > apetureInApeture.getSize()){
                                break;
                            }
                            capacityAfterRoute += (apetureInApeture.getSize() - possibleReqSize + 1);
                        }
                    }

                    iRoute.decreasesSlotsOcupy(slotsReqFake);
                    if (capacityBeforeRoute < capacityAfterRoute){
                        System.out.println("Menor");
                    }

                    lostCapacityTotal += capacityBeforeRoute - capacityAfterRoute;
                    
                }

                if (lostCapacityTotal < 0){
                    System.out.println("Perda de capacidade negativa");
                }
                if (lostCapacityTotal < bestLostCapacity){
                    bestLostCapacity = lostCapacityTotal;
                    bestSlotToCapacity = indexSlot;
                }
            }
        }
        List<Integer> slotsReq = new ArrayList<Integer>();
        for (int s = bestSlotToCapacity; s <= bestSlotToCapacity + sizeSlotReq - 1; s++){
            slotsReq.add(s);
        }

        route.setSlotsByMSCL(slotsReq);

        return bestLostCapacity;
    }

    public int findSlotLeft(RoutingAlgorithmSolution route, int initSlot){
        // Busca o mínimo a esquerda
        int minSlot = initSlot; // Força um erro
        for (int min = initSlot; min >= 0;min--){
            if (route.getSlotValue(min) == 0){
                minSlot = min;
            } else {
                break;
            }
        }
        assert(minSlot == -1): "Erro";
        return minSlot;
    }

    public int findSlotRight(RoutingAlgorithmSolution route, int initSlot){
        // Busca o máximo a direita
        int maxSlot = initSlot; // Força um erro
        for (int max = initSlot; max < parameters.getNumberOfSlots(); max++){
            if (route.getSlotValue(max) == 0){
                maxSlot = max;
            } else {
                break;
            }
        }
        assert(maxSlot == parameters.getNumberOfSlots() + 1): "Erro";
        return maxSlot;
    }

    public List<Apeture> getApetures(RoutingAlgorithmSolution route, int initSlotToSearch, int finalSlotToSearch){

        Function function = new Function();

        List<Apeture> allApertures = new ArrayList<Apeture>();

        int emptySlots = initSlotToSearch;
        assert(emptySlots == -1): "ERRO -1 ";

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
}   
