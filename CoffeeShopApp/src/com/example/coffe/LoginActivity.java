package com.example.coffe;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.coffeshop.facebook.AsyncFacebookRunner;
import com.coffeshop.facebook.AsyncFacebookRunner.RequestListener;
import com.coffeshop.facebook.DialogError;
import com.coffeshop.facebook.Facebook;
import com.coffeshop.facebook.Facebook.DialogListener;
import com.coffeshop.facebook.FacebookError;
import com.coffeshop.facebook.SessionStore;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class LoginActivity extends Activity {
	
	EditText txtUsername, txtPassword;
	private Facebook mFacebook;
	
	ImageButton facebk,btnLogin,register;
	private static final String[] PERMISSIONS = new String[] {"publish_stream","email","user_groups","user_birthday","read_stream","user_about_me","offline_access"};
	private static final String APP_ID = "123769667806194";
	ProgressDialog progressDialog;
	ConnectionDetector cd;
	AlertDialogManager alert = new AlertDialogManager();
	private SharedPreferences mPrefs;
	ProgressDialog dialog;
	private AsyncFacebookRunner mAsyncRunner;
	String s_name, s_gender, s_email, s_birthday,s_img,s_id,s_username,s_nickname;
	SessionManager session;
	String g_name,g_mail,g_gender,g_dob,g_img;
	String username;
	String password ;
	int chklogin;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); 
       
        session = new SessionManager(getApplicationContext());                
        
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword); 
        register=(ImageButton)findViewById(R.id.register);
        facebk=(ImageButton)findViewById(R.id.facebook);
        mFacebook		= new Facebook(APP_ID);
       
        btnLogin = (ImageButton) findViewById(R.id.btnLogin);
        mAsyncRunner = new AsyncFacebookRunner(mFacebook);
        
        SessionStore.clear(LoginActivity.this);
        cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			
			Log.e("Internet error","Internet Error");
			return;
		}
		
        mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);

		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putString("access_token", mFacebook.getAccessToken());
		editor.putLong("access_expires",
				mFacebook.getAccessExpires());
		editor.commit();
		
		
		
        btnLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 username = txtUsername.getText().toString();
				 password = txtPassword.getText().toString();
				
				// Check if username, password is filled				
				if(username.trim().length() > 0 && password.trim().length() > 0){
				
						new GetOperation().execute();
				}else{
						alert.showAlertDialog(LoginActivity.this, "Login failed..", "Please enter username and password", false);
				}
				
			}
		});
        
        //register button
        register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in=new Intent(LoginActivity.this,Register.class);
				startActivity(in);
				finish();
			}
		});
        //facebook
        facebk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onFacebookClick();
			}
		});
        
    }   


    public boolean chklog(String mail, String passwd) throws Exception {
    	String chk="";
		InputStream is = null;
		String result = "";
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs
				.add(new BasicNameValuePair("mail", mail.toString().trim()));
		//nameValuePairs
			//	.add(new BasicNameValuePair("pass",CryptWithMD5.cryptWithMD5(passwd.toString().trim())));
		nameValuePairs
			.add(new BasicNameValuePair("pass",(passwd.toString().trim())));
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(
				"http://www.nightskincream.com/pregnancy/chklog.php");
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		is = entity.getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
				"iso-8859-1"), 8);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		is.close();
		result = sb.toString();

		JSONArray jArray = new JSONArray(result);
		
			JSONObject json_data = jArray.getJSONObject(0);
			chk=(json_data.getString("email"));
			if(Integer.parseInt(chk)==0)
				return false;
			else
			return true;
	}
    //facebook
    public  void onFacebookClick() {
		if (!mFacebook.isSessionValid()) 
		{
			mFacebook.authorize(this, PERMISSIONS, -1, new FbLoginDialogListener());
		} 
	}
    private final class FbLoginDialogListener implements DialogListener {
        public void onComplete(Bundle values) {
           // SessionStore.save(mFacebook, LoginActivity.this);
        	//mFacebook.authorize(this, PERMISSIONS,-1,new LoginListener());	 
         getFbName();
        }

        public void onFacebookError(FacebookError error) {
           Toast.makeText(LoginActivity.this, "Facebook connection failed", Toast.LENGTH_SHORT).show();
           
           
        }
        
        public void onError(DialogError error) {
        	Toast.makeText(LoginActivity.this, "Facebook connection failed", Toast.LENGTH_SHORT).show(); 
        	
        	
        }

        public void onCancel() {
        	
        }

		
    }
    private void getFbName() {
		//mProgress.setMessage("Finalizing ...");
		//mProgress.show();
		
		new Thread() {
			@Override
			public void run() {
		        String name = "";
		        int what = 1;
		        
		        try {
		        	String me = mFacebook.request("me");
		        	
		        	JSONObject jsonObj = (JSONObject) new JSONTokener(me).nextValue();
		        	name = jsonObj.getString("name");
		        	  	what = 0;
		        } catch (Exception ex) {
		        	ex.printStackTrace();
		        }
		        
		        mFbHandler.sendMessage(mFbHandler.obtainMessage(what, name));
			}
		}.start();
	}
	private Handler mFbHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//mProgress.dismiss();
			
			if (msg.what == 0) {
				String username = (String) msg.obj;
		        username = (username.equals("")) ? "No Name" : username;
		            
		        SessionStore.saveName(username, LoginActivity.this);
		              
		        Toast.makeText(LoginActivity.this, "Connected to Facebook as " + username, Toast.LENGTH_SHORT).show();
		         try {
		        	progressDialog = ProgressDialog.show(LoginActivity.this, "", "Uploading files to server.....", false);
		        	
                    Thread thread=new Thread(new Runnable(){
                           public void run(){
                        	   getProfileInformation();
                        	   
                               runOnUiThread(new Runnable(){
                                   public void run() {
                                       if(progressDialog.isShowing())
                                           progressDialog.dismiss();
                                          
	                                        Intent in=new Intent(LoginActivity.this,MainActivity.class);
	                						startActivity(in);
	                						finish();
                                   }
                               });
                           }
                   });
                   thread.start();
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		       
		       
			} 
			else {
				Toast.makeText(LoginActivity.this, "Connected to Facebook", Toast.LENGTH_SHORT).show();
				 try {
			        	progressDialog = ProgressDialog.show(LoginActivity.this, "", "Uploading files to server.....", false);
			        	
	                    Thread thread=new Thread(new Runnable(){
	                           public void run(){
	                        	   getProfileInformation();
	                        	   
	                               runOnUiThread(new Runnable(){
	                                   public void run() {
	                                       if(progressDialog.isShowing())
	                                           progressDialog.dismiss();
	                                          
		                                        Intent in=new Intent(LoginActivity.this,MainActivity.class);
		                						startActivity(in);
		                						finish();
	                                   }
	                               });
	                           }
	                   });
	                   thread.start();
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
	};
	public void getProfileInformation() {
		mAsyncRunner.request("me", new RequestListener() {
		
			public void onComplete(String response) {
				// TODO Auto-generated method stub
				Log.d("Profile", response);
				
			    	
				
				String json = response;
				try {
					JSONObject profile = new JSONObject(json);
				
					if (null != profile.getString("name"))
						;
					{
						final String name = profile.getString("name");
						s_name = name;
						Log.d("s_name", s_name);

					}

					if (null != profile.getString("email"))
						;
					{
						final String email = profile.getString("email");
						s_email = email;
						Log.d("s_email", s_email);

					}

					if (null != profile.getString("birthday"))
						;
					{
						final String birthday = profile.getString("birthday");

						s_birthday = birthday;
						Log.d("s_birthday", s_birthday);
				
						Log.d("after", s_birthday);

					}

					if (null != profile.getString("gender"))
						;
					{
						final String gender = profile.getString("gender");
						s_gender = gender;
						Log.d("s_gender", s_gender);
					}
					
					
					// nick name
					if (null != profile.getString("nickname"))
						;
					{
						final String nickname = profile.getString("nickname");
						s_nickname = nickname;
						Log.d("s_Nick Name", s_nickname);
					}
					
					if (null != profile.getString("id"))
						;
					{
						final String id = profile.getString("id");
						s_id = id;
						Log.d("id", s_id);
					}
					 String userName=profile.getString("username");
					try {
					         String pro_image="http://graph.facebook.com/"+s_id+"/picture?type=small" ;
						 session.createLoginSession(s_name,s_email,s_gender,s_birthday,pro_image);
						register(s_name,s_email,s_gender,s_birthday);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (JSONException e) {
					e.printStackTrace();

				}
			
			}

			public void onIOException(IOException e) {
				// TODO Auto-generated method stub
				
			}

			public void onFileNotFoundException(FileNotFoundException e) {
				// TODO Auto-generated method stub
				
			}

			public void onMalformedURLException(MalformedURLException e) {
				// TODO Auto-generated method stub
				
			}

			public void onFacebookError(FacebookError e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	public void register(String name,String mail,String gender,String dob) throws Exception {
		ArrayList<String> res_cat = new ArrayList<String>();
		try {
			InputStream is = null;

			String result = "";
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("name",name));
			nameValuePairs.add(new BasicNameValuePair("mail",mail));
			nameValuePairs.add(new BasicNameValuePair("gender",gender));
			nameValuePairs.add(new BasicNameValuePair("dob",dob));
			HttpClient httpclient = new DefaultHttpClient();
     		HttpPost httppost = new HttpPost(
					"");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			
		} catch (Exception e) {
			Toast.makeText(LoginActivity.this, "Error in Server", Toast.LENGTH_LONG)
					.show();
			Log.v("in server ", e.getMessage());
		}
		
	}
	//get details
	
	 public void getdetails(String email) throws Exception {
		  	InputStream is = null;
			String result = "";
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs
					.add(new BasicNameValuePair("mail", email.toString().trim()));
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(
					"");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,
					"iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			  JSONArray jArray = new JSONArray(result);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
			   g_name=(json_data.getString("name"));
			   g_mail=json_data.getString("email");
			   g_gender=json_data.getString("gender");
			   g_dob=json_data.getString("dob");
			   g_img=json_data.getString("img_name");
			  
			}

		}
	 private class GetOperation extends AsyncTask<String, Void, String> {

			@Override
			protected String doInBackground(String... params) {

				try {
					chklogin=0;
					if(chklog(username, password)){
					
					getdetails(username);
					session.createLoginSession(g_name,g_mail,g_gender,g_dob,""+g_img);
						chklogin=1;			
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				

				return "";

			}

			@Override
			protected void onPreExecute() {

				showProgressdialog();
			}

			@Override
			protected void onPostExecute(String result) {

				progressDialog.cancel();
				if(chklogin==1)
				{
					Intent i = new Intent(LoginActivity.this,MainActivity.class);
					startActivity(i);			
					finish();
				}
				else{
					
					alert.showAlertDialog(LoginActivity.this, "Login failed..", "Username/Password is incorrect", false);
					}
				
			}
			public void showProgressdialog() {

				progressDialog = ProgressDialog.show(LoginActivity.this, "",
						"login...");
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.setCancelable(true);
			}


		
		}
	 
	 
	 
	 @Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
		 Intent intent = new Intent(Intent.ACTION_MAIN);
		  intent.addCategory(Intent.CATEGORY_HOME);
		  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		  startActivity(intent);   
//		  session.logoutUser();
		    finish();
			super.onBackPressed();
		}

	 
	
}