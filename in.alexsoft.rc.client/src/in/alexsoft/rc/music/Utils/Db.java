package in.alexsoft.rc.music.Utils;

import in.alexsoft.rc.music.Network.DownloadFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.zip.GZIPInputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;


class Log {
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

public class Db {

    private final static String TAG = "Db";
    private Context ctxt = null;

    // Databases information
    public static final String PATH = "/data/data/in.alexsoft.rc.music/files/";
    public static final String DB_SERVICES = "services.db";
    public static final String DB_PROBES = "probes.db";
    public static final String DB_NIC = "nic.db";
    public static final String DB_SAVES = "saves.db";

    public Db(Context ctxt) {
        this.ctxt = ctxt;
        // new File(PATH).mkdirs();
    }

    public static SQLiteDatabase openDb(String db_name) {
        return openDb(db_name, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
    }

    public static SQLiteDatabase openDb(String db_name, int flags) {
        try {
            return SQLiteDatabase.openDatabase(PATH + db_name, null, flags);
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    public void copyDbToDevice(int res, String db_name) throws NullPointerException, IOException {
        // InputStream in = ctxt.getResources().openRawResource(res);
        GZIPInputStream in = new GZIPInputStream(ctxt.getResources().openRawResource(res));
        OutputStream out = ctxt.openFileOutput(db_name, Context.MODE_PRIVATE);
        final ReadableByteChannel ic = Channels.newChannel(in);
        final WritableByteChannel oc = Channels.newChannel(out);
        DownloadFile.fastChannelCopy(ic, oc);
    }
}