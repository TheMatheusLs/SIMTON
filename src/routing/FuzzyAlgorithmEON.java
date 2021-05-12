package routing;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import call_request.CallRequest;
import exceptions.InvalidNodeIdException;
import parameters.SimulationParameters;
import utility.Function;

import net.sourceforge.jFuzzyLogic.FIS;

public class FuzzyAlgorithmEON{
	
	private static final FuzzyAlgorithmEON FA_INSTANCE = new FuzzyAlgorithmEON();	
	
	public List<RoutingAlgorithmSolution> findRoutesSugenoM1(List<RoutingAlgorithmSolution> routesYEN, final OpticalLink[][] network, final CallRequest callRequest, final List<OpticalSwitch> listOfNodes, final List<Double> listOfCoeff) throws InvalidNodeIdException{

		//Cria a estrutura para armazenar as soluções Fuzzy
		Vector<FuzzyAlgorithmSolution> fuzzySolutions = new Vector<>();

		SimulationParameters parameters = new SimulationParameters();

		Function function = new Function();

		final int numberOfSlots = parameters.getNumberOfSlots();
		final int numberOfReqSlots = callRequest.getRequiredNumberOfSlots();

		// Calcula o número de hops da rota com a maior quantidade
		int numberMaxhops = -1;
		for(RoutingAlgorithmSolution solution : routesYEN){
			if (solution.getNumHops() > numberMaxhops){
				numberMaxhops = solution.getNumHops();
			}
		}
		
		double maxDistRoute = routesYEN.get(routesYEN.size()-1).getSizeInBound();
		
		for(RoutingAlgorithmSolution solution : routesYEN){

			FuzzyAlgorithmSolution fuzzySolution = new FuzzyAlgorithmSolution();

			fuzzySolution.setRota(solution);

			// Calcula o número de formas disponíveis para alocar a requisição para encontrar o OSCR
			final int ways = function.getWaysToAllocate(numberOfSlots, numberOfReqSlots, solution.getUpLink(), solution.getDownLink());
			final double C_OSCR = 1 - ((double)ways / (numberOfSlots - numberOfReqSlots + 1));
		
			final double C_OMSR = ((double)solution.getNumHops() / numberMaxhops);
			//final double C_OMSR = 0;
			
			//final double C_DNR = (solution.getSizeInBound()/maxDistRoute);
			final double C_DNR = 0;

			fuzzySolution.setDNR(C_DNR);
			fuzzySolution.setOSMR(C_OMSR);
			fuzzySolution.setOSCR(C_OSCR);
			
			FuzzyRules rules = new FuzzyRules(7);
			rules.applyRules(fuzzySolution, listOfCoeff);
			
			fuzzySolution.setCTR(rules.getAlpha()*fuzzySolution.getOSCR()+rules.getBeta()*fuzzySolution.getOSMR()+
					rules.getGama()*fuzzySolution.getDNR());
			
			fuzzySolutions.addElement(fuzzySolution);						
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
				if(fuzzySolutions.get(nSolution).getCTR() <= minCTR){
					minCTR = fuzzySolutions.get(nSolution).getCTR(); // Salva o nome valor mínimo
					bestRouteIndex = nSolution;	//Salva a posição da rota
				}
			}
			
			solutions.add(fuzzySolutions.get(bestRouteIndex).getRota());

			fuzzySolutions.remove(bestRouteIndex);
		}

		return  solutions;
	}//end findRoutesSugeno 


	public RoutingAlgorithmSolution findRoute(List<RoutingAlgorithmSolution> routesYEN, final OpticalLink[][] network, final CallRequest callRequest, final List<OpticalSwitch> listOfNodes, final List<Double> listOfCoeff, FIS fis) throws InvalidNodeIdException{

		SimulationParameters parameters = new SimulationParameters();

		Function function = new Function();

		Vector<FuzzyAlgorithmSolutionEON> fuzzySolutions = new Vector<>();

		final int numberOfSlots = parameters.getNumberOfSlots();
		final int numberOfReqSlots = callRequest.getRequiredNumberOfSlots();

		// Calcula o número de hops da rota com a maior quantidade
		int numberMaxhops = -1;
		for(RoutingAlgorithmSolution solution : routesYEN){
			if (solution.getNumHops() > numberMaxhops){
				numberMaxhops = solution.getNumHops();
			}
		}
		
		for(RoutingAlgorithmSolution solution : routesYEN){

			FuzzyAlgorithmSolutionEON fuzzySolution = new FuzzyAlgorithmSolutionEON();

			fuzzySolution.setRota(solution);

			// Calcula o número de formas disponíveis para alocar a requisição
			final int ways = function.getWaysToAllocate(numberOfSlots, numberOfReqSlots, solution.getUpLink(), solution.getDownLink());

			final double CForms = ((double)ways / (numberOfSlots - numberOfReqSlots + 1));
			
			final double CHops = ((double)solution.getNumHops() / numberMaxhops);

			fis.setVariable("C_forms", CForms);
			fis.setVariable("C_hops", CHops);

			fis.evaluate();

			
			fuzzySolution.setForms(CForms);
			fuzzySolution.setHops(CHops);
			fuzzySolution.setCTR(fis.getVariable("C_tr").getValue());
			
			fuzzySolutions.addElement(fuzzySolution);
						
		}

		//Nesta etapa escolhemos a menor rota baseada no CTR
		FuzzyAlgorithmSolutionEON solutionRes = fuzzySolutions.firstElement();
		for (FuzzyAlgorithmSolutionEON solution : fuzzySolutions) {
			if(solution.getCTR()<solutionRes.getCTR())
				solutionRes = solution;
		}
		
		return  solutionRes.getRota();
		
	}

	/*
	 * Função para ordenar as rotas YEN de acordo com a lógica Fuzzy
	 */
	public List<RoutingAlgorithmSolution> findRouteKYEN(List<RoutingAlgorithmSolution> routesYEN, final OpticalLink[][] network, final CallRequest callRequest, final List<OpticalSwitch> listOfNodes, final List<Double> listOfCoeff, FIS fis) throws InvalidNodeIdException{

		SimulationParameters parameters = new SimulationParameters();

		Function function = new Function();

		Vector<FuzzyAlgorithmSolutionEON> fuzzySolutions = new Vector<>();

		final int numberOfSlots = parameters.getNumberOfSlots();
		final int numberOfReqSlots = callRequest.getRequiredNumberOfSlots();

		// Calcula o número de hops da rota com a maior quantidade
		int numberMaxhops = -1;
		for(RoutingAlgorithmSolution solution : routesYEN){
			if (solution.getNumHops() > numberMaxhops){
				numberMaxhops = solution.getNumHops();
			}
		}
		
		for(RoutingAlgorithmSolution solution : routesYEN){

			FuzzyAlgorithmSolutionEON fuzzySolution = new FuzzyAlgorithmSolutionEON();

			fuzzySolution.setRota(solution);

			// Calcula o número de formas disponíveis para alocar a requisição
			final int ways = function.getWaysToAllocate(numberOfSlots, numberOfReqSlots, solution.getUpLink(), solution.getDownLink());

			final double CForms = ((double)ways / (numberOfSlots - numberOfReqSlots + 1));
			
			final double CHops = ((double)solution.getNumHops() / numberMaxhops);

			fis.setVariable("C_forms", CForms);
			fis.setVariable("C_hops", CHops);

			fis.evaluate();

			
			fuzzySolution.setForms(CForms);
			fuzzySolution.setHops(CHops);
			fuzzySolution.setCTR(fis.getVariable("C_tr").getValue());
			
			fuzzySolutions.addElement(fuzzySolution);			
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
				if(fuzzySolutions.get(nSolution).getCTR() <= minCTR){
					minCTR = fuzzySolutions.get(nSolution).getCTR(); // Salva o nome valor mínimo
					bestRouteIndex = nSolution;	//Salva a posição da rota
				}
			}
			
			solutions.add(fuzzySolutions.get(bestRouteIndex).getRota());

			fuzzySolutions.remove(bestRouteIndex);
		}

		return  solutions;
	}
	
	public static FuzzyAlgorithmEON getFuzzyInstance(){
		return FA_INSTANCE;
	}
}
