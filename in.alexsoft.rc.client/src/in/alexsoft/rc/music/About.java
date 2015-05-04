package in.alexsoft.rc.music;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class About extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    
	TextView tv_url;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);   
        tv_url = (TextView) this.findViewById(R.id.about_content);
        tv_url.setOnClickListener(this);
    }
	
	//ShowUrl(hotel_html[index]);	
	
	public void onClick(View v)
    {
		//ShowUrl(getResources().getString(R.string.about_url));
	//	startActivity(new Intent(this, ADiscovery.class));
		finish();
    }
	
	private void ShowUrl(String URL)
    {   	
		Intent UrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
    	startActivity(UrlIntent);
    }
    
}