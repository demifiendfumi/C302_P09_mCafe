package sg.edu.rp.webservices.c302_p09_mcafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText etLoginID, etPassword;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etLoginID = (EditText)findViewById(R.id.editTextLoginID);
        etPassword = (EditText)findViewById(R.id.editTextPassword);
        btnSubmit = (Button)findViewById(R.id.buttonSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etLoginID.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (username.equalsIgnoreCase("")) {
                    Toast.makeText(LoginActivity.this, "Login failed. Please enter username.", Toast.LENGTH_LONG).show();

                } else if (password.equalsIgnoreCase("")) {
                    Toast.makeText(LoginActivity.this, "Login failed. Please enter password.", Toast.LENGTH_LONG).show();

                }else{
                    HttpRequest request = new HttpRequest
                            ("http://10.0.2.2/C302_P09mCafe/doLogin.php");
                    Log.d("url", "http://10.0.2.2/C302_P09mCafe/doLogin.php");
                    request.setOnHttpResponseListener(mHttpResponseListener);
                    request.setMethod("POST");
                    request.addData("username", username);
                    request.addData("password", password);
                    request.execute();
                }
            }
        });
    }

    private HttpRequest.OnHttpResponseListener mHttpResponseListener =
            new HttpRequest.OnHttpResponseListener() {
                @Override
                public void onResponse(String response){
                    // process response here
                    Log.i("JSON Results: ", response);
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        if(jsonObj.getBoolean("authenticated") == true){
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("loginID", jsonObj.getString("id"));
                            editor.putString("apiKey", jsonObj.getString("apikey"));
                            editor.commit();

                            startActivity(i);

                        }else{
                            Toast.makeText(LoginActivity.this, "Your username/password is invalid", Toast.LENGTH_SHORT).show();
                        }

                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }

                }
            };
}
