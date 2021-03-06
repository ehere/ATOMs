package com.example.myapp;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;




import android.content.Context;


public class Authenticate {
	private String token = "";
	private Context context;
	public Authenticate(final Context context)
    {	
		this.context = context;
    	FileManager tokenFile = new FileManager("token",context);
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", tokenFile.read()));
    	HttpRequest request = new HttpRequest("https://www.diyby.me/android-connect/login.php");
    	JSONObject result = request.get(params);
                
		if(result == null)
		{
			this.token = null;
		}
		else
		{
			
			try {
				int status = 0;
				status = result.getInt("success");
				this.token = tokenFile.read();
				if(status == 0)
				{
					this.token = "";
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			tokenFile.write(token);
		}
    }
    public Authenticate(final String username, final String password, final Context context)
    {
    	this.context = context;
    	FileManager tokenFile = new FileManager("token",context);
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
    	HttpRequest request = new HttpRequest("https://www.diyby.me/android-connect/login.php");
    	JSONObject result = request.get(params);
                
		if(result == null)
		{
			this.token = null;
		}
		else
		{
			
			try {
				int status = 0;
				status = result.getInt("success");
				if(status == 1)
				{
					this.token = result.getString("token");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			tokenFile.write(token);
		}
    }
    public boolean isConnect()
    {
    	if(this.token == null)
    	{
    		return false;
    	}
    	return true;
    }
    public boolean isLogin()
    {
    	if(this.token.isEmpty())
    	{
    		return false;
    	}
    	return true;
    }
    public String getToken()
    {
    	return this.token;
    }
    public boolean logout()
    {
    	if(isLogin())
    	{
    		FileManager tokenFile = new FileManager("token",context);
    		tokenFile.write("");
    		this.token = "";
    		return true;
    	}
    	return false;
    }
}
