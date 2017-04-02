package brijeshjoshi.whatsyourwifi;

import android.util.Log;

/**
 * Created by brijeshjoshi on 4/1/17.
 */

class LogHelper {
    private static final String TAG = "WhatsYourWifi";

    static void logDebug(final String message) {
        Log.d(TAG, message);
    }

    static void logError(final String message) {
        Log.e(TAG, message);
    }

    static void logWarn(final String message) {
        Log.w(TAG, message);
    }

}
