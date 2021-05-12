package MTHS;

import java.util.ArrayList;

import routing.RoutingAlgorithmSolution;

public class AlgorithmNewSolution {

    private RoutingAlgorithmSolution route;
	private double CTR;             // Resultado total da métrica

    public AlgorithmNewSolution(RoutingAlgorithmSolution route, double CTR) {
		this.route = route;
		this.CTR = CTR;
	}

    public void setCTR(double CTR){
        this.CTR = CTR;
    }

    public double getCTR(){
        return this.CTR;
    }

    public RoutingAlgorithmSolution getRoute(){
        return this.route;
    }
}
