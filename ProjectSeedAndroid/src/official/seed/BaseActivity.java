package official.seed;

import java.util.HashMap;
import java.util.Map;

import official.seed.core.User;
import official.seed.core.User.UserInitializationListener;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View.MeasureSpec;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class BaseActivity extends Activity implements UserInitializationListener {
	private ProgressDialog mDialog;

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

	protected void showProgressDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
		mDialog = new ProgressDialog(this);
		mDialog.setMessage("Loading");
		mDialog.setCancelable(false);
		mDialog.show();
		mDialog.setOnKeyListener(new Dialog.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					onStop();
					dialog.dismiss();
				}
				return false;
			}
		});
	}

	protected void hideProgressDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}

	protected ImageLoader imageLoader = ImageLoader.getInstance();

	public void setChips(MultiAutoCompleteTextView target) {
		if (target.getText().toString().contains(" ")) // check comman in
														// string
		{

			SpannableStringBuilder ssb = new SpannableStringBuilder(target.getText());
			// split string wich comma
			String chips[] = target.getText().toString().trim().split(",");
			int x = 0;
			// loop will generate ImageSpan for every country name separated by
			// comma
			for (String c : chips) {
				// inflate chips_edittext layout
				LayoutInflater lf = (LayoutInflater) target.getContext().getSystemService(
						Activity.LAYOUT_INFLATER_SERVICE);
				TextView textView = (TextView) lf.inflate(R.layout.bubble_edittext, null);
				textView.setText(c); // set text
				// setFlags(textView, c); // set flag image
				// capture bitmapt of genreated textview
				int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
				textView.measure(spec, spec);
				textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());
				Bitmap b = Bitmap.createBitmap(textView.getWidth(), textView.getHeight(),
						Bitmap.Config.ARGB_8888);
				Canvas canvas = new Canvas(b);
				canvas.translate(-textView.getScrollX(), -textView.getScrollY());
				textView.draw(canvas);
				textView.setDrawingCacheEnabled(true);
				Bitmap cacheBmp = textView.getDrawingCache();
				Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
				textView.destroyDrawingCache(); // destory drawable
				// create bitmap drawable for imagespan
				BitmapDrawable bmpDrawable = new BitmapDrawable(viewBmp);
				bmpDrawable.setBounds(0, 0, bmpDrawable.getIntrinsicWidth(),
						bmpDrawable.getIntrinsicHeight());
				// create and set imagespan
				ssb.setSpan(new ImageSpan(bmpDrawable), x, x + c.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				x = x + c.length() + 1;
			}
			// set chips span
			target.setText(ssb);
			// move cursor to last
			target.setSelection(target.getText().length());
		}

	}
	
	public void setTxtBubble(HashMap<String, String> data, MultiAutoCompleteTextView targetEditText) {
		Map<String, String> map = data;

		for (String key : map.keySet()) {
			targetEditText.append(map.get(key) + ",");
		}

		setChips(targetEditText);
	}
}
