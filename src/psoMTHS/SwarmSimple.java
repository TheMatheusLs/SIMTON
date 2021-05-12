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
import types.RoutingAlgorithmType;

public class SwarmSimple {

    private int numberOfParticles;
    private double gBestValue;
    private double[][] gBestPosition;
    private List<ParticleSimple> allParticles;
    private OpticalLink[][] network;
    private List<OpticalSwitch> listOfNodes;
    private ArrayList<List<RoutingAlgorithmSolution>> allRoutes;
    private int numberOfCalls;
    private RoutingAlgorithmType rType;
    private double meanRateBetCalls;
    private FuzzyMetricMethodType fuzzyLogicType;
    private int KYEN;
    private double[][] matrixRulesCoeffs;
    private double[] coefficientsMetric;

    Random rand = new Random();

    public SwarmSimple(int numberOfParticles, int dimentionRow, int dimentionCol, final OpticalLink[][] network, final List<OpticalSwitch> listOfNodes, ArrayList<List<RoutingAlgorithmSolution>> allRoutes, final int numberOfCalls, final RoutingAlgorithmType rType, final double meanRateBetCalls, FuzzyMetricMethodType fuzzyLogicType, int KYEN,  double[] coefficientsMetric, double[][] matrixRulesCoeffs) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException, InvalidListOfCoefficientsException{
        this.numberOfParticles = numberOfParticles;

        this.gBestValue = 0.0;

        this.allParticles = new ArrayList<ParticleSimple>(this.numberOfParticles);

        this.gBestPosition = new double[dimentionRow][dimentionCol];

        this.network = network;
        this.listOfNodes = listOfNodes;
        this.allRoutes = allRoutes;
        this.numberOfCalls = numberOfCalls;
        this.rType = rType;
        this.meanRateBetCalls = meanRateBetCalls;
        this.fuzzyLogicType = fuzzyLogicType;
        this.KYEN = KYEN;
        this.matrixRulesCoeffs = matrixRulesCoeffs;
        this.coefficientsMetric = coefficientsMetric;

        for (int p = 0; p < numberOfParticles;){
            final ParticleSimple particle = new ParticleSimple(0, 1, 27, 4);
            double tempFitness = this.fitness(particle);
            System.out.println("\nParticle " + p + " com fitness temp de " + tempFitness);

            if (tempFitness > 60){
                this.allParticles.add(particle);
                p++;
                System.out.println("Particle salva\n");
            }
        }
    }

    public double getgBestValue(){
        return this.gBestValue;
    }

    public void setgBestValue(double gBestValue){
        this.gBestValue = gBestValue;
    }
    
    public void setgBestPosition(double[][] position){
        this.gBestPosition = position.clone();
    }

    public double[][] getgBestPosition(){
        return this.gBestPosition;
    }

    public void resetNetwork(){
		for(int x=0;x<this.listOfNodes.size();x++){
			for(int y=0;y<this.listOfNodes.size();y++){
				if(network[x][y] != null){
					network[x][y].eraseOpticalLink();
				}								
			}
		}
	}

    public double fitness(ParticleSimple particle) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException, InvalidListOfCoefficientsException{
        final IRMLSASimulation simRMLSAInstance = RMLSASimulation.getRMLSASimulationInstance();

        final double pb_temp = simRMLSAInstance.simulation(this.network, this.listOfNodes, this.numberOfCalls, this.rType, this.meanRateBetCalls, this.allRoutes, this.coefficientsMetric, this.fuzzyLogicType, this.KYEN, particle.getPosition());

        this.resetNetwork();

        System.out.println("fit: "+ 1/pb_temp + " Pb:" + pb_temp);
        if (pb_temp > 0){
            return 1/pb_temp;
        }
        return pb_temp;
    }

    public void setpBest() throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException, InvalidListOfCoefficientsException{
        for(final ParticleSimple particle : this.allParticles){
            double fitnessCadidate = fitness(particle);
            if (particle.getpBestValue() < fitnessCadidate){
                particle.setpBestValue(fitnessCadidate);
                particle.setpBestPosition(particle.getPosition());
            }
        } 
    }

    public void setgBest() throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException, InvalidListOfCoefficientsException{
        for(final ParticleSimple particle : this.allParticles){
            //double bestFitnessCadidate = fitness(particle);
            double bestFitnessCadidate = particle.getpBestValue();
            if (this.getgBestValue() < bestFitnessCadidate){
                this.setgBestValue(bestFitnessCadidate);
                this.setgBestPosition(particle.getPosition());
            }
        }
    }

    public void moveParticles(){
        final double W = 0.5;
        final double C1 = 2.05;
        final double C2 = 2.05;
        for(final ParticleSimple particle : this.allParticles){

            for(int r=0; r < particle.getDimensionRow(); r++){
                for(int c=0; c < particle.getDimensionCol(); c++){

                    final double temp = (particle.getVelocity()[r][c] * W) + ((C1 * this.rand.nextDouble()) * (particle.getBestPosition()[r][c] - particle.getPosition()[r][c])) + ((C2 * this.rand.nextDouble()) * (this.getgBestPosition()[r][c] - particle.getPosition()[r][c]));

                    if (temp > particle.getMaxVelocity()){
                        particle.getVelocity()[r][c] = particle.getMaxVelocity();
                    } else if (temp < particle.getMinVelocity()){
                        particle.getVelocity()[r][c] = particle.getMinVelocity();
                    } else {
                        particle.getVelocity()[r][c] = temp;
                    }
                }	  
            }
        }
    }
}
