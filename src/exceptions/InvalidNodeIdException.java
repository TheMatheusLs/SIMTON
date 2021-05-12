package exceptions;
/**
 *Exce��o para verificar os identificadores dos n�s de cada
 *requisi��o de conex�o.  
 */
public class InvalidNodeIdException extends Exception {
			
	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 2111943885182580688L;
	/**
	 *C�digo da exce��o. 
	 *@author Andr� 
	 */		
	private int exceptionCode;
	/**
	 *Construtor da classe.
	 *@param message
	 *@param exceptionCode
	 *@author Andr�  
	 */	
	public InvalidNodeIdException(final String message, final int exceptionCode) {
		super(message);
		this.setExceptionCode(exceptionCode);
	}
	/**
	 *M�todo para retornar o c�digo da exce��o. 
	 *@return O atributo exceptionCode
	 *@author Andr� 
	 */		
	public int getExceptionCode() {
		return this.exceptionCode;
	}
	/**
	 *M�todo para configurar o c�digo da exce��o. 
	 *@author Andr� 
	 */
	public void setExceptionCode(final int exceptionCode) {
		this.exceptionCode = exceptionCode;
	}	

}
