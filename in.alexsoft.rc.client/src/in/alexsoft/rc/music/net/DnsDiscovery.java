package in.alexsoft.rc.music.net;

import in.alexsoft.rc.music.Network.HardwareAddress;
import in.alexsoft.rc.music.Network.HostBean;
import in.alexsoft.rc.music.Network.NetInfo;
import in.alexsoft.rc.music.Utils.Prefs;

import java.io.IOException;
import java.net.InetAddress;

import android.database.sqlite.SQLiteDatabaseCorruptException;

public class DnsDiscovery extends AbstractDiscovery {

    private final String TAG = "DnsDiscovery";

    public DnsDiscovery(ActivityDiscovery discover) {
        super(discover);
    }
    
	public static class Log {
	    private static final boolean logEnabled = false;

	    public static void i(String tag, String string) {
	        if (logEnabled) android.util.Log.i(tag, string);
	    }
	    public static void e(String tag, String string) {
	        if (logEnabled) android.util.Log.e(tag, string);
	    }
	    public static void d(String tag, String string) {
	        if (logEnabled) android.util.Log.d(tag, string);
	    }
	    public static void v(String tag, String string) {
	        if (logEnabled) android.util.Log.v(tag, string);
	    }
	    public static void w(String tag, String string) {
	        if (logEnabled) android.util.Log.w(tag, string);
	    }
	}

    @Override
    protected Void doInBackground(Void... params) {
        if (mDiscover != null) {
            final ActivityDiscovery discover = mDiscover.get();
            if (discover != null) {
                Log.i(TAG, "start=" + NetInfo.getIpFromLongUnsigned(start) + " (" + start
                        + "), end=" + NetInfo.getIpFromLongUnsigned(end) + " (" + end
                        + "), length=" + size);

                int timeout = Integer.parseInt(discover.prefs.getString(Prefs.KEY_TIMEOUT_DISCOVER,
                        Prefs.DEFAULT_TIMEOUT_DISCOVER));
                Log.i(TAG, "timeout=" + timeout + "ms");

                for (long i = start; i < end + 1; i++) {
                    hosts_done++;
                    HostBean host = new HostBean();
                    host.ipAddress = NetInfo.getIpFromLongUnsigned(i);
                    try {
                        InetAddress ia = InetAddress.getByName(host.ipAddress);
                        host.hostname = ia.getCanonicalHostName();
                        host.isAlive = ia.isReachable(timeout) ? 1 : 0;
                    } catch (java.net.UnknownHostException e) {
                        Log.e(TAG, e.getMessage());
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                    if (host.hostname != null && !host.hostname.equals(host.ipAddress)) {
                        // Is gateway ?
                        if (discover.net.gatewayIp.equals(host.ipAddress)) {
                            host.deviceType = 1;
                        }
                        // Mac Addr
                        host.hardwareAddress = HardwareAddress.getHardwareAddress(host.ipAddress);
                        // NIC vendor
                        try {
                            host.nicVendor = HardwareAddress.getNicVendor(host.hardwareAddress);
                        } catch (SQLiteDatabaseCorruptException e) {
                            Log.e(TAG, e.getMessage());
                        }
                        publishProgress(host);
                    } else {
                        publishProgress((HostBean) null);
                    }
                }
            }
        }
        return null;
    }

}