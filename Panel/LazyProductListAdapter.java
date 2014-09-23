package panel.dzienny.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import panel.dzienny.R;
import panel.dzienny.entities.Product;

import java.util.ArrayList;

public class LazyProductListAdapter extends BaseAdapter {

	private final Activity activity;
	private ArrayList<Product> ProductList;
	private static LayoutInflater inflater = null;
	private final Typeface myTypeface;
	// public ImageLoader imageLoader;

	public LazyProductListAdapter(Activity _activity,
			ArrayList<Product> fullList) {
		activity = _activity;
		ProductList = fullList;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		myTypeface = Typeface.createFromAsset(
				activity.getAssets(), "fonts/devIcons/dev1.ttf");
	}

	public int getCount() {
		return ProductList.size();
	}

	public Object getItem(int position) {
		return ProductList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		// Get Views
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.itemproductlist, parent,
					false);

			holder = new ViewHolder();
			holder.checkbox = (CheckBox) convertView
					.findViewById(R.id.itemproductlist_checkbox);
			holder.name = (TextView) convertView
					.findViewById(R.id.itemproductlist_text_name);
			holder.background = (TextView) convertView
					.findViewById(R.id.itemproductlist_text_background);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.checkbox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				Product product = (Product) cb.getTag();
				product.setSelected(cb.isChecked());
			}
		});
		// Get data from map
		final Product product = ProductList.get(position);
		holder.checkbox.setChecked(product.isSelected());
		holder.checkbox.setTag(product);
		holder.background.setTag(product);
		holder.name.setText(product.getName());
		return convertView;
	}

	static class ViewHolder {
		CheckBox checkbox;
		TextView name;
		TextView background;
	}
}