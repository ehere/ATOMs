package com.example.myapp;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.myapp.TransactionActivity.DialogBackgroud;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.View.OnClickListener;
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
	private TextView error;
	private ArrayList<HashMap<String, String>> MyArrList;
	private int row;
	private boolean oncreate = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_smsresend);
		
		mProgressView = findViewById(R.id.login_progress); 
		error = (TextView) findViewById(R.id.error_message);
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
				builder1.setTitle("Resend Message");
	            builder1.setMessage(
	            		"From: \t"+tvBankName.getText()
	            		+ "\nMessage: \t"+tvMessage.getText()
	            		+ "\nTime: \t"+tvTime.getText());
	            builder1.setCancelable(true);
	            builder1.setPositiveButton("Resend",
	                    new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int id) {
	                            resendTask = new DialogBackgroud(0);
	                            resendTask.execute((Void) null);

	                        }
	                    });

	            builder1.setNeutralButton("Delete",
	                    new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int id) {
		        	            AlertDialog.Builder builder2 = new AlertDialog.Builder(SMSResendActivity.this);
		        	            builder2.setTitle("Are You Sure?");
		        	            builder2.setMessage("Are you sure to remove this message from unsend list?");
		        	            builder2.setCancelable(true);
		        	            builder2.setPositiveButton("Yes",
		        	                    new DialogInterface.OnClickListener() {
		        	                        public void onClick(DialogInterface dialog, int id) {
		        	                        	resendTask = new DialogBackgroud(1);
		        		                        resendTask.execute((Void) null);
		        	                        }
		        	                    });

		        	            builder2.setNegativeButton("Cancel",
		        	                    new DialogInterface.OnClickListener() {
		        	                        public void onClick(DialogInterface dialog, int id) {
		        	                        	dialog.cancel();
		        	                        }
		        	                    });
		        	            AlertDialog alert = builder2.create();
		        	            alert.show();

	                        }
	                    });

	            builder1.setNegativeButton("Cancel",
	                    new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int id) {
	                            dialog.cancel();

	                        }
	            });
	            AlertDialog alert11 = builder1.create();
	            alert11.show();
			}
		});
        error.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onResume();
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
	protected void onResume() {

		   super.onResume();
		   if(!oncreate)
		   {
			   this.onCreate(null);
		   }
		   else
		   {
			   oncreate = false;
		   }
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
	public void setVisible(final View view,final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);
			

			view.setVisibility(show ? View.VISIBLE : View.GONE);
			view.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							view.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			view.setVisibility(show ? View.VISIBLE : View.GONE);
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
			setVisible(mProgressView, false);
			if(!auth.isConnect())
			{
				error.setText("No Internet Connection.\n       Click to refresh.");
				setVisible(error, true);
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
		            while (cursor.isAfterLast() == false) 
		            {
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
			setVisible(resendProgressView, true);
			setVisible(tvBankName, false);
			setVisible(tvMessage, false);
			setVisible(tvTime, false);
		}
		

		@Override
		protected Boolean doInBackground(Void... params) 
		{
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
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			resendTask = null;
			if(!success)
			{
				if(!auth.isConnect())
				{
					error.setText("No Internet Connection.\n       Click to refresh.");
					setVisible(error, true);
					setVisible(resendProgressView, false);
					setVisible(tvBankName, true);
					setVisible(tvMessage, true);
					setVisible(tvTime, true);
				}
				else if(auth.isLogin())
				{
					Toast.makeText(getApplicationContext(), "Something Wrong.", 7000).show();
					setVisible(resendProgressView, false);
					setVisible(tvBankName, true);
					setVisible(tvMessage, true);
					setVisible(tvTime, true);
	
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
				setVisible(resendProgressView, false);
                MyArrList.remove(row);
                sAdap.notifyDataSetChanged();
			}

		}

	}
}
