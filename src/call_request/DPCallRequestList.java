package call_request;

import java.util.ArrayList;
import java.util.List;
/**
 * Descreve o gerenciamento da lista de requisições de conexão da rede.
 * @author André 
 */
public class DPCallRequestList {
	/**
	 * Número de requisições estabelecidas.
	 * @author André 			
	 */		
	private transient int nOfEstSurvCalls;
	/**
	 * Lista das conexões ativas da rede.
	 * @author André 			
	 */
	private transient List<DPCallRequest> dpCallList;
	/**
	 * Construtor da classe.
	 * @author André		
	 */
	public DPCallRequestList() {		
		this.dpCallList = new ArrayList<DPCallRequest>();
		this.nOfEstSurvCalls = 0;
	}
	/**
	 * Método para retornar o número de conexões estabelecidas.
	 * @return O atributo nOfEstSurvCalls
	 * @author André 			
	 */		
	public int getNumberOfEstablishDPCalls() {
		return this.nOfEstSurvCalls;
	}
	/**
	  * Método para adicionar um nova conexão a lista.
	  * @param dpRequestCall
	  * @author André 			
	  */		
	public void addDPCall(final DPCallRequest dpRequestCall){
		 this.dpCallList.add(dpRequestCall);
		 this.nOfEstSurvCalls++;		 
	}
	/**
	  * Método para remover as conexões que já expiraram da rede.
	  * @param acumulatedTime
	  * @author André 			
	  */	
	public void removeDPCall(final double acumulatedTime){
		 
		 for(int x=0;x<this.dpCallList.size();x++){
			
			 final double time = this.dpCallList.get(x).getDecayTime();
			 
			 if(time<acumulatedTime){
				 this.dpCallList.get(x).desallocate();					 
				 this.dpCallList.remove(x);					 
			 }			 
		 }  	  
	}
	 /**
	  * Método para resetar o tempo das conexões ativas.
	  * @param acumulatedTime
	  * @author André 			
	  */		
	public void resetTimeDPCall(final double acumulatedTime){		 
		 for(int i=0;i<this.dpCallList.size();i++){
			 this.dpCallList.get(i).setDecayTime(this.dpCallList.get(i).getDecayTime()-acumulatedTime);
		 }
	}
	/**
	  * Método para resetar a lista de conexões.
	  * @author André 			
	  */		
	public void eraseSurvivabilityCallList(){
		this.dpCallList = new ArrayList<DPCallRequest>();
		this.nOfEstSurvCalls = 0; 
	}

}
