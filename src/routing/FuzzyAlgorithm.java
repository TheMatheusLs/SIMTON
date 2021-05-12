package routing;

import routing.RoutingAlgorithmSolution;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import call_request.CallRequest;
import exceptions.InvalidNodeIdException;
import parameters.SimulationParameters;

public class FuzzyAlgorithm{
	
	private static final FuzzyAlgorithm FA_INSTANCE = new FuzzyAlgorithm();	
		
	public RoutingAlgorithmSolution findRoute(List<RoutingAlgorithmSolution> routesYEN, final OpticalLink[][] network, final CallRequest callRequest, final List<OpticalSwitch> listOfNodes, final double[] listOfCoeff) throws InvalidNodeIdException{

		Vector<FuzzyAlgorithmSolution> fuzzySolutions = new Vector<>();
		SimulationParameters p = new SimulationParameters();
		
		int count;
		double ocupacaomedia;
		double maxDistRoute = routesYEN.get(routesYEN.size()-1).getSizeInBound();
		
		for(RoutingAlgorithmSolution solution : routesYEN){
			count = 0;
			ocupacaomedia = 0.0;
			int[] slotsContiguosMask = new int[p.getNumberOfSlots()];
			
			FuzzyAlgorithmSolution fuzzySolution = new FuzzyAlgorithmSolution();
			fuzzySolution.setRota(solution);
			
			for(OpticalLink link : solution.getUpLink()){				
				//Análise ocupação média e Slots Contiguos
				count = 0;  // Reseta a variável count para calcular o próximo link
				for(int s=0;s<p.getNumberOfSlots();s++){
					if(!link.availableSlot(s)){
						count++;
						slotsContiguosMask[s] = 1;
					}
				}
				double ocupationLink = (double) count/p.getNumberOfSlots();			
				ocupacaomedia += ocupationLink;
			}
			
			count = 0;
			for (int i = 0; i < slotsContiguosMask.length; i++) {
				if(slotsContiguosMask[i]==1)
					count++;
			}
			
			double OSCR = ((double)count)/p.getNumberOfSlots();
			fuzzySolution.setOSCR(OSCR);
			double OMSR = ocupacaomedia/solution.getUpLink().size();
			fuzzySolution.setOSMR(OMSR);
			double DNR = solution.getSizeInBound()/maxDistRoute;
			fuzzySolution.setDNR(DNR);
			
			FuzzyRules rules = new FuzzyRules(7);
			rules.applyRules(fuzzySolution, listOfCoeff);
			
			// double alpha = 0.6;
			// double beta = 0.0;
			// double gamma = 0.2;
			double alpha = rules.getAlpha();
			double beta = rules.getBeta();
			double gamma = rules.getGama();

			double ctr_aux = alpha * fuzzySolution.getOSCR() + beta * fuzzySolution.getOSMR()+
			gamma * fuzzySolution.getDNR();

			fuzzySolution.setCTR(ctr_aux);
			
			fuzzySolutions.addElement(fuzzySolution);

			// String dataLog = String.format("%f,%f,%f,%f,%f,%f,%f",ctr_aux, OSCR, OMSR, DNR, alpha, beta, gamma);

			// try {

			// 	final FileWriter file = new FileWriter("D:\\ProgrammingFiles\\ReportsSIMTON\\logMetrics.csv", true); 
			// 	final PrintWriter saveFile2 = new PrintWriter(file);

			// 	saveFile2.printf(dataLog + "\n");
			// 	saveFile2.close();
				
			// } catch (Exception e) {
			// 	//TODO: handle exception
			// }
						
		}

		//Nesta etapa escolhemos a menor rota baseada no CTR
		FuzzyAlgorithmSolution solutionRes = fuzzySolutions.firstElement();
		for (FuzzyAlgorithmSolution solution : fuzzySolutions) {
			if(solution.getCTR()<solutionRes.getCTR())
				solutionRes = solution;
		}
		
		return  solutionRes.getRota();
		
	}

	/*
	 * Função para ordenar as rotas YEN de acordo com a lógica Fuzzy
	 */
	public List<RoutingAlgorithmSolution> findRouteKYEN(List<RoutingAlgorithmSolution> routesYEN, final OpticalLink[][] network, final CallRequest callRequest, final List<OpticalSwitch> listOfNodes, final double[] listOfCoeff) throws InvalidNodeIdException{

		Vector<FuzzyAlgorithmSolution> fuzzySolutions = new Vector<>();
		SimulationParameters parameters = new SimulationParameters();
		
		int count;
		double ocupacaomedia;
		double maxDistRoute = routesYEN.get(routesYEN.size()-1).getSizeInBound();
		
		int yenID = 0;

		for(RoutingAlgorithmSolution solution : routesYEN){
			count = 0;
			ocupacaomedia = 0.0;

			solution.setYENID(yenID);
			yenID++;

			int[] slotsContiguosMask = new int[parameters.getNumberOfSlots()];
			
			FuzzyAlgorithmSolution fuzzySolution = new FuzzyAlgorithmSolution();
			fuzzySolution.setRota(solution);
			
			for(OpticalLink link : solution.getUpLink()){				
				//An�lise ocupa��o m�dia e Slots Contiguos
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
			
			fuzzySolution.setDNR(solution.getSizeInBound()/maxDistRoute);
			fuzzySolution.setOSMR(ocupacaomedia/solution.getUpLink().size());
			fuzzySolution.setOSCR(((double)count)/parameters.getNumberOfSlots());
			
			FuzzyRules rules = new FuzzyRules(7);
			rules.applyRules(fuzzySolution, listOfCoeff);
			
			fuzzySolution.setCTR(rules.getAlpha()*fuzzySolution.getOSCR()+rules.getBeta()*fuzzySolution.getOSMR()+
					rules.getGama()*fuzzySolution.getDNR());
			
			fuzzySolutions.addElement(fuzzySolution);
		}//end for routes

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
	
	public static FuzzyAlgorithm getFuzzyInstance(){
		return FA_INSTANCE;
	}
}
