package routing;

import java.util.List;

import parameters.SimulationParameters;
import utility.PertinenceFunction;

public class FuzzyRules {
	
	private int numRules;
	private double[] y; 
	private double[] w; //pesos
	final SimulationParameters parameters = new SimulationParameters();
	
	public FuzzyRules(int numRules) {
		this.numRules = numRules;
		this.y = new double[numRules];
		this.w = new double[numRules];
	}
	
	public double[] applyRules(FuzzyAlgorithmSolution solution, double[] listOfCoeff){
				
		double[] indicesOSCR ={0.1,0.3,0.2,0.4,0.6,0.8,0.7,0.9};
		double[] indicesOSMR ={0.1,0.2,0.1,0.3,0.5,0.7,0.6,0.9};
		double[] indicesDNR ={0.1,0.2,0.1,0.4,0.6,0.9,0.8,0.9};
		double[] fixW = {1,1,1,1,1,1,1};
		
		for (int i = 0; i < fixW.length; i++) {
			w[i] = fixW[i];
		}
		
		//Caso esteja no processo de otimiza��o, os indices n�o ser�o nulos
		if(listOfCoeff!=null) {
			if (listOfCoeff.length == 31){
				for (int i = 0; i < (parameters.getDimension()-w.length)/3; i++) {
					indicesOSCR[i]= listOfCoeff[i];
					indicesOSMR[i]= listOfCoeff[i+8];
					indicesDNR[i]= listOfCoeff[i+16];
				}
				int rulesIndice = parameters.getDimension()-w.length;
				for (int i = 0; i < w.length; i++) {
					w[i] = listOfCoeff[i+rulesIndice];
				}
			}
			if (listOfCoeff.length == 24){
				for (int i = 0; i < (parameters.getDimension()-w.length)/3; i++) {
					indicesOSCR[i]=listOfCoeff[i];
					indicesOSMR[i]=listOfCoeff[i+8];
					indicesDNR[i]=listOfCoeff[i+16];
				}
				int rulesIndice = parameters.getDimension()-w.length;
				for (int i = 0; i < w.length; i++) {
					w[i] = 1;
				}
			}
			if (listOfCoeff.length == 7){
				for (int i = 0; i < w.length; i++) {
					w[i] = listOfCoeff[i];
				}
			}
		}
		
		PertinenceFunction fpOSCR = new PertinenceFunction(indicesOSCR);
		PertinenceFunction fpOSMR = new PertinenceFunction(indicesOSMR);
		PertinenceFunction fpDNR = new PertinenceFunction(indicesDNR);
		//Regra1
		y[0] = 2 - fpOSCR.calculate(solution.getOSCR(), 2);
		//Regra2
		//y[1] = 1 + fpOSCR.calculate(solution.getOSCR(), 3) - fpOSCR.calculate(solution.getDNR(), 1);
		y[1] = 1 + fpOSCR.calculate(solution.getOSCR(), 3) - fpOSCR.calculate(solution.getOSCR(), 1);
		//Regra3
		y[2] = 0.5 + 2*fpOSCR.calculate(solution.getOSCR(), 2);
		//Regra4
		y[3] = 2 - 0.5*fpOSMR.calculate(solution.getOSMR(), 1);
		//Regra5
		y[4] = 2 - fpDNR.calculate(solution.getDNR(), 3);
		//Regra6
		y[5] = 2 - 0.5*fpDNR.calculate(solution.getDNR(), 2) - fpOSCR.calculate(solution.getOSCR(), 1);
		//Regra7
		y[6] = 2 + 0.5*fpDNR.calculate(solution.getDNR(), 3) + 0.5*fpOSMR.calculate(solution.getOSMR(), 3);
		return y.clone();
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
	
	public int getNumRules() {
		return numRules;
	}
	public void setNumRules(int numRules) {
		this.numRules = numRules;
	}
	public double[] getY() {
		return y;
	}
	public void setY(double[] y) {
		this.y = y;
	}
	

}
