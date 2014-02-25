package official.seed;

import java.util.List;

import official.seed.core.BaseListElement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlantFacebookAdapter extends ArrayAdapter<BaseListElement> {
	private List<BaseListElement> listElements;

	public PlantFacebookAdapter(Context context, int resourceId, List<BaseListElement> listElements) {
		super(context, resourceId, listElements);
		this.listElements = listElements;
		// Set up as an observer for list item changes to
		// refresh the view.
		for (int i = 0; i < listElements.size(); i++) {
			listElements.get(i).setAdapter(this);
		}
	}

//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		View view = convertView;
//		if (view == null) {
//			LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(
//					Context.LAYOUT_INFLATER_SERVICE);
//			view = inflater.inflate(R.layout.activity_plantfacebook_row, null);
//		}
//
//		BaseListElement listElement = listElements.get(position);
//		if (listElement != null) {
//			view.setOnClickListener(listElement.getOnClickListener());
//			ImageView icon = (ImageView) view.findViewById(R.id.fb_row_img_icon);
//			TextView name = (TextView) view.findViewById(R.id.fb_row_txt_name);
//			if (icon != null) {
//				icon.setImageDrawable(listElement.getIcon());
//			}
//			if (name != null) {
//				name.setText(listElement.getName());
//			}
//		}
//		return view;
//	}

}