package official.seed;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import official.seed.BaseActivity.ActivityInitiialization;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;

import com.facebook.UiLifecycleHelper;

public class PlantActivity extends BaseActivity implements ActivityInitiialization {
	private UiLifecycleHelper uiHelper;
	private MultiAutoCompleteTextView txtFacebookFrd;
	private HashMap<String, String> pendingData;
	private Button camBtn;
	private ImageButton imgBtn1, imgBtn2, imgBtn3;
	private Camera camera;
	private ArrayList<Bitmap> bitmapList;
	private int lastImgBtn = 0;

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
		bitmapList = new ArrayList<Bitmap>();
		imgBtn1 = (ImageButton) findViewById(R.id.plant_btn_img_1);
		imgBtn2 = (ImageButton) findViewById(R.id.plant_btn_img_2);
		imgBtn3 = (ImageButton) findViewById(R.id.plant_btn_img_3);
		camBtn = (Button) findViewById(R.id.plant_btn_camera);
		txtFacebookFrd = (MultiAutoCompleteTextView) findViewById(R.id.plant_txt_frd);
		if (pendingData == null) {
			pendingData = new HashMap<String, String>();
		}

	}

	@Override
	public void functionInit() {
		imgBtn1.setOnClickListener(imgBtnOnClickListener);
		imgBtn2.setOnClickListener(imgBtnOnClickListener);
		imgBtn3.setOnClickListener(imgBtnOnClickListener);
		camBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentCamera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intentCamera, 2);
			}
		});
		txtFacebookFrd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(PlantActivity.this, PlantFacebookFriendList.class);
				i.putExtra("selectedList", pendingData);
				startActivityForResult(i, 1);
			}
		});
	}

	private void updateImgFromList() {
		if (bitmapList.size() != 0) {
			for (int i = 0; i < bitmapList.size(); i++) {
				switch (i) {
				case 0:
					imgBtn1.setImageBitmap(bitmapList.get(i));
					break;
				case 1:
					imgBtn2.setImageBitmap(bitmapList.get(i));
					break;
				case 2:
					imgBtn3.setImageBitmap(bitmapList.get(i));
					break;
				default:
					break;
				}
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v("Comeback", "YES");
		switch (requestCode) {
		case 1:
			if (resultCode == 3) {
				pendingData.clear();
				txtFacebookFrd.getText().clear();
				pendingData = (HashMap<String, String>) data.getSerializableExtra("selectedList");
				if (pendingData != null) {
					setTxtBubble(pendingData, txtFacebookFrd);
				}
			}
			break;
		case 2:
			if (resultCode == RESULT_OK) {
				Bitmap bmp = (Bitmap) data.getExtras().get("data");
				bitmapList.add(bmp);
				updateImgFromList();
			}
			break;
		case 3:
			if (resultCode == RESULT_OK) {
				try {
					Uri imageUri = data.getData();
					InputStream imageStream = getContentResolver().openInputStream(imageUri);
					final Bitmap bmp = BitmapFactory.decodeStream(imageStream);
					switch (bitmapList.size()) {
					case 0:
						bitmapList.add(bmp);
						break;
					case 1:
					case 2:
					case 3:
						if (bitmapList.size()<=lastImgBtn) {
							bitmapList.add(bmp);
						} else {
							bitmapList.set(lastImgBtn, bmp);
						}
						break;
					default:
						break;
					}
					
					updateImgFromList();
					imageButtonUpdate();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			break;
		case 99:
			uiHelper.onActivityResult(requestCode, resultCode, data);
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void imagePicker() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intent, 3);
	}

	private void imageButtonUpdate() {
		switch (bitmapList.size()) {
		case 0:
			imgBtn2.setVisibility(View.GONE);
			imgBtn3.setVisibility(View.GONE);
			break;
		case 1:
			imgBtn2.setVisibility(View.VISIBLE);
			imgBtn3.setVisibility(View.GONE);
			break;
		case 2:
			imgBtn2.setVisibility(View.VISIBLE);
			imgBtn3.setVisibility(View.VISIBLE);
			break;
		}
	}

	private OnClickListener imgBtnOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.plant_btn_img_1:
				lastImgBtn = 0;
				break;
			case R.id.plant_btn_img_2:
				lastImgBtn = 1;
				break;
			case R.id.plant_btn_img_3:
				lastImgBtn = 2;
				break;

			default:
				break;
			}
			imagePicker();
		}
	};

}
