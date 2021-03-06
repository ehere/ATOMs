package com.example.myapp;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.drawable.StateListDrawable;

@SuppressLint("ShowToast")
public class MainActivity extends Activity {
	private View mProgressView;
	private Background mAuthTask;
	private TextView error;
	private Button btnSMS, btnOrder, btnTransaction;
	private boolean oncreate = true;
	private SQLiteDatabase mydatabase;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);
        mProgressView = findViewById(R.id.login_progress);
        error = (TextView) findViewById(R.id.error_message);
        btnSMS = (Button) findViewById(R.id.button1);
        btnOrder = (Button) findViewById(R.id.button2);
        btnTransaction = (Button) findViewById(R.id.button3);
        
        mAuthTask = new Background();
        mAuthTask.execute((Void) null);
        //create database if not existed
        mydatabase = openOrCreateDatabase("atoms",MODE_PRIVATE,null);
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
		

        
        StateListDrawable slDraw = new StateListDrawable(); 
        slDraw.addState(new int[] {android.R.attr.state_focused},  getResources().getDrawable(R.drawable.button_alert_clicked));
        slDraw.addState(new int[] {android.R.attr.state_selected},  getResources().getDrawable(R.drawable.button_alert_clicked));   
        slDraw.addState(new int[] {android.R.attr.state_pressed}, getResources().getDrawable(R.drawable.button_alert_clicked)); 
        slDraw.addState(new int[] {}, getResources().getDrawable(R.drawable.button_alert)); 
    	btnSMS.setBackground(slDraw);
    	
    	slDraw = new StateListDrawable(); 
        slDraw.addState(new int[] {android.R.attr.state_focused},  getResources().getDrawable(R.drawable.button_select));
        slDraw.addState(new int[] {android.R.attr.state_selected},  getResources().getDrawable(R.drawable.button_select));   
        slDraw.addState(new int[] {android.R.attr.state_pressed}, getResources().getDrawable(R.drawable.button_select)); 
        slDraw.addState(new int[] {}, getResources().getDrawable(R.drawable.button_normal));     	
    	btnOrder.setBackground(slDraw);
    	
    	slDraw = new StateListDrawable(); 
        slDraw.addState(new int[] {android.R.attr.state_focused},  getResources().getDrawable(R.drawable.button_select));
        slDraw.addState(new int[] {android.R.attr.state_selected},  getResources().getDrawable(R.drawable.button_select));   
        slDraw.addState(new int[] {android.R.attr.state_pressed}, getResources().getDrawable(R.drawable.button_select)); 
        slDraw.addState(new int[] {}, getResources().getDrawable(R.drawable.button_normal)); 
    	btnTransaction.setBackground(slDraw);
        
        btnSMS.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent newActivity = new Intent(MainActivity.this,SMSResendActivity.class);
				startActivity(newActivity);
				overridePendingTransition(R.animator.right_in, R.animator.left_out);
			}
		});


        btnOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent newActivity = new Intent(MainActivity.this,OrderListActivity.class);
				startActivity(newActivity);
				overridePendingTransition(R.animator.right_in, R.animator.left_out);
			}
		});

        
        btnTransaction.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent newActivity = new Intent(MainActivity.this,TransactionActivity.class);
				startActivity(newActivity);
				overridePendingTransition(R.animator.right_in, R.animator.left_out);
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
		if(id == R.id.action_logout)
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
			
			if(!auth.isConnect())
			{
				error.setText("No Internet Connection.\n       Click to refresh.");
				setVisible(error, true);
			}
			else if(auth.isLogin())
			{
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
				SQLiteDatabase mydatabase = openOrCreateDatabase("atoms",MODE_PRIVATE,null);
				Cursor mCount= mydatabase.rawQuery("select count(*) from remain_sms", null);
				mCount.moveToFirst();
				int count= mCount.getInt(0);
				mCount.close();
				if(count > 0 )
				{
					btnSMS.setText(count+" "+btnSMS.getText());
					setVisible(btnSMS, true);
				}
				setVisible(btnOrder, true);
				setVisible(btnTransaction, true);
				
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Login Fail.", 7000).show();				
				Intent newActivity = new Intent(MainActivity.this,LoginActivity.class);
				startActivity(newActivity);
				overridePendingTransition(R.animator.right_in, R.animator.left_out);
				finish();
			}
			setVisible(mProgressView, false);

		}

	}
}
