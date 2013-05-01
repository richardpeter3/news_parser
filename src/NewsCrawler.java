
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import java.util.regex.Pattern;
import edu.uci.ics.crawler4j.crawler.*;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.*;
public class NewsCrawler extends WebCrawler {

        private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" 
                                                          + "|png|tiff?|mid|mp2|mp3|mp4"
                                                          + "|wav|avi|mov|mpeg|ram|m4v|pdf" 
                                                          + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

        /**
         * You should implement this function to specify whether
         * the given url should be crawled or not (based on your
         * crawling logic).
         */
        @Override
        public boolean shouldVisit(WebURL url) {
                String href = url.getURL().toLowerCase();
                return !FILTERS.matcher(href).matches() && href.startsWith("http://www.foxbusiness.com/");
        }

        /**
         * This function is called when a page is fetched and ready 
         * to be processed by your program.
         */
        @Override
        public void visit(Page page) {          
        	try {  
 			   Thread.sleep(10000);  
	 		} catch (InterruptedException ie) {  
	 		} 
                String url = page.getWebURL().getURL();
                System.out.println("URL: " + url);

                if (page.getParseData() instanceof HtmlParseData) {
                        HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                        String text = htmlParseData.getText();
                        String html = htmlParseData.getHtml();
                        List<WebURL> links = htmlParseData.getOutgoingUrls();
                        
                        //create assistant to make this easier...
                        SpiderDBA dba = new SpiderDBA();
                        int databaseID = 1; //default reference to dummy entry
                        
                        try{
                        //CONNECT
                        dba.connect();
                        if(dba.isConnected()){
                        //GET ID
                        	databaseID = dba.insertURL(page.getWebURL().getURL(), links.size(), page.getWebURL().getParentUrl(), text.length());
                        }
                        }catch(Exception e){
                        	System.out.println("Unnable to insert URL: \n"+url+"\n"+e);
                        }
                        
						
                        //initialise the web page document.
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			            Date date = new Date();
			            
                        WebPge wp = new WebPge();
                        wp.setParentData(page.getWebURL().getParentUrl(), String.valueOf(dba.urlExistsInDB(page.getWebURL().getParentUrl())));
                        wp.setURLData(text, "", page.getWebURL().getURL(),String.valueOf(databaseID), page.getWebURL().getDomain());
                        wp.setMetadata(String.valueOf(0), String.valueOf(text.length()), "0.0", String.valueOf(links.size()),dateFormat.format(date));
                        
                        String fpath[] = new String[1];
                        fpath[0] = "/home/richard/Documents/crawl_results/Webpages/";
                        wp.writeToFile(fpath);
                        wp=null;

                        
                        System.out.println("Text length: " + text.length());
                        System.out.println("Html length: " + html.length());
                        System.out.println("Number of outgoing links: " + links.size());
                        try{
                            //DISCONECT
                            if(dba.isConnected())
                           		dba.close();
                        }catch(Exception e){
                            System.out.println("Error closing Connection:\n" +e);
                        }
                        
                }
        }
}