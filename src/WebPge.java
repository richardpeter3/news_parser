import nu.xom.*;
import java.io.*;

public class WebPge {
	//Create elements, test elements and an element attribute
			private Element article = new Element("article");
			private Element url = new Element("url");
			//private String urlString, parentUrlString, domainString, dateString;
			private Element databaseID	= new Element("databaseID");
			//private int dbID, parentdbID;
			private Element parent = new Element("parent");
			private Element parentUrl = new Element("parentUrl");
			private Element parentDatabaseID =  new Element("parentDatabaseID");
			private Element domain = new Element("domain");
			private Element dateRetrieved = new Element("dateRetrieved");
			private Element content = new Element("content");
			private Element metadata = new Element("metadata");
			private Element headder = new Element("headder");
			private Element headderLength = new Element("headderLength");
			private Element body = new Element("body");
			private Element bodyLength = new Element("bodyLength");
			private Element sentimentIndex	= new Element("sentimentIndex");
			private Element nOutgoing = new Element("nOutgoing");
			private boolean isInitialised = false;
			
			private Document doc;
		
	public WebPge(){
		//Constructor class
		//Create a new document with url as the root element
	
				
				//Add XML data to the root element
				article.appendChild(url);
				article.appendChild(databaseID);

				article.appendChild(domain);
				article.appendChild(dateRetrieved);

				article.appendChild(parent);
					parent.appendChild(parentDatabaseID);

					parent.appendChild(parentUrl);

				article.appendChild(content);
					content.appendChild(headder);

					content.appendChild(body);

				article.appendChild(metadata);
					metadata.appendChild(headderLength);
					metadata.appendChild(bodyLength);
					metadata.appendChild(nOutgoing);
					metadata.appendChild(sentimentIndex);	
					

				isInitialised=true;
	}
	
	//method to write the 
	public int writeToFile(String[] args){
		//check that everything is initialised
		if(!this.isInitialised)
			return -1;
		String filepath = "/home/richard/Documents/crawl_results/Webpages/";
		if (args.length >1){
			System.out.println("Please specify only the path for the results to be written");
			return -1;
		} else if(args.length==1){
			filepath = args[0];
		}
		//Everything seems to be in order so write the page to file:
		try{
			doc = new Document(article);
			String s = doc.getRootElement().getFirstChildElement("databaseID").getValue();
		FileOutputStream fos = new FileOutputStream(filepath+s+".xml");
		Serializer output = new Serializer(fos, "ISO-8859-1");
		output.setIndent(2);
		output.write(doc);
		}catch (Exception e){
			System.out.println("Error Writing to file: "+e + "\n "+ filepath);
			return -1;
		}
		
		
		return 1;
	}
	public void setMetadata(String headderLen, String bodyLen, String sentimentIdx, String numOutgoing, String dtRetrieved){
		headderLength.appendChild(headderLen);
		bodyLength.appendChild(bodyLen);
		sentimentIndex.appendChild(sentimentIdx);
		nOutgoing.appendChild(numOutgoing);
		dateRetrieved.appendChild(dtRetrieved);
	}
	
	public void setParentData(String parentURL, String parentDBID){
		parentUrl.appendChild(parentURL);
		parentDatabaseID.appendChild(parentDBID);
	}
	public void setURLData(String head, String bod, String URL, String dbid, String dom){
		databaseID.appendChild(dbid);
		headder.appendChild(head);
		body.appendChild(bod);
		url.appendChild(URL);
		domain.appendChild(dom);
	}
	
	public void setUrl(String urlString){
		url.appendChild(urlString);	
	}
	public void setDatabaseID(int i){
		databaseID.appendChild(String.valueOf(i));	
	}
	public void setParentUrl(String urlString){
		parentUrl.appendChild(urlString);	
	}
	public void setParentdbID(int i){
		parentDatabaseID.appendChild(String.valueOf(i));	
	}
	public void setBody(String string, int len){
		body.appendChild(string);
		bodyLength.appendChild(String.valueOf(len));
	}
	public void setHeadder(String string, int len){
		headder.appendChild(string);
		headderLength.appendChild(String.valueOf(len));
	}
	public void setDomain(String dom){
		domain.appendChild(dom);
	}
	public void setDateRetrieved(String dt){
		dateRetrieved.appendChild(dt);
	}
	public void setSentimentIndex(double idx){
		sentimentIndex.appendChild(String.valueOf(idx));
	}
	public void setOutgoing(int n){
		nOutgoing.appendChild(String.valueOf(n));
	}
}
