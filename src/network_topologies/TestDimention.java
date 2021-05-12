package network_topologies;

public class TestDimention implements INetworkDimention{
    
    private double[][] lengths;
    private int numberOfNodes = 7;

    public TestDimention(){
        //create network adjacency matrix
		lengths = new double[numberOfNodes][numberOfNodes];			
		for(int x=0;x<this.numberOfNodes;x++){
			for(int y=0;y<this.numberOfNodes;y++){
				lengths[x][y] = Double.MAX_VALUE;
			}
		}

        lengths[0][1]   = 100;
        lengths[1][0]   = 100;

        lengths[1][2]   = 100;
        lengths[2][1]   = 100;

        lengths[2][6]   = 100;
        lengths[6][2]   = 100;

        lengths[2][5]   = 100;
        lengths[5][2]   = 100;

        lengths[2][3]   = 100;
        lengths[3][2]   = 100;
		
        lengths[3][4]   = 100;
        lengths[4][3]   = 100;
    }

    public double[][] getLength(){
        return lengths;
    }
}