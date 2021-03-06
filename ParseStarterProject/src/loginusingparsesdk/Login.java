package loginusingparsesdk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.starter.R;

import java.util.Locale;

import trial.Convo;
import trial.Listing;
import trial.MainActivity;
import trial.Message;

public class Login extends Activity {
	Button btn_LoginIn = null;
	Button btn_SignUp = null;
	Button btn_ForgetPass = null;
	private EditText mUserNameEditText;
	private EditText mPasswordEditText;
	boolean isInitialized;


	// flag for Internet connection status
	Boolean isInternetPresent = false;
	// Connection detector class
	ConnectionDetector cd;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		//Initializing Parse SDK
//		onCreateParse();
		//Calling ParseAnalytics to see Analytics of our app
//		ParseAnalytics.trackAppOpened(getIntent());
		// creating connection detector class instance
		cd = new ConnectionDetector(getApplicationContext());
		Intent in = getIntent();
		isInitialized = in.getExtras().getBoolean("isInitialized");


		btn_LoginIn = (Button) findViewById(R.id.btn_login);
//		btn_SignUp = (Button) findViewById(R.id.btn_signup);
		btn_ForgetPass = (Button) findViewById(R.id.btn_ForgetPass);
		mUserNameEditText = (EditText) findViewById(R.id.username);
		mPasswordEditText = (EditText) findViewById(R.id.password);
		mPasswordEditText.setText("");

		btn_LoginIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// get Internet status
				isInternetPresent = cd.isConnectingToInternet();
				// check for Internet status
				if (isInternetPresent) {
					// Internet Connection is Present
					// make HTTP requests
					attemptLogin();
				} else {
					// Internet connection is not present
					// Ask user to connect to Internet
					showAlertDialog(Login.this, "No Internet Connection",
							"You don't have internet connection.", false);
				}

			}
		});

//		btn_SignUp.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent in =  new Intent(Login.this, SignUp.class);
//				startActivity(in);
//			}
//		});
		
		btn_ForgetPass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in =  new Intent(Login.this,ForgetParsePassword.class);
				startActivity(in);
			}
		});

	}

//	public void onCreateParse() {
//		ParseObject.registerSubclass(Listing.class);
//        ParseObject.registerSubclass(Message.class);
//		ParseObject.registerSubclass(Convo.class);
//		Parse.enableLocalDatastore(this);
//		Parse.initialize(this, "OHN1vjNjDDN5L46ztns0EGB7ApSq1rXi3RrHksN8", "w1xlxox48cSzRWflCfuzR5Oa6gG5qU6qlKAhA03z");
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_forgot_password:
			forgotPassword();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void forgotPassword(){
		/* 
		FragmentManager fm = getSupportFragmentManager();
	     ForgotPasswordDialogFragment forgotPasswordDialog = new ForgotPasswordDialogFragment();
	     forgotPasswordDialog.show(fm, null);
		 */
	}

	public void attemptLogin() {

		clearErrors();

		// Store values at the time of the login attempt.
		String username = mUserNameEditText.getText().toString();
		String password = mPasswordEditText.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(password)) {
			mPasswordEditText.setError(getString(R.string.error_field_required));
			focusView = mPasswordEditText;
			cancel = true;
		} else if (password.length() < 4) {
			mPasswordEditText.setError(getString(R.string.error_invalid_password));
			focusView =mPasswordEditText;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(username)) {
			mUserNameEditText.setError(getString(R.string.error_field_required));
			focusView = mUserNameEditText;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// perform the user login attempt.
			login(username.toLowerCase(Locale.getDefault()), password);
		}
	}

	private void login(String lowerCase, String password) {
		// TODO Auto-generated method stub
		ParseUser.logInInBackground(lowerCase, password, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException e) {
				// TODO Auto-generated method stub
				if(e == null)
					loginSuccessful();
				else
					loginUnSuccessful();
			}
		});

	}

	protected void loginSuccessful() {
		// TODO Auto-generated method stub
		Intent in =  new Intent(Login.this, MainActivity.class);
		in.putExtra("isInitialized",isInitialized);
		startActivity(in);
	}
	protected void loginUnSuccessful() {
		// TODO Auto-generated method stub
		showAlertDialog(Login.this,"Login", "Username or Password is invalid.", false);
	}

	private void clearErrors(){
		mUserNameEditText.setError(null);
		mPasswordEditText.setError(null);
	}

	@SuppressWarnings("deprecation")
	public void showAlertDialog(Context context, String title, String message, Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		// Setting alert dialog icon
		alertDialog.setIcon(R.drawable.fail);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}


    @Override
    public void onResume() {
        super.onResume();
        mPasswordEditText.setText("");
    }

}
