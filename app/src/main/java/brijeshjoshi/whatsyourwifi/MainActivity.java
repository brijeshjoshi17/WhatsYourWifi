package brijeshjoshi.whatsyourwifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_APP_PERMISSIONS = 7;
    private WifiManager mWifiManager;
    private BroadcastReceiver mWifiScanReceiver;
    private PermissionManager mPermissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPermissionManager = new PermissionManager(this);

        if (mPermissionManager.hasPermissionsForApp()) {
            //connectToNetwork();

            mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context
                    .WIFI_SERVICE);

            scanAvailableNetworks();
        } else {
            mPermissionManager.requestAppPermissions(REQUEST_CODE_APP_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionManager != null && requestCode == REQUEST_CODE_APP_PERMISSIONS) {
            if (mPermissionManager.processPermissionResult(permissions, grantResults)) {
                //connectToNetwork();

                mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context
                        .WIFI_SERVICE);

                scanAvailableNetworks();
            }
        }
    }

    private void scanAvailableNetworks() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
        mWifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
                    determineWifiSecurityType();
                    unregisterReceiver(mWifiScanReceiver);
                }
            }
        };
        registerReceiver(mWifiScanReceiver, intentFilter);
        mWifiManager.startScan();
    }

    private void determineWifiSecurityType() {
        List<ScanResult> networkList = mWifiManager.getScanResults();

        //get current connected SSID for comparison to ScanResult
        WifiInfo wi = mWifiManager.getConnectionInfo();
        String currentSSID = wi.getSSID().replace("\"", "");

        if (networkList != null) {
            for (ScanResult network : networkList) {
                //check if current connected SSID
                if (currentSSID.equals(network.SSID)) {
                    //get capabilities of current connection
                    String capabilities = network.capabilities;
                    LogHelper.logDebug(network.SSID + " capabilities : " + capabilities);

                    if (capabilities.contains("WPA2")) {
                        //do something
                    } else if (capabilities.contains("WPA")) {
                        //do something
                    } else if (capabilities.contains("WEP")) {
                        //do something
                    }
                }
            }
        }
    }

    private void connectToNetwork() {
        WifiConfiguration wfc = new WifiConfiguration();
        wfc.SSID = PreferenceHelper.getStringPreference(PreferenceHelper.KEY_SSID);
        wfc.preSharedKey = String.format("\"%s\"", PreferenceHelper.getStringPreference
                (PreferenceHelper.KEY_PASSPHRASE));

        wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wfc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

        int netId = mWifiManager.addNetwork(wfc);
        mWifiManager.disconnect();
        mWifiManager.enableNetwork(netId, true);
        mWifiManager.reconnect();
    }
}
