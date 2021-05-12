package survivability;

import routing.RoutingAlgorithmSolution;
/**
 * Descreve a solução de proteção dedicada usada nos algoritmos do simulador.
 * @author André 
 */
public class DedicatedProtectionAlgorithmSolution {
	/**
	 * Rota de trabalho.
	 * @author André 			
	 */	
	private RoutingAlgorithmSolution workingPath;
	/**
	 * Rota de proteção.
	 * @author André 			
	 */	
	private RoutingAlgorithmSolution protectionPath;	
	/**
	 * Construtor da classe.
	 * @author André
	 */	
	public DedicatedProtectionAlgorithmSolution() {
		this.workingPath = new RoutingAlgorithmSolution();
		this.protectionPath = new RoutingAlgorithmSolution();
	}
	/**
	 * Construtor da classe.
	 * @param workingPath
	 * @param backupPath
	 * @author André
	 */		
	public DedicatedProtectionAlgorithmSolution(final RoutingAlgorithmSolution workingPath, final RoutingAlgorithmSolution backupPath) {		
		this.workingPath = workingPath;
		this.protectionPath = backupPath;
	}
	/**
     * Método para retornar a rota de trabalho.
     * @return O atributo workingPath
     * @author André 			
     */		
	public RoutingAlgorithmSolution getWorkingPath() {
		return workingPath;
	}
	/**
     * Método para configurar a rota de trabalho.
     * @param workingPath
     * @author André 			
     */	
	public void setWorkingPath(final RoutingAlgorithmSolution workingPath) {
		this.workingPath = workingPath;
	}
	/**
     * Método para retornar a rota de proteção.
     * @return O atributo protectionPath
     * @author André 			
     */	
	public RoutingAlgorithmSolution getProtectionPath() {
		return protectionPath;
	}
	/**
     * Método para configurar a rota de proteção.
     * @param protectionPath
     * @author André 			
     */	
	public void setProtectionPath(final RoutingAlgorithmSolution protectionPath) {
		this.protectionPath = protectionPath;
	}
	/**
     * Método para resetar a solução.
     * @author André 			
     */	
	public void clear(){		
		workingPath.clear();
		protectionPath.clear();
	}
}
