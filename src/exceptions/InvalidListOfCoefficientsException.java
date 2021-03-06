package exceptions;
/**
 *Exce??o para verificar a lista de coeficientes
 *do algoritmo que usa a fun??o PSR.  
 */
public class InvalidListOfCoefficientsException extends Exception {
	
	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 672284858989854081L;
	/**
	 *C?digo da exce??o. 
	 *@author Andr? 
	 */		
	private int exceptionCode;
	/**
	 *Construtor da classe.
	 *@param message
	 *@param exceptionCode
	 *@author Andr?  
	 */	
	public InvalidListOfCoefficientsException(final String message, final int exceptionCode) {
		super(message);
		this.setExceptionCode(exceptionCode);
	}
	/**
	 *M?todo para retornar o c?digo da exce??o. 
	 *@return O atributo exceptionCode
	 *@author Andr? 
	 */	
	public int getExceptionCode() {
		return this.exceptionCode;
	}
	/**
	 *M?todo para configurar o c?digo da exce??o. 
	 *@author Andr? 
	 */
	public void setExceptionCode(final int exceptionCode) {
		this.exceptionCode = exceptionCode;
	}	

}
