package in.alexsoft.rc.music;

//import com.ivengo.ads.AdManager;
import android.app.Application;

public class MusicApplication extends Application {
	
	private static MusicApplication instance;
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	//	AdManager.getInstance().initialize(this);
	}
	
	public static MusicApplication getInstance() {
		return instance;
	}
}
