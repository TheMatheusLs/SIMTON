package network_topologies;

public class ToroidalDimention implements INetworkDimention{
    
    private double[][] lengths;
    private int numberOfNodes = 9;

    public ToroidalDimention(){
        //create network adjacency matrix
		lengths = new double[numberOfNodes][numberOfNodes];			
		for(int x=0;x<this.numberOfNodes;x++){
			for(int y=0;y<this.numberOfNodes;y++){
				lengths[x][y] = Double.MAX_VALUE;
			}
		}
		//Vetor com as distância dos enlace em Hectômetro de acordo com o artigo original.
		lengths[0][1]   = 300;
		lengths[0][2]   = 300;
		lengths[0][3]   = 300;
		lengths[0][6]   = 300;

		lengths[1][0]   = 300;
		lengths[1][2]   = 300;
		lengths[1][4]   = 300;
		lengths[1][7]   = 300;
        
		lengths[2][1]   = 300;
		lengths[2][6]   = 300;
		lengths[2][0]   = 300;
		lengths[2][8]   = 300;
        
		lengths[3][0]   = 300;
		lengths[3][4]   = 300;
		lengths[3][5]   = 300;
		lengths[3][6]   = 300;
        
		lengths[4][3]   = 300;
		lengths[4][1]   = 300;
		lengths[4][5]   = 300;
		lengths[4][7]   = 300;
        
		lengths[5][2]   = 300;
		lengths[5][4]   = 300;
		lengths[5][3]   = 300;
		lengths[5][8]   = 300;

        lengths[6][0]   = 300;
        lengths[6][3]   = 300;
        lengths[6][7]   = 300;
        lengths[6][8]   = 300;
        
        lengths[7][1]   = 300;
        lengths[7][4]   = 300;
        lengths[7][6]   = 300;
        lengths[7][8]   = 300;
        
        lengths[8][2]   = 300;
        lengths[8][5]   = 300;
        lengths[8][6]   = 300;
        lengths[8][7]   = 300;
    }

    public double[][] getLength(){
        return lengths;
    }
}