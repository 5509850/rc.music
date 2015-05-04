package in.alexsoft.rc.music;

import in.alexsoft.rc.music.R;
import in.alexsoft.rc.music.Network.HostBean;
import in.alexsoft.rc.music.Network.NetInfo;
import in.alexsoft.rc.music.Utils.Export;
import in.alexsoft.rc.music.Utils.Prefs;
import in.alexsoft.rc.music.net.ActivityNet;
import in.alexsoft.rc.music.net.ActivityPortscan;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;







import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

final public class ADiscovery extends ActivityNet implements OnItemClickListener, OnTaskCompleteListener {

	private AsyncTaskManager mAsyncTaskManager;
    private final String TAG = "ActivityDiscovery";
    public final static long VIBRATE = (long) 250;
    public final static int SCAN_PORT_RESULT = 1;    
    
    private static LayoutInflater mInflater;
    private int currentNetwork = 0;
    private long network_ip = 0;
    private long network_start = 0;
    private long network_end = 0;
    private List<HostBean> hosts = null;
    private HostsAdapter adapter;
    private Button btn_discover;
    private AbstractDiscov mDiscoveryTask = null;
    String defaultCode = "test";
    private   DBAdapter db;
	private   LevelHelperDB lh;	

    // private SlidingDrawer mDrawer;
    
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
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.discovery);
        mInflater = LayoutInflater.from(ctxt);
        
        //Создавать AsyncTaskManager в методе onCreate
        mAsyncTaskManager = new AsyncTaskManager(this, this);
        mAsyncTaskManager.handleRetainedTask(getLastNonConfigurationInstance());

        // Discover
        btn_discover = (Button) findViewById(R.id.btn_discover);
        btn_discover.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startDiscovering();
            }
        });

        // Close
        Button btn_options = (Button) findViewById(R.id.btn_options);
        btn_options.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
    	 
    	        if (mDiscoveryTask != null) {
    	            mDiscoveryTask.cancel(true);
    	            mDiscoveryTask = null;
    	        }
            	 
                finish();    	       
            }
        });

        // Hosts list
        adapter = new HostsAdapter(ctxt);
        ListView list = (ListView) findViewById(R.id.output);
        list.setAdapter(adapter);
        list.setItemsCanFocus(false);
        list.setOnItemClickListener(this);
        list.setEmptyView(findViewById(R.id.list_empty));
        
        setInfo();
        ShowSavedRecordsFromLocalDb();
//        try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {			
//			e.printStackTrace();
//		}
//        startDiscovering();        
    }
    
    //import by.sbb.wificallback.DBAdapter;
    //import by.sbb.wificallback.LevelHelperDB;
    
    private void ShowSavedRecordsFromLocalDb()
	{
    	
    	 //4 TEST-----------------------------------------------------------------------
//        final HostBean host = new HostBean();               
//        host.ipAddress = "ip";       
//        host.hardwareAddress = "MAC";
//        host.hostname = "hostname";
//        if(hosts == null)
//        {
//        	initList();	
//        }                
//        addHost(host);
//        host.ipAddress = "ip2";       
//        host.hardwareAddress = "MAC2";
//        host.hostname = "hostname2";
//        addHost(host);
//        host.ipAddress = "ip3";       
//        host.hardwareAddress = "MAC4";
//        host.hostname = "hostname3";
//        
//       //host.position = !!!!!!!!!!!!!!!!!!!
//        addHost(host);
        
        db = new DBAdapter(this.getBaseContext());
        lh = new LevelHelperDB();
        List<HostBean> hB = lh.GetAllRecords(db);
        initList();
        for (int i = 0; i < hB.size(); i++)
        {
        	if (hB.get(i) != null)
        		addHost(hB.get(i));
        }
//    		{
//    			sb.append(names.get(i) + "\n");
//    		}	
        
      //4 TEST-----------------------------------------------------------------------
		
//		db = new DBAdapter(this.getBaseContext());
//	        lh = new LevelHelperDB();
//	        
//		List<String> names = lh.GetAllRecords(db);
//		
//		StringBuilder sb = new StringBuilder();
//		
//		sb.append("Записи последних " + Prefs.getCountLastCalls(this.getBaseContext()) + " звонков в БД:\n");
//		 
//		for (int i = 0; i < names.size(); i++)
//		{
//			sb.append(names.get(i) + "\n");
//		}		
	}
    
    private boolean getAutoCode()
    {
    	return (Preferenc.getAutoCode(getApplicationContext()) || Preferenc.getFtpPass(getApplicationContext()).equals("test"));
    }
    

    @Override
    public void onResume() {
        super.onResume();
       
        //Toast.makeText(getApplicationContext(),  "OnResume", Toast.LENGTH_SHORT).show();
    } 
    
    @Override
    public void onPause()
    {   	    	
    	if (mDiscoveryTask != null) {
            mDiscoveryTask.cancel(true);
            mDiscoveryTask = null;
        }
    	//Toast.makeText(getApplicationContext(), "onPause" , Toast.LENGTH_LONG).show();
        super.onPause();
       
    }

   

    protected void setInfo() {
        // Info
        ((TextView) findViewById(R.id.info_ip)).setText(info_ip_str);
        ((TextView) findViewById(R.id.info_in)).setText(info_in_str);
        ((TextView) findViewById(R.id.info_mo)).setText(info_mo_str);

        // Scan button state
        if (mDiscoveryTask != null) {
            setButton(btn_discover, R.drawable.cancel, false);
            btn_discover.setText(R.string.btn_discover_cancel);
            btn_discover.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    cancelTasks();                    
                }
            });
        }

        if (currentNetwork != net.hashCode()) {
            Log.i(TAG, "Network info has changed");
            currentNetwork = net.hashCode();
          //  Toast.makeText(getApplicationContext(),  "3 cancelTasks" , Toast.LENGTH_SHORT).show();

            // Cancel running tasks
            //Toast.makeText(getApplicationContext(),  "3 cancelTasks" , Toast.LENGTH_SHORT).show();
            cancelTasks();
        } else {
            return;
        }

        // Get ip information
        network_ip = NetInfo.getUnsignedLongFromIp(net.ip);
        if (prefs.getBoolean(Prefs.KEY_IP_CUSTOM, Prefs.DEFAULT_IP_CUSTOM)) {
            // Custom IP
            network_start = NetInfo.getUnsignedLongFromIp(prefs.getString(Prefs.KEY_IP_START,
                    Prefs.DEFAULT_IP_START));
            network_end = NetInfo.getUnsignedLongFromIp(prefs.getString(Prefs.KEY_IP_END,
                    Prefs.DEFAULT_IP_END));
        } else {
            // Custom CIDR
            if (prefs.getBoolean(Prefs.KEY_CIDR_CUSTOM, Prefs.DEFAULT_CIDR_CUSTOM)) {
                net.cidr = Integer.parseInt(prefs.getString(Prefs.KEY_CIDR, Prefs.DEFAULT_CIDR));
            }
            // Detected IP
            int shift = (32 - net.cidr);
            if (net.cidr < 31) {
                network_start = (network_ip >> shift << shift) + 1;
                network_end = (network_start | ((1 << shift) - 1)) - 1;
            } else {
                network_start = (network_ip >> shift << shift);
                network_end = (network_start | ((1 << shift) - 1));
            }
            // Reset ip start-end (is it really convenient ?)
            Editor edit = prefs.edit();
            edit.putString(Prefs.KEY_IP_START, NetInfo.getIpFromLongUnsigned(network_start));
            edit.putString(Prefs.KEY_IP_END, NetInfo.getIpFromLongUnsigned(network_end));
            edit.commit();
        }
    }

    protected void setButtons(boolean disable) {
        if (disable) {
            setButtonOff(btn_discover, R.drawable.disabled);
        } else {
            setButtonOn(btn_discover, R.drawable.discover);
        }
    }

    protected void cancelTasks() {
        if (mDiscoveryTask != null) {
            mDiscoveryTask.cancel(true);
            mDiscoveryTask = null;
        }
    }

    // Listen for Activity results
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SCAN_PORT_RESULT:
                if (resultCode == RESULT_OK) {
                    // Get scanned ports
                    if (data != null && data.hasExtra(HostBean.EXTRA)) {
                        HostBean host = data.getParcelableExtra(HostBean.EXTRA);
                        if (host != null) {
                            hosts.set(host.position, host);
                        }
                    }
                }
            default:
                break;
        }
    }
    
   
		
	
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final HostBean host = hosts.get(position);
        AlertDialog.Builder dialog = new AlertDialog.Builder(ADiscovery.this);
        dialog.setTitle(R.string.discover_action_title);
        dialog.setItems(new CharSequence[] { getString(R.string.discover_action_assign), getString(R.string.discover_action_check),  getString(R.string.discover_action_scan), getString(R.string.discover_action_rename), getString(R.string.discover_action_up), getString(R.string.discover_action_down), getString(R.string.discover_action_del) }, 
        		new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:                    	
                        
                    	 Preferenc.setNewIpAdress(getApplicationContext(), host.ipAddress.toString());
                    	 Preferenc.setNewMacAdress(getApplicationContext(), host.hardwareAddress.toString());
                    	 if (getAutoCode())//save code auto if autocode pref
                    	 {
                    			String[] result = host.ipAddress.toString().split("[.]");
                    			if (result.length == 4)
                    			{                    				
                    				Preferenc.setNewPass(getApplicationContext(), result[0] + "" + result[1] + "" + result[2] + "" + result[3]);
                    			}
                    	 }                    	 
                    	 if (mDiscoveryTask != null) {
                             mDiscoveryTask.cancel(true);
                             mDiscoveryTask = null;
                         }
                    	 Toast.makeText(getApplicationContext(), getString(R.string.discover_action_saved) , Toast.LENGTH_LONG).show();
                    	 finish();
                    	 
                        break;
                        
                    case 1:                    	
                        
                   	 Preferenc.setNewIpAdress(getApplicationContext(), host.ipAddress.toString());
                   	 Preferenc.setNewMacAdress(getApplicationContext(), host.hardwareAddress.toString());
                   	 if (getAutoCode())//save code auto if autocode pref
                   	 {
                   			String[] result = host.ipAddress.toString().split("[.]");
                   			if (result.length == 4)
                   			{                    				
                   				Preferenc.setNewPass(getApplicationContext(), result[0] + "" + result[1] + "" + result[2] + "" + result[3]);
                   			}
                   	 }                    	 
                   	 if (mDiscoveryTask != null) {
                            mDiscoveryTask.cancel(true);
                            mDiscoveryTask = null;
                        }
                   	 Toast.makeText(getApplicationContext(), getString(R.string.discover_action_saved) , Toast.LENGTH_LONG).show();
                   	 
                   	startPing();
                   	 
                       break;

                    case 2:
                    	// Start portscan
                    	 Preferenc.setNewIpAdress(getApplicationContext(), host.ipAddress.toString());
                    	 Preferenc.setNewMacAdress(getApplicationContext(), host.hardwareAddress.toString());
                    	 if (getAutoCode())//save code auto if autocode pref
                    	 {
                    			String[] result = host.ipAddress.toString().split("[.]");
                    			if (result.length == 4)
                    			{                    				
                    				Preferenc.setNewPass(getApplicationContext(), result[0] + "" + result[1] + "" + result[2] + "" + result[3]);
                    			}
                    	 }                    	 
                    	 if (mDiscoveryTask != null) {
                             mDiscoveryTask.cancel(true);
                             mDiscoveryTask = null;
                         }
                    	 Toast.makeText(getApplicationContext(), getString(R.string.discover_action_saved) , Toast.LENGTH_LONG).show();
             
                    	
                        Intent intent = new Intent(ctxt, ActivityPortscan.class);
                        intent.putExtra(EXTRA_WIFI, NetInfo.isConnected(ctxt));
                        intent.putExtra(HostBean.EXTRA, host);
                        startActivityForResult(intent, SCAN_PORT_RESULT);
                        break;
                        
                    case 3:
                    	// Rename Host
                    	 final View v = mInflater.inflate(R.layout.dialog_edittext, null);
                         final EditText txt = (EditText) v.findViewById(R.id.edittext);
                         db = new DBAdapter(context);
                         lh = new LevelHelperDB();
                         txt.setText(lh.GetCurrentHostName(db, host.position));

                         final AlertDialog.Builder rename = new AlertDialog.Builder(
                                 ADiscovery.this);
                         rename.setView(v);
                         rename.setTitle(R.string.discover_action_rename);
                         rename.setPositiveButton(R.string.btn_ok, new OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 final String name = txt.getText().toString();
                                 host.hostname = name;
                                 lh.updateHostName(db, host.position, host.hostname);
                                 adapter.notifyDataSetChanged();
                                 Toast.makeText(ADiscovery.this,
                                         R.string.discover_action_saved, Toast.LENGTH_SHORT).show();
                             }
                         });
                         rename.setNegativeButton(R.string.btn_remove, new OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 host.hostname = null;
                                 lh.updateHostName(db, host.position, host.ipAddress);//
                                 adapter.notifyDataSetChanged();
                                 Toast.makeText(ADiscovery.this,
                                         R.string.discover_action_deleted, Toast.LENGTH_SHORT)
                                         .show();
                             }
                         });
                         rename.show();
                         break;
                         
                         
                    case 4:
                    	// move Host up                    	 
                         db = new DBAdapter(context);
                         lh = new LevelHelperDB();
                         lh.moveUpHostName(db, host.position);
                         ShowSavedRecordsFromLocalDb();
                         break;         
                    case 5:
                    	// move Host down
                    	 
                         db = new DBAdapter(context);
                         lh = new LevelHelperDB();
                         lh.moveDownHostName(db, host.position);
                         ShowSavedRecordsFromLocalDb();
                         break;
                    case 6:
                    	// remove Host 
                    	 
                         db = new DBAdapter(context);
                         lh = new LevelHelperDB();
                         lh.removeHost(db, host.position);
                         ShowSavedRecordsFromLocalDb();
                         break;
                                        }
            }
        });
        dialog.setNegativeButton(R.string.btn_discover_cancel, null);
        dialog.show();
    }

    static class ViewHolder {
        TextView host;
        TextView mac;
        TextView vendor;
        ImageView logo;
    }
    
    private void startPing()
    {
    	 if (!NetInfo.isConnected(ctxt)) {
             return;
         }
        // makeToast(R.string.scan_start);
         String status = Preferenc.getStatus(context);
 		
 		try {
 				 if (!checkConnection())
 					{	
 					    makeToast("NO CONNECTION TO THE SERVER!");
 						Preferenc.setReady(context, false);	
 						return;
 					} 			 
 			
 		} catch (Exception e) {
  
 			makeToast(e.getMessage());
 		}
 		
 		status = Preferenc.getStatus(context);
 		
 		//Toast.makeText(context, "status = " + status, Toast.LENGTH_LONG).show();
 		if (status.equals("-7"))
     	{  		
 			//ChangeServerIp(false);
 			makeToast(getResources().getString(R.string.ipnotcorrect));
     		Preferenc.setStatus(context, "1");     	
     		return;
     	}
     			    		
 		if (status.equals("-1"))
 		{ 			
 			makeToast(getResources().getString(R.string.codewrong));
 			Preferenc.setStatus(context, "1");
 			return;
 		}
 			
 		if (status.equals("-6"))
 		{			
 			makeToast(getResources().getString(R.string.ipbad));
 			Preferenc.setStatus(context, "1");			
 			return;
 		}
 			
 		if (status.equals("0"))
 		{			
 			makeToast("OK");
 			Preferenc.setReady(context, true);			
 		}
 		
 		if (!Preferenc.getReady(context))
 			{				
 				makeToast("not ready");
 				return;
 			}
    }
    
    private boolean checkConnection() throws InterruptedException
    {
    	mAsyncTaskManager.setupTask(new Task(getResources(), context, 0));
    	Thread.sleep(1500);
    	return Preferenc.getReady(context);
    }
    
    private Context context = this;
    
    public void onTaskComplete(Task task) 
    {	    	
		if (task.isCancelled()) 
		{
		    // Report about cancel
		    Toast.makeText(this, R.string.task_cancelled, Toast.LENGTH_LONG)
			    .show();
		} 		
    }
    
    private void makeToast(String msg) {
        Toast.makeText(getApplicationContext(), (CharSequence) msg, Toast.LENGTH_LONG).show();
    }


    // Custom ArrayAdapter
    private class HostsAdapter extends ArrayAdapter<Void> {
        public HostsAdapter(Context ctxt) {
            super(ctxt, R.layout.list_host, R.id.list);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_host, null);
                holder = new ViewHolder();
                holder.host = (TextView) convertView.findViewById(R.id.list);
                holder.mac = (TextView) convertView.findViewById(R.id.mac);
                holder.vendor = (TextView) convertView.findViewById(R.id.vendor);
                holder.logo = (ImageView) convertView.findViewById(R.id.logo);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final HostBean host = hosts.get(position);
            
            int number = host.position + 1;//-----------------------------
            
            if (host.deviceType == HostBean.TYPE_GATEWAY) {
                holder.logo.setImageResource(R.drawable.router);
            } else if (host.isAlive == 1 || !host.hardwareAddress.equals(NetInfo.NOMAC)) {
                holder.logo.setImageResource(R.drawable.computer);
            } else {
                holder.logo.setImageResource(R.drawable.computer_down);
            }
            if (host.hostname != null && !host.hostname.equals(host.ipAddress)) {
                holder.host.setText(number + ") " + host.hostname + " (" + host.ipAddress + ")");
            } else {
            	
                holder.host.setText(number + ") " + host.ipAddress);
            }
            if (!host.hardwareAddress.equals(NetInfo.NOMAC)) {
                holder.mac.setText(host.hardwareAddress);
                if(host.hostname != null && host.os != null){//if(host.nicVendor != null){
                    holder.vendor.setText(host.os);//.hostname //.nicVendor
                } else {
                    holder.vendor.setText(R.string.info_unknown + number);
                }
                holder.mac.setVisibility(View.VISIBLE);
                holder.vendor.setVisibility(View.VISIBLE);
            } else {
                holder.mac.setVisibility(View.GONE);
                holder.vendor.setVisibility(View.GONE);
            }
            return convertView;
        }
    }

    /**
     * Discover hosts------------------
     */
    private void startDiscovering() {
    	
    	
    	 db = new DBAdapter(this.getBaseContext());
         lh = new LevelHelperDB();
         lh.deleteAll(db);
         //	Toast.makeText(getApplicationContext(),  "ADiscovery.addSaveHost error", Toast.LENGTH_LONG).show();
    	
    	
           //     mDiscoveryTask = new DnsDiscov(ADiscovery.this);           
    	mDiscoveryTask = new DefaultDiscov(ADiscovery.this);
        
        mDiscoveryTask.setNetwork(network_ip, network_start, network_end);
        mDiscoveryTask.execute();
        btn_discover.setText(R.string.btn_discover_cancel);
        setButton(btn_discover, R.drawable.cancel, false);
        btn_discover.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancelTasks();
            }
        });
        makeToast(R.string.discover_start);
        setProgressBarVisibility(true);
        setProgressBarIndeterminateVisibility(true);
        initList();
    }   


    public void stopDiscovering() {
        Log.e(TAG, "stopDiscovering()");
        mDiscoveryTask = null;
        setButtonOn(btn_discover, R.drawable.discover);
        btn_discover.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startDiscovering();
            }
        });
        setProgressBarVisibility(false);
        setProgressBarIndeterminateVisibility(false);
        btn_discover.setText(R.string.btn_discover);
    }

    private void initList() {
        // setSelectedHosts(false);
        adapter.clear();
        hosts = new ArrayList<HostBean>();
    }

    public void addHost(HostBean host) {
    	//Toast.makeText(getApplicationContext(),  "ip = " + host.ipAddress, Toast.LENGTH_SHORT).show();
    	//Toast.makeText(getApplicationContext(),  "mac = " + host.hardwareAddress, Toast.LENGTH_SHORT).show();
        host.position = hosts.size();
        hosts.add(host);
        adapter.add(null);
        checkWorkHost(host);//my code - check good host and save in Pref settings IP and MAC        
    }
    
    public void addAndSaveHost(HostBean host) {
    	//Toast.makeText(getApplicationContext(),  "ip = " + host.ipAddress, Toast.LENGTH_SHORT).show();
    	//Toast.makeText(getApplicationContext(),  "mac = " + host.hardwareAddress, Toast.LENGTH_SHORT).show();
        host.position = hosts.size();
        hosts.add(host);
        adapter.add(null);
        checkWorkHost(host);//my code - check good host and save in Pref settings IP and MAC
        
        db = new DBAdapter(this.getBaseContext());
        lh = new LevelHelperDB();
        if (lh.saveHost(db, host) == -2)
        	Toast.makeText(getApplicationContext(),  "ADiscovery.addSaveHost error", Toast.LENGTH_LONG).show();
        //0 insert
		 //-2 error insert        
    }
    
    private void checkWorkHost(HostBean host)
    {
    	//TODO
    	boolean OK = false;
    	
    	
    	
    	
    	//Toast.makeText(getApplicationContext(),  "ip = " + host.ipAddress, Toast.LENGTH_SHORT).show();
    	
    	if (OK)
    	{
    		if (mDiscoveryTask != null) {
	            mDiscoveryTask.cancel(true);
	            mDiscoveryTask = null;
	        }
    		
    		//host.ipAddress;
    		//host.hardwareAddress;
        	 
            finish();	
    	}
    }

    public static void scanSingle(final Context ctxt, String ip) {
        // Alert dialog
        View v = LayoutInflater.from(ctxt).inflate(R.layout.scan_single, null);
        final EditText txt = (EditText) v.findViewById(R.id.ip);
        if (ip != null) {
            txt.setText(ip);
        }
        AlertDialog.Builder dialogIp = new AlertDialog.Builder(ctxt);
        dialogIp.setTitle(R.string.scan_single_title);
        dialogIp.setView(v);
        dialogIp.setPositiveButton(R.string.btn_scan, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                // start scanportactivity
                Intent intent = new Intent(ctxt, ActivityPortscan.class);
                intent.putExtra(HostBean.EXTRA_HOST, txt.getText().toString());
                try {
                    intent.putExtra(HostBean.EXTRA_HOSTNAME, (InetAddress.getByName(txt.getText()
                            .toString()).getHostName()));
                } catch (UnknownHostException e) {
                    intent.putExtra(HostBean.EXTRA_HOSTNAME, txt.getText().toString());
                }
                ctxt.startActivity(intent);
            }
        });
        dialogIp.setNegativeButton(R.string.btn_discover_cancel, null);
        dialogIp.show();
    }

    private void export() {
        final Export e = new Export(ctxt, hosts);
        final String file = e.getFileName();

        View v = mInflater.inflate(R.layout.dialog_edittext, null);
        final EditText txt = (EditText) v.findViewById(R.id.edittext);
        txt.setText(file);

        AlertDialog.Builder getFileName = new AlertDialog.Builder(this);
        getFileName.setTitle(R.string.export_choose);
        getFileName.setView(v);
        getFileName.setPositiveButton(R.string.export_save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                final String fileEdit = txt.getText().toString();
                if (e.fileExists(fileEdit)) {
                    AlertDialog.Builder fileExists = new AlertDialog.Builder(ADiscovery.this);
                    fileExists.setTitle(R.string.export_exists_title);
                    fileExists.setMessage(R.string.export_exists_msg);
                    fileExists.setPositiveButton(R.string.btn_yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (e.writeToSd(fileEdit)) {
                                        makeToast(R.string.export_finished);
                                    } else {
                                        export();
                                    }
                                }
                            });
                    fileExists.setNegativeButton(R.string.btn_no, null);
                    fileExists.show();
                } else {
                    if (e.writeToSd(fileEdit)) {
                        makeToast(R.string.export_finished);
                    } else {
                        export();
                    }
                }
            }
        });
        getFileName.setNegativeButton(R.string.btn_discover_cancel, null);
        getFileName.show();
    }
    

    public void makeToast(int msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void setButton(Button btn, int res, boolean disable) {
        if (disable) {
            setButtonOff(btn, res);
        } else {
            setButtonOn(btn, res);
        }
    }

    private void setButtonOff(Button b, int drawable) {
        b.setClickable(false);
        b.setEnabled(false);
        b.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0);
    }

    private void setButtonOn(Button b, int drawable) {
        b.setClickable(true);
        b.setEnabled(true);
        b.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0);
    }
}
