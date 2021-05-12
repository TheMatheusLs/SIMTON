package network_topologies;

public class FinlandNewDimention implements INetworkDimention{
    
    private double[][] lengths;
    private int numberOfNodes = 12;

    public FinlandNewDimention(){
        //create network adjacency matrix
		lengths = new double[numberOfNodes][numberOfNodes];			
		for(int x=0;x<this.numberOfNodes;x++){
			for(int y=0;y<this.numberOfNodes;y++){
				lengths[x][y] = Double.MAX_VALUE;
			}
		}
		// Dimensões em metro
        lengths[0][1]   = 250000;
		lengths[0][2]   = 200000;
		lengths[1][0]   = 250000;		
		lengths[1][3]   = 300000;
		lengths[1][4]   = 400000;
		lengths[1][5]   = 850000;
		lengths[2][0]   = 200000;
		lengths[2][3]   = 150000;
		lengths[2][7]   = 200000;
		lengths[3][1]   = 300000;		
		lengths[3][2]   = 150000;		
		lengths[3][8]   = 100000;	
		lengths[4][1]   = 400000;
		lengths[4][6]   = 300000;
		lengths[4][9]   = 350000;
		lengths[5][1]   = 850000;
		lengths[5][6]   = 450000;
		lengths[6][4]   = 300000;
		lengths[6][5]   = 450000;
		lengths[6][10]  = 550000;
		lengths[7][2]   = 200000;
		lengths[7][8]   = 250000;
		lengths[7][11]  = 350000;
		lengths[8][3]   = 100000;
		lengths[8][7]   = 250000;
		lengths[8][9]   = 300000;
		lengths[8][11]  = 200000;
		lengths[9][4]   = 350000;
		lengths[9][8]   = 300000;
		lengths[9][10]  = 400000;
		lengths[9][11]  = 500000;
		lengths[10][6]  = 550000;
		lengths[10][9]  = 400000;
		lengths[10][11] = 700000;
		lengths[11][7]  = 350000;
		lengths[11][8]  = 200000;
		lengths[11][9]  = 500000;
		lengths[11][10] = 700000;
    }

    public double[][] getLength(){
        return lengths;
    }
}