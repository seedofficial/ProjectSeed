package official.seed.core;

import official.seed.R;
import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

public class Initialize extends Application {

	private final static String PARSE_APPID = "HAz7nJLKWmHTN1kt3IHeXOABhN9aRKVT3v7znKTt";
	private final static String PARSE_CLIENT = "SL7akB7GczdPli1ROd88btxoGs1LbBvNd2aNNHgC";

	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, PARSE_APPID, PARSE_CLIENT);
		ParseFacebookUtils.initialize(getString(R.string.fb_appid));

	}
	
}
