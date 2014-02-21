package official.seed;

import official.seed.core.User;
import official.seed.core.User.UserInitializationListener;
import android.app.Activity;
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
	}

	@Override
	public void login() {
	}

	@Override
	public void prompt() {
	}
	

}
