package in.alexsoft.rc.music;

import java.io.IOException;
import java.net.SocketException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class VolumeForm extends Activity  implements OnClickListener{
	
	private Context context = this;
    
    private Vibrator mVibrator;
    private static final int VIBRATE_MILLIS = 50;
	private int FailConnections = 0;
    
	Button button_volup, button_voldown, button_vol7, button_vol18,	button_vol25, button_vol33, button_vol55,
	button_vol77, button_vol99;
	
	 int volup = 2;
     int voldown = 3;
     int vol18 = 54;
     int vol25 = 55;
     int vol33 = 56;
     int vol55 = 57;
     int vol77 = 58;
     int vol99 = 59;
     int vol7 = 60;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.volume);
       
    	button_volup = (Button)findViewById(R.id.button_volup);
        button_volup.setOnClickListener(this);
	
        button_voldown = (Button)findViewById(R.id.button_voldown);
        button_voldown.setOnClickListener(this);
	
        button_vol7 = (Button)findViewById(R.id.button_vol7);
        button_vol7.setOnClickListener(this);
	
        button_vol18 = (Button)findViewById(R.id.button_vol18);
        button_vol18.setOnClickListener(this);
	
        button_vol25 = (Button)findViewById(R.id.button_vol25);
        button_vol25.setOnClickListener(this);
	
        button_vol33 = (Button)findViewById(R.id.button_vol33);
        button_vol33.setOnClickListener(this);
	
        button_vol55 = (Button)findViewById(R.id.button_vol55);
        button_vol55.setOnClickListener(this);
	
        button_vol77 = (Button)findViewById(R.id.button_vol77);
        button_vol77.setOnClickListener(this);
	
        button_vol99 = (Button)findViewById(R.id.button_vol99);
        button_vol99.setOnClickListener(this);
        
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    } 
	
	
	
public void onClick(View v) {
	
		Vibrate();
			
			if (v.getId() == R.id.button_volup)
			{
				operate(volup);				
				return;
			}
			if (v.getId() == R.id.button_voldown)
			{
				operate(voldown);				
				return;
			}		


			if (v.getId() == R.id.button_vol7)
			{
				operate(vol7);
				finish();
				return;
			}
			if (v.getId() == R.id.button_vol18)
			{
				operate(vol18);
				finish();
				return;
			}
			if (v.getId() == R.id.button_vol25)
			{
				operate(vol25);
				finish();
				return;
			}
			if (v.getId() == R.id.button_vol33)
			{
				operate(vol33);
				finish();
				return;
			}
			if (v.getId() == R.id.button_vol55)
			{
				operate(vol55);
				finish();
				return;
			}
			if (v.getId() == R.id.button_vol77)
			{
				operate(vol77);
				finish();
				return;
			}
			if (v.getId() == R.id.button_vol99)
			{
				operate(vol99);
				finish();
				return;
			}			
}


@Override
public void onPause()
{       
	//operate(KillvideosearchStart, "empty");	
    super.onPause();
   
}
	
	 private void Vibrate()
		{
			  {
				  if (Preferenc.getVibro(context))
	             mVibrator.vibrate(
	                 new long[]{0l, VIBRATE_MILLIS},
	                     -1);
	         }
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
	 
	 private void operate(int code)
	    {
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