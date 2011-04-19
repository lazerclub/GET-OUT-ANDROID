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

public class AboutActivity extends Activity{
     
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
          setContentView(R.layout.about);
          
          lead = (TextView) findViewById(R.id.carrier);
          loading = (TextView) findViewById(R.id.loadingtext);
          b = (Button) findViewById(R.id.post_button);
          p = (ProgressBar) findViewById(R.id.progressbar);
          
	      lead.setText("We've been noticing something strange for a while on the internet.\n" + 
	      		"\n" + 
	      		"Dozens and dozens of social applications have sprung up over the years, but until recently, they've been all about the friends you already have. Sites like Twitter have introduced the notion of following people, which is basically a non-reciprocal friendship.\n" + 
	      		"\n" + 
	      		"Which led us to wonder, what if you could follow a place? What if you could follow an activity and be notified when someone was looking for someone to join them?\n" + 
	      		"\n" + 
	      		"We wondered, what if you could see what everyone around you was doing, and join them? That's not a social network. That's a social tool.\n" + 
	      		"\n" + 
	      		"So we've built something called GET OUT, and we think it's going to make it much easier to meet new people. We're currently testing, and we'll be opening up to a much larger audience very soon.\n" + 
	      		"\n" + 
	      		"If you're interested in knowing when invites are available, please sign up to be notified. And if you're a developer, we have already built an API which is in a preview phase - email chris@lazerclub.co for more information.\n" + 
	      		"\n" + 
	      		"We're pretty excited to see where this takes us. Maybe we can help some people grow their social networks. Or maybe we'll just make it much easier to Meet People.\n" + 
	      		"");

	}
	
}
