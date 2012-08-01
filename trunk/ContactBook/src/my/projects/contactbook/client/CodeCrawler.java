package my.projects.contactbook.client;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;


import my.projects.contactbook.shared.model.City;
import my.projects.contactbook.shared.model.Country;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CodeCrawler implements Runnable{
		final int SEARCH_LIMIT=10;
		Thread thread;
		Vector searchingUrls;
		Vector searchedUrls;
		
		String startingUrl;
		public static final String DISALLOW = "Disallow:";
		Connection conn;
		public CodeCrawler() throws ClassNotFoundException, SQLException{
			Class.forName("org.postgresql.Driver");
	    String dbURL = "jdbc:postgresql://localhost:5432/mydb";
	    String user = "postgres";
	    String pass = "admin";
	    //estabilish connection to database
	    conn = DriverManager.getConnection(dbURL,user,pass);
	    
			searchedUrls=new Vector();
			searchingUrls=new Vector();
			this.startingUrl="http://www.navigator.az/pcodes";
		}

		public void stop(){
			if(searchingUrls!=null)
				System.out.println("Stopping...");
			searchingUrls=null;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			try {
				runSomeThread(thread, startingUrl);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		public boolean exists(String URLName){
		    try {
		      HttpURLConnection.setFollowRedirects(false);
		      // note : you may also need
		      //        HttpURLConnection.setInstanceFollowRedirects(false)
		      HttpURLConnection con =
		         (HttpURLConnection) new URL(URLName).openConnection();
		      con.setRequestMethod("HEAD");
		      return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		    }
		    catch (Exception e) {
		       e.printStackTrace();
		       return false;
		    }
		  }
		
		public void runSomeThread(Thread th,String url1) throws Exception{
			int numberSearched = 0;
			int numberFound = 0;
			
			if(url1.length()==0){
				System.out.println("Enter correct url address");
				return;
			}
			
			searchedUrls.removeAllElements();
			searchingUrls.removeAllElements();
			
			searchingUrls.addElement(url1);
			
			while((searchingUrls.size()>0)){
				url1 = (String) searchingUrls.elementAt(0);
				
				URL url;
			    try { 
				url = new URL(url1);
				} catch (MalformedURLException e) {
				System.out.println("ERROR: invalid URL " + url1);
				break;
			    }
				System.out.println("sear " + searchingUrls.get(0));
			    // mark the URL as searched (we want this one way or the other)
			    searchingUrls.removeElementAt(0);
			    searchedUrls.addElement(url1);
			    
			    if (!url.getProtocol().equals("http") ) 
					break;
			   
			   
			    Document doc=Jsoup.connect(url1).timeout(999999).get();
			    Elements els=doc.select("a[href^=/pcodes/list]");
	
			    for(Element e:els)
			    	if(e.parent().attr("class").equals("td_commontext")){
			    	if(e.attr("href").matches(".*?list/\\d+$")){
			    		 System.out.println(e.attr("href").substring(e.attr("href").indexOf("list/")+5));
			    		String countryName=e.parent().text();
			    		int countryCode=Integer.parseInt(e.parent().nextElementSibling().text().substring(1));
			    	System.out.println("Country: "+countryName+" "+countryCode);
			    	Country country=new Country();
			    	country.setName(countryName);
			    	country.setCode(countryCode);
			        //if(!existCountry(country))
			    	//insertCountry(country);
			      
			    }
			    	if(e.attr("href").matches(".*?show/\\d+$")){
			    		 System.out.println(url1.substring(url1.indexOf("list/")+5));
				    		
			    		String cityName=e.parent().text();
			    		int countryCode=Integer.parseInt(e.parent().nextElementSibling().text().substring(1));
			    		String cityCode =null;
			    		
			    		if(!e.parent().nextElementSibling().nextElementSibling().text().isEmpty()){
			    			if(e.parent().nextElementSibling().nextElementSibling().text().contains(",")){
			    				String[] cityCodes=e.parent().nextElementSibling().nextElementSibling().text().split(",");	
				    			for(int i=0;i<cityCodes.length;i++){
				    				cityCode=cityCodes[i].trim();
				    				
							    	System.out.println("City: "+cityName+" "+countryCode+" "+cityCode);
							    	City city=new City();
							    	city.setName(cityName);
							    	city.setCode(cityCode);
							    	Country country=new Country();
							    	country.setCode(countryCode);
							    	city.setCountry(country);
						//	    	if(!existCity(city) && cityCode!=null)
							//    	insertCity(city);
							    	
				    				
				    			}
			    			
			    		}
			    	
			    			else{
			    				cityCode=e.parent().nextElementSibling().nextElementSibling().text();
			    	System.out.println("City: "+cityName+" "+countryCode+" "+cityCode);
			    	City city=new City();
			    	city.setName(cityName);
			    	city.setCode(cityCode);
			    	Country country=new Country();
			    	country.setCode(countryCode);
			    	city.setCountry(country);
			    	if(!existCity(city) && cityCode!=null)
			    	insertCity(city);
			    			}
			    			}
			    }
			    	if(!e.attr("href").contains("show")){
			    	 String linkString=url.getProtocol()+"://"+url.getHost()+e.attr("href");
			    	 if(!searchedUrls.contains(linkString) && !searchingUrls.contains(linkString))
			    			searchingUrls.addElement(linkString);
			    	}
			    }
			}
		}
		
		public void start(){
		 if (thread == null) {
				thread = new Thread(this);
			    }
			    thread.start();
				   
		}
		
		public boolean existCountry(Country country) throws ClassNotFoundException, SQLException {
			Statement st = conn.createStatement();
	        String query="SELECT * FROM countries WHERE name='"+country.getName()+"' AND code="+country.getCode();
	        ResultSet rs=st.executeQuery(query);
	        int c=0;
	        while(rs.next())
	        	c++;
	        if(c>0)
	        	return true;
	        return false;
		}
		
		public void insertCountry(Country country) throws ClassNotFoundException, SQLException {
			Statement st = conn.createStatement();
	        String query="INSERT INTO countries(name,code)" +
	        		" VALUES('"+country.getName()+"',"+country.getCode()+")";
	        st.execute(query);
	        st.close();
		}
		
		public boolean existCity(City country) throws ClassNotFoundException, SQLException {
			Statement st = conn.createStatement();
	        String query="SELECT * FROM cities WHERE name='"+country.getName()+"' AND code='"+country.getCode()+"'";
	        ResultSet rs=st.executeQuery(query);
	        int c=0;
	        while(rs.next())
	        	c++;
	        if(c>0)
	        	return true;
	        return false;
		}
		
		public void insertCity(City city) throws ClassNotFoundException, SQLException {
			Statement st = conn.createStatement();
	        String query="INSERT INTO cities(name,code,country_code)" +
	        		" VALUES('"+city.getName()+"','"+city.getCode()+"',"+city.getCountry().getCode()+")";
	        st.execute(query);
	        st.close();
		}
		
		public static void main(String []args) throws ClassNotFoundException, SQLException{
			CodeCrawler cc=new CodeCrawler();
			cc.start();
			
		}

}
