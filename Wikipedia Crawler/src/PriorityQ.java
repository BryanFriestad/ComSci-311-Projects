/**
 * @author bryanf
 */
public class PriorityQ {

    private PriorityNode[] queue; //an array to hold the PriorityNodes, indexing starts at 1
    private int size;

    public PriorityQ(){
        queue = new PriorityNode[8]; //makes a new array with 8 elements
        size = 0; //there are no elements in the queue to start
    }

    /**
     * Adds a new node onto the max heap with the following parameters.
     * @param s the value of the node
     * @param priority the priority of the node
     *                 It then shifts the node up to its proper position, maintaining max heap
     */
    public void add(String s, int priority){
        size++;
        if(size > queue.length - 1) {
            increaseQueueSize();
        }
        int i = size;
        queue[size] = new PriorityNode(s, priority); //the new node is placed at the end of the heap
        while((i > 1) && (queue[i].getKey() > queue[i/2].getKey())){
            swap(i, i/2); //if the new node's key is greater than that of it's parent, swap them in the array
            i = i/2;
        }
    }

    /**
     * returns the the string associated with the maximum priority
     * if the queue is empty, it throws an exception
     * @return String
     */
    public String returnMax(){
        if(isEmpty()){
            throw new IllegalStateException("Queue is empty");
        }
        else{
            return queue[1].getValue();
        }
    }

    public String extractMax(){
        if(isEmpty()){
            throw new IllegalStateException("Queue is empty");
        }
        return remove(1);
    }

    public String remove(int i){
        if(isEmpty()){
            throw new IllegalStateException("Queue is empty");
        }
        if(i <= 0){
            throw new IllegalArgumentException("Error: i must be greater than 0");
        }
        else if(i > size){
            throw new IllegalArgumentException("Error: i must be within the range of the queue");
        }
        String output = queue[i].getValue();
        swap(i, size);
        size--;
        while(i < size && (((2*i + 1) <= size) || ((2*i) <= size))) {
            int left = queue[2 * i].getKey();
            int max;
            if (2 * i + 1 <= size) {
                int right = queue[2 * i + 1].getKey();
                max = Math.max(left, right);
            } else {
                max = left;
            }

            if (max > queue[i].getKey()) {
                if (max == left) {
                    swap(i, 2 * i);
                    i = 2 * i;
                } else {
                    swap(i, 2 * i + 1);
                    i = 2 * i + 1;
                }
            } else {
                break;
            }
        }
        return output;
    }

    /**
     * Decrements the priority of the i-th(i >= 1) element of the queue by a value of k
     * @param i the element to change
     * @param k the amount to decrease the i-th element by
     */
    public void decrementPriority(int i, int k){
        if(isEmpty()){
            throw new IllegalStateException("Queue is empty");
        }
        if(i <= 0){
            throw new IllegalArgumentException("Error: i must be greater than 0");
        }
        else if(i > size){
            throw new IllegalArgumentException("Error: i must be within the range of the queue");
        }
        queue[i].changePriority(-k); //changePriority accepts positive or negative values

        //Reposition the element i to its proper position
        while(i < size && (((2*i + 1) <= size) || ((2*i) <= size))) {
            int left = queue[2 * i].getKey();
            int max;
            if (2 * i + 1 <= size) {
                int right = queue[2 * i + 1].getKey();
                max = Math.max(left, right);
            } else {
                max = left;
            }

            if (max > queue[i].getKey()) {
                if (max == left) {
                    swap(i, 2 * i);
                    i = 2 * i;
                } else {
                    swap(i, 2 * i + 1);
                    i = 2 * i + 1;
                }
            } else {
                break;
            }
        }
    }

    public int[] priorityArray(){
        if(isEmpty()){
            throw new IllegalStateException("Queue is empty");
        }
        int[] b = new int[size];
        for(int i = 0; i < size; i++){
            b[i] = queue[i+1].getKey();
        }
        return b;
    }

    public boolean isEmpty(){
        return (size == 0);
    }

    public int getKey(int i){
        if(isEmpty()){
            throw new IllegalStateException("Queue is empty");
        }
        if(i <= 0){
            throw new IllegalArgumentException("Error: i must be greater than 0");
        }
        else if(i > size){
            throw new IllegalArgumentException("Error: i must be within the range of the queue");
        }
        return queue[i].getKey();
    }

    public String getValue(int i){
        if(isEmpty()){
            throw new IllegalStateException("Queue is empty");
        }
        if(i <= 0){
            throw new IllegalArgumentException("Error: i must be greater than 0");
        }
        else if(i > size){
            throw new IllegalArgumentException("Error: i must be within the range of the queue");
        }
        return queue[i].getValue();
    }

    /**
     * Called when the queue is full and needs more space.
     * This creates a new array with twice as much space and then copies
     * over the elements
     */
    private void increaseQueueSize(){
        PriorityNode[] newQ = new PriorityNode[queue.length * 2 - 1]; //makes the queue twice as long, accounting for the initial empty cell
        for(int i = 1; i <= (size - 1); i++){ //size - 1 because size will be out of bounds
            newQ[i] = queue[i];
        }
        queue = newQ;
    }

    /**
     * swaps the positions of two elements in the priority queue
     * @param a index of the first element
     * @param b index of the second element
     */
    private void swap(int a, int b){
        PriorityNode temp = queue[b];
        queue[b] = queue[a];
        queue[a] = temp;
    }

    public void printQueue(){
        System.out.println("Priority Q: ");
        for(int i = 1; i <= size; i++){
            System.out.println("Index: " + i + "\tPriority: " + queue[i].getKey() + "\tValue: " + queue[i].getValue());
        }
        System.out.println();
    }
}
