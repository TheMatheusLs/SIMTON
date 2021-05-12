package simulation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import types.MetricMethodType;
import types.RoutingAlgorithmType;
import utility.Debug;
import arquitecture.OpticalLink;
import arquitecture.OpticalSwitch;
import exceptions.InvalidListOfCoefficientsException;
import exceptions.InvalidNodeIdException;
import exceptions.InvalidNumberOfFrequencySlotsException;
import exceptions.InvalidRoutesException;
import routing.RoutingAlgorithmSolution;

public interface IRMLSASimulation {	
	double simulation(OpticalLink[][] network, List<OpticalSwitch> listOfNodes, int numberOfCalls, RoutingAlgorithmType routingType, double meanRateBetCalls, ArrayList<List<RoutingAlgorithmSolution>> allRoutes, double[] listOfCoef, MetricMethodType metricLogicType, int KYEN , Debug debugClass) throws InvalidNumberOfFrequencySlotsException, InvalidRoutesException, InvalidNodeIdException, InvalidListOfCoefficientsException, IOException;
}
