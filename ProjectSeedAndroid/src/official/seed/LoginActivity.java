package official.seed;

import java.util.Arrays;
import java.util.List;

import official.seed.BaseActivity.ActivityInitiialization;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class LoginActivity extends BaseActivity implements ActivityInitiialization {

	private com.facebook.widget.LoginButton btnFb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		localInit();
		functionInit();
	}

	private void onLoginButtonClicked() {
		hideProgressDialog();
		if (ParseUser.getCurrentUser() == null) {
			List<String> permissions = Arrays.asList("basic_info", "user_about_me",
					"user_relationships", "user_birthday", "user_location");
			ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
				@Override
				public void done(ParseUser user, ParseException err) {
					if (err == null) {
						if (user == null) {
							Log.d("Facebook Related",
									"Uh oh. The user cancelled the Facebook login.");
							Toast.makeText(getApplicationContext(), "Why Cancelled",
									Toast.LENGTH_LONG).show();
							hideProgressDialog();
							finish();
						} else if (user.isNew()) {
							Log.d("Facebook Related",
									"User signed up and logged in through Facebook!");
							Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_LONG)
									.show();
							hideProgressDialog();
							finish();
						} else {
							Log.d("Facebook Related", "User logged in through Facebook!");
							Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_LONG)
									.show();
							hideProgressDialog();
							finish();
						}
					} else {
						Log.v("facebook", err.getMessage());
					}
				}
			});
		} else {
			ParseFacebookUtils.getSession().closeAndClearTokenInformation();
			ParseUser.logOut();
			finish();
		}
	}

	@Override
	public void localInit() {
		btnFb = (com.facebook.widget.LoginButton) findViewById(R.id.login_btn_fb);
	}

	@Override
	public void functionInit() {
		btnFb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onLoginButtonClicked();
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);

	}

}
