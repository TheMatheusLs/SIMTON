package pso; // NOPMD by Andr� on 27/06/17 10:51

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
import types.RoutingAlgorithmType;
/**
 * Descreve a part�cula usado no PSO.
 * @author Andr� 
 */
public class Particle {

	//Constantes//////////////////////////////////////
	/**
	 * Posi��o m�xima da part�cula.
	 * @author Andr� 
	 */
	private static final double MAX_POSITION = 1.0;
	/**
	 * Posi��o m�nima da part�cula.
	 * @author Andr� 
	 */
	private static final double MIN_POSITION = -1.0;
	/**
	 * Velocidade m�xima da part�cula.
	 * @author Andr� 
	 */
	private static final double MAX_VELOCITY = 1.0;
	/**
	 * Velocidade m�nima da part�cula.
	 * @author Andr� 
	 */
	private static final double MIN_VELOCITY = -1.0;
	/**
	 * Escala.
	 * @author Andr� 
	 */
	private static final double SIDESTEP_SCALE = 10.0;
	/**
	 * Limiar.
	 * @author Andr� 
	 */
	private static final double SIDESTEP_THRES = 10.0;
	/**
	 * Constante acc.
	 * @author Andr� 
	 */
	private static final double ACC_CONST = 2.05;
	/**
	 * Fator de constri��o.
	 * @author Andr� 
	 */
	private static final double CONSTRIC_FACTOR = 0.72984;
	/**
	 * Constante clow.
	 * @author Andr� 
	 */
	private static final double CLOW = 1.0;
	/**
	 * Constante clow.
	 * @author Andr� 
	 */
	private static final double CUP = 2.0;	
	///////////////////////////////////////////////////
	/**
	 * Objeto Random.
	 * @author Andr� 
	 */	
	private transient final Random rand = new Random();
	/**
	 * Var�avel GBest.
	 * @author Andr� 
	 */	
	private static double gBestPPS = 0.0; // NOPMD by Andr� on 27/06/17 09:54
	/**
	 * Vetor das posi��es da particula GBest.
	 * @author Andr� 
	 */	
	private static List<Double> gBestPositionPPS = new ArrayList<Double>();
	/**
	 * Var�avel pBestPPR.
	 * @author Andr� 
	 */	
	private transient double pBestPPR;
	/**
	 * Var�avel pBestXPPR.
	 * @author Andr� 
	 */	
	private transient List<Double> pBestXPPR;
	/**
	 * Var�avel fitnessPPR.
	 * @author Andr� 
	 */	
	private transient double fitnessPPR;
	/**
	 * Vetor de velocidade da part�cula.
	 * @author Andr� 
	 */	
	private transient final List<Double> velocityPPR;
	/**
	 * Vetor de posi��es da part�cula.
	 * @author Andr� 
	 */	
	private transient final List<Double> positionPPR;
	/**
	 * Dimens�o da part�cula.
	 * @author Andr� 
	 */	
	private transient int dimensionPPR;
	/**
	 * Var�avel sidestepPPR.
	 * @author Andr� 
	 */	
	private transient final boolean sidestepPPR;
	/**
	 * Var�avel sidestepThresPPR.
	 * @author Andr� 
	 */	
	private transient double sidestepThresPPR;
	/**
	 * Construtor da classe.
	 * @author Andr� 
	 */	
	/*public Particle(final int dimension, final boolean sidestep_p) {		
		this.sidestepPPR = sidestep_p;
		this.sidestepThresPPR = SIDESTEP_THRES*(MAX_POSITION-MIN_POSITION)/100;
		this.setDimension(dimension);
		this.velocityPPR = new ArrayList<Double>(dimension);
		this.positionPPR = new ArrayList<Double>(dimension);
		for(int i=0;i<2*dimension;i++){
			this.positionPPR.add(this.randomUniformGenerator(MIN_POSITION, MAX_POSITION)); 
			this.velocityPPR.add(this.randomUniformGenerator(MIN_VELOCITY, MAX_VELOCITY));	
		}
		this.pBestXPPR = this.positionPPR;
		this.pBestPPR = 0.0;
	}*/	

	//Construtor para o Fuzzy
	public Particle(final int dimension, final boolean sidestep_p) {		
		this.sidestepPPR = sidestep_p;
		this.sidestepThresPPR = SIDESTEP_THRES*(MAX_POSITION-MIN_POSITION)/100;
		this.setDimension(dimension);
		this.velocityPPR = new ArrayList<Double>(dimension);
		this.positionPPR = new ArrayList<Double>(dimension);
		
		int a=0,b=1,c=2,d=3,e=4,f=5,g=6,h=7;
		double[] xTemp = new double[dimension];
		
		for (int i = 0; i < (dimension-7)/8; i++) {
			xTemp[a+(i*8)]=randomUniformGenerator(0,0.4);
			xTemp[b+(i*8)]=randomUniformGenerator(xTemp[a+(i*8)],0.7);
			xTemp[c+(i*8)]=randomUniformGenerator(0,xTemp[b+(i*8)]);
			xTemp[d+(i*8)]=randomUniformGenerator((xTemp[c+(i*8)]>xTemp[a+(i*8)])? xTemp[c+(i*8)]:xTemp[a+(i*8)],0.6);
			xTemp[e+(i*8)]=randomUniformGenerator(xTemp[d+(i*8)],0.8);
			xTemp[f+(i*8)]=randomUniformGenerator(xTemp[e+(i*8)],1);
			xTemp[g+(i*8)]=randomUniformGenerator(xTemp[b+(i*8)],xTemp[f+(i*8)]);
			xTemp[h+(i*8)]=randomUniformGenerator((xTemp[e+(i*8)]>xTemp[g+(i*8)])? xTemp[e+(i*8)]:xTemp[g+(i*8)],1);
			// xTemp[a+(i*8)]=randomUniformGenerator(0,1);
			// xTemp[b+(i*8)]=randomUniformGenerator(xTemp[a+(i*8)],1);
			// xTemp[c+(i*8)]=randomUniformGenerator(0,xTemp[b+(i*8)]);
			// xTemp[d+(i*8)]=randomUniformGenerator((xTemp[c+(i*8)]>xTemp[a+(i*8)])? xTemp[c+(i*8)]:xTemp[a+(i*8)],1);
			// xTemp[e+(i*8)]=randomUniformGenerator(xTemp[d+(i*8)],1);
			// xTemp[f+(i*8)]=randomUniformGenerator(xTemp[e+(i*8)],1);
			// xTemp[g+(i*8)]=randomUniformGenerator(xTemp[b+(i*8)],xTemp[f+(i*8)]);
			// xTemp[h+(i*8)]=randomUniformGenerator((xTemp[g+(i*8)]>xTemp[f+(i*8)])? xTemp[g+(i*8)]:xTemp[f+(i*8)],1);
		}
//		inicia o restante do vetor
		for (int i = xTemp.length-7; i < xTemp.length; i++) {
			xTemp[i] = randomUniformGenerator(0,1);
		}
		
		for (int i = 0; i < xTemp.length; i++) {
			this.positionPPR.add(xTemp[i]);
			this.velocityPPR.add(this.randomUniformGenerator(MIN_VELOCITY, MAX_VELOCITY));
		}

		this.pBestXPPR = this.positionPPR;
		this.pBestPPR = 0.0;
	}
	
	private double randomUniformGenerator(final double minimum, final double maximum){
		return minimum + this.rand.nextDouble()*(maximum-minimum);		
	}	
	
//	private double randomUniformGenerator(final double minimum, final double maximum){
//		final double min = minimum;
//		final double max = maximum*1.0;
//		return min + this.rand.nextDouble()*max;		
//	}	
	/**
	 * M�todo para retornar a dimens�o da part�cula.
	 * @return O atributo dimensionPPR
	 * @author Andr� 
	 */	
	public int getDimension() {
		return dimensionPPR;
	}
	/**
	 * M�todo para configurar a dimens�o da part�cula.
	 * @author Andr� 
	 */	
	public void setDimension(final int dimensionPPR) {
		this.dimensionPPR = dimensionPPR;
	}
	/**
	 * M�todo para atualizar a velocidade da part�cula.
	 * @param time
	 * @author Andr� 
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
	 * M�todo para atualizar a velocidade da part�cula.
	 * @param lbestX
	 * @author Andr� 
	 */	
	public void updateVelocity(final List<Double> lbestX){
		for(int i=0; i<this.velocityPPR.size(); i++){

			final double temp = CONSTRIC_FACTOR*(this.velocityPPR.get(i) // NOPMD by Andr� on 27/06/17 10:04
					+ ACC_CONST*this.rand.nextDouble()*(this.pBestXPPR.get(i) - this.positionPPR.get(i)) // NOPMD by Andr� on 27/06/17 10:04
					+ ACC_CONST*this.rand.nextDouble()*(lbestX.get(i) - this.positionPPR.get(i))); // NOPMD by Andr� on 27/06/17 10:04

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
	 * M�todo para atualizar a velocidade da part�cula.
	 * @param lbestX
	 * @param time
	 * @param iterations
	 * @param worstFitness
	 * @author Andr� 
	 */	
	public void updateVelocity(final List<Double> lbestX, final int time, final int iterations, final double worstFitness){

		for(int i=0; i<this.velocityPPR.size(); i++){
			double inertialFactor;
			final double grade = (fitnessPPR - worstFitness)/(getgBest() - worstFitness);
			final double c2k = CLOW + (CUP - CLOW)*grade;

			inertialFactor = 0.4 + 0.5*(iterations - time)/iterations;

			final double temp = this.velocityPPR.get(i)*inertialFactor
					+c2k*this.rand.nextDouble()*(this.pBestXPPR.get(i) - this.positionPPR.get(i)) // NOPMD by Andr� on 27/06/17 10:12
					+ACC_CONST*this.rand.nextDouble()*(lbestX.get(i) - this.positionPPR.get(i)); // NOPMD by Andr� on 27/06/17 10:12

			this.velocityPPR.set(i, temp); 

			if (Math.abs(this.pBestXPPR.get(i)-this.positionPPR.get(i)) < this.sidestepThresPPR // NOPMD by Andr� on 27/06/17 10:12
					&& Math.abs(getgBestPosition().get(i)-this.positionPPR.get(i)) < this.sidestepThresPPR // NOPMD by Andr� on 27/06/17 10:12
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
//	/**
//	 * M�todo para atualizar a posi��o da part�cula.
//	 * @author Andr� 
//	 */		
//	public void updatePosition(){
//
//		for(int i=0; i< this.positionPPR.size(); i++){
//
//			final double position = this.positionPPR.get(i) + this.velocityPPR.get(i);
//
//			this.positionPPR.set(i,position);
//
//			if (this.positionPPR.get(i) > MAX_POSITION){
//				this.positionPPR.set(i, MAX_POSITION);
//				this.velocityPPR.set(i, (-1)*this.velocityPPR.get(i));
//			}else if (positionPPR.get(i) < MIN_POSITION){
//				this.positionPPR.set(i, MIN_POSITION);
//				this.velocityPPR.set(i, (-1)*this.velocityPPR.get(i));
//			}
//		}
//	}
	
	/**
	 * M�todo para atualizar a posi��o da part�cula.
	 * @author Isabella 
	 */		
	public void updatePosition(){
		
		double stepVelocity = 0.05;
		int a=0,b=1,c=2,d=3,e=4,f=5,g=6,h=7;
		double[] xTemp = new double[this.positionPPR.size()];
		double y = 0;//resultado temporario da posi��o antes do ajuste de limites
		for (int i = 0; i < xTemp.length/8; i++) {
			//************************update A*********************************
			y = this.positionPPR.get(a+(i*8)) + this.velocityPPR.get(a+(i*8))*stepVelocity;
			if(y<0)
				xTemp[a+(i*8)] = 0;
			else if(y>1)
				xTemp[a+(i*8)] = 1;
			//*****************************************************************
			//************************update B*********************************
			y = this.positionPPR.get(b+(i*8)) + this.velocityPPR.get(b+(i*8))*stepVelocity;
			if(y<xTemp[a+(i*8)])
				xTemp[b+(i*8)] = xTemp[a+(i*8)];
			else if(y>1)
				xTemp[b+(i*8)] = 1;
			//*****************************************************************
			//************************update C*********************************
			y = this.positionPPR.get(c+(i*8)) + this.velocityPPR.get(c+(i*8))*stepVelocity;
			if(y<0)
				xTemp[c+(i*8)] = 0;
			else if(y>xTemp[b+(i*8)])
				xTemp[c+(i*8)] = xTemp[b+(i*8)];
			//*****************************************************************
			//************************update D*********************************
			y = this.positionPPR.get(d+(i*8)) + this.velocityPPR.get(d+(i*8))*stepVelocity;
			double limit = (xTemp[c+(i*8)]>xTemp[a+(i*8)])? xTemp[c+(i*8)]:xTemp[a+(i*8)];
			if(y<limit)
				xTemp[d+(i*8)] = limit;
			else if(y>1)
				xTemp[d+(i*8)] = 1;
			//*****************************************************************
			//************************update E*********************************
			y = this.positionPPR.get(e+(i*8)) + this.velocityPPR.get(e+(i*8))*stepVelocity;
			if(y<xTemp[d+(i*8)])
				xTemp[e+(i*8)] = xTemp[d+(i*8)];
			else if(y>1)
				xTemp[e+(i*8)] = 1;
			//*****************************************************************
			//************************update F*********************************
			y = this.positionPPR.get(f+(i*8)) + this.velocityPPR.get(f+(i*8))*stepVelocity;
			if(y<xTemp[e+(i*8)])
				xTemp[f+(i*8)] = xTemp[e+(i*8)];
			else if(y>1)
				xTemp[f+(i*8)] = 1;
			//*****************************************************************
			//************************update G*********************************
			y = this.positionPPR.get(g+(i*8)) + this.velocityPPR.get(g+(i*8))*stepVelocity;
			if(y<xTemp[b+(i*8)])
				xTemp[g+(i*8)] = xTemp[b+(i*8)];
			else if(y>xTemp[f+(i*8)])
				xTemp[g+(i*8)] = xTemp[f+(i*8)];
			//*****************************************************************
			//************************update H*********************************
			y = this.positionPPR.get(h+(i*8)) + this.velocityPPR.get(h+(i*8))*stepVelocity;
			limit = (xTemp[g+(i*8)]>xTemp[f+(i*8)])? xTemp[g+(i*8)]:xTemp[f+(i*8)];
			if(y<limit)
				xTemp[h+(i*8)] = limit;
			else if(y>1)
				xTemp[h+(i*8)] = 1;
			//*****************************************************************
			
		}

//		for(int i=0; i< this.positionPPR.size()/8; i++){
//
//			final double position = this.positionPPR.get(i*8) + this.velocityPPR.get(i*8);
//
//			this.positionPPR.set(i*8,position);
//
//			if (this.positionPPR.get(i) > MAX_POSITION){
//				this.positionPPR.set(i, MAX_POSITION);
//				this.velocityPPR.set(i, (-1)*this.velocityPPR.get(i));
//			}else if (positionPPR.get(i) < MIN_POSITION){
//				this.positionPPR.set(i, MIN_POSITION);
//				this.velocityPPR.set(i, (-1)*this.velocityPPR.get(i));
//			}
//		}
	}
	/**
	 * M�todo para configurar a posi��o da part�cula.
	 * @param newPosition
	 * @author Andr� 
	 */		
	public void setPosition (final List<Double> newPosition){

		for(int i=0; i< this.positionPPR.size(); i++){
			this.positionPPR.set(i, newPosition.get(i));
		}

	}
	/**
	 * M�todo para avaliar o fitness da part�cula.
	 * @param network
	 * @param listOfNodes
	 * @param numberOfCalls
	 * @return O atributo fitness.
	 * @author Andr� 
	 */	
	public double fitnessEvaluation(final OpticalLink[][] network, final List<OpticalSwitch> listOfNodes, ArrayList<List<RoutingAlgorithmSolution>> allRoutes, final int numberOfCalls, final RoutingAlgorithmType rType, final double meanRateBetCalls, FuzzyMetricMethodType fuzzyLogicType, int KYEN) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException, InvalidListOfCoefficientsException{

		final IRMLSASimulation simRMLSAInstance = RMLSASimulation.getRMLSASimulationInstance();
		final double temp = simRMLSAInstance.simulation(network, listOfNodes, numberOfCalls, rType, meanRateBetCalls, allRoutes, positionPPR, fuzzyLogicType, KYEN); 

		if (temp != 0.0){ // NOPMD by Andr� on 27/06/17 10:51
			//Update particle info if needed
			if (1/temp > pBestPPR ){
				this.pBestPPR = 1/temp;
				this.pBestXPPR = positionPPR;
			}
			//Update global info if empty
			if (getgBestPosition().isEmpty()){ // NOPMD by Andr� on 27/06/17 10:51
				setgBestPosition(positionPPR);
				setgBest(this.pBestPPR); // NOPMD by Andr� on 27/06/17 10:51
			}
			//Update global best info if needed
			if (1/temp > getgBest()){
				setgBest(1/temp);
				setgBestPosition(positionPPR);
			}
			fitnessPPR = 1/temp;
			return 1/temp; // NOPMD by Andr� on 27/06/17 10:51
		}

		return 1/temp;

	}
	/**
	 * M�todo para retornar a var�avel pBestXPPR.
	 * @return O atributo pBestXPPR
	 * @author Andr� 
	 */		
	public List<Double> getpBestX() {
		return pBestXPPR;
	}
	/**
	 * M�todo para configurar a var�avel pBestXPPR.
	 * @param pBestX
	 * @author Andr� 
	 */
	public void setpBestX(final List<Double> pBestX) {
		for(int x=0;x<pBestX.size();x++){
			this.pBestXPPR.add(pBestX.get(x));
		}
	}
	/**
	 * M�todo para retornar a var�avel pBestPPR.
	 * @return O atributo pBestPPR
	 * @author Andr� 
	 */
	public double getpBest() {
		return pBestPPR;
	}
	/**
	 * M�todo para configurar a var�avel pBest.
	 * @param pBest
	 * @author Andr� 
	 */
	public void setpBest(final double pBest) {
		this.pBestPPR = pBest;
	}
	/**
	 * M�todo para retornar o vetor de velocidade da part�cula.
	 * @return O atributo velocityPPR
	 * @author Andr� 
	 */
	public List<Double> getVelocity() {
		return velocityPPR;
	}
	/**
	 * M�todo para configurar o vetor de velocidade da part�cula.
	 * @param velocity
	 * @author Andr� 
	 */
	public void setVelocity(final List<Double> velocity) {
		for(int x=0;x<velocity.size();x++){
			this.velocityPPR.add(velocity.get(x));
		}
	}
	/**
	 * M�todo para retornar o vetor de posi��es da part�cula.
	 * @return positionPPR
	 * @author Andr� 
	 */
	public List<Double> getPosition() {
		return positionPPR;
	}
	/**
	 * M�todo para calcular a dist�ncia at� a melhor part�cula.
	 * @return O valor da dist�ncia
	 * @author Andr� 
	 */
	public double distanceToBest(){
		//Evaluate distance form this particle for the best
		double temp = 0;

		for(int i=0;i<getgBestPosition().size();i++){ // NOPMD by Andr� on 27/06/17 10:36
			temp += Math.pow(getgBestPosition().get(i)-this.positionPPR.get(i),2); // NOPMD by Andr� on 27/06/17 10:36
		}
		return temp;
	}
	/**
	 * Método para retornar a posição da melhor partícula.
	 */
	public static List<Double> getgBestPosition() {
		return gBestPositionPPS;
	}
	/**
	 * M�todo para configurar a posi��o da melhor part�cula.
	 * @param gBestPosition
	 * @author Andr� 
	 */
	public static void setgBestPosition(final List<Double> gBestPosition) {
		Particle.gBestPositionPPS = gBestPosition;
	}
	/**
	 * M�todo para retornar o melhor fitness.
	 * @return O atributo gBestPPS
	 * @author Andr� 
	 */
	public static double getgBest() {
		return gBestPPS;
	}
	/**
	 * M�todo para configurar o melhor fitness.
	 * @param gBest
	 * @author Andr� 
	 */
	public static void setgBest(final double gBest) {
		Particle.gBestPPS = gBest;
	}
	/**
	 * M�todo para retornar o fitness da part�cula.
	 * @return O atributo fitnessPPR
	 * @author Andr� 
	 */
	public double getFitness() {
		return fitnessPPR;
	}
	/**
	 * M�todo para configurar o fitness da part�cula.
	 * @param fitness
	 * @author Andr� 
	 */
	public void setFitness(final double fitness) {
		this.fitnessPPR = fitness;
	}

}
