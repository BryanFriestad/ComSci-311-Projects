import java.util.ArrayList;

public class Tester {
    static long startTime;
    static long startTime_timer;

    public static void main(String[] args){
        String input = "resources/image4.txt";

        ImageProcessor ip = new ImageProcessor(input);
        ip.writeReduced(17, "test.txt");
    }

    private static void testEquality(WGraphVertex v1, WGraphVertex v2){
        System.out.println("Testing equality of " + v1 + " and " + v2 + ": " + v1.equals(v2) + "/" + v2.equals(v1));
    }

    private static ArrayList<Integer> intArrToIntList(int[] arr) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int x :
                arr) {
            list.add(x);
        }
        return list;
    }

    public static void printTime(long start_time){
        long curr_time = System.currentTimeMillis();
        long elapsed_time = curr_time - start_time;

        System.out.println("Elapsed time: " + elapsed_time);
    }

    public static void startTimer(){
        System.out.println("Timer Started");
        startTime_timer = System.nanoTime();
    }

    public static void showTimer(){
        long curr_time = System.nanoTime();
        long elapsed_time = curr_time - startTime_timer;

        System.out.println("Elapsed time: " + elapsed_time);
    }
}
