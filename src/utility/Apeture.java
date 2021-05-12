package utility;

public class Apeture {
    private int initPosition;
    private int size; 

    public Apeture(int initPosition, int size){
        this.initPosition = initPosition;
        this.size = size;
    }

    public int getPosition(){
        return initPosition;
    }

    public int getSize(){
        return size;
    }
}
