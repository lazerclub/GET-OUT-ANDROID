package com.lazerclub.getout;


import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ActivityListActivity extends Activity{
    
    ActivityAdapter adapter;
    ListView lv;
    Context c;
    ReqUtils ru;
    Handler mHandler;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.lister);    	

        final ArrayList<HashMap<String,String>> al = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> hm = new HashMap<String, String>();
        
        hm.put("activity", "cuddle");
        hm.put("id", "id");
        hm.put("description", "22/f looking for snugs");
        hm.put("distance", "1.1 miles");
        hm.put("user", "Rich");
        
        HashMap<String, String> hm2 = new HashMap<String, String>();
        
        hm2.put("activity", "asdf");
        hm2.put("id", "asdf");
        hm2.put("description", "asdf");
        hm2.put("distance", "asdf");
        hm.put("user", "Rich");
        
        al.add(hm);
        al.add(hm);
        al.add(hm);
        al.add(hm);
        al.add(hm);
        al.add(hm);
        al.add(hm);
        al.add(hm);
        al.add(hm);
        al.add(hm);
        al.add(hm);
        al.add(hm);
        al.add(hm);
        al.add(hm);
        al.add(hm);
        al.add(hm2);
        al.add(hm2);
        al.add(hm);
        al.add(hm2);
       
        //Setup the adapter views;
        adapter = new ActivityAdapter();
        adapter.setList(al);
        adapter.setContext(getBaseContext());
        adapter.setParent(this);
        lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(adapter);
        
        c=this;
        mHandler = new Handler();
        
        ru = new ReqUtils(c);
        
        mHandler.postDelayed(new Runnable() {

            public void run() {
                ru.getBeacons();
                
            }}, 10000);
        
        
        final ActivityListActivity c = ActivityListActivity.this;
        lv.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                HashMap<String, String> shmap = al.get(0);
                Intent i = new Intent(c, DetailContactActivity.class);
                i.putExtra("title", shmap.get("activity"));
                i.putExtra("desc", shmap.get("description"));
                i.putExtra("user", shmap.get("user"));
                startActivity(i);
                }
                
            });
        
    }

}
