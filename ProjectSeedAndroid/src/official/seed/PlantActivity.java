package official.seed;

import com.facebook.UiLifecycleHelper;

import official.seed.BaseActivity.ActivityInitiialization;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class PlantActivity extends BaseActivity implements ActivityInitiialization {
	private TextView txtFacebookFrd;
	private UiLifecycleHelper uiHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plant);
		localInit();
		functionInit();
	}

	@Override
	public void localInit() {
		txtFacebookFrd = (TextView) findViewById(R.id.plant_txt_frd);
	}

	@Override
	public void functionInit() {
		txtFacebookFrd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(PlantActivity.this, PlantFacebookFriendList.class));
//				startPickerActivity(PickFriendsActivity.FRIEND_PICKER, 2);
			}
		});
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2) {
			uiHelper.onActivityResult(requestCode, resultCode, data);
		} else if (resultCode == Activity.RESULT_OK) {
			// Do nothing for now
		}
	}
}
