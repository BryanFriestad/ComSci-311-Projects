/**
 * @author bryanf
 */
public class FifoNode {

    private FifoNode child;
    private String value;

    public FifoNode(String value){
        this.value = value;
        this.child = null;
    }

    public void setChild(FifoNode n){
        child = n;
    }

    public FifoNode getChild()
    {
        return child;
    }

    public String getValue(){
        return value;
    }
}
