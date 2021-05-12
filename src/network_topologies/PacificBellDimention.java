package network_topologies;

public class PacificBellDimention implements INetworkDimention{
    
    private double[][] lengths;
    private int numberOfNodes = 17;

    public PacificBellDimention(){
        //create network adjacency matrix
		lengths = new double[numberOfNodes][numberOfNodes];			
		for(int x=0;x<this.numberOfNodes;x++){
			for(int y=0;y<this.numberOfNodes;y++){
				lengths[x][y] = Double.MAX_VALUE;
			}
		}

        lengths[0][4]   = 1000;
		lengths[4][0]   = 1000;
		lengths[0][3]   = 900;
		lengths[3][0]   = 900;
		lengths[0][2]   = 1000;
		lengths[2][0]   = 1000;		
		lengths[0][5]  = 1200;
		lengths[5][0]  = 1200;		
		lengths[0][6]  = 1500;
		lengths[6][0]  = 1500;
		lengths[1][3]   = 800;
		lengths[3][1]   = 800;		
		lengths[1][4]   = 900;
		lengths[4][1]   = 900;
		lengths[2][7]   = 600;
		lengths[7][2]   = 600;
		lengths[3][8]  = 1400;
		lengths[8][3]  = 1400;		
		lengths[3][9]  = 1000;
		lengths[9][3]  = 1000;		
		lengths[3][12]  = 1000;
		lengths[12][3]  = 1000;		
		lengths[3][15]  = 1000;
		lengths[15][3]  = 1000;		   
		lengths[4][8]   = 1200;
		lengths[8][4]   = 1200;		
		lengths[4][13]  = 900;
		lengths[13][4]  = 900;		
		lengths[4][15]  = 1000;
		lengths[15][4]  = 1000;
		lengths[5][14]  = 1500;
		lengths[14][5]  = 1500;
		lengths[6][7]   = 1100;
		lengths[7][6]   = 1100;		
		lengths[6][9]   = 1000;
		lengths[9][6]   = 1000;		
		lengths[6][10]  = 1000;
		lengths[10][6]  = 1000;
		lengths[10][11] = 1000;
		lengths[11][10] = 1000;		
		lengths[11][12] = 900;
		lengths[12][11] = 900;
		lengths[13][16] = 900;
		lengths[16][13] = 900;		
		lengths[14][16]  = 900;
		lengths[16][14]  = 900;
    }

    public double[][] getLength(){
        return lengths;
    }
}