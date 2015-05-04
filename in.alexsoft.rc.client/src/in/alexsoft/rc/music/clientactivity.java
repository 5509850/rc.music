package in.alexsoft.rc.music;

/*
 * 2 проверить скачивание по ftp
3 проверить рекламу на рекламных активити

 * */


/*
ПРОВЕРИТЬ ЯЗЫК НА ВИРТУАЛЬНОЙ МАШИНЕ С АНГЛ ВИНДОЙ!!!!!!!!!!!!!!!

инструкция - 
http://www.intelliadmin.com/index.php/remote-control-client-manual/
 
5 добавить ТВ на вебконтроле

1 drawable all resolution
2 orientation landscape layers
3 ftp file transfer - add context menu to RCC - to PC, from PC(rcc take from pc to download)
4 make screenshot and mouse control (fast control - make ftp connections and send only command to server!!!)
5 selec films from define folders (more than one) - menu for start vlc or other program (select in server) (fast control - make ftp connections and send only command to server!!!)
 * 1 вместо создания файла и копирования просто копировать файлы зи внутреннего хранилиза assets (1.in)
 * myWebView.loadUrl(getResources().getString(R.string.searchurl));
 * <string name="searchurl">file:///android_asset/search.html</string>
  
 5 в настройки добавить музыку фоновую марьвана не будите спящих 
 5 звонок -пауза

3 если программа не найдена запускать сайт, где можно её скачать и установить 

---------------------------------------------------------------------------------------------
0 добавить 3 мин и 45 мин. в таймер 
1 убрать скринсейвер вместо часов
2 опционально - создавать каждый раз соединение или поддерживать старое

при входящем звонке	и закрытым Windows Media Player он запускается (глюк-фишка) 
было бы интереснее если он воспроизводил оповещение о входящем или просто лучше исправить 
этот глюк и OSD выводила оповещение :)

3 улучшить работу подключения убрать в потоке только при сканировании!!!!!!!!!!
4 Добавить 45 минут в таймер

6 блокировка экрана (фон) в сервер
7 сохранять в базу найденные компьютеры и выводить их до начала сканирования со временем и давать возмонжость переименовать
8 сделать другую версию для планшетов с большими экранами и под 4 андроид
9 пережедать путь для папки по умолчанию in - есть вопросы с правами на создание папки в 4андроид
10 Добавить программу сканирования mp3/flac и avi/mp4/mkv с сохранением в xml и чтением в клиенте с запуском...!!!!
11 добавить поиск по торрентам из базы и воспроизведение

12 записать демо видео с программой

управление мышью
http://stackoverflow.com/questions/3476779/how-to-get-the-touch-position-in-android
http://stackoverflow.com/questions/2939332/get-the-co-ordinates-of-a-touch-event-on-android
http://habrahabr.ru/company/mailru/blog/182750/
http://habrahabr.ru/company/mailru/blog/196912/


open source
http://4pda.ru/forum/index.php?showtopic=200537
http://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D0%BA%D0%BE%D0%B4%D0%BE%D0%B2_%D0%BE%D1%82%D0%B2%D0%B5%D1%82%D0%BE%D0%B2_FTP

 * */

/**/

import java.io.IOException;
import java.net.SocketException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.*;


//import com.ivengo.ads.AdType;
//import com.ivengo.ads.AdView;
//import com.ivengo.ads.DefaultInterstitialListener;
//import com.ivengo.ads.Error;
//import com.ivengo.ads.Interstitial;
//import com.ivengo.ads.InterstitialActivity;
//import com.ivengo.ads.Request;



/*
 * 

2 убрать из лога лишнюю инфу

 * */

public class clientactivity extends Activity implements OnClickListener    {
    /** Called when the activity is first created. */
	
//	protected AdView adView;
//	private static final String APP_ID = "apw5btz5ginq";
//	private Interstitial interstitial;
	private ProgressDialog progressDialog;
	 private AdView adView;
	 private InterstitialAd interstitial;
	 private LinearLayout layout;
	// private LinearLayout mainLayoutcenter;
	
	 
	 Button buttonMute,
	 status,
	 mute_player,
	 lockscreen,
	 buttonpower,
	 button_vol_percent,
	 button_show_time,
	 button_vol_up,
	 buttonTimer,
	 button_prev_track,
	 buttonPlay,
	 button_next_track,
	 button_full_stop,
	 button_vol_down,
	 button_pause,
	 button_full_screen,
	 button_playlist,
	 button_settings; 
				
		private Context context = this;
		
		String defaultIP = "192.168.1.0";
		String defaultCode = "test";
		
		private int FailConnections = 0;
		
		 private static final int POWER_ON = 0;
	     private static final int POWER_OFF = 1;     
	     private static final int POWER_REBOOT= 2;
	     private static final int POWER_SHUTDOWN = 3;
	     private static final int POWER_hibernate = 4;
	     private static final int POWER_Standby = 5;
	     
	     //CODE----------------------------------------
	     int mute = 1;
	     int volup = 2;
	     int voldown = 3;	     
	     int audiosearchStart = 5000;	     
	     int shutdown = 8;
	     int reboot = 9;
	     int poweroff = 10;
	     int hibernate = 31;
	     int standby = 32;
	     int aVOLUP = 4009;
        int aVOLDWN = 4010;
        int aSTOP = 4011;
        int aNEXT = 4012;
        int aRESTORE = 4013;
        int aPREV = 4014;
        int aMUTE = 4015;
        int aPAUSE = 4016;
        int aPLAY = 4017;
        int nowtime = 42;//вывод на экран круглого времени
        int lockscr = 1100;
        int aKill = 4028;
	     //---------------------------------------------
	     
	     private MACAddressValidator macAddressValidator;
		
		private static final int no_network = 0; 
		private static final int wifi = 1;
		private static final int GGG = 2;
		FtpLibrary ftp = new FtpLibrary();
		private Vibrator mVibrator;
		private static final int VIBRATE_MILLIS = 50;
		private static final String MY_AD_INTER_UNIT_ID = "ca-app-pub-2532664048077866/2168673839";
		private static final String MY_AD_UNIT_ID = "ca-app-pub-2532664048077866/9691940630";
				

		
	
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
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		 setContentView(R.layout.activity_fullscreen);
		 mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		 
		 buttonMute = (Button)findViewById(R.id.buttonMute);
		 status = (Button)findViewById(R.id.status);
		 mute_player = (Button)findViewById(R.id.mute_player);
		 lockscreen = (Button)findViewById(R.id.lockscreen);
		 buttonpower = (Button)findViewById(R.id.buttonpower);
		 button_vol_percent = (Button)findViewById(R.id.button_vol_percent);
		 button_show_time = (Button)findViewById(R.id.button_show_time);
		 button_vol_up = (Button)findViewById(R.id.button_vol_up);
		 buttonTimer = (Button)findViewById(R.id.buttonTimer);
		 button_prev_track = (Button)findViewById(R.id.button_prev_track);
		 buttonPlay = (Button)findViewById(R.id.buttonPlay);
		 button_next_track = (Button)findViewById(R.id.button_next_track);
		 button_full_stop = (Button)findViewById(R.id.button_full_stop);
		 button_vol_down = (Button)findViewById(R.id.button_vol_down);
		 button_pause = (Button)findViewById(R.id.button_pause);
		 button_full_screen = (Button)findViewById(R.id.button_full_screen);
		 button_playlist = (Button)findViewById(R.id.button_playlist); 
		 button_settings = (Button)findViewById(R.id.button_settings); 
	
		 buttonMute.setOnClickListener(this);
		 status.setOnClickListener(this);
		 mute_player.setOnClickListener(this);
		 lockscreen.setOnClickListener(this);
		 buttonpower.setOnClickListener(this);
		 button_vol_percent.setOnClickListener(this);
		 button_show_time.setOnClickListener(this);
		 button_vol_up.setOnClickListener(this);
		 buttonTimer.setOnClickListener(this);
		 button_prev_track.setOnClickListener(this);
		 buttonPlay.setOnClickListener(this);
		 button_next_track.setOnClickListener(this);
		 button_full_stop.setOnClickListener(this);
		 button_vol_down.setOnClickListener(this);
		 button_pause.setOnClickListener(this);
		 button_full_screen.setOnClickListener(this);
		 button_playlist.setOnClickListener(this);
		 button_settings.setOnClickListener(this); 
     
      	
      	 ShowServerIp();
		if (Preferenc.getFIRSTSTART(context))
			startActivity(new Intent(this, StartWizard.class));
		else
			{	
			//Ads  ivengo BEGIN --------------------------------------------------
			/*adView = (AdView) findViewById(R.id.adView);
			if (adView != null)
			{
				adView.setAdType(AdType.BANNER_STANDART);
				Request request = new Request();
				LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
				Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				request.setLatitude(location.getLatitude());
				request.setLongitude(location.getLongitude());
				request.setAccurancy(Math.round(location.getAccuracy()));
				request.setAppId(APP_ID);
				adView.loadRequest(request);
			}	
			*/
			
			// Поиск AdView как ресурса и отправка запроса.
			adView = new AdView(this);
		    adView.setAdUnitId(MY_AD_UNIT_ID);
		    adView.setAdSize(AdSize.BANNER);
		    layout = (LinearLayout)findViewById(R.id.mainLayout);
		   // mainLayoutcenter = (LinearLayout)findViewById(R.id.mainLayoutcenter);
		    
		    layout.addView(adView);
		   
		    
		    
		    adView.setAdListener(new AdListener() {
		        /** Called when an ad is clicked and about to return to the application. */
		        @Override
		        public void onAdClosed() {
		         // Log.d(LOG_TAG, "onAdClosed");
		          //Toast.makeText(clientactivity.this, "onAdClosed", Toast.LENGTH_SHORT).show();
		        }
		       
		        /**
		         * Called when an ad is clicked and going to start a new Activity that will
		         * leave the application (e.g. breaking out to the Browser or Maps
		         * application).
		         */
		        @Override
		        public void onAdLeftApplication() {
		         // Log.d(LOG_TAG, "onAdLeftApplication");
		          //Toast.makeText(clientactivity.this, "onAdLeftApplication", Toast.LENGTH_SHORT).show();
		        }

		        /**
		         * Called when an Activity is created in front of the app (e.g. an
		         * interstitial is shown, or an ad is clicked and launches a new Activity).
		         */
		        @Override
		        public void onAdOpened() {
		         // Log.d(LOG_TAG, "onAdOpened");
		          //Toast.makeText(clientactivity.this, "onAdOpened", Toast.LENGTH_SHORT).show();
		        }

		        /** Called when an ad is loaded. */
		        @Override
		        public void onAdLoaded() {
		         // Log.d(LOG_TAG, "onAdLoaded");
		         // Toast.makeText(clientactivity.this, "onAdLoaded", Toast.LENGTH_SHORT).show();
		        }
		      });    
		      // Add the AdView to the view hierarchy.
		    
		    
		    AdRequest adRequest = new AdRequest.Builder()		   // .addTestDevice("AC98C820A50B4AD8A2106EDE96FB87D4")
		    .build();		    		
		    
		    adView.loadAd(adRequest);
		}
		
		// Создание межстраничного объявления.
	    interstitial = new InterstitialAd(this);
	    interstitial.setAdUnitId(MY_AD_INTER_UNIT_ID);

	    // Создание запроса объявления.
	    AdRequest adRequest = new AdRequest.Builder().build();

	    // Запуск загрузки межстраничного объявления.
	    interstitial.loadAd(adRequest);

	}
    
 // Вызовите displayInterstitial(), когда будете готовы показать межстраничное объявление.
    public void displayInterstitial() {
      if (interstitial.isLoaded()) {
        interstitial.show();
      }
    }
    
    @Override
    public void onBackPressed() {
    	startLoadingInterstitial();    	
    }
    
    
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            // your code
//        	Toast.makeText(context, "Back" , Toast.LENGTH_LONG).show();
//            return true;
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null)
        	adView.resume();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        if (adView != null)
        	adView.pause();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adView != null)
        	adView.destroy();
    }
    
    private void startLoadingInterstitial() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(R.string.please_wait);
		progressDialog.setMessage(getString(R.string.advertisment_is_loading));

		/*
		interstitial = new Interstitial(AdType.BANNER_FULLSCREEN);
		
		interstitial.loadRequest(new Request());
		interstitial.showFromActivity(clientactivity.this);
		
		interstitial.setInterstitialListener(new DefaultInterstitialListener() {

			@Override
			public void onInterstitialReceiveAd(Interstitial interstitial) {
				super.onInterstitialReceiveAd(interstitial);
				//statusTextView.setText(null);
				progressDialog.dismiss();
				interstitial.showFromActivity(clientactivity.this);
			}

			@Override
			public void onInterstitialFailWithError(Interstitial interstitial, Error error) {
				super.onInterstitialFailWithError(interstitial, error);
				progressDialog.dismiss();
				finish();
			}

			@Override
			public void onInterstitialDidFinishAd(Interstitial interstitial) {
				super.onInterstitialDidFinishAd(interstitial);
				progressDialog.dismiss();
				finish();
			}

			@Override
			public void onInterstitialSkipAd(Interstitial interstitial) {
				super.onInterstitialSkipAd(interstitial);
				progressDialog.dismiss();
				finish();
			}

		});
		//statusTextView.setText(R.string.advertisment_is_loading);
		interstitial.loadRequest(new Request());
		*/
		progressDialog.show();
		displayInterstitial();
		finish();
	}

    //Ads  ivengo END
	
	 private void Vibrate()
		{
			 if (Preferenc.getVibro(context)) {
	             mVibrator.vibrate(
	                 new long[]{0l, VIBRATE_MILLIS},
	                     -1);
	         }
		}
	 
	 private boolean CheckWiFi()
	    {
	    	//Toast.makeText(context, "DEBUG - WIFI!!!" , Toast.LENGTH_LONG).show();
	    	//return true;//TODO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Убрать комментарии.
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

    private boolean CheckIpServerSet()
    {
    	return (!Preferenc.getFtpServer(context).equals(defaultIP));
    }	
	
    private void ShowInfoAbout()
    {
    	startActivity(new Intent(this, About.class));
    	// ChangeServerIp(true);
    	try {
   		 ShowServerIp();
  		} catch (Exception e) {
  			Toast.makeText(this, "ShowInfoAbout " + e.getMessage(), Toast.LENGTH_LONG).show();
  		}
    	
    }
    
    private void OpenPowerDialog()
    {
    	 new AlertDialog.Builder(this)
         .setTitle(R.string.power_select_title)
         .setItems(R.array.menuPower,
          new DialogInterface.OnClickListener() 
         {
             public void onClick(DialogInterface dialoginterface, int j) 
             {
            	 startMenuPower(j);                    
             }
          }			)
          .show();
    	/*
    	Toast.makeText(context,  getResources().getString(R.string.power_select_title), Toast.LENGTH_LONG).show();
    	RelativeLayout relativeLayout=new RelativeLayout(this);
 		RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
 		relativeLayout.setLayoutParams(layoutParams);
 		
 		RelativeLayout.LayoutParams listLayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
 		
 		ListView view = new ListView(this);
 		view.setLayoutParams(listLayoutParams);  
 	    String[] power = getResources().getStringArray(R.array.menuPower); 
 	    ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, power);  
 	   // View header = getLayoutInflater().inflate(R.layout.header, null);
 	   // view.addHeaderView(header);
 	    view.setAdapter( listAdapter );
    	 
    	 //initialize ad view
 	   // AdView adView=new AdView(this, AdView.BANNER_TYPE_IN_APP_AD, AdView.PLACEMENT_TYPE_INTERSTITIAL,  false,false, AdView.ANIMATION_TYPE_FADE );
// 	    adView.setId(99);
// 		adView.setAdListener(this);
 		RelativeLayout.LayoutParams adLayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
 		
 		adLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
 		adLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
 			
// 		adView.setLayoutParams(adLayoutParams);
//
// 		listLayoutParams.addRule(RelativeLayout.ABOVE, adView.getId());
 		
 		relativeLayout.addView(view);
// 		if (Preferenc.getAds(context))
// 			relativeLayout.addView(adView);
 		setContentView(relativeLayout);
 		
 		view.setOnItemClickListener(new OnItemClickListener() {
 		      public void onItemClick(AdapterView<?> parent, View view,
 		          int position, long id) {
 		    	 startMenuPower(position);
 		      }
 		  });   
 		  */	 		
    }
    
    private void startMenuPower(int t) 
    {   
//    	
//			if (!Preferenc.getReady(context))
//					return;
    	
    	 switch (t)
         {
	         case POWER_ON:
	         {	     
	        	 powerON();
	        	break;
	         }
	         case POWER_OFF:
	         {
	        	 operate(poweroff);
	        	break;
	         }
	         case POWER_REBOOT:
	         {
	        	 operate(reboot);
	        	break;
	         }
	         case POWER_SHUTDOWN:
	         {
	        	 operate(shutdown);
	        	break;
	         }
	         
	          case POWER_hibernate:
	         {
	        	 operate(hibernate);
	        	break;
	         }
	          case POWER_Standby:
	         {
	        	 operate(standby);
	        	break;
	         } 
         }	    	
    }
    
    private void operate(int code)
    {
    	operate(code, "empty");
    }
    
    int countpres = 0;
   
    private void operate(int code, String key)
    {
    	if (Preferenc.getServerTypeDefault(context).equals("1"))
    	{	    		 
    		UDPsend.sendUdpCommand(code, key, context, getResources().getString(R.string.versionprogram));	    		
    	}   
    	else
    		sendFtpCommand(code);
    	try{
	    	if (countpres % 5 == 0)
	    	{	   		
	    		
	    		layout.addView(adView);	    		
	    	}
	    	else 
	    		layout.removeAllViews();
    	}
    	catch(Exception e)
    	{
    		e.getMessage();
    	}
    	countpres++;
    }
    
    public  int sendFtpCommand(int code)
	{
		int result = 0;
		//FtpLibrary ftp = new FtpLibrary();
		try {
			
			if (FailConnections == 1)
	    	{
	    		FtpLibrary.ReActive();
	    	}
//			
//			if (!FtpLibrary.isActive()) 
//			{
				if (!FtpLibrary.connect(context))
				{	
					FtpLibrary.ReActive();
					if (!FtpLibrary.connect(context))
					{
						Preferenc.setReady(context, false);
						startActivity(new Intent(this, ADiscovery.class));				
						return -3;
					}
				}		
			//}
							
			//Toast.makeText(context, "*2*", Toast.LENGTH_LONG).show();
			result = FtpLibrary.sendCommand(code);
			//Toast.makeText(context, "*3*", Toast.LENGTH_LONG).show();
			FtpLibrary.disconnect();
			Preferenc.setReady(context, true);
			FailConnections = 0;
		//	Toast.makeText(context, "*4*", Toast.LENGTH_LONG).show();
		} catch (SocketException e) {
			Toast.makeText(context, "C1 SocketException = " + e.getMessage(), Toast.LENGTH_LONG).show();
			FailConnections++;
			if (FailConnections > 1)
				startActivity(new Intent(this, ADiscovery.class));
			////e.printStackTrace();();
		} catch (IOException e) {
			FailConnections++;			
			if (FailConnections > 1)
				startActivity(new Intent(this, ADiscovery.class));
			Toast.makeText(context, "C2 IOException = " + e.getMessage(), Toast.LENGTH_LONG).show();
			////e.printStackTrace();();
		}
		catch (Exception e) {
			FailConnections++;
			if (FailConnections > 1)
				startActivity(new Intent(this, ADiscovery.class));			
			Toast.makeText(context, "C3 Exception = " + e.getMessage(), Toast.LENGTH_LONG).show();
			////e.printStackTrace();();
		}
		//Toast.makeText(context, "result = " + result, Toast.LENGTH_SHORT).show();
		//Toast.makeText(context, "*5*", Toast.LENGTH_LONG).show();		
		return result;		
	}
    
    private void powerON()
    {
    	if (Preferenc.getMac(context).equals("XX-XX-XX-XX-XX-XX"))
			{
				Toast.makeText(context, getResources().getString(R.string.entermac), Toast.LENGTH_LONG).show();
				//return;
//				/* http://stackoverflow.com/questions/3349121/how-do-i-use-inputfilter-to-limit-characters-in-an-edittext-in-android
//				 * http://stackoverflow.com/questions/7319729/setinputtype-on-edittextpreference
//				 * http://stackoverflow.com/questions/10581337/how-to-apply-inputfilter-to-edittextpreferences
			}
			else
			{	
				if (!CheckValidMac())
				{
					Toast.makeText(context, Preferenc.getMac(context) + " - " + getResources().getString(R.string.notvalidmac) , Toast.LENGTH_LONG).show();
					startActivity(new Intent(this, ADiscovery.class));
					//ChangeServerMAC(false);
				}
				else
				{
					WakeOnLan.WakeUp("255.255.255.255", Preferenc.getMac(context));
					Toast.makeText(context, getResources().getString(R.string.wakeup), Toast.LENGTH_LONG).show();	
				}					
			}	
    }
    
    private boolean CheckValidMac() //TODO
	{
		//	String MAC_PATTERN = "^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$";
		macAddressValidator = new MACAddressValidator();
		return macAddressValidator.validate(Preferenc.getMac(context));
	}

	
	public void onClick(View v)  {
		
		
		ShowServerIp();
		
		//автообновление программы после 1 мая !!!!!!!!!!!!!!!
//		 if (julianDay > 150)//TODO NEED UPDATE IT !!!!!!!!!!!!!!!!!!!!!!!!!!!!
//	        {
//			 
//			 	 Toast.makeText(context, "Need UPDATE program" , Toast.LENGTH_LONG).show();
//				 ShowUrl(getResources().getString(R.string.myapp)); 
//				 return;
//	        }
		
		if (v.getId() == R.id.button_settings)//отдельно для кнопки настроек
		{
			startActivity(new Intent(this, Preferenc.class));
			ShowServerIp();
			return;
		}			
			
		Vibrate();
		
		//---------------------------------------------------------------------- for TEST!!!!!!!!!!!!!!!!!!!!!!!!!!!!=====================================================

		if(!CheckWiFi())
		{
		 Toast.makeText(context, getResources().getString(R.string.needwifi) , Toast.LENGTH_LONG).show();
			
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);		 
		 builder.setMessage(getResources().getString(R.string.needwifi))
		        .setCancelable(false)
		        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) 
		            {
		            	//YESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
		            	//context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
		            	startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
		            }
		        })
		        .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) 
		            {
		                 dialog.cancel();
		                 return;
		            }
		        });
		 AlertDialog alert = builder.create();
		 alert.show();
		 return;
		}
		
		if (v.getId() == R.id.status)
		{
			startActivity(new Intent(context, ADiscovery.class));
			
			return;
		}
		//-----------------------------------------------------------------------------
		
		
		
		
	/*	if (!CheckIpServerSet())
		{
			ShowInfoAbout();		
		
			//if (!CheckValidIP())
			//Toast.makeText(context, getResources().getString(R.string.ipnotcorrect) + Preferenc.getFtpServer(context), Toast.LENGTH_LONG).show();
			Preferenc.setNewIpAdress(context, defaultIP);
			//ChangeServerIp(true);			
			return;			  
		}*/
	
		
		
		//-----------------wakeup!!!!!!!!!!!!!!!!!!!!!!!!!
		//отдельно для кнопки Power - при включении
		if (v.getId() == R.id.buttonpower)
		{					
			operate(aKill);
			OpenPowerDialog();
			return;
		}
		
		
		//--------------------------------------------------------------------for TEST ------end-------------------------------------------------------------------------
			
		int id = v.getId();
		if (id == R.id.buttonMute)
		{
			Toast.makeText(context, "mute", Toast.LENGTH_LONG).show();
			operate(mute);
		}
		else
			if (id == R.id.button_playlist)
			{
				operate(audiosearchStart);
				startActivity(new Intent(this, MusicForm.class));
				Toast.makeText(context, "playlist music", Toast.LENGTH_LONG).show();
			}
			else
				if (id == R.id.button_next_track)
				{
					operate(aNEXT);
				}
				else
					if (id == R.id.button_prev_track)
					{
						operate(aPREV);
					}
					else
						if (id == R.id.button_full_screen)
						{
							operate(aRESTORE);
						}
						else
							if (id == R.id.mute_player)
						{
								operate(aMUTE);
						}
							else
								if (id == R.id.button_vol_down)
							{
									operate(aVOLDWN);
							}
								else
									if (id == R.id.button_vol_up)
								{
									operate(aVOLUP);	
								}
									else
										if (id == R.id.buttonPlay)
									{
										operate(aPLAY);
									}
										else
											if (id == R.id.button_show_time)
										{
												operate(nowtime);
										}
											else
												if (id == R.id.buttonTimer)
											{
												startActivity(new Intent(this, TimerForm.class));
											}
												else
													if (id == R.id.button_vol_percent)
												{
													startActivity(new Intent(this, VolumeForm.class));	
												}
													else
														if (id == R.id.button_full_stop)
													{
														operate(aSTOP);
													}
														else
															if (id == R.id.button_pause)
														{
															operate(aPAUSE);
														}
															else
																if (id == R.id.lockscreen)
															{
																operate(lockscr);
															}
	}
	
	private String GetWifiSSID()
    {	
    	try{
        	
        	WifiManager wim= (WifiManager) getSystemService(WIFI_SERVICE);
    		List<WifiConfiguration> l =  wim.getConfiguredNetworks(); 
    		WifiConfiguration wc = l.get(0); 
    		//tv.setText("\n"+ (wim.getConnectionInfo().getIpAddress()));
    		String result = wim.getConnectionInfo().getSSID();
    		if (result == null || result.equals("null"))
    			result = getResources().getString(R.string.noconnect);
    			
    		if (result.length() > 12)//12
    			result = result.substring(0, 11);
    	 		return result;
        	}
        catch(Exception ex)
        {
        	return "WIFI is OFF";    	
        }    		
    }
	
	private void ShowServerIp()
    {	    	
    	String ip = Preferenc.getFtpServer(context); //TODO

		   if (Preferenc.getServerTypeDefault(context).equals("1")) //4 allIP and UDP
		   {
			   ip = getResources().getString(R.string.allpc);
		   }
		   String type  = "";
		   if (Preferenc.getServerTypeDefault(context).equals("1"))
			   type = "windows 7";
		   else
			   type = "windows 8";
		   status.setText(ip + "\n" +
				   GetWifiSSID() + "\n" +  type + "\n" + Preferenc.getFtpPass(context)  );        
    }
	
	
	
	//Menu - click Button Menu--------------------------MENU(Button)
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	super.onCreateOptionsMenu(menu);;
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
    	return true;
    } 
    //Event when Click Menu Item
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {    	
    	int itemId = item.getItemId();
    	if (itemId == R.id.itemAbout)		
    	{
    		startActivity(new Intent(this, About.class));
    		return true; 
    	}
    	else
    		if (itemId == R.id.itemSetting)		
        	{
        		startActivity(new Intent(this, Preferenc.class));	    		
    		return true;
        	}
        	else
        		if (itemId == R.id.itemPlaylist)		
            	{
        			operate(audiosearchStart);
    				startActivity(new Intent(this, MusicForm.class));
    				Toast.makeText(context, "playlist music", Toast.LENGTH_LONG).show();	    		
        		return true;
            	}
            else
        		if (itemId == R.id.itemScan)		
            	{
            		startActivity(new Intent(this, StartWizard.class));
    		return true;
            	}
    	return false;
    } 
}
