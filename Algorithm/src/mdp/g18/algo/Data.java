package mdp.g18.algo;

public class Data{
    private int[] data;

    public Data(int s1, int s2){
        data = new int[] {s1,s2};
    }

    public int getElement(int index){
        return data[index];
    }
}
