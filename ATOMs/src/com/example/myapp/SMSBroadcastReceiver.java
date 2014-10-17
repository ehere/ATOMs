package com.example.myapp;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
public class SMSBroadcastReceiver extends BroadcastReceiver
{
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSBroadcastReceiver";
    @Override
    public void onReceive(final Context context, Intent intent)
    {
        Log.i(TAG, "Intent recieved: " + intent.getAction());
        if (intent.getAction().equals(SMS_RECEIVED))
        {
            Bundle bundle = intent.getExtras();
            if (bundle != null)
            {
                Object[] pdus = (Object[])bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++)
                {
                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                }
                if (messages.length > -1)
                {
                	Authenticate auth = new Authenticate(context);
                	String token = auth.getToken();
                    String sender = messages[0].getOriginatingAddress();
                    String message = messages[0].getMessageBody();

                    
                    ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("token", token));
                    params.add(new BasicNameValuePair("sender", sender));
                    params.add(new BasicNameValuePair("message", message));
                	HttpRequest request = new HttpRequest("https://www.diyby.me/android-connect/sms_receiver.php");
                	JSONObject result = request.get(params);
            		if(result == null) //no internet connection.
            		{
            			Toast.makeText(context, "No Internet Connection.", 7000).show();
            			//do something
            		}
            		else
            		{
            			
            			try 
            			{
            				int status = result.getInt("success");
            				if(status == 1)
            				{
                    			Toast.makeText(context, "Send sms succeed.", 7000).show();
                    			
            				}
            				else
            				{
            					Toast.makeText(context, "Send ms fail.", 7000).show();
            					//do something
            				}
            			} 
            			catch (JSONException e) 
            			{
            				e.printStackTrace();
                			Toast.makeText(context, "Send ms fail.", 7000).show();
                			//do something
            			}
            		}
                }
            }
        }
    }
}