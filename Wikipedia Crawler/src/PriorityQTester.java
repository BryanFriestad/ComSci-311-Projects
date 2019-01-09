public class PriorityQTester {

    public static void main(String[] args){
        PriorityQ q = new PriorityQ();
        q.add("Bryan", 20);
        q.printQueue();
        q.add("Ty", 19);
        q.printQueue();
        q.add("Ryan", 21);
        q.printQueue();
        System.out.println("Max = " + q.returnMax());
        System.out.println("Removed = " + q.remove(2));
        q.printQueue();
        System.out.print("Priority Array = ");
        int[] arr = q.priorityArray();
        for(int i : arr){
            System.out.print(i + ", ");
        }
        System.out.println("\n");

        q.add("Weird Kid", 17);
        q.add("Ty", 19);
        q.add("Simone", 19);
        q.add("Emily", 20);
        q.add("Zach", 24);
        q.add("Ben", 22);
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

        System.out.println("Max = " + q.extractMax());
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
