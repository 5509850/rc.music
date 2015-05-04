package in.alexsoft.rc.music;
import in.alexsoft.rc.music.R;
import in.alexsoft.rc.music.Network.NetInfo;

import java.io.*;
import java.net.*;

import android.content.Context;
import android.widget.Toast;

public class UDPsend {
	
	public static String sendUdpCommand(int code, String key, Context context, String versionprogram)
	{
		String ip = Preferenc.getFtpServer(context);
		String str = "empty";
		//InetAddress server = null;
//		try {
//			//server = InetAddress.getLocalHost();
//			ip = Preferenc.getFtpServer(context); //TODO
////			   if (!CheckValidIP(ip))
////			   {
////				   try {
////						InetAddress address = InetAddress.getByName(Preferenc.getFtpServer(context));
////						ip = address.getHostAddress(); //get IP by name HOST
////					} catch (UnknownHostException e) {
////						// TODO Auto-generated catch block
////						Toast.makeText(context, "E1 " + e.getMessage(), Toast.LENGTH_LONG).show();
////						//e.printStackTrace();
////					} 
////			   }
//			   
//			//server = InetAddress.getByName(ip);
//		} catch (UnknownHostException e) {
//			Toast.makeText(context, "E2  UnknownHostException = " + e.getMessage(), Toast.LENGTH_LONG).show();
//			e.printStackTrace();
//		}
//		catch (Exception e) {
//			Toast.makeText(context, "E3   Exception = " + e.getMessage(), Toast.LENGTH_LONG).show();
//			e.printStackTrace();
//		}
		
		 try {  
			 //codeexample = 0|1|2|3|4|5
	            //0 = code
	            //1 = pwd
	            //2 = ip
	            //3 = key
	            //4 = version program
	            //5 = serverIP 
		   String ipBrdcst = "255.255.255.255";//broadcast  address                  
           //String ip = Preferenc.getFtpServer(context);
           String ipsend = "";   
         	  ipsend  = ipBrdcst;
         	  
           String localIP = "ip";
           NetInfo net = null;
           net = new NetInfo(context);
           net.getWifiInfo();
           if (net.ssid != null) 
           {
               net.getIp();
              //  
               localIP = context.getString(R.string.my_ip, net.ip);
           }  
		    String fullcode = code + "|" 
	            + Preferenc.getFtpPass(context) + "|" 
		    		+ localIP +   "|"
		    		+ key +   "|"
		    		+ versionprogram +   "|"
		    		+ ipsend;//  1}|{2}|{3}|{4}|{5}", , textBox_code.Text, listIP[0], key, versionprogram, textBox_ip.Text), portudp);  
		   // if (Preferenc.getFast(context))
		    fullcode = fullcode + "|#";//add "|#" for not wait answer from server!
		    
		    byte[] bytes = new byte[256]; 
		    bytes = fullcode.getBytes();
		    
//		   if (!CheckValidIP(ip))
//		   {
//			   try {
//					InetAddress address = InetAddress.getByName(Preferenc.getFtpServer(context));
//					ip = address.getHostAddress(); //get IP by name HOST
//				} catch (UnknownHostException e) { 
//					Toast.makeText(context, "E4 " + e.getMessage(), Toast.LENGTH_LONG).show();						
//				} 
//		   }
		    
		    InetAddress address;	
			  //broadcast send OR only one IP
	       	       
	     	  address = InetAddress.getByName(ipBrdcst);	       
	    	  //address = InetAddress.getByName(ip);
	      
       int port = Integer.valueOf(Preferenc.getUdpPort(context));	
       //String.Format("#{0}#", code)
       DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
       DatagramSocket socket = new DatagramSocket();       
       socket.send(packet);       
      /* if (!Preferenc.getFast(context)) //not receive if fast mode
	       {
	//-----------------------------------
	       byte[] received = new byte[512];
	       packet = new DatagramPacket(received, received.length );
	       socket.receive(packet);       
	       str = new String(received, "UTF-8");     
	  //----------------------------------------
	       }*/
       socket.close();
   }
   catch (Exception e) {
	   Toast.makeText(context, "E4 " + e.getMessage() + " - " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();				
        
   }
   return str;
		 }
	
	public static String sendUdpPhone(String phone, Context context, String versionprogram)
	{
		String ip = Preferenc.getFtpServer(context);
		String str = "empty";
	
		
		 try {  
			
		   String ipBrdcst = "255.255.255.255";//broadcast  address
		    String fullcode = "$" + phone + "$|#";
		    byte[] bytes = new byte[256]; 
		    bytes = fullcode.getBytes();		    
		    InetAddress address = InetAddress.getByName(ip);	
			  //broadcast send OR only one IP	     
	     	  address = InetAddress.getByName(ipBrdcst);  
	     
	      
       int port = Integer.valueOf(Preferenc.getUdpPort(context));      
       DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
       DatagramSocket socket = new DatagramSocket();
       socket.send(packet);      
       socket.close();
   }
   catch (Exception e) {
	   Toast.makeText(context, "E4 " + e.getMessage() + " - " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();				
        
   }
   return str;
		 }
	
	 private static IPAddressValidator ipAddressValidator;
	
	 private static boolean CheckValidIP(String ip)
		{
			ipAddressValidator = new IPAddressValidator();
			return ipAddressValidator.validate(ip);			
		}
	    

}


