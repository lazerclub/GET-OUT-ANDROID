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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProfileActivity extends Activity{
     
	private int numBeacons;
	TextView lead;
	EditText email;
	EditText username;
	EditText password;
	Button b;
	ProgressBar p;
	TextView loading;
	ReqUtils ru;
	SharedPreferences sp;
	
	public void onCreate(Bundle icicle) { 
          super.onCreate(icicle); 
          
          //no title bar
          requestWindowFeature(Window.FEATURE_NO_TITLE);
          //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
          setContentView(R.layout.profile);
          
          lead = (TextView) findViewById(R.id.carrier);
          loading = (TextView) findViewById(R.id.loadingtext);
          email = (EditText) findViewById(R.id.email);
          username = (EditText) findViewById(R.id.username);
          password = (EditText) findViewById(R.id.password);
          b = (Button) findViewById(R.id.thebutton);
          p = (ProgressBar) findViewById(R.id.progressbar);
          
	      lead.setText("What do you want your profile to say?");
	      
	      sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	      String u = sp.getString("username", "");
	      if(!u.equals("")) {
	          username.setText(u);
	      }
	      
          
          b.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				if(email.getText().toString().equals("") || username.getText().toString().equals("")|| password.getText().toString().equals("")){
					return;
				}
				if(password.getText().toString().length()<6){
					return;
				}
				
				b.setPressed(true);
				b.setEnabled(false);
				p.setVisibility(View.VISIBLE);
				loading.setVisibility(View.VISIBLE);
				loading.setText("Sending..");
				
				Handler mHandler = new Handler();
				mHandler.postDelayed(new Runnable(){

					public void run() {
						
						try {
							String response = ru.registerUser(username.getText().toString(), email.getText().toString(), password.getText().toString());
//							if(ru.postUser(phone.getText().toString(), profile.getText().toString())){
//								loading.setText("Okay!");
//								Intent i = new Intent(RegisterActivity.this, SplashActivity.class);
//								startActivity(i);
//							}
//							else{
//								p.setVisibility(View.GONE);
//								b.setPressed(false);
//								b.setEnabled(true);
//							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							b.setPressed(false);
							b.setEnabled(true);
						}
				
					}}, 500);
			}});
	      
	}
}
