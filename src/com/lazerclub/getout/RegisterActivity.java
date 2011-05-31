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
import android.widget.Toast;

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
	Context c;
    private SharedPreferences prefs;
	
	public void onCreate(Bundle icicle) { 
          super.onCreate(icicle); 
          c = this;
          
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
          
	      lead.setText("Hello, new friend!\nYou need to register before you can meet new people!");
          
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
				loading.setText("Registering..");
				
				Handler mHandler = new Handler();
				mHandler.postDelayed(new Runnable(){

					public void run() {
						
						try {
						    System.out.println("Registering..");
							String response = ru.registerUser(username.getText().toString(), email.getText().toString(), password.getText().toString());
							
							if(response != "Victory!") {
		                            b.setPressed(false);
		                            b.setEnabled(true);
		                            p.setVisibility(View.GONE);
		                            loading.setVisibility(View.GONE);
		                            Toast.makeText(c, response, Toast.LENGTH_SHORT).show();
							}else {
                              Intent i = new Intent(RegisterActivity.this, ProfileActivity.class);
                              startActivity(i);
							}
							
						} catch (Exception e) {
							e.printStackTrace();
							b.setPressed(false);
							b.setEnabled(true);
						}
					}}, 200);
			}});
          
          ru = new ReqUtils(this);
          
          prefs = PreferenceManager.getDefaultSharedPreferences(this);
          String oat = prefs.getString("oat", null);
          String oats = prefs.getString("oats", null);
          if(oat != null && oats != null) {
              Intent i = new Intent(RegisterActivity.this, MainMenuActivity.class);
              startActivity(i);
          }
		     
          Account[] accounts = AccountManager.get(this).getAccounts();
          for (Account account : accounts) {
            String possibleEmail = account.name;
            email.setText(possibleEmail);
            
            if(possibleEmail.contains("@")) {
                username.setText(possibleEmail.split("@")[0]);
            }
            
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
