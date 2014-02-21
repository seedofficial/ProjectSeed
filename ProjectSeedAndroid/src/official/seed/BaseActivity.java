package official.seed;

import com.parse.ParseFacebookUtils;

import official.seed.core.User;
import official.seed.core.User.UserInitializationListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;

public class BaseActivity extends Activity implements UserInitializationListener {

	public interface ActivityInitiialization {
		public void localInit();
		public void functionInit();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		User.initialize(this);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork() // or
				// .detectAll()
				// for
				// all
				// detectable
				// problems
				.permitNetwork() // permit Network access
				.build());
	}

	@Override
	public void login() {
	}

	@Override
	public void prompt() {
	}
	

}
