package sg.edu.rp.webservices.c302_p09_mcafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuItemsByCategoryActivity extends AppCompatActivity {

    private ListView lvMenu;
    private ArrayList<Menu> alMenu;
    private ArrayAdapter<Menu> aaMenu;
    String categoryId, menuDescription, unitPrice, menuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_items_by_category);
//        lvMenu = findViewById(R.id.listViewMenu);
//        alMenu = new ArrayList<Menu>();
//        aaMenu = new ArrayAdapter<Menu>(this, android.R.layout.simple_list_item_1, alMenu);
//        aaMenu.clear();
//        lvMenu.setAdapter(aaMenu);
//        Intent i = getIntent();
//        categoryId = i.getStringExtra("category_id");
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        String loginID = prefs.getString("loginID", "");
//        String apiKey = prefs.getString("apiKey", "");
//
//        if (loginID.equalsIgnoreCase("") || apiKey.equalsIgnoreCase("")) {
//            // redirect back to login screen
//            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            startActivity(intent);
//        }else{
//            HttpRequest request = new HttpRequest
//                    ("http://10.0.2.2/C302_P09mCafe/getMenuItemsByCategories.php?categoryId=" + categoryId);
//            request.setOnHttpResponseListener(mHttpResponseListener);
//            request.setMethod("POST");
//            request.addData("loginId", loginID);
//            request.addData("apikey", apiKey);
//            request.execute();
//        }
//
//        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                String categoryId = alCategories.get(position).getCategoryId();
////                Intent intent = new Intent(MainActivity.this, MenuItemsByCategoryActivity.class);
////                intent.putExtra("loginID", loginId);
////                intent.putExtra("apiKey", apikey);
////                intent.putExtra("category_id", categoryId);
////                startActivity(intent);
//
//            }
//        });
    }

    protected void onResume(){
        super.onResume();
        lvMenu = findViewById(R.id.listViewMenu);
        alMenu = new ArrayList<Menu>();
        aaMenu = new ArrayAdapter<Menu>(this, android.R.layout.simple_list_item_1, alMenu);
        aaMenu.clear();
        lvMenu.setAdapter(aaMenu);
        Intent i = getIntent();
        categoryId = i.getStringExtra("category_id");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String loginID = prefs.getString("loginID", "");
        String apiKey = prefs.getString("apiKey", "");

        if (loginID.equalsIgnoreCase("") || apiKey.equalsIgnoreCase("")) {
            // redirect back to login screen
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }else{
            HttpRequest request = new HttpRequest
                    ("http://10.0.2.2/C302_P09mCafe/getMenuItemsByCategories.php?categoryId=" + categoryId);
            request.setOnHttpResponseListener(mHttpResponseListener);
            request.setMethod("POST");
            request.addData("loginId", loginID);
            request.addData("apikey", apiKey);
            request.execute();
        }

        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String menuItemId = alMenu.get(position).getMenuId();
                String menuItemDescription = alMenu.get(position).getDescription();
                String price = alMenu.get(position).getUnit_price();
                Intent intent = new Intent(MenuItemsByCategoryActivity.this, EditDeleteActivity.class);
                intent.putExtra("menu_item_id", menuItemId);
                intent.putExtra("category_id", categoryId);
                intent.putExtra("menu_item_description", menuItemDescription);
                intent.putExtra("menu_item_unit_price", price);
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
            Intent i = new Intent(MenuItemsByCategoryActivity.this, AddMenuItem.class);
            i.putExtra("categoryId", categoryId);
            startActivity(i);
            return true;
        }else if(id==R.id.LogOut){
            Intent i = new Intent(MenuItemsByCategoryActivity.this, LoginActivity.class);
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
                            menuId = jsonObj.getString("menu_item_id");
                            menuDescription = jsonObj.getString("menu_item_description");
                            unitPrice = jsonObj.getString("menu_item_unit_price");
                            Menu menu = new Menu(menuId, menuDescription, unitPrice);
                            alMenu.add(menu);
                        }

                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    aaMenu.notifyDataSetChanged();
                }
            };
}
