package rysuj.w.piasku;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.stay.gif.GifEncoder;


public class GiFActivity extends Activity {

	int size;
	int i;
	ProgressDialog dialog;
	GifEncoder giffle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		if (isExternalStoragePresent())
		{
			size = 0;
			i = 0;
			SearchForGiF();

			Bundle extras = getIntent().getExtras(); 
			if (extras != null) {
				size = extras.getInt("size");
			}
			new CreateGif().execute();	
		}

	}

	private class CreateGif extends AsyncTask<Void, Integer, Void>{

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(GiFActivity.this);
			dialog.setTitle("Creating GiF...");
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setMax(100);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			encode(0, 2);
			publishProgress(10);
			encode(3, 5);
			publishProgress(20);
			encode(6, 8);
			publishProgress(30);
			encode(9, 11);
			publishProgress(40);
			encode(12, 14);
			publishProgress(50);
			encode(15, 17);
			publishProgress(60);
			encode(18, 20);
			publishProgress(70);
			encode(21, 23);
			publishProgress(80);
			encode(24, 26);
			publishProgress(90);
			encode(27, 31);
			encodeSmall();
			publishProgress(100);
			Display();
			return null;
		}

		@Override
		protected void onProgressUpdate(final Integer... values) {
			if (GifEncoder.getSharedInstance() != null)
				dialog.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(Void result) {
			dialog.dismiss();
			DrawingActivity.getSharedInstance().finish();
			finish();
		}
	}

	public void Display()
	{
		Intent newIntent;
		newIntent = new Intent().setClass(GiFActivity.this,GifDisplayActivity.class);
		newIntent.putExtra("i", i);
		startActivity(newIntent);
	}

	public void encode(int start, int stop) {

		if (start == 0)
			giffle = new GifEncoder();
		giffle.encode(GiFActivity.this, start, stop, size, 350, Environment.getExternalStorageDirectory()+"/rysuj/mygif"+i+".gif");
		if (stop == 31)
			giffle.close();
	}

	public void encodeSmall() {
		giffle = new GifEncoder();
		//Log.d("DEBUG", "SIZE 1 = "+size);
		giffle.encodeSmall(GiFActivity.this, size, 350, Environment.getExternalStorageDirectory()+"/rysuj/mygifSmall"+i+".gif");
		giffle.close();
	}

	private void SearchForGiF() {
		if (isExternalStoragePresent())
		{
			i++;
			// Create Folder if donesnt exist
			File dir = new File(Environment.getExternalStorageDirectory(), "/rysuj/gif");
			if (!dir.exists()) 
				dir.mkdirs();
			String fileName = "/mygif"+i+".gif";
			File nextFile = new File(dir, fileName);
			if (nextFile.exists())
				SearchForGiF();
		}
	}

	private boolean isExternalStoragePresent() {

		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// Something else is wrong. It may be one of many other states, but
			// all we need
			// to know is we can neither read nor write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		if (!((mExternalStorageAvailable) && (mExternalStorageWriteable))) {
			Toast.makeText(this, "SD card not present", Toast.LENGTH_LONG)
			.show();

		}
		return (mExternalStorageAvailable) && (mExternalStorageWriteable);
	}
}
