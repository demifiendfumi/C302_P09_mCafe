package sg.edu.rp.webservices.c302_p09_mcafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ListView lvCategories;
    private ArrayList<MenuCategory> alCategories;
    private ArrayAdapter<MenuCategory> aaCategories;
    String menuId;
    String description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvCategories = (ListView)findViewById(R.id.listViewCategories);
        alCategories = new ArrayList<MenuCategory>();
        aaCategories = new ArrayAdapter<MenuCategory>(this, android.R.layout.simple_list_item_1, alCategories);
        lvCategories.setAdapter(aaCategories);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String loginID = prefs.getString("loginID", "");
        String apiKey = prefs.getString("apiKey", "");

        if (loginID.equalsIgnoreCase("") || apiKey.equalsIgnoreCase("")) {
            // redirect back to login screen
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }else{
            HttpRequest request = new HttpRequest
                    ("http://10.0.2.2/C302_P09mCafe/getMenuCategories.php");
            request.setOnHttpResponseListener(mHttpResponseListener);
            request.setMethod("POST");
            request.addData("loginId", loginID);
            request.addData("apikey", apiKey);
            request.execute();
        }

        lvCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String categoryId = alCategories.get(position).getCategoryId();
                Intent intent = new Intent(MainActivity.this, MenuItemsByCategoryActivity.class);
                intent.putExtra("category_id", categoryId);
                startActivity(intent);

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
            Intent i = new Intent(MainActivity.this, AddMenuItem.class);
            startActivity(i);
            return true;
        }else if(id==R.id.LogOut){
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
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
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject jsonObj = jsonArray.getJSONObject(i);

                            menuId = jsonObj.getString("menu_item_category_id");
                            description = jsonObj.getString("menu_item_category_description");
                            MenuCategory menuCategory = new MenuCategory(menuId, description);
                            alCategories.add(menuCategory);
                        }

                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    aaCategories.notifyDataSetChanged();
                }
            };
}
