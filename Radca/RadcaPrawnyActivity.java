package radca.prawny;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import radca.prawny.AktualnosciActivity.ReturnList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;


public class RadcaPrawnyActivity extends TabActivity {

	private ProgressDialog dialog = null;
	private boolean isThisTablet;
	static int infos = 10;
	static int on = 0;
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	this.finish();
	    	System.exit(0);

	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main); 
   
	    isThisTablet = getResources().getBoolean(R.bool.CheckIfTablet);
		if (isThisTablet == false)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
	    // Check If XML Files Exist
	    int Check = CheckFiles();
	    if (Check == 0&& on == 0)
	    {
	    	  int connected = CheckInternet();
			  
			  if (connected == 0)
			  {
				  AlertDialog.Builder dialogBuilder
				  = new AlertDialog.Builder(this)
				  .setTitle("ALERT")
				  .setCancelable(false)
				  .setMessage("Ta Aplikacja wymaga aktywnego po�aczenia internetowego przy pierwszym uruchomieniu")
				  .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) 
						{
							// Download new Database
							finish();
				            System.exit(0);
						}
						});
				  dialogBuilder.create().show();  
				  
			  }
			  else
				  new TheDownload().execute();
	    }
	    else
	    {
	    	  int connected = CheckInternet();
			  
			  // Ask If User wants to UPDATE Database
			  if (connected == 1 && on ==0)
			  {
				  //Update();
				  AlertDialog.Builder dialogBuilder
				  = new AlertDialog.Builder(this)
				  .setTitle("AKTUALIZACJA")
				  .setMessage("Czy chcesz zaktualizowa� baz�?")
				  .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) 
						{
							// Download new Database
							new TheUpdate().execute();
						}
						})
				  .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) 
						{

						}
						});
				  dialogBuilder.create().show(); 
			  }
				  
	    }
	    // Check Files
	    // if no files check for internet and download them
	    // if files exist ask if user wants to search for updates
	    // if yes download
	    // if no internet and no files write alert and close aplication
	    // else show menu
	    
	    on=1;
	}
	
	public int CheckFiles()
	{
		//Get the XML File
	  	String xml;
	    Document doc;
	    File file;
	  		
	    // check if XML File exists on Android
	 	file = getBaseContext().getFileStreamPath("Aktualnosci_XML");
	 	if(!file.exists())
	 	{	
	 		return 0;
	 	}
	 	
	 	file = getBaseContext().getFileStreamPath("Szkolenia_XML");
	 	if(!file.exists())
	 	{	
	 		return 0;
	 	}
	 	
	 	file = getBaseContext().getFileStreamPath("Moim_Zdaniem_XML");
	 	if(!file.exists())
	 	{	
	 		return 0;
	 	}
	 	
	 	file = getBaseContext().getFileStreamPath("Experts_XML");
	 	if(!file.exists())
	 	{	
	 		return 0;
	 	}
	 	
	 	return 1;
	}
	
	public void Download()
	{
		//Get the XML File
	  	String xml;
	    Document doc;
	    File file;
	    
	    // check if XML Files exists on Android
	 	file = getBaseContext().getFileStreamPath("Aktualnosci_XML");
	 	file.delete();
	 	if(!file.exists())
	 	{	
	 		xml = XMLfunctions.getXML("aktualnosci");
		    String XML_Fix = XMLfunctions.addHTMLEncoding(xml);
			doc = XMLfunctions.XMLfromString(XML_Fix);
			FileOutputStream fos; 
			
			try {
				fos = openFileOutput("Aktualnosci_XML", Context.MODE_PRIVATE);
				fos.write(XML_Fix.getBytes());
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 	}
	 	
	 	file = getBaseContext().getFileStreamPath("Szkolenia_XML");
	 	file.delete();
	 	if(!file.exists())
	 	{	
	 		xml = XMLfunctions.getXML("szkolenia");
		    String XML_Fix = XMLfunctions.addHTMLEncoding(xml);
			doc = XMLfunctions.XMLfromString(XML_Fix);
			FileOutputStream fos; 
			
			try {
				fos = openFileOutput("Szkolenia_XML", Context.MODE_PRIVATE);
				fos.write(XML_Fix.getBytes());
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 	}
	 	
	 	file = getBaseContext().getFileStreamPath("Moim_Zdaniem_XML");
	 	file.delete();
	 	if(!file.exists())
	 	{	
	 		xml = XMLfunctions.getXML("moim_zdaniem");
		    String XML_Fix = XMLfunctions.addHTMLEncoding(xml);
			doc = XMLfunctions.XMLfromString(XML_Fix);
			FileOutputStream fos; 
			
			try {
				fos = openFileOutput("Moim_Zdaniem_XML", Context.MODE_PRIVATE);
				fos.write(XML_Fix.getBytes());
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 	}
	 	
	 	file = getBaseContext().getFileStreamPath("Experts_XML");
	 	file.delete();
	 	if(!file.exists())
	 	{	
	 		xml = XMLfunctions.getXML("experts");
		    String XML_Fix = XMLfunctions.addHTMLEncoding(xml);
			doc = XMLfunctions.XMLfromString(XML_Fix);
			FileOutputStream fos; 
			
			try {
				fos = openFileOutput("Experts_XML", Context.MODE_PRIVATE);
				fos.write(XML_Fix.getBytes());
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 	}
	}
	
	public void GetDiffs()
	{
		//Get the XML File
	  	String xml;
	    Document doc;
	    File file;
	    String XML_Fix;
	    FileOutputStream fos;
	    
	    xml = XMLfunctions.getXML("aktual_diff");
	    XML_Fix = XMLfunctions.addHTMLEncoding(xml);
	    doc = XMLfunctions.XMLfromString(XML_Fix);

	    try {
	    	fos = openFileOutput("Aktualnosci_Diff_XML", Context.MODE_PRIVATE);
			fos.write(XML_Fix.getBytes());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 	
	 	xml = XMLfunctions.getXML("szkolenia_diff");
		XML_Fix = XMLfunctions.addHTMLEncoding(xml);
		doc = XMLfunctions.XMLfromString(XML_Fix);
			
		try {
			fos = openFileOutput("Szkolenia_Diff_XML", Context.MODE_PRIVATE);
			fos.write(XML_Fix.getBytes());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 	
	 	xml = XMLfunctions.getXML("moim_zdaniem_diff");
		XML_Fix = XMLfunctions.addHTMLEncoding(xml);
		doc = XMLfunctions.XMLfromString(XML_Fix);
		 	
		try {
			fos = openFileOutput("Moim_Zdaniem_Diff_XML", Context.MODE_PRIVATE);
			fos.write(XML_Fix.getBytes());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		xml = XMLfunctions.getXML("experts_diff");
		XML_Fix = XMLfunctions.addHTMLEncoding(xml);
		doc = XMLfunctions.XMLfromString(XML_Fix);
		 	
		try {
			fos = openFileOutput("Experts_Diff_XML", Context.MODE_PRIVATE);
			fos.write(XML_Fix.getBytes());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Update()
	{
		String xml;
		String xml_diff;
	    Document doc;  
	    Document doc_diff;
	    File file;
	    File file_diff;
	  		
	    // check if XML File exists on Android
	 	file = getBaseContext().getFileStreamPath("Aktualnosci_XML");
	 	file_diff = getBaseContext().getFileStreamPath("Aktualnosci_Diff_XML");
	 	if(file.exists() && file_diff.exists())
	 	{	
	 		// open xml file and convert from Bytes to String
	 		try {
	 			FileInputStream fIn = new FileInputStream(file);
	 			xml = IOUtils.toString(fIn);
	 		    doc = XMLfunctions.XMLfromString(xml);
	 			fIn.close();
	 		} catch (FileNotFoundException e) {
	 			xml = XMLfunctions.getXML("aktualnosci");
	 			doc = XMLfunctions.XMLfromString(xml);
	 			e.printStackTrace();
	 		} catch (IOException e) {
	 			xml = XMLfunctions.getXML("aktualnosci");
	 			doc = XMLfunctions.XMLfromString(xml);
	 			e.printStackTrace();
	 		}
	 		
	 		// open xml_diff file and convert from Bytes to String
	 		try {
	 			FileInputStream fIn = new FileInputStream(file_diff);
	 			xml_diff = IOUtils.toString(fIn);
	 		    doc_diff = XMLfunctions.XMLfromString(xml_diff);
	 			fIn.close();
	 		} catch (FileNotFoundException e) {
	 			xml_diff = XMLfunctions.getXML("aktual_diff");
	 			doc_diff = XMLfunctions.XMLfromString(xml_diff);
	 			e.printStackTrace();
	 		} catch (IOException e) {
	 			xml_diff = XMLfunctions.getXML("aktual_diff");
	 			doc_diff = XMLfunctions.XMLfromString(xml_diff);
	 			e.printStackTrace();
	 		}
	 		
	 		// Check if main file has articles from diff
	 		Node article = doc.getDocumentElement();
	 		Node article_diff = doc_diff.getDocumentElement();
	 		
	 		int res = 1;
	 		int res_diff = 1;
	 		String test = article.getFirstChild().getFirstChild().getAttributes().getNamedItem("d").getNodeValue();
	 		String test_diff = article_diff.getFirstChild().getLastChild().getAttributes().getNamedItem("d").getNodeValue();
	 		
			try{
				res = Integer.valueOf(article.getFirstChild().getFirstChild().getAttributes().getNamedItem("d").getNodeValue());
				res_diff = Integer.valueOf(article_diff.getFirstChild().getLastChild().getAttributes().getNamedItem("d").getNodeValue());
			}catch(Exception e ){
				res = -1;
				res_diff = -1;
			}
			
			/*AlertDialog.Builder dialogBuilder
			  = new AlertDialog.Builder(this)
			  .setTitle(test)
			  .setMessage(test_diff);
			  dialogBuilder.create().show();*/  
			  
			if (1 == 1)
			{
				xml = XMLfunctions.getXML("aktual_diff");
				//XML_Fix = XMLfunctions.addHTMLEncoding(xml);
				FileOutputStream fos; 
					
					try {
						fos = openFileOutput("Aktualnosci_XML", Context.MODE_APPEND);
						fos.write(xml.getBytes());
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
	 	}
	 	/* Aktualnosci END */
	 	
	 	file = getBaseContext().getFileStreamPath("Szkolenia_XML");
	 	file_diff = getBaseContext().getFileStreamPath("Szkolenia_Diff_XML");
	 	if(file.exists() && file_diff.exists())
	 	{	
	 	// open xml file and convert from Bytes to String
	 		try {
	 			FileInputStream fIn = new FileInputStream(file);
	 			xml = IOUtils.toString(fIn);
	 		    doc = XMLfunctions.XMLfromString(xml);
	 			fIn.close();
	 		} catch (FileNotFoundException e) {
	 			xml = XMLfunctions.getXML("szkolenia");
	 			doc = XMLfunctions.XMLfromString(xml);
	 			e.printStackTrace();
	 		} catch (IOException e) {
	 			xml = XMLfunctions.getXML("szkolenia");
	 			doc = XMLfunctions.XMLfromString(xml);
	 			e.printStackTrace();
	 		}
	 		
	 		// open xml_diff file and convert from Bytes to String
	 		try {
	 			FileInputStream fIn = new FileInputStream(file_diff);
	 			xml_diff = IOUtils.toString(fIn);
	 		    doc_diff = XMLfunctions.XMLfromString(xml_diff);
	 			fIn.close();
	 		} catch (FileNotFoundException e) {
	 			xml_diff = XMLfunctions.getXML("szkolenia_diff");
	 			doc_diff = XMLfunctions.XMLfromString(xml_diff);
	 			e.printStackTrace();
	 		} catch (IOException e) {
	 			xml_diff = XMLfunctions.getXML("szkolenia_diff");
	 			doc_diff = XMLfunctions.XMLfromString(xml_diff);
	 			e.printStackTrace();
	 		}
	 		
	 		Node article = doc.getDocumentElement();
	 	}
	 	/* Szkolenia END*/
	 	
	 	file = getBaseContext().getFileStreamPath("Moim_Zdaniem_XML");
	 	file_diff = getBaseContext().getFileStreamPath("Moim_Zdaniem_Diff_XML");
	 	if(file.exists() && file_diff.exists())
	 	{	
	 	// open xml file and convert from Bytes to String
	 		try {
	 			FileInputStream fIn = new FileInputStream(file);
	 			xml = IOUtils.toString(fIn);
	 		    doc = XMLfunctions.XMLfromString(xml);
	 			fIn.close();
	 		} catch (FileNotFoundException e) {
	 			xml = XMLfunctions.getXML("moim_zdaniem");
	 			doc = XMLfunctions.XMLfromString(xml);
	 			e.printStackTrace();
	 		} catch (IOException e) {
	 			xml = XMLfunctions.getXML("moim_zdaniem");
	 			doc = XMLfunctions.XMLfromString(xml);
	 			e.printStackTrace();
	 		}
	 		
	 		// open xml_diff file and convert from Bytes to String
	 		try {
	 			FileInputStream fIn = new FileInputStream(file_diff);
	 			xml_diff = IOUtils.toString(fIn);
	 		    doc_diff = XMLfunctions.XMLfromString(xml_diff);
	 			fIn.close();
	 		} catch (FileNotFoundException e) {
	 			xml_diff = XMLfunctions.getXML("moim_zdaniem_diff");
	 			doc_diff = XMLfunctions.XMLfromString(xml_diff);
	 			e.printStackTrace();
	 		} catch (IOException e) {
	 			xml_diff = XMLfunctions.getXML("moim_zdaniem_diff");
	 			doc_diff = XMLfunctions.XMLfromString(xml_diff);
	 			e.printStackTrace();
	 		}
	 		
	 		Node article = doc.getDocumentElement();
	 	}
	 	/*Moim Zdaniem END*/
	 	
	 	file = getBaseContext().getFileStreamPath("Experts_XML");
	 	file_diff = getBaseContext().getFileStreamPath("Experts_Diff_XML");
	 	if(file.exists() && file_diff.exists())
	 	{	
	 	// open xml file and convert from Bytes to String
	 		try {
	 			FileInputStream fIn = new FileInputStream(file);
	 			xml = IOUtils.toString(fIn);
	 		    doc = XMLfunctions.XMLfromString(xml);
	 			fIn.close();
	 		} catch (FileNotFoundException e) {
	 			xml = XMLfunctions.getXML("experts");
	 			doc = XMLfunctions.XMLfromString(xml);
	 			e.printStackTrace();
	 		} catch (IOException e) {
	 			xml = XMLfunctions.getXML("experts");
	 			doc = XMLfunctions.XMLfromString(xml);
	 			e.printStackTrace();
	 		}
	 		
	 		// open xml_diff file and convert from Bytes to String
	 		try {
	 			FileInputStream fIn = new FileInputStream(file_diff);
	 			xml_diff = IOUtils.toString(fIn);
	 		    doc_diff = XMLfunctions.XMLfromString(xml_diff);
	 			fIn.close();
	 		} catch (FileNotFoundException e) {
	 			xml_diff = XMLfunctions.getXML("experts_diff");
	 			doc_diff = XMLfunctions.XMLfromString(xml_diff);
	 			e.printStackTrace();
	 		} catch (IOException e) {
	 			xml_diff = XMLfunctions.getXML("experts_diff");
	 			doc_diff = XMLfunctions.XMLfromString(xml_diff);
	 			e.printStackTrace();
	 		}
	 		
	 		Node article = doc.getDocumentElement();
	 	}
	 	/*Experts END*/
		
	}
	
	public int CheckInternet()
	{
		  // Check Internet connection
		  ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		  if (connectivityManager != null)
		  {
			  
			  if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null)
				  if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() != null)
					  if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) 
					  return 1;
			  
			  if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null)
				  if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() != null)
					  if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) 
						  return 1;

			  
			  return 0;
			  //we are connected to a network
		  }   
		  else
			  return 0;
	}
	
	private class TheDownload extends AsyncTask<Void, Void, Void>{

	    @Override
	    protected void onPreExecute() {
	    	dialog = ProgressDialog.show(RadcaPrawnyActivity.this, "", "Downloading Database...", true, false);
	    }

	    @Override
	    protected Void doInBackground(Void... params) {
	        Download(); 
	        GetDiffs();
	        return null;
	    }

	    @Override
	    protected void onPostExecute(Void result) {
	    	dialog.dismiss();
	    }
	}
	
	private class TheUpdate extends AsyncTask<Void, Void, Void>{

	    @Override
	    protected void onPreExecute() {
	    	dialog = ProgressDialog.show(RadcaPrawnyActivity.this, "", "Szukanie, Sci�ganie Aktualizacji...", true, false);
	    }

	    @Override
	    protected Void doInBackground(Void... params) {
	    	Download();
	    	GetDiffs();
	        return null;
	    }

	    @Override
	    protected void onPostExecute(Void result) {
	    	dialog.dismiss();
	    }
	}

	public void ReturntoAktual(View paramView)
	{
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost); 
		tabHost.setCurrentTab(3);
		tabHost.setCurrentTab(0);
	}
	
	public void ReturntoMoim(View paramView)
	{
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost); 	
		tabHost.setCurrentTab(3);
		tabHost.setCurrentTab(1);
	}
	
	public void ReturntoSzkolenia(View paramView)
	{
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost); 	
		tabHost.setCurrentTab(3);
		tabHost.setCurrentTab(2);
	}
	
	public void ReturntoKontakt(View paramView)
	{
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost); 
		tabHost.setCurrentTab(3);
	}
	
	public void aktualnosci(View paramView)
    {   	    
		setContentView(R.layout.aktualnosci);
    	Resources res = getResources(); // Resource object to get Drawables
    	TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost); 
    	TabHost.TabSpec spec;  // Resusable TabSpec for each tab
    	Intent intent;  // Reusable Intent for each tab
        
	    // Aktualno�ci Tab
	    intent = new Intent().setClass(this, AktualnosciActivity.class);
	    spec = tabHost.newTabSpec("aktualno�ci")
	    		.setIndicator("",
	            res.getDrawable(R.drawable.tab_aktualnosci))
	           .setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	    tabHost.addTab(spec);

	    // Moim Zdaniem Tab
	    intent = new Intent(this, MoimZdaniemActivity.class);
	    spec = tabHost.newTabSpec("moim zdaniem")
	    		.setIndicator("",
	            res.getDrawable(R.drawable.tab_moim_zdaniem))
	           .setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	    tabHost.addTab(spec);

	    // Szkolenia Tab
	    intent = new Intent().setClass(this, SzkoleniaActivity.class);
	    spec = tabHost.newTabSpec("szkolenia")
	    		.setIndicator("",
	    	    res.getDrawable(R.drawable.tab_szkolenia))
	           .setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	    tabHost.addTab(spec);
	    
	    // Kontakt Tab
	    intent = new Intent(this, KontaktActivity.class);
	   	    spec = tabHost.newTabSpec("kontakt")
	    		.setIndicator("",
	            res.getDrawable(R.drawable.tab_kontakt))
	           .setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	    tabHost.addTab(spec);
    
	    tabHost.setCurrentTab(0); 
	    
	    /*tabHost.setOnTabChangedListener(new OnTabChangeListener() {

	    	@Override
	    	public void onTabChanged(String tabId) {
	            if(tabId.equals("aktualno�ci")) {
	                //tab1
	            	//setContentView(R.layout.aktualnosci);
	            	/*WebView webView = (WebView)findViewById(R.id.more_info_txt);
	            	if (webView != null)
	            		webView.setVisibility(View.GONE);
	            	TextView text = (TextView)findViewById(R.id.top_text_small);
	        		if (text != null)
	        			text.setText("Aktualnosci");
	        	
	        	
	        		
	        	    //startActivity(intent2);
	        	    
	            	//AktualnosciActivity test = new AktualnosciActivity();
	        		//test.new ReturnList().execute();
	            }
	            else if(tabId.equals("moim zdaniem")) {
	                //tab2
	            	
	            }
	            else if(tabId.equals("szkolenia")) {
	                //tab3
	            	
	            }
	            else if(tabId.equals("kontakt")) {
	                //tab4
	            	
	            }
	        }
	    }); */   
    }
    
    public void moim_zdaniem(View paramView)
    {
    	setContentView(R.layout.moim_zdaniem);
    	Resources res = getResources(); // Resource object to get Drawables
    	TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost); 
    	TabHost.TabSpec spec;  // Resusable TabSpec for each tab
    	Intent intent;  // Reusable Intent for each tab
        
	    // Aktualno�ci Tab
	    intent = new Intent().setClass(this, AktualnosciActivity.class);
	    spec = tabHost.newTabSpec("aktualno�ci")
	    		.setIndicator("",
	            res.getDrawable(R.drawable.tab_aktualnosci))
	           .setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	    tabHost.addTab(spec);

	    // Moim Zdaniem Tab
	    intent = new Intent(this, MoimZdaniemActivity.class);
	    spec = tabHost.newTabSpec("moim zdaniem")
	    		.setIndicator("",
	            res.getDrawable(R.drawable.tab_moim_zdaniem))
	           .setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	    tabHost.addTab(spec);

	    // Szkolenia Tab
	    intent = new Intent().setClass(this, SzkoleniaActivity.class);
	    spec = tabHost.newTabSpec("szkolenia")
	    		.setIndicator("",
	    	    res.getDrawable(R.drawable.tab_szkolenia))
	           .setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	    tabHost.addTab(spec);
	    
	    // Kontakt Tab
	    intent = new Intent(this, KontaktActivity.class);
	   	    spec = tabHost.newTabSpec("kontakt")
	    		.setIndicator("",
	            res.getDrawable(R.drawable.tab_kontakt))
	           .setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(1);
	}

    public void szkolenia(View paramView)
    {
    	setContentView(R.layout.szkolenia);
    	Resources res = getResources(); // Resource object to get Drawables
    	TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost); 
    	TabHost.TabSpec spec;  // Resusable TabSpec for each tab
    	Intent intent;  // Reusable Intent for each tab
        
	    // Aktualno�ci Tab
	    intent = new Intent().setClass(this, AktualnosciActivity.class);
	    spec = tabHost.newTabSpec("aktualno�ci")
	    		.setIndicator("",
	            res.getDrawable(R.drawable.tab_aktualnosci))
	           .setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	    tabHost.addTab(spec);

	    // Moim Zdaniem Tab
	    intent = new Intent(this, MoimZdaniemActivity.class);
	    spec = tabHost.newTabSpec("moim zdaniem")
	    		.setIndicator("",
	            res.getDrawable(R.drawable.tab_moim_zdaniem))
	           .setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	    tabHost.addTab(spec);

	    // Szkolenia Tab
	    intent = new Intent().setClass(this, SzkoleniaActivity.class);
	    spec = tabHost.newTabSpec("szkolenia")
	    		.setIndicator("",
	    	    res.getDrawable(R.drawable.tab_szkolenia))
	           .setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	    tabHost.addTab(spec);
	    
	    // Kontakt Tab
	    intent = new Intent(this, KontaktActivity.class);
	   	    spec = tabHost.newTabSpec("kontakt")
	    		.setIndicator("",
	            res.getDrawable(R.drawable.tab_kontakt))
	           .setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(2);
	}
    
    public void kontakt(View paramView)
    {	
    	setContentView(R.layout.aktualnosci);
    	Resources res = getResources(); // Resource object to get Drawables
    	TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost); 
    	TabHost.TabSpec spec;  // Resusable TabSpec for each tab
    	Intent intent;  // Reusable Intent for each tab
        
	    // Aktualno�ci Tab
	    intent = new Intent().setClass(this, AktualnosciActivity.class);
	    spec = tabHost.newTabSpec("aktualno�ci")
	    		.setIndicator("",
	            res.getDrawable(R.drawable.tab_aktualnosci))
	           .setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	    tabHost.addTab(spec);

	    // Moim Zdaniem Tab
	    intent = new Intent(this, MoimZdaniemActivity.class);
	    spec = tabHost.newTabSpec("moim zdaniem")
	    		.setIndicator("",
	            res.getDrawable(R.drawable.tab_moim_zdaniem))
	           .setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	    tabHost.addTab(spec);

	    // Szkolenia Tab
	    intent = new Intent().setClass(this, SzkoleniaActivity.class);
	    spec = tabHost.newTabSpec("szkolenia")
	    		.setIndicator("",
	    	    res.getDrawable(R.drawable.tab_szkolenia))
	           .setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	    tabHost.addTab(spec);
	    
	    // Kontakt Tab
	    intent = new Intent(this, KontaktActivity.class);
	   	    spec = tabHost.newTabSpec("kontakt")
	    		.setIndicator("",
	            res.getDrawable(R.drawable.tab_kontakt))
	           .setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(3); 
	}
}
