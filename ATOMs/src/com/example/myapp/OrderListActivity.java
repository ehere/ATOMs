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
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.view.KeyEvent;

@SuppressLint({ "ShowToast", "SetJavaScriptEnabled" })
public class OrderListActivity extends Activity 
{
	private View mProgressView, orderProgressView;
	private TextView orderID, orderAmount, orderStatus, orderURLView, error;
	private EditText editSkipto;
	private Button btnBack, btnForword;
	private WebView myWebView;
	private LinearLayout rowview;
	private Background mAuthTask;
	private ListView lisView1;
	private DialogBackgroud resendTask;
	private ArrayList<HashMap<String, String>> MyArrList;
	private SpecialAdapter sAdap;
	private int row, page = 1, totalpage;
	private boolean oncreate = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_orderlist);
		btnBack = (Button) findViewById(R.id.button_back);
		btnForword = (Button) findViewById(R.id.button_forward);		
		mProgressView = findViewById(R.id.login_progress);  
		error = (TextView) findViewById(R.id.error_message);
        error.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onResume();
			}
		});		
        editSkipto = (EditText) findViewById(R.id.editTextSkipto);		
        editSkipto.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
            	if(!editSkipto.getText().toString().equals("") && Integer.parseInt(editSkipto.getText().toString()) <= totalpage && Integer.parseInt(editSkipto.getText().toString()) > 0)
            	{
	                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) 
	                {
	                	page = Integer.parseInt(editSkipto.getText().toString());
	                	onResume();
	                	return true;
	                }
            	}
            	else if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
            	{
            		Toast.makeText(getApplicationContext(), "Max page is "+totalpage, 7000).show();
            	}
                return false;
            }
        });
        mAuthTask = new Background();
        mAuthTask.execute((Void) null);
        
        myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        
        StateListDrawable slDraw = new StateListDrawable(); 
        slDraw.addState(new int[] {android.R.attr.state_focused},  getResources().getDrawable(R.drawable.button_paginate_select));
        slDraw.addState(new int[] {android.R.attr.state_selected},  getResources().getDrawable(R.drawable.button_paginate_select));   
        slDraw.addState(new int[] {android.R.attr.state_pressed}, getResources().getDrawable(R.drawable.button_paginate_select)); 
        slDraw.addState(new int[] {}, getResources().getDrawable(R.drawable.button_paginate_normal)); 
        btnBack.setBackground(slDraw);
    	
    	slDraw = new StateListDrawable(); 
        slDraw.addState(new int[] {android.R.attr.state_focused},  getResources().getDrawable(R.drawable.button_paginate_select));
        slDraw.addState(new int[] {android.R.attr.state_selected},  getResources().getDrawable(R.drawable.button_paginate_select));   
        slDraw.addState(new int[] {android.R.attr.state_pressed}, getResources().getDrawable(R.drawable.button_paginate_select)); 
        slDraw.addState(new int[] {}, getResources().getDrawable(R.drawable.button_paginate_normal));     	
        btnForword.setBackground(slDraw); 

        btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(page > 1)
				{
					page = page -1;
					onResume();
				}
			}
		});
        btnForword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(page != totalpage)
				{
					page = page + 1;
					onResume();
				}
			}
		});        
        lisView1 = (ListView)findViewById(R.id.listView1); 
		lisView1.setTextFilterEnabled(true);	
		lisView1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				row = position;
				rowview = (LinearLayout) view; // get the parent layout view
				orderID = (TextView) rowview.findViewById(R.id.ColOrderID);
				orderAmount = (TextView) rowview.findViewById(R.id.ColAmount);
				orderStatus = (TextView) rowview.findViewById(R.id.ColStatus);
				orderProgressView = rowview.findViewById(R.id.order_progress);
				orderURLView = (TextView) rowview.findViewById(R.id.ColURL);
	            
	            final CharSequence[] items = {"Open Invoice", "Mark as Paid", "Mark as Not Paid", "Mark as Shipped", "Delete", "Cancel"};

	            AlertDialog.Builder builder = new AlertDialog.Builder(OrderListActivity.this);
	            builder.setTitle("Order #"+orderID.getText());
	            builder.setCancelable(true);
	            builder.setItems(items, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int item) {
	                	if(item == 0)
	                	{
	                		setVisible(lisView1, false);
	                		setVisible(myWebView, true);
	                		myWebView.loadUrl((String) orderURLView.getText());
	                		myWebView.setWebViewClient(new WebViewClient() {
	                	        @Override
	                	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	                	            view.loadUrl(url);
	                	            return false;
	                	        }
	                	    });
	                	}
	                	else if(item < 4 )
	                	{
	                		resendTask = new DialogBackgroud(item);
                        	resendTask.execute((Void) null);
	                	}
	                	else if(item == 4)
	                	{
	        	            AlertDialog.Builder builder1 = new AlertDialog.Builder(OrderListActivity.this);
	        	            builder1.setTitle("Are You Sure?");
	        	            builder1.setMessage("Are you sure to remove this order?");
	        	            builder1.setCancelable(true);
	        	            builder1.setPositiveButton("Yes",
	        	                    new DialogInterface.OnClickListener() {
	        	                        public void onClick(DialogInterface dialog, int id) {
	        	                            resendTask = new DialogBackgroud(4);
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
		        params.add(new BasicNameValuePair("page", Integer.toString(page)));

				MyArrList = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> map;
				
		        HttpRequest request = new HttpRequest("https://www.diyby.me/android-connect/get_user_order.php");
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
        					totalpage = result.getInt("totalpage");
        			        if(page == totalpage)
        			        {
        			        	btnForword.setText("");       
        			        }  
        			        else
        			        {
        			        	btnForword.setText("Page "+(page+1));
        			        }
        			        if(page == 1)
        			        {
        			        	btnBack.setText("");       
        			        }
        			        else
        			        {
        			        	btnBack.setText("Page "+(page-1));   
        			        }
        					JSONArray orders = result.getJSONArray("order");
        					for (int i = 0; i < orders.length(); i++) {
        						JSONObject order = orders.getJSONObject(i);
        						map = new HashMap<String, String>();
        						map.put("ID", order.getString("id"));
        				       	map.put("Amount", "�" + String.format("%.2f",order.getDouble("money")));
        				       	map.put("rawStatus", order.getString("status"));
        				       	map.put("URL", order.getString("url"));
        				       	if(order.getString("status").equals("0")){
        				       		//not paid
        				       		map.put("Status", "Not Paid");
        				       		map.put("Color", "#FFB45E");
        				       	}
        				       	else if(order.getString("status").equals("1")){	//not paid
        				       		map.put("Status", "Paid");
        				       		map.put("Color", "#47FF6C");
        				       	}
        						else{
        				       		map.put("Status", "Shipped");
        				       		map.put("Color", "#47D7FF");
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
		        
		        sAdap = new SpecialAdapter(OrderListActivity.this, MyArrList, R.layout.activity_orderlistcolumn,
		                new String[] {"ID", "Amount", "Status", "rawStatus", "URL", "Color"}, new int[] {R.id.ColOrderID, R.id.ColAmount, R.id.ColStatus, R.id.ColRaw, R.id.ColURL, R.id.ColColor});      
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
	public class DialogBackgroud extends AsyncTask<Void, Void, Boolean> {
		private Authenticate auth;
		private int mode;
		private JSONObject result;
		private String message;
		DialogBackgroud(int mode) {
			this.mode = mode;
			setVisible(orderProgressView, true);
			setVisible(orderID, false);
			setVisible(orderAmount, false);
			setVisible(orderStatus, false);
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
				if(mode == 1)//mark_as_paid mode
				{     
                	ArrayList<NameValuePair> updateParams = new ArrayList<NameValuePair>();
                	updateParams.add(new BasicNameValuePair("token", auth.getToken()));
                	updateParams.add(new BasicNameValuePair("mark_as_paid", "1"));
                	updateParams.add(new BasicNameValuePair("id", (String) orderID.getText()));
                	
                	HttpRequest request = new HttpRequest("https://www.diyby.me/android-connect/set_user_order.php");
    		    	result = request.get(updateParams);
				}
				else if(mode == 2)//mark_as_not_paid mode
				{    		        
                	ArrayList<NameValuePair> updateParams = new ArrayList<NameValuePair>();
                	updateParams.add(new BasicNameValuePair("token", auth.getToken()));
                	updateParams.add(new BasicNameValuePair("mark_as_not_paid", "1"));
                	updateParams.add(new BasicNameValuePair("id", (String) orderID.getText()));
                	
                	HttpRequest request = new HttpRequest("https://www.diyby.me/android-connect/set_user_order.php");
    		    	result = request.get(updateParams);
				}
				else if(mode == 3)//mark_as_shipped mode
				{   		        
                	ArrayList<NameValuePair> updateParams = new ArrayList<NameValuePair>();
                	updateParams.add(new BasicNameValuePair("token", auth.getToken()));
                	updateParams.add(new BasicNameValuePair("mark_as_shipped", "1"));
                	updateParams.add(new BasicNameValuePair("id", (String) orderID.getText()));
                	
                	HttpRequest request = new HttpRequest("https://www.diyby.me/android-connect/set_user_order.php");
    		    	result = request.get(updateParams);
				}
				else if(mode == 4)//delete mode
				{
                	ArrayList<NameValuePair> updateParams = new ArrayList<NameValuePair>();
                	updateParams.add(new BasicNameValuePair("token", auth.getToken()));
                	updateParams.add(new BasicNameValuePair("delete", "1"));
                	updateParams.add(new BasicNameValuePair("id", (String) orderID.getText()));
                	
                	HttpRequest request = new HttpRequest("https://www.diyby.me/android-connect/set_user_order.php");
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
			setVisible(orderProgressView, false);
			if(!success)
			{
				if(!auth.isConnect())
				{
					error.setText("No Internet Connection.\n       Click to refresh.");
					setVisible(error, true);
					setVisible(orderID, true);
					setVisible(orderAmount, true);
					setVisible(orderStatus, true);
				}
				else if(auth.isLogin())
				{
					Toast.makeText(getApplicationContext(), message, 7000).show();
					setVisible(orderID, true);
					setVisible(orderAmount, true);
					setVisible(orderStatus, true);
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
			else
			{
				if(mode >= 1 && mode < 4)
				{
					String[] status = {"Paid", "Not Paid", "Shipped"};
					String[] color = {"#47FF6C", "#FFB45E", "#47D7FF"};
					for (HashMap<String, String> map : MyArrList)
				    {
				        if(map.get("ID").equals(orderID.getText()))
				        {
				       		map.put("Status", status[mode-1]);
				       		map.put("Color", color[mode-1]);
				       		orderStatus.setText(status[mode-1]);
				       		rowview.setBackgroundColor(Color.parseColor(color[mode-1]));
				            break;
				        }
				        sAdap = new SpecialAdapter(OrderListActivity.this, MyArrList, R.layout.activity_orderlistcolumn,
				                new String[] {"ID", "Amount", "Status", "rawStatus", "URL", "Color"}, new int[] {R.id.ColOrderID, R.id.ColAmount, R.id.ColStatus, R.id.ColRaw, R.id.ColURL, R.id.ColColor});      
				        lisView1.setAdapter(sAdap);
				        sAdap.notifyDataSetChanged();
				        lisView1.invalidateViews();
				        lisView1.refreshDrawableState();
				    }
					setVisible(orderID, true);
					setVisible(orderAmount, true);
					setVisible(orderStatus, true);
				}
				else if(mode == 4)
				{
					MyArrList.remove(row);
	                sAdap.notifyDataSetChanged();
				}
				Toast.makeText(getApplicationContext(), message, 7000).show();
			}

		}

	}
}
