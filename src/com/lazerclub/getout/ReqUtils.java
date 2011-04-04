package com.lazerclub.getout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.lazerclub.getout.MyLocation.LocationResult;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

public class ReqUtils {
	
	private String consumer_key = "G6qHqbf6r4ye55HXlZjlLQ";
	private String consumer_secret = "eUjrmg7tDDw6t9dmTx30ksaI9tqVyyBBQpTGwiuI";
	private String name = "Down To Cuddle";
	private String callback_url = "http://api.downto.be/";
	private String request_url = "http://api.downto.be/oauth/request";
	private String request_token_url = "http://api.downto.be/oauth/request_token";
	private String access_token_url = "http://api.downto.be/oauth/access_token";
	private String authorize_token_url = "http://api.downto.be/oauth/authorize_token";
	
	private double lat;
	private double lon;

	private String oauth_token;
	private String oauth_token_secret;
	
	private Context c;
	private Location loc;
	public Boolean hasLoc = false;
	
	private SharedPreferences prefs;
	
	public ReqUtils(Context ccc){
		c = ccc;
		MyLocation myLocation = new MyLocation();
		LocationResult locationResult = new LocationResult(){
		    @Override
		    public void gotLocation(final Location location){
		        //Got the location!
		    	System.out.println("Got location!");
		    	
		    	loc = location;
		    	if (location != null) {
			    	lat = location.getLatitude();
			    	lon = location.getLongitude();
		    	}
		    	hasLoc = true;
		        };
		    };
		myLocation.getLocation(c, locationResult);
		prefs = PreferenceManager.getDefaultSharedPreferences(c);
	}
	
	public ReqUtils(Context ccc, double glat, double glon){
		c = ccc;
		lat = glat;
		lon = glon;
		
		MyLocation myLocation = new MyLocation();
		LocationResult locationResult = new LocationResult(){
		    @Override
		    public void gotLocation(final Location location){
		        //Got the location!
		    	System.out.println("Got location!");
		    	
		    	loc = location;
		    	hasLoc = true;
		        };
		    };
		myLocation.getLocation(c, locationResult);
		prefs = PreferenceManager.getDefaultSharedPreferences(c);
	}
	
	public Location getLocation(){
		return loc;
	}
	
	public int getBeacons(){
		double lat = loc.getLatitude();
		double lon = loc.getLongitude();
		String activity = "cuddle";
		
		HttpGet req = new HttpGet("http://api.downto.be/beacons?activity=" + activity + "&latitude=" + lat + "&longitude=" + lon);
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumer_key, consumer_secret);
		consumer.setTokenWithSecret(prefs.getString("oat", ""), prefs.getString("oats", ""));
		
		try {
			consumer.sign(req);
		} catch (OAuthMessageSignerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpClient hc = new DefaultHttpClient();
		HttpResponse response = null;
		try {
			 response =  hc.execute(req);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Requested URL: ");
		System.out.println(req);
		System.out.println("Beacon response: ");
		String r = parseEntity(response.getEntity());
		System.out.println(r);
		HashMap<String, String> hm = null;
		try {
			hm = parseJSON(r);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Parsed JSON HashMap: ");
		System.out.println(hm);
		
		return 0;
		
	}
	
	public boolean putValidationCode(String vc){
		
		
		HttpPut req = new HttpPut("http://api.downto.be/me/validation");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>(2);
		nvps.add(new BasicNameValuePair("validation_code", vc));
		try {
			req.setEntity(new UrlEncodedFormEntity(nvps));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumer_key, consumer_secret);
		consumer.setTokenWithSecret(prefs.getString("oat", ""), prefs.getString("oats", ""));
		try {
			consumer.sign(req);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		HttpClient hc = new DefaultHttpClient();
		HttpResponse response = null;
		try {
			 response =  hc.execute(req);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		System.out.println("Validation result:");
		System.out.println(response.getEntity());
		return true;
	}
	
	public boolean postBeacon(String message){
		String activity = "cuddle";
		
		HttpPost req = new HttpPost("http://api.downto.be/beacons");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>(2);
		nvps.add(new BasicNameValuePair("latitude", "" + lat));
		nvps.add(new BasicNameValuePair("longitude", "" + lon));
		nvps.add(new BasicNameValuePair("message", message));
		nvps.add(new BasicNameValuePair("activity", activity));
		try {
			req.setEntity(new UrlEncodedFormEntity(nvps));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumer_key, consumer_secret);
		consumer.setTokenWithSecret(prefs.getString("oat", ""), prefs.getString("oats", ""));
		try {
			consumer.sign(req);
		} catch (OAuthMessageSignerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpClient hc = new DefaultHttpClient();
		HttpResponse response = null;
		try {
			 response =  hc.execute(req);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Requested URL: ");
		System.out.println(req);
		System.out.println("Beacon response: ");
		String r = parseEntity(response.getEntity());
		System.out.println(r);
		HashMap<String, String> hm = null;
		try {
			hm = parseJSON(r);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Parsed JSON HashMap: ");
		System.out.println(hm);
		
		if(hm.containsKey("errors")){; 
			return false;
		}
		
		return true;
		
	}
	
	public boolean postUser(String number, String profile){
		
		HttpPost req = new HttpPost("http://api.downto.be/users");	
		List<NameValuePair> nvps = new ArrayList<NameValuePair>(2);
		nvps.add(new BasicNameValuePair("phone_number", number));
		nvps.add(new BasicNameValuePair("profile", profile));
		try {
			req.setEntity(new UrlEncodedFormEntity(nvps));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumer_key, consumer_secret);
		consumer.setTokenWithSecret(oauth_token, oauth_token_secret);
		try {
			consumer.sign(req);
		} catch (OAuthMessageSignerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpClient hc = new DefaultHttpClient();
		HttpResponse response = null;
		try {
			 response =  hc.execute(req);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Requested URL: ");
		System.out.println(req);
		System.out.println("Users Response: ");
		String r = parseEntity(response.getEntity());
		System.out.println(r);
		HashMap<String, String> hm = null;
		try {
			hm = parseJSON(r);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Parsed JSON from Posting User: ");
		System.out.println(hm);
		
		String oats = hm.get("oauth_token_secret");
		String oat = hm.get("oauth_token");
		
		if(oats != null && oat != null){
	    	final SharedPreferences.Editor editor;
			prefs = PreferenceManager.getDefaultSharedPreferences(c);
			editor = prefs.edit();
			editor.putString("oat", oat);
			editor.putString("oats", oats);
			editor.commit();
		}
		
		return true;
	}
	

	@SuppressWarnings("unused")
	private HashMap<String, String> parseJSON(final String s) throws JSONException{
		
		HashMap<String, String> hm = new HashMap<String, String>();
		JSONObject json = new JSONObject(s);
		JSONArray jn = json.names();
		JSONArray va = json.toJSONArray(jn);
		for(int i=0; i<va.length();i++){
			hm.put(jn.getString(i), va.getString(i));
		}
		return hm;
	}
	
	private String parseEntity(HttpEntity he){
		try {
			return convertStreamToString(he.getContent());
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Bad response! :(";
	}
    
	public static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
	
	public String authenticate() throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException{
		OAuthProvider provider = new CommonsHttpOAuthProvider(request_token_url, access_token_url, authorize_token_url);
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumer_key, consumer_secret);
		String authUrl = provider.retrieveRequestToken(consumer, OAuth.OUT_OF_BAND);
		
		oauth_token = consumer.getToken();
		oauth_token_secret = consumer.getTokenSecret();
		
		System.out.println(authUrl);
		System.out.println("OAuth Token: " + oauth_token);
		System.out.println("OAuth Secret: " + oauth_token_secret);
		return authUrl;
	}
	
}