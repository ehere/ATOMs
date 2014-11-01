package com.example.myapp;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.graphics.Color;
import android.graphics.PorterDuff;

@SuppressLint("ShowToast")
public class MainActivity extends Activity {
	private View mProgressView;
	private Background mAuthTask;
	private Button btnSMS, btnOrder, btnTransaction;
	private boolean oncreate = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);
        mProgressView = findViewById(R.id.login_progress);   
        mAuthTask = new Background();
        mAuthTask.execute((Void) null);
        //create database if not existed
        SQLiteDatabase mydatabase = openOrCreateDatabase("atoms",MODE_PRIVATE,null);
        mydatabase.execSQL(
        		"CREATE TABLE IF NOT EXISTS remain_sms("
        		+ "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
        		+ "sender TEXT,"
        		+ "message TEXT,"
        		+ "service_center TEXT,"
        		+ "time DATETIME);");

        mydatabase.execSQL(
        		"CREATE TABLE IF NOT EXISTS bank_name("
        		+ "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
        		+ "name TEXT,"
        		+ "number TEXT);"); 

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	HttpRequest request = new HttpRequest("https://www.diyby.me/android-connect/get_bank_detail.php");
    	JSONObject result = request.get(params);
		if(result != null) //no internet connection.
		{
			mydatabase.execSQL("DELETE FROM bank_name;");
	        Iterator<?> keys = result.keys();

	        while( keys.hasNext() ){
	            String bankname = (String)keys.next();
	            try {
					mydatabase.execSQL("INSERT INTO bank_name (name,number) VALUES ('"+bankname+"', '"+result.get(bankname)+"');");
				} catch (JSONException e) {
					e.printStackTrace();
				}
	        }
			
		}
    	
        btnSMS = (Button) findViewById(R.id.button1);
        btnOrder = (Button) findViewById(R.id.button2);
        btnTransaction = (Button) findViewById(R.id.button3);
        
        btnSMS.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
        btnSMS.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent newActivity = new Intent(MainActivity.this,SMSResendActivity.class);
				startActivity(newActivity);
				overridePendingTransition(R.animator.right_in, R.animator.left_out);
			}
		});
        
        btnOrder.getBackground().setColorFilter(Color.parseColor("#00D0FF"), PorterDuff.Mode.MULTIPLY);
        btnOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent newActivity = new Intent(MainActivity.this,OrderListActivity.class);
				startActivity(newActivity);
				overridePendingTransition(R.animator.right_in, R.animator.left_out);
			}
		});

        btnTransaction.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
        btnTransaction.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent newActivity = new Intent(MainActivity.this,LoginActivity.class);
				startActivity(newActivity);
				overridePendingTransition(R.animator.right_in, R.animator.left_out);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
				Intent newActivity = new Intent(MainActivity.this,LoginActivity.class);
				startActivity(newActivity);
				overridePendingTransition(R.animator.right_in, R.animator.left_out);
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
	public void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);
			

			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mProgressView.animate().setDuration(shortAnimTime)
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
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
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
			showProgress(false);
			if(!auth.isConnect())
			{
				Toast.makeText(getApplicationContext(), "No Internet Connection.", 7000).show();
			}
			else if(auth.isLogin())
			{
				Toast.makeText(getApplicationContext(), auth.getLastSubmit(), 7000).show();
				btnSMS.setVisibility(View.VISIBLE);
				btnOrder.setVisibility(View.VISIBLE);
				btnTransaction.setVisibility(View.VISIBLE);
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Login Fail.", 7000).show();				
				Intent newActivity = new Intent(MainActivity.this,LoginActivity.class);
				startActivity(newActivity);
				overridePendingTransition(R.animator.right_in, R.animator.left_out);
				finish();
			}

		}

	}
}
