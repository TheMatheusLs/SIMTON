package test;

import java.util.ArrayList;
import java.util.List;

import arquitecture.OpticalLink;
import arquitecture.OpticalSpan;
import exceptions.InvalidFiberLengthException;
import exceptions.InvalidRoutesException;
import gain_algorithm.GainAlgorithm;
import parameters.SimulationParameters;
import utility.Function;

public class OpticalLinkTest {

	public static void main(String[] args) throws InvalidFiberLengthException, InvalidRoutesException {
		/**
		 * Identificador do optical link.
		 * @author Andr� 			
		 */
		final int opticalLinkId = 9;
		/**
		 * N� fonte do optical link.
		 * @author Andr� 			
		 */
		final int source = 5;
		final SimulationParameters parameter = new SimulationParameters();
		final double laserInLinear = Math.pow(10,parameter.getLaserPower()/10);
		final double switchLoss = Math.pow(10,parameter.getSwitchLoss()/10);
		final double AtenCoeff = parameter.getFiberAtenuationCoefficient();
		final double muxGain = Math.pow(10,parameter.getMuxLoss()/10);
		final int slot = 0;
		final int destination = 6;
		final double length = 100.0;
		final int srlg = 20;
		final List<OpticalLink> opticalLinkList = new ArrayList<OpticalLink>();
		Function function = new Function();
		
		final GainAlgorithm gain = GainAlgorithm.getGainInstance();
		
		final OpticalLink opticallink = new OpticalLink(opticalLinkId, source, destination, srlg, length);
		//OpticalLink opticallink2 = new OpticalLink(10, destination, 7, SRLG, length);
		
		gain.configureGain(opticallink);
		
		opticalLinkList.add(opticallink);
		//v.add(opticallink2);
		
		
		opticallink.allocate(slot, laserInLinear*switchLoss);
		//opticallink2.allocate(slot, opticallink.getPowerB(slot)*switchLoss);
		/*
		System.out.println("Fibra id: "+opticallink.getId()+" Source e destination: "+opticallink.getSource()+"-"+opticallink.getDestination()+" SRLG: "+opticallink.getSRLG());
		
		System.out.println("Teste de cria��o de Spans:");
		
		System.out.println("Booster: ganho (dB) "+opticallink.getBooster().getGainIndB()+" ganho(linear): "+opticallink.getBooster().getGainInLinear());
		
		for(Span s : opticallink.getSpans()){
			count++;
			System.out.println("Span "+count+" id Span: "+s.getId()+" fiberLength: "+s.getOpticalFiber().getLength()+" gain(dB):"+s.getOpticalAmplifier().getGainIndB()+" gain(Linear): "+s.getOpticalAmplifier().getGainInLinear());
			
		}*/
		
		double powerA = opticallink.getPowerA(slot);
		double powerB = opticallink.getPowerB(slot);
		//double powerA2 = opticallink2.getPowerA(slot);
		//double powerB2 = opticallink2.getPowerB(slot);
		
		System.out.println("Sinal no enlace 1 no ponto A: "+10*Math.log10(powerA));
		System.out.println("Sinal no enlace 1 ponto B: "+10*Math.log10(powerB));
		
		System.out.println("Booster enlace 1: "+opticallink.getBooster().getGainIndB());
		
	//	System.out.println("Sinal no enlace 2 no ponto A: "+10*Math.log10(powerA2));
	//	System.out.println("Sinal no enlace 2 ponto B: "+10*Math.log10(powerB2));
		
	//	System.out.println("Booster enlace 2: "+opticallink2.getBooster().getGainIndB());
		
		//////////////////////////////////////Calculo de OSNR considerando ASE.
		
		double signal = Math.pow(10,parameter.getLaserPower()/10);
		double noise = Math.pow(10,parameter.getLaserPower()/10)/Math.pow(10,parameter.getOSNRIn()/10);
		double boosterGain = 0.0;		
		double fiberGain = 0.0;
		double amplifierGain = 0.0;
		
		for(OpticalLink o : opticalLinkList){
			
			
			final double dioLossInLinear = Math.pow(10,parameter.getDioLoss()/10);
			final double switchGain = Math.pow(10,parameter.getSwitchLoss()/10);
			
			signal *= switchGain*muxGain;
			noise *= switchGain*muxGain;
			
			boosterGain = o.getBooster().getGainInLinear();			
			double noisefactorBooster = o.getBooster().getNoiseFactorInLinear();
			double addedNoiseByBooster = 500*(boosterGain-1)*o.getFrequency(slot)*SimulationParameters.getPlanck()*SimulationParameters.getSpacing()*noisefactorBooster;
			
			signal = signal*boosterGain;
			noise = (noise*boosterGain)+addedNoiseByBooster;
			
			for(OpticalSpan s : o.getSpans()){
				
				signal *= dioLossInLinear; //First connector in fiber
				noise = noise*dioLossInLinear;	
				
				fiberGain = AtenCoeff*s.getOpticalFiber().getLength();
				signal *= Math.pow(10,fiberGain/10);
				noise = noise*Math.pow(10,fiberGain/10);
				
				signal *= dioLossInLinear; //Second connector in fiber
				noise = noise*dioLossInLinear;	
				
				amplifierGain = s.getOpticalAmplifier().getGainInLinear();
				double noiseFactorAmplifier = s.getOpticalAmplifier().getNoiseFactorInLinear();			
				double addedNoiseByAmplifier = 500*(amplifierGain-1)*o.getFrequency(slot)*SimulationParameters.getPlanck()*SimulationParameters.getSpacing()*noiseFactorAmplifier;
								
				signal *= amplifierGain;
				noise = noise*amplifierGain+addedNoiseByAmplifier;
			}
			
			signal = signal*muxGain;
			noise = noise*muxGain;
			
		}
		
		System.out.println("Signal (dB): "+10*Math.log10(signal));
		System.out.println("Noise (dB): "+10*Math.log10(noise));
		System.out.println("OSNR (dB) : "+10*Math.log10(signal/noise));	
		
		System.out.println("Calculo com a fun��o");
		double osnr = function.evaluateOSNR(opticalLinkList, slot);	
		
		System.out.println("resultado da fun��o OSNR : "+10*Math.log10(osnr));
		
				

	}

}
