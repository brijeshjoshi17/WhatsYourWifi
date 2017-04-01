package brijeshjoshi.whatsyourwifi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by brijeshjoshi on 4/1/17.
 */

class PreferenceHelper {

    private static final String TAG = "whats_your_wifi_prefs";
    private static SharedPreferences sSharedPreferences;

    static final String KEY_SSID = "ssid";
    static final String KEY_PASSPHRASE = "passphrase";

    static void initialize(Context context) {
        sSharedPreferences = context.getSharedPreferences(TAG, 0);
    }

    static String getStringPreference(final String key) {
        return getStringPreference(key, "");
    }

    private static String getStringPreference(final String key, final String defaultValue) {
        return sSharedPreferences.getString(key, defaultValue);
    }

    @SuppressLint("ApplySharedPref")
    public static void setStringPreference(final String key, final String value) {
        sSharedPreferences.edit().putString(key, value).commit();
    }
}
