package MTHS.metrics;

import java.io.FileWriter;
import java.io.PrintWriter;

import arquitecture.OpticalLink;
import call_request.CallRequest;
import parameters.SimulationParameters;
import routing.RoutingAlgorithmSolution;
import utility.Function;

public class Danilo {
    
    // Adicione quantas métricas precisar
    private double MetricX;
    private double MetricY;
    private double MetricZ;

    private double CTR;

	final SimulationParameters parameters = new SimulationParameters();

    private static final Danilo metricInstance = new Danilo();

    public static Danilo getDaniloInstance(){
		return metricInstance;
	}

    public double getX() {
        return MetricX;
    }

    public double getY() {
        return MetricY;
    }

    public double getZ() {
        return MetricZ;
    }

    public double calculate(RoutingAlgorithmSolution route, double[] coeffB){

        /// *** Aqui deve ser retornado o valor para cada rota
        // DANILO
        CTR = 1; 
        return CTR;
    }
}   
