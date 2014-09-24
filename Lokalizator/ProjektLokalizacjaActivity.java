package projekt.lokalizacja;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import projekt.lokalizacja.GeofenceUtils.REQUEST_TYPE;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.MapView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class ProjektLokalizacjaActivity extends MapActivity  {    
	//XML-RPC Client
	private XMLRPCClient client;
	private URI uri;

	//Check if is tablet
	private boolean isThisTablet;

	//Cords
	private double latitude;
	private double longitude;
	private int range = 10;

	//Needed for JSON Code
	private String category;
	private String attribute;
	private String open = "false";

	//Mapview, GPS, Geofence
	MapView mapView = null; 
	MyLocationOverlay myLocationOverlay;
	boolean MapActive = false;
	private GeofenceReceiver mBroadcastReceiver;
    private IntentFilter mIntentFilter;
    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
    private static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * DateUtils.HOUR_IN_MILLIS;
    List<Geofence> mCurrentGeofences;
    
	//Objects on map list + adapter
	private Drawable drawable;
	private ArrayList<TreeMap<String, String>> mylist = new ArrayList<TreeMap<String, String>>();
	ListView mylistView;
	LazyAdapter adapter;
	HelloItemizedOverlay itemizedoverlay;
	@SuppressWarnings("rawtypes")
	HashMap MyMap;

	// Arrays for XML Response
	Object MiejscaArray[];
	Object CategoryArray[];
	Object RangeArray[];
	Object AttributeArray[];
	Object OnePoint;
	Integer AtrributeChecks[];

	//Adding Category Spinner
	private Spinner CategorySelection;
	private ArrayAdapter<CharSequence> CategoryAdapter;
	private List<CharSequence> CategoryList;

	//Adding Range Spinner
	private Spinner RangeSelection;
	private ArrayAdapter<CharSequence> RangeAdapter;
	private List<CharSequence> RangeList;

	//Adding Attribute Spinner
	private Spinner AtrSelection;
	private ArrayAdapter<CharSequence> AtrAdapter;
	private List<CharSequence> AtrList;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main); 
		// serwer XML + client 
		uri = URI.create("http://server");
		client = new XMLRPCClient(uri);
		// tablet?
		isThisTablet = getResources().getBoolean(R.bool.CheckIfTablet);

		// Create a new broadcast receiver to receive updates from the listeners and service
        mBroadcastReceiver = new GeofenceReceiver();

        // Create an intent filter for the broadcast receiver
        mIntentFilter = new IntentFilter();
        // Action for broadcast Intents that report successful addition of geofences
        mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCES_ADDED);
        // Action for broadcast Intents that report successful removal of geofences
        mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCES_REMOVED);
        // Action for broadcast Intents containing various types of geofencing errors
        mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCE_ERROR);
        // All Location Services sample apps use this category
        mIntentFilter.addCategory(GeofenceUtils.CATEGORY_LOCATION_SERVICES);
        
		// Szukamy Naszej pozycji
		final LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
			}

			@Override
			public void onProviderDisabled(String provider) {
			}

			@Override
			public void onProviderEnabled(String provider) {
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}
		};

		//Szukamy Naszej pozycji
		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, locationListener);	
		Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (loc != null)
		{
			latitude = loc.getLatitude();
			longitude = loc.getLongitude();
		}
		else
		{
			latitude = 50;
			longitude = 20;
		}

		Geocoder gc = new Geocoder(this, Locale.getDefault());
		try 
		{	
			List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
			StringBuilder sb = new StringBuilder();
			if (addresses.size() > 0) 
			{
				Address address = addresses.get(0);
				for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
				{
					sb.append(address.getAddressLine(i)).append(", ");
				}
				TextView myLoc = (TextView) findViewById(R.id.editText1);
				myLoc.setText(sb.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {}
	}

	public void ShowAll (View paramView)
	{
		// Tworzymy liste objektow i wybieramy im grafike
		final MapView mapView = (MapView) findViewById(R.id.mapView);


		mapView.getOverlays().clear();
		itemizedoverlay.clear();
		mapView.setBuiltInZoomControls(false);

		// add our position to map
		mapView.getOverlays().add(myLocationOverlay);
		mapView.postInvalidate();

		final List<Overlay> mapOverlays = mapView.getOverlays();
		myLocationOverlay.enableMyLocation();

		// add our position to map and refresh it
		mapView.getOverlays().add(myLocationOverlay);
		mapView.postInvalidate();
		myLocationOverlay.enableMyLocation();

		// add all points from list to map
		for(int i = 0; i<MiejscaArray.length; i++)
		{	
			@SuppressWarnings("rawtypes")
			HashMap MojaMapa = (HashMap) MiejscaArray[i];
			@SuppressWarnings("rawtypes")
			HashMap MyMap = (HashMap) MojaMapa.get("Category");
			System.out.println(MojaMapa);

			float Lat = Float.parseFloat(""+MojaMapa.get("latitude"));
			float Long = Float.parseFloat(""+MojaMapa.get("longitude"));
			int MicroLat = (int) (Lat*1e6);
			int MicroLong = (int) (Long*1e6);

			GeoPoint point = new GeoPoint(MicroLat, MicroLong);
			MyOverlayItem overlayitem = new MyOverlayItem(point, 
					""+i, 
					""+category, 
					""+MojaMapa.get("address"),
					""+MojaMapa.get("city"),
					""+MojaMapa.get("zip_code"),
					""+MojaMapa.get("country"),
					String.format("%.2f", Float.parseFloat(MojaMapa.get("kilometers").toString())),
					""+MojaMapa.get("latitude"),
					""+MojaMapa.get("longitude"),
					""+MyMap.get("icon"), 
					Lat+","+Long);

			SetDrawable(""+MyMap.get("icon"), ""+MyMap.get("color"));
			itemizedoverlay = new HelloItemizedOverlay(drawable, this);
			itemizedoverlay.addOverlay(overlayitem);
			mapOverlays.add(itemizedoverlay);
		}

		// Remove WebView and place MapView in his place
		WebView webView = (WebView) findViewById(R.id.webView);
		webView.setVisibility(View.GONE);

		mylistView =(ListView)findViewById(android.R.id.list);

		if (isThisTablet == false)
		{
			mylistView.setVisibility(View.GONE);
			mapView.setVisibility(View.VISIBLE);

			Button Mapbutton = (Button) findViewById(R.id.map_button);
			Mapbutton.setBackgroundColor(Color.BLUE);

			Button ListButton = (Button) findViewById(R.id.list_button);
			ListButton.setBackgroundColor(Color.rgb(192, 192, 192));

			Button SzczeButton = (Button) findViewById(R.id.szczegoly_button);
			SzczeButton.setBackgroundColor(Color.rgb(192, 192, 192));

		}
		else
		{
			//Button Mapbutton = (Button) findViewById(R.id.map);
			//Mapbutton.setVisibility(View.GONE);

			//Button Szczebutton = (Button) findViewById(R.id.szczegoly);
			//Szczebutton.setVisibility(View.VISIBLE);

			mapView.setVisibility(View.VISIBLE);	
		}
	}

	public void More (View paramView)
	{
		if (!CheckInternet())
			return;

		if (!CheckGPS())
			return;

		setContentView(R.layout.more);

		/*for(int i = 0; i < 20; i++) {
			CheckBox cb = new CheckBox(this);
			cb.setText("I'm dynamic!");
			ll.addView(cb);
		}
		 */
		//Set Attribute Spinner
		try {
			AttributeArray = (Object[])client.call("placeAttribute.fetchAllFilters");
			System.out.println(AttributeArray[0]);
		} catch (XMLRPCException e1) {
			SendAlert("placeAttribute.fetchAllFilters");
			e1.printStackTrace();
		}

		if (AttributeArray == null)
		{

		}
		else
		{			
			for(int i = 0; i<AttributeArray.length; i++)
			{	
				@SuppressWarnings("rawtypes")
				final HashMap MojaMapa = (HashMap) AttributeArray[i];

				if (MojaMapa.get("filter").toString().equals("true"))
				{
					LinearLayout LL = (LinearLayout) findViewById(R.id.MyLayoutMoreAtribute);

					final CheckBox cb  = new CheckBox(this);
					cb.setText(""+MojaMapa.get("name"));
					LL.addView(cb); 

					cb.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) 
						{
							if (cb.isChecked())
							{
								//AtrributeChecks[i] = 1;
								System.out.println(""+MojaMapa.get("name"));
							}
						}
					});
				}
			}
		}

		//Set Range Spinner
		try {
			RangeArray = (Object[])client.call("searchDistance.fetchAll");
			//System.out.println(RangeArray[0]);
		} catch (XMLRPCException e1) {
			SendAlert("searchDistance.fetchAll");
			e1.printStackTrace();
		}

		if (RangeArray == null)
		{
			RangeList = new ArrayList<CharSequence>();
			RangeAdapter = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item, RangeList);
			RangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			RangeSelection = (Spinner)this.findViewById(R.id.rangeSpinner);
			RangeSelection.setAdapter(RangeAdapter);
			RangeAdapter.add("Null");
		}
		else
		{
			RangeList = new ArrayList<CharSequence>();
			RangeAdapter = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item, RangeList);
			RangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			RangeSelection = (Spinner)this.findViewById(R.id.rangeSpinner);
			RangeSelection.setAdapter(RangeAdapter);
			RangeSelection.setOnItemSelectedListener(new OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
					// Here go your instructions when the user chose something
					@SuppressWarnings("rawtypes")
					HashMap MojaMapa = (HashMap) RangeArray[position];
					Toast.makeText(getBaseContext(), ""+MojaMapa.get("distance"), 0).show();
					range = Integer.parseInt(""+MojaMapa.get("distance"));
				}
				public void onNothingSelected(AdapterView<?> arg0) { }
			});

			for(int i = 0; i<RangeArray.length; i++)
			{	
				@SuppressWarnings("rawtypes")
				HashMap MojaMapa = (HashMap) RangeArray[i];		
				//items[i] = ""+MojaMapa.get("name");
				//System.out.println(MojaMapa.get("name"));
				RangeAdapter.add(""+MojaMapa.get("distance")+ "km");
			}
		}

		//Set Category Spinner
		try {
			CategoryArray = (Object[])client.call("category.fetchAllFlat");
			//System.out.println(CategoryArray[0]);
		} catch (XMLRPCException e1) {
			SendAlert("category.fetchAllFlat");
			e1.printStackTrace();
		}

		if (CategoryArray == null)
		{
			CategoryList = new ArrayList<CharSequence>();
			CategoryAdapter = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item, CategoryList);
			CategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			CategorySelection = (Spinner)this.findViewById(R.id.categorySpinner);
			CategorySelection.setAdapter(CategoryAdapter);
			CategoryAdapter.add("Null");
		}
		else
		{
			CategoryList = new ArrayList<CharSequence>();
			CategoryAdapter = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item, CategoryList);
			CategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			CategorySelection = (Spinner)this.findViewById(R.id.categorySpinner);
			CategorySelection.setAdapter(CategoryAdapter);
			CategorySelection.setOnItemSelectedListener(new OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
					// Here go your instructions when the user chose something
					if (position == 0)
					{
						Toast.makeText(getBaseContext(), "All Categories", 0).show();
						category = "";
					}
					else
					{
						@SuppressWarnings("rawtypes")
						HashMap MojaMapa = (HashMap) CategoryArray[position-1];
						Toast.makeText(getBaseContext(), ""+MojaMapa.get("name"), 0).show();
						category = ""+MojaMapa.get("id");
					}
				}
				public void onNothingSelected(AdapterView<?> arg0) { }
			});

			CategoryAdapter.add("All Categories");
			for(int i = 0; i<CategoryArray.length; i++)
			{	
				@SuppressWarnings("rawtypes")
				HashMap MojaMapa = (HashMap) CategoryArray[i];		
				//items[i] = ""+MojaMapa.get("name");
				//System.out.println(MojaMapa.get("name"));
				CategoryAdapter.add(""+MojaMapa.get("name"));
			}
		}


	}

	public void SetList (View paramView)
	{
		mylistView =(ListView)findViewById(android.R.id.list);
		MapView mapView = (MapView) findViewById(R.id.mapView);

		WebView webView = (WebView) findViewById(R.id.webView);
		webView.setVisibility(View.GONE);

		mapView.setVisibility(View.VISIBLE);

		mylistView.setVisibility(View.VISIBLE);

		Button Mapbutton = (Button) findViewById(R.id.map_button);
		Mapbutton.setBackgroundColor(Color.rgb(192, 192, 192));

		Button ListButton = (Button) findViewById(R.id.list_button);
		ListButton.setBackgroundColor(Color.BLUE);

		Button SzczeButton = (Button) findViewById(R.id.szczegoly_button);
		SzczeButton.setBackgroundColor(Color.rgb(192, 192, 192));
	}

	public void SetSzczegoly (View paramView)
	{
		mylistView =(ListView)findViewById(android.R.id.list);
		MapView mapView = (MapView) findViewById(R.id.mapView);
		WebView webView = (WebView) findViewById(R.id.webView);

		mylistView.setVisibility(View.GONE);
		mapView.setVisibility(View.GONE);
		webView.setVisibility(View.VISIBLE);

		Button Mapbutton = (Button) findViewById(R.id.map_button);
		Mapbutton.setBackgroundColor(Color.rgb(192, 192, 192));

		Button ListButton = (Button) findViewById(R.id.list_button);
		ListButton.setBackgroundColor(Color.rgb(192, 192, 192));  

		Button SzczeButton = (Button) findViewById(R.id.szczegoly_button);
		SzczeButton.setBackgroundColor(Color.BLUE);
	}

	public void SetMap (View paramView)
	{
		mylistView =(ListView)findViewById(android.R.id.list);
		MapView mapView = (MapView) findViewById(R.id.mapView);

		WebView webView = (WebView) findViewById(R.id.webView);
		webView.setVisibility(View.GONE);

		if (isThisTablet == false)
		{
			mylistView.setVisibility(View.GONE);
			mapView.setVisibility(View.VISIBLE);

			Button Mapbutton = (Button) findViewById(R.id.map_button);
			Mapbutton.setBackgroundColor(Color.BLUE);

			Button ListButton = (Button) findViewById(R.id.list_button);
			ListButton.setBackgroundColor(Color.rgb(192, 192, 192));

			Button SzczeButton = (Button) findViewById(R.id.szczegoly_button);
			SzczeButton.setBackgroundColor(Color.rgb(192, 192, 192));

		}
		else
		{
			mapView.setVisibility(View.VISIBLE);	
		}
	}

	public void BackToSearch (View paramView)
	{
		setContentView(R.layout.main); 
		Geocoder gc = new Geocoder(this, Locale.getDefault());
		try 
		{	
			List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
			StringBuilder sb = new StringBuilder();
			if (addresses.size() > 0) 
			{
				Address address = addresses.get(0);
				for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
				{
					sb.append(address.getAddressLine(i)).append(", ");
				}
				TextView myLoc = (TextView) findViewById(R.id.editText1);
				myLoc.setText(sb.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {}
	}

	public boolean CheckInternet()
	{
		/* sprawdzamy czy istnieje po�aczenie internetowe */
		boolean connected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || 
				connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
			//we are connected to a network
			connected = true;
		}
		else
			connected = false;

		if (connected == false)
		{
			/* Brak po�aczenia = Alert i brak moznosci dalszego uzywania aplikacji */
			AlertDialog.Builder dialogBuilder
			= new AlertDialog.Builder(this)
			.setTitle("ALERT")
			.setMessage("This Application Requires An Active Internet Connection.");
			dialogBuilder.create().show();  
		}
		return connected;
	}

	public boolean CheckGPS()
	{
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			return true;
		}
		else
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
			.setCancelable(false)
			.setPositiveButton("Enable GPS",	
					new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(callGPSSettingIntent);
				}
			});
			alertDialogBuilder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					dialog.cancel();
				}
			});
			AlertDialog alert = alertDialogBuilder.create();
			alert.show();
			return false;
		}
	}

	public void SendAlert(String function)
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage(function)
		.setCancelable(false)
		.setPositiveButton("Exit Application",	
				new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				/*finish();
	            System.exit(0);*/
			}
		});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}

	public class SendJson 
	{
		private final String attributes;
		private final String category_id;
		private final String openNow;

		Object attribute[];

		public SendJson() { 
			this(null, category, null /*open*/);
		}

		public SendJson(String attributes, String category_id, String openNow) {
			this.attributes = attributes;
			this.category_id = category_id;
			this.openNow = openNow;
		}
	}

	public Bitmap drawable_from_url(String url) throws java.net.MalformedURLException, java.io.IOException {
		Bitmap x;

		HttpURLConnection connection = (HttpURLConnection)new URL(url) .openConnection();
		connection.setRequestProperty("User-agent","Mozilla/4.0");

		connection.connect();
		InputStream input = connection.getInputStream();

		x = BitmapFactory.decodeStream(input);
		return x;
	}

	public void SetDrawable(String iconName, String color)
	{
		System.out.println(iconName + color);
		String url = "http://chart.googleapis.com/chart?chst=d_map_xpin_icon&chld=pin%7C"+iconName+"%7C"+color;
		String new_url = url.replaceAll("#", "");
		System.out.println(new_url);
		Bitmap bitmap = null;

		try {
			bitmap = drawable_from_url(new_url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (bitmap == null)
			SendAlert("bitmap");
		drawable = new BitmapDrawable(getResources(), bitmap);
	}

	// Znajd� w pobli�u lista - tworzy liste pobliskich objekt�w
	@SuppressWarnings("rawtypes")
	public void Szukaj(View paramView)
	{
		if (!CheckInternet())
			return;

		if (!CheckGPS())
			return;

		final CheckBox checkBox = (CheckBox) findViewById(R.id.openNowCB);
		if (checkBox != null)
		{
			if (checkBox.isChecked()) 
			{
				open = "true";
			}
		}

		//Creating JSON
		Gson gson = new GsonBuilder().serializeNulls().create();

		SendJson sendItem = new SendJson();
		Object json = gson.toJson(sendItem);

		try {
			MiejscaArray = (Object[])client.call("place.findWithinDistance", latitude, longitude, range, json);
		} catch (XMLRPCException e1) {
			SendAlert("place.findWithinDistance");
			e1.printStackTrace();
		}

		if (MiejscaArray == null)
		{
			SendAlert("Znaleziono Zero Objekt�w");
			return;
		}

		MapActive = true;	
		setContentView(R.layout.list);

		final MapView mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(false);
		// create an overlay that shows our current location
		myLocationOverlay = new MyLocationOverlay(this, mapView);

		// add this overlay to the MapView and refresh it
		mapView.getOverlays().add(myLocationOverlay);
		mapView.postInvalidate();

		// Tworzymy liste objektow i wybieramy im grafike
		final List<Overlay> mapOverlays = mapView.getOverlays();

		myLocationOverlay.enableMyLocation();
		for(int i = 0; i<MiejscaArray.length; i++)
		{	
			@SuppressWarnings("rawtypes")
			HashMap MojaMapa = (HashMap) MiejscaArray[i];
			MyMap = (HashMap) MojaMapa.get("Category");
			System.out.println(MojaMapa);

			TreeMap<String, String> sortedMap1=new TreeMap<String, String>();
			sortedMap1.put("i", ""+i);
			sortedMap1.put("Id", ""+MojaMapa.get("id"));
			sortedMap1.put("Address", ""+MojaMapa.get("address"));
			sortedMap1.put("Distance", ""+MojaMapa.get("kilometers"));
			sortedMap1.put("Category", category);
			sortedMap1.put("Icon", ""+MyMap.get("icon"));
			sortedMap1.put("Color", ""+MyMap.get("color"));
			sortedMap1.put("City", ""+MojaMapa.get("city"));
			sortedMap1.put("Country", ""+MojaMapa.get("country"));
			sortedMap1.put("Zip", ""+MojaMapa.get("zip_code"));
			sortedMap1.put("Latitude", ""+MojaMapa.get("latitude"));
			sortedMap1.put("Longitude", ""+MojaMapa.get("longitude"));


			mylist.add(sortedMap1);

			float Lat = Float.parseFloat(""+MojaMapa.get("latitude"));
			float Long = Float.parseFloat(""+MojaMapa.get("longitude"));
			int MicroLat = (int) (Lat*1e6);
			int MicroLong = (int) (Long*1e6);

			GeoPoint point = new GeoPoint(MicroLat, MicroLong);
			MyOverlayItem overlayitem = new MyOverlayItem(point, 
					""+i, 
					""+category, 
					""+MojaMapa.get("address"),
					""+MojaMapa.get("city"),
					""+MojaMapa.get("zip_code"),
					""+MojaMapa.get("country"),
					String.format("%.2f", Float.parseFloat(MojaMapa.get("kilometers").toString())),
					""+MojaMapa.get("latitude"),
					""+MyMap.get("color"),
					""+MyMap.get("icon"), 
					Lat+","+Long);

			REQUEST_TYPE mRequestType = GeofenceUtils.REQUEST_TYPE.ADD;
			if (servicesConnected()) {
			    SimpleGeofence geofence = new SimpleGeofence(
			            ""+i,
			            // Get latitude, longitude, and radius from the UI
			            Double.valueOf(""+MojaMapa.get("latitude")),
			            Double.valueOf(""+MojaMapa.get("longitude")),
			            1000,
			            // Set the expiration time
			            GEOFENCE_EXPIRATION_IN_MILLISECONDS,
			            // Only detect entry transitions
			            Geofence.GEOFENCE_TRANSITION_ENTER);
			    mCurrentGeofences.add(geofence.toGeofence());
	        }
			SetDrawable(""+MyMap.get("icon"), ""+MyMap.get("color"));
			itemizedoverlay = new HelloItemizedOverlay(drawable, this);
			itemizedoverlay.addOverlay(overlayitem);
			mapOverlays.add(itemizedoverlay);
		}	
		
		if (!mCurrentGeofences.isEmpty())
		{
    		// Start the request. Fail if there's already a request in progress
            try {
                // Try to add geofences
                mGeofenceRequester.addGeofences(mCurrentGeofences);
            } catch (UnsupportedOperationException e) {
                // TODO
            }
		}
        
		mylistView =(ListView)findViewById(android.R.id.list);
		adapter= new LazyAdapter(this, mylist);
		mylistView.setAdapter(adapter);	

		Button Mapbutton = (Button) findViewById(R.id.map_button);
		Mapbutton.setBackgroundColor(Color.rgb(192, 192, 192));

		Button ListButton = (Button) findViewById(R.id.list_button);
		ListButton.setBackgroundColor(Color.BLUE);

		Button SzczeButton = (Button) findViewById(R.id.szczegoly_button);
		SzczeButton.setBackgroundColor(Color.rgb(192, 192, 192));

		mylistView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				//Having Trouble with this line, how to retrieve value???
				@SuppressWarnings("unchecked")
				TreeMap<String, String> map2 = (TreeMap<String, String>) mylistView.getAdapter().getItem(position);
				float Lat = Float.parseFloat(map2.get("Latitude"));
				float Long = Float.parseFloat(map2.get("Longitude"));

				int myPoint = Integer.parseInt(map2.get("Id"));
				System.out.println(myPoint);

				try {
					OnePoint = (Object)client.call("place.fetchOne", myPoint);
					//System.out.println(Arrays.toString(OnePoint));
					System.out.println(OnePoint);
				} catch (XMLRPCException e1) {
					SendAlert("place.fetchOne");
					e1.printStackTrace();
					return;
				}

				@SuppressWarnings("rawtypes")
				HashMap MojaMapa = (HashMap) OnePoint;
				MojaMapa.get("description");

				String myString = ""
						+"<br>"+"<br>"+"<br>"
						+"Category: "+category+"<p>"
						+"Address: "+map2.get("Address")+"<p>"
						+"City: "+map2.get("City")+"<p>"
						+"Zip Code: "+map2.get("Zip")+"<p>"
						+"Country: "+map2.get("Country")+"<p>"
						+"Distance: "+String.format("%.2f", Float.parseFloat(map2.get("Distance").toString()))+"km"+"<p>"
						+"Latitude: "+map2.get("Latitude")+"<p>"
						+"Longitude: "+map2.get("Longitude")+"<p>"
						+"Opis: " + MojaMapa.get("description");

				int MicroLat = (int) (Lat*1e6);
				int MicroLong = (int) (Long*1e6);

				mapView.getOverlays().clear();
				mapOverlays.clear();
				itemizedoverlay.clear();

				GeoPoint point = new GeoPoint(MicroLat, MicroLong);
				MyOverlayItem overlayitem = new MyOverlayItem(point, map2.get("i"), 
						""+category, 
						""+map2.get("Address"),
						""+map2.get("City"),
						""+map2.get("Zip"),
						""+map2.get("Country"),
						String.format("%.2f", Float.parseFloat(map2.get("Distance").toString())),
						""+map2.get("Latitude"),
						""+map2.get("Color"),
						""+map2.get("Icon"), Lat+","+Long);

				SetDrawable(""+map2.get("Icon"), ""+map2.get("Color"));

				HelloItemizedOverlay singleitemizedoverlay = new HelloItemizedOverlay(drawable, ProjektLokalizacjaActivity.this);            
				singleitemizedoverlay.clear();
				singleitemizedoverlay.addOverlay(overlayitem);
				mapOverlays.add(singleitemizedoverlay);

				// add this overlay to the MapView and refresh it
				mapView.getOverlays().add(myLocationOverlay);
				mapView.postInvalidate();
				myLocationOverlay.enableMyLocation();
				if (isThisTablet == false)
				{
					mylistView.setVisibility(View.GONE);
					mapView.setVisibility(View.GONE);

					WebView webView = (WebView) findViewById(R.id.webView);
					webView.setVisibility(View.VISIBLE);

					webView.clearView();
					webView.clearCache(true);
					webView.loadDataWithBaseURL(null, myString, "text/html", "utf-8", null);

					Button Mapbutton = (Button) findViewById(R.id.map_button);
					Mapbutton.setBackgroundColor(Color.rgb(192, 192, 192));

					Button ListButton = (Button) findViewById(R.id.list_button);
					ListButton.setBackgroundColor(Color.rgb(192, 192, 192));

					Button SzczeButton = (Button) findViewById(R.id.szczegoly_button);
					SzczeButton.setBackgroundColor(Color.BLUE);
					//SearchItemOnMap(Lat, Long, MicroLat, MicroLong);	
				}
				else
				{
					WebView webView = (WebView) findViewById(R.id.webView);
					webView.setVisibility(View.VISIBLE);
					mapView.setVisibility(View.GONE);

					webView.clearView();
					webView.clearCache(true);
					webView.loadDataWithBaseURL(null, myString, "text/html", "utf-8", null);

				}
			}
		});	   
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Tylko je�eli jest aktywna mapa
		if (MapActive == true)
		{
			// when our activity resumes, we want to register for location updates
			myLocationOverlay.enableMyLocation();
		    LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, mIntentFilter);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Tylko je�eli jest aktywna mapa
		if (MapActive == true)
			// when our activity pauses, we want to remove listening for location updates
			myLocationOverlay.disableMyLocation();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public class GeofenceReceiver extends BroadcastReceiver {
        /*
         * Define the required method for broadcast receivers
         * This method is invoked when a broadcast Intent triggers the receiver
         */
        @Override
        public void onReceive(Context context, Intent intent) {

            // Check the action code and determine what to do
            String action = intent.getAction();

            // Intent contains information about errors in adding or removing geofences
            if (TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCE_ERROR)) {

                handleGeofenceError(context, intent);

            // Intent contains information about successful addition or removal of geofences
            } else if (
                    TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCES_ADDED)
                    ||
                    TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCES_REMOVED)) {

                handleGeofenceStatus(context, intent);

            // Intent contains information about a geofence transition
            } else if (TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCE_TRANSITION)) {

                handleGeofenceTransition(context, intent);

            // The Intent contained an invalid action
            } else {
                Log.e(GeofenceUtils.APPTAG, getString(R.string.invalid_action_detail, action));
                Toast.makeText(context, R.string.invalid_action, Toast.LENGTH_LONG).show();
            }
        }

        /**
         * If you want to display a UI message about adding or removing geofences, put it here.
         *
         * @param context A Context for this component
         * @param intent The received broadcast Intent
         */
        private void handleGeofenceStatus(Context context, Intent intent) {

        }

        /**
         * Report geofence transitions to the UI
         *
         * @param context A Context for this component
         * @param intent The Intent containing the transition
         */
        private void handleGeofenceTransition(Context context, Intent intent) {
            /*
             * If you want to change the UI when a transition occurs, put the code
             * here. The current design of the app uses a notification to inform the
             * user that a transition has occurred.
             */
        }

        /**
         * Report addition or removal errors to the UI, using a Toast
         *
         * @param intent A broadcast Intent sent by ReceiveTransitionsIntentService
         */
        private void handleGeofenceError(Context context, Intent intent) {
            String msg = intent.getStringExtra(GeofenceUtils.EXTRA_GEOFENCE_STATUS);
            Log.e(GeofenceUtils.APPTAG, msg);
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }
	
	private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {

            // In debug mode, log the status
            Log.d(GeofenceUtils.APPTAG, getString(R.string.play_services_available));

            // Continue
            return true;

        // Google Play services was not available for some reason
        } else {

            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(dialog);
                errorFragment.show(getSupportFragmentManager(), GeofenceUtils.APPTAG);
            }
            return false;
        }
    }
}