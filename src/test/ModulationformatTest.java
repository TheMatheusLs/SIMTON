package test;

import call_request.CallRequest;
import parameters.SimulationParameters;
import types.CallRequestType;
import types.ModulationLevelType;
import utility.Function;
/**
 * Descreve a o teste do calculo do n�mero de slots 
 * para cada formato de modula��o, como tamb�m da OSNR limiar.
 * @author Andr� 
 */
public class ModulationformatTest {

	public static void main(String[] args) {
		
		final SimulationParameters parameters = new SimulationParameters();
		final Function function = new Function();		
		final int [] bitRate = parameters.getBitRate();
		final ModulationLevelType [] modulLevel = parameters.getModulationLevelType();
		
		for(int x=0;x<bitRate.length;x++){			
			for(int y=0;y<modulLevel.length;y++){
				CallRequest call = new CallRequest(0,bitRate, CallRequestType.BIDIRECTIONAL);
				call.setBitRate(bitRate[x]);
				call.setModulationType(modulLevel[y]);
				
				final double snrLinear = Math.pow(10,modulLevel[y].getSNRIndB()/10);
				
				final double osnrLinear = (((double)call.getBitRate()*1E9)/(2*SimulationParameters.getSpacing()))*snrLinear;
				
				int numbSlots = function.calculateNumberOfSlots(modulLevel[y], call);
				System.out.println("Taxa: "+bitRate[x]+" Modula��o: "+modulLevel[y].getDescription());
				System.out.println("N. Slots: "+numbSlots+ " OSNR linear: "+osnrLinear+" OSNR: "+10*Math.log10(osnrLinear));				
			}
		}
		
		
		
		// TODO Auto-generated method stub

	}

}
