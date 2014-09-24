package nastroj.moj;

import java.net.URI;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NastrojmojActivity extends Activity {
	private XMLRPCClient client;
	private URI uri;
	private int picture;
	private int currentViewId;
	
	  /* Called when the activity is first created. */
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setCurrentViewById(R.layout.main);
	      /* ��czymy si� z serwerem XML i tworzymy clienta */
	      uri = URI.create("http://server");
		  client = new XMLRPCClient(uri);
		  picture = 1;
	  }
	  
	  /**
	   * Kontroler przycisku MENU - przy u�yciu go otwiera si� Alertdialog z przekierowaniem na codefathers 
	   * 
	   * @param  keycode wartosc int wcisnietego przycisku
	   * @param  event KeyEvent
	   * @return true or false
	   * @see onKeyDown
	   */
	  @Override
	  public boolean onKeyDown(int keycode, KeyEvent event ) {
		  if(keycode == KeyEvent.KEYCODE_MENU)
		  {
			  AlertDialog.Builder dialogBuilder
			  = new AlertDialog.Builder(this)
			  .setPositiveButton("About", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) 
					{
						 WebView myView = (WebView) findViewById(R.id.webview);
						 myView.loadUrl("http://www");
					}
					})
			  .setTitle("Poka� jakim smokiem jeste�!");
			  dialogBuilder.create().show();
		  }
		  else if (keycode == KeyEvent.KEYCODE_BACK && keycode == KeyEvent.ACTION_UP)
		  {
			  if (currentViewId == R.layout.already_voted) //already_voted
			  {
				  setCurrentViewById(R.layout.stats);
				  Shader textShader=
						  new LinearGradient(0, 0, 0, 30,
				            new int[]{Color.WHITE,Color.rgb(206, 206, 206)},
				            new float[]{0, 1}, TileMode.CLAMP);
				  
				  Typeface myTypeface = Typeface.createFromAsset(getAssets(), "font/my1.ttf");

				  TextView love_status = (TextView)findViewById(R.id.love_stat);  
			      love_status.setText(25+ "%");
			      love_status.getPaint().setShader(textShader);
			      love_status.setTypeface(myTypeface);
			      love_status.setShadowLayer(1, 0, 0, Color.BLACK);
			      
			      TextView good_status = (TextView)findViewById(R.id.good_stat);
			      good_status.setText(25+ "%");
			      good_status.getPaint().setShader(textShader);
			      good_status.setTypeface(myTypeface);
			      good_status.setShadowLayer(1, 0, 0, Color.BLACK);
			      
			      TextView neutral_status = (TextView)findViewById(R.id.neutral_stat);
			      neutral_status.setText(25+ "%");
			      neutral_status.getPaint().setShader(textShader);
			      neutral_status.setTypeface(myTypeface);
			      neutral_status.setShadowLayer(1, 0, 0, Color.BLACK);
			      
			      TextView bad_status = (TextView)findViewById(R.id.bad_stat);
			      bad_status.setText(25+ "%");
			      bad_status.getPaint().setShader(textShader);
			      bad_status.setTypeface(myTypeface);
			      bad_status.setShadowLayer(1, 0, 0, Color.BLACK);
			  }
			  else if (currentViewId == R.layout.main) // main
			  {
				  finish();
			  }
			  else if (currentViewId == R.layout.result) //result
			  {
				  setCurrentViewById(R.layout.already_voted);
				  Button Button = (Button) findViewById(R.id.button1);
				  Button.setVisibility(View.VISIBLE);
				  Button.setBackgroundColor(Color.TRANSPARENT);
			  }
			  else if (currentViewId == R.layout.stats) //stats
			  {
				  setCurrentViewById(R.layout.vote);
			  }
			  else if (currentViewId == R.layout.vote) //vote
			  {
				  setCurrentViewById(R.layout.main);
			  }
			  
		  }
		  return super.onKeyDown(keycode,event); 
	  }
	    
	  // Alternative variant for API 5 and higher
	  @Override
	  public void onBackPressed() 
	  {
		  if (currentViewId == R.layout.already_voted) //already_voted
		  {
			  setCurrentViewById(R.layout.stats);
			  Shader textShader=
					  new LinearGradient(0, 0, 0, 30,
			            new int[]{Color.WHITE,Color.rgb(206, 206, 206)},
			            new float[]{0, 1}, TileMode.CLAMP);
			  
			  Typeface myTypeface = Typeface.createFromAsset(getAssets(), "font/my1.ttf");

			  TextView love_status = (TextView)findViewById(R.id.love_stat);  
		      love_status.setText(25+ "%");
		      love_status.getPaint().setShader(textShader);
		      love_status.setTypeface(myTypeface);
		      love_status.setShadowLayer(1, 0, 0, Color.BLACK);
		      
		      TextView good_status = (TextView)findViewById(R.id.good_stat);
		      good_status.setText(25+ "%");
		      good_status.getPaint().setShader(textShader);
		      good_status.setTypeface(myTypeface);
		      good_status.setShadowLayer(1, 0, 0, Color.BLACK);
		      
		      TextView neutral_status = (TextView)findViewById(R.id.neutral_stat);
		      neutral_status.setText(25+ "%");
		      neutral_status.getPaint().setShader(textShader);
		      neutral_status.setTypeface(myTypeface);
		      neutral_status.setShadowLayer(1, 0, 0, Color.BLACK);
		      
		      TextView bad_status = (TextView)findViewById(R.id.bad_stat);
		      bad_status.setText(25+ "%");
		      bad_status.getPaint().setShader(textShader);
		      bad_status.setTypeface(myTypeface);
		      bad_status.setShadowLayer(1, 0, 0, Color.BLACK);
		  }
		  else if (currentViewId == R.layout.main) // main
		  {
			  finish();
		  }
		  else if (currentViewId == R.layout.result) //result
		  {
			  setCurrentViewById(R.layout.already_voted);
			  Button Button = (Button) findViewById(R.id.button1);
			  Button.setVisibility(View.VISIBLE);
			  Button.setBackgroundColor(Color.TRANSPARENT);
		  }
		  else if (currentViewId == R.layout.stats) //stats
		  {
			  setCurrentViewById(R.layout.vote);
		  }
		  else if (currentViewId == R.layout.vote) //vote
		  {
			  setCurrentViewById(R.layout.main);
		  }
	  }
	    
	  /**
	   * Kontroler przycisku Good <p>
	   * Przycisk Dobrze wysyla glos do serwera <p>
	   * result = (Integer)client.call("cf.androidVote", 1, ID); <p>
	   * Jezeli glos zostal przyjety result przyjmuje wartosc 3 i wyswietlane sa statystyki <p>
	   * Statystyki pobieramy za pomoca <p>
	   * good_proc = (String)client.call("cf.getProcent", 1); <p>
	   * 1=happy, 2=neutral, 3=bad, 4=love 
	   * 
	   * @param  paramView uzywany by dana funkcja mog�a by� wywo�ana przez funkcje OnClick w plikach Layout-XML
	   * @see Good
	   */
	  public void Good(View paramView)
	  {
		  /* Pobieramy secure key z androida i wysylamy zapytanie do serwera XML */
		  String ID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);	
		  Integer result = 0;
		  try 
	  	  {	
			  /*  (Integer)client.call("cf.androidVote", x, ID); 
			   *  wysy�a nazwe funkcji + interesujacy nas g�os "x" + Secure ID danego telefonu
			   *  x przyjmuje wartosci: 1=dobrze, 2=neutralnie, 3=�le, 4=zakochany
			   *  zwraca wartosc Integer "result"
			   *  3=glos oddany, pokazujemy statystyki
			   *  0=glos nieoddany, aplikacja czeka
			   */
			  result = (Integer)client.call("cf.androidVote", 1, ID);
	  	  } catch (XMLRPCException e) {
	  		setCurrentViewById(R.layout.main);
	  	  }
		  
		  /*Je�eli serwer zwroci Nam 3 pokazujemy statystyki*/
		  if (result == 3)
		  {
			  /* Wysylamy zapytanie do servera XML - zwraca Nam % wartosci danego g�osu */
			  String love_proc = "0";
			  String good_proc = "0";
			  String neutral_proc = "0";
			  String bad_proc = "0";
			  try 
		  	  {
				  /* (String)client.call("cf.getProcent", x) 
				   *  wysy�a nazwe funkcji + interesujacy nas g�os "x"
				   *  x przyjmuje wartosci: 1=dobrze, 2=neutralnie, 3=�le, 4=zakochany
				   *  zwraca % wartosci z bazy
				   */
				  love_proc = (String)client.call("cf.getProcent", 4);
				  good_proc = (String)client.call("cf.getProcent", 1);
				  neutral_proc = (String)client.call("cf.getProcent", 2);
				  bad_proc = (String)client.call("cf.getProcent", 3);
		  	  }
			  catch (XMLRPCException e) {
				  setCurrentViewById(R.layout.main);
			  }
			  
			  /* Ustawiamy odpowiedni widok i wypelniamy go % wartosciami (dodajemy Shadery do liter) */
			  setCurrentViewById(R.layout.stats);
			  
			  Shader textShader=
					  new LinearGradient(0, 0, 0, 30,
			            new int[]{Color.WHITE,Color.rgb(206, 206, 206)},
			            new float[]{0, 1}, TileMode.CLAMP);
			  
			  Typeface myTypeface = Typeface.createFromAsset(getAssets(), "font/my1.ttf");

			  TextView love_status = (TextView)findViewById(R.id.love_stat);  
		      love_status.setText(love_proc+ "%");
		      love_status.getPaint().setShader(textShader);
		      love_status.setTypeface(myTypeface);
		      love_status.setShadowLayer(2, 0, 0, Color.BLACK);
		      
		      TextView good_status = (TextView)findViewById(R.id.good_stat);
		      good_status.setText(good_proc+ "%");
		      good_status.getPaint().setShader(textShader);
		      good_status.setTypeface(myTypeface);
		      good_status.setShadowLayer(3, 0, 0, Color.BLACK);
		      
		      TextView neutral_status = (TextView)findViewById(R.id.neutral_stat);
		      neutral_status.setText(neutral_proc+ "%");
		      neutral_status.getPaint().setShader(textShader);
		      neutral_status.setTypeface(myTypeface);
		      neutral_status.setShadowLayer(5, 0, 0, Color.BLACK);
		      
		      TextView bad_status = (TextView)findViewById(R.id.bad_stat);
		      bad_status.setText(bad_proc+ "%");
		      bad_status.getPaint().setShader(textShader);
		      bad_status.setTypeface(myTypeface);
		      bad_status.setShadowLayer(10, 0, 0, Color.BLACK);
		  }
	  }
	  
	  /**
	   * Kontroler przycisku Neutral <p>
	   * Przycisk Neutral wysyla glos do serwera <p>
	   * result = (Integer)client.call("cf.androidVote", 2, ID); <p>
	   * Jezeli glos zostal przyjety result przyjmuje wartosc 3 i wyswietlane sa statystyki <p>
	   * Statystyki pobieramy za pomoca <p>
	   * nuetral_proc = (String)client.call("cf.getProcent", 2); <p>
	   * 1=happy, 2=neutral, 3=bad, 4=love
	   * 
	   * @param  paramView uzywany by dana funkcja mog�a by� wywo�ana przez funkcje OnClick w plikach Layout-XML
	   * @see Neutral 
	   */
	  public void Neutral(View paramView)
	  {
		  String ID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);	
		  Integer result = 0;
		  try 
	  	  {
				result = (Integer)client.call("cf.androidVote", 2, ID);
	  	  } catch (XMLRPCException e) {
	  		setCurrentViewById(R.layout.main);
	  	  }
		  
		  if (result == 3)
		  {
			  String love_proc = "0";
			  String good_proc = "0";
			  String neutral_proc = "0";
			  String bad_proc = "0";
			  try 
		  	  {
				  love_proc = (String)client.call("cf.getProcent", 4);
				  good_proc = (String)client.call("cf.getProcent", 1);
				  neutral_proc = (String)client.call("cf.getProcent", 2);
				  bad_proc = (String)client.call("cf.getProcent", 3);
		  	  }
			  catch (XMLRPCException e) {
				  setCurrentViewById(R.layout.main);
			  }
			  setCurrentViewById(R.layout.stats);
			  
			  Shader textShader=
					  new LinearGradient(0, 0, 0, 30,
			            new int[]{Color.WHITE,Color.rgb(206, 206, 206)},
			            new float[]{0, 1}, TileMode.CLAMP);
			  
			  Typeface myTypeface = Typeface.createFromAsset(getAssets(), "font/my1.ttf");

			  TextView love_status = (TextView)findViewById(R.id.love_stat);  
		      love_status.setText(love_proc+ "%");
		      love_status.getPaint().setShader(textShader);
		      love_status.setTypeface(myTypeface);
		      love_status.setShadowLayer(2, 0, 0, Color.BLACK);
		      
		      TextView good_status = (TextView)findViewById(R.id.good_stat);
		      good_status.setText(good_proc+ "%");
		      good_status.getPaint().setShader(textShader);
		      good_status.setTypeface(myTypeface);
		      good_status.setShadowLayer(3, 0, 0, Color.BLACK);
		      
		      TextView neutral_status = (TextView)findViewById(R.id.neutral_stat);
		      neutral_status.setText(neutral_proc+ "%");
		      neutral_status.getPaint().setShader(textShader);
		      neutral_status.setTypeface(myTypeface);
		      neutral_status.setShadowLayer(5, 0, 0, Color.BLACK);
		      
		      TextView bad_status = (TextView)findViewById(R.id.bad_stat);
		      bad_status.setText(bad_proc+ "%");
		      bad_status.getPaint().setShader(textShader);
		      bad_status.setTypeface(myTypeface);
		      bad_status.setShadowLayer(10, 0, 0, Color.BLACK);
		  }
	  }
	  
	  /**
	   * Kontroler przycisku Bad <p>
	   * Przycisk Bad wysyla glos do serwera <p>
	   * result = (Integer)client.call("cf.androidVote", 3, ID); <p>
	   * Jezeli glos zostal przyjety result przyjmuje wartosc 3 i wyswietlane sa statystyki <p>
	   * Statystyki pobieramy za pomoca <p>
	   * bad_proc = (String)client.call("cf.getProcent", 3); <p>
	   * 1=happy, 2=neutral, 3=bad, 4=love 
	   * 
	   * @param  paramView uzywany by dana funkcja mog�a by� wywo�ana przez funkcje OnClick w plikach Layout-XML
	   * @see Bad 
	   */
	  public void Bad(View view)
	  {
		  String ID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);	
		  Integer result = 0;
		  try 
	  	  {
				result = (Integer)client.call("cf.androidVote", 3, ID);
	  	  } catch (XMLRPCException e) {
	  		setCurrentViewById(R.layout.main);
	  	  }
		  
		  if (result == 3)
		  {
			  String love_proc = "0";
			  String good_proc = "0";
			  String neutral_proc = "0";
			  String bad_proc = "0";
			  try 
		  	  {
				  love_proc = (String)client.call("cf.getProcent", 4);
				  good_proc = (String)client.call("cf.getProcent", 1);
				  neutral_proc = (String)client.call("cf.getProcent", 2);
				  bad_proc = (String)client.call("cf.getProcent", 3);
		  	  }
			  catch (XMLRPCException e) {
				  setCurrentViewById(R.layout.main);
			  }
			  setCurrentViewById(R.layout.stats);
			  
			  Shader textShader=
					  new LinearGradient(0, 0, 0, 30,
			            new int[]{Color.WHITE,Color.rgb(206, 206, 206)},
			            new float[]{0, 1}, TileMode.CLAMP);
			  
			  Typeface myTypeface = Typeface.createFromAsset(getAssets(), "font/my1.ttf");

			  TextView love_status = (TextView)findViewById(R.id.love_stat);  
		      love_status.setText(love_proc+ "%");
		      love_status.getPaint().setShader(textShader);
		      love_status.setTypeface(myTypeface);
		      love_status.setShadowLayer(2, 0, 0, Color.BLACK);
		      
		      TextView good_status = (TextView)findViewById(R.id.good_stat);
		      good_status.setText(good_proc+ "%");
		      good_status.getPaint().setShader(textShader);
		      good_status.setTypeface(myTypeface);
		      good_status.setShadowLayer(3, 0, 0, Color.BLACK);
		      
		      TextView neutral_status = (TextView)findViewById(R.id.neutral_stat);
		      neutral_status.setText(neutral_proc+ "%");
		      neutral_status.getPaint().setShader(textShader);
		      neutral_status.setTypeface(myTypeface);
		      neutral_status.setShadowLayer(5, 0, 0, Color.BLACK);
		      
		      TextView bad_status = (TextView)findViewById(R.id.bad_stat);
		      bad_status.setText(bad_proc+ "%");
		      bad_status.getPaint().setShader(textShader);
		      bad_status.setTypeface(myTypeface);
		      bad_status.setShadowLayer(10, 0, 0, Color.BLACK);
		  }
	  }

	  /**
	   * Kontroler przycisku Love <p>
	   * Przycisk Love wysyla glos do serwera <p>
	   * result = (Integer)client.call("cf.androidVote", 4, ID); <p>
	   * Jezeli glos zostal przyjety result przyjmuje wartosc 3 i wyswietlane sa statystyki <p>
	   * Statystyki pobieramy za pomoca <p>
	   * love_proc = (String)client.call("cf.getProcent", 4); <p>
	   * 1=happy, 2=neutral, 3=bad, 4=love 
	   * 
	   * @param  paramView uzywany by dana funkcja mog�a by� wywo�ana przez funkcje OnClick w plikach Layout-XML
	   * @see Love 
	   */	  
	  public void Love(View paramView)
	  {
		  String ID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);	
		  Integer result = 0;
		  try 
	  	  {
				result = (Integer)client.call("cf.androidVote", 4, ID);
	  	  } catch (XMLRPCException e) {
	  		  setCurrentViewById(R.layout.main);
	  	  }
		  
		  if (result == 3)
		  {
			  String love_proc = "0";
			  String good_proc = "0";
			  String neutral_proc = "0";
			  String bad_proc = "0";
			  try 
		  	  {
				  love_proc = (String)client.call("cf.getProcent", 4);
				  good_proc = (String)client.call("cf.getProcent", 1);
				  neutral_proc = (String)client.call("cf.getProcent", 2);
				  bad_proc = (String)client.call("cf.getProcent", 3);
		  	  }
			  catch (XMLRPCException e) {
				  setCurrentViewById(R.layout.main);
			  }
			  setCurrentViewById(R.layout.stats);
			  
			  Shader textShader=
					  new LinearGradient(0, 0, 0, 30,
			            new int[]{Color.WHITE,Color.rgb(206, 206, 206)},
			            new float[]{0, 1}, TileMode.CLAMP);
			  
			  Typeface myTypeface = Typeface.createFromAsset(getAssets(), "font/my1.ttf");

			  TextView love_status = (TextView)findViewById(R.id.love_stat);  
		      love_status.setText(love_proc+ "%");
		      love_status.getPaint().setShader(textShader);
		      love_status.setTypeface(myTypeface);
		      love_status.setShadowLayer(2, 0, 0, Color.BLACK);
		      
		      TextView good_status = (TextView)findViewById(R.id.good_stat);
		      good_status.setText(good_proc+ "%");
		      good_status.getPaint().setShader(textShader);
		      good_status.setTypeface(myTypeface);
		      good_status.setShadowLayer(3, 0, 0, Color.BLACK);
		      
		      TextView neutral_status = (TextView)findViewById(R.id.neutral_stat);
		      neutral_status.setText(neutral_proc+ "%");
		      neutral_status.getPaint().setShader(textShader);
		      neutral_status.setTypeface(myTypeface);
		      neutral_status.setShadowLayer(5, 0, 0, Color.BLACK);
		      
		      TextView bad_status = (TextView)findViewById(R.id.bad_stat);
		      bad_status.setText(bad_proc+ "%");
		      bad_status.getPaint().setShader(textShader);
		      bad_status.setTypeface(myTypeface);
		      bad_status.setShadowLayer(10, 0, 0, Color.BLACK);
		  }
	  }

	  /**
	   * Kontroler przycisku Start <p>
	   * Przycisk Start sprawdza czy istnieje polaczenie internetowe, <p>
	   * Jezeli istnieje przechodzi do dalszej czesci aplikacji <p>
	   * Sprawdzane jest rowniez czy dana osoba glosowala juz dzis <p>
	   * result = (Integer)client.call("cf.userExistAndroid", ID); <p>
	   * Oraz godzina z Serwera XML RPC <p>
	   * time = (Integer)client.call("cf.checkTime"); <p>
	   * W zaleznosci od wartosci result i time taki widok mamy wyswietlany <p>
	   *
	   * @param  paramView uzywany by dana funkcja mog�a by� wywo�ana przez funkcje OnClick w plikach Layout-XML
	   * @see Start 
	   */
	  public void Start(View paramView)
	  {
		  /* sprawdzamy czy istnieje po�aczenie internetowe */
		  boolean connected = false;
		  ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		  if (connectivityManager != null)
		  {
			  if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null)
			  {
				  if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
				  {
					  connected = true;
				  }
			  }
			  
			  if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null)
			  {
				  if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
				  {
					  connected = true;
				  }
			  }	  
		  }
		  
		  if (connected == false)
		  {
			  /* Brak po�aczenia = Alert i brak moznosci dalszego uzywania aplikacji */
			  AlertDialog.Builder dialogBuilder
			  = new AlertDialog.Builder(this)
			  .setTitle("ALERT")
			  .setMessage("This Application Requires An Active Internet Connection.");
			  dialogBuilder.create().show();  
		  }
		  else
		  {
		  
			  /* Je�eli mamy polaczenie wysy�amy zapytanie do serwera XML */
			  int time = 0;
			  Integer result = 0;
			  String ID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
			  try 
			  {
				  /* (Integer)client.call("cf.userExistAndroid", ID);
				   *  sprawdzamy czy komorka o danym Secure ID glosowala juz dzis
				   *  1=juz dzis glosowal
				   *  0=nie glosowal dzis
				   */
				  result = (Integer)client.call("cf.userExistAndroid", ID);
			  }
			  catch (XMLRPCException e) {
				  setCurrentViewById(R.layout.main);
			  }
		  
			  try 
		  	  {
				  /* (Integer)client.call("cf.checkTime");
				   *  sprawdzamy czas
				   *  1=po czasie, wyswietlamy wyniki
				   *  0=glosowanie trwa, 
				   *  w zaleznosci czy glosowal czy nie wysylamy do odpowiedniego okienka
				   */
				  time = (Integer)client.call("cf.checkTime");
		  	  }
			  catch (XMLRPCException e) {
				  setCurrentViewById(R.layout.main);
			  }
			  
			  if (time == 1)
			  {
				  int highest = 0;
				  try 
			  	  {
					  /* (Integer)client.call("cf.getHighestVote");
					   *  sprawdzamy kt�ry smok dostal najwiecej glosow
					   *  1=happy
					   *  2=neutral
					   *  3=bad
					   *  4=love
					   */
					  highest = (Integer)client.call("cf.getHighestVote");

			  	  }
				  catch (XMLRPCException e) {
					  setCurrentViewById(R.layout.main);
				  }
				  
				  setCurrentViewById(R.layout.result);
				  if (highest == 1)
				  {
					  ImageView img = (ImageView) findViewById(R.id.dragon_result);
					  img.setImageResource(R.drawable.result_dragon_happy);
				  }
				  else if (highest == 2)
				  {
					  ImageView img = (ImageView) findViewById(R.id.dragon_result);
					  img.setImageResource(R.drawable.result_dragon_neutrozaur);
				  }
				  else if (highest == 3)
				  {
					  ImageView img = (ImageView) findViewById(R.id.dragon_result);
					  img.setImageResource(R.drawable.result_dragon_furiatus);
				  }
				  else if (highest == 4)
				  {
					  ImageView img = (ImageView) findViewById(R.id.dragon_result);
					  img.setImageResource(R.drawable.result_dragon_napalonozaur);
				  }
			  }
			  else if (result == 1)
			  {
				  setCurrentViewById(R.layout.vote);
				  //setCurrentViewById(R.layout.already_voted);
			  }
			  else
				  setCurrentViewById(R.layout.vote);
		  }
	  }
	  
	  public void GoResult(View paramView)
	  {
		  setCurrentViewById(R.layout.result);
		  Button Button = (Button) findViewById(R.id.button1);
		  Button.setVisibility(View.VISIBLE);
		  Button.setBackgroundColor(Color.TRANSPARENT);
	  }
	  
	  public void ChangeResult(View paramView)
	  {
		  setCurrentViewById(R.layout.result);
		  Button Button = (Button) findViewById(R.id.button1);
		  Button.setVisibility(View.VISIBLE);
		  Button.setBackgroundColor(Color.TRANSPARENT);
		  if (picture == 1)
		  {
			  ImageView img = (ImageView) findViewById(R.id.dragon_result);
			  img.setImageResource(R.drawable.result_dragon_happy);
		  }
		  else if (picture == 2)
		  {
			  ImageView img = (ImageView) findViewById(R.id.dragon_result);
			  img.setImageResource(R.drawable.result_dragon_neutrozaur);
		  }
		  else if (picture == 3)
		  {
			  ImageView img = (ImageView) findViewById(R.id.dragon_result);
			  img.setImageResource(R.drawable.result_dragon_furiatus);
		  }
		  else if (picture == 4)
		  {
			  ImageView img = (ImageView) findViewById(R.id.dragon_result);
			  img.setImageResource(R.drawable.result_dragon_napalonozaur);
		  }
		  picture += 1;
		  if (picture > 4)
			  picture = 1;
	  }
	  
	  public void ChangeResultBig(View paramView)
	  {
		  setCurrentViewById(R.layout.result);
		  Button Button = (Button) findViewById(R.id.button1);
		  Button.setVisibility(View.VISIBLE);
		  Button.setBackgroundColor(Color.TRANSPARENT);
		  if (picture == 1)
		  {
			  ImageView img = (ImageView) findViewById(R.id.dragon_result);
			  img.setImageResource(R.drawable.big_smok_zly);
			  ImageView img2 = (ImageView) findViewById(R.id.imageGolebie);
			  if (img2 != null)
				  img2.setImageResource(R.drawable.big_golebie_smok_zly );
		  }
		  else if (picture == 2)
		  {
			  ImageView img = (ImageView) findViewById(R.id.dragon_result);
			  img.setImageResource(R.drawable.big_smok_napalony);
		  }
		  else if (picture == 3)
		  {
			  ImageView img = (ImageView) findViewById(R.id.dragon_result);
			  img.setImageResource(R.drawable.big_smok_neutral);
		  }
		  else if (picture == 4)
		  {
			  ImageView img = (ImageView) findViewById(R.id.dragon_result);
			  img.setImageResource(R.drawable.big_smok_szczesliwy);
		  }
		  picture += 1;
		  if (picture > 4)
			  picture = 1;
	  }
	  /**
	   * Kontroler przycisku Refresh <p>
	   * Przycisk Refresh jest uzywany w okienku z statystykami, <p>
	   * wysylane jest ponownie zapytanie <p>
	   * (String)client.call("cf.getProcent", 4); <p>
	   * i wartosci % sa odswiezane
	   * 
	   * @param  paramView uzywany by dana funkcja mog�a by� wywo�ana przez funkcje OnClick w plikach Layout-XML
	   * @see Refresh 
	   */
	  public void Refresh(View paramView)
	  {
		  String love_proc = "0";
		  String good_proc = "0";
		  String neutral_proc = "0";
		  String bad_proc = "0";
		  try 
	  	  {
			  love_proc = (String)client.call("cf.getProcent", 4);
			  good_proc = (String)client.call("cf.getProcent", 1);
			  neutral_proc = (String)client.call("cf.getProcent", 2);
			  bad_proc = (String)client.call("cf.getProcent", 3);
	  	  }
		  catch (XMLRPCException e) {
			  setCurrentViewById(R.layout.main);
		  }
	  
		  Shader textShader=
				    new LinearGradient(0, 0, 0, 15,
				    new int[]{Color.WHITE,Color.rgb(206, 206, 206)},
		            new float[]{0, 1}, TileMode.CLAMP);
		  
		  TextView love_status = (TextView)findViewById(R.id.love_stat);  
	      love_status.setText(love_proc+ "%");
	      love_status.getPaint().setShader(textShader);
	      
	      TextView good_status = (TextView)findViewById(R.id.good_stat);
	      good_status.setText(good_proc+ "%");
	      good_status.getPaint().setShader(textShader);
	      
	      TextView neutral_status = (TextView)findViewById(R.id.neutral_stat);
	      neutral_status.setText(neutral_proc+ "%");
	      neutral_status.getPaint().setShader(textShader);
	      
	      TextView bad_status = (TextView)findViewById(R.id.bad_stat);
	      bad_status.setText(bad_proc+ "%");
	      bad_status.getPaint().setShader(textShader);
	  }

	  /**
	   * Kontroler przycisku Dalej <p>
	   * Przycisk Dalej przerzuca Nas albo do okienka z oczekiwaniem albo do okienka z wynikami (w zaleznosci ktora jest godzina). <p>
	   * Godzine sprawdza serwer XML  <p>
	   * (Integer)client.call("cf.checkTime");
	   * 
	   * @param  paramView uzywany by dana funkcja mog�a by� wywo�ana przez funkcje OnClick w plikach Layout-XML
	   * @see Dalej
	   */
	  public void Dalej(View paramView)
	  {
		  int time = 0;
		  
		  try 
	  	  {
			  time = (Integer)client.call("cf.checkTime");
	  	  }
		  catch (XMLRPCException e) {
			  setCurrentViewById(R.layout.main);
		  }
		  
		  if (time == 1)
		  {
			  int highest = 0;
			  try 
		  	  {
				  highest = (Integer)client.call("cf.getHighestVote");

		  	  }
			  catch (XMLRPCException e) {
				  setCurrentViewById(R.layout.main);
			  }
			  
			  setCurrentViewById(R.layout.result);
			  if (highest == 1)
			  {
				  ImageView img = (ImageView) findViewById(R.id.dragon_result);
				  img.setImageResource(R.drawable.result_dragon_happy);
			  }
			  else if (highest == 2)
			  {
				  ImageView img = (ImageView) findViewById(R.id.dragon_result);
				  img.setImageResource(R.drawable.result_dragon_neutrozaur);
			  }
			  else if (highest == 3)
			  {
				  ImageView img = (ImageView) findViewById(R.id.dragon_result);
				  img.setImageResource(R.drawable.result_dragon_furiatus);
			  }
			  else if (highest == 4)
			  {
				  ImageView img = (ImageView) findViewById(R.id.dragon_result);
				  img.setImageResource(R.drawable.result_dragon_napalonozaur);
			  }
		  }
		  else
		  {
			  setCurrentViewById(R.layout.already_voted);
			  Button Button = (Button) findViewById(R.id.button1);
			  Button.setVisibility(View.VISIBLE);
			  Button.setBackgroundColor(Color.TRANSPARENT);
		  }
	  }
	  
	  /**
	   * Kontroler przycisku Stats <p>
	   * Przycisk Stats przerzuca nas z do widoku z statystykami i odrazu nastepuje ponowny refresh statystyk <p>
	   * 
	   * @param  paramView uzywany by dana funkcja mog�a by� wywo�ana przez funkcje OnClick w plikach Layout-XML
	   * @see Stats
	   */
	  public void Stats(View paramView)
	  {
		  String love_proc = "0";
		  String good_proc = "0";
		  String neutral_proc = "0";
		  String bad_proc = "0";
		  try 
	  	  {
			  love_proc = (String)client.call("cf.getProcent", 4);
			  good_proc = (String)client.call("cf.getProcent", 1);
			  neutral_proc = (String)client.call("cf.getProcent", 2);
			  bad_proc = (String)client.call("cf.getProcent", 3);
	  	  }
		  catch (XMLRPCException e) {
			  setCurrentViewById(R.layout.main);
		  }
		  
		  setCurrentViewById(R.layout.stats);
		  
		  Shader textShader=
				    new LinearGradient(0, 0, 0, 15,
				    new int[]{Color.WHITE,Color.rgb(206, 206, 206)},
		            new float[]{0, 1}, TileMode.CLAMP);
		  
		  TextView love_status = (TextView)findViewById(R.id.love_stat);  
	      love_status.setText(love_proc+ "%");
	      love_status.getPaint().setShader(textShader);
	      
	      TextView good_status = (TextView)findViewById(R.id.good_stat);
	      good_status.setText(good_proc+ "%");
	      good_status.getPaint().setShader(textShader);
	      
	      TextView neutral_status = (TextView)findViewById(R.id.neutral_stat);
	      neutral_status.setText(neutral_proc+ "%");
	      neutral_status.getPaint().setShader(textShader);
	      
	      TextView bad_status = (TextView)findViewById(R.id.bad_stat);
	      bad_status.setText(bad_proc+ "%");
	      bad_status.getPaint().setShader(textShader);
	  }
	  	  
	  
	  public void setCurrentViewById(int id)
	  {
		  setContentView(id);
	      currentViewId = id;
	  }

	  public int getCurrentViewById()
	  {
		  return currentViewId;
	  }
}