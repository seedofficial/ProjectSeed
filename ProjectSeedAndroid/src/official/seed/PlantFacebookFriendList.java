package official.seed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import official.seed.BaseActivity.ActivityInitiialization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.Response;
import com.facebook.Session;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

@SuppressWarnings("deprecation")
public class PlantFacebookFriendList extends BaseActivity implements ActivityInitiialization {
	private Request request;
	private ListView list;
	private FriendListAdapter adapter;
	private List<HashMap<String, String>> data;
	private HashMap<String, String> selected;
	private DisplayImageOptions options;
	private Bundle params, fromBundle;
	private MultiAutoCompleteTextView bubbleTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plantfacebook_friendlist);
		localInit();
		functionInit();
		this.getWindow()
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_plantfacebook_friendlist, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_fb_confirm:
			onConfirmClick();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void localInit() {
		fromBundle = getIntent().getExtras();
		list = (ListView) findViewById(R.id.fb_listview);
		params = new Bundle();
		bubbleTxt = (MultiAutoCompleteTextView) findViewById(R.id.fb_list_txt);
//		if (selected == null) {
//			selected = new HashMap<String, String>();
//		}
		selected = (HashMap<String, String>) fromBundle.getSerializable("selectedList");
		setTxtBubble(selected, bubbleTxt);

	}

	@Override
	public void functionInit() {
		params.putString("fields", "id,name,picture");
		request = new Request(Session.getActiveSession(), "me/friends", params, HttpMethod.GET,
				new Callback() {
					@Override
					public void onCompleted(Response response) {
						adapter = new FriendListAdapter();
						data = getParsedList(response.getGraphObject().getInnerJSONObject());
						list.setAdapter(adapter);
					}
				});
		request.executeAsync();

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View view, int position, long id) {

				if (selected.containsKey(adapter.getFacebookId(position))) {
					Log.v("selectedFriends", "Remove");
					selected.remove(adapter.getFacebookId(position));
					int st = bubbleTxt.getText().toString().indexOf(adapter.getName(position));
					int en = st + adapter.getName(position).length();
					if (st == -1) {
						st = 0;
					}
					bubbleTxt.getText().delete(st, en + 1);
				} else {
					Log.v("selectedFriends", "Add");
					selected.put(adapter.getFacebookId(position), adapter.getName(position));
					bubbleTxt.append(adapter.getName(position) + ",");
					setChips(bubbleTxt);
				}
			}
		});

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.com_facebook_profile_picture_blank_square)
				.showImageOnFail(R.drawable.com_facebook_profile_picture_blank_square)
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.displayer(new SimpleBitmapDisplayer()).build();
	}

	private void onConfirmClick() {
		Intent resultIntent = new Intent();
		resultIntent.putExtra("selectedList", selected);
		this.setResult(3, resultIntent);
		finish();
	}

	private List<HashMap<String, String>> getParsedList(JSONObject jsonRaw) {
		JSONArray jsonArr = null;
		try {
			jsonArr = jsonRaw.getJSONArray("data");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return getProfiles(jsonArr);
	}

	private List<HashMap<String, String>> getProfiles(JSONArray jsonArr) {
		int arrCount = jsonArr.length();
		List<HashMap<String, String>> profList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> prof = null;
		for (int i = 0; i < arrCount; i++) {
			try {
				prof = getProfile((JSONObject) jsonArr.get(i));
				profList.add(prof);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return profList;
	}

	private HashMap<String, String> getProfile(JSONObject jsonProf) {
		HashMap<String, String> prof = new HashMap<String, String>();
		try {
			prof.put("id", jsonProf.getString("id"));
			prof.put("name", jsonProf.getString("name"));
			prof.put("url", jsonProf.getJSONObject("picture").getJSONObject("data")
					.getString("url"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return prof;
	}

	private class FriendListAdapter extends BaseAdapter {
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		public int getCount() {
			return data.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public String getFacebookId(int position) {
			return data.get(position).get("id");
		}

		public String getName(int position) {
			return data.get(position).get("name");
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null) {
				convertView = getLayoutInflater()
						.inflate(R.layout.activity_plantfacebook_row, null);
				holder = new ViewHolder();
				holder.fbName = (TextView) convertView.findViewById(R.id.fb_row_txt_name);
				holder.fbIcon = (ImageView) convertView.findViewById(R.id.fb_row_img_icon);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			HashMap<String, String> tmp = data.get(position);
			holder.fbName.setText(tmp.get("name"));
			if (holder.fbIcon != null) {
				imageLoader.displayImage(tmp.get("url"), holder.fbIcon, options,
						animateFirstListener);
			}

			return convertView;
		}

		private class ViewHolder {
			TextView fbName;
			ImageView fbIcon;
		}

	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

}
