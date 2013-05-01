//Extends the Database Assistant with specific methods for the spider:

import java.sql.SQLException;


import edu.uci.ics.crawler4j.url.WebURL;


public class SpiderDBA extends DatabaseAssistant {
	
	
	// returns int for the id of the url if it exists, otherwise 0, and finally -1 for an error.
	public int urlExistsInDB(String url) {
		String query = "Select mediaid from mediastore where (" +
				"url = '"+url+"' or url = '"+ url + "/')";
		try{
			this.executeQuery(query);
		}catch (Exception e){
			System.out.println("Error getting media id for url:\n"+url+"\n"+e);
			return -1;
		}
		int id = -1;
		 try{
			 if (rs.next())
				 id = this.getResults().getInt("mediaid");	
		 }catch(SQLException e){
			 System.out.println("Error getting media id for resultset of length 1\n"+e);
		 }
		 
		 return id;
		
	}


	//inserts an url into the database and returns the mediastore mediaid
	public int insertURL(String url, int nChildren, String parentURL, int length){
		int urlID = urlExistsInDB(url);
		int nParents = 0;
		int parentID = urlExistsInDB(parentURL);
		if(parentID>0 && parentURL != ""&& parentURL!=null){
			nParents = 1;
		}
		if ( urlID<=0){
			//it is not in the database presently so insert it:
			//get the domainID sourcetypeID Dateretrieved, url, mediatypeID, numchildern, nparents
			//insert the url
			//get the domainID , if necessary create a new entry:
			int domID = getDomainIDCreateIfNecessary(url);
			if(domID <=0){
				//Problem
				System.out.println("Error creating and retrieving new domain ID");
				return -1;
		
			}
			nParents = urlExistsInDB(parentURL);
			int sourceTypeID = getSourceTypeID(url);
			int mediaTypeID = getMediaTypeID(url);
			
			String query =  "INSERT INTO  mediastore (domainid, sourcetypeid, mediatypeid, dateretrieved, numchildren, numparents, length, url) " +
					"VALUES ("+domID+", "+sourceTypeID+","+ mediaTypeID+", NOW()," +nChildren+", 0 ,"+length+", '"+url+"')";
			
			try{
				this.execute(query);
			}catch (SQLException e){
				System.out.println("Error inserting into mediastore for url:\n"+url+"\n"+e);
			}
			
			//return its new id
			return urlExistsInDB(url);
		}	
		if(parentID>0){
			//update the parentchildtable
			nParents = updateParentChild(urlID, parentID);
			String query = "UPDATE mediastore SET  numparents = "+ nParents + " WHERE mediaid =" + urlID;
			try{
				this.execute(query);
			}catch(SQLException e){
				System.out.println("Error updating mediastore with numparents for urlID: "+urlID+"\n"+e);
			}
		}
		return urlID;
	}
	

	public int getDomainIDCreateIfNecessary(String url) {
		int domID = getDomainID(url);
		if(domID <=0){
			if(createNewDomainEntry(url)){
				domID =  getDomainID(url);
				return domID;
			}else{
				//Problem
				System.out.println("Error creating and retrieving new domain ID");
				return -1;
			}
		}
		return domID;
	}


	protected int updateParentChild(int urlID, int parentID) {
		//Method to update the database table parentChild, returns
		//the integer number of parents
		String query = "INSERT INTO parentchild (parentmediaid, childmediaid)" +
				"VALUES ("+parentID+","+urlID+")";
		try{
			this.execute(query);
		}catch(SQLException e){
			System.out.println("Failed to update parentChild table for parent "+parentID+" and child "+urlID+".");
			statementExecutedCorrectly = false;
			return -1;
			//flag that this failed and should be recalculated
		}
		query = "SELECT * from parentchild WHERE childmediaID = "+urlID;
		try{
			this.executeQuery(query);
			return rs.getFetchSize();
		}catch(SQLException e){
			System.out.println("Failed to determine number of parents for childID "+ urlID+"\n"+e);
			return -1;
		}
	}

	protected boolean createNewDomainEntry(String url){
		String query = "INSERT INTO domains (domainname, description)" +
				"VALUES ('" + getDomainName(url) +"', 'Empty')";
		try{
		this.execute(query);
		return true;
		}catch (SQLException e){
			System.out.println("Failed to insert "+ getDomainName(url)+ " into database: "+e);
			return false;
		}
	}
	
	protected String getDomainName(String url){
		WebURL wu = new WebURL();
		wu.setURL(url);
		return wu.getDomain();
	}
	
	protected int getSourceTypeID(String url){
		//default type is UNKNOWN (7);
		return 7;
	}
	
	protected int getMediaTypeID(String url){
		if(url.contains("twitter"))
			return 3;
		if(url.contains("facebook.com"))
			return 2;
		if(url.contains("blog"))
			return 4;
		else return 1;
	}
	
	protected int getDomainID(String url){
		String query = "SELECT domainid FROM domains WHERE domainname = '"+getDomainName(url)+"'";
		try {
			this.executeQuery(query);
			if(rs.next())
				return rs.getInt("domainid");//return the id
			return 0;///its not in the database
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to select domainid for: "+getDomainName(url));
			e.printStackTrace();
			return -1; //Error
		}
		
	}


	public int getURLIDCreateIfNecessary(String url) {
		// Checks whether an url is in the database and inserts it if not
		//returns the mediaID of the inserted url.
		int mediaID = this.urlExistsInDB(url);
		if(mediaID >0)
			return mediaID;
		// The url is not in the database so insert it:
		return this.insertURL(url, 0, "", 0);
		
	}
}
