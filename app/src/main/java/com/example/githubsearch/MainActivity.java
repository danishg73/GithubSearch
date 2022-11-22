package com.example.githubsearch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String perpage = "5";
    private ListView listView;
    private List<Search_list> array_search_list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listview);

        SearchView simpleSearchView = (SearchView) findViewById(R.id.simpleSearchView); // inititate a search view
        CharSequence query = simpleSearchView.getQuery();
        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                getdata(query,perpage);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(MainActivity.this, newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
    public void getdata(String input, String page)
    {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.Github_link);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
//                        insertdata(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
//                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);

    }
    public void insertdata(String output)
    {
        try {
            JSONArray jsonObject = new JSONArray(output);
            for (int i=0; i<jsonObject.length(); i++)
            {
                Search_list searchList = new Search_list();
                JSONObject obj = jsonObject.getJSONObject(i);
                searchList.fullname = obj.getString("fullname").trim();
                array_search_list.add(searchList);
                searchList = null;
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        Searchlist_adapter adapter_main =  new Searchlist_adapter(array_search_list, getBaseContext(), MainActivity.this);
        Parcelable state = listView.onSaveInstanceState();
        adapter_main.notifyDataSetChanged();
        listView.setAdapter(adapter_main);
        listView.onRestoreInstanceState(state);
    }


}