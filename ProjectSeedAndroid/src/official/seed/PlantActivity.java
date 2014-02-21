package official.seed;

import official.seed.BaseActivity.ActivityInitiialization;
import android.os.Bundle;

public class PlantActivity extends BaseActivity implements ActivityInitiialization{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plant);
	}

	@Override
	public void localInit() {
		
	}

	@Override
	public void functionInit() {
		
	}
}
