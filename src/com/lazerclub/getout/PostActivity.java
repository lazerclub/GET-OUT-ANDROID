package com.lazerclub.getout;

import java.util.Random;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class PostActivity extends Activity{
     
	private int numBeacons;
	TextView lead;
	EditText activity;
	EditText desc;
	Button b;
	ProgressBar p;
	TextView loading;
	ReqUtils ru;
	Context c;
	
	public void onCreate(Bundle icicle) { 
          super.onCreate(icicle); 
          c=this;
          //no title bar
          requestWindowFeature(Window.FEATURE_NO_TITLE);
          //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
          setContentView(R.layout.post);
          
          lead = (TextView) findViewById(R.id.carrier);
          loading = (TextView) findViewById(R.id.loadingtext);
          b = (Button) findViewById(R.id.post_button);
          p = (ProgressBar) findViewById(R.id.progressbar);
          
          activity = (EditText) findViewById(R.id.activity);
          desc = (EditText) findViewById(R.id.desc);
          
	      lead.setText("So, what do you feel like doing?\n");
          
          b.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				if(activity.getText().toString().equals("") || desc.getText().toString().equals("")){
					return;
				}
				
				b.setPressed(true);
				b.setEnabled(false);
				p.setVisibility(View.VISIBLE);
				loading.setVisibility(View.VISIBLE);
				loading.setText("Posting..");
				
				Handler mHandler = new Handler();
				mHandler.postDelayed(new Runnable(){

					public void run() {
						
						try {
						    Boolean resp = ru.postBeacon(activity.getText().toString(), desc.getText().toString());
						    
						    if(resp) {
						      Toast.makeText(c, "Posted!", Toast.LENGTH_SHORT).show();
                              Intent i = new Intent(PostActivity.this, ActivityListActivity.class);
                              startActivity(i);
                              finish();
						    }else {
						      Toast.makeText(c, "Something went wrong!", Toast.LENGTH_SHORT).show();
                              p.setVisibility(View.GONE);
                              loading.setVisibility(View.GONE);
                              b.setPressed(false);
                              b.setEnabled(true);
						    }
						    
						} catch (Exception e) {
                            Toast.makeText(c, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            p.setVisibility(View.GONE);
                            loading.setVisibility(View.GONE);
                            b.setPressed(false);
                            b.setEnabled(true);
						}
				
					}}, 500);
			}});
          
          ru = new ReqUtils(this);

	      
	}
	
}
