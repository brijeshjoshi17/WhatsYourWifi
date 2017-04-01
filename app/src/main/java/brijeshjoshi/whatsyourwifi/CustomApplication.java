package brijeshjoshi.whatsyourwifi;

import android.app.Application;

/**
 * Created by brijeshjoshi on 4/1/17.
 */

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceHelper.initialize(getApplicationContext());
    }
}

