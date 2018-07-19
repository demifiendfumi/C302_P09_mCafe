package sg.edu.rp.webservices.c302_p09_mcafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class AddMenuItem extends AppCompatActivity {
    EditText etName, etPrice;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);
        etName = findViewById(R.id.etItem);
        etPrice = findViewById(R.id. etPrice);
        btnAdd = findViewById(R.id. btnAdd);
        Intent i = getIntent();
        final String categoryId = i.getStringExtra("categoryId");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String loginID = prefs.getString("loginID", "");
        final String apiKey = prefs.getString("apiKey", "");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpRequest request = new HttpRequest
                        ("http://10.0.2.2/C302_P09mCafe/addMenuItem.php");
                request.setOnHttpResponseListener(mHttpResponseListener);
                request.setMethod("POST");
                request.addData("loginId", loginID);
                request.addData("apikey", apiKey);
                request.addData("categoryId", categoryId);
                request.addData("description", etName.getText().toString());
                request.addData("price", etPrice.getText().toString());
                request.execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Step 2:
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.AddMenu) {
            Toast.makeText(AddMenuItem.this, "You are now Add Item Page", Toast.LENGTH_SHORT).show();
            return true;
        }else if(id==R.id.LogOut){
            Intent i = new Intent(AddMenuItem.this, LoginActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private HttpRequest.OnHttpResponseListener mHttpResponseListener =
            new HttpRequest.OnHttpResponseListener() {
                @Override
                public void onResponse(String response){

                    // process response here
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        if(jsonObj.getBoolean("success") == true){
                            Toast.makeText(AddMenuItem.this, "Menu Item added successful", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(AddMenuItem.this, "Menu Item added unsuccessful", Toast.LENGTH_SHORT).show();
                        }

                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            };
}
