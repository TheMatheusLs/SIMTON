package MTHS.metrics;

import arquitecture.OpticalLink;
import MTHS.AlgorithmNewSolution;
import parameters.SimulationParameters;
import routing.RoutingAlgorithmSolution;
import utility.PertinenceFunction;

public class OriginalMetricMths {
    
    private double OSCR;
    private double OMSR;
    private double DNR;

	private double[] y; // Lista com os valores para as regras
	final SimulationParameters parameters = new SimulationParameters();
    private double CTR;

    // Coeficientes para o especialistas
    private double[] indicesOSCR ={0.1,0.3,0.2,0.4,0.6,0.8,0.7,0.9};
    private double[] indicesOSMR ={0.1,0.2,0.1,0.3,0.5,0.7,0.6,0.9};
    private double[] indicesDNR ={0.1,0.2,0.1,0.4,0.6,0.9,0.8,0.9};

    private static final OriginalMetricMths metricInstance = new OriginalMetricMths();

    public static OriginalMetricMths getOriginalMetricMthsInstance(){
		return metricInstance;
	}

    public void calculate(RoutingAlgorithmSolution route, double maxDistRoute){

        SimulationParameters parameters = new SimulationParameters();
		
		int count = 0;
		double ocupacaomedia = 0.0;

		int[] slotsContiguosMask = new int[parameters.getNumberOfSlots()];
	
        for(OpticalLink link : route.getUpLink()){				
            //Análise ocupação média e Slots Contiguos
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

    public double calculateCTRbyRules(double[] coeffMetric, double[][] matrixRulesCoeffs){

        y = new double[27];

        for (int i = 0; i < y.length; i++) {
            y[i] = 0.0;
        }

        double[] w = new double[27];
        double[] alpha = new double[27];

		//Caso esteja no processo de otimização, os indices não serão nulos
		if(coeffMetric!=null) {
			if (coeffMetric.length == 31){
				for (int i = 0; i < (parameters.getDimension()-w.length)/3; i++) {
					indicesOSCR[i]= coeffMetric[i];
					indicesOSMR[i]= coeffMetric[i+8];
					indicesDNR[i]= coeffMetric[i+16];
				}
				int rulesIndice = parameters.getDimension()-w.length;
				for (int i = 0; i < w.length; i++) {
					w[i] = coeffMetric[i+rulesIndice];
				}
			}else if (coeffMetric.length == 24){
				for (int i = 0; i < (parameters.getDimension()-w.length)/3; i++) {
					indicesOSCR[i]=coeffMetric[i];
					indicesOSMR[i]=coeffMetric[i+8];
					indicesDNR[i]=coeffMetric[i+16];
				}
				int rulesIndice = parameters.getDimension()-w.length;
				for (int i = 0; i < w.length; i++) {
					w[i] = 1;
				}
			}else if (coeffMetric.length == 7){
				for (int i = 0; i < w.length; i++) {
					w[i] = coeffMetric[i];
				}
			}
		}

        // for (int r = 0; r < 27; r++) {
        //     for (int c = 0; c < 4; c++) {
        //         matrixRulesCoeffs[r][c] = 1.0;
        //     }
        // }
		
		PertinenceFunction fpOSCR = new PertinenceFunction(indicesOSCR);
		PertinenceFunction fpOMSR = new PertinenceFunction(indicesOSMR);
		PertinenceFunction fpDNR = new PertinenceFunction(indicesDNR);
        
        // Regras para o artigo original. Total de 27 regras
		// Regra 1 - Good | Good | Small
		if (OSCRisGood(fpOSCR) && OMSRisGood(fpOMSR) && DNRisSmall(fpDNR)){
            int rule = 0;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 1) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 1) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 1);
            double[] values = {fpOSCR.calculate(OSCR, 1), fpOMSR.calculate(OMSR, 1), fpDNR.calculate(DNR, 1)};
            alpha[rule] = min(values);
        }
        // Regra 2 - Good | Good | Medium
		if (OSCRisGood(fpOSCR) && OMSRisGood(fpOMSR) && DNRisMedium(fpDNR)){
            int rule = 1;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 1) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 1) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 2);
            double[] values = {fpOSCR.calculate(OSCR, 1), fpOMSR.calculate(OMSR, 1), fpDNR.calculate(DNR, 2)};
            alpha[rule] = min(values);
        }
        // Regra 3 - Good | Good | Big
		if (OSCRisGood(fpOSCR) && OMSRisGood(fpOMSR) && DNRisBig(fpDNR)){
            int rule = 2;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 1) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 1) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 3);
            double[] values = {fpOSCR.calculate(OSCR, 1), fpOMSR.calculate(OMSR, 1), fpDNR.calculate(DNR, 3)};
            alpha[rule] = min(values);
        }

        // Regra 4 - Good | Reasonable | Small
        if (OSCRisGood(fpOSCR) && OMSRisReasonable(fpOMSR) && DNRisSmall(fpDNR)){
            int rule = 3;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 1) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 2) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 1);
            double[] values = {fpOSCR.calculate(OSCR, 1), fpOMSR.calculate(OMSR, 2), fpDNR.calculate(DNR, 1)};
            alpha[rule] = min(values);
        }
        // Regra 5 - Good | Reasonable | Medium
		if (OSCRisGood(fpOSCR) && OMSRisReasonable(fpOMSR) && DNRisMedium(fpDNR)){
            int rule = 4;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 1) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 2) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 2);
            double[] values = {fpOSCR.calculate(OSCR, 1), fpOMSR.calculate(OMSR, 2), fpDNR.calculate(DNR, 2)};
            alpha[rule] = min(values);
        }
        // Regra 6 - Good | Reasonable | Big
		if (OSCRisGood(fpOSCR) && OMSRisReasonable(fpOMSR) && DNRisBig(fpDNR)){
            int rule = 5;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 1) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 2) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 3);
            double[] values = {fpOSCR.calculate(OSCR, 1), fpOMSR.calculate(OMSR, 2), fpDNR.calculate(DNR, 3)};
            alpha[rule] = min(values);
        }

        // Regra 7 - Good | Bad | Small
        if (OSCRisGood(fpOSCR) && OMSRisBad(fpOMSR) && DNRisSmall(fpDNR)){
            int rule = 6;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 1) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 3) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 1);
            double[] values = {fpOSCR.calculate(OSCR, 1), fpOMSR.calculate(OMSR, 3), fpDNR.calculate(DNR, 1)};
            alpha[rule] = min(values);
        }
        // Regra 8 - Good | Bad | Medium
		if (OSCRisGood(fpOSCR) && OMSRisBad(fpOMSR) && DNRisMedium(fpDNR)){
            int rule = 7;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 1) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 3) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 2);
            double[] values = {fpOSCR.calculate(OSCR, 1), fpOMSR.calculate(OMSR, 3), fpDNR.calculate(DNR, 2)};
            alpha[rule] = min(values);
        }
        // Regra 9 - Good | Bad | Big
		if (OSCRisGood(fpOSCR) && OMSRisBad(fpOMSR) && DNRisBig(fpDNR)){
            int rule = 8;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 1) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 3) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 3);
            double[] values = {fpOSCR.calculate(OSCR, 1), fpOMSR.calculate(OMSR, 3), fpDNR.calculate(DNR, 3)};
            alpha[rule] = min(values);
        }

        // Regra 10 - Reasonable | Good | Small
		if (OSCRisReasonable(fpOSCR) && OMSRisGood(fpOMSR) && DNRisSmall(fpDNR)){
            int rule = 9;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 2) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 1) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 1);
            double[] values = {fpOSCR.calculate(OSCR, 2), fpOMSR.calculate(OMSR, 1), fpDNR.calculate(DNR, 1)};
            alpha[rule] = min(values);
        }
        // Regra 11 - Reasonable | Good | Medium
		if (OSCRisReasonable(fpOSCR) && OMSRisGood(fpOMSR) && DNRisMedium(fpDNR)){
            int rule = 10;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 2) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 1) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 2);
            double[] values = {fpOSCR.calculate(OSCR, 2), fpOMSR.calculate(OMSR, 1), fpDNR.calculate(DNR, 2)};
            alpha[rule] = min(values);
        }
        // Regra 12 - Reasonable | Good | Big
		if (OSCRisReasonable(fpOSCR) && OMSRisGood(fpOMSR) && DNRisBig(fpDNR)){
            int rule = 11;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 2) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 1) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 3);
            double[] values = {fpOSCR.calculate(OSCR, 2), fpOMSR.calculate(OMSR, 1), fpDNR.calculate(DNR, 3)};
            alpha[rule] = min(values);
        }

        // Regra 13 - Reasonable | Reasonable | Small
        if (OSCRisReasonable(fpOSCR) && OMSRisReasonable(fpOMSR) && DNRisSmall(fpDNR)){
            int rule = 12;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 2) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 2) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 1);
            double[] values = {fpOSCR.calculate(OSCR, 2), fpOMSR.calculate(OMSR, 2), fpDNR.calculate(DNR, 1)};
            alpha[rule] = min(values);
        }
        // Regra 14 - Reasonable | Reasonable | Medium
		if (OSCRisReasonable(fpOSCR) && OMSRisReasonable(fpOMSR) && DNRisMedium(fpDNR)){
            int rule = 13;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 2) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 2) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 2);
            double[] values = {fpOSCR.calculate(OSCR, 2), fpOMSR.calculate(OMSR, 2), fpDNR.calculate(DNR, 2)};
            alpha[rule] = min(values);
        }
        // Regra 15 - Reasonable | Reasonable | Big
		if (OSCRisReasonable(fpOSCR) && OMSRisReasonable(fpOMSR) && DNRisBig(fpDNR)){
            int rule = 14;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 2) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 2) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 3);
            double[] values = {fpOSCR.calculate(OSCR, 2), fpOMSR.calculate(OMSR, 2), fpDNR.calculate(DNR, 3)};
            alpha[rule] = min(values);
        }

        // Regra 16 - Reasonable | Bad | Small
        if (OSCRisReasonable(fpOSCR) && OMSRisBad(fpOMSR) && DNRisSmall(fpDNR)){
            int rule = 15;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 2) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 3) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 1);
            double[] values = {fpOSCR.calculate(OSCR, 2), fpOMSR.calculate(OMSR, 3), fpDNR.calculate(DNR, 1)};
            alpha[rule] = min(values);
        }
        // Regra 17 - Reasonable | Bad | Medium
		if (OSCRisReasonable(fpOSCR) && OMSRisBad(fpOMSR) && DNRisMedium(fpDNR)){
            int rule = 16;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 2) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 3) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 2);
            double[] values = {fpOSCR.calculate(OSCR, 2), fpOMSR.calculate(OMSR, 3), fpDNR.calculate(DNR, 2)};
            alpha[rule] = min(values);
        }
        // Regra 18 - Reasonable | Bad | Big
		if (OSCRisReasonable(fpOSCR) && OMSRisBad(fpOMSR) && DNRisBig(fpDNR)){
            int rule = 17;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 2) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 3) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 3);
            double[] values = {fpOSCR.calculate(OSCR, 2), fpOMSR.calculate(OMSR, 3), fpDNR.calculate(DNR, 3)};
            alpha[rule] = min(values);
        }

        // Regra 19 - Bad | Good | Small
		if (OSCRisBad(fpOSCR) && OMSRisGood(fpOMSR) && DNRisSmall(fpDNR)){
            int rule = 18;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 3) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 1) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 1);
            double[] values = {fpOSCR.calculate(OSCR, 3), fpOMSR.calculate(OMSR, 1), fpDNR.calculate(DNR, 1)};
            alpha[rule] = min(values);
        }
        // Regra 20 - Bad | Good | Medium
		if (OSCRisBad(fpOSCR) && OMSRisGood(fpOMSR) && DNRisMedium(fpDNR)){
            int rule = 19;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 3) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 1) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 2);
            double[] values = {fpOSCR.calculate(OSCR, 3), fpOMSR.calculate(OMSR, 1), fpDNR.calculate(DNR, 2)};
            alpha[rule] = min(values);
        }
        // Regra 21 - Bad | Good | Big
		if (OSCRisBad(fpOSCR) && OMSRisGood(fpOMSR) && DNRisBig(fpDNR)){
            int rule = 20;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 3) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 1) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 3);
            double[] values = {fpOSCR.calculate(OSCR, 3), fpOMSR.calculate(OMSR, 1), fpDNR.calculate(DNR, 3)};
            alpha[rule] = min(values);
        }

        // Regra 22 - Bad | Reasonable | Small
        if (OSCRisBad(fpOSCR) && OMSRisReasonable(fpOMSR) && DNRisSmall(fpDNR)){
            int rule = 21;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 3) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 2) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 1);
            double[] values = {fpOSCR.calculate(OSCR, 3), fpOMSR.calculate(OMSR, 2), fpDNR.calculate(DNR, 1)};
            alpha[rule] = min(values);
        }
        // Regra 23 - Bad | Reasonable | Medium
		if (OSCRisBad(fpOSCR) && OMSRisReasonable(fpOMSR) && DNRisMedium(fpDNR)){
            int rule = 22;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 3) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 2) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 2);
            double[] values = {fpOSCR.calculate(OSCR, 3), fpOMSR.calculate(OMSR, 2), fpDNR.calculate(DNR, 2)};
            alpha[rule] = min(values);
        }
        // Regra 24 - Bad | Reasonable | Big
		if (OSCRisBad(fpOSCR) && OMSRisReasonable(fpOMSR) && DNRisBig(fpDNR)){
            int rule = 23;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 3) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 2) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 3);
            double[] values = {fpOSCR.calculate(OSCR, 3), fpOMSR.calculate(OMSR, 2), fpDNR.calculate(DNR, 3)};
            alpha[rule] = min(values);
        }

        // Regra 25 - Bad | Bad | Small
        if (OSCRisBad(fpOSCR) && OMSRisBad(fpOMSR) && DNRisSmall(fpDNR)){
            int rule = 24;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 3) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 3) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 1);
            double[] values = {fpOSCR.calculate(OSCR, 3), fpOMSR.calculate(OMSR, 3), fpDNR.calculate(DNR, 1)};
            alpha[rule] = min(values);
        }
        // Regra 26 - Bad | Bad | Medium
		if (OSCRisBad(fpOSCR) && OMSRisBad(fpOMSR) && DNRisMedium(fpDNR)){
            int rule = 25;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 3) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 3) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 2);
            double[] values = {fpOSCR.calculate(OSCR, 3), fpOMSR.calculate(OMSR, 3), fpDNR.calculate(DNR, 2)};
            alpha[rule] = min(values);
        }
        // Regra 27 - Bad | Bad | Big
		if (OSCRisBad(fpOSCR) && OMSRisBad(fpOMSR) && DNRisBig(fpDNR)){
            int rule = 26;
            y[rule] = matrixRulesCoeffs[rule][0] + matrixRulesCoeffs[rule][1]*fpOSCR.calculate(OSCR, 3) + matrixRulesCoeffs[rule][2]*fpOMSR.calculate(OMSR, 3) + matrixRulesCoeffs[rule][3]*fpDNR.calculate(DNR, 3);
            double[] values = {fpOSCR.calculate(OSCR, 3), fpOMSR.calculate(OMSR, 3), fpDNR.calculate(DNR, 3)};
            alpha[rule] = min(values);
        }

        double sup = 0.0;
        double inf = 0.0;
        for (int i = 0; i < y.length; i++){
            sup += (alpha[i] * y[i]);
            inf += alpha[i];
        }

        CTR = sup / inf;

        return CTR;
    }

    private double min(double[] values){
        double minValue = Double.MAX_VALUE;
        for (int i = 0; i < values.length; i++){
            if (values[i] < minValue) minValue = values[i];
        }

        return minValue;
    }

    private boolean OSCRisGood(PertinenceFunction fp){
        //if ((fp.calculate(OSCR, 1) > fp.calculate(OSCR, 2)) && (fp.calculate(OSCR, 1) > fp.calculate(OSCR, 3))){
        if (fp.calculate(OSCR, 1) > 0){
            return true;
        }
        return false;
    }

    private boolean OSCRisReasonable(PertinenceFunction fp){
        //if ((fp.calculate(OSCR, 2) > fp.calculate(OSCR, 1)) && (fp.calculate(OSCR, 2) > fp.calculate(OSCR, 3))){
        if (fp.calculate(OSCR, 2) > 0){
            return true;
        }
        return false;
    }

    private boolean OSCRisBad(PertinenceFunction fp){
        //if ((fp.calculate(OSCR, 3) > fp.calculate(OSCR, 1)) && (fp.calculate(OSCR, 3) > fp.calculate(OSCR, 2))){
        if (fp.calculate(OSCR, 3) > 0){
            return true;
        }
        return false;
    }

    private boolean OMSRisGood(PertinenceFunction fp){
        //if ((fp.calculate(OMSR, 1) > fp.calculate(OMSR, 2)) && (fp.calculate(OMSR, 1) > fp.calculate(OMSR, 3))){
        if (fp.calculate(OMSR, 1) > 0){
            return true;
        }
        return false;
    }

    private boolean OMSRisReasonable(PertinenceFunction fp){
        //if ((fp.calculate(OMSR, 2) > fp.calculate(OMSR, 1)) && (fp.calculate(OMSR, 2) > fp.calculate(OMSR, 3))){
        if (fp.calculate(OMSR, 2) > 0){
            return true;
        }
        return false;
    }

    private boolean OMSRisBad(PertinenceFunction fp){
        //if ((fp.calculate(OMSR, 3) > fp.calculate(OMSR, 1)) && (fp.calculate(OMSR, 3) > fp.calculate(OMSR, 2))){
        if (fp.calculate(OMSR, 3) > 0){
            return true;
        }
        return false;
    }

    private boolean DNRisSmall(PertinenceFunction fp){
        //if ((fp.calculate(DNR, 1) > fp.calculate(DNR, 2)) && (fp.calculate(DNR, 1) > fp.calculate(DNR, 3))){
        if (fp.calculate(DNR, 1) > 0){
            return true;
        }
        return false;
    }

    private boolean DNRisMedium(PertinenceFunction fp){
        //if ((fp.calculate(DNR, 2) > fp.calculate(DNR, 1)) && (fp.calculate(DNR, 2) > fp.calculate(DNR, 3))){
        if (fp.calculate(DNR, 2) > 0){
            return true;
        }
        return false;
    }

    private boolean DNRisBig(PertinenceFunction fp){
        //if ((fp.calculate(DNR, 3) > fp.calculate(DNR, 1)) && (fp.calculate(DNR, 3) > fp.calculate(DNR, 2))){
        if (fp.calculate(DNR, 3) > 0){
            return true;
        }
        return false;
    }
}   

