package survivability;

import routing.RoutingAlgorithmSolution;
/**
 * Descreve a solu��o de prote��o dedicada usada nos algoritmos do simulador.
 * @author Andr� 
 */
public class DedicatedProtectionAlgorithmSolution {
	/**
	 * Rota de trabalho.
	 * @author Andr� 			
	 */	
	private RoutingAlgorithmSolution workingPath;
	/**
	 * Rota de prote��o.
	 * @author Andr� 			
	 */	
	private RoutingAlgorithmSolution protectionPath;	
	/**
	 * Construtor da classe.
	 * @author Andr�
	 */	
	public DedicatedProtectionAlgorithmSolution() {
		this.workingPath = new RoutingAlgorithmSolution();
		this.protectionPath = new RoutingAlgorithmSolution();
	}
	/**
	 * Construtor da classe.
	 * @param workingPath
	 * @param backupPath
	 * @author Andr�
	 */		
	public DedicatedProtectionAlgorithmSolution(final RoutingAlgorithmSolution workingPath, final RoutingAlgorithmSolution backupPath) {		
		this.workingPath = workingPath;
		this.protectionPath = backupPath;
	}
	/**
     * M�todo para retornar a rota de trabalho.
     * @return O atributo workingPath
     * @author Andr� 			
     */		
	public RoutingAlgorithmSolution getWorkingPath() {
		return workingPath;
	}
	/**
     * M�todo para configurar a rota de trabalho.
     * @param workingPath
     * @author Andr� 			
     */	
	public void setWorkingPath(final RoutingAlgorithmSolution workingPath) {
		this.workingPath = workingPath;
	}
	/**
     * M�todo para retornar a rota de prote��o.
     * @return O atributo protectionPath
     * @author Andr� 			
     */	
	public RoutingAlgorithmSolution getProtectionPath() {
		return protectionPath;
	}
	/**
     * M�todo para configurar a rota de prote��o.
     * @param protectionPath
     * @author Andr� 			
     */	
	public void setProtectionPath(final RoutingAlgorithmSolution protectionPath) {
		this.protectionPath = protectionPath;
	}
	/**
     * M�todo para resetar a solu��o.
     * @author Andr� 			
     */	
	public void clear(){		
		workingPath.clear();
		protectionPath.clear();
	}
}
