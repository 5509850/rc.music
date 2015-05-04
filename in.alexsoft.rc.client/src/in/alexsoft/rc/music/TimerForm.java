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
import android.widget.RadioButton;
import android.widget.Toast;

public class TimerForm extends Activity  implements OnClickListener{
	
	private Context context = this;
    
    private Vibrator mVibrator;
    private static final int VIBRATE_MILLIS = 50;
	private int FailConnections = 0;
    
	Button button_timeroff, b1,b3, b5,	b10, b20, b30, b45, b60, b90, b120, b150, b180, button_back;		
	RadioButton radioPOFF, radioHIBERNATE;
	
	int sleep1 = 53;
	int sleep3 = 1110;
	int sleep5 = 101;//--------------------------
	int sleep10 = 52;
	int sleep20 = 51;
    int sleep30 = 44;//--------for pult code
    int sleep45 = 1111;
    int sleep60 = 45;
    int sleep90 = 46;
    int sleep120 = 47;
    int sleep150 = 48;
    int sleep180 = 49;
   
    int sleepOff = 50;
    
    //NEW!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    int hsleep1 = 9000;
    int hsleep3 = 9001;
    int hsleep5 = 9002;//--------------------------
    int hsleep10 = 9003;
    int hsleep20 = 9004;
    int hsleep30 = 9005;//--------for pult code
    int hsleep45 = 9006;
    int hsleep60 = 9007;
    int hsleep90 = 9008;
    int hsleep120 = 9009;
    int hsleep150 = 9010;
    int hsleep180 = 9011;
   
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);
        
        radioPOFF = (RadioButton) findViewById(R.id.radioPOFF);
        radioHIBERNATE = (RadioButton) findViewById(R.id.radioHIBERNATE);
        
        
       if (Preferenc.getTIMERSHUTDOWN(context))
    	   radioPOFF.setChecked(true);
       else
    	   radioHIBERNATE.setChecked(true);
    	   
       
        button_timeroff = (Button)findViewById(R.id.button_timeroff);
        button_timeroff.setOnClickListener(this);
	
	b1 = (Button)findViewById(R.id.b1);
	b1.setOnClickListener(this);
	
	b3 = (Button)findViewById(R.id.b3);
	b3.setOnClickListener(this);
	
	b5 = (Button)findViewById(R.id.b5);
	b5.setOnClickListener(this);
	
	b10 = (Button)findViewById(R.id.b10);
	b10.setOnClickListener(this);
	
	b20 = (Button)findViewById(R.id.b20);
	b20.setOnClickListener(this);
	
	b30 = (Button)findViewById(R.id.b30);
	b30.setOnClickListener(this);
	
	b45 = (Button)findViewById(R.id.b45);
	b45.setOnClickListener(this);
	
	b60 = (Button)findViewById(R.id.b60);
	b60.setOnClickListener(this);
	
	b90 = (Button)findViewById(R.id.b90);
	b90.setOnClickListener(this);
	
	b120 = (Button)findViewById(R.id.b120);
	b120.setOnClickListener(this);
	
	b150 = (Button)findViewById(R.id.b150);
	b150.setOnClickListener(this);
	
	b180 = (Button)findViewById(R.id.b180);
	b180.setOnClickListener(this);
	
	button_back = (Button)findViewById(R.id.button_back);
	button_back.setOnClickListener(this);
	
        
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    } 
	
	
	
public void onClick(View v) {
	
	
		if (v.getId() == R.id.button_back)
		{				
			finish();									
			return;
		}
	
		Vibrate();
		
		if (v.getId() == R.id.button_timeroff)
		{
			operate(sleepOff);
			finish();
			return;
		}
		
		if (radioPOFF.isChecked())
		{
			Preferenc.setTIMERSHUTDOWN(context, true);
			
			if (v.getId() == R.id.b1)
			{
				operate(sleep1);
				finish();
				return;
			}
			if (v.getId() == R.id.b3)
			{
				operate(sleep3);
				finish();
				return;
			}
			if (v.getId() == R.id.b5)
			{
				operate(sleep5);
				finish();
				return;
			}
			if (v.getId() == R.id.b10)
			{
				operate(sleep10);
				finish();
				return;
			}
			if (v.getId() == R.id.b20)
			{
				operate(sleep20);
				finish();
				return;
			}
			if (v.getId() == R.id.b30)
			{
				operate(sleep30);
				finish();
				return;
			}
			if (v.getId() == R.id.b45)
			{
				operate(sleep45);
				finish();
				return;
			}
			if (v.getId() == R.id.b60)
			{
				operate(sleep60);
				finish();
				return;
			}
			if (v.getId() == R.id.b90)
			{
				operate(sleep90);
				finish();
				return;
			}
			if (v.getId() == R.id.b120)
			{
				operate(sleep120);
				finish();
				return;
			}
			if (v.getId() == R.id.b150)
			{
				operate(sleep150);
				finish();
				return;
			}
			if (v.getId() == R.id.b180)
			{
				operate(sleep180);
				finish();
				return;
			}
		}
		else
		{
			Preferenc.setTIMERSHUTDOWN(context, false);
			if (v.getId() == R.id.b1)
			{
				operate(hsleep1);
				finish();
				return;
			}
			if (v.getId() == R.id.b3)
			{
				operate(hsleep3);
				finish();
				return;
			}
			if (v.getId() == R.id.b5)
			{
				operate(hsleep5);
				finish();
				return;
			}
			if (v.getId() == R.id.b10)
			{
				operate(hsleep10);
				finish();
				return;
			}
			if (v.getId() == R.id.b20)
			{
				operate(hsleep20);
				finish();
				return;
			}
			if (v.getId() == R.id.b30)
			{
				operate(hsleep30);
				finish();
				return;
			}
			if (v.getId() == R.id.b45)
			{
				operate(hsleep45);
				finish();
				return;
			}
			if (v.getId() == R.id.b60)
			{
				operate(hsleep60);
				finish();
				return;
			}
			if (v.getId() == R.id.b90)
			{
				operate(hsleep90);
				finish();
				return;
			}
			if (v.getId() == R.id.b120)
			{
				operate(hsleep120);
				finish();
				return;
			}
			if (v.getId() == R.id.b150)
			{
				operate(hsleep150);
				finish();
				return;
			}
			if (v.getId() == R.id.b180)
			{
				operate(hsleep180);
				finish();
				return;
			}
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