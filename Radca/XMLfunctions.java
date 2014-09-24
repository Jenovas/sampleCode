package radca.prawny;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class XMLfunctions {

	
	public final static Document XMLfromString(String xml){
		
		Document doc = null;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
        	
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			InputSource is = new InputSource(new StringReader(xml));   
	        is.setEncoding("UTF-8");
	        doc = db.parse(is); 
	        
		} catch (ParserConfigurationException e) {
			System.out.println("XML parse error: " + e.getMessage());
			return null;
		} catch (SAXException e) {
			System.out.println("Wrong XML file structure: " + e.getMessage());
            return null;
		} catch (IOException e) {
			System.out.println("I/O exeption: " + e.getMessage());
			return null;
		}
		       
        return doc;
        
	}
	
	/** Returns element value
	  * @param elem element (it is XML tag)
	  * @return Element value otherwise empty String
	  */
	 public final static String getElementValue( Node elem ) {
	     Node kid;
	     if( elem != null){
	         if (elem.hasChildNodes()){
	             for( kid = elem.getFirstChild(); kid != null; kid = kid.getNextSibling() ){
	                 //if( kid.getNodeType() == Node.TEXT_NODE  ){
	                     return kid.getNodeValue();
	                 //}
	             }
	         }
	     }
	     return "";
	 }
		 
	 public static String getXML(String type){	 
			String line = null;

			if (type == "aktualnosci")
			{
				try {
					
					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://");
	
					HttpResponse httpResponse = httpClient.execute(httpPost);
					HttpEntity httpEntity = httpResponse.getEntity();
					line = EntityUtils.toString(httpEntity, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
				} catch (MalformedURLException e) {
					line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
				} catch (IOException e) {
					line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
				}
			}
			else if (type == "aktual_diff")
			{
				try {
					
					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://");
	
					HttpResponse httpResponse = httpClient.execute(httpPost);
					HttpEntity httpEntity = httpResponse.getEntity();
					line = EntityUtils.toString(httpEntity, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
				} catch (MalformedURLException e) {
					line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
				} catch (IOException e) {
					line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
				}
			}
			else if (type == "moim_zdaniem")
			{
				try {
					
					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://");
	
					HttpResponse httpResponse = httpClient.execute(httpPost);
					HttpEntity httpEntity = httpResponse.getEntity();
					line = EntityUtils.toString(httpEntity, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
				} catch (MalformedURLException e) {
					line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
				} catch (IOException e) {
					line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
				}
			}
			else if (type == "moim_zdaniem_diff")
			{
				try {
					
					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://");
	
					HttpResponse httpResponse = httpClient.execute(httpPost);
					HttpEntity httpEntity = httpResponse.getEntity();
					line = EntityUtils.toString(httpEntity, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
				} catch (MalformedURLException e) {
					line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
				} catch (IOException e) {
					line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
				}
			}
			else if (type == "experts")
			{
				try {
					
					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://");
	
					HttpResponse httpResponse = httpClient.execute(httpPost);
					HttpEntity httpEntity = httpResponse.getEntity();
					line = EntityUtils.toString(httpEntity, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
				} catch (MalformedURLException e) {
					line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
				} catch (IOException e) {
					line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
				}
			}
			else if (type == "experts_diff")
			{
				try {
					
					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://");
	
					HttpResponse httpResponse = httpClient.execute(httpPost);
					HttpEntity httpEntity = httpResponse.getEntity();
					line = EntityUtils.toString(httpEntity, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
				} catch (MalformedURLException e) {
					line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
				} catch (IOException e) {
					line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
				}
			}
			else if (type == "szkolenia")
			{
				try {
					
					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://");
	
					HttpResponse httpResponse = httpClient.execute(httpPost);
					HttpEntity httpEntity = httpResponse.getEntity();
					line = EntityUtils.toString(httpEntity, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
				} catch (MalformedURLException e) {
					line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
				} catch (IOException e) {
					line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
				}
			}
			else if (type == "szkolenia_diff")
			{
				try {
					
					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://");
	
					HttpResponse httpResponse = httpClient.execute(httpPost);
					HttpEntity httpEntity = httpResponse.getEntity();
					line = EntityUtils.toString(httpEntity, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
				} catch (MalformedURLException e) {
					line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
				} catch (IOException e) {
					line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
				}
			}			
			return line;
	}
	 
	public static int numResults(Document doc){		
		Node article = doc.getDocumentElement();
		int res = 1;
		
		try{
			res = Integer.valueOf(article.getFirstChild().getFirstChild().getAttributes().getNamedItem("id").getNodeValue());
		}catch(Exception e ){
			res = -1;
		}
		return res;
	}

	public static String getValue(Element item, String str) {		
		NodeList n = item.getElementsByTagName(str);		
		return XMLfunctions.getElementValue(n.item(0));
	}
	
	public static String getMiniValue(Element item, String str) {		
		NodeList n = item.getElementsByTagName(str);			
		return n.item(0).getAttributes().getNamedItem("src").getNodeValue();
	}
	
	public static String getMiniWidthValue(Element item, String str) {		
		NodeList n = item.getElementsByTagName(str);			
		return n.item(0).getAttributes().getNamedItem("width").getNodeValue();
	}
	
	public static String getMiniHeightValue(Element item, String str) {		
		NodeList n = item.getElementsByTagName(str);			
		return n.item(0).getAttributes().getNamedItem("height").getNodeValue();
	}
	
	public static String addHTMLEncoding(String s) {	
		return s;
	}
}
