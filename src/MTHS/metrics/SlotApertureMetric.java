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

public class SlotApertureMetric {

    private double MetricX;
    private double MetricY;
    private double MetricZ;

	final SimulationParameters parameters = new SimulationParameters();
    private double CTR;

    private static final SlotApertureMetric metricInstance = new SlotApertureMetric();

    public static SlotApertureMetric getSlotApertureMetricInstance(){
		return metricInstance;
	}

    public double routeCapacity(RoutingAlgorithmSolution route, int reqSlotSize){

        //Testes
        // reqSlotSize = 3;
        // route.incrementSlots(0);
        // route.incrementSlots(1);
        // route.incrementSlots(6);
        // route.incrementSlots(10);

        //Encontra o primeiro slot disponível (FS) e o tamanho do buraco (SA) para alocar a requisição de tamanho routeReq em route.
        Apeture firstApeture = getFirstAperture(route, reqSlotSize);

        //Se FS não exite então Retorna um valor alto
        if (firstApeture == null){
            return Double.MAX_VALUE / 2; //Divide por 2 para evitar bugs do desempate
        }

        int FS = firstApeture.getPosition();
        int SA = firstApeture.getSize();

        // Cria uma lista dos possíveis tamanhos para a requisição (Reqs)
        int[] listNumReqs = { 1, 2, 3, 4, 6, 7, 8, 11 , 16};

        double Ct = 0.0;

        // Cria a lista dos slots para a requisição
        List<Integer> slotsReqFake = new ArrayList<Integer>();
        for (int s = FS; s < FS + reqSlotSize; s++){
            slotsReqFake.add(s);
        }

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
            if (nReq > (SA - nReq)){
                break;
            }

            Cb += ((SA - nReq) - nReq + 1); //Ca <= Calcula a quantidade de formas que é possível alocar uma requisição de tamanho nReq em SA;
        }

        Ct += Ca - Cb; // Perda de capacidade na rota principal

        int nIRoute = 0;
        final int maxNIRoute = 20;
        // ** Rotas interferentes
        for (RoutingAlgorithmSolution iRoute: route.getConflictRoute()){

            // Interferente 1
            // iRoute.incrementSlots(0);
            // iRoute.incrementSlots(1);
            // iRoute.incrementSlots(5);
            // iRoute.incrementSlots(6);
            // Interferente 2
            // iRoute.incrementSlots(3);
            // iRoute.incrementSlots(7);
            // iRoute.incrementSlots(8);
            // iRoute.incrementSlots(9);

            // Busca o mínimo a esquerda
            int minSlot = 0; // Força um erro
            for (int min = FS; min >= 0;min--){
                if (min == -1){
                    System.out.println("OK");
                }
                if (iRoute.getSlotValue(min) == 0){
                    minSlot = min;
                } else {
                    break;
                }

            }
            // Busca o máximo a direita
            int maxSlot = parameters.getNumberOfSlots() - 1; // Força um erro
            for (int max = (FS + reqSlotSize - 1); max < parameters.getNumberOfSlots(); max++){
                if (iRoute.getSlotValue(max) == 0){
                    maxSlot = max;
                } else {
                    break;
                }

            }

            // LA <= Lista todos os buracos que têm ao menos um slot da requisição com inicio em FS e indo até (FS + routeReq);
            if (minSlot == -1 || maxSlot == parameters.getNumberOfSlots() + 1){
                System.out.println("OK");
            }
            List<Apeture> LA = getApetures(iRoute, minSlot, maxSlot);

            for (Apeture apeture : LA) {
                // Capacidade antes da alocação
                Ca = 0;
                for (int nReq: listNumReqs){
                    if (nReq > apeture.getSize()){
                        break;
                    }

                    Ca += (apeture.getSize() - nReq + 1); //Ca <= Calcula a quantidade de formas que é possível alocar uma requisição de tamanho nReq em SA;
                }

                // Aloca a requisição fake
                iRoute.incrementSlotsOcupy(slotsReqFake);

                // Calcula o novo tamanho do buraco
                int newApetureSize = 0; // Força um erro
                for (int size = apeture.getPosition(); size < apeture.getPosition() + apeture.getSize(); size++){
                    if (size == -1){
                        System.out.println("OK");
                    }
                    if (iRoute.getSlotValue(size) == 0){
                        newApetureSize++;
                    }
                    // Aqui pode ser otimizado, conhecendo se é esquerda ou direita
                }

                Cb = 0;
                for (int nReq: listNumReqs){
                    if (nReq > newApetureSize){
                        break;
                    }

                    Cb += (newApetureSize - nReq + 1); //Ca <= Calcula a quantidade de formas que é possível alocar uma requisição de tamanho nReq em SA;
                }

                // Desaloca a requisição fake
                iRoute.decreasesSlotsOcupy(slotsReqFake);

                Ct += Ca - Cb; // Perda de capacidade na rota interferente

            }
            // if (maxNIRoute == nIRoute){
            //     break;
            // }
            // nIRoute++;
        }

        return Ct;
    }

    public double routeCapacityNew(RoutingAlgorithmSolution route, int reqSlotSize){

        Apeture firstApeture = getFirstAperture(route, reqSlotSize); // Encontra o primeiro slot capaz de alocar a requisição

        //Se FS não exite então Retorna um valor alto
        if (firstApeture == null){
            return Double.MAX_VALUE / 2; //Divide por 2 para evitar bugs do desempate
        }

        int FS = firstApeture.getPosition();
        int SA = firstApeture.getSize();

        // Cria uma lista dos possíveis tamanhos para a requisição (Reqs)
        int[] listNumReqs = { 1, 2, 3, 4, 6, 7, 8, 11 , 16};

        double Ct = 0.0;

        // Cria a lista dos slots para a requisição
        List<Integer> slotsReqFake = new ArrayList<Integer>();
        for (int s = FS; s < FS + reqSlotSize; s++){
            slotsReqFake.add(s);
        }

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
            if (nReq > (SA - nReq)){
                break;
            }

            Cb += ((SA - nReq) - nReq + 1); //Ca <= Calcula a quantidade de formas que é possível alocar uma requisição de tamanho nReq em SA;
        }

        Ct += Ca - Cb; // Perda de capacidade na rota principal

        for (RoutingAlgorithmSolution iRoute: route.getConflictRoute()){
    
            // Busca o mínimo a esquerda
            int minSlot = 0; // Força um erro
            for (int min = FS; min >= 0;min--){
                if (iRoute.getSlotValue(min) == 0){
                    minSlot = min;
                } else {
                    break;
                }
            }
            // Busca o máximo a direita
            int maxSlot = parameters.getNumberOfSlots() - 1; // Força um erro
            for (int max = (FS + reqSlotSize - 1); max < parameters.getNumberOfSlots(); max++){
                if (iRoute.getSlotValue(max) == 0){
                    maxSlot = max;
                } else {
                    break;
                }

            }

            // LA <= Lista todos os buracos que têm ao menos um slot da requisição com inicio em FS e indo até (FS + routeReq);
            if (minSlot == -1 || maxSlot == parameters.getNumberOfSlots() + 1){
                System.out.println("OK");
            }
            List<Apeture> LA = getApetures(iRoute, minSlot, maxSlot);

            for (Apeture apeture : LA) {
                // Capacidade antes da alocação
                Ca = 0;
                for (int nReq: listNumReqs){
                    if (nReq > apeture.getSize()){
                        break;
                    }

                    Ca += (apeture.getSize() - nReq + 1); //Ca <= Calcula a quantidade de formas que é possível alocar uma requisição de tamanho nReq em SA;
                }

                // Aloca a requisição fake
                iRoute.incrementSlotsOcupy(slotsReqFake);

                // Calcula o novo tamanho do buraco
                int newApetureSize = 0; // Força um erro
                for (int size = apeture.getPosition(); size < apeture.getPosition() + apeture.getSize(); size++){
                    if (size == -1){
                        System.out.println("OK");
                    }
                    if (iRoute.getSlotValue(size) == 0){
                        newApetureSize++;
                    }
                    // Aqui pode ser otimizado, conhecendo se é esquerda ou direita
                }

                Cb = 0;
                for (int nReq: listNumReqs){
                    if (nReq > newApetureSize){
                        break;
                    }

                    Cb += (newApetureSize - nReq + 1); //Ca <= Calcula a quantidade de formas que é possível alocar uma requisição de tamanho nReq em SA;
                }

                // Desaloca a requisição fake
                iRoute.decreasesSlotsOcupy(slotsReqFake);

                Ct += Ca - Cb; // Perda de capacidade na rota interferente

            }
            // if (maxNIRoute == nIRoute){
            //     break;
            // }
            // nIRoute++;
        }

        return Ct;
    }

    public void calculate(RoutingAlgorithmSolution route, CallRequest callRequest){

        SimulationParameters parameters = new SimulationParameters();
        Function function = new Function();

        int[] allBitRates = parameters.getBitRate();

        int bitRateIndex = -1;
        for (int b = 0; b <= allBitRates.length; b++){
            if (allBitRates[b] == callRequest.getBitRate()){
                bitRateIndex = b;
                break;
            }
        }
        
        final int reqNumbOfSlots = function.calculateNumberOfSlots(route.getModulationsTypeByBitrate().get(bitRateIndex), callRequest);

        this.MetricX = routeCapacity(route, reqNumbOfSlots);

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


    public Apeture getFirstAperture(RoutingAlgorithmSolution route, int reqSlotSize) {

        SimulationParameters parameters = new SimulationParameters();

        Function function = new Function();

        int emptySlots = 0;

		INDEX_SLOT:for(int indexSlot = emptySlots; indexSlot < parameters.getNumberOfSlots();indexSlot++){

            if (route.getSlotValue(indexSlot) > 0){
                continue INDEX_SLOT;
            }

            // for (OpticalLink link : route.getUpLink()){
            //     final boolean availSlotIn = function.avaliableSlotInOpticalLink(link, indexSlot);
			// 	if(!availSlotIn){           //Analisa se o slot disponível
			// 		continue INDEX_SLOT;    //Procura o pr�ximo slot dispon�vel na grade para come�ar o processo.
			// 	}
            // }

            int countEmptySlots = 0;

			// Para cada slot necessário para alocar a requisição;
			EMPTY_SLOTS:for (emptySlots = indexSlot; emptySlots < parameters.getNumberOfSlots(); emptySlots++){

                if (route.getSlotValue(emptySlots) > 0){
                    break EMPTY_SLOTS;
                }

				// for (OpticalLink link : route.getUpLink()){
                //     final boolean availSlotIn = function.avaliableSlotInOpticalLink(link, emptySlots);
                //     if(!availSlotIn){           //Analisa se o slot disponível
                //         break EMPTY_SLOTS;      //Procura o pr�ximo slot dispon�vel na grade para come�ar o processo.
                //     }
                // }

				countEmptySlots++;
			}

			if (countEmptySlots >= reqSlotSize){
                return new Apeture(indexSlot, countEmptySlots);
            } else {
                indexSlot = emptySlots;
            }

        }

		return null;
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

        // double ctr = 0;

        // int N = 3;

        // double X = getX();
        // double Y = getY();
        // //double Z = getZ();

        // for (int s1 = 0; s1 < N; s1++){
        //     for (int s2 = 0; s2 < N; s2++){
        //         ctr += coeffB[s1 * N + s2] * Math.pow(X, s1) * Math.pow(Y, s2);
        //     }
        // }

        // return ctr;

        return getX();

    }
}
