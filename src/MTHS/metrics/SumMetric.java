package MTHS.metrics;

import java.util.ArrayList;
import java.util.List;

import arquitecture.OpticalLink;
import call_request.CallRequest;
import MTHS.AlgorithmNewSolution;
import parameters.SimulationParameters;
import routing.RoutingAlgorithmSolution;
import utility.Function;
import utility.PertinenceFunction;

public class SumMetric {
    
    private double OSCR;
    private double OMSR;
    private double DNR;

    private int numRules;
	private double[] y; // Lista com os valores para as regras
	private double[] w; // Lista com os pesos para as regras
	final SimulationParameters parameters = new SimulationParameters();
    private double CTR;

    // Coeficientes para o especialistas
    private double[] indicesOSCR ={0.1,0.3,0.2,0.4,0.6,0.8,0.7,0.9};
    private double[] indicesOSMR ={0.1,0.2,0.1,0.3,0.5,0.7,0.6,0.9};
    private double[] indicesDNR ={0.1,0.2,0.1,0.4,0.6,0.9,0.8,0.9};

    private static final FormsEONMetric metricInstance = new FormsEONMetric();

    public static FormsEONMetric getOriginalMetricInstance(){
		return metricInstance;
	}

    public void calculate(RoutingAlgorithmSolution route, double maxDistRoute, CallRequest callRequest){

        SimulationParameters parameters = new SimulationParameters();
        Function function = new Function();

        final int numberOfSlots = parameters.getNumberOfSlots();                // Número total de Slots
		final int numberOfReqSlots = callRequest.getRequiredNumberOfSlots();    // Número de Slots para a alocação

        // Calcula o número de formas possíveis para alocar a requisição em todos os links da rota
        final int waysFree = function.getWaysToAllocate(numberOfSlots, numberOfReqSlots, route.getUpLink(), route.getDownLink());
        // Dividindo pelo total de slots possíveis e invertendo o valor para que valores baixos sejam melhores.
        final double C_OSCR = ((double)((numberOfSlots - numberOfReqSlots + 1) - waysFree) / (numberOfSlots - numberOfReqSlots + 1));

        // Calcula a número de formas possíveis médio para os links
        double auxMeanLinkForms = 0.0;
        for (OpticalLink link: route.getUpLink()){
            List<OpticalLink> listLink = new ArrayList<OpticalLink>();
            listLink.add(link);

            final int waysFreeLink = function.getWaysToAllocate(numberOfSlots, numberOfReqSlots, listLink, listLink);
            auxMeanLinkForms += ((double)((numberOfSlots - numberOfReqSlots + 1) - waysFreeLink) / (numberOfSlots - numberOfReqSlots + 1));
        }

        // Calcula e armazena o OSCR
        this.OSCR = C_OSCR;
        
        // Calcula e armazena o OSMR
        this.OMSR = auxMeanLinkForms/route.getUpLink().size();
        
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

    public double calculateCTRbyRules(double[] coeffMetric){

        return getAlpha() * OSCR + getBeta() * OMSR + getGama() * DNR;
    }

    public double getAlpha(){
		double sumW=0;
		double res = 0;
		for (int i = 0; i <= 2; i++) {
			res+=this.y[i]*w[i];
			sumW+=w[i];
		}
		return res/sumW;
	}

	public double getBeta(){
		double sumW=0;
		double res = 0;
		for (int i = 3; i <= 4; i++) {
			res+=this.y[i];
			sumW+=w[i];
		}
		return res/sumW;
	}

	public double getGama(){
		double sumW=0;
		double res = 0;
		for (int i = 5; i <= 6; i++) {
			res+=this.y[i];
			sumW+=w[i];
		}
		return res/sumW;
	}

    private boolean OSCRisGood(PertinenceFunction fp){
        if ((fp.calculate(OSCR, 1) > fp.calculate(OSCR, 2)) && (fp.calculate(OSCR, 1) > fp.calculate(OSCR, 3))){
            return true;
        }
        return false;
    }

    private boolean OSCRisReasonable(PertinenceFunction fp){
        if ((fp.calculate(OSCR, 2) > fp.calculate(OSCR, 1)) && (fp.calculate(OSCR, 2) > fp.calculate(OSCR, 3))){
            return true;
        }
        return false;
    }

    private boolean OSCRisBad(PertinenceFunction fp){
        if ((fp.calculate(OSCR, 3) > fp.calculate(OSCR, 1)) && (fp.calculate(OSCR, 3) > fp.calculate(OSCR, 2))){
            return true;
        }
        return false;
    }

    private boolean OMSRisGood(PertinenceFunction fp){
        if ((fp.calculate(OMSR, 1) > fp.calculate(OMSR, 2)) && (fp.calculate(OMSR, 1) > fp.calculate(OMSR, 3))){
            return true;
        }
        return false;
    }

    private boolean OMSRisReasonable(PertinenceFunction fp){
        if ((fp.calculate(OMSR, 2) > fp.calculate(OMSR, 1)) && (fp.calculate(OMSR, 2) > fp.calculate(OMSR, 3))){
            return true;
        }
        return false;
    }

    private boolean OMSRisBad(PertinenceFunction fp){
        if ((fp.calculate(OMSR, 3) > fp.calculate(OMSR, 1)) && (fp.calculate(OMSR, 3) > fp.calculate(OMSR, 2))){
            return true;
        }
        return false;
    }

    private boolean DNRisSmall(PertinenceFunction fp){
        if ((fp.calculate(DNR, 1) > fp.calculate(DNR, 2)) && (fp.calculate(DNR, 1) > fp.calculate(DNR, 3))){
            return true;
        }
        return false;
    }

    private boolean DNRisMedium(PertinenceFunction fp){
        if ((fp.calculate(DNR, 2) > fp.calculate(DNR, 1)) && (fp.calculate(DNR, 2) > fp.calculate(DNR, 3))){
            return true;
        }
        return false;
    }

    private boolean DNRisBig(PertinenceFunction fp){
        if ((fp.calculate(DNR, 3) > fp.calculate(DNR, 1)) && (fp.calculate(DNR, 3) > fp.calculate(DNR, 2))){
            return true;
        }
        return false;
    }
}   