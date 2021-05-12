package exceptions;
/**
 *Exceção para verificar a lista de coeficientes
 *do algoritmo que usa a função PSR.  
 */
public class InvalidListOfCoefficientsException extends Exception {
	
	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 672284858989854081L;
	/**
	 *Código da exceção. 
	 *@author André 
	 */		
	private int exceptionCode;
	/**
	 *Construtor da classe.
	 *@param message
	 *@param exceptionCode
	 *@author André  
	 */	
	public InvalidListOfCoefficientsException(final String message, final int exceptionCode) {
		super(message);
		this.setExceptionCode(exceptionCode);
	}
	/**
	 *Método para retornar o código da exceção. 
	 *@return O atributo exceptionCode
	 *@author André 
	 */	
	public int getExceptionCode() {
		return this.exceptionCode;
	}
	/**
	 *Método para configurar o código da exceção. 
	 *@author André 
	 */
	public void setExceptionCode(final int exceptionCode) {
		this.exceptionCode = exceptionCode;
	}	

}
