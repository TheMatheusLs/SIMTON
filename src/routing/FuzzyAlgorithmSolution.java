package routing;

public class FuzzyAlgorithmSolution {
	
	private RoutingAlgorithmSolution rota;
	private double DNR;
	private double OSCR;
	private double OSMR;
	private double CTR;
	
	public FuzzyAlgorithmSolution() {
		this.rota = new RoutingAlgorithmSolution();
		this.DNR = 0.0;
		this.OSCR = 0.0;
		this.OSMR = 0.0;
		this.CTR = 0.0;
	}
	
	public RoutingAlgorithmSolution getRota() {
		return rota;
	}
	public void setRota(RoutingAlgorithmSolution rota) {
		this.rota = rota;
	}
	public double getDNR() {
		return DNR;
	}
	public void setDNR(double dNR) {
		DNR = dNR;
	}
	public double getOSCR() {
		return OSCR;
	}
	public void setOSCR(double oSCR) {
		OSCR = oSCR;
	}
	public double getOSMR() {
		return OSMR;
	}
	public void setOSMR(double oSMR) {
		OSMR = oSMR;
	}

	public double getCTR() {
		return CTR;
	}

	public void setCTR(double cTR) {
		CTR = cTR;
	}	

}
