package psoMTHS;

import java.util.Random;

public class ParticleSimple {

    private double[][] position;
    private double[][] pBestPosition;
    private double[][] velocity;
    private double minPostion;
    private double maxPostion;
    private final double MAX_VELOCITY = 1.0;
	private final double MIN_VELOCITY = -1.0;
    private int dimentionRow;
    private int dimentionCol;
    private double pBestValue;

    Random rand = new Random();

    public ParticleSimple(double minPostion, double maxPostion, int dimentionRow, int dimentionCol){

        this.minPostion = minPostion;
        this.maxPostion = maxPostion;
        this.dimentionRow = dimentionRow;
        this.dimentionCol = dimentionCol;

        this.pBestValue = 0.0; // Buscamos o maior fitness

        this.position = new double[dimentionRow][dimentionCol];
        this.velocity = new double[dimentionRow][dimentionCol];
        for (int c = 0; c < dimentionCol; c++){
            for (int r = 0; r < dimentionRow; r++){
                this.position[r][c] = randomUniformGenerator(this.minPostion, this.maxPostion);
                this.velocity[r][c] = randomUniformGenerator(MIN_VELOCITY, MAX_VELOCITY);
            }
        }
        this.pBestPosition = this.position.clone();
    }

    public double getMaxVelocity() {
        return this.MAX_VELOCITY;
    }

    public double getMinVelocity() {
        return this.MIN_VELOCITY;
    }

    public double getDimensionRow(){
        return this.dimentionRow;
    }

    public double getDimensionCol(){
        return this.dimentionCol;
    }

    public double getpBestValue(){
        return this.pBestValue;
    }

    public void setpBestValue(double pBestValue){
        this.pBestValue = pBestValue;
    }

    public double[][] getPosition(){
        return this.position;
    }
    
    public double[][] getBestPosition(){
        return this.position;
    }

    public double[][] getVelocity(){
        return this.velocity;
    }

    public void setpBestPosition(double[][] position){
        this.pBestPosition = position.clone();
    }

    public void move(){
        this.position = new double[dimentionRow][dimentionCol];
        this.velocity = new double[dimentionRow][dimentionCol];
        for (int c = 0; c < dimentionCol; c++){
            for (int r = 0; r < dimentionRow; r++){
                this.position[r][c] = this.position[r][c] + this.velocity[r][c];
            }
        }
    }

    private double randomUniformGenerator(final double minimum, final double maximum){
		return minimum + this.rand.nextDouble()*(maximum-minimum);		
	}	
}
