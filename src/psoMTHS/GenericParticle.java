package psoMTHS;

import java.util.Random;
import types.PSOType;

public class GenericParticle {

    private double[] position;
    private double[] pBestPosition;
    private double[] velocity;
    private final double minPosition;
    private final double maxPosition;
    private final double MAX_VELOCITY = 0.5;
	private final double MIN_VELOCITY = -0.5;
    private final int dimention;
    private double pBestFitness;
    private double fitness;
    private int neighborhood;

    Random rand = new Random();

    public GenericParticle(double minPosition, double maxPosition, int dimentionParticle, double initFitness, PSOType neighborhood){

        this.minPosition = minPosition;
        this.maxPosition = maxPosition;
        this.dimention = dimentionParticle;
        this.neighborhood = neighborhood.getCode();

        this.fitness = initFitness;
        this.pBestFitness = initFitness; 

        this.position = new double[this.dimention];
        this.velocity = new double[this.dimention];

        for (int d = 0; d < this.dimention; d++){
            this.position[d] = randomUniformGenerator(this.minPosition, this.maxPosition);
            //this.velocity[d] = randomUniformGenerator(0.0, 0.0); //randomUniformGenerator(MIN_VELOCITY, MAX_VELOCITY);
            this.velocity[d] = randomUniformGenerator(MIN_VELOCITY, MAX_VELOCITY);
        }

        this.pBestPosition = this.position.clone();
    }

    public double getMaxVelocity() {
        return this.MAX_VELOCITY;
    }

    public double getMinVelocity() {
        return this.MIN_VELOCITY;
    }

    public double getDimension(){
        return this.dimention;
    }

    public double getpBestFitness(){
        return this.pBestFitness;
    }
    
    public double getFitness(){
        return this.fitness;
    }

    public void setFitness(double fitness){

        this.fitness = fitness;

        // if (this.pBestFitness > fitness){
        //     setpBestFitness(fitness);
        // }
    }

    public void setpBestFitness(double pBestFitness){
        this.pBestFitness = pBestFitness;
    }

    public void setPosition(double[] position){
        this.position = position;
    }

    public double[] getPosition(){
        return this.position;
    }
    
    public double[] getBestPosition(){
        return this.pBestPosition;
    }

    public double[] getVelocity(){
        return this.velocity;
    }

    public void setpBestPosition(double[] position){
        this.pBestPosition = position.clone();
    }

    public void setVelocity(int pos, double value){
        // if (value > MAX_VELOCITY){
        //     this.velocity[pos] = MAX_VELOCITY;
        // } else if (value < MIN_VELOCITY ){
        //     this.velocity[pos] = MIN_VELOCITY;
        // } else {
        //     this.velocity[pos] = value;
        // }

        this.velocity[pos] = value;
    }

    private double randomUniformGenerator(final double minimum, final double maximum){
		return minimum + this.rand.nextDouble()*(maximum-minimum);		
	}	
}
