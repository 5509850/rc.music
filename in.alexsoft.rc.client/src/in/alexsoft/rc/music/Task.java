package in.alexsoft.rc.music;


import in.alexsoft.rc.music.R;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.Toast;

public class Task extends AsyncTask<Void, String, Boolean> {
    
    protected final Resources mResources;
    protected final Context context;
    
    private Boolean mResult;
    private String mProgressMessage;
    private int myresult;
    private IProgressTracker mProgressTracker;  
    private int typeTask = 0;
    int TYPE_CHECKCONNECTION = 0;
    int TYPE_SCREENSHOT = 1;
    
    String screenshotfilename = "screen.jpg"; 
    String screenshotfilenameUTF = "115_99_114_101_101_110_46_106_112_103" ;
	int screenshotbig = 1200;
	int delayScreenshot = 5000;//0.5сек задержка скриншота
	String folderout = "Download";
    
    
    int nowtime = 42;//вывод на экран круглого времени

    /* UI Thread */
    public Task(Resources resources, Context mcontext, int type) 
    {
	// Keep reference to resources
    	typeTask = type;
	mResources = resources;
	context = mcontext;
	// Initialise initial pre-execute message
	mProgressMessage = resources.getString(R.string.task_starting);	
    }

    /* UI Thread */
    public void setProgressTracker(IProgressTracker progressTracker) {
	// Attach to progress tracker
	mProgressTracker = progressTracker;
	// Initialise progress tracker with current task state
	if (mProgressTracker != null) {
	    mProgressTracker.onProgress(mProgressMessage);
	    if (mResult != null) {
		mProgressTracker.onComplete();
	    }
		}
    }

    /* UI Thread */
    @Override
    protected void onCancelled() {
	// Detach from progress tracker
	mProgressTracker = null;
    }
    
    /* UI Thread */
    @Override
    protected void onProgressUpdate(String... values) {
	// Update progress message 
	mProgressMessage = values[0];
	// And send it to progress tracker
	if (mProgressTracker != null) {
	    mProgressTracker.onProgress(mProgressMessage);
	}
    }

    /* UI Thread */
    @Override
    protected void onPostExecute(Boolean result) {
	// Update result
	mResult = result;
	// And send it to progress tracker
	if (mProgressTracker != null) {
	    mProgressTracker.onComplete();
	}
	// Detach from progress tracker
	mProgressTracker = null;
    }

    /* Separate Thread */
    @Override
    protected Boolean doInBackground(Void... arg0) 
    {
	// Working in separate thread    	
	
    	if (typeTask  == TYPE_CHECKCONNECTION)
    	{
    		 for (int i = 1; i > 0; --i) //(int i = 10; i > 0; --i)
    			{
    			    // Check if task is cancelled
    			    if (isCancelled()) {
    				// This return causes onPostExecute call on UI thread
    				return false;
    			    }

    			    try {
    				// This call causes onProgressUpdate call on UI thread
    			    
    			    	publishProgress(mResources.getString(R.string.task_working));
    			    	myresult = sendFtpCommand();
    			    	String mess = "";
    			    	if (myresult == -7)
    			    	{
    			    		Preferenc.setStatus(context, "-7");	    		
    			    		mess = "не верный IP адрес (не включен сервер, в фаерволе закрыт доступ)";
    			    	}
    			    			    		
    					if (myresult == -1)
    					{
    						Preferenc.setStatus(context, "-1");
    						mess = "не верный код";
    					}
    						
    					if (myresult == -6)
    					{
    						Preferenc.setStatus(context, "-6");
    						mess = "не известная ошибка";
    					}
    						
    					if (myresult == 0)
    					{
    						Preferenc.setStatus(context, "0");
    						Preferenc.setReady(context, true);
    						mess = "ОК";
    					}
    						
    					//defaut 1
    					Thread.sleep(500);
    					publishProgress(mess);
    				
    				Thread.sleep(1000);
    			    } catch (InterruptedException e) {
    				e.printStackTrace();
    				// This return causes onPostExecute call on UI thread
    				return false;
    			    }
    			}
    			// This return causes onPostExecute call on UI thread
    	}
    	if (typeTask  == TYPE_SCREENSHOT)
    	{
    		if (isCancelled()) { 
 				return false;
 			    }
    		try {
    			publishProgress("Make screenshot");
    			if (!FtpLibrary.connect(context))
	    				return false;
	    	
	    			FtpLibrary.sendCommand(screenshotbig);
					Thread.sleep(1500);					
	    			
    		if (isCancelled()) {
 				// This return causes onPostExecute call on UI thread
 				return false;
 			    }    		    			
    			publishProgress("download screenshot");
    			Thread.sleep(500);
    			String error = "";
                	if (!FtpLibrary.download(screenshotfilename, folderout, false, error))
        			{
        				FtpLibrary.disconnect();
        				publishProgress(error);
        				return false;
        			}                                    			
    			FtpLibrary.disconnect();
    		}
    		catch (InterruptedException e) {			
    			Toast.makeText(context, "error - " + e.getMessage(), Toast.LENGTH_LONG).show();
    			//e.printStackTrace();
    		}
    	 catch (SocketException e) {
			Toast.makeText(context, "1 SocketException = " + e.getMessage(), Toast.LENGTH_LONG).show();			
			e.printStackTrace();
		} catch (IOException e) {			
			Toast.makeText(context, "2 IOException = " + e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		catch (Exception e) {			
			Toast.makeText(context, "3 Exception = " + e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}    		
    	}
    	return true;
    }
    
    public int IsConnectOK()
    {
    	return myresult; 
    }
    
    public boolean CheckConnect() // фукция проверки
    {    	
        try
        {
        	Thread.sleep(1000);
        }
        catch (Exception e)
        {
            
        }
        return true;  ///!!!!!!!!!!!!!!!!!!!!!
    };
    
    public  int sendFtpCommand()
	{    	
		int result = 0;	
		try{
			
			result = FtpLibrary.CheckConnect(context);			
				
		}
		catch (Exception e) {
			//Toast.makeText(context, "Exception = " + e.getMessage() , Toast.LENGTH_LONG).show();
			result = -6;
		}	
				
		return result;
		
	}
    
}