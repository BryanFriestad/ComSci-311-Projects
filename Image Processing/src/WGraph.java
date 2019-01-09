import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IllegalFormatException;

public class WGraph {

    private int numV;
    private int numE;
    private ArrayList<WGraphVertex> vertices = new ArrayList<>(); //don't think i need this
    private HashMap<WGraphVertex, ArrayList<WGraphVertex>> adjacencyMap = new HashMap<>();
    private HashMap<Pair<WGraphVertex, WGraphVertex>, Integer> weights = new HashMap<>();
    private HashMap<WGraphVertex, WGraphVertex> parent = new HashMap<>();
    private HashMap<WGraphVertex, Integer> distance = new HashMap<>();
    private MinimumQ minQ = new MinimumQ();
    private HashMap<WGraphVertex, Boolean> found = new HashMap<>();

    /**
     * Generate a new weighted graph from an external file at the location FName
     *
     * (a) First line contains a number indicating the number of vertices in the graph
     * (b) Second line contains a number indicating the number of edges in the graph
     * (c) All subsequent lines have five numbers: source vertex coordinates
     *      (first two numbers), destination vertex coordinates (third and fourth numbers)
     *      and weight of the edge connecting the source vertex to the destination
     *      (assume direction of edge from source to destination).
     *
     * For example:
     * 4
     * 4
     * 1 2 3 4 10
     * 5 6 1 2 9
     * 3 4 5 6 8
     * 1 2 7 8 2
     *
     * @param FName location of the text file representing the weighted graph
     */
    public WGraph(String FName){
        File file = new File(FName);
        try {
            FileReader reader;
            BufferedReader buff_reader;
            try {
                reader = new FileReader(file);
                buff_reader = new BufferedReader(reader);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
            ArrayList<String> lines = new ArrayList<String>();
            while(buff_reader.ready()){
                lines.add(buff_reader.readLine());
            }
            numV = Integer.parseInt(lines.get(0));
            numE = Integer.parseInt(lines.get(1));
            if(lines.size() != numE + 2){
                throw new IllegalStateException("Error: graph doesn't have correct number of edge data");
            }
            for(int i = 0; i < numE; i++){
                String[] data = lines.get(i + 2).split(" ");
                if(data.length != 5){
                    throw new IllegalStateException("Error: graph file has invalid format"); //why is illegal format exception private lol
                }
                int ux = Integer.parseInt(data[0]);
                int uy = Integer.parseInt(data[1]);
                int vx = Integer.parseInt(data[2]);
                int vy = Integer.parseInt(data[3]);
                int weight = Integer.parseInt(data[4]);
                if(weight < 0){
                    throw new IllegalStateException("Error: edge weight cannot be negative");
                }
                if(ux < 0 || uy < 0 || vx < 0 || vy < 0){
                    throw new IllegalStateException("Error: node vertex coordinates cannot be less than 0");
                }
                WGraphVertex u = new WGraphVertex(ux, uy);
                WGraphVertex v = new WGraphVertex(vx, vy);
                Pair<WGraphVertex, WGraphVertex> p = new Pair<>(u, v);

                //add info to adjacencyMap
                if(!adjacencyMap.containsKey(u)){
                    ArrayList<WGraphVertex> temp_list = new ArrayList<>();
                    temp_list.add(v);
                    adjacencyMap.put(u, temp_list);
                    if(!vertices.contains(u)){
                        vertices.add(u);
                    }
                    if(!vertices.contains(v)){
                        vertices.add(v);
                    }
                }
                else{//vertex already exists
                    ArrayList<WGraphVertex> temp_list = adjacencyMap.get(u);
                    temp_list.add(v);
                    adjacencyMap.put(u, temp_list);
                    if(!vertices.contains(v)){
                        vertices.add(v);
                    }
                }

                //add info to weight data
                if(!weights.containsKey(p)){
                    weights.put(p, weight);
                }
                else{//edge pair already exists
                    throw new IllegalStateException("Error: this edge already exists");
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error: no file " + FName + " found");
        }
        catch (IOException e){
            e.printStackTrace();
        }

        for(WGraphVertex v : vertices){
            parent.put(v, null);
            distance.put(v, Integer.MAX_VALUE);
            found.put(v, false);
        }
    }

    public WGraph(ArrayList<String> text_graph, int height, int width){
        numV = Integer.parseInt(text_graph.get(0));
        numE = Integer.parseInt(text_graph.get(1));
        if(text_graph.size() != numE + 2){
            throw new IllegalStateException("Error: graph doesn't have correct number of edge data");
        }
        for(int i = 0; i < numE; i++){

            String[] data = text_graph.get(i + 2).split(" ");
            if(data.length != 5){
                throw new IllegalStateException("Error: graph file has invalid format"); //why is illegal format exception private lol
            }
            int ux = Integer.parseInt(data[0]);
            int uy = Integer.parseInt(data[1]);
            int vx = Integer.parseInt(data[2]);
            int vy = Integer.parseInt(data[3]);
            int weight = Integer.parseInt(data[4]);
            if(weight < 0){
                throw new IllegalStateException("Error: edge weight cannot be negative");
            }
            if(ux < 0 || uy < 0 || vx < 0 || vy < 0){
                throw new IllegalStateException("Error: node vertex coordinates cannot be less than 0");
            }

            WGraphVertex u = new WGraphVertex(ux, uy);
            WGraphVertex v = new WGraphVertex(vx, vy);
            Pair<WGraphVertex, WGraphVertex> p = new Pair<>(u, v);
            ArrayList<WGraphVertex> temp_list = adjacencyMap.get(u);
            if(temp_list == null){
                temp_list = new ArrayList<>();
            }
            temp_list.add(v);
            adjacencyMap.put(u, temp_list);
            if(!weights.containsKey(p)){
                weights.put(p, weight);
            }
            else{//edge pair already exists
                throw new IllegalStateException("Error: this edge already exists");
            }
        }

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                vertices.add(new WGraphVertex(i, j));
            }
        }

        for(WGraphVertex v : vertices){
            parent.put(v, null);
            distance.put(v, Integer.MAX_VALUE);
            found.put(v, false);
        }
    }

    // pre:  ux, uy, vx, vy are valid coordinates of vertices u and v
    //       in the graph
    // post: return arraylist contains even number of integers,
    //       for any even i,
    //       i-th and i+1-th integers in the array represent
    //       the x-coordinate and y-coordinate of the i-th vertex
    //       in the returned path (path is an ordered sequence of vertices)
    public ArrayList<Integer> V2V(int ux, int uy, int vx, int vy){
        if(vx == ux && vy == uy){
            ArrayList<Integer> output = new ArrayList<Integer>();
            output.add(ux);
            output.add(uy);
            return output;
        }
        for(WGraphVertex v : vertices){
            parent.put(v, null);
            distance.put(v, Integer.MAX_VALUE);
            found.put(v, false);
        }
        distance.put(new WGraphVertex(ux, uy), 0);
        for(WGraphVertex v : vertices){
            minQ.add(v, distance.get(v));
        }
        while(!minQ.isEmpty()){
            WGraphVertex u = minQ.extractMin();
            found.put(u, true);
            ArrayList<WGraphVertex> v_prime = adjacencyMap.get(u);
            if(v_prime == null){ //if there are no children, do not make an iterator
                continue;
            }
            for(WGraphVertex v : v_prime){
                if(!found.get(v)){
                    Pair<WGraphVertex, WGraphVertex> p = new Pair<>(u, v);
                    int new_dist = (weights.get(p) + distance.get(u));
                    int old_dist = distance.get(v);
                    if(old_dist > new_dist){
                        distance.put(v, new_dist);
                        parent.put(v, u);
                        minQ.decrementPriority(v, old_dist - new_dist);
                    }
                }
            }
        }

        if(parent.get(new WGraphVertex(vx, vy)) == null){
            return new ArrayList<Integer>();
        }
        ArrayList<WGraphVertex> path = new ArrayList<>();
        ArrayList<Integer> output = new ArrayList<>();
        WGraphVertex node = new WGraphVertex(vx, vy);
        path.add(node);
        while((node = parent.get(node)) != null){
            path.add(node);
        }
        if(!path.get(path.size() - 1).equals(new WGraphVertex(ux, uy))){
            return new ArrayList<Integer>();
        }
        for(int i = path.size() - 1; i >= 0; i--){
            output.add(path.get(i).getX());
            output.add(path.get(i).getY());
        }
        //turn vertex list into even length int list as described in post-conditions
        return output;
    }


    // pre:  ux, uy are valid coordinates of vertex u from the graph
    //       S represents a set of vertices.
    //       The S arraylist contains even number of intergers
    //       for any even i,
    //       i-th and i+1-th integers in the array represent
    //       the x-coordinate and y-coordinate of the i-th vertex
    //       in the set S.
    // post: same structure as the last method’s post.
    public ArrayList<Integer> V2S(int ux, int uy, ArrayList<Integer> S){
        for(WGraphVertex v : vertices){
            parent.put(v, null);
            distance.put(v, Integer.MAX_VALUE);
            found.put(v, false);
        }
        distance.put(new WGraphVertex(ux, uy), 0);
        for(WGraphVertex v : vertices){
            minQ.add(v, distance.get(v));
        }
        while(!minQ.isEmpty()){
            WGraphVertex u = minQ.extractMin();
            found.put(u, true);
            ArrayList<WGraphVertex> v_prime = adjacencyMap.get(u);
            if(v_prime == null){
                continue;
            }
            for(WGraphVertex v : v_prime){
                if(!found.get(v)){
                    Pair<WGraphVertex, WGraphVertex> p = new Pair<>(u, v);
                    int new_dist = (weights.get(p) + distance.get(u));
                    int old_dist = distance.get(v);
                    if(old_dist > new_dist){
                        distance.put(v, new_dist);
                        parent.put(v, u);
                        minQ.decrementPriority(v, old_dist - new_dist);
                    }
                }
            }
        }

        int min_vx = S.get(0);
        int min_vy = S.get(1);
        int min_dist = distance.get(new WGraphVertex(min_vx, min_vy));
        for(int i = 2; i < S.size(); i+=2){
            int new_vx = S.get(i);
            int new_vy = S.get(i+1);
            int new_dist = distance.get(new WGraphVertex(new_vx, new_vy));
            if(new_dist < min_dist){
                min_dist = new_dist;
                min_vx = new_vx;
                min_vy = new_vy;
            }
        }
        if(min_vx == ux && min_vy == uy){
            ArrayList<Integer> output = new ArrayList<Integer>();
            output.add(ux);
            output.add(uy);
            return output;
        }
        if(parent.get(new WGraphVertex(min_vx, min_vy)) == null){
            return new ArrayList<Integer>();
        }
        ArrayList<WGraphVertex> path = new ArrayList<>();
        ArrayList<Integer> output = new ArrayList<>();
        WGraphVertex node = new WGraphVertex(min_vx, min_vy);
        path.add(node);
        while((node = parent.get(node)) != null){
            path.add(node);
        }
        if(!path.get(path.size() - 1).equals(new WGraphVertex(ux, uy))){
            return new ArrayList<Integer>();
        }
        for(int i = path.size() - 1; i >= 0; i--){
            output.add(path.get(i).getX());
            output.add(path.get(i).getY());
        }
        //turn vertex list into even length int list as described in post-conditions
        return output;
    }

    // pre:  S1 and S2 represent sets of vertices (see above for
    //       the representation of a set of vertices as arrayList)
    // post: same structure as the last method’s post.
    public ArrayList<Integer> S2S(ArrayList<Integer> S1, ArrayList<Integer> S2){
        for(WGraphVertex v : vertices){
            parent.put(v, null);
            distance.put(v, Integer.MAX_VALUE);
            found.put(v, false);
        }
        int ux = -1;
        int uy = -1;
        WGraphVertex fake = new WGraphVertex(ux, uy);
        ArrayList<WGraphVertex> children = new ArrayList<>();
        for(int i = 0; i < S1.size(); i+=2){
            int temp_x = S1.get(i);
            int temp_y = S1.get(i+1);
            children.add(new WGraphVertex(temp_x, temp_y));
            weights.put(new Pair<>(fake, new WGraphVertex(temp_x, temp_y)), 0);
        }
        adjacencyMap.put(fake, children);

        ///////////////////DIJKSTRA'S ALGO/////////////////////////////////////
        distance.put(fake, 0);
        found.put(fake, false);
        parent.put(fake, null);
        minQ.add(fake, 0);
        for(WGraphVertex v : vertices){
            minQ.add(v, distance.get(v)); //generate minimum priority queue
        }
        while(!minQ.isEmpty()){
            WGraphVertex u = minQ.extractMin();
            found.put(u, true);
            ArrayList<WGraphVertex> v_prime = adjacencyMap.get(u);
            if(v_prime == null){
                continue;
            }
            for(WGraphVertex v : v_prime){
                if(!found.get(v)){
                    Pair<WGraphVertex, WGraphVertex> p = new Pair<>(u, v);
                    int new_dist = (weights.get(p) + distance.get(u));
                    int old_dist = distance.get(v);
                    if(old_dist > new_dist){
                        distance.put(v, new_dist);
                        parent.put(v, u);
                        minQ.decrementPriority(v, old_dist - new_dist);
                    }
                }
            }
        }

        int min_vx = S2.get(0);
        int min_vy = S2.get(1);
        int min_dist = distance.get(new WGraphVertex(min_vx, min_vy));
        for(int i = 2; i < S2.size(); i+=2){
            int new_vx = S2.get(i);
            int new_vy = S2.get(i+1);
            int new_dist = distance.get(new WGraphVertex(new_vx, new_vy));
            if(new_dist < min_dist){
                min_dist = new_dist;
                min_vx = new_vx;
                min_vy = new_vy;
            }
        }
        if(min_vx == ux && min_vy == uy){
            ArrayList<Integer> output = new ArrayList<Integer>();
            output.add(ux);
            output.add(uy);
            return output;
        }
        if(parent.get(new WGraphVertex(min_vx, min_vy)) == null){
            return new ArrayList<Integer>();
        }
        ArrayList<WGraphVertex> path = new ArrayList<>();
        ArrayList<Integer> output = new ArrayList<>();
        WGraphVertex node = new WGraphVertex(min_vx, min_vy);
        path.add(node);
        while((node = parent.get(node)) != null){
            path.add(node);
        }
        ArrayList<WGraphVertex> s1_wg = new ArrayList<>();
        for(int i = 0; i < S1.size(); i+=2){
            int temp_x = S1.get(i);
            int temp_y = S1.get(i+1);
            s1_wg.add(new WGraphVertex(temp_x, temp_y));
        }
        if(!s1_wg.contains(path.get(path.size() - 2))){
            return new ArrayList<Integer>();
        }
        for(int i = path.size() - (1 + 1); i >= 0; i--){ //adds the path, not including the fake vertex
            output.add(path.get(i).getX());
            output.add(path.get(i).getY());
        }
        //turn vertex list into even length int list as described in post-conditions
        return output;
    }
}