/**
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package official.seed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.facebook.FacebookException;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.PickerFragment;

// This class provides an example of an Activity that uses FriendPickerFragment to display a list of
// the user's friends. It takes a programmatic approach to creating the FriendPickerFragment with the
// desired parameters -- see PickPlaceActivity in the PlacePickerSample project for an example of an
// Activity creating a fragment (in this case a PlacePickerFragment) via XML layout rather than
// programmatically.
public class PickFriendsActivity extends FragmentActivity {
	public static final Uri FRIEND_PICKER = Uri.parse("picker://friend");
	private FriendPickerFragment friendPickerFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plantfacebook);

		Bundle args = getIntent().getExtras();
		FragmentManager manager = getSupportFragmentManager();
		Fragment fragmentToShow = null;
		Uri intentUri = getIntent().getData();

		if (FRIEND_PICKER.equals(intentUri)) {
			if (savedInstanceState == null) {
				friendPickerFragment = new FriendPickerFragment(args);
			} else {
				friendPickerFragment = (FriendPickerFragment) manager
						.findFragmentById(R.id.picker_fragment);
			}
			// Set the listener to handle errors
			friendPickerFragment.setOnErrorListener(new PickerFragment.OnErrorListener() {
				@Override
				public void onError(PickerFragment<?> fragment, FacebookException error) {
					PickFriendsActivity.this.onError(error);
				}
			});
			// Set the listener to handle button clicks
			friendPickerFragment
					.setOnDoneButtonClickedListener(new PickerFragment.OnDoneButtonClickedListener() {
						@Override
						public void onDoneButtonClicked(PickerFragment<?> fragment) {
							finishActivity();
						}
					});
			fragmentToShow = friendPickerFragment;

		} else {
			// Nothing to do, finish
			setResult(RESULT_CANCELED);
			finish();
			return;
		}

		manager.beginTransaction().replace(R.id.picker_fragment, fragmentToShow).commit();
	}

	private void onError(Exception error) {
		onError(error.getLocalizedMessage(), false);
	}

	private void onError(String error, final boolean finishActivity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.error_dialog_title)
				.setMessage(error)
				.setPositiveButton(R.string.error_dialog_button_text,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								if (finishActivity) {
									finishActivity();
								}
							}
						});
		builder.show();
	}

	private void finishActivity() {
		setResult(RESULT_OK, null);
		finish();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (FRIEND_PICKER.equals(getIntent().getData())) {
			try {
				friendPickerFragment.loadData(false);
			} catch (Exception ex) {
				onError(ex);
			}
		}
	}
	

}
