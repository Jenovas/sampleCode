package rysuj.w.piasku;

import java.io.File;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class DrawingActivity extends Activity {

	static ProgressBar Bar;
	int visible;
	static DrawingActivity instance;
	public static DrawingActivity getSharedInstance() {
		return instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		visible = 0;
		setContentView(R.layout.main); 
		DrawingView mDrawingView=new DrawingView(this); 
		LinearLayout mDrawingPad=(LinearLayout)findViewById(R.id.view_drawing_pad);
		mDrawingPad.addView(mDrawingView);
		Bar=(ProgressBar)findViewById(R.id.progressBar1);
		Bar.setMax(100);
		setColours(Bar, 
				Color.rgb(171, 171, 171),   //bgCol1  grey 
				Color.rgb(171, 171, 171),   //bgCol2  grey 
				Color.rgb(238, 133, 0),   //fg1Col1 orange 
				Color.rgb(238, 133, 0),   //fg1Col2 orange
				0,           //value1
				Color.rgb(238, 133, 0),   //fg2Col1 orange 
				Color.rgb(238, 133, 0),   //fg2Col2 orange
				0);
	}

	public void setColours(ProgressBar progressBar,
			int bgCol1, int bgCol2, 
			int fg1Col1, int fg1Col2, int value1,
			int fg2Col1, int fg2Col2, int value2)
	{

		GradientDrawable.Orientation fgGradDirection
		= GradientDrawable.Orientation.TOP_BOTTOM;
		GradientDrawable.Orientation bgGradDirection
		= GradientDrawable.Orientation.TOP_BOTTOM;

		//Background
		GradientDrawable bgGradDrawable = new GradientDrawable(
				bgGradDirection, new int[]{bgCol1, bgCol2});
		bgGradDrawable.setShape(GradientDrawable.RECTANGLE);
		bgGradDrawable.setCornerRadius(5);
		ClipDrawable bgclip = new ClipDrawable(
				bgGradDrawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);     
		bgclip.setLevel(10000);

		//SecondaryProgress
		GradientDrawable fg2GradDrawable = new GradientDrawable(
				fgGradDirection, new int[]{fg2Col1, fg2Col2});
		fg2GradDrawable.setShape(GradientDrawable.RECTANGLE);
		fg2GradDrawable.setCornerRadius(5);
		ClipDrawable fg2clip = new ClipDrawable(
				fg2GradDrawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);        

		//Progress
		GradientDrawable fg1GradDrawable = new GradientDrawable(
				fgGradDirection, new int[]{fg1Col1, fg1Col2});
		fg1GradDrawable.setShape(GradientDrawable.RECTANGLE);
		fg1GradDrawable.setCornerRadius(5);
		ClipDrawable fg1clip = new ClipDrawable(
				fg1GradDrawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);        

		//Setup LayerDrawable and assign to progressBar
		Drawable[] progressDrawables = {bgclip, fg2clip, fg1clip};
		LayerDrawable progressLayerDrawable = new LayerDrawable(progressDrawables);     
		progressLayerDrawable.setId(0, android.R.id.background);
		progressLayerDrawable.setId(1, android.R.id.secondaryProgress);
		progressLayerDrawable.setId(2, android.R.id.progress);

		//Copy the existing ProgressDrawable bounds to the new one.
		Rect bounds = progressBar.getProgressDrawable().getBounds();
		progressBar.setProgressDrawable(progressLayerDrawable);     
		progressBar.getProgressDrawable().setBounds(bounds);

		progressBar.setProgress(value1);
		progressBar.setSecondaryProgress(value2);

		//now force a redraw
		progressBar.invalidate();
	}

	public void Save(View paramView)
	{
		if (Bar.getProgress() < 100)
			return;

		Intent newIntent;
		newIntent = new Intent().setClass(this, GiFActivity.class);
		newIntent.putExtra("size", DrawingView.getSharedInstance().bitmapArray.size());
		startActivity(newIntent);
	}

	public void Restart(View paramView)
	{
		if (DrawingView.getSharedInstance().task != null)
			DrawingView.getSharedInstance().task.cancel();
		Intent newIntent;
		newIntent = new Intent().setClass(this, DrawingActivity.class);
		startActivity(newIntent);
		finish();
	}

	public void Resume(View paramView)
	{
		RelativeLayout HiddenLayout=(RelativeLayout)findViewById(R.id.RealLayoutHidden);
		Button HiddenResume=(Button)findViewById(R.id.ButtonResume);
		Button HiddenEnd=(Button)findViewById(R.id.ButtonEnd);
		DrawingView.getSharedInstance().paused = false;
		HiddenLayout.setVisibility(View.INVISIBLE);
		HiddenResume.setVisibility(View.INVISIBLE);
		HiddenEnd.setVisibility(View.INVISIBLE);
	}

	public void End(View paramView)
	{
		if (DrawingView.getSharedInstance().task != null)
			DrawingView.getSharedInstance().task.cancel();
		Intent newIntent;
		newIntent = new Intent().setClass(this, MainActivity.class);
		newIntent.putExtra("scene", "MenuActivity");
		this.startActivity(newIntent);	
		this.finish();
	}

	public void ChangeSize(View paramView)
	{
		RelativeLayout HiddenLayout=(RelativeLayout)findViewById(R.id.RealLayoutHidden);
		if (HiddenLayout.getVisibility() == View.VISIBLE)
			return;

		ImageView changeView = (ImageView)findViewById(R.id.sizeBackground);
		ImageButton sizeButton=(ImageButton)findViewById(R.id.changeSizeButton);
		ImageButton big=(ImageButton)findViewById(R.id.BigButton);
		ImageButton mid=(ImageButton)findViewById(R.id.MediumButton);
		ImageButton small=(ImageButton)findViewById(R.id.SmallButton);

		if (visible == 0)
		{
			visible = 1;
			DrawingView.getSharedInstance().paused = true;
			big.setVisibility(View.VISIBLE);
			mid.setVisibility(View.VISIBLE);
			small.setVisibility(View.VISIBLE);
			changeView.setVisibility(View.VISIBLE);
			sizeButton.setImageResource(R.drawable.change_size_focus);
		}
		else
		{
			visible = 0;
			DrawingView.getSharedInstance().paused = false;
			big.setVisibility(View.INVISIBLE);
			mid.setVisibility(View.INVISIBLE);
			small.setVisibility(View.INVISIBLE);
			changeView.setVisibility(View.INVISIBLE);
			sizeButton.setImageResource(R.drawable.change_size);
		}
	}

	public void BigStroke(View paramView)
	{
		ImageButton sizeButton=(ImageButton)findViewById(R.id.changeSizeButton);
		sizeButton.setImageResource(R.drawable.change_size);
		DrawingView.getSharedInstance().paused = false;
		DrawingView.getSharedInstance().mPaint.setStrokeWidth(4);
		DrawingView.getSharedInstance().mPaint2.setStrokeWidth(20);
		ImageView changeView = (ImageView)findViewById(R.id.sizeBackground);
		ImageButton big=(ImageButton)findViewById(R.id.BigButton);
		ImageButton mid=(ImageButton)findViewById(R.id.MediumButton);
		ImageButton small=(ImageButton)findViewById(R.id.SmallButton);
		visible = 0;
		big.setVisibility(View.INVISIBLE);
		mid.setVisibility(View.INVISIBLE);
		small.setVisibility(View.INVISIBLE);
		big.setImageResource(R.drawable.big_on);
		mid.setImageResource(R.drawable.medium_off);
		small.setImageResource(R.drawable.small_off);
		changeView.setVisibility(View.INVISIBLE);
	}

	public void MediumStroke(View paramView)
	{
		ImageButton sizeButton=(ImageButton)findViewById(R.id.changeSizeButton);
		sizeButton.setImageResource(R.drawable.change_size);
		DrawingView.getSharedInstance().paused = false;
		DrawingView.getSharedInstance().mPaint.setStrokeWidth(3);
		DrawingView.getSharedInstance().mPaint2.setStrokeWidth(15);
		ImageView changeView = (ImageView)findViewById(R.id.sizeBackground);
		ImageButton big=(ImageButton)findViewById(R.id.BigButton);
		ImageButton mid=(ImageButton)findViewById(R.id.MediumButton);
		ImageButton small=(ImageButton)findViewById(R.id.SmallButton);
		visible = 0;
		big.setVisibility(View.INVISIBLE);
		mid.setVisibility(View.INVISIBLE);
		small.setVisibility(View.INVISIBLE);
		big.setImageResource(R.drawable.big_off);
		mid.setImageResource(R.drawable.medium_on);
		small.setImageResource(R.drawable.small_off);
		changeView.setVisibility(View.INVISIBLE);
	}

	public void SmallStroke(View paramView)
	{
		ImageButton sizeButton=(ImageButton)findViewById(R.id.changeSizeButton);
		sizeButton.setImageResource(R.drawable.change_size);
		DrawingView.getSharedInstance().paused = false;
		DrawingView.getSharedInstance().mPaint.setStrokeWidth(2);
		DrawingView.getSharedInstance().mPaint2.setStrokeWidth(10);
		ImageView changeView = (ImageView)findViewById(R.id.sizeBackground);
		ImageButton big=(ImageButton)findViewById(R.id.BigButton);
		ImageButton mid=(ImageButton)findViewById(R.id.MediumButton);
		ImageButton small=(ImageButton)findViewById(R.id.SmallButton);
		visible = 0;
		big.setVisibility(View.INVISIBLE);
		mid.setVisibility(View.INVISIBLE);
		small.setVisibility(View.INVISIBLE);
		big.setImageResource(R.drawable.big_off);
		mid.setImageResource(R.drawable.medium_off);
		small.setImageResource(R.drawable.small_on);
		changeView.setVisibility(View.INVISIBLE);
	}

	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) 
	{
		//replaces the menu button
		if(pKeyCode == KeyEvent.KEYCODE_MENU && pEvent.getAction() == KeyEvent.ACTION_DOWN) 
		{
			//NOTHING!
		}
		//replaces the default 'Back' button action  
		if(pKeyCode==KeyEvent.KEYCODE_BACK)  
		{  
			if (Bar.getProgress() >= 100)
				return true;

			DrawingView.getSharedInstance().paused = true;

			RelativeLayout HiddenLayout=(RelativeLayout)findViewById(R.id.RealLayoutHidden);
			Button HiddenResume=(Button)findViewById(R.id.ButtonResume);
			Button HiddenEnd=(Button)findViewById(R.id.ButtonEnd);
			HiddenLayout.setBackgroundColor(Color.argb(150, 229, 132, 14));
			if (Locale.getDefault().getDisplayLanguage().equals("polski"))
			{
				HiddenLayout.setVisibility(View.VISIBLE);
				HiddenResume.setVisibility(View.VISIBLE);
				HiddenEnd.setVisibility(View.VISIBLE);
			}
			else
			{
				HiddenResume.setBackgroundResource(R.drawable.popup_wznow_en);
				HiddenEnd.setBackgroundResource(R.drawable.popup_wyjdz_en);
				HiddenLayout.setVisibility(View.VISIBLE);
				HiddenResume.setVisibility(View.VISIBLE);
				HiddenEnd.setVisibility(View.VISIBLE);
			}
			return true;
		}
		return super.onKeyDown(pKeyCode, pEvent);
	}

	@Override
	public void onStop() {
		super.onStop();
		trimCache(this);
		if (DrawingView.getSharedInstance().task != null)
			DrawingView.getSharedInstance().task.cancel();
		this.finish();
	}

	public Bitmap ThreadSaveBitmap()
	{
		final Bitmap bitmap = Bitmap.createBitmap(DrawingView.getSharedInstance().myView.getWidth(), DrawingView.getSharedInstance().myView.getHeight(), Bitmap.Config.ARGB_8888);
		runOnUiThread(new Runnable() {
			public void run() {
				
			}
		});	
		return bitmap;
	}
	
	public void FinishProgress()
	{
		runOnUiThread(new Runnable() {
			public void run() {
				RelativeLayout HiddenLayout=(RelativeLayout)findViewById(R.id.RealLayoutHidden);
				HiddenLayout.setBackgroundColor(Color.argb(150, 229, 132, 14));
				Button HiddenSave=(Button)findViewById(R.id.ButtonSave);
				Button HiddenRestart=(Button)findViewById(R.id.ButtonRestart);
				if (Locale.getDefault().getDisplayLanguage().equals("polski"))
				{
					HiddenLayout.setVisibility(View.VISIBLE);
					HiddenSave.setVisibility(View.VISIBLE);
					HiddenRestart.setVisibility(View.VISIBLE);
					trimCache(instance);
				}
				else
				{
					HiddenSave.setBackgroundResource(R.drawable.popup_zapisz_en);
					HiddenRestart.setBackgroundResource(R.drawable.popup_nowy_en);
					HiddenLayout.setVisibility(View.VISIBLE);
					HiddenSave.setVisibility(View.VISIBLE);
					HiddenRestart.setVisibility(View.VISIBLE);
					trimCache(instance);
				}
			}
		});		
	}

	public static void trimCache(Context context) {
		try {
			File dir = context.getCacheDir();
			if (dir != null && dir.isDirectory()) {
				deleteDir(dir);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

	public static void SetProgressBar(int size) {
		Bar.setProgress((int) (size*3.5));
	}
}
