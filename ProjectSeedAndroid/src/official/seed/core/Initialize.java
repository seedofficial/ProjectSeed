package official.seed.core;

import java.util.Collection;

import android.app.Application;
import android.content.Context;

import com.facebook.model.GraphUser;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

public class Initialize extends Application {

	private final static String PARSE_APPID = "HAz7nJLKWmHTN1kt3IHeXOABhN9aRKVT3v7znKTt";
	private final static String PARSE_CLIENT = "SL7akB7GczdPli1ROd88btxoGs1LbBvNd2aNNHgC";

	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, PARSE_APPID, PARSE_CLIENT);
		ParseFacebookUtils.initialize("1424091494500320");
		initImageLoader(getApplicationContext());

	}

	private Collection<GraphUser> selectedUsers;

	public Collection<GraphUser> getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(Collection<GraphUser> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	public static void initImageLoader(Context context) {
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method..discCacheFileNameGenerator(new Md5FileNameGenerator())
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.discCacheFileCount(80)
				.build();
		ImageLoader.getInstance().init(config);
	}
}
