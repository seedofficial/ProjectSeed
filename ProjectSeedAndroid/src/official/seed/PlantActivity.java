package official.seed;

import java.util.HashMap;
import java.util.Map;

import official.seed.BaseActivity.ActivityInitiialization;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.MultiAutoCompleteTextView;

import com.facebook.UiLifecycleHelper;

public class PlantActivity extends BaseActivity implements ActivityInitiialization {
	private UiLifecycleHelper uiHelper;
	private MultiAutoCompleteTextView txtFacebookFrd;
	private HashMap<String, String> pendingData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plant);
		localInit();
		functionInit();
		this.getWindow()
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	@Override
	public void localInit() {
		txtFacebookFrd = (MultiAutoCompleteTextView) findViewById(R.id.plant_txt_frd);
	}

	@Override
	public void functionInit() {
		txtFacebookFrd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(
						new Intent(PlantActivity.this, PlantFacebookFriendList.class), 1);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.v("Comeback", "YES");
		switch (requestCode) {
		case 1:
			if (resultCode == 3) {
				pendingData = (HashMap<String, String>) data.getSerializableExtra("selectedList");
				if (pendingData != null) {
					setTxtBubble(pendingData);
				}
			}
			break;
		case 2:
			uiHelper.onActivityResult(requestCode, resultCode, data);
			break;
		default:
			break;
		}

	}

	private void setTxtBubble(HashMap<String, String> data) {
		Map<String, String> map = data;

		for (String key : map.keySet()) {
			txtFacebookFrd.append(map.get(key) + ",");
		}

		setChips(txtFacebookFrd);
	}
}
