package exceptions;

public class ExceptionsByMTHS extends Exception {
    
	private static final long serialVersionUID = 8517899543157461034L;

    // Código
	private int exceptionCode;

	public ExceptionsByMTHS(final String message, final int exceptionCode){		
		super(message);
		this.setExceptionCode(exceptionCode);
	}

	public int getExceptionCode() {
		return this.exceptionCode;
	}

	public void setExceptionCode(final int exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
}
