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

public class FormsEONMetric {
    
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

        y = new double[7];
        w = new double[7];

        for (int i = 0; i < w.length; i++){
            y[i] = 0;
            w[i] = 1.0;
        }

		//Caso esteja no processo de otimização, os indices não serão nulos
		if(coeffMetric!=null) {
            if (coeffMetric.length == 6){
                
                indicesOSCR[0]= 0;
                indicesOSCR[1]= coeffMetric[0];
                indicesOSCR[2]= 0;
                indicesOSCR[3]= coeffMetric[1];
                indicesOSCR[4]= coeffMetric[1];
                indicesOSCR[5]= 1;
                indicesOSCR[6]= coeffMetric[0];
                indicesOSCR[7]= 1;

                indicesOSMR[0]= 0;
                indicesOSMR[1]= coeffMetric[2];
                indicesOSMR[2]= 0;
                indicesOSMR[3]= coeffMetric[3];
                indicesOSMR[4]= coeffMetric[3];
                indicesOSMR[5]= 1;
                indicesOSMR[6]= coeffMetric[2];
                indicesOSMR[7]= 1;
				
                indicesDNR[0]= 0;
                indicesDNR[1]= coeffMetric[4];
                indicesDNR[2]= 0;
                indicesDNR[3]= coeffMetric[5];
                indicesDNR[4]= coeffMetric[5];
                indicesDNR[5]= 1;
                indicesDNR[6]= coeffMetric[4];
                indicesDNR[7]= 1;

                for (int i = 0; i < w.length; i++) {
					w[i] = 1.0;
				}

            } else if (coeffMetric.length == 31){
				for (int i = 0; i < (parameters.getDimension()-w.length)/3; i++) {
					indicesOSCR[i]= coeffMetric[i];
					indicesOSMR[i]= coeffMetric[i+8];
					indicesDNR[i]= coeffMetric[i+16];
				}
				int rulesIndice = parameters.getDimension()-w.length;
				for (int i = 0; i < w.length; i++) {
					w[i] = coeffMetric[i+rulesIndice];
				}
			} else  if (coeffMetric.length == 24){
				for (int i = 0; i < (parameters.getDimension()-w.length)/3; i++) {
					indicesOSCR[i]=coeffMetric[i];
					indicesOSMR[i]=coeffMetric[i+8];
					indicesDNR[i]=coeffMetric[i+16];
				}
				int rulesIndice = parameters.getDimension()-w.length;
				for (int i = 0; i < w.length; i++) {
					w[i] = 1;
				}
			} else if (coeffMetric.length == 7){
				for (int i = 0; i < w.length; i++) {
					w[i] = coeffMetric[i];
				}
			}
		}
		
		PertinenceFunction fpOSCR = new PertinenceFunction(indicesOSCR);
		PertinenceFunction fpOMSR = new PertinenceFunction(indicesOSMR);
		PertinenceFunction fpDNR = new PertinenceFunction(indicesDNR);
        
        // Regras para o artigo original. Total de 27 regras
		// Regra 1 - OSCR is Bad
		//if (OSCRisBad(fpOSCR)){
            y[0] = 2 - fpOSCR.calculate(OSCR, 2);
        //}
        // Regra 2 - OSCR is Reasonable
		//if (OSCRisReasonable(fpOSCR)){
            y[1] = 1 + fpOSCR.calculate(OSCR, 3) - fpOSCR.calculate(OSCR, 1);
        //}
        // Regra 3 - OSCR is Good
		//if (OSCRisGood(fpOSCR)){
            y[2] = 0.5 + 2*fpOSCR.calculate(OSCR, 2);
        //}
        // Regra 4 - OMSR is Good and OSCR is Bad
		//if (OMSRisGood(fpOMSR) && OSCRisBad(fpOSCR)){
            y[3] = 2 - 0.5*fpOMSR.calculate(OMSR, 1);
        //}
        // Regra 5 - OMSR is Reasonable and DNR is Bad
		//if (OMSRisReasonable(fpOMSR) && DNRisBig(fpDNR)){
            y[4] = 2 - fpDNR.calculate(DNR, 3);
        //}
        // Regra 6 - DNR is Reasonable and OSCR is Good
		//if (DNRisMedium(fpDNR) && OSCRisGood(fpOSCR)){
            y[5] = 2 - 0.5*fpDNR.calculate(DNR, 2) - fpOSCR.calculate(OSCR, 1);
        //}
        // Regra 7 - DNR is Big and OMSR is Bad
		//if (DNRisMedium(fpDNR) && OSCRisGood(fpOSCR)){
            y[6] = 2 + 0.5*fpDNR.calculate(DNR, 3) + 0.5*fpOMSR.calculate(OMSR, 3);
        //}
        
        double alpha = getAlpha();
        double beta = getBeta();
        double gamma = getGama();

        //double ctr = 1 * OSCR +  1 * OMSR + 1 * DNR;
        double ctr = alpha * OSCR +  beta * OMSR + gamma * DNR;

        return ctr;
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