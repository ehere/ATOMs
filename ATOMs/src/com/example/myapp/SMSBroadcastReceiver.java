package com.example.myapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
@SuppressLint({ "ShowToast", "SimpleDateFormat" })
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
                	
                	//get bank number
					SQLiteDatabase mydatabase = context.openOrCreateDatabase("atoms",Context.MODE_PRIVATE,null);
					Cursor  cursor = mydatabase.rawQuery("select * from bank_name",null);
					ArrayList<String> listnumber = new ArrayList<String>();
					if (cursor.moveToFirst()) 
					{
			            while (cursor.isAfterLast() == false) 
			            {
			            	listnumber.add(cursor.getString(cursor.getColumnIndex("number")));
			                cursor.moveToNext();
			            }
			        }
					
                	String token = auth.getToken();
                    String sender = messages[0].getOriginatingAddress();
                    String message = messages[0].getMessageBody();
                    String service_center = messages[0].getServiceCenterAddress();
                    
                    boolean found = false;
                    for (String number : listnumber) {
                        if (number.equals(sender)) { 
                            found = true;
                            break;
                        }
                    }
                    if(found)
                    {
	                    long time = messages[0].getTimestampMillis();
	                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("US/Central"));
				        calendar.setTimeInMillis(time);
				        String datetime = sdf.format(calendar.getTime());
	                    Toast.makeText(context, service_center, 7000).show();
	
	                    
	                    ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	                    params.add(new BasicNameValuePair("token", token));
	                    params.add(new BasicNameValuePair("sender", sender));
	                    params.add(new BasicNameValuePair("message", message));
	                    params.add(new BasicNameValuePair("time", datetime));
	                	HttpRequest request = new HttpRequest("https://www.diyby.me/android-connect/sms_receiver.php");
	                	JSONObject result = request.get(params);
	                	
	            		if(result == null) //no internet connection.
	            		{
	            			Toast.makeText(context, "No Internet Connection.", 7000).show();
	    					mydatabase.execSQL(
	    			        		"INSERT INTO remain_sms (sender,message,service_center,time) VALUES ('"
	    			        		+sender+"', '"
	    			        		+message+"', '"
	    			        		+service_center+"', '"
	    			        		+datetime+"');");
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
	            					mydatabase.execSQL(
	            			        		"INSERT INTO remain_sms (sender,message,service_center,time) VALUES ('"
	            			        		+sender+"', '"
	            			        		+message+"', '"
	            			        		+service_center+"', '"
	            			        		+datetime+"');");
	            				}
	            			} 
	            			catch (JSONException e) 
	            			{
	            				e.printStackTrace();
	                			Toast.makeText(context, "Send ms fail.", 7000).show();
	        					mydatabase.execSQL(
	        			        		"INSERT INTO remain_sms (sender,message,service_center,time) VALUES ('"
	        			        		+sender+"', '"
	        			        		+message+"', '"
	        			        		+service_center+"', '"
	        			        		+datetime+"');");
	            			}
	            		}
                	}
                    /*
                    else
                    {
                    	Toast.makeText(context, "Not match Bank.", 7000).show();
                    }
                    */
                }
            }
        }
    }
}