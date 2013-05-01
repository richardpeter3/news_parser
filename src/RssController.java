
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import edu.uci.ics.crawler4j.crawler.*;
import edu.uci.ics.crawler4j.fetcher.*;
import edu.uci.ics.crawler4j.robotstxt.*;
public class RssController {
	public static Hashtable<String, String> pageSource;
	public static Vector<String> goodDomains;
	
    public static void main(String[] args) throws Exception {
    	pageSource = new Hashtable<String, String>();
    	goodDomains = new Vector<String>();
            String crawlStorageFolder = "/home/richard/Documents/crawl_results";
            int numberOfCrawlers = 5;
            //BasicConfigurator.configure();
            CrawlConfig config = new CrawlConfig();
            config.setCrawlStorageFolder(crawlStorageFolder);
            config.setMaxDepthOfCrawling(1);
            config.setPolitenessDelay(5000);//default 200

            /*
             * Instantiate the controller for this crawl.
             */
            PageFetcher pageFetcher = new PageFetcher(config);
            RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
            RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
            CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

            /*
             * For each crawl, you need to add some seed urls. These are the first
             * URLs that are fetched and then the crawler starts following links
             * which are found in these pages
             */
            Vector<String> articles = new Vector<String>();
            RssDBA dba = new RssDBA();
            dba.connect();
            dba.getRssArticles(articles, pageSource, goodDomains);
            controller.addSeed("http://www.infinitybeckon.com/FakeNews/bbc1.html");
            for (Iterator<String> it = articles.iterator();it.hasNext();){
           	 controller.addSeed((String)it.next());
            }
            

            /*
             * Start the crawl. This is a blocking operation, meaning that your code
             * will reach the line after this only when crawling is finished.
             */
            controller.start(RssCrawler.class, numberOfCrawlers); 
            System.out.println("RSS Update Complete");
    }
}
