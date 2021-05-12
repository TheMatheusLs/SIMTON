package psoMTHS;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.List;

import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import exceptions.InvalidFiberLengthException;
import exceptions.InvalidListOfCoefficientsException;
import exceptions.InvalidNodeIdException;
import exceptions.InvalidNumberOfFrequencySlotsException;
import exceptions.InvalidRoutesException;
import parameters.SimulationParameters;
import psoMTHS.Particle;
import routing.RoutingAlgorithmSolution;
import types.FuzzyMetricMethodType;
import types.ParticlePSOType;
import types.RoutingAlgorithmType;

public class Swarm {

    private transient final OpticalLink[][] network;
    private transient final List<OpticalSwitch> listOfNodes;
    private transient final int numberOfCalls;
    private transient final RoutingAlgorithmType routAlgType;
    private transient final double meanRateBetCalls;
    private transient final List<Particle> agents;
    private transient int numberOfAgents;
    private transient final int neighborhood;
    private transient double worstFitness; // Fitness from the worst particle
    private int dimension;
    public transient int iterations;
    private transient ArrayList<List<RoutingAlgorithmSolution>> allRoutes;
    private transient FuzzyMetricMethodType fuzzyLogicType;
    private transient int KYEN;
    private transient ParticlePSOType particleType;
    private transient double[] coeffSeed;
    private transient double[][] matrixRulesCoeffs;

    public Swarm(final int neighborhood, final int numberOfAgents, final boolean sidestep,
            final OpticalLink[][] network, final List<OpticalSwitch> listOfNodes,
            ArrayList<List<RoutingAlgorithmSolution>> allRoutes, final int numberOfCalls,
            final RoutingAlgorithmType routAlgType, final double meanRateBetCalls, final int numberOfIter,
            FuzzyMetricMethodType fuzzyLogicType, int KYEN, final ParticlePSOType particleType, double[] coeffSeed, double[][] matrixRulesCoeffs) 
            throws InvalidNumberOfFrequencySlotsException, InvalidClassException, InvalidFiberLengthException,
            InvalidListOfCoefficientsException, InvalidRoutesException, InvalidNodeIdException {
		
		final SimulationParameters parameters = new SimulationParameters();

        this.particleType = particleType;

        this.agents = new ArrayList<Particle>(numberOfAgents);

        if (this.particleType.equals(ParticlePSOType.FULL_METRIC_SEED)){
            this.numberOfAgents = numberOfAgents - 1;
        }else{
            this.numberOfAgents = numberOfAgents;
        }

		this.neighborhood = neighborhood;
		this.worstFitness = Double.MAX_VALUE;
		this.network = network;
		this.listOfNodes = listOfNodes;
		this.allRoutes = allRoutes;
		this.numberOfCalls = numberOfCalls;
		this.routAlgType = routAlgType;
		this.meanRateBetCalls = meanRateBetCalls;
		this.iterations = numberOfIter;
		this.fuzzyLogicType = fuzzyLogicType;
		this.KYEN = KYEN;
        this.coeffSeed = coeffSeed;
		this.matrixRulesCoeffs = matrixRulesCoeffs;

        System.out.println("Inicia as partículas");

        for (int i=0; i<this.numberOfAgents;i++){
			   
            final Particle particle = new Particle(this.particleType, sidestep);
            
            particle.fitnessEvaluation(network, listOfNodes, allRoutes, numberOfCalls, routAlgType, meanRateBetCalls, fuzzyLogicType, KYEN, this.matrixRulesCoeffs);
            
            resetNetwork();
            
            System.out.println("Id do individuo:  "+i+" Fitness: "+1/(particle.getFitness())); // NOPMD by Andr� on 27/06/17 11:45
            
            if (particle.getFitness() < worstFitness){
                worstFitness = particle.getFitness();
            }
            
            this.agents.add(particle);
           
        }

        if (this.particleType.equals(ParticlePSOType.FULL_METRIC_SEED)){
            final Particle particle = new Particle(this.particleType, sidestep, this.coeffSeed);

            particle.fitnessEvaluation(network, listOfNodes, allRoutes, numberOfCalls, routAlgType, meanRateBetCalls, fuzzyLogicType, KYEN, this.matrixRulesCoeffs);
            
            resetNetwork();
            
            System.out.println("Id do individuo:  "+this.numberOfAgents+" Fitness: "+1/(particle.getFitness())); // NOPMD by Andr� on 27/06/17 11:45
            
            if (particle.getFitness() < worstFitness){
                worstFitness = particle.getFitness();
            }
            
            this.agents.add(particle);
        }

    }//End Swarm constructor
    /**
	 * Reseta a topologia de rede.
	 */	
	public void resetNetwork(){
		for(int x=0;x<this.listOfNodes.size();x++){
			for(int y=0;y<this.listOfNodes.size();y++){
				if(network[x][y] != null){
					network[x][y].eraseOpticalLink();
				}								
			}
		}
	}
    /**
	 * Método para inicializar o exame.
	 */
	public void develop (final int iteration) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException, InvalidListOfCoefficientsException{
		
		double wFitness = Double.MAX_VALUE;
		
		System.out.println("Iteração: "+ iteration+" ------------------" );
		
		for (int i=0; i<this.agents.size(); i++){
			
            // Usado para GLOBAL_BEST
			if(this.neighborhood == 0){
				
				this.agents.get(i).updateVelocity(lbestMaximum(agents.get((i+agents.size()-1)%agents.size()),agents.get((i+1)%agents.size())), iteration, iterations,worstFitness);				
				
			}else if (neighborhood == 1){ // Usado para LOCAL_BEST
				this.agents.get(i).updateVelocity(iterations);
			}
	   
			this.agents.get(i).updatePosition();
			
			this.agents.get(i).fitnessEvaluation(network, listOfNodes, allRoutes, numberOfCalls, routAlgType, meanRateBetCalls, fuzzyLogicType, KYEN, this.matrixRulesCoeffs);

			this.resetNetwork();
			
			//////////
			System.out.println("Id do individuo: "+i+" Fitnesse: "+1/this.agents.get(i).getpBest()); 

//			for(int j=0;j<this.agents.get(i).getpBestX().size();j++){
//				System.out.print(this.agents.get(i).getpBestX().get(j)+" "); // NOPMD by Andr� on 27/06/17 11:54
//			}
			System.out.println(); // NOPMD by Andr� on 27/06/17 11:54
			//////////
			
			if(this.agents.get(i).getFitness() < wFitness){
				wFitness = this.agents.get(i).getFitness();
			}
		}
		
		this.worstFitness = wFitness;     //Update the iteration's worst fitness

	}

    public List<Double> lbestMaximum(final Particle neighbor1, final Particle neighbor2){
		
		if (neighbor1.getgBestFitness() > neighbor2.getgBestFitness()){
			return neighbor1.getpBestX();
		}else{
			return neighbor2.getpBestX();
		}
	}
    /**
	 * Método para retornar o melhor fitness do exame.
	 */
	@SuppressWarnings("static-access")
	public double getHigherFitness(){
	   return this.agents.get(0).getgBestFitness();
	}
    /**
	 * Método para retornar a melhro posi��o do exame.
	 */	
	@SuppressWarnings("static-access")
	public List<Double> getHigherSolution(){
	   return this.agents.get(0).getgBestPosition();
	}
}// END class Swarm
