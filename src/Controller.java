
import edu.uci.ics.crawler4j.crawler.*;
import edu.uci.ics.crawler4j.fetcher.*;
import edu.uci.ics.crawler4j.robotstxt.*;
public class Controller {
    public static void main(String[] args) throws Exception {
            String crawlStorageFolder = "/home/richard/Documents/crawl_results";
            int numberOfCrawlers = 1;
            //BasicConfigurator.configure();
            CrawlConfig config = new CrawlConfig();
            config.setCrawlStorageFolder(crawlStorageFolder);
            config.setMaxDepthOfCrawling(3);
            config.setPolitenessDelay(30000);//default 200

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
            controller.addSeed("http://www.foxbusiness.com/index.html");
            

            /*
             * Start the crawl. This is a blocking operation, meaning that your code
             * will reach the line after this only when crawling is finished.
             */
            controller.start(NewsCrawler.class, numberOfCrawlers);    
    }
}