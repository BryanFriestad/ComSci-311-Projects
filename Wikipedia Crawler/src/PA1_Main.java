public class PA1_Main {

    public static void main(String[] args){
        String seed = "/wiki/A.html";
        String[] topics = {};
        WikiCrawler crawler = new WikiCrawler(seed, 6, topics, "output.txt");
        crawler.crawl(false);
    }
}
