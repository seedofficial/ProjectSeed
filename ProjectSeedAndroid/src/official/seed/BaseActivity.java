package official.seed;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork() // or
				// .detectAll()
				// for
				// all
				// detectable
				// problems
				.permitNetwork() // permit Network access
				.build());
	}
}
