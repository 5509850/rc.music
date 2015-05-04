package in.alexsoft.rc.music.Utils;

import in.alexsoft.rc.music.Network.HostBean;

import java.lang.IllegalStateException;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class Save {

    private static final String TAG = "Save";
    private static final String SELECT = "select name from nic where mac=?";
    private static final String INSERT = "insert or replace into nic (name,mac) values (?,?)";
    private static final String DELETE = "delete from nic where mac=?";
    private static SQLiteDatabase db;

    public void closeDb(){
        if(db != null && db.isOpen()){
            db.close();
        }
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

    public synchronized String getCustomName(HostBean host) {
        Log.e(TAG, "getCustomName");
        String name = null;
        Cursor c = null;
        try {
            db = getDb();
            c = db.rawQuery(SELECT, new String[] { host.hardwareAddress.replace(":", "").toUpperCase() });
            if (c.moveToFirst()) {
                name = c.getString(0);
            } else 
            	if(host.hostname != null) 
            	{
                name = host.hostname;
            	}
            	else
            		name = "null";
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());
        } catch (IllegalStateException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return name;
    }

    public void setCustomName(final String name, final String mac) {
        db = Db.openDb(Db.DB_SAVES, SQLiteDatabase.NO_LOCALIZED_COLLATORS|SQLiteDatabase.OPEN_READWRITE);
        try {
            if (db.isOpen()) {
                db.execSQL(INSERT, new String[] { name, mac.replace(":", "").toUpperCase() });
            }
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            closeDb();
        }
    }

    public boolean removeCustomName(String mac) {
        db = Db.openDb(Db.DB_SAVES, SQLiteDatabase.NO_LOCALIZED_COLLATORS|SQLiteDatabase.OPEN_READWRITE);
        try {
            if (db.isOpen()) {
                db.execSQL(DELETE, new String[] { mac.replace(":", "").toUpperCase() });
                return true;
            }
            return false;
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());
            return false;
        } finally {
            closeDb();
        }
    }

    private static synchronized SQLiteDatabase getDb(){
        if(db == null || !db.isOpen()) {
            // FIXME: read only ?
            db = Db.openDb(Db.DB_SAVES, SQLiteDatabase.NO_LOCALIZED_COLLATORS|SQLiteDatabase.OPEN_READONLY);
        }
        return db;
    }
}