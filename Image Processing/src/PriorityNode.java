/**
 * @author bryanf
 */
public class PriorityNode {
    private WGraphVertex value;
    private int key;

    public PriorityNode(WGraphVertex v, int k){
        value = v;
        key = k;
    }

    public WGraphVertex getValue(){
        return value;
    }

    public int getKey(){
        return key;
    }

    public void changePriority(int change){
        key += change;
    }
}
