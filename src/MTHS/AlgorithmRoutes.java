package MTHS;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import call_request.CallRequest;
import exceptions.InvalidNodeIdException;
import exceptions.InvalidRoutesException;
import parameters.SimulationParameters;
import routing.RoutingAlgorithmSolution;
import types.MetricMethodType;
import utility.Debug;
import MTHS.metrics.AlphaBetaGamma;
import MTHS.metrics.Danilo;
import MTHS.metrics.FormsEONMetric;
import MTHS.metrics.MSCLApeture;
import MTHS.metrics.OriginalMetric;
import MTHS.metrics.OriginalMetricMths;
import MTHS.metrics.PSRMetric;
import MTHS.metrics.SlotApertureMetric;
import MTHS.metrics.YENMetric;

/*
 * Classe responsável por ordenar o conjunto de K rotas YEN utilizando a lógica Fuzzy.
 */
public class AlgorithmRoutes {
    
    // Cria a instância da classe
    private static final AlgorithmRoutes FA_INSTANCE = new AlgorithmRoutes();	

    /*
	 * Função para ordenar as rotas YEN de acordo com a lógica Fuzzy
	 */
	public List<RoutingAlgorithmSolution> sortRoutes(List<RoutingAlgorithmSolution> routesYEN, final OpticalLink[][] network, final CallRequest callRequest, final List<OpticalSwitch> listOfNodes, final double[] coeffMetric, MetricMethodType metricType, Debug debugClass) throws InvalidNodeIdException, InvalidRoutesException{

		Vector<AlgorithmNewSolution> fuzzySolutions = new Vector<>();

		SimulationParameters parameters = new SimulationParameters();

		int YENID = 0;
		// Percorre todas as K Rotas
		for(RoutingAlgorithmSolution route : routesYEN){

			route.setYENID(YENID);

			double CTR = -1.0;
			if (metricType.equals(MetricMethodType.SUGENO_WDM_ONE_ORIGINAL)){
				double maxDistRoute = routesYEN.get(routesYEN.size()-1).getSizeInBound();
				
				OriginalMetric orinalMetricInstance = OriginalMetric.getOriginalMetricInstance();
				orinalMetricInstance.calculate(route, maxDistRoute);

				//CTR = orinalMetricInstance.calculateCTRbyRules(coeffMetric, matrixRulesCoeffs);
			} else if (metricType.equals(MetricMethodType.SUGENO_WDM_ONE_MTHS)){
				double maxDistRoute = routesYEN.get(routesYEN.size()-1).getSizeInBound();
				
				OriginalMetricMths orinalMetricMthsInstance = OriginalMetricMths.getOriginalMetricMthsInstance();
				orinalMetricMthsInstance.calculate(route, maxDistRoute);

				CTR = 1; //orinalMetricMthsInstance.calculateCTRbyRules(coeffMetric, matrixRulesCoeffs);
			} else if (metricType.equals(MetricMethodType.SUGENO_EON_ONE)){
				double maxDistRoute = routesYEN.get(routesYEN.size()-1).getSizeInBound();

				FormsEONMetric formsEonMetric = FormsEONMetric.getOriginalMetricInstance();

				formsEonMetric.calculate(route, maxDistRoute, callRequest);

				CTR = formsEonMetric.calculateCTRbyRules(coeffMetric);

			} else if (metricType.equals(MetricMethodType.ORIGINAL_KYEN_MTHS)){
				double maxDistRoute = routesYEN.get(routesYEN.size()-1).getSizeInBound();

				OriginalMetric originalMetric = OriginalMetric.getOriginalMetricInstance();

				originalMetric.calculate(route, maxDistRoute);

				CTR = originalMetric.calculateCTRbyRules(coeffMetric, callRequest, YENID);
			} else if (metricType.equals(MetricMethodType.ALPHA_BETA_GAMMA_ONE)){
				double maxDistRoute = routesYEN.get(routesYEN.size()-1).getSizeInBound();

				AlphaBetaGamma alphaBetaGamma = AlphaBetaGamma.getAlphaBetaGammaInstance();

				alphaBetaGamma.calculate(route, maxDistRoute);

				CTR = alphaBetaGamma.calculateCTRbyRules(coeffMetric, callRequest, YENID);

			} else if (metricType.equals(MetricMethodType.PSR_METRIC)){

				double maxDistRoute = 0;
				int maxHopsRoute = 0;
				int maxIRoutes = 0;
				double maxHopsxBanda = 0;

				for (RoutingAlgorithmSolution auxRoute: routesYEN){
					if (auxRoute.getSizeInBound() > maxDistRoute){
						maxDistRoute = auxRoute.getSizeInBound();
					}

					if (auxRoute.getNumHops() > maxHopsRoute){
						maxHopsRoute = auxRoute.getNumHops();
					}

					if (auxRoute.getConflictRoute().size() > maxIRoutes){
						maxIRoutes = auxRoute.getConflictRoute().size();
					}

					int numHops = auxRoute.getNumHops();

					int bitRate = callRequest.getBitRate();

					int[] allBitRates = parameters.getBitRate();

					int bitRateIndex = -1;
					for (int i = 0; i < allBitRates.length; i++){
						if (allBitRates[i] == bitRate){
							bitRateIndex = i;
							break;
						}
					}

					int modulationConstelation = auxRoute.getModulationsTypeByBitrate().get(bitRateIndex).getConstelation();

					double hopsxBandaTemp = numHops * (bitRate / (Math.log10(modulationConstelation) / Math.log10(2)));

					if (hopsxBandaTemp > maxHopsxBanda){
						maxHopsxBanda = hopsxBandaTemp;
					}

				}

				PSRMetric PSR = PSRMetric.getPSRMetricInstance();

				PSR.calculateOHBI(route, maxHopsRoute, maxIRoutes, maxHopsxBanda, callRequest); // (Ocupação, Hops x Banda, Irotas)
				//PSR.calculateOHB(route, maxHopsRoute, maxHopsxBanda, callRequest); // (Ocupação, Hops x Banda)
				//PSR.calculateOI(route, maxIRoutes, callRequest); // (Ocupação, Irotas)

				CTR = PSR.calculateCTR3(coeffMetric, callRequest);
				//CTR = PSR.calculateCTR2(coeffMetric, callRequest);
				
			} else if (metricType.equals(MetricMethodType.YEN)){
				YENMetric YEN = YENMetric.getYENMetricInstance();

				//CTR = YEN.calculateCTRHxB(route, callRequest); //(Para Hops x banda)
				//CTR = YEN.calculateCTRDistance(); //(Para Distância)
				//CTR = YEN.calculateCTRHops(route); //(Para Hops)
				CTR = YEN.calculateCTROcu(route); //(Para Ocupação)
				//CTR = YEN.calculateCTRIRoute(route); //(Para Rotas interferentes)

			} else if (metricType.equals(MetricMethodType.SLOT_APERTURE)){
				SlotApertureMetric slotAperture = SlotApertureMetric.getSlotApertureMetricInstance();

				slotAperture.calculate(route, callRequest);

				CTR = slotAperture.calculateCTR(coeffMetric);
			} else if (metricType.equals(MetricMethodType.MSCL_APETURE)){
				MSCLApeture slotAperture = MSCLApeture.getMSCLApetureInstance();

				slotAperture.calculate(route, callRequest);

				CTR = slotAperture.calculateCTR(coeffMetric);
			} else if (metricType.equals(MetricMethodType.DANILO)){
				Danilo metric = Danilo.getDaniloInstance();


				// Cálculo para a rota atual
				CTR = metric.calculate(route, coeffMetric);
			}

			// Cria a solução fuzzy para a rota
			AlgorithmNewSolution fuzzySolution = new AlgorithmNewSolution(route, CTR);

			fuzzySolutions.addElement(fuzzySolution);

			YENID++;
		}

		//Nesta etapa ordenamos as rotas de acordo com o CTR
		final List<RoutingAlgorithmSolution> solutions = new ArrayList<RoutingAlgorithmSolution>();

		//Percorre o número K de soluções de rota
		final int KYEN = fuzzySolutions.size();
		for (int nRoute = 0; nRoute < KYEN; nRoute++){
			//Percorre as soluções Fuzzy
			int bestRouteIndex = -1;
			double minCTR = Double.MAX_VALUE;
			for (int nSolution = 0; nSolution < fuzzySolutions.size(); nSolution++){
				// Procura a rota com o menor CTR 
				if(fuzzySolutions.get(nSolution).getCTR() < minCTR){
					minCTR = fuzzySolutions.get(nSolution).getCTR(); // Salva o nome valor mínimo
					bestRouteIndex = nSolution;	//Salva a posição da rota
				}
			}
			
			solutions.add(fuzzySolutions.get(bestRouteIndex).getRoute());

			fuzzySolutions.remove(bestRouteIndex);
		}

		return  solutions;

    
    }//end sortRoutesFuzzy
    
	public static AlgorithmRoutes getInstance(){
		return FA_INSTANCE;
	}

}
