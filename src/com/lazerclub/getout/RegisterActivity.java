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

public class RegisterActivity extends Activity{
     
	private int numBeacons;
	TextView lead;
	EditText email;
	EditText username;
	EditText password;
	Button b;
	ProgressBar p;
	TextView loading;
	ReqUtils ru;
	
	public void onCreate(Bundle icicle) { 
          super.onCreate(icicle); 
          
          //no title bar
          requestWindowFeature(Window.FEATURE_NO_TITLE);
          //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
          setContentView(R.layout.register);
          
          lead = (TextView) findViewById(R.id.carrier);
          loading = (TextView) findViewById(R.id.loadingtext);
          email = (EditText) findViewById(R.id.email);
          username = (EditText) findViewById(R.id.username);
          password = (EditText) findViewById(R.id.password);
          b = (Button) findViewById(R.id.thebutton);
          p = (ProgressBar) findViewById(R.id.progressbar);
          
	      lead.setText("Hello, new friend!\nYou need to make a profile so you can meet new people!");
          
          b.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				if(email.getText().toString().equals("") || username.getText().toString().equals("")|| password.getText().toString().equals("")){
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
						
//						try {
//							ru.authenticate();
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
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//							b.setPressed(false);
//							b.setEnabled(true);
//						}
				
					}}, 500);
			}});
          
          ru = new ReqUtils(this);
		     
          Account[] accounts = AccountManager.get(this).getAccounts();
          for (Account account : accounts) {
            String possibleEmail = account.name;
            email.setText(possibleEmail);
            return;
          }

	      
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	return false;
	    }
	    return super.onKeyDown(keyCode, event);
	}

}
