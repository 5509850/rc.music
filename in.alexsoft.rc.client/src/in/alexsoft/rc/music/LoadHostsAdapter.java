package in.alexsoft.rc.music;

import java.util.List;

import in.alexsoft.rc.music.R;
import in.alexsoft.rc.music.ADiscovery.ViewHolder;
import in.alexsoft.rc.music.Network.HostBean;
import in.alexsoft.rc.music.Network.NetInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LoadHostsAdapter extends ArrayAdapter<Void> 
{
	
	private static LayoutInflater mInflater;
	private List<HostBean> hosts = null;
	
	
    public LoadHostsAdapter(Context ctxt) {
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
        if (host.deviceType == HostBean.TYPE_GATEWAY) {
            holder.logo.setImageResource(R.drawable.router);
        } else if (host.isAlive == 1 || !host.hardwareAddress.equals(NetInfo.NOMAC)) {
            holder.logo.setImageResource(R.drawable.computer);
        } else {
            holder.logo.setImageResource(R.drawable.computer_down);
        }
        if (host.hostname != null && !host.hostname.equals(host.ipAddress)) {
            holder.host.setText(host.hostname + " (" + host.ipAddress + ")");
        } else {
            holder.host.setText(host.ipAddress);
        }
        if (!host.hardwareAddress.equals(NetInfo.NOMAC)) {
            holder.mac.setText(host.hardwareAddress);
            if(host.hostname != null){//if(host.nicVendor != null){
                holder.vendor.setText(host.hostname);//.nicVendor
            } else {
                holder.vendor.setText(R.string.info_unknown);
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