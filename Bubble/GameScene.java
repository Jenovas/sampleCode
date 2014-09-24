package moje.babelki;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.BlendFunctionParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.IParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.entity.util.FPSCounter;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXObject;
import org.andengine.extension.tmx.TMXObjectGroup;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.render.RenderTexture;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import android.app.Activity;
import android.opengl.GLES20;
import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class GameScene extends Scene implements IOnSceneTouchListener, IScrollDetectorListener{

	private static float droga;
	private static float speed;
	private static long time;
	private static final int STATE_WAIT = 0;
	private static final int STATE_SCROLLING = 1;
	private static final int STATE_MOMENTUM = 2;
	private static final int STATE_DISABLE = 3;
	private static final float FREQ_D = 60.0f;
	private TimerHandler thandle;
	private static int mState = STATE_DISABLE;
	private static VertexBufferObjectManager msharedVertex = null;
	private long t0;

	private TMXTiledMap mTMXTiledMap;

	// Menu
	public static MainActivity BaseAct = MainActivity.getSharedInstance();
	static GameScene GameSc;
	static Sprite blue;
	static Sprite green;
	static Sprite yellow;
	static Sprite red;

	// Color Controller
	public static boolean isblue;
	public static boolean isgreen;
	public static boolean isred;
	public static boolean isyellow;

	// Object&Sprite Arrays
	public static ArrayList<Sprite> Paint = new ArrayList<Sprite>();
	public static ArrayList<Sprite> PaintSlope = new ArrayList<Sprite>();
	public static ArrayList<Particle<Sprite>> ActiveBodyCount = new ArrayList<Particle<Sprite>>();
	public static ArrayList<SpriteBatch> EntityCount = new ArrayList<SpriteBatch>();

	static int i = 0;
	static int timer = 1;
	public HashMap<String,String> properties;

	final static FixtureDef cellFixtureDef = PhysicsFactory.createFixtureDef(0.01f, 0.01f, 0.01f);
	static boolean clicked;
	ScrollDetector mScrollDetector = new ScrollDetector(this);

	static ITextureRegion texture;
	static ITextureRegion texture2;
	static ITextureRegion splatterTexture;

	static boolean add;
	static boolean addSprite;
	static FPSCounter fpsCounter;

	public static GameScene instance;
	public static GameScene getSharedInstance() {
		return instance;
	}

	public GameScene() {
		super();
		add = true;
		addSprite = true;
		isblue = true;
		isgreen = false;
		isred = false;
		isyellow = false;

		BaseAct = MainActivity.getSharedInstance();
		GameSc = this;
		instance = this;

		// Used for changing scenes in MainActivity
		BaseAct.WhatScene = 2;

		// Path to TMX Map
		String map = "";
		map = "tmx/gameproba.tmx";
		if (Locale.getDefault().getDisplayLanguage().equals("polski"))
			map = "tmx/gameproba.tmx";
		else
			map = "tmx/gameproba.tmx";
		// Load the TMX level
		try {
			final TMXLoader tmxLoader = new TMXLoader(BaseAct.getAssets(), BaseAct.getEngine().getTextureManager(), TextureOptions.BILINEAR, BaseAct.getEngine().getVertexBufferObjectManager());
			this.mTMXTiledMap = tmxLoader.loadFromAsset(map);
		} catch (final TMXLoadException e) {
			Debug.e(e);
		}
		for(TMXLayer tmxLayer : this.mTMXTiledMap.getTMXLayers()) 
		{
			tmxLayer.setPosition(0, 0);
			GameSc.attachChild(tmxLayer);
		}
		// Add all TMX objects to map
		final ArrayList<TMXObjectGroup> groups = mTMXTiledMap.getTMXObjectGroups();
		ArrayList<TMXObject> objects;
		for(final TMXObjectGroup group: groups) 
		{
			objects = group.getTMXObjects();
			for(final TMXObject object : objects) 
			{
				String type = "";
				if(group.getTMXObjectGroupProperties().size() > 0) 
				{
					type = group.getTMXObjectGroupProperties().get(0).getValue();
				}

				properties = new HashMap<String,String>();
				int size = object.getTMXObjectProperties().size();
				for(int i=0;i<size;i++) 
				{
					properties.put(object.getTMXObjectProperties().get(i).getName(),
							object.getTMXObjectProperties().get(i).getValue());
				}

				if(properties.containsKey("type")) 
				{
					type = properties.get("type");
				}

				Entities.addEntity
				(
						BaseAct,
						GameSc,
						object.getX(),
						object.getY(),
						object.getWidth(),
						object.getHeight(),
						type,
						properties
						);
			}
		}	
		BaseAct.mCamera.setHUD(new TopScene());
		setOnSceneTouchListener(this);
		fpsCounter = new FPSCounter();
		BaseAct.getEngine().registerUpdateHandler(fpsCounter);
		mScrollDetector = new SurfaceScrollDetector(this);
		registerUpdateHandler(new TimerHandler(10f, true, new ITimerCallback()
		{
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler)
			{
				fpsCounter.reset();
			}
		}));
		thandle = new TimerHandler(1.0f / FREQ_D, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				doSetPos();
			}
		});
		registerUpdateHandler(thandle);
		mState = STATE_WAIT;
	}

	public static class Entities {
		public final static Object LEVEL_TYPE_BUBBLE = "bubble";
		public final static Object LEVEL_TYPE_WALL = "wall";

		public static void addEntity(
				Activity pParent,
				Scene pScene,
				int pX,
				int pY,
				int pWidth,
				int pHeight,
				String pType,
				HashMap<String,String> properties)
		{
			if(pType.equals(Entities.LEVEL_TYPE_BUBBLE)) 
			{	
				Entities.addBubble(pParent, pScene, pX, pY, pWidth, pHeight,properties);
			}

			if(pType.equals(Entities.LEVEL_TYPE_WALL)) 
			{	
				Entities.addWall(pParent, pScene, pX, pY, pWidth, pHeight,properties);
			}
		}

		private static void addWall(
				Activity pParent,
				Scene pScene,
				int pX,
				int pY,
				int pWidth,
				int pHeight,
				HashMap<String,String> properties)
		{
			final Rectangle  wall = new Rectangle(pX, pY, pWidth, pHeight, BaseAct.getVertexBufferObjectManager());
			wall.setVisible(false);
			Body body1 = PhysicsFactory.createBoxBody(
					BaseAct.mPhysicsWorld,
					wall,
					BodyType.StaticBody,
					cellFixtureDef
					);
			pScene.attachChild(wall);
			BaseAct.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(wall, body1, true, true));
		}

		public static SpriteParticleSystem createBodyParticleSystem (final Sprite splatter, final float x, final float y, final float R, final float G, final float B) {
			if(msharedVertex  == null)
				msharedVertex = BaseAct.getVertexBufferObjectManager();
		
			final SpriteBatch newBatch  = new SpriteBatch(BaseAct.mBitmapTextureAtlas2, 85, msharedVertex);
			
			int amount = (int)(Math.random()*3);
			final SpriteParticleSystem particleSystem = new SpriteParticleSystem(new PointParticleEmitter(x+10+(35*amount), y+90), 1, 1, 1, BaseAct.mParticleTextureRegion, BaseAct.getVertexBufferObjectManager());
			particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_DST_COLOR));
			particleSystem.addParticleInitializer(new ColorParticleInitializer<Sprite>(R, G, B));
			particleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(15f));
			particleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(0, 0, 0f, 0f));
			particleSystem.addParticleModifier(new ColorParticleModifier<Sprite>(0.0f, 10f, R, R, G, G, B, B));
			particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(0f, 0f, 0.0f, 0.0f));
			particleSystem.addParticleInitializer(new IParticleInitializer<Sprite>() 
			{
				public void onInitializeParticle(final Particle<Sprite> pParticle) 
				{

					GameSc.attachChild(newBatch);
					newBatch.setX(x);
					newBatch.setY(y);
					newBatch.setZIndex(15);
					pParticle.getEntity().registerUpdateHandler(new TimerHandler(0.01f, new ITimerCallback() 
					{
						@Override
						public void onTimePassed(final TimerHandler pTimerHandler)
						{
							Body body = PhysicsFactory.createCircleBody(BaseAct.mPhysicsWorld, pParticle.getEntity(), BodyType.DynamicBody, cellFixtureDef);
							BaseAct.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(pParticle.getEntity(), body));
							body.setLinearDamping(1.5f);
							GameSc.unregisterUpdateHandler(pTimerHandler);
							ActiveBodyCount.add(pParticle);

							if (ActiveBodyCount.size() > 5)
							{
								Sprite myParticle = ActiveBodyCount.get(0).getEntity();
								myParticle.clearUpdateHandlers();
								GameSc.detachChild(myParticle);
								ActiveBodyCount.remove(0);
							}
						}
					}));

					pParticle.getEntity().registerUpdateHandler(new TimerHandler(0.085f, true, new ITimerCallback() 
					{
						@Override
						public void onTimePassed(final TimerHandler pTimerHandler)
						{
							
							final Sprite sprite = new Sprite(pParticle.getEntity().getX()-newBatch.getX(), pParticle.getEntity().getY()-newBatch.getY(), BaseAct.mParticleTextureRegion, msharedVertex);
							sprite.setColor(R, G, B);
							//sprite.setScale(0.03f);	

							if (pParticle.getEntity().collidesWith(splatter))
								addSprite = false;
							else
								addSprite = true;
							sprite.setScale(0.85f);	

							if (addSprite == true)
							{
								newBatch.attachChild(sprite);
							}
						}
					}));

					GameSc.registerUpdateHandler(new TimerHandler(7f, new ITimerCallback() 
					{
						@Override
						public void onTimePassed(final TimerHandler pTimerHandler)
						{
							pParticle.getEntity().clearUpdateHandlers();
							Body body = BaseAct.mPhysicsWorld.getPhysicsConnectorManager().findBodyByShape(pParticle.getEntity());
							BaseAct.mPhysicsWorld.unregisterPhysicsConnector(BaseAct.mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(pParticle.getEntity()));
							BaseAct.mPhysicsWorld.destroyBody(body);
							GameSc.unregisterUpdateHandler(pTimerHandler);
							ActiveBodyCount.remove(pParticle.getEntity());
							EntityCount.add(newBatch);
						}

					}));

				}
			});
			return particleSystem;
		}

		private static void destroyBall(final Sprite ball)
		{
			BaseAct.runOnUpdateThread(new Runnable(){

				@Override
				public void run() {
					Body body = (Body)ball.getUserData();
					body.setActive(true);
					ball.setVisible(false);
				}});

		}

		private static void addBubble(
				Activity pParent,
				final Scene pScene,
				int pX,
				int pY,
				int pWidth,
				int pHeight,
				HashMap<String,String> properties)
		{
			texture = BaseAct.mBubble1TextureRegion;
			int amount = (int)(Math.random()*15);
			switch (amount)
			{
				case 1:
					texture = BaseAct.mBubble1TextureRegion;
					break;
				case 2:
					texture = BaseAct.mBubble2TextureRegion;
					break;
				case 3:
					texture = BaseAct.mBubble3TextureRegion;
					break;
				case 4:
					texture = BaseAct.mBubble4TextureRegion;
					break;
				case 5:
					texture = BaseAct.mBubble5TextureRegion;
					break;
				case 6:
					texture = BaseAct.mBubble6TextureRegion;
					break;
				case 7:
					texture = BaseAct.mBubble7TextureRegion;
					break;
				case 8:
					texture = BaseAct.mBubble8TextureRegion;
					break;
				case 9:
					texture = BaseAct.mBubble9TextureRegion;
					break;
				case 10:
					texture = BaseAct.mBubble10TextureRegion;
					break;
				case 11:
					texture = BaseAct.mBubble11TextureRegion;
					break;
				case 12:
					texture = BaseAct.mBubble12TextureRegion;
					break;
				case 13:
					texture = BaseAct.mBubble13TextureRegion;
					break;
				case 14:
					texture = BaseAct.mBubble14TextureRegion;
					break;
				case 15:
					texture = BaseAct.mBubble15TextureRegion;
					break;		
			}
			Sprite bubble = new Sprite(pX-19, pY-11, texture, BaseAct.getVertexBufferObjectManager()) {
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) 
				{
					switch(pSceneTouchEvent.getAction()) 
					{
					case TouchEvent.ACTION_DOWN:
						GameSc.mScrollDetector.setEnabled(true);
						clicked = true;
						if (mState == STATE_MOMENTUM)
						{
							droga = 0;
							speed = 0;
							mState = STATE_WAIT;
							BaseAct.runOnUpdateThread(new Runnable()
							{
								@Override
								public void run()
								{
									CheckBubblePosition();
								}
							});
						}
						break;
					case TouchEvent.ACTION_UP:
						if (clicked == false)
							return false;

						if (!this.isVisible())
							return false;

						if (GameSc.getChildCount() > 270)
						{
							if (EntityCount.size()>10)
							{
								for(int i=0; i < 6; i++)
								{                  
									try 
									{
										SpriteBatch MainSprite = EntityCount.get(i);
										MainSprite.detachChildren();
										GameSc.detachChild(MainSprite);
										EntityCount.remove(i);	           
									} catch (Exception e) 
									{
										Debug.d("SPK - THE SPRITE DOES NOT WANT TO DIE: " + e);
									}
								}
							}
						}
						
						BaseAct.ClickSound.play();
						destroyBall(this);
						float R = 0;
						float G = 0;
						float B = 0;
						if (isred)
						{
							R = 0.93f;
							G = 0.01f;
							B = 0.01f;
						}
						else if (isgreen)
						{
							R = 0f;
							G = 0.91f;
							B = 0.15f;
						}
						else if (isblue)
						{
							R = 0;
							G = 0.77f;
							B = 1f;
						}
						else if (isyellow)
						{
							R = 1f;
							G = 0.64f;
							B = 0.02f;
						}

						texture2 = BaseAct.mBubbleP2TextureRegion;
						int amount = (int)(Math.random()*8);
						switch (amount)
						{
							case 1:
							    texture2 = BaseAct.mBubbleP2TextureRegion;
								break;
							case 2:
							    texture2 = BaseAct.mBubbleP3TextureRegion;
								break;
							case 3:
							    texture2 = BaseAct.mBubbleP4TextureRegion;
								break;
							case 4:
							    texture2 = BaseAct.mBubbleP5TextureRegion;
								break;
							case 5:
							    texture2 = BaseAct.mBubbleP6TextureRegion;
								break;
							case 6:
							    texture2 = BaseAct.mBubbleP7TextureRegion;
								break;
							case 7:
							    texture2 = BaseAct.mBubbleP8TextureRegion;
								break;
							case 8:
							    texture2 = BaseAct.mBubbleP9TextureRegion;
								break;
						}
						
						int amount2 = (int)(Math.random()*5);
						float scale = 1;	
						splatterTexture = BaseAct.mSplatter1TextureRegion;
						switch (amount2)
						{
							case 1:
								splatterTexture = BaseAct.mSplatter1TextureRegion;
								scale = 0.85f;
								break;
							case 2:
								splatterTexture = BaseAct.mSplatter2TextureRegion;
								break;
							case 3:
								splatterTexture = BaseAct.mSplatter3TextureRegion;
								break;
							case 4:
								splatterTexture = BaseAct.mSplatter4TextureRegion;
								break;
							case 5:
								splatterTexture = BaseAct.mSplatter5TextureRegion;
								break;
						}

						// Bubble after splatter
						final Sprite splatter1 = new Sprite(getX()-10, getY()-10, texture2, BaseAct.getVertexBufferObjectManager());
						// Color splatter
						final Sprite splatter2 = new Sprite(getX()-38, getY()-40, splatterTexture, BaseAct.getVertexBufferObjectManager());
						final SpriteParticleSystem BodyParticle = createBodyParticleSystem(this, getX(), getY(), R, G, B);

						splatter2.setColor(R, G, B);
						splatter2.setScale(scale);
						splatter2.setUserData(splatter1);
						splatter1.setUserData(this);
						splatter1.setScale(0.80f);
						splatter1.setAlpha(0.35f);

						pScene.attachChild(splatter2);
						pScene.attachChild(splatter1);
						pScene.attachChild(BodyParticle);

						pScene.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() {
							@Override
							public void onTimePassed(final TimerHandler pTimerHandler){
								BodyParticle.setParticlesSpawnEnabled(false);
								pScene.unregisterUpdateHandler(pTimerHandler);
							}

						}));
						
						pScene.registerUpdateHandler(new TimerHandler(10f, new ITimerCallback() {
							@Override
							public void onTimePassed(final TimerHandler pTimerHandler){
								pScene.detachChild(BodyParticle);
								Paint.add(splatter2);
								pScene.unregisterUpdateHandler(pTimerHandler);
							}              
						}));
						break;
					}
					GameSc.mScrollDetector.onTouchEvent(pSceneTouchEvent);
					return true;

				}   
			};
			bubble.setScale(0.81f);
			Body body = PhysicsFactory.createCircleBody(BaseAct.mPhysicsWorld, bubble, BodyType.StaticBody, cellFixtureDef);
			bubble.setUserData(body);
			BaseAct.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(bubble, body));
			pScene.attachChild(bubble);
			pScene.registerTouchArea(bubble);
		}		
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (mState == STATE_DISABLE)
			return true;

		if (mState == STATE_MOMENTUM) {
			mState = STATE_WAIT;
			droga = 0;
			speed = 0;
			BaseAct.runOnUpdateThread(new Runnable()
			{
				@Override
				public void run()
				{
					CheckBubblePosition();
				}
			});
		}

		mScrollDetector.onTouchEvent(pSceneTouchEvent);
		return true;
	}

	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
		clicked = false;
		t0 = System.currentTimeMillis();
		mState = STATE_SCROLLING;
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
		clicked = false;
		time = System.currentTimeMillis() - t0;
		if (time == 0)
			return;

		droga = pDistanceX;
		speed = droga/time;
		t0 = System.currentTimeMillis();
		mState = STATE_SCROLLING;
	}

	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
		mState = STATE_MOMENTUM;
		clicked = false;
	}

	protected synchronized void doSetPos() {

		if (droga == 0)
			return;

		BaseAct.mCamera.offsetCenter(-droga, 0);
		if (speed > 0)
		{
			if (droga > 0)
				droga -= 1f;
			else
			{
				speed = 0;
				droga = 0;
				BaseAct.runOnUpdateThread(new Runnable()
				{
					@Override
					public void run()
					{
						CheckBubblePosition();
					}
				});
			}
		}
		else if (speed < 0)
		{
			if (droga < 0)
				droga += 1f;
			else
			{
				speed = 0;
				droga = 0;
				BaseAct.runOnUpdateThread(new Runnable()
				{
					@Override
					public void run()
					{
						CheckBubblePosition();
					}
				});
			}
		}
		else
			droga = 0;
	}

	public static void CheckBubblePosition ()
	{
		final float newX = BaseAct.mCamera.getCenterX();
		if (EntityCount.size()>0)
		{
			for(int i=0; i < EntityCount.size(); i++)
			{                  
				try 
				{
					float X = EntityCount.get(i).getX();
					
					if (newX - 1100/2 > X || X > newX + 1100/2)
					{
						SpriteBatch MainSprite = EntityCount.get(i);
						MainSprite.detachChildren();
						GameSc.detachChild(MainSprite);
						EntityCount.remove(i);	
					}                
				} catch (Exception e) 
				{
					Debug.d("SPK - THE SPRITE DOES NOT WANT TO DIE: " + e);
				}
			}
		}
		if (Paint.size()> 0)
		{
			for(int i=0; i < Paint.size(); i++)
			{                  
				try 
				{
					float X = Paint.get(i).getX();
					
					if (newX - 1100/2 > X || X > newX + 1100/2)
					{
						Sprite MainSprite = Paint.get(i);
						IEntity Sprite2 = (IEntity) Paint.get(i).getUserData();
						Sprite Sprite3 = (Sprite) Sprite2.getUserData();
						Body body = (Body) Sprite3.getUserData();
						GameSc.detachChild(MainSprite);
						GameSc.detachChild(Sprite2);
						Sprite3.setVisible(true);
						body.setActive(true);
						Paint.remove(i);	
					}                
				} catch (Exception e) 
				{
					Debug.d("SPK - THE SPRITE DOES NOT WANT TO DIE: " + e);
				}
			}            
		}
	}
}
