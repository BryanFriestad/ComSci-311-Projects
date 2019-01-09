/**
 * @author bryanf
 */
public class FifoQueue {

    private FifoNode root;
    private FifoNode base;
    private int size;

    public FifoQueue(){
        size = 0;
    }

    public FifoQueue(String v){
        size = 1;
        root = new FifoNode(v);
        base = root;
    }

    public void push(String v) {
        size++;
        if (size == 1) {
            root = new FifoNode(v);
            base = root;
        } else {
            FifoNode temp = new FifoNode(v);
            base.setChild(temp);
            base = temp;
        }
    }

    public String pop(){
        if(isEmpty()){
            throw new IllegalStateException("Error! FifoQueue is empty!");
        }
        size--;
        String temp = root.getValue();
        root = root.getChild();
        return temp;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public void printQueue(){
        FifoNode t = root;
        while(t != null){
            System.out.print(t.getValue() + ", ");
            t = t.getChild();
        }
        System.out.println();
    }
}
