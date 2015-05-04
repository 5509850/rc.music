package in.alexsoft.rc.music;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;

/* DB = client.db //wificallback.db
 * Table = hosts // records
 * row = _id, position, ipAddress, hardwareAddress, hostname, saved
 *					_id, mcallid, deviceid, phone, fio, isactive, begincall, endcall*/

public class DBAdapter {
	
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_POS = "position";	
	public static final String KEY_IP = "ipAddress";
	public static final String KEY_HARDMAC = "hardwareAddress";
	public static final String KEY_SAVED = "saved";
	public static final String KEY_HOST = "hostname";
	
	
	private static final String TAG = "DBAdapter";
	private static final String DATABASE_NAME = "client.db";
	private static final String DATABASE_TABLE = "hosts";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE =
	"create table IF NOT EXISTS hosts (_id integer primary key autoincrement, "
	+ "position integer, "	
	+ "ipAddress text not null, "
	+ "hardwareAddress text not null, "
	+ "saved text not null, "
	+ "hostname text not null);";
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;	
	
	private final SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
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
	
	
	public DBAdapter(Context ctx)
	{
	this.context = ctx;
	DBHelper = new DatabaseHelper(context);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
	
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(DATABASE_CREATE);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion,	int newVersion)
		{
			Log.w(TAG, "Upgrading database from version " + oldVersion
			+ " to "
			+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS hosts");
			onCreate(db);
		}
	}
	
	//---opens the database---
	public DBAdapter open() throws SQLException
	{
		db = DBHelper.getWritableDatabase();
	return this;
	}
	
	//---closes the database---
	public void close()
	{
		DBHelper.close();
	}
	
	private int ConvertBooleanToInt(boolean bit)
	{
		if (bit)
			return 1;
		return 0;
	}
	
	private boolean ConvertIntToBoolean(int vol)
	{		
		return (1 == vol);		
	}
	
	//---insert a level into the database---
	public long SavenewHost(int position, String ipAddress, String hardwareAddress, String hostname)
	{
		
//		public static final String KEY_POS = "position";	
//		public static final String KEY_IP = "ipAddress";
//		public static final String KEY_HARDMAC = "hardwareAddress";
//		public static final String KEY_SAVED = "saved";
//		public static final String KEY_HOST = "hostname";
//		
//		+ "position integer, "	
//		+ "ipAddress text not null, "
//		+ "hardwareAddress text not null, "
//		+ "saved text not null, "
//		+ "hostname text not null);";
		
		
//		public static final String KEY_ROWID = "_id";
//		public static final String KEY_POS = "position";	
//		public static final String KEY_IP = "ipAddress";
//		public static final String KEY_HARDMAC = "hardwareAddress";
//		public static final String KEY_SAVED = "saved";
//		public static final String KEY_HOST = "hostname";
//		
		
	ContentValues dataToInsert = new ContentValues();
	dataToInsert.put(KEY_POS,  position);
	dataToInsert.put(KEY_IP, ipAddress );
	dataToInsert.put(KEY_HARDMAC, hardwareAddress);
	dataToInsert.put(KEY_SAVED, getCurrentDateTime());	
	dataToInsert.put(KEY_HOST, hostname );
			
	
	return db.insert(DATABASE_TABLE, null, dataToInsert);
	}
	
	//---deletes a particular title---
	public boolean deleteAllRecords()
	{
	return db.delete(DATABASE_TABLE, KEY_POS +
	">" + -1, null) > 0;
	}
	
	public boolean deleteRecords(int id)
	{
	return db.delete(DATABASE_TABLE, KEY_ROWID +
	"=" + id, null) > 0;
	}
	
	//---retrieves all the titles---
	public Cursor getAllRecords()
	{
	return db.query(DATABASE_TABLE, new String[] {
			KEY_ROWID,
			KEY_POS,
			KEY_IP,
			KEY_HARDMAC,
			KEY_SAVED,			
			KEY_HOST
			},
			null,
	null,
	null,
	null,
	KEY_POS //+ " DESC " 
				);
	}
	
	//---retrieves a particular title---
//	public Cursor getCursorByparamID(boolean isActive) throws SQLException
//	{
//	Cursor mCursor =
//			db.query(true, DATABASE_TABLE, new String[] 
//					{
//					KEY_ROWID,
//					KEY_CALLID,
//					KEY_ISACTIVE,
//					KEY_BEGINCALL,
//					KEY_ENDCALL
//					},
//					KEY_ISACTIVE + "= " + ConvertBooleanToInt(isActive),
//				null,
//				null,
//				null,
//				null,
//				null);
//		if (mCursor != null) 
//		{
//			mCursor.moveToFirst();
//		}		
//	return mCursor;
//	}
	
	private  String getCurrentDateTime() 
	  {
	  	 Date date = new Date();
		 return  myDateFormat.format(date);
   }	
	
	
	public boolean updateHostName(long position, String hostname)	
	{			
		ContentValues dataToUpdate = new ContentValues();
		dataToUpdate.put(KEY_HOST, hostname );		
		
		return db.update(DATABASE_TABLE, dataToUpdate,
				KEY_POS + "=" + position, null) > 0;
	}
	
	public boolean updateHostPosition(long position, long id)	
	{			
		ContentValues dataToUpdate = new ContentValues();
		dataToUpdate.put(KEY_POS, position);		
		
		return db.update(DATABASE_TABLE, dataToUpdate,
				KEY_ROWID + "=" + id, null) > 0;
	}
	
	public Cursor getCursorByparamID(int pos) throws SQLException
	{
	Cursor mCursor =
			db.query(true, DATABASE_TABLE, new String[] 
					{								
					KEY_HOST
					},
					KEY_POS + "= " + pos,
				null,
				null,
				null,
				null,
				null);
		if (mCursor != null) 
		{
			mCursor.moveToFirst();
		}		
	return mCursor;
	}
	
//	public boolean updateCloseAllActiveCalls()	
//	{			
//		ContentValues dataToUpdate = new ContentValues();
//		dataToUpdate.put(KEY_ISACTIVE, ConvertBooleanToInt(false) );
//		dataToUpdate.put(KEY_ENDCALL, getCurrentDateTime() );
//		
//		return db.update(DATABASE_TABLE, dataToUpdate,
//				KEY_ISACTIVE + " = 1"  , null) > 0;
//	}
	
}