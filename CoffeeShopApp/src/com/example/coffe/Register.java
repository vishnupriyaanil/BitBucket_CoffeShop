package com.example.coffe;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SdCardPath")
public class Register extends Activity{
	
	
	EditText ET_name, ET_email,password;
	ProgressDialog dialog = null;
	Button register;
	SessionManager session;
	TextView img;
	ConnectionDetector cd;
	AlertDialogManager alert = new AlertDialogManager();
	String selectedPath1 = "NONE";
	 private static final int SELECT_FILE1 = 1;
	String imgurl;
	HttpEntity resEntity;
	 ProgressDialog progressDialog;
	String name, email,dob, gender,chkmail;
	int serverResponseCode = 0;
	private static int RESULT_LOAD_IMAGE = 1;
	private DefaultHttpClient mHttpClient = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainregister);
		img=(TextView)findViewById(R.id.image);
		ET_name = (EditText)findViewById(R.id.et_registername);
		ET_email = (EditText)findViewById(R.id.et_registeremail);
		register = (Button)findViewById(R.id.btn_registerid);
		password=(EditText)findViewById(R.id.password);
		  cd = new ConnectionDetector(getApplicationContext());
		 session = new SessionManager(getApplicationContext());  
		ArrayAdapter<CharSequence> adapter = ArrayAdapter
				.createFromResource(this, R.array.gender_array,
						android.R.layout.simple_spinner_item);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	
		if (!cd.isConnectingToInternet()) {
		//	alert.showAlertDialog(Register.this, "Internet Connection Error",
					//"Please connect to working Internet connection", false);
			Log.e("Internet error","Internet Error");
			return;
		}
		
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				try
				{
					

				
					if (ET_name.getText().toString().equals("")) 
					{
					
						Toast.makeText(getApplicationContext(), "Please enter your Name", Toast.LENGTH_LONG).show();
					} 
					else if (ET_email.getText().toString().equals("")) 
					{
						Toast.makeText(getApplicationContext(), "Please enter your Email Id", Toast.LENGTH_LONG).show();
						
					}
					else if (password.getText().toString().equals("")&& password.getText().toString().length()<6) {
						Toast.makeText(getApplicationContext(), "password should be min 6 characters", Toast.LENGTH_LONG).show();
		
					} 
					else if((selectedPath1.trim().equalsIgnoreCase("NONE")))
					{
						Toast.makeText(getApplicationContext(), "Select the image", Toast.LENGTH_LONG).show(); 
					}
					else if(chkmail(ET_email.getText().toString()))
					{
						Toast.makeText(getApplicationContext(), "This mail id already registred", Toast.LENGTH_LONG).show(); 
					}
					else
					{
						progressDialog = ProgressDialog.show(Register.this, "", "Uploading files to server.....", false);
	                     Thread thread=new Thread(new Runnable(){
	                            public void run(){
	                               
	                                runOnUiThread(new Runnable(){
	                                    public void run() {
	                                    	if(progressDialog.isShowing())
	                                            progressDialog.dismiss();
	                                          //  session.createLoginSession(ET_name.getText().toString(),ET_email.getText().toString(),
	                                            	//	Constants.selectgender,ET_dob.getText().toString(),
	                                            	//	"http://www.nightskincream.com/pregnancy/uploads/"+selectedPath1);
	                                        Intent in=new Intent(Register.this,LoginActivity.class);
	                						startActivity(in);
	                						finish();  
	                                    }
	                                });
	                            }
	                    });
	                    thread.start();
	                  
						
						
					}
				}
				catch(Exception e)
				{
					Toast.makeText(getApplicationContext(), "Error in registration", Toast.LENGTH_LONG).show();
				}
				
						
			}
		});
		
		
		
	}
	
	
	public class MyOnItemSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View arg1, int pos,
				long arg3) {
			// TODO Auto-generated method stub
//			 Constants.selectgender = parent.getItemAtPosition(pos).toString();
//			 Log.i("UserLevelData",Constants.selectgender);

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	}
	
	
	public boolean eMailValidation(String emailstring) {
		Pattern emailPattern = Pattern
				.compile("^[\\w\\-]+(\\.[\\w\\-]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$");
		Matcher emailMatcher = emailPattern.matcher(emailstring);
		return emailMatcher.matches();
	}
	
	

/*	public void register() throws Exception {
		ArrayList<String> res_cat = new ArrayList<String>();
		try {
			InputStream is = null;

			String result = "";
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("uname",ET_name.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("mail",ET_email.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("pass",CryptWithMD5.cryptWithMD5(password.getText().toString())));
			nameValuePairs.add(new BasicNameValuePair("gender",Constants.selectgender));
			nameValuePairs.add(new BasicNameValuePair("dob",ET_dob.getText().toString()));
			HttpClient httpclient = new DefaultHttpClient();
     		HttpPost httppost = new HttpPost(
					"http://www.nightskincream.com/pregnancy/register.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			
		} catch (Exception e) {
			Toast.makeText(Register.this, "Error in Server", Toast.LENGTH_LONG)
					.show();
			Log.v("in server ", e.getMessage());
		}
		
	}*/
	
	  public boolean chkmail(String email) throws Exception {
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
			//for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(0);
			   chkmail=(json_data.getString("email"));
			   Toast.makeText(getApplicationContext(), chkmail, Toast.LENGTH_LONG).show();
			   if(Integer.parseInt(chkmail)==0)
				   return false;
			   else
				   return true;
			//}

		}
	  public void openGallery(int req_code){
		  
	        Intent intent = new Intent();
	        intent.setType("image/*");
	        intent.setAction(Intent.ACTION_GET_CONTENT);
	        startActivityForResult(Intent.createChooser(intent,"Select file to upload "), req_code);
	   }
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
		  
	        if (resultCode == RESULT_OK) {
	            Uri selectedImageUri = data.getData();
	            if (requestCode == SELECT_FILE1)
	            {
	                selectedPath1 = getPath(selectedImageUri);
	                System.out.println("selectedPath1 : " + selectedPath1);
	            }
	           
	            img.setText("Selected File paths : " + selectedPath1 );
	          /*  if(!(selectedPath1.trim().equalsIgnoreCase("NONE"))){
                    progressDialog = ProgressDialog.show(Register.this, "", "Uploading files to server.....", false);
                     Thread thread=new Thread(new Runnable(){
                            public void run(){
                                doFileUpload();
                                runOnUiThread(new Runnable(){
                                    public void run() {
                                        if(progressDialog.isShowing())
                                            progressDialog.dismiss();
                                    }
                                });
                            }
                    });
                    thread.start();
                }else{
                            Toast.makeText(getApplicationContext(),"Please select two files to upload.", Toast.LENGTH_SHORT).show();
                }*/
	        }
	    }
	  public String getPath(Uri uri) {
	        String[] projection = { MediaStore.Images.Media.DATA };
	        Cursor cursor = managedQuery(uri, projection, null, null, null);
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        cursor.moveToFirst();
	        return cursor.getString(column_index);
	    }
	 
		 public void onBackPressed() {
		    	// TODO Auto-generated method stub
		    	 Intent i = new Intent(Register.this,LoginActivity.class);
		         startActivity(i);
		    	super.onBackPressed();
		    }
	    
		

}
