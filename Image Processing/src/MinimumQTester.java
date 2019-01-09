public class MinimumQTester {

    public static void main(String[] args){
        MinimumQ q = new MinimumQ();
        q.add(new WGraphVertex(1, 2), 20);
        q.printQueue();
        q.add(new WGraphVertex(3, 4), 19);
        q.printQueue();
        q.add(new WGraphVertex(5, 6), 21);
        q.printQueue();
        System.out.println("Max = " + q.returnMin());
        System.out.println("Removed = " + q.remove(2));
        q.printQueue();
        System.out.print("Priority Array = ");
        int[] arr = q.priorityArray();
        for(int i : arr){
            System.out.print(i + ", ");
        }
        System.out.println("\n");

        q.add(new WGraphVertex(1, 2), 17);
        q.add(new WGraphVertex(3, 4), 19);
        q.add(new WGraphVertex(5, 6), 19);
        q.add(new WGraphVertex(7, 8), 20);
        q.add(new WGraphVertex(11, 12), 24);
        q.add(new WGraphVertex(9, 10), 22);
        q.printQueue();
        System.out.print("Priority Array = ");
        arr = q.priorityArray();
        for(int i : arr){
            System.out.print(i + ", ");
        }
        System.out.println("\n");

        q.decrementPriority(6, 3);
        q.decrementPriority(2, 14);
        q.printQueue();

        System.out.println("Max = " + q.extractMin());
        q.printQueue();

        System.out.println("Removed = " + q.remove(6));
        q.printQueue();
        System.out.println("Removed = " + q.remove(4));
        q.printQueue();
        System.out.print("Priority Array = ");
        arr = q.priorityArray();
        for(int i : arr){
            System.out.print(i + ", ");
        }
        System.out.println("\n");
    }
}
