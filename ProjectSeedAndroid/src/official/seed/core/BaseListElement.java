package official.seed.core;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.BaseAdapter;

public abstract class BaseListElement {
	private Drawable icon;
	private String name;
	private String fbid;
	private int requestCode;
	private BaseAdapter adapter;

	public BaseListElement(Drawable icon, String name, String fbid, int requestCode) {
		super();
		this.icon = icon;
		this.name = name;
		this.fbid = fbid;
		this.requestCode = requestCode;
	}

	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter;
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	public String getName() {
		return name;
	}

	public String getFacebookId() {
		return fbid;
	}

	public Drawable getIcon() {
		return icon;
	}

	protected abstract View.OnClickListener getOnClickListener();
}
