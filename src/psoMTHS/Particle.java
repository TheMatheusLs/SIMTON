package psoMTHS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import exceptions.InvalidListOfCoefficientsException;
import exceptions.InvalidNodeIdException;
import exceptions.InvalidNumberOfFrequencySlotsException;
import exceptions.InvalidRoutesException;
import routing.RoutingAlgorithmSolution;
import simulation.IRMLSASimulation;
import simulation.RMLSASimulation;
import types.FuzzyMetricMethodType;
import types.ParticlePSOType;
/*
 *  Classe para criar a particula usando as 3 métricas do trapézio.
 * 
 *  Autor: Matheus Lôbo
 * 
 */
import types.RoutingAlgorithmType;

public class Particle {
    
    private static final double MAX_POSITION = 1.0;
	private static final double MIN_POSITION = -1.0;
	private static final double MAX_VELOCITY = 1.0;
	private static final double MIN_VELOCITY = -1.0;
	private static final double SIDESTEP_SCALE = 10.0;
	private static final double SIDESTEP_THRES = 10.0;
	private static final double ACC_CONST = 2.05;
	private static final double CONSTRIC_FACTOR = 0.72984;
	private static final double CLOW = 1.0;
	private static final double CUP = 2.0;	
	private transient final Random rand = new Random();	
	private static double gBestFitnessPPS = 0.0; 
	private static List<Double> gBestPositionPPS = new ArrayList<Double>();
	private transient double pBestPPR;
	private transient List<Double> pBestXPPR;
	private transient double fitnessPPR;
	private transient final List<Double> velocityPPR;
	private transient final List<Double> positionPPR;
	private transient int dimensionPPR;
	private transient final boolean sidestepPPR;
	private transient double sidestepThresPPR;
    private transient double[] coeffSeed;
    private ParticlePSOType particleType;

    //Construtor para o Fuzzy
	public Particle(final ParticlePSOType particleType, final boolean sidestep_p) {	
        this.sidestepPPR = sidestep_p;
		this.sidestepThresPPR = SIDESTEP_THRES*(MAX_POSITION-MIN_POSITION)/100;
        this.particleType = particleType;

        if (particleType.equals(ParticlePSOType.FULL_METRIC) || particleType.equals(ParticlePSOType.FULL_METRIC_SEED)){
            this.setDimension(8+8+8);
        } else if (particleType.equals(ParticlePSOType.WI_WEIGHT)){
            this.setDimension(7);
        } else if (particleType.equals(ParticlePSOType.TWO_POINTS_METRIC)){
            this.setDimension(6);
		}
        
		this.velocityPPR = new ArrayList<Double>(getDimension());
		this.positionPPR = new ArrayList<Double>(getDimension());	
        
		double[] posTemp = new double[getDimension()];
		
        // Inicializa os valores para a métrica Full
        if (particleType.equals(ParticlePSOType.FULL_METRIC) || particleType.equals(ParticlePSOType.FULL_METRIC_SEED)){
            int a=0,b=1,c=2,d=3,e=4,f=5,g=6,h=7;
            for (int i = 0; i < getDimension()/8; i++) {   
                posTemp[a+(i*8)]=randomUniformGenerator(0,1);
                posTemp[b+(i*8)]=randomUniformGenerator(posTemp[a+(i*8)],1);
                posTemp[c+(i*8)]=randomUniformGenerator(0,posTemp[b+(i*8)]);
                posTemp[d+(i*8)]=randomUniformGenerator((posTemp[c+(i*8)]>posTemp[a+(i*8)])? posTemp[c+(i*8)]:posTemp[a+(i*8)],1);
                posTemp[e+(i*8)]=randomUniformGenerator(posTemp[d+(i*8)],1);
                posTemp[f+(i*8)]=randomUniformGenerator(posTemp[e+(i*8)],1);
                posTemp[g+(i*8)]=randomUniformGenerator(posTemp[b+(i*8)],posTemp[f+(i*8)]);
                posTemp[h+(i*8)]=randomUniformGenerator((posTemp[g+(i*8)]>posTemp[f+(i*8)])? posTemp[g+(i*8)]:posTemp[f+(i*8)],1);
                // posTemp[a+(i*8)]=randomUniformGenerator(0,0.4);
                // posTemp[b+(i*8)]=randomUniformGenerator(posTemp[a+(i*8)],0.7);
                // posTemp[c+(i*8)]=randomUniformGenerator(0,posTemp[b+(i*8)]);
                // posTemp[d+(i*8)]=randomUniformGenerator((posTemp[c+(i*8)]>posTemp[a+(i*8)])? posTemp[c+(i*8)]:posTemp[a+(i*8)],0.6);
                // posTemp[e+(i*8)]=randomUniformGenerator(posTemp[d+(i*8)],0.8);
                // posTemp[f+(i*8)]=randomUniformGenerator(posTemp[e+(i*8)],1);
                // posTemp[g+(i*8)]=randomUniformGenerator(posTemp[b+(i*8)],posTemp[f+(i*8)]);
                // posTemp[h+(i*8)]=randomUniformGenerator((posTemp[e+(i*8)]>posTemp[g+(i*8)])? posTemp[e+(i*8)]:posTemp[g+(i*8)],1);
            }
        } else if (particleType.equals(ParticlePSOType.WI_WEIGHT)){
            for (int i = 0; i < getDimension(); i++) {   
                posTemp[i]= randomUniformGenerator(0,1);
            }
        } else if (particleType.equals(ParticlePSOType.TWO_POINTS_METRIC)){
			for (int i = 0; i < getDimension(); i++) {   
                posTemp[i]= randomUniformGenerator(0.5,0.95);
            }
		}

		for (int i = 0; i < posTemp.length; i++) {
			this.positionPPR.add(posTemp[i]);
			this.velocityPPR.add(this.randomUniformGenerator(MIN_VELOCITY, MAX_VELOCITY));
		}

		this.pBestXPPR = this.positionPPR;
		this.pBestPPR = 0.0;
    }
    //Construtor para o Fuzzy
	public Particle(final ParticlePSOType particleType, final boolean sidestep_p, final double[] coeffSeed) {	
        this.sidestepPPR = sidestep_p;
		this.sidestepThresPPR = SIDESTEP_THRES*(MAX_POSITION-MIN_POSITION)/100;
        this.coeffSeed = coeffSeed;
        this.particleType = particleType;

        if (particleType.equals(ParticlePSOType.FULL_METRIC) || particleType.equals(ParticlePSOType.FULL_METRIC_SEED)){
            this.setDimension(8+8+8);
        }

		this.velocityPPR = new ArrayList<Double>(getDimension());
		this.positionPPR = new ArrayList<Double>(getDimension());	

		double[] posTemp = new double[getDimension()];
		
        // Inicializa os valores para a seed
        if (particleType.equals(ParticlePSOType.FULL_METRIC_SEED) && (posTemp.length == coeffSeed.length)){
            posTemp = coeffSeed;
        }

		for (int i = 0; i < posTemp.length; i++) {
			this.positionPPR.add(posTemp[i]);
			this.velocityPPR.add(this.randomUniformGenerator(MIN_VELOCITY, MAX_VELOCITY));
		}

		this.pBestXPPR = this.positionPPR;
		this.pBestPPR = 0.0;
    }
    /**
	 * Método para retornar a variável pBestPPR.
	 */
	public double getpBest() {
		return pBestPPR;
	}
    public int getDimension() {
		return dimensionPPR;
	}

	public void setDimension(final int dimensionPPR) {
		this.dimensionPPR = dimensionPPR;
	}
    /**
	 * Método para retornar a posição da melhor partícula.
	 */
	public static List<Double> getgBestPosition() {
		return gBestPositionPPS;
	}
    /**
	 * Método para configurar a posição da melhor partícula.
	 */
	public static void setgBestPosition(final List<Double> gBestPosition) {
		Particle.gBestPositionPPS = gBestPosition;
	}
    /**
	 * Método para configurar o melhor fitness.
	 */
	public static void setgBestFitness(final double gBest) {
		Particle.gBestFitnessPPS = gBest;
	}
    /**
	 * Método para retornar o melhor fitness.
	 */
	public static double getgBestFitness() {
		return gBestFitnessPPS;
	}
    /**
	 * Método para retornar o fitness da partícula.
	 */
	public double getFitness() {
		return fitnessPPR;
	}
    /**
	 * Método para retornar a variável pBestXPPR.
	 */		
	public List<Double> getpBestX() {
		return pBestXPPR;
	}
    private double randomUniformGenerator(final double minimum, final double maximum){
		return minimum + this.rand.nextDouble()*(maximum-minimum);		
	}	

    public double fitnessEvaluation(final OpticalLink[][] network, final List<OpticalSwitch> listOfNodes, ArrayList<List<RoutingAlgorithmSolution>> allRoutes, final int numberOfCalls, final RoutingAlgorithmType rType, final double meanRateBetCalls, FuzzyMetricMethodType fuzzyLogicType, int KYEN, double[][] matrixRulesCoeffs) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException, InvalidListOfCoefficientsException{

		final IRMLSASimulation simRMLSAInstance = RMLSASimulation.getRMLSASimulationInstance();
		double[] positionAux = new double[positionPPR.size()];
		for (int i = 0; i < positionPPR.size(); i++){
			positionAux[i] = positionPPR.get(i);
		}
		final double pb_temp = simRMLSAInstance.simulation(network, listOfNodes, numberOfCalls, rType, meanRateBetCalls, allRoutes, positionAux, fuzzyLogicType, KYEN, matrixRulesCoeffs); 

		if (pb_temp != 0.0){
			// Atualiza as informações da partícula
			if (1/pb_temp > pBestPPR){
				this.pBestPPR = 1/pb_temp;
				this.pBestXPPR = positionPPR;
			}
			//Atualiza a posição global caso esteja vazia
			if (getgBestPosition().isEmpty()){
				setgBestPosition(positionPPR);
				setgBestFitness(this.pBestPPR);
			}
			//Update global best info if needed
			if (1/pb_temp > getgBestFitness()){
				setgBestFitness(1/pb_temp); // O fitness será 1 sobre a probabilidade de bloqueio
				setgBestPosition(positionPPR);
			}
			fitnessPPR = 1/pb_temp;
			return 1/pb_temp; // NOPMD by Andr� on 27/06/17 10:51
		}

		return 0.0; //
	}// end fitnessEvaluation
    /**
	 * Método para atualizar a velocidade da part�cula.
	 */
	public void updateVelocity(final int time){
		for(int i=0;i<this.velocityPPR.size();i++){
			final double inertialFactor = 0.4 + 0.6/(1+Math.exp(0.5*(time-20)));


			final double temp = this.velocityPPR.get(i)*inertialFactor + ACC_CONST*this.rand.nextDouble()*(this.pBestXPPR.get(i)-this.positionPPR.get(i)) // NOPMD by Andr� on 27/06/17 10:04
					+ ACC_CONST*this.rand.nextDouble()*(getgBestPosition().get(i)-this.positionPPR.get(i)); // NOPMD by Andr� on 27/06/17 10:04


			this.velocityPPR.set(i, temp); 


			if (Math.abs(this.pBestXPPR.get(i)-this.positionPPR.get(i)) < this.sidestepThresPPR // NOPMD by Andr� on 27/06/17 10:04
					&& Math.abs(getgBestPosition().get(i)-this.positionPPR.get(i)) < this.sidestepThresPPR // NOPMD by Andr� on 27/06/17 10:04
					&& this.velocityPPR.get(i) < this.sidestepThresPPR
					&& this.sidestepPPR){

				if (this.velocityPPR.get(i) < 0){
					final double velocid = this.velocityPPR.get(i) - this.sidestepThresPPR*SIDESTEP_SCALE;
					this.velocityPPR.set(i, velocid); 
				}else{
					final double velocid = this.velocityPPR.get(i) + this.sidestepThresPPR*SIDESTEP_SCALE;
					this.velocityPPR.set(i, velocid);
					this.sidestepThresPPR = this.sidestepThresPPR/2;
				}	 
			}

			if (this.velocityPPR.get(i) > MAX_VELOCITY){
				this.velocityPPR.set(i, MAX_VELOCITY);
			} else if ( this.velocityPPR.get(i) < MIN_VELOCITY ){
				this.velocityPPR.set(i, MIN_VELOCITY);
			}				 
		}		
	}
    /**
	 * Método para atualizar a velocidade da partácula. Usando o tipo global
	 */	
	public void updateVelocity(final List<Double> lbestX){
		for(int i=0; i<this.velocityPPR.size(); i++){

			final double temp = CONSTRIC_FACTOR * (this.velocityPPR.get(i)
					+ ACC_CONST*this.rand.nextDouble()*(this.pBestXPPR.get(i) - this.positionPPR.get(i)) 
					+ ACC_CONST*this.rand.nextDouble()*(lbestX.get(i) - this.positionPPR.get(i))); 

			this.velocityPPR.set(i, temp);  

			if (Math.abs(this.pBestXPPR.get(i)-this.positionPPR.get(i)) < this.sidestepThresPPR 
					&& Math.abs(getgBestPosition().get(i)-this.positionPPR.get(i)) < this.sidestepThresPPR 
					&& this.velocityPPR.get(i) < this.sidestepThresPPR
					&& this.sidestepPPR){

				if (this.velocityPPR.get(i) < 0){
					final double velocid = this.velocityPPR.get(i) - this.sidestepThresPPR*SIDESTEP_SCALE;
					this.velocityPPR.set(i, velocid); 
				}else{
					final double velocid = this.velocityPPR.get(i) + this.sidestepThresPPR*SIDESTEP_SCALE;
					this.velocityPPR.set(i, velocid);
					this.sidestepThresPPR = this.sidestepThresPPR/2;
				}	 
			}	  

			if (this.velocityPPR.get(i) > MAX_VELOCITY){
				this.velocityPPR.set(i, MAX_VELOCITY);
			} else if ( this.velocityPPR.get(i) < MIN_VELOCITY ){
				this.velocityPPR.set(i, MIN_VELOCITY);
			}
		}
	}
	/**
	 * Método para atualizar a velocidade da partícula. Usando local best
	 */	
	public void updateVelocity(final List<Double> lbestX, final int time, final int iterations, final double worstFitness){

		for(int i=0; i<this.velocityPPR.size(); i++){
			double inertialFactor;
			final double grade = (fitnessPPR - worstFitness)/(getgBestFitness() - worstFitness);
			final double c2k = CLOW + (CUP - CLOW)*grade;

			inertialFactor = 0.4 + 0.5*(iterations - time)/iterations;

			final double temp = this.velocityPPR.get(i)*inertialFactor
					+c2k*this.rand.nextDouble()*(this.pBestXPPR.get(i) - this.positionPPR.get(i)) 
					+ACC_CONST*this.rand.nextDouble()*(lbestX.get(i) - this.positionPPR.get(i));

			this.velocityPPR.set(i, temp); 

			if (Math.abs(this.pBestXPPR.get(i)-this.positionPPR.get(i)) < this.sidestepThresPPR 
					&& Math.abs(getgBestPosition().get(i)-this.positionPPR.get(i)) < this.sidestepThresPPR 
					&& this.velocityPPR.get(i) < this.sidestepThresPPR
					&& this.sidestepPPR){

				if (this.velocityPPR.get(i) < 0){
					final double velocid = this.velocityPPR.get(i) - this.sidestepThresPPR*SIDESTEP_SCALE;
					this.velocityPPR.set(i, velocid); 
				}else{
					final double velocid = this.velocityPPR.get(i) + this.sidestepThresPPR*SIDESTEP_SCALE;
					this.velocityPPR.set(i, velocid);
					this.sidestepThresPPR = this.sidestepThresPPR/2;
				}	 
			}	  

			if (this.velocityPPR.get(i) > MAX_VELOCITY){
				this.velocityPPR.set(i, MAX_VELOCITY);
			} else if ( this.velocityPPR.get(i) < MIN_VELOCITY ){
				this.velocityPPR.set(i, MIN_VELOCITY);
			}
		}
	}
    /**
	 * Método para atualizar a posição da partícula.
	 * @author Isabella 
	 */		
	public void updatePosition(){
		
        double stepVelocity = 0.05;
        double[] xTemp = new double[this.positionPPR.size()];
        double y = 0; //resultado temporario da posição antes do ajuste de limites

        if (this.particleType.equals(ParticlePSOType.FULL_METRIC) || this.particleType.equals(ParticlePSOType.FULL_METRIC_SEED)){
            int a=0,b=1,c=2,d=3,e=4,f=5,g=6,h=7;
            for (int i = 0; i < xTemp.length/8; i++) {
                //************************update A*********************************
                y = this.positionPPR.get(a+(i*8)) + this.velocityPPR.get(a+(i*8))*stepVelocity;
                if(y<0)
                    xTemp[a+(i*8)] = 0;
                else if(y>1)
                    xTemp[a+(i*8)] = 1;
                else 
                    xTemp[a+(i*8)] = y;
                //*****************************************************************
                //************************update B*********************************
                y = this.positionPPR.get(b+(i*8)) + this.velocityPPR.get(b+(i*8))*stepVelocity;
                if(y<xTemp[a+(i*8)])
                    xTemp[b+(i*8)] = xTemp[a+(i*8)];
                else if(y>1)
                    xTemp[b+(i*8)] = 1;
                else
                    xTemp[b+(i*8)] = y;
                //*****************************************************************
                //************************update C*********************************
                y = this.positionPPR.get(c+(i*8)) + this.velocityPPR.get(c+(i*8))*stepVelocity;
                if(y<0)
                    xTemp[c+(i*8)] = 0;
                else if(y>xTemp[b+(i*8)])
                    xTemp[c+(i*8)] = xTemp[b+(i*8)];
                else
                    xTemp[c+(i*8)] = y;
                //*****************************************************************
                //************************update D*********************************
                y = this.positionPPR.get(d+(i*8)) + this.velocityPPR.get(d+(i*8))*stepVelocity;
                double limit = (xTemp[c+(i*8)]>xTemp[a+(i*8)])? xTemp[c+(i*8)]:xTemp[a+(i*8)];
                if(y<limit)
                    xTemp[d+(i*8)] = limit;
                else if(y>1)
                    xTemp[d+(i*8)] = 1;
                else
                    xTemp[d+(i*8)] = y;
                //*****************************************************************
                //************************update E*********************************
                y = this.positionPPR.get(e+(i*8)) + this.velocityPPR.get(e+(i*8))*stepVelocity;
                if(y<xTemp[d+(i*8)])
                    xTemp[e+(i*8)] = xTemp[d+(i*8)];
                else if(y>1)
                    xTemp[e+(i*8)] = 1;
                else
                    xTemp[e+(i*8)] = y;
                //*****************************************************************
                //************************update F*********************************
                y = this.positionPPR.get(f+(i*8)) + this.velocityPPR.get(f+(i*8))*stepVelocity;
                if(y<xTemp[e+(i*8)])
                    xTemp[f+(i*8)] = xTemp[e+(i*8)];
                else if(y>1)
                    xTemp[f+(i*8)] = 1;
                else
                    xTemp[f+(i*8)] = y;
                //*****************************************************************
                //************************update G*********************************
                y = this.positionPPR.get(g+(i*8)) + this.velocityPPR.get(g+(i*8))*stepVelocity;
                if(y<xTemp[b+(i*8)])
                    xTemp[g+(i*8)] = xTemp[b+(i*8)];
                else if(y>xTemp[f+(i*8)])
                    xTemp[g+(i*8)] = xTemp[f+(i*8)];
                else
                    xTemp[g+(i*8)] = y;
                //*****************************************************************
                //************************update H*********************************
                y = this.positionPPR.get(h+(i*8)) + this.velocityPPR.get(h+(i*8))*stepVelocity;
                limit = (xTemp[g+(i*8)]>xTemp[f+(i*8)])? xTemp[g+(i*8)]:xTemp[f+(i*8)];
                if(y<limit)
                    xTemp[h+(i*8)] = limit;
                else if(y>1)
                    xTemp[h+(i*8)] = 1;
                else
                    xTemp[h+(i*8)] = y;
                //*****************************************************************
            }//end for xTemp
        }//end if metric

        if (particleType.equals(ParticlePSOType.WI_WEIGHT) || particleType.equals(ParticlePSOType.TWO_POINTS_METRIC)){
            for (int i = 0; i < getDimension(); i++) {  
                y = this.positionPPR.get(i) + this.velocityPPR.get(i)*stepVelocity; 
                if(y < 0)
                    xTemp[i] = 0;
                else if(y>1)
                    xTemp[i] = 1;
                else
                    xTemp[i] = y;
            }
        }

		for(int i=0; i< this.positionPPR.size(); i++){
		    this.positionPPR.set(i, xTemp[i]);
		}
	}
    /**
	 * Método para configurar a posição da part�cula.
	 */		
	public void setPosition (final List<Double> newPosition){
		for(int i=0; i< this.positionPPR.size(); i++){
			this.positionPPR.set(i, newPosition.get(i));
		}

	}

}
