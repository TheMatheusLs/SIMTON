package test;

import arquitecture.OpticalAmplifier;
/**
 * Descreve o teste de criação de um amplificador óptico.
 * @author André 
 */
public class OpticalAmplifierTest {

	public static void main(final String[] args) {
		
		
		final double gaindB = 20.0;
		final double noisefactor = 5.5;
		final OpticalAmplifier amplifier = new OpticalAmplifier(gaindB,noisefactor);
		
		System.out.println("Gain (db):"+amplifier.getGainIndB()+" Gain (linear): "+amplifier.getGainInLinear());
		System.out.println("NF (db):"+amplifier.getNoiseFactorIndB()+" NF (linear): "+amplifier.getNoiseFactorInLinear());

	}

}
