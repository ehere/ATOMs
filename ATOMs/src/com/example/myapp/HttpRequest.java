package com.example.myapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

//import android.content.Context;
import android.util.Log;

public class HttpRequest {
	//private Context context;
	private String URL;
	private JSONObject result = null;
	public HttpRequest(String URL)
	{
		//this.context = context;
		this.URL = URL;
	}
    public JSONObject get(final ArrayList<NameValuePair> params)
    {
    	Thread thread = new Thread(new Runnable()
        {
            public void run()
            {
            	InputStream steam = null;
                try
                {	
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(URL);
                    httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                    HttpResponse response = httpclient.execute(httppost);
	                HttpEntity entity = response.getEntity();
	                steam = entity.getContent();
                }
                catch(Exception e)
                {
                    Log.d("log_err", "Error in http connection " + e.toString());
                }
                
                //convert response
                try 
                {
                    BufferedReader reader = new BufferedReader(new InputStreamReader( steam, "utf-8"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) 
                    {
                        sb.append(line + "\n");
                    }
                    steam.close();
                    //JSONParser jParser = new JSONParser();

                    String tresult = sb.toString();
                    result = new JSONObject(tresult);
                    
                } 
                catch (Exception e)
                {
                    Log.v("log", "Error converting result " + e.toString());
                }
            }
            
        }
        );
    	thread.start(); // spawn thread
        try 
        {
        	thread.join();
        	return result;
        } 
        catch (InterruptedException e) 
        {
        	e.printStackTrace();
        	return result;
        }
    }
}
