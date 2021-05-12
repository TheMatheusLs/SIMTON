package MTHS.metrics;

import java.io.FileWriter;
import java.io.PrintWriter;

import arquitecture.OpticalLink;
import call_request.CallRequest;
import MTHS.AlgorithmNewSolution;
import parameters.SimulationParameters;
import routing.RoutingAlgorithmSolution;
import utility.PertinenceFunction;

public class OriginalMetric {
    
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

    private static final OriginalMetric metricInstance = new OriginalMetric();

    public static OriginalMetric getOriginalMetricInstance(){
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

        y = new double[7];
        w = new double[7];

        for (int i = 0; i < w.length; i++){
            y[i] = 0.0;
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
		// if (OSCRisBad(fpOSCR)){
        //     y[0] = 2 - fpOSCR.calculate(OSCR, 2);
        // }
        // // Regra 2 - OSCR is Reasonable
		// if (OSCRisReasonable(fpOSCR)){
        //     y[1] = 1 + fpOSCR.calculate(OSCR, 3) - fpOSCR.calculate(OSCR, 1);
        // }
        // // Regra 3 - OSCR is Good
		// if (OSCRisGood(fpOSCR)){
        //     y[2] = 0.5 + 2*fpOSCR.calculate(OSCR, 2);
        // }
        // // Regra 4 - OMSR is Good and OSCR is Bad
		// if (OMSRisGood(fpOMSR) && OSCRisBad(fpOSCR)){
        //     y[3] = 2 - 0.5*fpOMSR.calculate(OMSR, 1);
        // }
        // // Regra 5 - OMSR is Reasonable and DNR is Bad
		// if (OMSRisReasonable(fpOMSR) && DNRisBig(fpDNR)){
        //     y[4] = 2 - fpDNR.calculate(DNR, 3);
        // }
        // // Regra 6 - DNR is Reasonable and OSCR is Good
		// if (DNRisMedium(fpDNR) && OSCRisGood(fpOSCR)){
        //     y[5] = 2 - 0.5*fpDNR.calculate(DNR, 2) - fpOSCR.calculate(OSCR, 1);
        // }
        // // Regra 7 - DNR is Big and OMSR is Bad
		// if (DNRisMedium(fpDNR) && OSCRisGood(fpOSCR)){
        //     y[6] = 2 + 0.5*fpDNR.calculate(DNR, 3) + 0.5*fpOMSR.calculate(OMSR, 3);
        // }

        y[0] = 2 - fpOSCR.calculate(OSCR, 2);
        y[1] = 1 + fpOSCR.calculate(OSCR, 3) - fpOSCR.calculate(OSCR, 1);
        y[2] = 0.5 + 2*fpOSCR.calculate(OSCR, 2);
        y[3] = 2 - 0.5*fpOMSR.calculate(OMSR, 1);
        y[4] = 2 - fpDNR.calculate(DNR, 3);
        y[5] = 2 - 0.5*fpDNR.calculate(DNR, 2) - fpOSCR.calculate(OSCR, 1);
        y[6] = 2 + 0.5*fpDNR.calculate(DNR, 3) + 0.5*fpOMSR.calculate(OMSR, 3);
        double alpha = getAlpha();
        double beta = getBeta();
        double gamma = getGama();


        //double ctr = alpha * OSCR + beta * OMSR + gamma * DNR;
        //double ctr = OSCR + OMSR + DNR; 
        //double ctr = 2 * OSCR +  1.5 * OMSR - 3 * DNR;
        //double ctr = 2 * OSCR +  2.5 * OMSR - 2 * DNR;
        //double ctr = 1 * OSCR +  3 * OMSR + 5 * DNR;
        double ctr = 3 * OSCR +  5 * OMSR + 1 * DNR;

        //"id,kyen,ctr,OSCR,OMSR,DNR,alpha,beta,gamma,y0,y1,y2,y3,y4,y5,y6"

        // String dataLog = String.format("%d,%d,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f,%f",callRequest.getReqID(), KYEN, ctr, OSCR, OMSR, DNR, alpha, beta, gamma, y[0], y[1], y[2], y[3], y[4], y[5], y[6]);

        // try {

        //     final FileWriter file = new FileWriter("D:\\ProgrammingFiles\\ReportsSIMTON\\logMetrics.csv", true); 
        //     final PrintWriter saveFile2 = new PrintWriter(file);

        //     saveFile2.printf(dataLog + "\n");
        //     saveFile2.close();
            
        // } catch (Exception e) {
        //     //TODO: handle exception
        // }

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