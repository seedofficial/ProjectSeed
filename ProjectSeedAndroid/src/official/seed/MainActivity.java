package official.seed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class MainActivity extends BaseActivity {

	private Button btnPlant;
	private GoogleMap map;
	private LatLng HK;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		localInit();
		functionInit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void localInit() {
		btnPlant = (Button) findViewById(R.id.main_btn_plant);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		HK = new LatLng(22.2783, 114.1589);
	}

	private void functionInit() {
		// btn
		btnPlant.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ParseUser.getCurrentUser() == null) {
					Log.d("Facebook Related", "No Session");
					// Intent i = new Intent(MainActivity.this,
					// LoginActivity.class);
					// startActivity(i);
					ParseFacebookUtils.logIn(getParent(), new LogInCallback() {
						@Override
						public void done(ParseUser user, ParseException e) {
							if (e == null) {
								if (user == null) {
									Toast.makeText(getApplicationContext(), "Why Cancelled",Toast.LENGTH_LONG).show();
								} else if (user.isNew()) {
									Toast.makeText(getApplicationContext(), "Welcome!",Toast.LENGTH_LONG).show();
									Intent i = new Intent(MainActivity.this, PlantActivity.class);
									startActivity(i);
								} else {
									Log.d("Facebook Related", "User logged in through Facebook!");
								}
							} else {
								Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
							}
						}
					});
				} else {
					Log.d("Facebook Related", "Got Session");
					Intent i = new Intent(MainActivity.this, PlantActivity.class);
					startActivity(i);
				}
			}
		});
		// map
		map.setMyLocationEnabled(true);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(HK, 13));

		map.addMarker(new MarkerOptions().title("HongKong").snippet("Seed Project Launched")
				.position(HK));
	}
}
