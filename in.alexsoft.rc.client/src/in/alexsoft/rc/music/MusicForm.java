package in.alexsoft.rc.music;

import in.alexsoft.rc.music.R;

import java.io.IOException;
import java.net.SocketException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MusicForm extends Activity  implements OnClickListener
{
	
	private Context context = this;
    
    private Vibrator mVibrator;
    private static final int VIBRATE_MILLIS = 50;
	Button buttonx, button_driveleft, button_start, button_driveright, button_fullscan, button_stopscan,
		but_pgUP, but_UP, but_fav, but_left, but_PLAY, but_right, but_pgDN, but_down, but_orderLF, but_orderNM,
		but_orderFD, but_orderFV, but_exit, but_orderART, but_orderAlbum;
	TextView txtStack, txtInput, txtMemory;
	EditText searchTextBox;
	
	private static final int Onesong = 0;
	private static final int AllFolder =1;
	private static final int Album	= 2;
	private static final int Artist = 3;
	private static final int All = 4;
	private static final int Shuffle =5;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music);
        
        searchTextBox = (EditText) findViewById(R.id.editText_Search);
        final TextView myOutputBox = (TextView) findViewById(R.id.txtInput);
        searchTextBox.addTextChangedListener(new TextWatcher()
        {
        	public void afterTextChanged(Editable s) {
        	   }
        	 
        	   public void beforeTextChanged(CharSequence s, int start, 
        	     int count, int after) {
        	   }
        	 
        	   public void onTextChanged(CharSequence s, int start, 
        	     int before, int count) {
        	
        	   
        	   if (s.length() == 0)
        	   {
        		   operate(resetSearch, "empty");
        		   myOutputBox.setText("");
        		   return;
        	   }
        	   final StringBuilder sb = new StringBuilder(s.length());
        	   sb.append(s);
        	   String word = sb.toString();;
        	   if (word.length() > myOutputBox.getText().length())
        	   {
        		   operate(keysend, word.substring(word.length() - 1));   
        	   }
        	   else
        	   {
        		   operate(searchRemove, "empty");
        	   }
        	   myOutputBox.setText(word);
        	   
        	   }
        	  });
	
	buttonx = (Button)findViewById(R.id.buttonx);
	buttonx.setOnClickListener(this);
	
	button_driveleft = (Button)findViewById(R.id.button_driveleft);
	button_driveleft.setOnClickListener(this);
	
	button_start =  (Button)findViewById(R.id.button_start);
	button_start.setOnClickListener(this);
	
	button_driveright = (Button)findViewById(R.id.button_driveright);
	button_driveright.setOnClickListener(this);
	
	button_fullscan = (Button)findViewById(R.id.button_fullscan);
	button_fullscan.setOnClickListener(this);
	
	button_stopscan = (Button)findViewById(R.id.button_stopscan);
	button_stopscan.setOnClickListener(this);
	
	but_pgUP = (Button)findViewById(R.id.but_pgUP);
	but_pgUP.setOnClickListener(this);
	
	but_UP = (Button)findViewById(R.id.but_UP);
	but_UP.setOnClickListener(this);
	
	but_fav = (Button)findViewById(R.id.but_fav);
	but_fav.setOnClickListener(this);
	
	but_left = (Button)findViewById(R.id.but_left);
	but_left.setOnClickListener(this);
	
	but_PLAY = (Button)findViewById(R.id.but_PLAY);
	but_PLAY.setOnClickListener(this);
	
	but_right = (Button)findViewById(R.id.but_right);
	but_right.setOnClickListener(this);
	
	but_pgDN = (Button)findViewById(R.id.but_pgDN);
	but_pgDN.setOnClickListener(this);
	
	but_down = (Button)findViewById(R.id.but_down);
	but_down.setOnClickListener(this);
	
//	but_del = (Button)findViewById(R.id.but_del);
//	but_del.setOnClickListener(this);
	
	but_orderLF = (Button)findViewById(R.id.but_orderLF);
	but_orderLF.setOnClickListener(this);
	
	but_orderNM = (Button)findViewById(R.id.but_orderNM);
	but_orderNM.setOnClickListener(this);
	
	but_orderFD = (Button)findViewById(R.id.but_orderFD);
	but_orderFD.setOnClickListener(this);
	
	but_orderFV = (Button)findViewById(R.id.but_orderFV);
	but_orderFV.setOnClickListener(this);
	
	but_orderART = (Button)findViewById(R.id.but_orderART);
	but_orderART.setOnClickListener(this);
	
	
	but_orderAlbum = (Button)findViewById(R.id.but_orderAlbum);
	but_orderAlbum.setOnClickListener(this);
	
        
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
      
      //  LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayout);
      //  final TextView textView = (TextView)findViewById(R.id.textView);
        but_exit = (Button)findViewById(R.id.but_exit);
        but_exit.setOnClickListener(this);
       
    } 
	
	 int audiosearchStart = 5000;

     int resetSearch = 5001;
     int searchRemove = 5002;
     int scanPrepare = 5003;
     int orderToLeft = 5004;
     int orderToRight = 5005;
     int stopScan = 5006;
     int selectedDown = 5007;
     int selectedUp = 5008;
     int play = 5009;
     int selectTimeOrder = 5010;
     int selectNameOrder = 5011;

     int favoriteadd = 5012;
     int selectFolderOrder = 5013;
     int selectFavoriteOrder = 5014;

     int driverToLeft = 5015;
     int driverToRight = 5016;

     int fullscan = 5017;
     int pgup = 5018;
     int pgdn = 5019;

     int playfolder = 5022;
     int playalbum = 5023;
     int playartist = 5024;
     int playall = 5025;
     int playrandom = 5026;
     int ArtistOrder = 5027;
     int AlbumOrder = 5028;

     int KillaudiosearchStart = 6000;

     int keysend = 2020;

     int ruskey = 2021;
	
public void onClick(View v) {
	
	
		if (v.getId() == R.id.but_exit)
		{	
			operate(KillaudiosearchStart, "empty");
			finish();									
			return;
		}
	
		Vibrate();
		if (v.getId() == R.id.buttonx)
		{	
			searchTextBox.setText("");
			operate(resetSearch, "empty");						
			return;
		}
		if (v.getId() == R.id.button_driveleft)
		{				
			operate(driverToLeft, "empty");						
			return;
		}
		if (v.getId() == R.id.button_start)
		{				
			operate(scanPrepare, "empty");						
			return;
		}
		if (v.getId() == R.id.button_driveright)
		{				
			operate(driverToRight, "empty");						
			return;
		}
		if (v.getId() == R.id.button_fullscan)
		{				
			operate(fullscan, "empty");						
			return;
		}
		if (v.getId() == R.id.button_stopscan)
		{				
			operate(stopScan, "empty");						
			return;
		}
		if (v.getId() == R.id.but_pgUP)
		{				
			operate(pgup, "empty");						
			return;
		}
		if (v.getId() == R.id.but_UP)
		{				
			operate(selectedUp, "empty");						
			return;
		}
		if (v.getId() == R.id.but_fav)			
		{				
			operate(favoriteadd, "empty");						
			return;
		}
		if (v.getId() == R.id.but_left)
		{				
			operate(orderToLeft, "empty");						
			return;
		}
		
		if (v.getId() == R.id.but_PLAY)
		{				
			//operate(play, "empty");	
			Playdialog();
			return;
		}
		if (v.getId() == R.id.but_right)
		{				
			operate(orderToRight, "empty");						
			return;
		}
		if (v.getId() == R.id.but_pgDN)
		{				
			operate(pgdn, "empty");						
			return;
		}
		if (v.getId() == R.id.but_down)
		{				
			operate(selectedDown, "empty");						
			return;
		}
	
		if (v.getId() == R.id.but_orderLF)
		{				
			operate(selectTimeOrder, "empty");						
			return;
		}
		if (v.getId() == R.id.but_orderNM)
		{				
			operate(selectNameOrder, "empty");						
			return;
		}
		if (v.getId() == R.id.but_orderFD)
		{				
			operate(selectFolderOrder, "empty");						
			return;
		}
		if (v.getId() == R.id.but_orderFV)
		{				
			operate(selectFavoriteOrder, "empty");						
			return;
		}
		if (v.getId() == R.id.but_orderART)
		{				
			operate(ArtistOrder, "empty");						
			return;
		}
		if (v.getId() == R.id.but_orderAlbum)
		{				
			operate(AlbumOrder, "empty");						
			return;
		}
		
		
		
		
	
		
		
		
	}



private void Playdialog() {
	// TODO Auto-generated method stub
	new AlertDialog.Builder(this)
    .setTitle(R.string.playaudio_select_title)
    .setItems(R.array.menuPlaymusic,
     new DialogInterface.OnClickListener() 
    {
        public void onClick(DialogInterface dialoginterface, int j) 
        {
       	 startMenuPlay(j);                    
        }		
     }			)
     .show();	
	
}



private void startMenuPlay(int j) {
	// TODO Auto-generated method stub
	switch (j)
    {

        case Onesong:
        {	     
        	operate(play, "empty");
       	break;
        }
        case AllFolder:
        {	     
        	operate(playfolder, "empty");
       	break;
        }
        case Album:
        {	     
        	operate(playalbum, "empty");
       	break;
        }
        case Artist:
        {	     
        	operate(playartist, "empty");
       	break;
        }
        case All:
        {	     
        	operate(playall, "empty");
       	break;
        }
        case Shuffle:
        {	     
        	operate(playrandom, "empty");
       	break;
        }
    }
	finish();
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
	
	    super.onDestroy();
	  }
	 
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	        	operate(KillaudiosearchStart, "empty");
	        	finish();
	            return true;
	        }
	        return super.onKeyDown(keyCode, event);
	    }
	
	 
	 private void operate(int code)
	    {
	    	operate(code, "empty");
	    }
	    
	    private void operate(int code, String key)
	    {
	    	if (Preferenc.getServerTypeDefault(context).equals("1"))
	    	{
	    		String result = UDPsend.sendUdpCommand(code, key, context, getResources().getString(R.string.versionprogram));
	    	}	
	    	else
	    	{	
	    		if (code == keysend)
	    		{
	    			if (key.equals("é"))
	    				sendFtpCommand(ruskey, "1");
	    			else
	    				if (key.equals("ö"))
		    				sendFtpCommand(ruskey, "2");
		    			else
		    				if (key.equals("ó"))
			    				sendFtpCommand(ruskey, "3");
			    			else
			    				if (key.equals("ê"))
				    				sendFtpCommand(ruskey, "4");
				    			else
				    				if (key.equals("å"))
					    				sendFtpCommand(ruskey, "5");
					    			else
					    				if (key.equals("í"))
						    				sendFtpCommand(ruskey, "6");
						    			else
						    				if (key.equals("ã"))
							    				sendFtpCommand(ruskey, "7");
							    			else
							    				if (key.equals("ø"))
								    				sendFtpCommand(ruskey, "8");
								    			else
								    				if (key.equals("ù"))
									    				sendFtpCommand(ruskey, "9");
									    			else
									    				if (key.equals("ç"))
										    				sendFtpCommand(ruskey, "10");
										    			else
										    				if (key.equals("õ"))
											    				sendFtpCommand(ruskey, "11");
											    			else
											    				if (key.equals("ú"))
												    				sendFtpCommand(ruskey, "12");
												    			else
												    				if (key.equals("ô"))
													    				sendFtpCommand(ruskey, "13");
													    			else
													    				if (key.equals("û"))
														    				sendFtpCommand(ruskey, "14");
														    			else
														    				if (key.equals("â"))
															    				sendFtpCommand(ruskey, "15");
															    			else
															    				if (key.equals("à"))
																    				sendFtpCommand(ruskey, "16");
																    			else
																    				if (key.equals("ï"))
																	    				sendFtpCommand(ruskey, "17");
																	    			else
																	    				if (key.equals("ð"))
																		    				sendFtpCommand(ruskey, "18");
																		    			else
																		    				if (key.equals("î"))
																			    				sendFtpCommand(ruskey, "19");
																			    			else
																			    				if (key.equals("ë"))
																				    				sendFtpCommand(ruskey, "20");
																				    			else
																				    				if (key.equals("ä"))
																					    				sendFtpCommand(ruskey, "21");
																					    			else
																					    				if (key.equals("æ"))
																						    				sendFtpCommand(ruskey, "22");
																						    			else
																						    				if (key.equals("ý"))
																							    				sendFtpCommand(ruskey, "23");
																							    			else
																							    				if (key.equals("ÿ"))
																								    				sendFtpCommand(ruskey, "24");
																								    			else
																								    				if (key.equals("÷"))
																									    				sendFtpCommand(ruskey, "25");
																									    			else
																									    				if (key.equals("ñ"))
																										    				sendFtpCommand(ruskey, "26");
																										    			else
																										    				if (key.equals("ì"))
																											    				sendFtpCommand(ruskey, "27");
																											    			else
																											    				if (key.equals("è"))
																												    				sendFtpCommand(ruskey, "28");
																												    			else																												    				
																												    				if (key.equals("ò"))
																													    				sendFtpCommand(ruskey, "29");
																													    			else
																													    				if (key.equals("ü"))
																														    				sendFtpCommand(ruskey, "30");
																														    			else
																														    				if (key.equals("á"))
																															    				sendFtpCommand(ruskey, "31");
																															    			else
																															    				if (key.equals("þ"))
																																    				sendFtpCommand(ruskey, "32");
																																    			else
																																    				if (key.equals("¸"))
																																	    				sendFtpCommand(ruskey, "33");
	    		else
	    			sendFtpCommand(code, key);
	    			return;
	    			}
	    		sendFtpCommand(code, key);
	    		}
		
	    }
	    
	    public  int sendFtpCommand(int code, String key)
		{
			int result = 0;
			//FtpLibrary ftp = new FtpLibrary();
			try {			
				
		    		FtpLibrary.ReActive();
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
			
				result = FtpLibrary.sendPlaylistCommand(code, key, context);			
				FtpLibrary.disconnect();
				Preferenc.setReady(context, true);		
	
			} catch (SocketException e) {
				Toast.makeText(context, "1 SocketException = " + e.getMessage(), Toast.LENGTH_LONG).show();
				
					startActivity(new Intent(this, ADiscovery.class));				
			} catch (IOException e) {
			
					startActivity(new Intent(this, ADiscovery.class));
				Toast.makeText(context, "2 IOException = " + e.getMessage(), Toast.LENGTH_LONG).show();				
			}
			catch (Exception e) {
					
				Toast.makeText(context, "3 Exception = " + e.getMessage(), Toast.LENGTH_LONG).show();				
			}
			//		
			return result;		
		}


}