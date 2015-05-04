package in.alexsoft.rc.music;

import java.io.IOException;
import java.net.SocketException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class StartWizard extends Activity  implements OnClickListener{
	
	private Context context = this;    
	private static final int no_network = 0; 
	private static final int wifi = 1;
	private static final int GGG = 2;
	 int nowtime = 42;//вывод на экран круглого времени
    
	private int FailConnections = 0;
    
	Button prev, next, startscan, textViewStep;
	RadioButton win7, win8;
	ImageView img;
	TextView about_content;
	
	int Step = 1;
	
	private  final String windows7 = "1"; 
	private  final String windows8 = "2";

	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard);
       
        prev = (Button)findViewById(R.id.prev);
        prev.setOnClickListener(this);
	
        next = (Button)findViewById(R.id.next);
        next.setOnClickListener(this);
	
        textViewStep = (Button)findViewById(R.id.textViewStep);
        textViewStep.setOnClickListener(this);
	
        win7 = (RadioButton)findViewById(R.id.win7);
        win7.setOnClickListener(this);
        
        win8 = (RadioButton)findViewById(R.id.win8);
        win8.setOnClickListener(this);
        
        img =  (ImageView)findViewById(R.id.imageViewWizard);
        img.setOnClickListener(this);
        
        about_content  =  (TextView)findViewById(R.id.about_content);
        about_content.setOnClickListener(this);
        
        setButtons();
        
        
         
    } 
	
	private void setButtons()
	{
		switch(Step)
		{
			case 1:
			{
				win7.setVisibility(View.INVISIBLE);
				win8.setVisibility(View.INVISIBLE);
				about_content.setText(getResources().getString(R.string.aboutwizard1));
				img.setImageDrawable(getResources().getDrawable(R.drawable.firewall));
				textViewStep.setText(getResources().getString(R.string.wizardsetstep) + " 1");
				break;	
			}
			case 2:
			{
				win7.setVisibility(View.VISIBLE);
				win8.setVisibility(View.VISIBLE);
				about_content.setText(getResources().getString(R.string.aboutwizard2));
				img.setImageDrawable(getResources().getDrawable(R.drawable.wizard2));
				textViewStep.setText(getResources().getString(R.string.wizardsetstep) + " 2");
				if (Preferenc.getServerTypeDefault(context).equals("1"))
		    	{	    		 
					win7.setChecked(true);	    		
		    	}   
		    	else
		    		win8.setChecked(true);
				break;
					
			}
			case 3:
			{
				win7.setVisibility(View.INVISIBLE);
				win8.setVisibility(View.INVISIBLE);
				textViewStep.setText(getResources().getString(R.string.wizardsetstep) + " 3");
				if (win7.isChecked())
					{					
					about_content.setText(getResources().getString(R.string.aboutwizard3));
					img.setImageDrawable(getResources().getDrawable(R.drawable.showtime07));
					}
				else
				{
					about_content.setText(getResources().getString(R.string.aboutwizard4));
					img.setImageDrawable(getResources().getDrawable(R.drawable.wizard3));
				}
				
				break;	
			}
			
			case 4:
			{
				win7.setVisibility(View.INVISIBLE);
				win8.setVisibility(View.INVISIBLE);
				textViewStep.setText(getResources().getString(R.string.wizardsetstep) + " 4");
				if (win7.isChecked())
					{					
					about_content.setText(getResources().getString(R.string.aboutwizard5));
					img.setImageDrawable(getResources().getDrawable(R.drawable.wizard3));
					}
				else
				{
					about_content.setText(getResources().getString(R.string.aboutwizard6));
					img.setImageDrawable(getResources().getDrawable(R.drawable.wizard4));
				}
				
				break;	
			}
			default:
			{
				finish();
			}
		
		}
	}
	
	
	
public void onClick(View v) {
	
	if (Step != 1)
		Preferenc.setFIRSTSTART(context, false);
		
			
			if (v.getId() == R.id.prev)
			{
				if (Step == 1)
					finish();
				else
				{
					Step --;
					setButtons();	
				}
					
				return;
			}
			if (v.getId() == R.id.next)
			{				
				Step ++;
				setButtons();
				return;
			}		


			if (v.getId() == R.id.textViewStep)
			{	
				return;
			}
			
			if (v.getId() == R.id.imageViewWizard)
			{	
				if (win7.isChecked() && Step == 3)
				{
					operate(nowtime);
					return;
				}
			
			 if ((win7.isChecked() && Step == 4) || (!win7.isChecked() && Step == 3))
				{
					//!!!!!
					startActivity(new Intent(context, ADiscovery.class));
					return;
				}		
			 
			    Step ++;
				setButtons();												
				return;
			}
			if (v.getId() == R.id.win7)
			{	
				if (win7.isChecked())
					Preferenc.setServerTypeDefault(context, windows7);
				else
					Preferenc.setServerTypeDefault(context, windows8);
				return;
			}
			if (v.getId() == R.id.win8)
			{	
				if (win7.isChecked())
					Preferenc.setServerTypeDefault(context, windows7);
				else
					Preferenc.setServerTypeDefault(context, windows8);								
				return;
			}
}


@Override
public void onPause()
{       
	//operate(KillvideosearchStart, "empty");	
    super.onPause();
   
}
	
	
	 @Override
	 protected void onResume() 
	    {
		 super.onResume();
	    }	    
	   
	    
	@Override
	protected void onRestart()
	    {
	    	 
	    	super.onRestart();
	    }
	
	@Override
	  public void onDestroy() {
	/*	if (adView != null)
		{
			adView.destroy();
		}
		*/		
	    super.onDestroy();
	  }
	
	 
	 private boolean CheckWiFi()
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
	 
	 private void operate(int code)
	    {
		 
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
		 
	    	operate(code, "empty");
	    }
	 
	 private void operate(int code, String key)
	    {
	    	if (Preferenc.getServerTypeDefault(context).equals("1"))
	    	{	    		 
	    		UDPsend.sendUdpCommand(code, key, context, getResources().getString(R.string.versionprogram));	    		
	    	}   
	    	else
	    		sendFtpCommand(code);    			
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
//					if (!FtpLibrary.isActive()) 
//					{
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
		    
		
}