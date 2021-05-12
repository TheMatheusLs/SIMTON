package utility;

public class PertinenceFunction {

	private double[] indices;
	
	public PertinenceFunction(double[] indices) {
		this.indices = indices; 
	}
	
	/*
	 * Caso retorne -1, considera-se erro
	 */
	public double calculate(double value, int state){
		double result = 0;
		switch (state) {
		case 1:
			if(value<=indices[0]){
				result = 1;
			}else if(value>=indices[1]){
				result = 0;
			}else{
				result = (indices[1]-value)/(indices[1]-indices[0]);
			}
			break;
		case 2:
			
			if(value<=indices[2] || value>=indices[5]){
				result = 0;
			}else if(value>=indices[3] && value<=indices[4]){
				result = 1;
			}else if(value>indices[2] && value<indices[3]){
				result = (value-indices[2])/(indices[3]-indices[2]);
			}else if(value>indices[4] && value<indices[5]){
				result = (indices[5]-value)/(indices[5]-indices[4]);
			}
			break;
		case 3:
			if(value<=indices[6]){
				result = 0;
			}else if(value>=indices[7]){
				result = 1;
			}else{
				result = (value-indices[6])/(indices[7]-indices[6]);
			}
			break;

		default:
			result = -1;
			break;
		}
		return result;
	}

}
