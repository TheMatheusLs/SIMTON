package network_topologies;

public class RingDimention implements INetworkDimention{
    
    private double[][] lengths;
    private int numberOfNodes = 6;

    public RingDimention(){
        //create network adjacency matrix
		lengths = new double[numberOfNodes][numberOfNodes];			
		for(int x=0;x<this.numberOfNodes;x++){
			for(int y=0;y<this.numberOfNodes;y++){
				lengths[x][y] = Double.MAX_VALUE;
			}
		}
		//Vetor com as distância dos enlace em Hectômetro de acordo com o artigo original.
		lengths[0][1]   = 200;
		lengths[0][5]   = 200;

		lengths[1][0]   = 200;
		lengths[1][2]   = 200;
		lengths[1][4]   = 400;
        
		lengths[2][1]   = 200;
		lengths[2][3]   = 200;
		lengths[2][5]   = 400;
        
		lengths[3][2]   = 200;
		lengths[3][4]   = 200;
        
		lengths[4][3]   = 200;
		lengths[4][1]   = 200;
		lengths[4][5]   = 200;
        
		lengths[5][2]   = 400;
		lengths[5][4]   = 200;
		lengths[5][0]   = 200;
    }

    public double[][] getLength(){
        return lengths;
    }
}