package in.alexsoft.rc.music;

import java.io.IOException;
import java.net.SocketException;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.RemoteViews;
import android.widget.Toast;

public class widgetButtonNext extends  AppWidgetProvider {
	
	public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";
	
	  private static final int wifi = 1;
 	  private static final int GGG = 2;
 	  int code = 2;
 	  
 
     //-------------------------------------------------audioPlayer - new menu

      int aVOLUP = 4009;
      int aVOLDWN = 4010;
      int aSTOP = 4011;
      int aNEXT = 4012;
      int aRESTORE = 4013;
      int aPREV = 4014;
      int aMUTE = 4015;
      int aPAUSE = 4016;
      int aPLAY = 4017;
     //-------------------------------------------------PhotoViewver - new menu

    
    
      
 	  
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
	 
	//update if widget every 86400 sec
  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
       //Создаем новый RemoteViews
       RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widgetnext);

       //Подготавливаем Intent для Broadcast
       Intent active = new Intent(context, widgetButtonNext.class);
       active.setAction(ACTION_WIDGET_RECEIVER);
       active.putExtra("msg", "Hello Habrahabr");

       //создаем наше событие
       PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);

       //регистрируем наше событие
       remoteViews.setOnClickPendingIntent(R.id.widgeticonnext, actionPendingIntent);

       //обновляем виджет
       appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
  }

  @Override
  public void onReceive(final Context context, Intent intent) {

       //Ловим наш Broadcast, проверяем и выводим сообщение
       final String action = intent.getAction();
       if (ACTION_WIDGET_RECEIVER.equals(action)) {
            String msg = "null";
            try {
                  msg = intent.getStringExtra("msg");
            } catch (NullPointerException e) {
                  Log.e("Error", "msg = null");
            }
            //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            
            if(!CheckWiFi(context))
			{
            	Toast.makeText(context, context.getResources().getString(R.string.needwifi), Toast.LENGTH_LONG).show();
			 return;
			 
			}
            
            if(Preferenc.getFtpServer(context).equals("192.168.1.0") && Preferenc.getServerTypeDefault(context).equals("2"))
			{
            	Toast.makeText(context, context.getResources().getString(R.string.ipadresstip) , Toast.LENGTH_LONG).show();
            	return;
			 
			}
				operate(aNEXT, context);
       } 
       super.onReceive(context, intent);
 }
  
  private boolean CheckWiFi(Context context)
  {
  	return (haveNetworkConnectionType(context) == wifi);
  }  
   private  void operate(int code, Context context)
	{
	  	String key = "empty";
	  	if (Preferenc.getServerTypeDefault(context).equals("1"))
  	{	 
  		UDPsend.sendUdpCommand(code, key, context, context.getResources().getString(R.string.versionprogram));
  			
  	}   
  	else
  	{
  	  	FtpLibrary ftp = new FtpLibrary();
       	//Toast.makeText(context, "*0*", Toast.LENGTH_LONG).show();
   		int result = 0;
   		//FtpLibrary ftp = new FtpLibrary();
   		try {
   			if (!FtpLibrary.connect(context))
   			{	 		
   				Preferenc.setReady(context, false);
   				return;
   			}							
   			//Toast.makeText(context, "*2*", Toast.LENGTH_LONG).show();
   			result = FtpLibrary.sendCommand(code);
   			//Toast.makeText(context, "*3*", Toast.LENGTH_LONG).show();
   			FtpLibrary.disconnect();
   			Preferenc.setReady(context, true);
   		//	Toast.makeText(context, "*4*", Toast.LENGTH_LONG).show();
   		} catch (SocketException e) {
   			Toast.makeText(context, "1 SocketException = " + e.getMessage(), Toast.LENGTH_LONG).show();
   			Preferenc.setReady(context, false);
   			//e.printStackTrace();
   		} catch (IOException e) {
   			Preferenc.setReady(context, false);
   			Toast.makeText(context, "2 IOException = " + e.getMessage(), Toast.LENGTH_LONG).show();
   			//e.printStackTrace();
   		}
   		catch (Exception e) {
   			Preferenc.setReady(context, false);
   			Toast.makeText(context, "3 Exception = " + e.getMessage(), Toast.LENGTH_LONG).show();
   			//e.printStackTrace();
   		}
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
                  type = GGG;
              } 
          }
      }
      if (!haveConnectedWifi && !haveConnectedMobile)
      	return 0;
      
      return type;
  }
  
  

}