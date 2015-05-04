package in.alexsoft.rc.music;

import in.alexsoft.rc.music.R;
import in.alexsoft.rc.music.Network.NetInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;




import android.content.Context;
import android.widget.Toast;

public final class FtpLibrary {
	
	private static FTPClient mFtp; 
	
	public FtpLibrary()
	{
		mFtp = new FTPClient();
	}
	
	public static boolean isActive()
	{
		return (mFtp != null);
		
	}
	
	public static void ReActive()
	{
		mFtp = new FTPClient();		
	}
	
	public static boolean connect(Context context) throws SocketException, IOException
	{
		boolean result = false;
		String userid = Preferenc.getFtpLogin(context);
		String pwd = Preferenc.getFtpPass(context);;
		InetAddress server = null;
		try {
			//server = InetAddress.getLocalHost();
			String ip = Preferenc.getFtpServer(context); //TODO
			   if (!CheckValidIP(ip))
			   {
				   try {
						InetAddress address = InetAddress.getByName(Preferenc.getFtpServer(context));
						ip = address.getHostAddress(); //get IP by name HOST
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
						//e.printStackTrace();
					} 
			   }
			   
			server = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			Toast.makeText(context, "UnknownHostException = " + e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		catch (Exception e) {
			Toast.makeText(context, "0  Exception = " + e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		int port = Integer.valueOf(Preferenc.getFtpPort(context));			
		try{
			
				mFtp = new FTPClient();
//			
//				if (!isActive())
//					mFtp = new FTPClient();
						
			
			mFtp.connect(server, port); // Using port no=21	
		}
		catch (Exception e) {
			Toast.makeText(context, "-7 Exception = " + e.getMessage(), Toast.LENGTH_LONG).show();
			return false;
		}
		
		result = mFtp.login(userid, pwd);
		return result;
	}
	
	  private static boolean CheckValidIP(String ip)
		{
		  IPAddressValidator ipAddressValidator = new IPAddressValidator();
			return ipAddressValidator.validate(ip);			
		}
	
	public static int CheckConnect(Context context) throws SocketException, IOException
	{
		//-7 не найден хост (не верный ip)
		//-1 не верный пароль (код)
		//0 = ОК		
		String userid = Preferenc.getFtpLogin(context);
		String pwd = Preferenc.getFtpPass(context);;
		InetAddress server = null;
		try {
			//server = InetAddress.getLocalHost();
			String ip = Preferenc.getFtpServer(context); //TODO
			   if (!CheckValidIP(ip))
			   {
				   try {
						InetAddress address = InetAddress.getByName(Preferenc.getFtpServer(context));
						ip = address.getHostAddress(); //get IP by name HOST
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
						//e.printStackTrace();
					} 
			   }
			server = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			Toast.makeText(context, "UnknownHostException = " + e.getMessage(), Toast.LENGTH_LONG).show();			
		}
		catch (Exception e) {			
			Toast.makeText(context, "Exception = " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
		int port = Integer.valueOf(Preferenc.getFtpPort(context));			
		try{
			mFtp = new FTPClient();
			mFtp.connect(server, port); // Using port no=22	
		}
		catch (Exception e) {			
			return -7;
		}
		
		if (mFtp.login(userid, pwd))
		{
			mFtp.disconnect();
			return 0;
		}
		else
		{
			mFtp.disconnect();
			return -1;
		}
	}
	
	public static String CheckServerConnect(Context context) throws SocketException, IOException
	{
		boolean result = false;
		String userid = Preferenc.getFtpLogin(context);
		String pwd = Preferenc.getFtpPass(context);;
		InetAddress server = null;
		try {
			//server = InetAddress.getLocalHost();
			String ip = Preferenc.getFtpServer(context); //TODO
			   if (!CheckValidIP(ip))
			   {
				   try {
						InetAddress address = InetAddress.getByName(Preferenc.getFtpServer(context));
						ip = address.getHostAddress(); //get IP by name HOST
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
						//e.printStackTrace();
					} 
			   }
			server = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			Toast.makeText(context, "UnknownHostException = " + e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		int port = Integer.valueOf(Preferenc.getFtpPort(context));			
		
		mFtp.connect(server, port); // Using port no=21
		//result = mFtp.login(userid, pwd);
		
		return mFtp.getStatus();
	}
	
	//upload one file
	public static boolean upload(String remoteFileName, InputStream aInputStream) throws Exception
	{
		mFtp.setFileType(FTP.BINARY_FILE_TYPE);
		mFtp.enterLocalPassiveMode();		
		boolean aRtn= mFtp.storeFile(remoteFileName, aInputStream);
		aInputStream.close();
		return aRtn;
	}
	
	//upload all files from folder in SD card! //upload ONLY file *.in
	public static void upload(String SourceFolderName, boolean deleteAfterCopy, Context context, int code) throws Exception
	{
		File dir;
		if (SourceFolderName != null && context == null)
		{
			dir = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + SourceFolderName + "/");
		}
		else
		{	  
			dir = new File(context.getResources().getString(R.string.searchurl) + "/" + code + ".in");
		}
		   	            
         if (dir != null && !dir.exists() && !dir.mkdirs()) {
        	 throw new Exception("local folder not found" + SourceFolderName + " not found");
			}
         if (code == 0) //old methode from SD
         {
        	 File[] filesource = dir.listFiles();
             
    		 for(int i = 0; i < filesource.length; i++) 
             {
                 File from = filesource[i];
                 if (from.isFile())//пропускаем папки
                 {
                	String fileName = from.getName();
                	
                	
                	int dotPos = fileName.lastIndexOf(".");
                	String ext = fileName.substring(dotPos);  
                	
                
                	//пропускаем файлы с отличным расширением от *.in
    	            	if (ext != null && ext.equals(".in"))		            	
    			            	{
    			            		File source = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + SourceFolderName + "/" + fileName);		
    			             		InputStream aInputStream = null;
    			             		try {
    			             			aInputStream = new FileInputStream(source);
    			             		} catch (FileNotFoundException e) {
    			             			e.printStackTrace();
    			             			throw new Exception("1 - aInputStream = new FileInputStream(source) ");			
    			             		}		
    			             		mFtp.setFileType(FTP.BINARY_FILE_TYPE);
    			             		mFtp.enterLocalPassiveMode();		
    			             		boolean aRtn= mFtp.storeFile(fileName, aInputStream);
    			             		aInputStream.close();
    			             		if (!aRtn)
    			             			throw new Exception("2 - boolean aRtn= mFtp.storeFile(fileName, aInputStream)");
    			             		
    			             		if (deleteAfterCopy)
    			                  	{
    			    	                		try {
    			    		                		if(!from.delete()){
    			    		                			//файл не удален!
    			    		                		}
    			    	                	}
    			    	                	catch (Exception e) {
    			    	                		throw new Exception("3 - error deleting file");
    			    	    				}
    			                  	}          
    			                }
                		
                	}
             
             }//end cycl		
         }
         else //(code != 0) - new methode copy to ftp from Assets
         {
        	 
             if (dir.isFile())//пропускаем папки
             {
            	String fileName = dir.getName();
            	
            	
            	int dotPos = fileName.lastIndexOf(".");
            	String ext = fileName.substring(dotPos);  
            	
            
            	//пропускаем файлы с отличным расширением от *.in
	            	if (ext != null && ext.equals(".in"))		            	
			            	{
			            		File source = dir;		
			             		InputStream aInputStream = null;
			             		try {
			             			aInputStream = new FileInputStream(source);
			             		} catch (FileNotFoundException e) {
			             			e.printStackTrace();
			             			throw new Exception("101 - aInputStream = new FileInputStream(source) ");			
			             		}		
			             		mFtp.setFileType(FTP.BINARY_FILE_TYPE);
			             		mFtp.enterLocalPassiveMode();		
			             		boolean aRtn= mFtp.storeFile(fileName, aInputStream);			             		
			             		aInputStream.close();
			             		if (!aRtn)
			             			throw new Exception("202 - boolean aRtn= mFtp.storeFile(fileName, aInputStream)");      
			                }            		
            	} 	
         }
	}
	
	public static int sendCommand(int code) throws Exception
	{
		try{
			mFtp.setFileType(FTP.BINARY_FILE_TYPE);
     		mFtp.enterLocalPassiveMode();
     		return mFtp.sendCommand("#" + code + "#");
     		             		}
		catch(Exception ex)
		{
			return -666;
		}				             		
	}
	
	public static int sendPlaylistCommand(int code, String key, Context context) throws Exception
	{
		try{
			mFtp.setFileType(FTP.BINARY_FILE_TYPE);
     		mFtp.enterLocalPassiveMode();
     		//ftp.SendCom(String.Format("{0}|{1}|{2}|{3}|{4}|{5}", code, textBox_code.Text, listIP[0], key, versionprogram, textBox_ip.Text));
     		//code, key, context, 
     		NetInfo net = null;
            net = new NetInfo(context);
            net.getWifiInfo();
            String localIP = "";
            if (net.ssid != null) 
            {
            	net.getIp();
                localIP = context.getString(R.string.my_ip, net.ip);
            }  
     		  
     		 String fullcode = code + "|" 
	            + Preferenc.getFtpPass(context) + "|" 
		    		+ localIP +   "|"
		    		+ key +   "|"
		    		+ context.getResources().getString(R.string.versionprogram) +   "|"
		    		+ Preferenc.getFtpServer(context);
     		return mFtp.sendCommand(fullcode);
     		             		}
		catch(Exception ex)
		{
			return -666;
		}				             		
	}
	
	public static int sendPhone(String phone) throws Exception
	{
		try{
			mFtp.setFileType(FTP.BINARY_FILE_TYPE);
     		mFtp.enterLocalPassiveMode();
     		return mFtp.sendCommand("$" + phone + "$");
     		             		}
		catch(Exception ex)
		{
			return -666;
		}				             		
	}
	
	public static void uploadfile(File source) throws Exception
	{              		
			             		InputStream aInputStream = null;
			             		try {
			             			aInputStream = new FileInputStream(source);
			             		} catch (FileNotFoundException e) {
			             			e.printStackTrace();
			             			throw new Exception("1 - aInputStream = new FileInputStream(source) ");			
			             		}		
			             		mFtp.setFileType(FTP.BINARY_FILE_TYPE);
			             		mFtp.enterLocalPassiveMode();
			             		
			             		boolean aRtn= mFtp.storeFile(source.getName(), aInputStream);
			             		aInputStream.close();
			             		if (!aRtn)
			             			throw new Exception("2 - boolean aRtn= mFtp.storeFile(fileName, aInputStream)");
			             		
			             		    		try {
			    		                		if(!source.delete()){
			    		                			//файл не удален!
			    		                		}
			    	                	}
			    	                	catch (Exception e) {
			    	                		throw new Exception("3 - ошибка при удалении файла");
			    	    				}
	}
	
	
	public static void disconnect() throws Exception
	{
		if (mFtp != null && mFtp.isConnected())
			mFtp.logout();//666
		
		mFtp.disconnect();
	}
	
	public static boolean download(String SourceFileName, String SourceFolderName, boolean needDelete, String error)	        throws Exception
	    {
		error = "";
		File dir = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + SourceFolderName + "/");  	            
        if (dir != null && !dir.exists() && !dir.mkdirs()) {
        	error = ("local folder " + SourceFolderName + " not found");
        	return false;
       	 	//throw new Exception("local folder " + SourceFolderName + " not found");
			}     
        
        		File file = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + SourceFolderName + "/" + SourceFileName);
        		
        		OutputStream aOuputStream = null;
        		
        		try {
        			aOuputStream = new FileOutputStream(file);
        		} catch (FileNotFoundException e) {
        			e.printStackTrace();
        			//throw new Exception("aInputStream = new FileInputStream(source) ");
        			error = ("aInputStream = new FileInputStream(source) ");
        			return false;
        		}	
        		
        		mFtp.setFileType(FTP.BINARY_FILE_TYPE);
        		mFtp.enterLocalPassiveMode();        		
        		boolean aRtn= mFtp.retrieveFile(SourceFileName, aOuputStream);        		
        		aOuputStream.close();        		
        		if (!aRtn)
        		{
        			if (file.exists())
        			{
        				try{
        					file.delete();
        				}
        				catch(Exception ex)
        				{
        					error = (ex.getMessage());
        					return false;
        					//throw new Exception(ex.getMessage());
        				}
        				
        			}
        			error = ("can not download file!");
        			return false;
        			//throw new Exception("can not download file!");
        		}
        			
        		else
        			if(needDelete)
        			{
        				if (!mFtp.deleteFile(SourceFileName))
        				{
        					error = 	("can not delete file!");
        					return false;
        					//throw new Exception("can not delete file!");
        				}	
        			}
        		return true;
	    }	
}
