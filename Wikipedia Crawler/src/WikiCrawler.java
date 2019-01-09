import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bryanf
 */
public class WikiCrawler {
    public static final String BASE_URL = "https://en.wikipedia.org";
    //public static final String BASE_URL = "http://web.cs.iastate.edu/~pavan";
    String seed;
    int max;
    String[] topics;
    String output;

    public WikiCrawler(String seed, int max, String[] topics, String output){
        this.seed = seed;
        this.max = max;
        this.topics = topics;
        this.output = output;
    }

    private ArrayList<String> extractLinks(String document){
        ArrayList<String> links = new ArrayList<String>();
        boolean p_found = false;
        for(int i = 0; i < document.length(); i++){
            if(!p_found && document.charAt(i) == '<' && document.charAt(i + 1) == 'p' && document.charAt(i + 2) == '>'){
                p_found = true;
            }
            if(p_found) {
                if (document.charAt(i) == '<' && document.charAt(i + 1) == 'a' && document.charAt(i + 2) == ' ') {
                    int j = i;
                    String link = "";
                    while (document.charAt(j) != '>') {
                        link += document.charAt(j);
                        j++;
                    }
                    links.add(link);
                }
            }
        }
        for(int i = 0; i < links.size(); i++){
            if(links.get(i).startsWith("<a href=\"")){
                links.set(i, links.get(i).substring(9, links.get(i).indexOf('\"', 9)));
                if(links.get(i).contains("#")){
                    links.remove(i);
                    i--;
                }
                else if(links.get(i).contains(":")){
                    links.remove(i);
                    i--;
                }
                else if(!links.get(i).startsWith("/wiki/")){
                    links.remove(i);
                    i--;
                }
            }
            else{
                links.remove(i);
                i--;
            }
        }
        return links;
    }

    /**
     * (a) if focused is false then explore in a BFS fashion
     * (b) if focused is true then for every page a, compute the Relevance(T,a), and during
     * exploration, instead of adding the pages in the FIFO queue,
     * • add the pages and their corresponding relevance (to topic) to priority queue. The priority of a page is its relevance;
     * • extract elements from the queue using extractMax.
     *
     * After the crawl is done, the edges explored in the crawl method should be written to the
     * output file.
     *
     * @param focused
     */
    public void crawl(boolean focused){
        ArrayList<String> edge_list = new ArrayList<String>();
        edge_list.add(Integer.toString(this.max));
        if(focused){
            PriorityQ q = new PriorityQ();
            q.add(this.seed, getRelevance(getPageHTML(this.seed)));
            ArrayList<String> discovered = new ArrayList<String>();
            discovered.add(this.seed);
            while(!q.isEmpty()){
                String v = q.extractMax();
                ArrayList<String> links = extractLinks(getPageHTML(v));
                for(String v_prime : links){
                    //discovered cant be full && v_prime is already seen
                    if(!discovered.contains(v_prime)  && (discovered.size() + 1 <= this.max)){
                        int relevance = 0;
                        //topics must be empty || v_prime has at least one reference to each topic word
                        if((topics.length == 0) || (relevance = getRelevance(getPageHTML(v_prime))) > 0){
                            q.add(v_prime, relevance);
                            discovered.add(v_prime);
                            String t = v + " " + v_prime;
                            if (!edge_list.contains(t)) {
                                edge_list.add(t);
                            }
                        }
                    }
                    else if(discovered.contains(v_prime) && !(v.equals(v_prime))){
                        String t = v + " " + v_prime;
                        if(!edge_list.contains(t)){
                            edge_list.add(t);
                        }
                    }
                }
            }
        }
        else{
            /**
             * 1. Input: Directed Graph G = (V, E)
             *    Root of exploration: root
             * 2. Initilize a FIFO queue Q and a list Discovered
             * 3. Add root to Q and Discovered
             * 4. while Q is not empty do
             * 5.   Extract vertex v from the head of the Q
             * 6.   For every (v, v’) in E do
             * 7.       if v’ is not in Discovered then
             * 8.           add v’ to Q and Discovered
             */
            FifoQueue fifoQ = new FifoQueue(this.seed);
            ArrayList<String> discovered = new ArrayList<String>();
            discovered.add(this.seed);
            while(!fifoQ.isEmpty()){
                String v = fifoQ.pop();
                ArrayList<String> links = extractLinks(getPageHTML(v));
                for(String v_prime : links){
                    //discovered cant be full && v_prime is already seen
                    if((discovered.size() + 1 <= this.max) && (!discovered.contains(v_prime))){
                        int relevance;
                        //topics must be empty || v_prime has at least one reference to each topic word
                        if((topics.length == 0) || (relevance = getRelevance(getPageHTML(v_prime))) > 0){
                            fifoQ.push(v_prime);
                            discovered.add(v_prime);
                            String t = v + " " + v_prime;
                            if (!edge_list.contains(t)) {
                                edge_list.add(t);
                            }
                        }
                    }
                    else if(discovered.contains(v_prime) && !(v.equals(v_prime))){
                        String t = v + " " + v_prime;
                        if(!edge_list.contains(t)){
                            edge_list.add(t);
                        }
                    }
                }
            }
        }
        System.out.println("Number of edges: " + edge_list.size());
        writeToFile(edge_list);
    }

    private String getPageHTML(String page){
        URL url = null;
        try {
            url = new URL(BASE_URL + page);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStream is = null;
        try {
            is = url.openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String document = "";
        String line;
        try{
            while((line = br.readLine()) != null){
                document += line;
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

        //BE POLITE
        try {
            Thread.sleep((3 * 1000) / 20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return document;
    }

    private int getRelevance(String document){
        document = document.substring(document.indexOf("<p>") + 3);
        int sum = 0;
        for(int i = 0; i < topics.length; i++){
            int count = getCountOfTopic(document, topics[i]);
            if(count == 0){
                return 0;
            }
            sum += count;
        }
        return sum;
    }

    private int getCountOfTopic(String document, String topic){
        int start = 0;
        int count = 0;
        while(document.indexOf(topic, start) != -1){
            start = document.indexOf(topic, start) + topic.length();
            count++;
        }
        return count;
    }

    private void writeToFile(List<String> edge_list){
        Path file = Paths.get(this.output);
        try {
            Files.write(file, edge_list, Charset.forName("UTF-8"));
        } catch (IOException e) {
            System.out.println("Error when writing to external file: " + this.output);
            e.printStackTrace();
        }
    }
}
