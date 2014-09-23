package rysuj.w.piasku;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

class DrawingView extends View
{

	private Bitmap BitmapSandOutside;
	private Bitmap BitmapSandOutsideRescale;

	private Bitmap BitmapSandInside;
	private Bitmap BitmapSandInsideRescale;
	
	File        mainDir;
	Paint       mPaint;
	Paint       mPaint2;
	Context     myContext;
	Bitmap      mBitmap;
	Canvas      mCanvas;
	//Canvas      ImpCanvas;
	Path        mPath;
	Paint       mBitmapPaint;
	DrawingView myView;
	TimerTask   task;
	int         start;
	int         draw;
	static DrawingView instance;
	boolean paused;
	boolean dontdraw;
	public ArrayList<Integer> bitmapArray = new ArrayList<Integer>();

	public static DrawingView getSharedInstance() {
		return instance;
	}

	public DrawingView(Context context) {
		super(context);
		draw = 0;
		start = 0;
		paused = false;
		dontdraw = false;
		bitmapArray.clear();
		instance = this;
		myView = this;
		myContext = context;

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(Color.argb(255, 204, 102, 1));
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(3);

		BitmapSandInside = BitmapFactory.decodeResource(context.getResources(), R.drawable.sand2);
		BitmapShader mBitmapShaderIn = new BitmapShader(BitmapSandInside, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);   
		mPaint.setShader(null);
		mPaint.setShader(mBitmapShaderIn); 

		mPaint2 = new Paint();
		mPaint2.setAntiAlias(true);
		mPaint2.setDither(true);
		mPaint2.setColor(Color.argb(100, 204, 102, 1));
		mPaint2.setStyle(Paint.Style.STROKE);
		mPaint2.setStrokeJoin(Paint.Join.ROUND);
		mPaint2.setStrokeCap(Paint.Cap.ROUND);
		mPaint2.setStrokeWidth(15);

		BitmapSandOutside = BitmapFactory.decodeResource(context.getResources(), R.drawable.sand1);
		BitmapSandOutsideRescale = Bitmap.createScaledBitmap(BitmapSandOutside, 25, 25, true);
		BitmapShader mBitmapShaderOut = new BitmapShader(BitmapSandOutsideRescale, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);  
		mPaint2.setShader(null); 
		mPaint2.setShader(mBitmapShaderOut); 
		
		mPath = new Path();
		mBitmapPaint = new Paint();
		mBitmapPaint.setColor(Color.RED);

		myView.setBackgroundResource(R.drawable.piasek);
		myView.setDrawingCacheEnabled(true);
		// Check for SDCard
		if (isExternalStoragePresent())
		{
			// Create Folder if doesnt exist
			mainDir = new File(Environment.getExternalStorageDirectory(), "/rysuj/image");
			if (!mainDir.exists()) 
				mainDir.mkdirs();

			// Delete old images if exist
			for (int i = 0; i <= 30; i++)
			{
				String fileName = "/image"+i+".jpg";
				File nextFile = new File(mainDir, fileName);
				if (nextFile.exists())
					nextFile.delete();
			}
			// Delete old small images if exist
			for (int i = 0; i <= 30; i++)
			{
				String fileName = "/imageSmall"+i+".jpg";
				File nextFile = new File(mainDir, fileName);
				if (nextFile.exists())
					nextFile.delete();
			}
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);	
		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
		canvas.drawPath(mPath, mPaint2);
		canvas.drawPath(mPath, mPaint);
	}

	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 0;

	private void saveBitmap()
	{
		if (paused == true)
			return;

		DrawingActivity.SetProgressBar(bitmapArray.size());
		if (bitmapArray.size()>30)
		{
			task.cancel();
			draw = 1;
			DrawingActivity.getSharedInstance().FinishProgress();
			return;
		}
		// Create Bitmap
		Bitmap bitmap = Bitmap.createBitmap(myView.getWidth(), myView.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		myView.draw(canvas);
		Bitmap rescaled;
		rescaled = Bitmap.createScaledBitmap(bitmap, 430, 590, true);	
		Bitmap rescaledSmall;
		//50 - rescaledSmall = Bitmap.createScaledBitmap(bitmap, 90, 125, true);
		rescaledSmall = Bitmap.createScaledBitmap(bitmap, 109, 150, true);

		// Check For SDCard
		if (isExternalStoragePresent())
		{
			// Check image name
			String fileName = "image";
			for (int i = 0; i <= 30; i++)
			{
				fileName = "/image"+i+".jpg";
				File nextFile = new File(mainDir, fileName);
				if (!nextFile.exists())
				{
					// Save image
					OutputStream outStream = null;
					try {
						outStream = new FileOutputStream(nextFile);
						rescaled.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
						outStream.flush();
						outStream.close();		
					}
					catch(Exception e)
					{}
					break;
				}	
			}

			// Check image name
			String fileNameSmall = "imageSmall";
			for (int i = 0; i <= 30; i++)
			{
				fileNameSmall = "/imageSmall"+i+".jpg";
				File nextFileSmall = new File(mainDir, fileNameSmall);
				if (!nextFileSmall.exists())
				{
					// Save image
					OutputStream outStream = null;
					try {
						outStream = new FileOutputStream(nextFileSmall);
						rescaledSmall.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
						outStream.flush();
						outStream.close();		
					}
					catch(Exception e)
					{}
					break;
				}	
			}
		}
		rescaled.recycle();
		rescaledSmall.recycle();
		bitmapArray.add(1);
	}

	private void touch_start(float x, float y) {
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
		if (start == 0)
		{
			start = 1;
			task = new TimerTask()
			{
				@Override
				public void run() {
					new Thread(new Runnable() {
						@Override
						public void run() {
							if (paused == true)
								return;
							saveBitmap();
						}
					}).start();
				}
			};
			new Timer().scheduleAtFixedRate(task, 550, 550);
		}	
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
			mX = x;
			mY = y;
		}
	}

	private void touch_up() {
		mPath.lineTo(mX, mY);
		// commit the path to our offscreen	
		mCanvas.drawPath(mPath, mPaint2);
		mCanvas.drawPath(mPath, mPaint);
		// kill this so we don't double draw
		mPath.reset();	
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		if (paused == true)
			return false;

		if (draw == 0)
		{
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touch_start(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				touch_move(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				touch_up();
				invalidate();
				break;
			}
		}
		return true;
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
			Toast.makeText(myContext, "SD card not present", Toast.LENGTH_LONG)
			.show();

		}
		return (mExternalStorageAvailable) && (mExternalStorageWriteable);
	}
}

