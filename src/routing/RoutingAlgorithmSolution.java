package routing;

import java.util.ArrayList;
import java.util.List;

import arquitecture.OpticalLink;
import parameters.SimulationParameters;
import types.ModulationLevelType;
/**
 * Descreve a solu��o de roteamento usada nos algoritmos do simulador.
 * @author Andr� 
 */
public class RoutingAlgorithmSolution {
	/**
	 * Rota de uplink.
	 * @author Andr� 			
	 */		
	private List<OpticalLink> upLink;
	/**
	 * Rota de downlink.
	 * @author Andr� 			
	 */	
	private List<OpticalLink> downLink;
	/**
	 * Construtor da classe.
	 * @author Andr�
	 */		
	private int YENID;

	private List<RoutingAlgorithmSolution> conflictRoutes;

	private int routeID;

	private short[] slotOcupation;
	private List<ModulationLevelType> modulationsTypeByBitrate;
	private List<Integer> sizeSlotsbyBitrate;
	private List<Integer> slotsByMSCL = null;

	public RoutingAlgorithmSolution() {		
		this.upLink = new ArrayList<OpticalLink>();
		this.downLink = new ArrayList<OpticalLink>();
	}
	/**
	 * Construtor da classe.
	 * @param upLink
	 * @param downLink
	 * @author Andr�
	 */	
	public RoutingAlgorithmSolution(final List<OpticalLink> upLink, final List<OpticalLink> downLink, int routeID) {		
		this.upLink = upLink;
		this.downLink = downLink;
		this.YENID = -1;
		this.routeID = routeID;

		SimulationParameters parameters = new SimulationParameters();
		// Criar um vetor de disponibilidade 
		this.slotOcupation = new short[parameters.getNumberOfSlots()];

		// Criar um assert para o valor mínimo ser 0 e o maior ser o número de as rotas conflitantes
	}
	/**
     * M�todo para retornar a rota de uplink.
     * @return O atributo upLink
     * @author Andr� 			
     */	
	public List<OpticalLink> getUpLink() {
		return this.upLink;
	}
	/**
     * M�todo para configurar a rota de uplink.
     * @param upLink
     * @author Andr� 			
     */	
	public void setUpLink(final List<OpticalLink> upLink) {
		this.upLink = upLink;
	}
	/**
     * M�todo para retornar a rota de downLink.
     * @return O atributo downLink
     * @author Andr� 			
     */
	public List<OpticalLink> getDownLink() {
		return this.downLink;
	}
	/**
     * M�todo para configurar a rota de uplink.
     * @param downLink
     * @author Andr� 			
     */
	public void setDownLink(final List<OpticalLink> downLink) {
		this.downLink = downLink;
	}
	/**
     * M�todo para resetar a solu��o.
     * @author Andr� 			
     */	
	public void clear(){		
		upLink.clear();
		downLink.clear();
	}
	
	public double getSizeInBound(){
		double sum = 0;
		for (OpticalLink fiber : upLink) {
			sum+=fiber.getLength();
		}
		return sum;
	}
	
	public double getSizeOutBound(){
		double sum = 0;
		for (OpticalLink fiber : downLink) {
			sum+=fiber.getLength();
		}
		return sum;
	}

	public int getNumHops(){
		return upLink.size();
	}

	public int getRouteID(){
		return routeID;
	}

	public void setYENID(int yenID){
		this.YENID = yenID;
	}

	public int getYENID(){
		return this.YENID;
	}

	public void setConflitList(List<RoutingAlgorithmSolution> conflictRoutes){
		this.conflictRoutes = conflictRoutes;
	}

	public List<RoutingAlgorithmSolution> getConflictRoute(){
		return this.conflictRoutes;
	}

	public short getSlotValue(int index){
		return this.slotOcupation[index];
	}

	public void resetSlotValue(){
		SimulationParameters parameters = new SimulationParameters();
		// Criar um vetor de disponibilidade 
		this.slotOcupation = new short[parameters.getNumberOfSlots()];
	}

	public void incrementSlotsOcupy(List<Integer> slots){

		for (int s: slots){
			this.incrementSlots(s);

			assert ((this.slotOcupation[s] <= this.conflictRoutes.size()) || (this.slotOcupation[s] > 0)):
			"Ocupação fora do limite";

			for (RoutingAlgorithmSolution route : this.conflictRoutes){
				route.incrementSlots(s);

				assert((route.slotOcupation[s] <= route.conflictRoutes.size()) || (route.slotOcupation[s] > 0)):
				"Ocupação fora do limite";
			}
		}
	}

	public void decreasesSlotsOcupy(List<Integer> slots){

		for (int s: slots){
			this.decreasesSlots(s);

			assert((this.slotOcupation[s] <= this.conflictRoutes.size()) || (this.slotOcupation[s] > 0));

			for (RoutingAlgorithmSolution route : this.conflictRoutes){
				route.decreasesSlots(s);

				assert((route.slotOcupation[s] <= route.conflictRoutes.size()) || (route.slotOcupation[s] > 0));
			}
		}
	}

	public void incrementSlots(int slot){
		this.slotOcupation[slot]++;
	}

	public void decreasesSlots(int slot){
		this.slotOcupation[slot]--;
	}

	public void setModulationsTypeByBitrate(List<ModulationLevelType> modulationsTypeByBitrate){
		this.modulationsTypeByBitrate = modulationsTypeByBitrate;
	}

	public void setSizeSlotTypeByBitrate(List<Integer> sizeSlotsbyBitrate){
		this.sizeSlotsbyBitrate = sizeSlotsbyBitrate;
	}
	
	public List<ModulationLevelType> getModulationsTypeByBitrate(){
		return this.modulationsTypeByBitrate;
	}

	public List<Integer> getSizeSlotTypeByBitrate(){
		return this.sizeSlotsbyBitrate;
	}

	public void setSlotsByMSCL(List<Integer> slotsByMSCL){
		this.slotsByMSCL = slotsByMSCL;
	}

	public List<Integer> getSlotsByMSCL(){
		return this.slotsByMSCL;
	}
}
