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
import types.MetricMethodType;
import types.PSOType;
import types.RoutingAlgorithmType;

public class GenericSwarm {

    private int numberOfParticles;
    private double gBestFitness;
    private double[] gBestPosition;
    private List<GenericParticle> allParticles;
    private double minPosition;
    private double maxPosition;
    private boolean sidestepPPR;
    private double sidestepThresPPR;

    private final double SIDESTEP_SCALE = 10.0;
    private final double SIDESTEP_THRES = 10.0;

    private final double W = 0.729844;
    //private final double C1 = 2.05;
    //private final double C2 = 2.05;
    private final double C1 = 1.496180;
    private final double C2 = 1.496180;

    Random rand = new Random();

    public GenericSwarm(int numberOfParticles, double gBestFitness, int particleDimension, PSOType PSO_TYPE, double minPosition, double maxPosition){

        this.numberOfParticles = numberOfParticles;

        this.gBestFitness = gBestFitness; 

        this.allParticles = new ArrayList<GenericParticle>(this.numberOfParticles);

        this.gBestPosition = new double[particleDimension];

        this.minPosition = minPosition;
        this.maxPosition = maxPosition;
        this.sidestepPPR = true;
        this.sidestepThresPPR = SIDESTEP_THRES * (this.maxPosition - this.minPosition)/100;


        for (int p = 0; p < this.numberOfParticles; p++){

            final GenericParticle particle = new GenericParticle(this.minPosition, this.maxPosition, particleDimension, this.gBestFitness, PSO_TYPE);

            this.allParticles.add(particle);
        }
    }

    public double getgBestFitness(){
        return this.gBestFitness;
    }

    public void setgBestFitness(double gBestValue){
        this.gBestFitness = gBestValue;
    }
    
    public void setgBestPosition(double[] position){
        this.gBestPosition = position.clone();
    }

    public double[] getgBestPosition(){
        return this.gBestPosition;
    }

    public List<GenericParticle> getAllParticles(){
        return this.allParticles;
    }

    public void updateVelocityGlobal(){     
        for (GenericParticle particle : this.allParticles){
            double R1 =  this.rand.nextDouble();
            double R2 =  this.rand.nextDouble();
            for(int i = 0; i < particle.getVelocity().length; i++){

                double temp1 = particle.getVelocity()[i] * W;
                double temp2 = C1 * R1 * (particle.getBestPosition()[i] - particle.getPosition()[i]);
                double temp3 = C2 * R2 * (this.getgBestPosition()[i] - particle.getPosition()[i]);

                final double temp = temp1 + temp2 + temp3; 

                //final double temp = particle.getVelocity()[i] * W + C1 * this.rand.nextDouble() * (particle.getBestPosition()[i] - particle.getPosition()[i]) + C2 * this.rand.nextDouble() * (this.getgBestPosition()[i] - particle.getPosition()[i]); 
                
                double velocid = temp;

                // if (Math.abs(particle.getBestPosition()[i] - particle.getPosition()[i]) < sidestepThresPPR 
                // && Math.abs(getgBestPosition()[i] - particle.getPosition()[i]) < sidestepThresPPR 
                // && particle.getVelocity()[i] < sidestepThresPPR
                // && this.sidestepPPR){
                    
                //     if (temp < 0){
                //         velocid = temp - this.sidestepThresPPR * SIDESTEP_SCALE;
                //     }else{
                //         velocid = temp + this.sidestepThresPPR * SIDESTEP_SCALE;
                //         this.sidestepThresPPR = this.sidestepThresPPR / 2;
                //     }	 
                // }
                
                particle.setVelocity(i, velocid); 
            }		
        }
	}

    public void updateVelocityLocal(){     
        for (int indexParticle = 0; indexParticle < (this.allParticles.size()); indexParticle++){
            
            GenericParticle lBestLeft = this.allParticles.get( (indexParticle + this.allParticles.size() - 1) % this.allParticles.size() );
            GenericParticle lBestRight = this.allParticles.get( (indexParticle + 1) % this.allParticles.size() );

            double[] lBest;
            if (lBestLeft.getpBestFitness() < lBestRight.getpBestFitness()){
                lBest = lBestLeft.getBestPosition();
            }else{
                lBest = lBestRight.getBestPosition();
            }
            GenericParticle particleCurrent = this.allParticles.get(indexParticle);

            double R1 =  this.rand.nextDouble();
            double R2 =  this.rand.nextDouble();

            for(int iVel=0; iVel < particleCurrent.getVelocity().length; iVel++){

                double temp1 = particleCurrent.getVelocity()[iVel] * W;
                double temp2 = C1 * R1 * (particleCurrent.getBestPosition()[iVel] - particleCurrent.getPosition()[iVel]);
                double temp3 = C2 * R2 * (lBest[iVel] - particleCurrent.getPosition()[iVel]);

                final double temp = temp1 + temp2 + temp3; 

                //final double temp = particleCurrent.getVelocity()[iVel] * W
                //        + C1 *this.rand.nextDouble()*(particleCurrent.getBestPosition()[iVel] - particleCurrent.getPosition()[iVel]) 
                //        + C2 *this.rand.nextDouble()*(lBest[iVel] - particleCurrent.getPosition()[iVel]);

                double velocid = temp;

                // if (Math.abs(particleCurrent.getBestPosition()[iVel] - particleCurrent.getPosition()[iVel]) < sidestepThresPPR 
                // && Math.abs(getgBestPosition()[iVel] - particleCurrent.getPosition()[iVel]) < sidestepThresPPR 
                // && Math.abs(particleCurrent.getVelocity()[iVel]) < sidestepThresPPR
                // && this.sidestepPPR){
                    
                //     if (temp < 0){
                //         velocid = temp - this.sidestepThresPPR * SIDESTEP_SCALE;
                //     }else{
                //         velocid = temp + this.sidestepThresPPR * SIDESTEP_SCALE;
                //         this.sidestepThresPPR = this.sidestepThresPPR / 2;
                //     }	 
                // }
                
                particleCurrent.setVelocity(iVel, velocid);
            }
        }		
	}

    public void updatePosition(){
        for (GenericParticle particle : this.allParticles){
            
            double[] tempPosition = new double[particle.getPosition().length];

            for (int i = 0; i < tempPosition.length; i++){
                double temp = particle.getPosition()[i] + particle.getVelocity()[i];

                if (temp > this.maxPosition){
                    tempPosition[i] = this.maxPosition;
                } else if (temp < this.minPosition){
                    tempPosition[i] = this.minPosition;
                } else {
                    tempPosition[i] = temp;
                }
            }

            particle.setPosition(tempPosition);
        }
    }
}
