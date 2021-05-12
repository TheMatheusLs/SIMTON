package exceptions;
/**
 *Exceção para verificar a rota encontrada.
 */
public class InvalidRoutesException extends Exception {
	
	/**
	 * Serial.
	 */
	private static final long serialVersionUID = -3837452125601015355L;
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
	public InvalidRoutesException(final String message, final int exceptionCode) {
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
