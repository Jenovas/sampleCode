package com.stay.gif;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * @Description: GifEncoder 
 * you can switch images into gif
 *
 * @author Stay
 *
 * @Date 2011-12-13 05:58:56 pm
 * 
 */
public class GifEncoder {
	public Context mContext;
	static GifEncoder giffle = null;
	private int height;
	private int width;
	private int length;
	private int[] pixels;
	private Bitmap bitmap;
	private static final int COLOR = 256;
	private static final int QUALITY = 10;
	public int progress;

	static GifEncoder instance;

	public static GifEncoder getSharedInstance() {
		return instance;
	}

	static {
		System.loadLibrary("gifflen");
	}

	/** 
	 * @Description: encode image to gif
	 *
	 * @param context
	 * @param size you can change this to paths
	 * @param delay  each image's delay in image
	 * @param name   generate file in sd card
	 *
	 * @return void 返回类型 
	 */ 
	public void encode(Context context, int start, int stop, int size, int delay, String name) {
		instance = this;
		progress = 0;
		File dir = new File(Environment.getExternalStorageDirectory(), "/rysuj/image");
		String fileName = "/image"+0+".jpg";
		File nextFile = new File(dir, fileName);
		if (nextFile.exists())
		{
			bitmap = BitmapFactory.decodeFile(nextFile+"");
		}
		width = bitmap.getWidth();
		height = bitmap.getHeight();
		length = size;
		//bitmap.recycle();
		if (start == 0)
		{
			int state = init(name, width, height, COLOR, QUALITY, delay / 10);
			if (state != 0) {
				return;
			}
		}
		pixels = new int[width * height];
		for (int i = start; i < stop && i < length; i++) {
			progress = i;
			File dir2 = new File(Environment.getExternalStorageDirectory(), "/rysuj/image");
			String fileName2 = "/image"+i+".jpg";
			File nextFile2 = new File(dir2, fileName2);
			if (nextFile2.exists())
			{
				bitmap = BitmapFactory.decodeFile(nextFile2+"");
				if (width < bitmap.getWidth() || height < bitmap.getHeight()) {
					bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
				}
				bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
				addFrame(pixels);
				bitmap.recycle();
			}	
		}
		//if (stop > length)
			//close();
	}

	public void encodeSmall(Context context, int size, int delay, String name) {
		instance = this;
		progress = 0;
		File dir = new File(Environment.getExternalStorageDirectory(), "/rysuj/image");
		String fileName = "/imageSmall"+0+".jpg";
		File nextFile = new File(dir, fileName);
		if (nextFile.exists())
		{
			bitmap = BitmapFactory.decodeFile(nextFile+"");
		}
		width = bitmap.getWidth();
		height = bitmap.getHeight();
		length = size;
		//bitmap.recycle();
		int state = init(name, width, height, COLOR, QUALITY, delay / 10);
		if (state != 0) {
			return;
		}
		pixels = new int[width * height];
		for (int i = 0; i < length; i++) {
			progress = i;
			File dir2 = new File(Environment.getExternalStorageDirectory(), "/rysuj/image");
			String fileName2 = "/imageSmall"+i+".jpg";
			File nextFile2 = new File(dir2, fileName2);
			if (nextFile2.exists())
			{
				bitmap = BitmapFactory.decodeFile(nextFile2+"");
				if (width < bitmap.getWidth() || height < bitmap.getHeight()) {
					bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
				}
				bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
				addFrame(pixels);
				bitmap.recycle();
			}	
		}
		close();
	}

	/** 
	 * @Description: 
	 *
	 * @param pixels pixels of one frame of gif 
	 * @return
	 *
	 * @return int  
	 */ 
	public native int addFrame(int[] pixels);

	/**
	 * @param name
	 * @param width
	 * @param height
	 * @param color
	 * @param quality
	 * @param delay
	 * @return 0-->ok -1--->OutOfMemoryError already thrown -2--->file not exist
	 */
	public native int init(String name, int width, int height, int color, int quality, int delay);

	/** 
	 * @Description: 
	 *       when finish encode gif ,close the resource
	 *
	 * @return void 
	 */ 
	public native void close();
}