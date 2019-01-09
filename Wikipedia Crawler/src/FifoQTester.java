public class FifoQTester {

    public static void main(String[] args){
        FifoQueue q = new FifoQueue();
        q.push("hello");
        q.printQueue();
        q.push("hi");
        q.push("blue");
        q.printQueue();
        q.push("red");
        q.printQueue();
        q.pop();
        q.pop();
        q.printQueue();
        q.push("yellow");
        q.printQueue();
        q.pop();
        q.printQueue();
        q.pop();
        q.printQueue();
        q.pop();
        q.printQueue();
        q.push("blood");
        q.printQueue();
        q.pop();
        q.printQueue();
        q.pop(); //should error
    }
}
