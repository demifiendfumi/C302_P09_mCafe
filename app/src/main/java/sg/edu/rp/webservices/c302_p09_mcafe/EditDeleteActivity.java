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

public class EditDeleteActivity extends AppCompatActivity {
    EditText etItemName, etUnitPrice;
    Button btnUpdate, btnDelete;
    String menuID, menuDesc, menuPrice, category_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete);

        etItemName = findViewById(R.id.etItemName);
        etUnitPrice = findViewById(R.id.etUnitPrice);
        btnUpdate = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        Intent i = getIntent();
        menuID = i.getStringExtra("menu_item_id");
        menuDesc = i.getStringExtra("menu_item_description");
        menuPrice = i.getStringExtra("menu_item_unit_price");
        category_id = i.getStringExtra("category_id");
        etItemName.setText(menuDesc);
        etUnitPrice.setText(menuPrice);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String loginID = prefs.getString("loginID", "");
        final String apiKey = prefs.getString("apiKey", "");

        if (loginID.equalsIgnoreCase("") || apiKey.equalsIgnoreCase("")) {
            // redirect back to login screen
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }


            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HttpRequest request = new HttpRequest
                            ("http://10.0.2.2/C302_P09mCafe/updateMenuItemById.php");
                    request.setOnHttpResponseListener(mHttpResponseListener);
                    request.setMethod("POST");
                    request.addData("loginId", loginID);
                    request.addData("apikey", apiKey);
                    request.addData("id", menuID);
                    request.addData("description", etItemName.getText().toString());
                    request.addData("unit_price", etUnitPrice.getText().toString());
                    request.execute();
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HttpRequest request = new HttpRequest
                            ("http://10.0.2.2/C302_P09mCafe/deleteMenuItemById.php");
                    request.setOnHttpResponseListener(mHttpResponseListener);
                    request.setMethod("POST");
                    request.addData("loginId", loginID);
                    request.addData("apikey", apiKey);
                    request.addData("id", menuID);
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
            Intent i = new Intent(EditDeleteActivity.this, AddMenuItem.class);
            i.putExtra("categoryId", category_id);
            startActivity(i);
            return true;
        }else if(id==R.id.LogOut){
            Intent i = new Intent(EditDeleteActivity.this, LoginActivity.class);
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
                        Toast.makeText(EditDeleteActivity.this, jsonObj.getString("message"), Toast.LENGTH_SHORT);
                        finish();

                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            };
}
