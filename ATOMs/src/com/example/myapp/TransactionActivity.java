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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint({ "ShowToast", "SetJavaScriptEnabled" })
public class TransactionActivity extends Activity 
{
	private View mProgressView, transactionProgressView;
	private TextView transactionID, transactionAmount, transactionStatus, transactionURLView;
	private WebView myWebView;
	private LinearLayout rowview;
	private Background mAuthTask;
	private ListView lisView1;
	private DialogBackgroud resendTask;
	private ArrayList<HashMap<String, String>> MyArrList;
	private SpecialAdapter sAdap;
	private int row;
	private TextView error;
	private boolean oncreate = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_transaction);
		
		mProgressView = findViewById(R.id.login_progress);  
		error = (TextView) findViewById(R.id.error_message);
        error.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onResume();
			}
		});		
		
		
        mAuthTask = new Background();
        mAuthTask.execute((Void) null);
        
        myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        
        
        lisView1 = (ListView)findViewById(R.id.listView1); 
		lisView1.setTextFilterEnabled(true);	
		lisView1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				row = position;
				// When clicked, show a toast with the TextView text
				rowview = (LinearLayout) view; // get the parent layout view
				transactionID = (TextView) rowview.findViewById(R.id.ColOrderID);
				transactionAmount = (TextView) rowview.findViewById(R.id.ColAmount);
				transactionStatus = (TextView) rowview.findViewById(R.id.ColStatus);
				transactionProgressView = rowview.findViewById(R.id.order_progress);
				transactionURLView = (TextView) rowview.findViewById(R.id.ColURL);
				AlertDialog.Builder builder1 = new AlertDialog.Builder(TransactionActivity.this);
	            builder1.setMessage(
	            		Html.fromHtml("<b>Amount: </b>"+transactionAmount.getText()
	            				+"<br /><b>Status: </b>"+transactionStatus.getText()));
	            
	            builder1.setCancelable(true);
	            builder1.setTitle("transaction #"+transactionID.getText());
	            
	            final CharSequence[] items = {"Mark as Used", "Mark as Unused", "Delete", "Cancel"};

	            AlertDialog.Builder builder = new AlertDialog.Builder(TransactionActivity.this);
	            builder.setTitle("Make your selection");
	            builder.setItems(items, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int item) {
	                	if(item < 2 )
	                	{
	                		resendTask = new DialogBackgroud(item);
                        	resendTask.execute((Void) null);
	                	}
	                	else if(item == 2)
	                	{
	        	            AlertDialog.Builder builder1 = new AlertDialog.Builder(TransactionActivity.this);
	        	            builder1.setTitle("Are You Sure?");
	        	            builder1.setMessage("Are you sure to remove this transaction?");
	        	            builder1.setCancelable(true);
	        	            builder1.setPositiveButton("Yes",
	        	                    new DialogInterface.OnClickListener() {
	        	                        public void onClick(DialogInterface dialog, int id) {
	        	                            resendTask = new DialogBackgroud(2);
	        	                            resendTask.execute((Void) null);
	        	                        }
	        	                    });

	        	            builder1.setNegativeButton("Cancel",
	        	                    new DialogInterface.OnClickListener() {
	        	                        public void onClick(DialogInterface dialog, int id) {
	        	                        	dialog.cancel();
	        	                        }
	        	                    });
	        	            AlertDialog alert2 = builder1.create();
	        	            alert2.show();
	                	}
	                }
	            });
	            AlertDialog alert = builder.create();
	            alert.show();
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
		if(myWebView.canGoBack())
		{
			myWebView.goBack();
		}
		else if(myWebView.getVisibility() == View.VISIBLE)
		{
			setVisible(myWebView, false);
			setVisible(lisView1, true);
		}
		else
		{
			super.onBackPressed();
	    	overridePendingTransition(R.animator.left_in, R.animator.right_out);
	    	finish();
		}
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
			if(myWebView.canGoBack())
			{
				myWebView.goBack();
			}
			else if(myWebView.getVisibility() == View.VISIBLE)
			{
				setVisible(myWebView, false);
				setVisible(lisView1, true);
			}
			else
			{
				finish();
				overridePendingTransition(R.animator.left_in, R.animator.right_out);
			}
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
				Intent newActivity = new Intent(TransactionActivity.this,LoginActivity.class);
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
		        
		        FileManager tokenFile = new FileManager("token", getBaseContext());
		        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		        params.add(new BasicNameValuePair("token", tokenFile.read()));
		        
				MyArrList = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> map;
				
		        HttpRequest request = new HttpRequest("https://www.diyby.me/android-connect/get_user_transaction.php");
		    	JSONObject result = request.get(params);
		    	if(result == null) //no internet connection.
        		{
					error.setText("No Internet Connection.\n       Click to refresh.");
					setVisible(error, true);
        		}
        		else
        		{
        			try 
        			{
        				int status = result.getInt("success");
        				if(status == 1)
        				{
        					JSONArray transactions = result.getJSONArray("transaction");
        					for (int i = 0; i < transactions.length(); i++) {
        						JSONObject transaction = transactions.getJSONObject(i);
        						map = new HashMap<String, String>();
        						map.put("ID", transaction.getString("id"));
        				       	map.put("Amount", "ß" + transaction.getString("money"));
        				       	map.put("rawStatus", transaction.getString("status"));
        				       	map.put("URL", "");
        				       	if(transaction.getString("status").equals("0")){
        				       		//not paid
        				       		map.put("Status", "Unused");
        				       		map.put("Color", "#FFB45E");
        				       	}
        				       	else if(transaction.getString("status").equals("1")){	//not paid
        				       		map.put("Status", "Used");
        				       		map.put("Color", "#47FF6C");
        				       	}
        				       	MyArrList.add(map);
        					}
        				}
        				else
        				{
        					Toast.makeText(getBaseContext(), "Send ms fail 1.", 7000).show();
        				}
        			} 
        			catch (JSONException e) 
        			{
        				e.printStackTrace();
            			Toast.makeText(getBaseContext(), "Send ms fail 2.", 7000).show();
            			//do something
        			}
        		}
		        
		        sAdap = new SpecialAdapter(TransactionActivity.this, MyArrList, R.layout.activity_transactioncolumn,
		                new String[] {"ID", "Amount", "Status", "rawStatus", "URL", "Color"}, new int[] {R.id.ColOrderID, R.id.ColAmount, R.id.ColStatus, R.id.ColRaw, R.id.ColURL, R.id.ColColor});      
		        lisView1.setAdapter(sAdap);
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Login Fail.", 7000).show();				
				Intent newActivity = new Intent(TransactionActivity.this,LoginActivity.class);
				startActivity(newActivity);
				overridePendingTransition(R.animator.right_in, R.animator.left_out);
				finish();
			}

		}

	}
	public class DialogBackgroud extends AsyncTask<Void, Void, Boolean> {
		private Authenticate auth;
		private int mode;
		private JSONObject result;
		private String message;
		DialogBackgroud(int mode) {
			this.mode = mode;
			setVisible(transactionProgressView, true);
			setVisible(transactionID, false);
			setVisible(transactionAmount, false);
			setVisible(transactionStatus, false);
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
				if(mode == 0)//mark_as_used mode
				{     
                	ArrayList<NameValuePair> updateParams = new ArrayList<NameValuePair>();
                	updateParams.add(new BasicNameValuePair("token", auth.getToken()));
                	updateParams.add(new BasicNameValuePair("mark_as_used", "1"));
                	updateParams.add(new BasicNameValuePair("id", (String) transactionID.getText()));
                	
                	HttpRequest request = new HttpRequest("https://www.diyby.me/android-connect/set_user_transaction.php");
    		    	result = request.get(updateParams);
				}
				else if(mode == 1)//mark_as_unused mode
				{    		        
                	ArrayList<NameValuePair> updateParams = new ArrayList<NameValuePair>();
                	updateParams.add(new BasicNameValuePair("token", auth.getToken()));
                	updateParams.add(new BasicNameValuePair("mark_as_unused", "1"));
                	updateParams.add(new BasicNameValuePair("id", (String) transactionID.getText()));
                	
                	HttpRequest request = new HttpRequest("https://www.diyby.me/android-connect/set_user_transaction.php");
    		    	result = request.get(updateParams);
				}
				else if(mode == 2)//delete mode
				{
                	ArrayList<NameValuePair> updateParams = new ArrayList<NameValuePair>();
                	updateParams.add(new BasicNameValuePair("token", auth.getToken()));
                	updateParams.add(new BasicNameValuePair("delete", "1"));
                	updateParams.add(new BasicNameValuePair("id", (String) transactionID.getText()));
                	
                	HttpRequest request = new HttpRequest("https://www.diyby.me/android-connect/set_user_transaction.php");
    		    	result = request.get(updateParams);
				}
            	if(result == null) //no internet connection.
        		{
            		return false;
        		}
        		else
        		{	
        			try 
        			{
        				int status = result.getInt("success");
        				message = result.getString("message");
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
			setVisible(transactionProgressView, false);
			if(!success)
			{
				if(!auth.isConnect())
				{
					error.setText("No Internet Connection.\n       Click to refresh.");
					setVisible(error, true);
					setVisible(transactionID, true);
					setVisible(transactionAmount, true);
					setVisible(transactionStatus, true);
				}
				else if(auth.isLogin())
				{
					Toast.makeText(getApplicationContext(), message, 7000).show();
					setVisible(transactionID, true);
					setVisible(transactionAmount, true);
					setVisible(transactionStatus, true);
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Login Fail.", 7000).show();				
					Intent newActivity = new Intent(TransactionActivity.this,LoginActivity.class);
					startActivity(newActivity);
					overridePendingTransition(R.animator.right_in, R.animator.left_out);
					finish();
				}
			}
			else
			{
				if(mode == 0)
				{
					transactionStatus.setText("Used");
					rowview.setBackgroundColor(Color.parseColor("#47FF6C"));
					setVisible(transactionID, true);
					setVisible(transactionAmount, true);
					setVisible(transactionStatus, true);
				}
				else if(mode == 1)
				{
					transactionStatus.setText("Unused");
					rowview.setBackgroundColor(Color.parseColor("#FFB45E"));
					setVisible(transactionID, true);
					setVisible(transactionAmount, true);
					setVisible(transactionStatus, true);
				}
				else if(mode == 2)
				{
					MyArrList.remove(row);
	                sAdap.notifyDataSetChanged();
				}
				Toast.makeText(getApplicationContext(), message, 7000).show();
			}

		}

	}
}
