package com.example.myapp;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class SMSResendActivity extends Activity 
{
	private View mProgressView, resendProgressView;
	private TextView value, tvBankName, tvMessage, tvTime;
	private Background mAuthTask;
	private DialogBackgroud resendTask;
	private ListView lisView1;
	private SimpleAdapter sAdap;
	private ArrayList<HashMap<String, String>> MyArrList;
	private int row;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_smsresend);
		
		mProgressView = findViewById(R.id.login_progress);   
        mAuthTask = new Background();
        mAuthTask.execute((Void) null);
        
           

        
        lisView1 = (ListView)findViewById(R.id.listView1); 
		lisView1.setTextFilterEnabled(true);
		lisView1.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(final AdapterView<?> parent, final View view,
					final int position, long id) {
				// When clicked, show a toast with the TextView text
				LinearLayout ll = (LinearLayout) view; // get the parent layout view
				value = (TextView) ll.findViewById(R.id.Value);
				tvBankName = (TextView) ll.findViewById(R.id.ColBankName);
				tvMessage = (TextView) ll.findViewById(R.id.ColMessage);
				tvTime = (TextView) ll.findViewById(R.id.ColTime);
				resendProgressView = ll.findViewById(R.id.resend_progress);
				row = position;
				AlertDialog.Builder builder1 = new AlertDialog.Builder(SMSResendActivity.this);
	            builder1.setMessage(
	            		"id: "+value.getText()
	            		+"\nส่งข้อความนี้ขึ้น Server อีกครั้งหรือนำออกจากรายการ\n"
	            		+ "\nส่งจาก: "+tvBankName.getText()
	            		+ "\nข้อความ: "+tvMessage.getText()
	            		+ "\nส่งเมื่อ: "+tvTime.getText());
	            builder1.setCancelable(true);
	            builder1.setPositiveButton("ส่งใหม่",
	                    new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int id) {
	                            resendTask = new DialogBackgroud(0);
	                            resendTask.execute((Void) null);

	                        }
	                    });

	            builder1.setNeutralButton("ลบออก",
	                    new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int id) {
	                        	resendTask = new DialogBackgroud(1);
		                        resendTask.execute((Void) null);
	                        }
	                    });

	            builder1.setNegativeButton("ยกเลิก",
	                    new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int id) {
	                            dialog.cancel();

	                        }
	            });
	            AlertDialog alert11 = builder1.create();
	            alert11.show();
			}
		});


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.animator.left_in, R.animator.right_out);
	    finish();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) 
		{
			finish();
			overridePendingTransition(R.animator.left_in, R.animator.right_out);
	        return true;
	    }
		if (id == R.id.action_settings) 
		{
			return true;
		}
		else if(id == R.id.action_logout)
		{
			Authenticate auth = new Authenticate(this);
			if(!auth.isConnect())
			{
				Toast.makeText(getApplicationContext(), "No Internet Connection.", 7000).show();
				return false;
			}
			else if(auth.logout())
			{	
				Intent newActivity = new Intent(SMSResendActivity.this,LoginActivity.class);
				startActivity(newActivity);
				overridePendingTransition(R.animator.left_in, R.animator.right_out);
				finish();
				return true;
			}
			else
			{
				return false;
			}
			
		}
		return super.onOptionsItemSelected(item);
	}
	public void showProgress(View ProgressView,final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);
			

			ProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			ProgressView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mProgressView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			ProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
		}
	}
	
	public class Background extends AsyncTask<Void, Void, Boolean> {
		private Authenticate auth;
		/*
		Background(Context context) {
			this.context = context;
		}
		*/

		@Override
		protected Boolean doInBackground(Void... params) {

			auth = new Authenticate(getApplicationContext());

			return true;
			

			// TODO: register the new account here.
			//return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(mProgressView, false);
			if(!auth.isConnect())
			{
				Toast.makeText(getApplicationContext(), "No Internet Connection.", 7000).show();
			}
			else if(auth.isLogin())
			{
		        // listView1
		        lisView1 = (ListView)findViewById(R.id.listView1); 
		        lisView1.setVisibility(View.VISIBLE);
				MyArrList = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> map;
				SQLiteDatabase mydatabase = openOrCreateDatabase("atoms",MODE_PRIVATE,null);
				Cursor  cursor = mydatabase.rawQuery("select * from remain_sms ORDER BY id desc",null);
				if (cursor .moveToFirst()) 
				{

		            while (cursor.isAfterLast() == false) {
		            	map = new HashMap<String, String>();
		            	map.put("Value", Integer.toString(cursor.getInt(cursor.getColumnIndex("id"))));
				       	map.put("Bank", cursor.getString(cursor.getColumnIndex("sender")));
						map.put("Message", cursor.getString(cursor.getColumnIndex("message")));
						map.put("Time", cursor.getString(cursor.getColumnIndex("time")));
						
						//map.put("service_center", cursor.getString(cursor.getColumnIndex("service_center")));
						MyArrList.add(map);
		                cursor.moveToNext();
		            }
		        }


		       
		        
		        sAdap = new SimpleAdapter(SMSResendActivity.this, MyArrList, R.layout.activity_column,
		                new String[] {"Value", "Bank", "Message", "Time"}, new int[] {R.id.Value, R.id.ColBankName, R.id.ColMessage, R.id.ColTime});      
		        lisView1.setAdapter(sAdap);
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Login Fail.", 7000).show();				
				Intent newActivity = new Intent(SMSResendActivity.this,LoginActivity.class);
				startActivity(newActivity);
				overridePendingTransition(R.animator.right_in, R.animator.left_out);
				finish();
			}

		}

	}
	
	public class DialogBackgroud extends AsyncTask<Void, Void, Boolean> {
		private Authenticate auth;
		private int mode;
		DialogBackgroud(int mode) {
			this.mode = mode;
			showProgress(resendProgressView, true);
			showProgress(tvBankName, false);
			showProgress(tvMessage, false);
			showProgress(tvTime, false);
		}
		

		@Override
		protected Boolean doInBackground(Void... params) {
			

			auth = new Authenticate(getApplicationContext());
			if(!auth.isConnect())
			{
				return false;
			}
			else if(auth.isLogin())
			{
				if(mode == 1)
				{
					return true;
				}
				ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("token", auth.getToken()));
				list.add(new BasicNameValuePair("sender", (String) tvBankName.getText()));
				list.add(new BasicNameValuePair("message", (String) tvMessage.getText()));
				list.add(new BasicNameValuePair("time", (String) tvTime.getText()));
            	HttpRequest request = new HttpRequest("https://www.diyby.me/android-connect/sms_receiver.php");
            	JSONObject result = request.get(list);
            	if(result == null) //no internet connection.
        		{
            		return false;
        		}
        		else
        		{
        			
        			try 
        			{
        				int status = result.getInt("success");
        				if(status == 1)
        				{
        					return true;
                			
        				}
        				else
        				{
        					return false;
        					
        				}
        			} 
        			catch (JSONException e) 
        			{
        				e.printStackTrace();
        				return false;
        			}
        		}

			}
			else
			{
				return false;
			}
			

			// TODO: register the new account here.
			//return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			resendTask = null;
			if(!success)
			{
				if(!auth.isConnect())
				{
					Toast.makeText(getApplicationContext(), "No Internet Connection.", 7000).show();
					showProgress(resendProgressView, false);
					showProgress(tvBankName, true);
					showProgress(tvMessage, true);
					showProgress(tvTime, true);
				}
				else if(auth.isLogin())
				{
					Toast.makeText(getApplicationContext(), "Something Wrong.", 7000).show();
					showProgress(resendProgressView, false);
					showProgress(tvBankName, true);
					showProgress(tvMessage, true);
					showProgress(tvTime, true);
	
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Login Fail.", 7000).show();				
					Intent newActivity = new Intent(SMSResendActivity.this,LoginActivity.class);
					startActivity(newActivity);
					overridePendingTransition(R.animator.right_in, R.animator.left_out);
					finish();
				}
			}
			else
			{
				SQLiteDatabase mydatabase = openOrCreateDatabase("atoms",MODE_PRIVATE,null);
				mydatabase.execSQL("DELETE FROM `remain_sms` WHERE id = "+value.getText()+";");
				showProgress(resendProgressView, false);
                MyArrList.remove(row);
                sAdap.notifyDataSetChanged();
			}

		}

	}
}
