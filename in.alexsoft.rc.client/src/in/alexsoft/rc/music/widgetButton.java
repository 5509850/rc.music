package in.alexsoft.rc.music;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import android.widget.RemoteViews;
import android.widget.Toast;

public class widgetButton extends  AppWidgetProvider {
	public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";
	
	  private static final int no_network = 0; 
 	  private static final int wifi = 1;
 	  private static final int GGG = 2;
 	  
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
       //������� ����� RemoteViews
       RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

       //�������������� Intent ��� Broadcast
       Intent active = new Intent(context, widgetButton.class);
       active.setAction(ACTION_WIDGET_RECEIVER);
       active.putExtra("msg", "Hello Habrahabr");

       //������� ���� �������
       PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);

       //������������ ���� �������
       remoteViews.setOnClickPendingIntent(R.id.widgeticon, actionPendingIntent);

       //��������� ������
       appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
  }

  @Override
  public void onReceive(final Context context, Intent intent) {

       //����� ��� Broadcast, ��������� � ������� ���������
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
			 Toast.makeText(context, context.getResources().getString(R.string.needwifi) , Toast.LENGTH_LONG).show();
//				
//			 AlertDialog.Builder builder = new AlertDialog.Builder(context);		 
//			 builder.setMessage("need wifi connection")
//			        .setCancelable(false)
//			        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//			            public void onClick(DialogInterface dialog, int id) 
//			            {
//			            	//YESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
//			            	context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
//			            }
//			        })
//			        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//			            public void onClick(DialogInterface dialog, int id) 
//			            {
//			                 dialog.cancel();
//			                 return;
//			            }
//			        });
//			 AlertDialog alert = builder.create();
//			 alert.show();
			 return;
			}
           
            if (Preferenc.getMac(context).equals("XX-XX-XX-XX-XX-XX"))
			{
				Toast.makeText(context, context.getResources().getString(R.string.entermac), Toast.LENGTH_LONG).show();
				//context.startActivity(new Intent(context, ADiscovery.class));
				return;
			}
            
            WakeOnLan.WakeUp("255.255.255.255", Preferenc.getMac(context));
       } 
       super.onReceive(context, intent);
 }
  
  private boolean CheckWiFi(Context context)
  {
  	return (haveNetworkConnectionType(context) == wifi);
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