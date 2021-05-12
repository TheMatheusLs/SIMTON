package arquitecture;

/**
 * Descreve o componente optical fiber usado no simulador.
 * @author André 
 */

public class OpticalFiber {
	/**
	 * Tamanho da fibra.
	 * @author André
	 */	
	private double length;	
	/**
	* Construtor da classe.
	* @author André
	*/	
	public OpticalFiber(final double length){ 
		this.length = length;		
	}
	/**
	 * Método para retornar o valor do tamanho da fibra.
	 * @return O atributo length.
	 * @author André 			 
	 */
	public double getLength() {
		return length;
	}	
	/**
	 * Método para configurar o valor do tamanho da fibra.
	 * @author André
	 */
	public void setLength(final double length) {
		this.length = length;
	}	
}
