package com.example.myapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ListView;
import android.widget.SimpleAdapter;

import android.widget.Toast;

@SuppressLint("ShowToast")
public class OrderListActivity extends Activity 
{
	private View mProgressView;
	private Background mAuthTask;
	private ListView lisView1;
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
				Intent newActivity = new Intent(OrderListActivity.this,LoginActivity.class);
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
		        // listView1
		        lisView1 = (ListView)findViewById(R.id.listView1); 
		        lisView1.setVisibility(View.VISIBLE);
		        
		        FileManager tokenFile = new FileManager("token", getBaseContext());
		        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		        params.add(new BasicNameValuePair("token", tokenFile.read()));
		        
				ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> map;
				
		        HttpRequest request = new HttpRequest("https://www.diyby.me/android-connect/get_user_order.php");
		    	JSONObject result = request.get(params);
		    	if(result == null) //no internet connection.
        		{
        			Toast.makeText(getBaseContext(), "No Internet Connection.", 7000).show();
        			//do something
        		}
        		else
        		{
        			
        			try 
        			{
        				int status = result.getInt("success");
        				if(status == 1)
        				{
        					JSONArray orders = result.getJSONArray("order");
        					for (int i = 0; i < orders.length(); i++) {
        						JSONObject order = orders.getJSONObject(i);
        						map = new HashMap<String, String>();
        						map.put("ID", order.getString("id"));
        				       	map.put("Amount", "�" + order.getString("money"));
        				       	if(order.getString("status").equals("0")){
        				       		//not paid
        				       		map.put("Status", "Not Paid!");
        				       	}
        				       	else{
        				       		map.put("Status", "Paid!");
        				       	}
        				       	MyArrList.add(map);
        					}
        				}
        				else
        				{
        					Toast.makeText(getBaseContext(), "Send ms fail.", 7000).show();
        				}
        			} 
        			catch (JSONException e) 
        			{
        				e.printStackTrace();
            			Toast.makeText(getBaseContext(), "Send ms fail.", 7000).show();
            			//do something
        			}
        		}
		        
		        SimpleAdapter sAdap;
		        sAdap = new SimpleAdapter(OrderListActivity.this, MyArrList, R.layout.activity_orderlistcolumn,
		                new String[] {"ID", "Amount", "Status"}, new int[] {R.id.ColOrderID, R.id.ColAmount, R.id.ColStatus});      
		        lisView1.setAdapter(sAdap);
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Login Fail.", 7000).show();				
				Intent newActivity = new Intent(OrderListActivity.this,LoginActivity.class);
				startActivity(newActivity);
				overridePendingTransition(R.animator.right_in, R.animator.left_out);
				finish();
			}

		}

	}
}
