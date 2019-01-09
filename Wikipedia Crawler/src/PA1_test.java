import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PA1_test {

    public static void main(String[] args){
        String BASE_URL = "https://en.wikipedia.org";
        URL url = null;
        try {
            url = new URL(BASE_URL+"/wiki/Johnston_High_School");
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

        }
        //Finds all links from html document text==========================================================
        ArrayList<String> links = new ArrayList<String>();
        boolean p_found = false;
        for(int i = 0; i < document.length(); i++){
            if(!p_found && document.charAt(i) == '<' && document.charAt(i + 1) == 'p'){
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
        System.out.println(links.size());
        for(String s : links){
            System.out.println(s);
        }
        //=================================================================================================
        //System.out.println(document);
    }
}
