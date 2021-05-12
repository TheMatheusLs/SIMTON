package utility;
/**
 * Descreve a classe auxiliar tuple usada para armazenar informa��es da rede.
 * @author Andr� 
 */
public class Tuple {
	/**
	 * Identificador do n� fonte.
	 * @author Andr� 			
	 */	
	private int sourceId;
	/**
	 * Identificador do n� destino.
	 * @author Andr� 			
	 */
	private int destinationId;	
	/**
	 * Identificador do optical link.
	 * @author Andr� 			
	 */
	private transient int opticalLinkId;
	/**
	 * Comprimento do optical link..
	 * @author Andr� 			
	 */
	private double length;
	/**
	 * Construtor da classe.
	 * @param sourceId
	 * @param destinationId
	 * @param opticalLinkId
	 * @param length
	 * @author Andr�
	 */	
	public Tuple(final int sourceId, final int destinationId, final int opticalLinkId, final double length){		
		this.sourceId = sourceId;
		this.destinationId = destinationId;
		this.opticalLinkId = opticalLinkId;
		this.length = length;
	}
	/**
	 * M�todo para retornar o identificador do n� fonte.
	 * @return O atributo sourceId
	 * @author Andr� 			
	 */	
	public int getSourceId() {
		return sourceId;
	}
	/**
	 * M�todo para configurar o identificador do n� fonte.
	 * @param sourceId
	 * @author Andr� 			
	 */
	public void setSourceId(final int sourceId) {
		this.sourceId = sourceId;
	}
	/**
	 * M�todo para retornar o identificador do n� fonte.
	 * @return O atributo destinationId
	 * @author Andr� 			
	 */	
	public int getDestinationId() {
		return destinationId;
	}
	/**
	 * M�todo para configurar o identificador do n� destino.
	 * @param destinationId
	 * @author Andr� 			
	 */
	public void setDestinationId(final int destinationId) {
		this.destinationId = destinationId;
	}
	/**
	 * M�todo para retornar o identificador do optical link.
	 * @return O atributo opticalLinkId
	 * @author Andr� 			
	 */	
	public int getOpticalLinkId() {
		return opticalLinkId;
	}
	/**
	 * M�todo para configurar o identificador do optical link.
	 * @param opticalLinkId
	 * @author Andr� 			
	 */
	public void setOpticalLinkId(final int opticalLinkId) {
		this.opticalLinkId = opticalLinkId;
	}
	/**
	 * M�todo para retornar o comprimento do optical link.
	 * @return O atributo length
	 * @author Andr� 			
	 */	
	public double getLength() {
		return length;
	}
	/**
	 * M�todo para configurar o comprimento do optical link.
	 * @param length
	 * @author Andr� 			
	 */
	public void setLength(final double length) {
		this.length = length;
	}	

}
