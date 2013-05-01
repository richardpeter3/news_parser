
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

import nu.xom.*;


public class RssDBA extends SpiderDBA {
	private static String XML_SAVE_PATH = "/home/richard/Documents/crawl_results/XMLs/";
	//TO-DO Enable saving of the change in xml locally
	
	public void getRssArticles(Vector<String> articles, Hashtable<String, String> pageSource, Vector<String> goodDomains){
		String query = "SELECT rssurl FROM rssfeeds";
		String xmlPath;
		ResultSet tmp = null;
		try{
			tmp = st2.executeQuery(query);
			tmp.next();
		}catch(Exception e){
			System.out.println("Unnable to obtain RSS Locations from DB. "+e);
			return;
		}
		do{
			try{
				xmlPath = tmp.getString("rssurl");
				
				this.getArticlesFromXML(xmlPath, articles, pageSource, goodDomains);
				if(tmp.isLast()==true)
					return;
				tmp.next();
			}catch(Exception e){
				System.out.println("Error Retrieveing articles from RSS feeds. ");
				e.printStackTrace();
				return;
			}
			
		}while(true);
	}
	
			
			
		
		
		
	

	private void getArticlesFromXML(String xmlPath, Vector<String> articles, Hashtable<String, String> pageSource, Vector<String> goodDomains) {
		//Method to add all links in the xml located at xmlPath to articles.
		try{
			//fill tree with the XMLs data
			Builder bob = new Builder();
			Document doc = bob.build( xmlPath);
			Element rss = doc.getRootElement();
			
			Element channel = rss.getFirstChildElement("channel");
			
			//get all items and loop throgh them
			Elements items =  channel.getChildElements("item");
			for(int i=0;i<items.size();i++){
				Element item = items.get(i);
				Element link = item.getFirstChildElement("link");
				if((link.getChild(0) instanceof Text)){
					//check if it is already in the database
					int dbid = urlExistsInDB(link.getChild(0).getValue());
					if(dbid<=0){
						articles.add(link.getChild(0).getValue());
						pageSource.put(link.getChild(0).getValue(), xmlPath);
						WebURL wu = new WebURL();
						wu.setURL(link.getChild(0).getValue());
						goodDomains.add(wu.getDomain());
						wu=null;
						
					}
				}else{
					System.out.println("not a link or null");
				}
			}
			
		}catch(Exception e){
			System.out.println("Error building xml document:\n"+e);
			return;
		}
	}








	public void getDateAndTitleFromXml(StringBuilder dt, StringBuilder title, String url, String rssUrl) {
		//method to retrieve the date and title for a specific article:
		
		try {  
			   Thread.sleep(1000);  
		} catch (InterruptedException ie) {  
		}  
		try{
			
			//fill tree with the XMLs data
			Builder bob = new Builder();
			Document doc = bob.build(rssUrl);
			Element rss = doc.getRootElement();
			
			Element channel = rss.getFirstChildElement("channel");
			
			//get all items and loop throgh them
			Elements items =  channel.getChildElements("item");
			for(int i=0;i<items.size();i++){
				Element item = items.get(i);
				
				Element link = item.getFirstChildElement("link");
				if((link.getChild(0) instanceof Text)){
					if(link.getChild(0).getValue().equals(url)){
						//We have the right item:
						//Get the title
						Element ttle = item.getFirstChildElement("title");
						if(ttle.getChild(0) instanceof Text){
							title.delete(0, title.length());
					        title.append(ttle.getChild(0).getValue());
							
						}
						//Get the Publication date
						Element pubDate = item.getFirstChildElement("pubDate");
						if(pubDate.getChild(0) instanceof Text){
							dt.delete(0, dt.length());
							dt.append(pubDate.getChild(0).getValue());
						}
						return;
					}
					
				}else{
					System.out.println("not a link or null");
				}
			}
			
		}catch(Exception e){
			System.out.println("Error parsing xml document for web info:\n"+e);
			return;
		}
	}








	public void updateMediaStoreEntry(int databaseID, String date) throws SQLException {
		// method to insert the publication date into a specific entry
		String query = "UPDATE mediastore SET datepublished = '"+date+"' WHERE mediaid = "+databaseID;
		this.execute(query);
	}

}
