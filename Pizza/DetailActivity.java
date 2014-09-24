
package kupon.dominion.pizza;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

import org.alexd.jsonrpc.JSONRPCClient;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class DetailActivity extends Activity {
	Context context;

	JSONRPCClient client;
	JSONObject json = new JSONObject();
	public ImageLoader imageLoader;
	ImageView listImage;

	public static DetailActivity instance;

	public static DetailActivity getSharedInstance() {
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;

		setContentView(R.layout.detail);
		context = getApplicationContext();

		client = JSONRPCClient.create("http://coupon");
		client.setConnectionTimeout(2000);
		client.setSoTimeout(2000);

		Typeface light;
		Typeface regular;
		Typeface bold;
		Typeface semibold;
		regular = Typeface.createFromAsset(getAssets(), "opensans_regular.ttf");
		light = Typeface.createFromAsset(getAssets(), "opensans_light.ttf");
		bold = Typeface.createFromAsset(getAssets(), "opensans_bold.ttf");
		semibold = Typeface.createFromAsset(getAssets(),
				"opensans_semibold.ttf");

		TextView title = (TextView) findViewById(R.id.title_text);
		TextView description = (TextView) findViewById(R.id.more_text);
		TextView price = (TextView) findViewById(R.id.price_text);
		TextView date1 = (TextView) findViewById(R.id.date_text1);
		TextView date2 = (TextView) findViewById(R.id.date_text2);
		ImageView image = (ImageView) findViewById(R.id.detail_kupon_image);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String TITLE = extras.getString("NAME");
			String MESSAGE = extras.getString("DESCRIPTION");
			String PRICE = extras.getString("PRICE");
			String DELIMITER = "-";
			String START = extras.getString("START");
			String END = extras.getString("END");
			String VISIBLE = extras.getString("VISIBLE");
			String[] strSTART = START.split(DELIMITER);
			String[] strEND = END.split(DELIMITER);

			title.setTypeface(light);
			title.setText(TITLE);

			description.setTypeface(regular);
			description.setText(MESSAGE);

			price.setTypeface(bold);
			if (PRICE.equals("0.00"))
				price.setText("GRATIS");
			else
				price.setText(PRICE + " Z�");

			date1.setTypeface(semibold);

			date2.setTypeface(bold);
			date2.setText(strSTART[2] + "." + strSTART[1] + "."
					+ strSTART[0].substring(2, 4) + " - " + strEND[2] + "."
					+ strEND[1] + "." + strEND[0].substring(2, 4));

			imageLoader = new ImageLoader(getApplicationContext());
			imageLoader.DisplayImage(extras.getString("URL"), image, false);

			if (VISIBLE.equals("0"))
			{
				ImageButton myimage = (ImageButton) findViewById(R.id.ImageButton1);
				myimage.setVisibility(View.INVISIBLE);
				ImageView image2 = (ImageView) findViewById(R.id.imageView2);
				ScrollView scroller = (ScrollView) findViewById(R.id.scrollView1);
				
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				lp.addRule(RelativeLayout.BELOW, image2.getId());
				lp.addRule(RelativeLayout.ALIGN_LEFT, myimage.getId());
				lp.addRule(RelativeLayout.ALIGN_RIGHT, myimage.getId());
				lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				lp.bottomMargin = 15;
				scroller.setLayoutParams(lp);
			}
		}

		ScrollView sView = (ScrollView) findViewById(R.id.scrollView1);
		// Hide the Scollbar
		sView.setVerticalScrollBarEnabled(false);
		sView.setHorizontalScrollBarEnabled(false);
		sView.setHorizontalFadingEdgeEnabled(false);
		sView.setVerticalFadingEdgeEnabled(false);
	}

	private Handler handler = new Handler();
	int i = 0;
	public void GetTheKupon(View paramView) {
		//listImage.setImageResource(R.drawable.list_gray);
		ListActivity.getSharedInstance().ChangeView();

		ImageButton image = (ImageButton) findViewById(R.id.ImageButton1);
		animate(image).setDuration(500);
		// animate(sView).scaleY(0f);
		animate(image).yBy(200);

		handler.postDelayed(runnable, 75);	
	}

    // Makes the Bottom btn smoothly exit the screen
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			/* do what you need to do */
			ImageButton image = (ImageButton) findViewById(R.id.ImageButton1);
			ImageView image2 = (ImageView) findViewById(R.id.imageView2);
			ScrollView scroller = (ScrollView) findViewById(R.id.scrollView1);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.BELOW, image2.getId());
			lp.addRule(RelativeLayout.ALIGN_LEFT, image.getId());
			lp.addRule(RelativeLayout.ALIGN_RIGHT, image.getId());
			lp.height = scroller.getHeight() + 5;
			lp.bottomMargin = 15;
			scroller.setLayoutParams(lp);
			/* and here comes the "trick" */
			handler.postDelayed(this, 12);
		}
	};

	public void SendView(ImageView view) {
		listImage = view;
	}
}