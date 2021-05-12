package routing;

public class FuzzyAlgorithmSolutionEON {
	
	private RoutingAlgorithmSolution route;
	private double HOPS;
	private double FORMS;
	private double CTR;
	
	public FuzzyAlgorithmSolutionEON() {
		this.route = new RoutingAlgorithmSolution();
		this.HOPS = 0.0;
		this.FORMS = 0.0;
		this.CTR = 0.0;
	}
	
	public RoutingAlgorithmSolution getRota() {
		return route;
	}
	public void setRota(RoutingAlgorithmSolution rota) {
		this.route = rota;
	}
	public double getHops() {
		return this.HOPS;
	}
	public void setHops(double hops) {
		this.HOPS = hops;
	}
	public double getForms() {
		return this.FORMS;
	}
	public void setForms(double forms) {
		this.FORMS = forms;
	}
	public double getCTR() {
		return this.CTR;
	}
	public void setCTR(double cTR) {
		this.CTR = cTR;
	}	

}
