package in.alexsoft.rc.music;

import in.alexsoft.rc.music.Network.HostBean;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

public class LevelHelperDB {

	 public int saveHost(DBAdapter db, HostBean host)
	    {
		 
		// int position, String ipAddress, String hardwareAddress, String hostname
		 //0 insert
		 //-2 error insert
	    	int result = -2;	     	
	    		
	    		db.open();
	    		long id = db.SavenewHost(host.position, host.ipAddress, host.hardwareAddress, host.hostname);
	    		//Toast.makeText(this, "id = " + id, Toast.LENGTH_LONG).show();//
	    		db.close();
	    		if (id != -1)//recods added to DB    		
	    			result = 0;   	
	    	
	    	return result;    	
	    }
	 
	 public void updateHostName(DBAdapter db, long position, String hostname)
	 {
		 db.open();
		 db.updateHostName(position, hostname);
		 db.close();
	 }
	 
	 public void moveUpHostName(DBAdapter db, long position)
	 {
		 List<HostBean> list = GetAllRecords(db);
		 if (list != null && list.size() > 1 && position != 0)
		 {
			 db.open();
			 long newpos = 1;
			 for (int i = 0; i < list.size(); i++)
			 {
				 if (i == position) //move this position to up
				 {
					 db.updateHostPosition(0, list.get(i).responseTime);// responseTime = KEY_ROWID
				 }
				 else
				 {
					 db.updateHostPosition(newpos, list.get(i).responseTime);// responseTime = KEY_ROWID
					 newpos++;
				 }
			 }			 
			 db.close();
		 }		 
	 }
	 
	 public void moveDownHostName(DBAdapter db, long position)
	 {
		 List<HostBean> list = GetAllRecords(db);
		 if (list != null && list.size() > 1 && position != list.size() - 1)
		 {
			 db.open();
			 long newpos = list.size() - 2;
			 for (int i = list.size() - 1; i > -1; i--)
			 {
				 if (i == position) //move this position to down
				 {
					 db.updateHostPosition(list.size() - 1, list.get(i).responseTime);// responseTime = KEY_ROWID
				 }
				 else
				 {
					 db.updateHostPosition(newpos, list.get(i).responseTime);// responseTime = KEY_ROWID
					 newpos--;
				 }
			 }			 
			 db.close();
		 }		 
	 }
	 
	 public String GetCurrentHostName(DBAdapter db, int pos)
	    {
		 	//-1 not found!!!!!!!!!!!!!!!
		 	//-3 Exception!!!!!!!!!!!!!!!
	    	String hostname = "MyPC";
	    	db.open();
	    	try{
	    		Cursor c = db.getCursorByparamID(pos);
		    	if (c == null)
		    		return hostname;
		    	if (c.moveToFirst())
		    	{
		    		hostname = c.getString(0) ; 
		    	}		    	
		    	
		    	c.close();
		    	db.close();	
	    	}
	    	catch (Exception e) {
	    		hostname = "error";
			}
	    		    	
	    	return hostname;
	    }
	 
	 public void deleteAll(DBAdapter db)
	 {
		 db.open();
		 db.deleteAllRecords();
		 db.close();
	 } 
	 
	 
	 public void removeHost(DBAdapter db, int pos)
	 {
		 List<HostBean> list = GetAllRecords(db);
		 if (list != null && list.size() > 0)
		 {
			 db.open();
			 long newpos = 0;
			 for (int i = 0; i < list.size(); i++)
			 {
				 if (i == pos) //move this position to down
				 {					
					 db.deleteRecords(list.get(i).responseTime);// responseTime = KEY_ROWID
				 }
				 else
				 {
					 db.updateHostPosition(newpos, list.get(i).responseTime);// responseTime = KEY_ROWID
					 newpos++;
				 }
			 }			 
			 db.close();
		 }
	 } 
	 
	
	 
	 public List<HostBean> GetAllRecords(DBAdapter db)
	    {
		 	List<HostBean> hosts = new ArrayList<HostBean>();
		 	     
			 	try{
			 	db.open();
			 	Cursor cursor = db.getAllRecords();			 	
			 	
			 			 if (cursor.moveToFirst()) {		 
			 			 do {
			 			 /*
			0 KEY_ROWID,
			1 KEY_POS,
			2 KEY_IP,
			3 KEY_HARDMAC,
			4 KEY_SAVED,			
			5 KEY_HOST*/		 				
			 				HostBean host = new HostBean();
			 				host.ipAddress =  cursor.getString(2);       
			 		        host.hardwareAddress =  cursor.getString(3);
			 		        host.hostname =  cursor.getString(5)  ;
			 		        host.os = " (" + cursor.getString(4)  + ")";
			 		        host.position = cursor.getInt(1);
			 		        host.responseTime = cursor.getInt(0); //KEY_ROWID
			 		        //+ "; deviceID = " + cursor.getString(7)  + ": \n" + cursor.getString(3) + "\nтел." + cursor.getString(2)  + ": \nc " + cursor.getString(5) + "\nпо " + cursor.getString(6)
			 				hosts.add(host);
			 			
			 			 } while (cursor.moveToNext());
			 			 
			 			 }
			 			 
			 			 if (cursor != null && !cursor.isClosed()) {
			 			 
			 			 cursor.close();
			 			 
			 			 }
			 			 
			 			 db.close();
			 		}
		 	catch (Exception e) {
		 		hosts.clear();
		 		HostBean host = new HostBean();
		 		host.hostname =
		 				host.ipAddress =
		 						host.hardwareAddress = e.getMessage(); 
		 		hosts.add(host);
			} 
		 	
		 			 
		 			 return hosts;	
	    }
	 
	 
	 
	 
//	 public void updateCloseAllActiveCalls(DBAdapter db)
//	 {
//		 db.open();
//		 db.updateCloseAllActiveCalls();
//		 db.close();
//	 }
	 
//	 public long GetCurrentCallid(DBAdapter db)
//	    {
//		 	//-1 not found!!!!!!!!!!!!!!!
//		 	//-3 Exception!!!!!!!!!!!!!!!
//	    	int callid = 0;
//	    	db.open();
//	    	try{
//	    		Cursor c = db.getCursorByparamID(true);
//		    	if (c == null)
//		    		return -1;
//		    	if (c.moveToFirst())
//		    	{
//		    		callid = Integer.parseInt( c.getString(1) ); 
//		    	}
//		    	else
//		    		callid = -1;//not found!!!!!!!!!!!!!!!
//		    	
//		    	c.close();
//		    	db.close();	
//	    	}
//	    	catch (Exception e) {
//	    		callid = -3;
//			}
//	    		    	
//	    	return callid;
//	    }
	 
	
	
	 
	
	/* 
	  //--------------------------------------------------------------------------------------------------------------------------------------------------------
	    private void update(){
	    	db.open();
	    	if (db.updateLevel(1, "123"))
	    	Toast.makeText(this, "Update successful.",
	    	Toast.LENGTH_LONG).show();
	    	else
	    	Toast.makeText(this, "Update failed.",
	    	Toast.LENGTH_LONG).show();
	    	//-------------------
	    	//---retrieve the same title to verify---
	    	Cursor c = db.getLevel(1);
	    	if (c.moveToFirst())
	    	DisplayLevel(c);
	    	else
	    	Toast.makeText(this, "No title found",
	    	Toast.LENGTH_LONG).show();
	    	//-------------------
	    	db.close();
	    	}
	    
	    private void insert()
	    {
	    	db.open();
	    	long id;
	    	id = db.insertLevel("9");    	
	    	Toast.makeText(this, "id = " + id, Toast.LENGTH_LONG).show();    	
	    	db.close();
	   	}
	    
	    private void disAll(){
	    	db.open();
	    	
	    	try
	    	{
	    		Cursor c = db.getAllTitles();
		    	if (c.moveToFirst())
		    	{
			    	do 
			    	{
				    	System.out.println("bool2");
				    	DisplayLevel(c);
			    	} 
			    	while (c.moveToNext());
		    	}
	    	
	    	}
	    	
	    	catch(Exception e)
	    	{
	    		System.out.println(e);
		    }
	    	
	    		db.close();
		    }
	    
	    private void dis ( int j ){
	    	db.open();
	    	Cursor c = db.getLevel(j);
	    	if (c.moveToFirst())
	    		DisplayLevel(c);
	    	else
	    		Toast.makeText(this, "No level found", Toast.LENGTH_LONG).show();
	    	db.close();
	    	}
	    
	    private void del ( int j ){
	    	db.open();
	    	if (db.deleteLevel(j))
	    	Toast.makeText(this, "Delete successful.",
	    	Toast.LENGTH_LONG).show();
	    	else
	    	Toast.makeText(this, "Delete failed.",
	    	Toast.LENGTH_LONG).show();
	    	db.close();
	    	}
	    
	    public void DisplayLevel(Cursor c)
	    {
	    System.out.println("bool");
	    Toast.makeText(this,
	    "id: " + c.getString(0) + "\n" +
	    "level: " + c.getString(1) + "\n", 
	    Toast.LENGTH_LONG).show();
	    }   
	*/
}
