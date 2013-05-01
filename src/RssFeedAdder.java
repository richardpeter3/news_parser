import java.sql.SQLException;

import edu.uci.ics.crawler4j.url.WebURL;


public class RssFeedAdder {
	private static String parentHomepageURL= "http://money.cnn.com/magazines/fortune/";
	private static String rssURL = "http://rss.cnn.com/rss/magazines_fortune.rss";
	private static String sourceName ="Fortune Magazine News";
	private static String description= "News from Fortune Magazine";
	private static int sourceTypeID = 1;  	//1;"Global News Corp.";"Published or funded by an International news corporation"
									//2;"National News Corp.";"Published or funded by a National news source"
									//3;"Local News";"Published or funded by a local news source"
									//4;"Independent author";"Published by an independent individual"
									//5;"Independent org.";"Published or funded by an independent organisation"
									//6;"Government";"Published or funded by a government organisation or body"
									//7;"UNKNOWN";"Published or funded by an unclassified source"
								
	
	
	
	
	//adds an rss feed and parent domain
	public static void main(String[] args){
		SpiderDBA dba = new SpiderDBA();
		try{
			dba.connect();
		}catch(SQLException e){
			System.out.println("Unnable to connect to database");
			e.printStackTrace();
			return ;
		} catch (ClassNotFoundException e) {
			System.out.println("Unnable to connect to database");
			e.printStackTrace();
			return ;
		}
		WebURL feedURL = new WebURL();
		feedURL.setURL(parentHomepageURL);
		int homeURLID = dba.getURLIDCreateIfNecessary(parentHomepageURL);
		if(homeURLID<=0)
			return ;
		
		
	 String query = "INSERT INTO rssfeeds (rssurl, sourcename, description, homeurlmediaid, sourcetypeid)" +
	 		"VALUES('"+rssURL+"','"+sourceName+"','"+description+"',"+homeURLID+","+sourceTypeID+")";
	 try{
	 dba.execute(query);
	 }catch(Exception e){
		 System.out.println("Error inserting rssfeed into database:");
		 e.printStackTrace();
		 return ;
	 }
	 System.out.println("Insertion completed successfully for rss url: \n" + rssURL);
		return ;
	}
	
	
}
