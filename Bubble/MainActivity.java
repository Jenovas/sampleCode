package moje.babelki;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;


public class MainActivity extends SimpleBaseGameActivity implements IAccelerationListener {

	AdView adView;
	public static final int CAMERA_WIDTH = 768;
	public static final int CAMERA_HEIGHT = 1280;
	public static final int TILE_WIDTH = 32;
	public static final int TILE_HEIGHT = 32;
	public float camera = 0;

	public Context mContext;
	public SmoothCamera mCamera;
	EngineOptions mEngineOptions;

	public Scene mCurrentScene;
	public int WhatScene = 0;

	// MainMenu Textures
	public BuildableBitmapTextureAtlas mMainTextureAtlas;
	public TiledTextureRegion mStartTextureRegion;
	public TiledTextureRegion mStart2TextureRegion;
	public TiledTextureRegion mAdwebTextureRegion;
	public TiledTextureRegion mInfoTextureRegion;
	public TiledTextureRegion mInfoBackTextureRegion;

	// Bubble Textures
	public BuildableBitmapTextureAtlas mBubbleTextureAtlas;
	public TiledTextureRegion mBubble1TextureRegion;
	public TiledTextureRegion mBubble2TextureRegion;
	public TiledTextureRegion mBubble3TextureRegion;
	public TiledTextureRegion mBubble4TextureRegion;
	public TiledTextureRegion mBubble5TextureRegion;
	public TiledTextureRegion mBubble6TextureRegion;
	public TiledTextureRegion mBubble7TextureRegion;
	public TiledTextureRegion mBubble8TextureRegion;
	public TiledTextureRegion mBubble9TextureRegion;
	public TiledTextureRegion mBubble10TextureRegion;
	public TiledTextureRegion mBubble11TextureRegion;
	public TiledTextureRegion mBubble12TextureRegion;
	public TiledTextureRegion mBubble13TextureRegion;
	public TiledTextureRegion mBubble14TextureRegion;
	public TiledTextureRegion mBubble15TextureRegion;

	// Pop Bubble
	public TiledTextureRegion mBubbleP1TextureRegion;
	public TiledTextureRegion mBubbleP2TextureRegion;
	public TiledTextureRegion mBubbleP3TextureRegion;
	public TiledTextureRegion mBubbleP4TextureRegion;
	public TiledTextureRegion mBubbleP5TextureRegion;
	public TiledTextureRegion mBubbleP6TextureRegion;
	public TiledTextureRegion mBubbleP7TextureRegion;
	public TiledTextureRegion mBubbleP8TextureRegion;
	public TiledTextureRegion mBubbleP9TextureRegion;
	public TiledTextureRegion mBubbleP10TextureRegion;
	public TiledTextureRegion mBubbleP11TextureRegion;
	public TiledTextureRegion mBubbleP12TextureRegion;
	public TiledTextureRegion mBubbleP13TextureRegion;
	public TiledTextureRegion mBubbleP14TextureRegion;
	public TiledTextureRegion mBubbleP15TextureRegion;

	// Splatter/Particle Textures
	public BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	public BuildableBitmapTextureAtlas mBitmapTextureAtlas2;
	public TiledTextureRegion mSplatter1TextureRegion;
	public TiledTextureRegion mSplatter2TextureRegion;
	public TiledTextureRegion mSplatter3TextureRegion;
	public TiledTextureRegion mSplatter4TextureRegion;
	public TiledTextureRegion mSplatter5TextureRegion;
	public TiledTextureRegion mParticleTextureRegion;

	// Top Texture
	public BuildableBitmapTextureAtlas mBitmapTopTextureAtlas;
	public TiledTextureRegion mTopTextureRegion;
	public TiledTextureRegion mTopENTextureRegion;
	public TiledTextureRegion mTopTexture1Region;
	public TiledTextureRegion mTopTexture11Region;
	public TiledTextureRegion mTopTexture2Region;
	public TiledTextureRegion mTopTexture22Region;
	public TiledTextureRegion mTopTexture3Region;
	public TiledTextureRegion mTopTexture33Region;
	public TiledTextureRegion mTopTexture4Region;
	public TiledTextureRegion mTopTexture44Region;

	public PhysicsWorld mPhysicsWorld;

	public ArrayList<Sprite> dSprite = new ArrayList<Sprite>();
	public ArrayList<Shape> dShape = new ArrayList<Shape>();

	public Sound ClickSound;
	public Font mFont3;

	public static MainActivity instance;
	public static MainActivity getSharedInstance() {
		return instance;
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		instance = this;
		mCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, CAMERA_WIDTH*1.5f, 0, 0);
		mCamera.setBounds(0, 0, 2304, 1280);
		mCamera.setBoundsEnabled(true);
		mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		mContext = getApplicationContext();
		mEngineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED,
				new FillResolutionPolicy(), mCamera);
		mEngineOptions.getAudioOptions().setNeedsSound(true);
		mEngineOptions.getAudioOptions().setNeedsMusic(true);
		mEngineOptions.getRenderOptions().setDithering(true);
		return mEngineOptions;
	}

	@Override protected void onSetContentView() {
		// CREATING the parent FrameLayout //
		final FrameLayout frameLayout = new FrameLayout(this);

		// CREATING the layout parameters, fill the screen //
		final FrameLayout.LayoutParams frameLayoutLayoutParams =
				new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,
						FrameLayout.LayoutParams.FILL_PARENT);

		// CREATING a Smart Banner View //
		this.adView = new AdView(this, AdSize.BANNER, "ca-app-pub-8814498005274281/6933194853");

		// Doing something I'm not 100% sure on, but guessing by the name //
		adView.refreshDrawableState();
		adView.setVisibility(AdView.VISIBLE);

		// ADVIEW layout, show at the bottom of the screen //
		final FrameLayout.LayoutParams adViewLayoutParams =
				new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
						FrameLayout.LayoutParams.WRAP_CONTENT,
						Gravity.CENTER_HORIZONTAL|Gravity.TOP);

		// REQUEST an ad (Test ad) //
		AdRequest adRequest = new AdRequest();
		//adRequest.addTestDevice("MY_TEST_DEVICE_CODE");
		adView.loadAd(adRequest);

		// RENDER the add on top of the scene //
		this.mRenderSurfaceView = new RenderSurfaceView(this);
		mRenderSurfaceView.setRenderer(mEngine, this);

		// SURFACE layout ? //
		final android.widget.FrameLayout.LayoutParams surfaceViewLayoutParams =
				new FrameLayout.LayoutParams(super.createSurfaceViewLayoutParams());

		// ADD the surface view and adView to the frame //
		frameLayout.addView(this.mRenderSurfaceView, surfaceViewLayoutParams);
		frameLayout.addView(adView, adViewLayoutParams);

		// SHOW AD //
		this.setContentView(frameLayout, frameLayoutLayoutParams);
	} // End of onSetContentView() //
	
	@Override
	public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onAccelerationChanged(AccelerationData pAccelerationData) 
	{
		final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX(), pAccelerationData.getY());
		this.mPhysicsWorld.setGravity(gravity);
		Vector2Pool.recycle(gravity);
	}

	@Override
	protected void onCreateResources() {
		// TODO Auto-generated method stub
	}

	@Override
	protected Scene onCreateScene() {
		// Start Film 
		PlayStart();
		return mCurrentScene;
	}

	public void setCurrentScene(Scene scene) {
		mCurrentScene = scene;

		mCurrentScene.registerUpdateHandler(mPhysicsWorld);
		getEngine().setScene(mCurrentScene);
	}

	public void PlayStart()
	{
		Intent newIntent;
		newIntent = new Intent().setClass(this, SplashScene.class);
		this.startActivity(newIntent);	
	}

	public void LoadAtlas(BuildableBitmapTextureAtlas Atlas)
	{
		try {
			// !!! make sure that last param of BlackPawn builder is greater than 0
			Atlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
			Atlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	public void LoadResources()
	{
		FontFactory.setAssetBasePath("font/");
		final ITexture plokFontTexture3 = new BitmapTextureAtlas(this.getTextureManager(), 2048, 2048, TextureOptions.REPEATING_BILINEAR);
		mFont3 = FontFactory.createFromAsset(this.getFontManager(), plokFontTexture3, this.getAssets(), "1234.ttf", 120, true, Color.WHITE.hashCode());
		mFont3.load();

		/**** SOUNDS ****/
		SoundFactory.setAssetBasePath("mfx/");
		try 
		{
			this.ClickSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "pop.mp3");
			this.ClickSound.stop();
		} 
		catch (final IOException e) 
		{
			Debug.e(e);
		}

		/***************************/
		/*     Main Menu SCENE     */
		/***************************/
		mMainTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 500, 500,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mStartTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mMainTextureAtlas, this, "gfx/Start/active.png",1,1);
		LoadAtlas(mMainTextureAtlas);
		//mMainTextureAtlas.load();

		mMainTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 500, 500,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mInfoBackTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mMainTextureAtlas, this, "gfx/Start/info_back.png",1,1);
		LoadAtlas(mMainTextureAtlas);

		mMainTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 500, 500,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mStart2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mMainTextureAtlas, this, "gfx/Start/unactive.png",1,1);
		LoadAtlas(mMainTextureAtlas);
		//mMainTextureAtlas.load();

		mMainTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 500, 500,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mInfoTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mMainTextureAtlas, this, "gfx/Start/start_info.png",1,1);
		LoadAtlas(mMainTextureAtlas);
		//mMainTextureAtlas.load();

		mMainTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 500, 500,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mAdwebTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mMainTextureAtlas, this, "gfx/Start/start_adweb.png",1,1);
		LoadAtlas(mMainTextureAtlas);
		//mMainTextureAtlas.load();

		/***************************/
		/* Game Scene Bubbles FULL */
		/***************************/
		mBubbleTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mBubble1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBubbleTextureAtlas, this, "gfx/Bubbles/Full/b1.png",1,1);
		LoadAtlas(mBubbleTextureAtlas);
		//mBubbleTextureAtlas.load();

		mBubbleTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256,
				TextureOptions.DEFAULT);
		mBubble2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBubbleTextureAtlas, this, "gfx/Bubbles/Full/b2.png",1,1);
		LoadAtlas(mBubbleTextureAtlas);
		//mBubbleTextureAtlas.load();

		mBubbleTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256,
				TextureOptions.DEFAULT);
		mBubble3TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBubbleTextureAtlas, this, "gfx/Bubbles/Full/b3.png",1,1);
		LoadAtlas(mBubbleTextureAtlas);
		//mBubbleTextureAtlas.load();

		mBubbleTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256,
				TextureOptions.DEFAULT);
		mBubble4TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBubbleTextureAtlas, this, "gfx/Bubbles/Full/b4.png",1,1);
		LoadAtlas(mBubbleTextureAtlas);
		//mBubbleTextureAtlas.load();

		mBubbleTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256,
				TextureOptions.DEFAULT);
		mBubble5TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBubbleTextureAtlas, this, "gfx/Bubbles/Full/b5.png",1,1);
		LoadAtlas(mBubbleTextureAtlas);
		//mBubbleTextureAtlas.load();

		mBubbleTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256,
				TextureOptions.DEFAULT);
		mBubble6TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBubbleTextureAtlas, this, "gfx/Bubbles/Full/b6.png",1,1);
		LoadAtlas(mBubbleTextureAtlas);
		//mBubbleTextureAtlas.load();

		mBubbleTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256,
				TextureOptions.DEFAULT);
		mBubble7TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBubbleTextureAtlas, this, "gfx/Bubbles/Full/b7.png",1,1);
		LoadAtlas(mBubbleTextureAtlas);
		//mBubbleTextureAtlas.load();

		mBubbleTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256,
				TextureOptions.DEFAULT);
		mBubble8TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBubbleTextureAtlas, this, "gfx/Bubbles/Full/b8.png",1,1);
		LoadAtlas(mBubbleTextureAtlas);
		//mBubbleTextureAtlas.load();

		mBubbleTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256,
				TextureOptions.DEFAULT);
		mBubble9TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBubbleTextureAtlas, this, "gfx/Bubbles/Full/b9.png",1,1);
		LoadAtlas(mBubbleTextureAtlas);
		//mBubbleTextureAtlas.load();

		mBubbleTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256,
				TextureOptions.DEFAULT);
		mBubble10TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBubbleTextureAtlas, this, "gfx/Bubbles/Full/b10.png",1,1);
		LoadAtlas(mBubbleTextureAtlas);
		//mBubbleTextureAtlas.load();

		mBubbleTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(),256, 256,
				TextureOptions.DEFAULT);
		mBubble11TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBubbleTextureAtlas, this, "gfx/Bubbles/Full/b11.png",1,1);
		LoadAtlas(mBubbleTextureAtlas);
		//mBubbleTextureAtlas.load();

		mBubbleTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256,
				TextureOptions.DEFAULT);
		mBubble12TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBubbleTextureAtlas, this, "gfx/Bubbles/Full/b12.png",1,1);
		LoadAtlas(mBubbleTextureAtlas);
		//mBubbleTextureAtlas.load();

		mBubbleTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256,
				TextureOptions.DEFAULT);
		mBubble13TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBubbleTextureAtlas, this, "gfx/Bubbles/Full/b13.png",1,1);
		LoadAtlas(mBubbleTextureAtlas);
		//mBubbleTextureAtlas.load();

		mBubbleTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256,
				TextureOptions.DEFAULT);
		mBubble14TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBubbleTextureAtlas, this, "gfx/Bubbles/Full/b14.png",1,1);
		LoadAtlas(mBubbleTextureAtlas);
		//mBubbleTextureAtlas.load();

		mBubbleTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256,
				TextureOptions.DEFAULT);
		mBubble15TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBubbleTextureAtlas, this, "gfx/Bubbles/Full/b15.png",1,1);
		LoadAtlas(mBubbleTextureAtlas);
		//mBubbleTextureAtlas.load();;

		/***************************/
		/* Game Scene Bubbles POP */
		/***************************/
		mBubbleTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256,
				TextureOptions.DEFAULT);
		mBubbleP1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBubbleTextureAtlas, this, "gfx/Bubbles/Pop/p1.png",1,1);
		LoadAtlas(mBubbleTextureAtlas);
		//mBubbleTextureAtlas.load();

		mBubbleTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256,
				TextureOptions.DEFAULT);
		mBubbleP2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBubbleTextureAtlas, this, "gfx/Bubbles/Pop/p2.png",1,1);
		LoadAtlas(mBubbleTextureAtlas);
		//mBubbleTextureAtlas.load();

		mBubbleTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256,
				TextureOptions.DEFAULT);
		mBubbleP3TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBubbleTextureAtlas, this, "gfx/Bubbles/Pop/p3.png",1,1);
		LoadAtlas(mBubbleTextureAtlas);
		//mBubbleTextureAtlas.load();

		mBubbleTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256,
				TextureOptions.DEFAULT);
		mBubbleP4TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBubbleTextureAtlas, this, "gfx/Bubbles/Pop/p4.png",1,1);
		LoadAtlas(mBubbleTextureAtlas);
		//mBubbleTextureAtlas.load();

		mBubbleTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256,
				TextureOptions.DEFAULT);
		mBubbleP5TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBubbleTextureAtlas, this, "gfx/Bubbles/Pop/p5.png",1,1);
		LoadAtlas(mBubbleTextureAtlas);
		//mBubbleTextureAtlas.load();

		mBubbleTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256,
				TextureOptions.DEFAULT);
		mBubbleP6TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBubbleTextureAtlas, this, "gfx/Bubbles/Pop/p6.png",1,1);
		LoadAtlas(mBubbleTextureAtlas);
		//mBubbleTextureAtlas.load();

		mBubbleTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256,
				TextureOptions.DEFAULT);
		mBubbleP7TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBubbleTextureAtlas, this, "gfx/Bubbles/Pop/p7.png",1,1);
		LoadAtlas(mBubbleTextureAtlas);
		//mBubbleTextureAtlas.load();

		mBubbleTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256,
				TextureOptions.DEFAULT);
		mBubbleP8TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBubbleTextureAtlas, this, "gfx/Bubbles/Pop/p8.png",1,1);
		LoadAtlas(mBubbleTextureAtlas);
		//mBubbleTextureAtlas.load();

		mBubbleTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256,
				TextureOptions.DEFAULT);
		mBubbleP9TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBubbleTextureAtlas, this, "gfx/Bubbles/Pop/p9.png",1,1);
		LoadAtlas(mBubbleTextureAtlas);
		//mBubbleTextureAtlas.load();
		/* POP END */

		// Point Particle
		mBitmapTextureAtlas2 = new BuildableBitmapTextureAtlas(getTextureManager(), 128, 128, 
				TextureOptions.BILINEAR);
		mParticleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBitmapTextureAtlas2, this, "gfx/Splatter/particle_point2.png", 1,1);
		LoadAtlas(mBitmapTextureAtlas2);
		//mBitmapTextureAtlas2.load();

		// Splatter 
		mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mSplatter1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBitmapTextureAtlas, this, "gfx/Splatter/r1.png", 1,1);
		LoadAtlas(mBitmapTextureAtlas);
		//mBitmapTextureAtlas.load()

		mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mSplatter2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBitmapTextureAtlas, this, "gfx/Splatter/r2.png", 1,1);
		LoadAtlas(mBitmapTextureAtlas);
		//mBitmapTextureAtlas.load()

		mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mSplatter3TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBitmapTextureAtlas, this, "gfx/Splatter/r3.png", 1,1);
		LoadAtlas(mBitmapTextureAtlas);
		//mBitmapTextureAtlas.load()

		mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mSplatter4TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBitmapTextureAtlas, this, "gfx/Splatter/r4.png", 1,1);
		LoadAtlas(mBitmapTextureAtlas);
		//mBitmapTextureAtlas.load()

		mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 256, 256, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mSplatter5TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBitmapTextureAtlas, this, "gfx/Splatter/r5.png", 1,1);
		LoadAtlas(mBitmapTextureAtlas);
		//mBitmapTextureAtlas.load()

		// HUD
		mBitmapTopTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 800, 400, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mTopTexture1Region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBitmapTopTextureAtlas, this, "gfx/Top/top_btn1.png", 1,1);
		LoadAtlas(mBitmapTopTextureAtlas);
		//mBitmapTopTextureAtlas.load()

		mBitmapTopTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 800, 400, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mTopTexture11Region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBitmapTopTextureAtlas, this, "gfx/Top/top_btn1_active.png", 1,1);
		LoadAtlas(mBitmapTopTextureAtlas);
		//mBitmapTopTextureAtlas.load()

		mBitmapTopTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 800, 400, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mTopTexture2Region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBitmapTopTextureAtlas, this, "gfx/Top/top_btn2.png", 1,1);
		LoadAtlas(mBitmapTopTextureAtlas);
		//mBitmapTopTextureAtlas.load()

		mBitmapTopTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 800, 400, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mTopTexture22Region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBitmapTopTextureAtlas, this, "gfx/Top/top_btn2_active.png", 1,1);
		LoadAtlas(mBitmapTopTextureAtlas);
		//mBitmapTopTextureAtlas.load()

		mBitmapTopTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 800, 400, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mTopTexture3Region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBitmapTopTextureAtlas, this, "gfx/Top/top_btn3.png", 1,1);
		LoadAtlas(mBitmapTopTextureAtlas);
		//mBitmapTopTextureAtlas.load()

		mBitmapTopTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 800, 400, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mTopTexture33Region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBitmapTopTextureAtlas, this, "gfx/Top/top_btn3_active.png", 1,1);
		LoadAtlas(mBitmapTopTextureAtlas);
		//mBitmapTopTextureAtlas.load()

		mBitmapTopTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 800, 400, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mTopTexture4Region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBitmapTopTextureAtlas, this, "gfx/Top/top_btn4.png", 1,1);
		LoadAtlas(mBitmapTopTextureAtlas);
		//mBitmapTopTextureAtlas.load()

		mBitmapTopTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 800, 400, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mTopTexture44Region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBitmapTopTextureAtlas, this, "gfx/Top/top_btn4_active.png", 1,1);
		LoadAtlas(mBitmapTopTextureAtlas);
		//mBitmapTopTextureAtlas.load()

		mBitmapTopTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 800, 400, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mTopTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBitmapTopTextureAtlas, this, "gfx/Top/top_bg.png", 1,1);
		LoadAtlas(mBitmapTopTextureAtlas);
		
		mBitmapTopTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), 800, 400, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mTopENTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset
				(mBitmapTopTextureAtlas, this, "top_bg_en.png", 1,1);
		LoadAtlas(mBitmapTopTextureAtlas);
		//mBitmapTopTextureAtlas.load()
	}

	@Override
	public void onResumeGame() {
		super.onResumeGame();
		this.enableAccelerationSensor(this);
	}

	@Override
	public void onPauseGame() {
		super.onPauseGame();
		this.disableAccelerationSensor();
	}

	public void myGarbageCollection(final Scene mScene){

		Iterator<Body> allMyBodies = this.mPhysicsWorld.getBodies();
		while(allMyBodies.hasNext())
		{
			try {
				final Body myCurrentBody = allMyBodies.next();
				mPhysicsWorld.destroyBody(myCurrentBody);                
			} catch (Exception e) {
				Debug.d("SPK - THE BODY DOES NOT WANT TO DIE: " + e);
			}
		}

		Iterator<Joint> allMyJoints = this.mPhysicsWorld.getJoints();
		while(allMyJoints.hasNext())
		{
			try {
				final Joint myCurrentJoint = allMyJoints.next();
				mPhysicsWorld.destroyJoint(myCurrentJoint);                
			} catch (Exception e) {
				Debug.d("SPK - THE JOINT DOES NOT WANT TO DIE: " + e);
			}
		}

		if(dSprite.size()>0){
			for(int i=0; i < dSprite.size(); i++){                  
				try {
					final int myI = i;
					mScene.detachChild(dSprite.get(myI));                
				} catch (Exception e) {
					Debug.d("SPK - THE SPRITE DOES NOT WANT TO DIE: " + e);
				}
			}
		}
		dSprite.clear();

		if(dShape.size()>0){
			for(int i=0; i < dShape.size(); i++){                   
				try {
					final int myI = i;
					mScene.detachChild(dShape.get(myI));                
				} catch (Exception e) {
					Debug.d("SPK - THE SHAPE DOES NOT WANT TO DIE: " + e);
				}
			}
		}
		dShape.clear();

		mScene.clearChildScene();
		mScene.detachChildren();
		mScene.reset();
		mScene.detachSelf();
		mPhysicsWorld.clearForces();
		mPhysicsWorld.clearPhysicsConnectors();
		mPhysicsWorld.reset();
		System.gc();
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
			if (WhatScene == 3)
			{
				setCurrentScene(new MainMenuScene());			
			}
			else if (WhatScene == 2)
			{
				runOnUpdateThread(new Runnable()
		        {
		                @Override
		                public void run()
		                {
		                	mCamera.setCenterDirect(0, 0);
		                	myGarbageCollection(getEngine().getScene());
		                	setCurrentScene(new MainMenuScene());
		                	mCamera.setHUD(null);
		                }
		        });
			}
			else if (WhatScene == 1)
			{
				finish();
			}
			return true;
		}


		return super.onKeyDown(pKeyCode, pEvent);
	}
}
