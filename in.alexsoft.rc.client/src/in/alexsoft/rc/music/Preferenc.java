package in.alexsoft.rc.music;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceClickListener;
import android.provider.Settings;
import android.widget.Toast;


public class Preferenc extends PreferenceActivity 
{
	 private Context ctxt;
	 private PreferenceScreen ps = null;
	 private static final int wifi = 1;
	
	private static final String OPT_FTP = "serverftp" ;
	private static final String OPT_FTP_DEF = "192.168.1.0";
	
	private static final String OPT_PORTFTP = "portftp" ;	
	private static final String OPT_PORTFTP_DEF = "22";
	
	private static final String OPT_PORTUDP = "portudp" ;
	private static final String OPT_PORTUDP_DEF = "4568";
	
	private static final String OPT_NAMEFTP = "nameftp" ;
	private static final String OPT_NAMEFTP_DEF = "qwe#sdf6AsdfgzTs";
	
	private static final String OPT_PASSFTP = "passftp" ;
	private static final String OPT_PASSFTP_DEF = "test";	
	
	
	private static final String OPT_VIBRO = "vibro" ;
	private static final boolean OPT_VIBRO_DEF = true;
	
		
	private static final String OPT_SIMPLE = "ready" ;//ready to work and pair
	private static final boolean OPT_SIMPLE_DEF = true;
		
	
	private static final String OPT_STATUS = "statusconnect" ;
	private static final String OPT_STATUS_DEF = "1";
	
	private static final String OPT_MAC = "mac" ;
	private static final String OPT_MAC_DEF = "XX-XX-XX-XX-XX-XX";
		
	
	private static final String OPT_SERVERTYPE= "servertype" ;
	private static final String OPT_SERVERTYPE_DEF = "1"; //ftp= 2; udp = 1 windows 7
	
	private static final String windows7 = "1"; 
	private static final String windows8 = "2";
			
	public static final String KEY_WIFI = "wifi";
	public static final String KEY_MANUAL = "manual";
	public static final String KEY_ABOUT = "about";
	
	private static final String OPT_TIMERSUTDOWN = "timershutdown" ;//ready to work and pair
	private static final boolean OPT_TIMERSUTDOWN_DEF = true;
	
	private static final String OPT_AUTOCODE = "autocode" ;
	private static final boolean OPT_AUTOCODE_DEF = false;
	
	private static final String OPT_FIRSTSTART = "firststart" ;
	private static final boolean OPT_FIRSTSTART_DEF = true;
		
	@Override
   protected void onCreate(Bundle savedInstanceState) 
	{
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.settings);
      
      ctxt = getApplicationContext();

      ps = getPreferenceScreen();
      //ps.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
      
     // SharedPreferences sharedpreferences;

      if (Preferenc.getServerTypeDefault(ctxt).equals(windows7))
      {
    	  ((Preference) ps.findPreference(OPT_SERVERTYPE)).setTitle("Windows 7 PC"); 
    	  Preferenc.setServerTypeDefault(ctxt, windows7);
      }
      else
      {
    	  ((Preference) ps.findPreference(OPT_SERVERTYPE)).setTitle("Windows 8 PC");
    	  Preferenc.setServerTypeDefault(ctxt, windows8);
      }
      
      ((Preference) ps.findPreference(OPT_SERVERTYPE))
      .setOnPreferenceChangeListener(new OnPreferenceChangeListener(){
    	  public boolean onPreferenceChange(Preference preference, Object newValue) {
              System.out.println("Twitter Preference Changed!");
              if (newValue.toString().equals(windows7)) {
            	  ((Preference) ps.findPreference(OPT_SERVERTYPE)).setTitle("Windows 7 PC");                  
              } else {
            	  ((Preference) ps.findPreference(OPT_SERVERTYPE)).setTitle("Windows 8 PC");
              }
              return true;
          }
      });
//      .setOnPreferenceChangeListener(new OnPreferenceChangeListener){       
//   						
//						public boolean onPreferenceChange(
//								Preference preference, Object newValue) {
//							 if (Preferenc.getServerTypeDefault(ctxt).equals(windows7))
//	   						 {
//	   				    	  ((Preference) ps.findPreference(OPT_SERVERTYPE)).setTitle("Windows 7 PC"); 
//	   				    	  
//	   						 }
//	   						 else
//	   						 {
//	   				    	  ((Preference) ps.findPreference(OPT_SERVERTYPE)).setTitle("Windows 8 PC");   				    	 
//	   						 }		
//							return false;
//						}
//   					});
      	
      // Wifi settings listener
   	  ((Preference) ps.findPreference(KEY_WIFI))
   					.setOnPreferenceClickListener(new OnPreferenceClickListener() {
   						public boolean onPreferenceClick(Preference preference) {
   							
   							if(haveNetworkConnectionType(ctxt) == wifi) 
   							{
   								startActivity(new Intent(ctxt, ADiscovery.class));
   								finish();
   							}   							
   								
   									else
   									{
   										Toast.makeText(ctxt, getResources().getString(R.string.needwifi) , Toast.LENGTH_LONG).show();
   										startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
   									}   							
   							return true;
   						}
   					});
   	  
   // MANUAL settings listener
   	  ((Preference) ps.findPreference(KEY_MANUAL))
   					.setOnPreferenceClickListener(new OnPreferenceClickListener() {
   						public boolean onPreferenceClick(Preference preference) {
   							
   							
   								startActivity(new Intent(ctxt, StartWizard.class));//!!!!!!!!!!!!!!MANUAL!!!!!!!!!!!!
   								finish();   							   							
   							return true;
   						}
   					});
   	  
   	  // ABOUT settings listener
   	  ((Preference) ps.findPreference(KEY_ABOUT))
   					.setOnPreferenceClickListener(new OnPreferenceClickListener() {
   						public boolean onPreferenceClick(Preference preference) {
   							
   							
   							startActivity(new Intent(ctxt, About.class));   								   							   							
   							return true;
   						}
   					});
    }
	

	
	public static void setReady(Context context, boolean Newvalue) 
	{
	   // PreferenceManager.getDefaultSharedPreferences(context)
	     //    .getBoolean(OPT_REC, OPT_REC_DEF);
	    
		Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();		
		editor.putBoolean(OPT_SIMPLE, Newvalue);
		editor.commit();
	}
	
	
	/** Get the current value of the music option */
		
	
	public static void setNewMacAdress(Context context, String Newvalue) 
	{
	   // PreferenceManager.getDefaultSharedPreferences(context)
	     //    .getBoolean(OPT_REC, OPT_REC_DEF);
	    
		Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();		
		editor.putString(OPT_MAC, SplitMacClear(Newvalue));
		editor.commit();
	}
	
	public static void setStatus(Context context, String Newvalue) 
	{
	   // PreferenceManager.getDefaultSharedPreferences(context)
	     //    .getBoolean(OPT_REC, OPT_REC_DEF);
	    
		Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();		
		editor.putString(OPT_STATUS, Newvalue);
		editor.commit();
	}
	
	public static boolean getFIRSTSTART(Context context) 
	{
	   return PreferenceManager.getDefaultSharedPreferences(context)
	         .getBoolean(OPT_FIRSTSTART, OPT_FIRSTSTART_DEF);
	}
	
	public static void setFIRSTSTART(Context context, boolean Newvalue) 
	{
	   // PreferenceManager.getDefaultSharedPreferences(context)
	     //    .getBoolean(OPT_REC, OPT_REC_DEF);
	    
		Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();		
		editor.putBoolean(OPT_FIRSTSTART, Newvalue);
		editor.commit();
	}
	
	public static String getStatus(Context context) 
	{
	   return SplitIpClear(PreferenceManager.getDefaultSharedPreferences(context)
	         .getString(OPT_STATUS, OPT_STATUS_DEF));
	}
	
	public static boolean getAutoCode(Context context) 
	{
	   return PreferenceManager.getDefaultSharedPreferences(context)
	         .getBoolean(OPT_AUTOCODE, OPT_AUTOCODE_DEF);
	}
	
	public static void setNewPass(Context context, String Newvalue) 
	{
	   // PreferenceManager.getDefaultSharedPreferences(context)
	     //    .getBoolean(OPT_REC, OPT_REC_DEF);
	    
		Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();		
		editor.putString(OPT_PASSFTP, SplitIpClear(Newvalue));
		editor.commit();
	}
	
	public static void setNewIpAdress(Context context, String Newvalue) 
	{
	   // PreferenceManager.getDefaultSharedPreferences(context)
	     //    .getBoolean(OPT_REC, OPT_REC_DEF);
	    
		Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();		
		editor.putString(OPT_FTP, SplitIpClear(Newvalue));
		editor.commit();
	}
	
	public static boolean getTIMERSHUTDOWN(Context context) 
	{
	   return PreferenceManager.getDefaultSharedPreferences(context)
	         .getBoolean(OPT_TIMERSUTDOWN, OPT_TIMERSUTDOWN_DEF);
	}
	
	public static void setTIMERSHUTDOWN(Context context, boolean Newvalue) 
	{
	   // PreferenceManager.getDefaultSharedPreferences(context)
	     //    .getBoolean(OPT_REC, OPT_REC_DEF);
	    
		Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();		
		editor.putBoolean(OPT_TIMERSUTDOWN, Newvalue);
		editor.commit();
	}
	
	//------------------------------------------------------------------------
	
	
	public static String getFtpServer(Context context) 
	{
	   return SplitIpClear(PreferenceManager.getDefaultSharedPreferences(context)
	         .getString(OPT_FTP, OPT_FTP_DEF));
	}
	
	public static String getFtpPort(Context context) 
	{
	   return PreferenceManager.getDefaultSharedPreferences(context)
	         .getString(OPT_PORTFTP, OPT_PORTFTP_DEF);
	}
	
	public static String getUdpPort(Context context) 
	{
	   return PreferenceManager.getDefaultSharedPreferences(context)
	         .getString(OPT_PORTUDP, OPT_PORTUDP_DEF);
	}
	
	public static String getFtpLogin(Context context) 
	{
	   return PreferenceManager.getDefaultSharedPreferences(context)
	         .getString(OPT_NAMEFTP, OPT_NAMEFTP_DEF);
	}
	
	public static String getFtpPass(Context context) 
	{
	   return SplitIpClear(PreferenceManager.getDefaultSharedPreferences(context)
	         .getString(OPT_PASSFTP, OPT_PASSFTP_DEF));
	}
	
	public static String getServerTypeDefault(Context context) 
	{
	   return PreferenceManager.getDefaultSharedPreferences(context)
	         .getString(OPT_SERVERTYPE, OPT_SERVERTYPE_DEF);
	}
	
	public static void setServerTypeDefault(Context context, String Newvalue) 
	{   
		 Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();		
		editor.putString(OPT_SERVERTYPE, Newvalue);
		editor.commit();
	}
	
	public static boolean getVibro(Context context) 
	{
	   return PreferenceManager.getDefaultSharedPreferences(context)
	         .getBoolean(OPT_VIBRO, OPT_VIBRO_DEF);
	}
	
		
	public static boolean getReady(Context context) 
	{
	   return PreferenceManager.getDefaultSharedPreferences(context)
	         .getBoolean(OPT_SIMPLE, OPT_SIMPLE_DEF);
	}
	
	public static String getMac(Context context) 
	{
	   return SplitIpClear(PreferenceManager.getDefaultSharedPreferences(context)
	         .getString(OPT_MAC, OPT_MAC_DEF));
	}
	
	//очищаем ip адрес от пробелов
	private static String SplitIpClear(String ip)
    {
    	
    	if (ip == null || ip.equals(""))
    		return "192.168.1.0";
    	try {    	          
    	          return  ip.replaceAll("\\s", "");
    	}
    	catch (Exception ex)
    	{
    		return "192.168.1.0";
    	}  	
    }
	
	//очищаем mac адрес от пробелов
			private static String SplitMacClear(String ip)
		    {
		    	
		    	if (ip == null || ip.equals(""))
		    		return "XX-XX-XX-XX-XX-XX";
		    	try {    	          
		    	          return  ip.replaceAll("\\s", "").replaceAll("o","0").replaceAll("O","0").replaceAll("a","A").replaceAll("b","B").replaceAll("c","C").replaceAll("d","D").replaceAll("e","E").replaceAll("f","F");
		    	}
		    	catch (Exception ex)
		    	{
		    		return "XX-XX-XX-XX-XX-XX";
		    	}  	
		    }
			
	
	 private int  haveNetworkConnectionType(Context context)
	    {
			//return int
			//0 - no network
			//1 - only wifi
			//2 - only 3G
			int WIFI = 1;
			int GGG = 2;
			int type = 0;
			
	        boolean haveConnectedWifi = false;
	        boolean haveConnectedMobile = false;

	        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	        for (NetworkInfo ni : netInfo)
	        {
	            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            {
	                if (ni.isConnected())
	                {
	                    haveConnectedWifi = true;
	                    //Log.v("WIFI CONNECTION ", "AVAILABLE");
	                    //Toast.makeText(this,"WIFI CONNECTION is Available", Toast.LENGTH_LONG).show();
	                    type = WIFI; 
	                } else
	                {
	                   // Log.v("WIFI CONNECTION ", "NOT AVAILABLE");
	                    //Toast.makeText(this,"WIFI CONNECTION is NOT AVAILABLE", Toast.LENGTH_LONG).show();
	                }
	            }
	            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            {
	                if (ni.isConnected())
	                {
	                    haveConnectedMobile = true;
	                   // Log.v("MOBILE INTERNET CONNECTION ", "AVAILABLE");
	                    //Toast.makeText(this,"MOBILE INTERNET CONNECTION - AVAILABLE", Toast.LENGTH_LONG).show();
	                    type = GGG;
	                } else
	                {
	                  // Log.v("MOBILE INTERNET CONNECTION ", "NOT AVAILABLE");
	                    //Toast.makeText(this,"MOBILE INTERNET CONNECTION - NOT AVAILABLE", Toast.LENGTH_LONG).show();
	                }
	            }
	        }
	        if (!haveConnectedWifi && !haveConnectedMobile)
	        	return 0;
	        
	        return type;
	    }

//	
	
}