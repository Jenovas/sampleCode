package panel.dzienny;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import database.entities.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserGenerateRaportActivity extends Activity {

	private static final String MyPREFERENCES = "session";
	private static final String panelID = "panelIdKey";
	private SharedPreferences sharedpreferences;
	private long panelid;
	
	private static ProgressDialog dialog;
    private static UserGenerateRaportActivity _context;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_context = UserGenerateRaportActivity.this;
		sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
		panelid = sharedpreferences.getLong(panelID, 0);

		// Delete all existing records for raports with given panelid
		List<Raports> DeleteRaportList = null;
		DeleteRaportList = Raports.find(Raports.class, "panelid = ?", ""
				+ panelid);
		for (int i = 0; i < DeleteRaportList.size(); i++) {
			DeleteRaportList.get(i).delete();
		}

		List<RaportParticipant> DeleteParticipantList = null;
		DeleteParticipantList = RaportParticipant.find(RaportParticipant.class,
				"panelid = ?", "" + panelid);
		for (int i = 0; i < DeleteParticipantList.size(); i++) {
			DeleteParticipantList.get(i).delete();
		}

		// Create Raport
		Raports raport = new Raports(UserGenerateRaportActivity.this, panelid,
				"Raport " + panelid, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
						new Date()).toString());
		raport.save();
		// Creating Raport Participant
		List<Members> MembersList = null;
		MembersList = Members.find(Members.class, "panelid = ?", "" + panelid);
		for (int i = 0; i < MembersList.size(); i++) {
			RaportParticipant participant = new RaportParticipant(
					UserGenerateRaportActivity.this, panelid, raport.getId(),
					MembersList.get(i).fullname, MembersList.get(i).mail,
					MembersList.get(i).departmentID, MembersList.get(i).departmentName);
			participant.save();
		}

		List<RaportProduct> ProductList = null;
        ProductList = RaportProduct.find(RaportProduct.class,
                "panelid = ?", "" + panelid);
        if (ProductList.isEmpty())
            finish();
        else
            new SendRaport().execute();
	}
	
	static class hHandler extends Handler {
        public final WeakReference<UserGenerateRaportActivity> mTarget;

        hHandler(UserGenerateRaportActivity target) {
            mTarget = new WeakReference<UserGenerateRaportActivity>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            _context.finish();
        }
    }

    private void runThread(final int x, final int y) {
        runOnUiThread(new Thread(new Runnable() {
            public void run() {
                dialog.setMessage((x + 1) + "/" + y);
                dialog.setProgress((100*(x+1))/y);
            }
        }));
    }
    
	private class SendRaport extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
		    List<RaportProduct> ProductList = null;
            ProductList = RaportProduct.find(RaportProduct.class,
                    "panelid = ?", "" + panelid);
            if (!ProductList.isEmpty()) {
                dialog = new ProgressDialog(UserGenerateRaportActivity.this);
                dialog.setTitle("Wysyłanie Raportów");
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setMax(100);
                dialog.setMessage("");
                dialog.setCancelable(false);
                dialog.show();
            }
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(client.getParams(),
						10000); // Timeout Limit
				HttpResponse response;
				JSONObject json = new JSONObject();
				String adres = "";
                List<ApkData> apkData = ApkData.listAll(ApkData.class);
                adres = apkData.get(0).serveradres;
                HttpPost post = new HttpPost(adres);
				json.put("method", "raport.send");
				json.put("id", "0");
				json.put("jsonrpc", "2.0");
				System.out.println(3);
				JSONObject raportObject = new JSONObject();
				raportObject.put("name", "Daily Panel Report " + new SimpleDateFormat("yyyy-MM-dd").format(
                        new Date()));
				List<Panels> panelsList;
		        panelsList = Panels.find(Panels.class, "id = ?",
		                "" + sharedpreferences.getLong(panelID, 0));
		        if (!panelsList.isEmpty())
		        {
		            raportObject.put("period", panelsList.get(0).period);
		        }
		        else
		            raportObject.put("period", 0);
				
				raportObject.put("date", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(
						new Date()));
				/*** Participant Array ***/
				List<RaportParticipant> ParticipantList = null;
				ParticipantList = RaportParticipant.find(RaportParticipant.class,
						"panelid = ?", "" + panelid);
				JSONArray participantArray = new JSONArray();
				for (int i = 0; i < ParticipantList.size(); i++)
				{
					JSONObject participantObject = new JSONObject();
					participantObject.put("fullname", ParticipantList.get(i).fullname);
					participantObject.put("mail", ParticipantList.get(i).mail);
					participantObject.put("department", ParticipantList.get(i).departmentname);
					participantArray.put(participantObject);
				}
				System.out.println(4);
				raportObject.put("participants", participantArray);
				/********************************************************/

                /*** Product Array ***/
				List<RaportProduct> ProductList = null;
				ProductList = RaportProduct.find(RaportProduct.class,
						"panelid = ?", "" + panelid);
				JSONArray productArray = new JSONArray();
				for (int i = 0; i < ProductList.size(); i++)
				{
				    
					JSONObject productObject = new JSONObject();
					productObject.put("code", ""+ProductList.get(i).code);
					productObject.put("name", ProductList.get(i).name);
					productObject.put("bag_code", ""+ProductList.get(i).bagcode);
					productObject.put("line", ""+ProductList.get(i).line);
					Formulas formula = ProductList.get(i).formula;
					productObject.put("formula_id", ""+formula.formulacode);
					productObject.put("size", ProductList.get(i).size);
					List<RaportProductPack> ProductPackList = null;
					ProductPackList = RaportProductPack.find(RaportProductPack.class,
							"panelid = ? AND mainid = ?", "" + panelid, ""+ ProductList.get(i).mainid);
					productObject.put("lot", ProductPackList.get(0).lot);
					if (!ProductPackList.isEmpty())
					{
						productObject.put("hour", ""+ProductPackList.get(0).hour + ":" + ProductPackList.get(0).minute);
						JSONObject productPackObject = new JSONObject();
						productPackObject.put("weight_test", ProductPackList.get(0).weighttest);
						productPackObject.put("drop_test", ""+ProductPackList.get(0).droptest);
						productPackObject.put("sealing_test", ""+ProductPackList.get(0).sealingtest);
						productPackObject.put("sew_quality", ""+ProductPackList.get(0).sewquality);
						productPackObject.put("printing_quality", ""+ProductPackList.get(0).printingquality);
						productPackObject.put("oxygen_test", ""+ProductPackList.get(0).oxygentest);
						productObject.put("product_pack", productPackObject);
					}
					List<RaportProductDetail> ProductDetailList = null;
					ProductDetailList = RaportProductDetail.find(RaportProductDetail.class,
							"panelid = ? AND mainid = ?", "" + panelid, ""+ ProductList.get(i).mainid);
					JSONObject productDetailObject = new JSONObject();
					if (!ProductDetailList.isEmpty())
					{
						productDetailObject.put("foreign_bodies", ProductDetailList.get(0).foreignbodies);
						productDetailObject.put("mix_prduct", ""+ProductDetailList.get(0).mixproduct);
						productDetailObject.put("diameter", ""+ProductDetailList.get(0).diameter);
						productDetailObject.put("thickness", ""+ProductDetailList.get(0).thickness);
						productDetailObject.put("appearance", ""+ProductDetailList.get(0).color);
						productDetailObject.put("oil_sticky", ""+ProductDetailList.get(0).oilysticky);
						productDetailObject.put("broken_kibbles", ""+ProductDetailList.get(0).brokenkibbles);
						productDetailObject.put("fines", ""+ProductDetailList.get(0).fines);
						productDetailObject.put("density", ""+ProductDetailList.get(0).density);
						productObject.put("product_detail", productDetailObject);
					}
					productObject.put("product_no_conform", ProductList.get(i).status);
					
					List<RaportProductImage> ProductImageList = null;
					ProductImageList = RaportProductImage.find(RaportProductImage.class,
							"panelid = ? AND raportproductid = ?", "" + panelid, ""+ ProductList.get(i).mainid);
					JSONArray productImageArray = new JSONArray();
					for (int i2 = 0; i2 < ProductImageList.size(); i2++)
					{
						JSONObject productImageObject = new JSONObject();
						productImageObject.put("product_type", ProductImageList.get(i2).producttype);
						productImageObject.put("image", ImageToBase64(ProductImageList.get(i2).image));
						System.out.println(ProductImageList.get(i2).image);
						productImageArray.put(productImageObject);
					}
					productObject.put("images", productImageArray);
					List<RaportProductComment> ProductCommentList = null;
					ProductCommentList = RaportProductComment.find(RaportProductComment.class,
							"panelid = ? AND raportproductid = ?", "" + panelid, ""+ ProductList.get(i).mainid);
					JSONArray productCommentArray = new JSONArray();
					for (int i2 = 0; i2 < ProductCommentList.size(); i2++)
					{
						JSONObject productCommentObject = new JSONObject();
						productCommentObject.put("product_type", ""+ProductCommentList.get(i2).producttype);
						productCommentObject.put("comment", ProductCommentList.get(i2).comment);
						productCommentArray.put(productCommentObject);
					}
					productObject.put("comments", productCommentArray);
					List<RaportProductMeasure> MeasureList = null;
					MeasureList = RaportProductMeasure.find(RaportProductMeasure.class,
							"panelid = ? AND productid = ?", "" + panelid, ""+ ProductList.get(i).mainid);
					JSONArray measurementArray = new JSONArray();
					for (int i2 = 0; i2 < MeasureList.size(); i2++)
					{
						JSONObject productMeasureObject = new JSONObject();
						productMeasureObject.put("d1", MeasureList.get(i2).d);
						productMeasureObject.put("d2", MeasureList.get(i2).dd);
						productMeasureObject.put("cl", MeasureList.get(i2).cl);
						measurementArray.put(productMeasureObject);
					}
					productObject.put("measurement", measurementArray);
					List<RaportProductMeasureAVGDEV> MeasureAVGDEVList = null;
					MeasureAVGDEVList = RaportProductMeasureAVGDEV.find(RaportProductMeasureAVGDEV.class,
							"panelid = ? AND productid = ?", "" + panelid, ""+ ProductList.get(i).mainid);
					JSONObject measurementAVGDEVObject = new JSONObject();
					if (!MeasureAVGDEVList.isEmpty())
					{
						measurementAVGDEVObject.put("avg_d1", MeasureAVGDEVList.get(0).davg);
						measurementAVGDEVObject.put("avg_d2", MeasureAVGDEVList.get(0).ddavg);
						measurementAVGDEVObject.put("avg_cl", MeasureAVGDEVList.get(0).clavg);
						measurementAVGDEVObject.put("dev_d1", MeasureAVGDEVList.get(0).ddev);
						measurementAVGDEVObject.put("dev_d2", MeasureAVGDEVList.get(0).dddev);
						measurementAVGDEVObject.put("dev_cl", MeasureAVGDEVList.get(0).cldev);
						productObject.put("measurement_dev", measurementAVGDEVObject);
					}
					else
					{
						measurementAVGDEVObject.put("avg_d1", ""+0);
						measurementAVGDEVObject.put("avg_d2", ""+0);
						measurementAVGDEVObject.put("avg_cl", ""+0);
						measurementAVGDEVObject.put("dev_d1", ""+0);
						measurementAVGDEVObject.put("dev_d2", ""+0);
						measurementAVGDEVObject.put("dev_cl", ""+0);
						productObject.put("measurement_dev", measurementAVGDEVObject);
					}
					productArray.put(productObject);
					runThread(i, ProductList.size());
				}
				raportObject.put("products", productArray);
				/********************************************************/

				JSONObject raport2Object = new JSONObject();
				raport2Object.put("raport", raportObject);
				JSONObject dataObject = new JSONObject();
				dataObject.put("data", raport2Object);
				json.put("params", dataObject);
				System.out.println(json.toString());
				StringEntity se = new StringEntity(json.toString(), "UTF-8");
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				post.setEntity(se);
				response = client.execute(post);

				/* Checking response */
				if (response != null) {
					BufferedReader rd = new BufferedReader(
							new InputStreamReader(response.getEntity()
									.getContent()));
					String line = "";
					ArrayList<JSONObject> JSONList = new ArrayList<JSONObject>();
					while ((line = rd.readLine()) != null) {
						System.out.println(line);
						JSONObject obj = new JSONObject(line);
						JSONList.add(obj);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		public String ImageToBase64(String destination) throws FileNotFoundException
		{
			if (destination.equals(""))
				return "";
			
			System.out.println("ImageToBase64 LOADING!");
			FileInputStream in = new FileInputStream(destination);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			Bitmap bmp = BitmapFactory.decodeStream(in, null, options);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			byte [] byte_arr = stream.toByteArray();
			return Base64.encodeToString(byte_arr, Base64.DEFAULT);
		}
		
		@Override
		protected void onPostExecute(Void result) {
		    final hHandler h = new hHandler(UserGenerateRaportActivity.this);
            h.sendMessageDelayed(new Message(), 1000);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menupanel, menu);
		// Set up your ActionBar
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent newIntent;
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.menu_mainmenu:
			newIntent = new Intent().setClass(UserGenerateRaportActivity.this,
					UserMainMenuActivity.class);
			newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(newIntent);
			finish();
			return true;
		case R.id.menu_panelmenu:
			newIntent = new Intent().setClass(UserGenerateRaportActivity.this,
					UserNewPanelActivity.class);
			newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(newIntent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
