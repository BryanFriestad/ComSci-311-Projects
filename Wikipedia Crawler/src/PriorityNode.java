/**
 * @author bryanf
 */
public class PriorityNode {
    private String value;
    private int key;

    public PriorityNode(String v, int k){
        value = v;
        key = k;
    }

    public String getValue(){
        return value;
    }

    public int getKey(){
        return key;
    }

    public void changePriority(int change){
        key += change;
    }
}
